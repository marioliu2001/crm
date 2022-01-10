package com.bjpowernode.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description TODO
 * @Date 2022/1/10 17:31
 * @Version 1.0
 */
@Controller
@RequestMapping("/settings/user")
public class UserController {
    /**
     * 跳转到登陆页面
     * @return
     */
    @RequestMapping("/toLogin.do")
    public String toLogin(){
        //拼接试图解析器的前缀+返回值+后缀名称，找到响应的jsp
        // 前缀 = /WEB-INF/jsp
        //返回值 = /login
        //后缀 = .jsp
        return "/login";
    }
}
