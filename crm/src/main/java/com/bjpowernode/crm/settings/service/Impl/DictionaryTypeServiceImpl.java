package com.bjpowernode.crm.settings.service.Impl;

import com.bjpowernode.crm.settings.dao.DictionaryTypeDao;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.service.DictionaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
