package com.bjpowernode.crm.settings.service.Impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.dao.DictionaryTypeDao;
import com.bjpowernode.crm.settings.dao.DictionaryValueDao;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.domain.DictionaryValue;
import com.bjpowernode.crm.settings.service.DictionaryTypeService;
import com.bjpowernode.crm.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Date 2022/1/15 16:57
 * @Version 1.0
 */
@Service
public class DictionaryTypeServiceImpl implements DictionaryTypeService {

    @Autowired
    private DictionaryTypeDao dictionaryTypeDao;
    @Autowired
    private DictionaryValueDao dictionaryValueDao;

    @Override
    public List<DictionaryType> findAllDictionaryType() {
        return dictionaryTypeDao.findAllDictionaryType();
    }

    @Override
    public DictionaryType findOneTypeCode(String code) {
        return dictionaryTypeDao.findByCode(code);
    }

    @Override
    public boolean saveDictionaryType(DictionaryType dictionaryType) {
        return dictionaryTypeDao.insertDictionaryType(dictionaryType);
    }

    @Override
    public boolean updateDictionaryType(DictionaryType dictionaryType) {
        return dictionaryTypeDao.update(dictionaryType);
    }

    @Override
    public boolean batchDeleteDictionaryType(String[] codes) throws AjaxRequestException {
        //批量删除
        //1.遍历删除
        /*for (String code:codes) {
            boolean flag = dictionaryTypeDao.delete(code);
            if (!flag){
                throw new AjaxRequestException("删除失败，请刷新后重试...");
            }
        }
        return true;*/

        //2.通过sql语句批量删除
        boolean flag = dictionaryTypeDao.batchDeleteDictionaryTypeList(codes);
        if (!flag){
            throw new AjaxRequestException("删除失败，请刷新页面后重试...");
        }
        return true;
    }

    @Override
    public List<String> batchDeleteDictionaryTypeCondition(String[] codes) throws AjaxRequestException {
        //考虑关联关系，部分删除，部分不能删除
        //1.先遍历codes
        List<String> codeList = new ArrayList<>();
        for (String code : codes) {
            int count = dictionaryValueDao.findListByTypeCode(code);
            if (count == 0){
                //没有关联关系，可以删除
                boolean flag = dictionaryTypeDao.delete(code);
                if (!flag){
                    throw new AjaxRequestException("删除失败，请刷新页面后重试...");
                }
            }else {
                //不可以删除，存到list集合中
                codeList.add(code);
            }
        }
        return codeList;
    }

    @Override
    public List<DictionaryValue> getDictionaryValueList() {
        return dictionaryValueDao.findAll();
    }

    @Override
    public boolean saveDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException {
        //引入工具类给dictionaryValue添加id值
        dictionaryValue.setId(UUIDUtil.getUUID());
        //新增操作
        boolean flag = dictionaryValueDao.insert(dictionaryValue);
        if (!flag)
            throw new AjaxRequestException("添加失败,请重试");
        return true;
    }
}
