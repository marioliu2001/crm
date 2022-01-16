package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.domain.DictionaryValue;

import java.util.List;

public interface DictionaryTypeService {
    List<DictionaryType> findAllDictionaryType();

    DictionaryType findOneTypeCode(String code);

    boolean saveDictionaryType(DictionaryType dictionaryType);

    boolean updateDictionaryType(DictionaryType dictionaryType);

    boolean batchDeleteDictionaryType(String[] codes) throws AjaxRequestException;

    List<String> batchDeleteDictionaryTypeCondition(String[] codes) throws AjaxRequestException;

    List<DictionaryValue> getDictionaryValueList();
}
