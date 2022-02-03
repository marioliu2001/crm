package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    boolean insert(Contacts contacts);

    List<Contacts> findByFullName(String fullname);

}
