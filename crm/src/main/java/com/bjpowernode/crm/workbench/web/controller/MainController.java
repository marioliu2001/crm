package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description TODO
 * @Date 2022/1/14 16:01
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/workbench/main")
public class MainController {

    /**
     * 加载首页面内容区域
     * @return
     */
    @RequestMapping(value = "/toIndex.do")
    public String toIndex(){
        return "/workbench/main/index";
    }
}
