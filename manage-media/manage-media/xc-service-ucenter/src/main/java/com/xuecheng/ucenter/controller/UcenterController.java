package com.xuecheng.ucenter.controller;


import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@RequestMapping("/ucenter")
public class UcenterController {
    @Autowired
    UserService userService;

    @GetMapping("/getuserext")
    public XcUserExt getUserext(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }

    @GetMapping("/testPermissions")
    @PreAuthorize("hasAuthority('course_find_pic')")
    public XcUserExt testPermissions(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }

    @GetMapping("/testUnPermissions")
    @PreAuthorize("hasAuthority('course_get_un_baseinfo')")
    public XcUserExt testUnPermissions(@RequestParam("username") String username) {
        return userService.getUserExt(username);
    }
}
