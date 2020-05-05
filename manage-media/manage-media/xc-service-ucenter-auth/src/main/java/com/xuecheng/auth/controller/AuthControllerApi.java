package com.xuecheng.auth.controller;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * 登录接口
 */
public interface AuthControllerApi {
    public LoginResult login(LoginRequest loginRequest);


    public ResponseResult logout();

    public JwtResult userjwt();
}
