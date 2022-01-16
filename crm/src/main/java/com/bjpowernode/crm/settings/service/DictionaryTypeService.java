package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DictionaryType;

import java.util.List;

public interface DictionaryTypeService {
    List<DictionaryType> findAllDictionaryType();

    DictionaryType findOneTypeCode(String code);

    boolean saveDictionaryType(DictionaryType dictionaryType);
}
