package com.xuecheng.manage_media.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi{

    @Autowired
    private MediaFileService mediaFileService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            QueryMediaFileRequest queryMediaFileRequest) {
        return mediaFileService.findList(page,size,queryMediaFileRequest);
    }


    @Autowired
    WebApplicationContext applicationContext;

    @GetMapping("/getAllUrl2")
    @Override
    public String getAllUrl(String a) {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        JSONArray jsonArray = null;
        try{
            Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
                Map<String, String> map1 = new HashMap<String, String>();
                RequestMappingInfo info = m.getKey();
                HandlerMethod method = m.getValue();
                PatternsRequestCondition p = info.getPatternsCondition();
                for (String url : p.getPatterns()) {
                    map1.put("url", url);
                }
                map1.put("className", method.getMethod().getDeclaringClass().getName()); // 类名
                map1.put("method", method.getMethod().getName()); // 方法名
                RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
                for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                    map1.put("type", requestMethod.toString());
                }

                list.add(map1);
            }

            jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonArray.toJSONString();
    }

}
