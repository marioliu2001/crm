package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer findByName(String customerName);

    boolean insert(Customer customer);

}
