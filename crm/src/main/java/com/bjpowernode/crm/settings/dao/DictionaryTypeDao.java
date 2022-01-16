package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DictionaryType;

import java.util.List;

public interface DictionaryTypeDao {
    List<DictionaryType> findAllDictionaryType();

    DictionaryType findByCode(String code);

    boolean insertDictionaryType(DictionaryType dictionaryType);

    boolean update(DictionaryType dictionaryType);

    boolean delete(String code);

    boolean batchDeleteDictionaryTypeList(String[] codes);
}
