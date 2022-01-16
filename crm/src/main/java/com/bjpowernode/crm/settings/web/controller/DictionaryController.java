package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.service.DictionaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description TODO
 * @Date 2022/1/14 23:48
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/settings/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryTypeService typeService;

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
     *
     * @return
     */

    //字典类型主页面
    @RequestMapping(value = "/type/toIndex.do")
    public String toTypeIndex(Model model) {

        //查询字典类型的数据
        List<DictionaryType> typeList = typeService.findAllDictionaryType();
        model.addAttribute("typeList",typeList);
        //将数据遍历到前端页面

        return "/settings/dictionary/type/index";
    }

    //字典值主页面
    @RequestMapping(value = "/value/toIndex.do")
    public String toValueIndex(){
        return "/settings/dictionary/value/index";
    }

    /**
     *跳转到字典类型添加页面
     * @return
     */
    @RequestMapping(value = "/toTypeSave.do")
    public String toTypeSave(){
        return "/settings/dictionary/type/save";
    }

    /**
     * 校验是否能添加
     * @param code
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/type/checkCode.do")
    @ResponseBody
    public Map<String,Object> checkCode(String code) throws AjaxRequestException {
        //去数据库查询code
        DictionaryType dictionaryType = typeService.findOneTypeCode(code);

        Map<String,Object> resultMap = new HashMap<>();
        //判断
        if (ObjectUtils.isEmpty(dictionaryType)){
            //返回错误信息
            resultMap.put("code",0);
            resultMap.put("msg","编码未重复，可以新增...");
            return resultMap;
        }
        throw new AjaxRequestException("编码已存在请更改...");
    }

    /**
     * 字典类型添加操作
     * @param dictionaryType
     * @return
     */
    @RequestMapping(value = "/type/saveDictionaryType.do")
    @ResponseBody
    public Map<String,Object> saveDictionaryType(DictionaryType dictionaryType) throws AjaxRequestException {
        //调用service
        boolean count = typeService.saveDictionaryType(dictionaryType);
        Map<String,Object> resultMap = new HashMap<>();
        //判断
        if (count){
            //添加成功
            resultMap.put("code", 0);
            resultMap.put("msg","添加成功...");
            return resultMap;
        }else {
            throw new AjaxRequestException("添加失败，请刷新后再试...");
        }
    }

    @RequestMapping(value = "/type/saveDictionaryValue.do")
    public String saveDictionaryValue(){
        return "/settings/dictionary/value/save";
    }

}
