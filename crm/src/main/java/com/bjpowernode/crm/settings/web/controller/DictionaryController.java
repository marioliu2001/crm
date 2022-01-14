package com.bjpowernode.crm.settings.web.controller;

import javafx.scene.chart.ValueAxis;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description TODO
 * @Date 2022/1/14 23:48
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/settings/dictionary")
public class DictionaryController {

    /**
     * 跳转数据字典表主模块
     * @return
     */
    @RequestMapping(value = "/toIndex.do")
    public String toIndex(){
        return "/settings/dictionary/index";
    }

    /**
     * 跳转到字典模块子模块主页面
     * @return
     */
    @RequestMapping(value = "/type/toIndex.do")
    public String toTypeIndex(){
        return "/settings/dictionary/type/index";
    }

    @RequestMapping(value = "/value/toIndex.do")
    public String toValueIndex(){
        return "/settings/dictionary/value/index";
    }
}
