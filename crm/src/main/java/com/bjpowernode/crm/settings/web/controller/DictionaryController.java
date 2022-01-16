package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TranditionRequestException;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.domain.DictionaryValue;
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
     * 跳转到字典类型主页面
     *
     * @return
     */
    @RequestMapping(value = "/type/toIndex.do")
    public String toTypeIndex(Model model) {

        //查询字典类型的数据
        List<DictionaryType> typeList = typeService.findAllDictionaryType();
        model.addAttribute("typeList",typeList);
        //将数据遍历到前端页面

        return "/settings/dictionary/type/index";
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
     * 跳转到添加页面
     * @return
     */
    @RequestMapping(value = "saveDictionaryType.do")
    public String saveDictionaryType(){
        return "/settings/dictionary/type/save";
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

    /**
     * 跳转到字典类型编辑页面
     *      根据编码查询字典类型数据
     * @param code
     * @return
     */
    @RequestMapping("/type/toEdit.do")
    public String toTypeEdit(String code,Model model) throws TranditionRequestException {

        //根据编码查询字典类型数据
        DictionaryType OneDictionaryType = typeService.findOneTypeCode(code);

        if(OneDictionaryType == null)
            throw new TranditionRequestException("当前数据查询异常...");

        //封装到Model对象中
        model.addAttribute("OneDictionaryType",OneDictionaryType);

        //跳转到修改页面
        return "/settings/dictionary/type/edit";
    }

    /**
     * 更新操作
     * @param dictionaryType
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/type/updateDictionaryType.do")
    @ResponseBody
    public Map<String,Object> updateDictionaryType(DictionaryType dictionaryType) throws AjaxRequestException {
        boolean flag = typeService.updateDictionaryType(dictionaryType);
        if (!flag){
            //修改失败
            throw new AjaxRequestException("修改失败，请刷新后再试...");
        }
        //修改成功
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("msg","修改成功");
        return resultMap;
    }

    /**
     * 删除操作
     * @param codes
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/type/batchDeleteDictionaryType.do")
    @ResponseBody
    public Map<String,Object> batchDeleteDictionaryType(String[] codes) throws AjaxRequestException {
        //service调用删除命令

        boolean flag = typeService.batchDeleteDictionaryType(codes);
        if (!flag){
            //删除失败
            throw new AjaxRequestException("删除失败，请刷新页面后重试...");
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("msg","删除成功");
        return resultMap;
    }

    /**
     * 考虑一方多方的关系（批量删除）
     * @param codes
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/type/batchDeleteDictionaryTypeCondition.do")
    @ResponseBody
    public Map<String,Object> batchDeleteDictionaryTypeCondition(String[] codes) throws AjaxRequestException {
        //service调用删除命令

        //批量删除
        List<String> codeList = typeService.batchDeleteDictionaryTypeCondition(codes);
        if (ObjectUtils.isEmpty(codeList)) {
            //批量删除成功（都没有关联关系）
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("code",0);
            resultMap.put("msg","删除成功");
            return resultMap;
        }
        //有关联关系，没有全部删除
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",1);
        resultMap.put("msg","删除成功");
        resultMap.put("data",codeList);
        return resultMap;
    }

    /**
     * 跳转到字典值主页面
     * @return
     */
    @RequestMapping(value = "/value/toIndex.do")
    public String toValueIndex(){
        return "/settings/dictionary/value/index";
    }


    @RequestMapping(value = "/value/getDictionaryValueList.do")
    @ResponseBody
    public Map<String, Object> getDictionaryValueList() {
        //查询数据
        List<DictionaryValue> dictionaryValuesList = typeService.getDictionaryValueList();
        //封装返回结果集
        Map<String,Object> resultMap = new HashMap<>();
        if (ObjectUtils.isEmpty(dictionaryValuesList)) {
            //没查询出来
            resultMap.put("code",1);
            resultMap.put("msg","查询失败...");
            return resultMap;
        }
        //查询成功
        resultMap.put("code",0);
        resultMap.put("msg","查询成功");
        resultMap.put("data",dictionaryValuesList);
        return resultMap;
    }
}
