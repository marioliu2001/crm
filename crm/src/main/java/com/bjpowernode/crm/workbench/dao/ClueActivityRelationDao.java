package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueActivityRelationDao {


    List<ClueActivityRelation> findListByClueId(String clueId);

    boolean deleteListByClueId(String clueId);
}
