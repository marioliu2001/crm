package com.bjpowernode.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description TODO
 * @Date 2022/1/14 23:39
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/settings")
public class SettingsIndexController {

    /**
     * 跳转系统设置主页面
     * @return
     */
    @RequestMapping(value = "/toIndex.do")
    public String toIndex() {
        return "/settings/index";
    }
}
