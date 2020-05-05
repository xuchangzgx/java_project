package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    // token过期时间
    @Value("${auth.tokenValiditySeconds}")
    private int tokenValiditySeconds;

    private static final String TOKEN_PRE = "token:";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 删除token
     * @param accessToken
     * @return
     */
    public boolean delToken(String accessToken){
        stringRedisTemplate.delete(TOKEN_PRE+accessToken);
        return false;
    }

    /**
     * 获得token
     * @param token
     * @return
     */
    public AuthToken getUserToken(String token){
        token = TOKEN_PRE+token;
        String userTokenStr = stringRedisTemplate.opsForValue().get(token);
        if(userTokenStr!=null){
            AuthToken authToken = null;
            try{
                authToken = JSON.parseObject(userTokenStr,AuthToken.class);
            }catch (Exception e){
                LOGGER.error(
                        "getUserToken from redis and execute JSON.parseObject error{}",
                        e.getMessage());
                e.printStackTrace();
            }

            return authToken;
        }

        return  null;
    }

    /**
     *
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    public AuthToken login(String username,String password,String clientId,String clientSecret){
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        if(authToken==null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        // 将token存储到redis中
        String access_token = authToken.getAccess_token();
        String content = JSON.toJSONString(authToken);
        boolean saveTokenResult = saveAuthToken(access_token, content, tokenValiditySeconds);
        if(!saveTokenResult){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }

        return authToken;
    }

    public boolean saveAuthToken(String key,String value,long ttl) {
        key = TOKEN_PRE+key;
        // 保存令牌
        stringRedisTemplate.boundValueOps(key).set(value, ttl, TimeUnit.SECONDS);
        //读取过期时间，已过期返回‐2
        Long expire = stringRedisTemplate.getExpire(key);

        return expire>0;
    }

    public AuthToken applyToken(String username,String password,String clientId,String clientSecret){
        //从eureka中获取认证服务的地址（因为spring security在认证服务中）
        //从eureka中获取认证服务的一个实例的地址
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        if (serviceInstance == null) {
            LOGGER.error("choose an auth instance fail");
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_AUTHSERVER_NOTFOUND);
        }
        //此地址就是http://ip:port
        URI uri = serviceInstance.getUri();
        //令牌申请的地址 http://localhost:40400/auth/oauth/token
        String authUrl = uri+ "/auth/oauth/token";
        //定义header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add("Authorization",httpBasic);

        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        // 授权方式
        body.add("grant_type","password");
        // 账号
        body.add("username",username);
        // 密码
        body.add("password",password);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);
        //String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables

        //设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });
        Map bodyMap = null;

        try{
            ResponseEntity<Map> exchange = restTemplate.exchange(
                    authUrl, HttpMethod.POST, httpEntity, Map.class);
            //申请令牌信息
            bodyMap= exchange.getBody();
        }catch (RestClientException e){
            e.printStackTrace();
            LOGGER.error("request oauth_token_password error: {}",e.getMessage());
            e.printStackTrace();
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        // 登录失败
        if(
                bodyMap==null||bodyMap.get("access_token")==null
                        ||bodyMap.get("refresh_token")==null
                        ||bodyMap.get("jti")==null // jti是jwt令牌的唯一标，可以作为用户身份令牌
            ){
            String error_description = (String) bodyMap.get("error_description");
            if(StringUtils.isNotEmpty(error_description)){
                if(error_description.equals("坏的凭证")){
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }else if(error_description.indexOf("UserDetailsService returned null")>=0){
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }
            }

            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }

        AuthToken authToken = new AuthToken();
        //访问令牌(jwt)
        String jwt_token = (String) bodyMap.get("access_token");
        //刷新令牌(jwt)
        String refresh_token = (String) bodyMap.get("refresh_token");
        //jti，作为用户的身份标识
        String access_token = (String) bodyMap.get("jti");
        authToken.setJwt_token(jwt_token);
        authToken.setAccess_token(access_token);
        authToken.setRefresh_token(refresh_token);
        return authToken;
    }

    //获取httpbasic的串
    private String getHttpBasic(String clientId,String clientSecret){
        String string = clientId+":"+clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }

}
