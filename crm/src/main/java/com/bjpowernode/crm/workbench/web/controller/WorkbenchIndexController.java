package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description TODO
 * @Date 2022/1/14 15:42
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/workbench")
public class WorkbenchIndexController {

    /**
     * 加载主页面
     * @return
     */
    @RequestMapping(value = "/toIndex.do")
    public String toIndex(){
        return "/workbench/index";
    }
}
