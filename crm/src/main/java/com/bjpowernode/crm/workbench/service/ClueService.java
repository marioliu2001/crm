package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueService {
    /**
     * 查询所有数据（未分页）
     * @return
     */
    @Deprecated
    List<Clue> getClueList();

    /**
     * 查询总记录数
     * @return
     */
    long getTotalCounts();

    /**
     * 查询所有数据（分页）
     * @param pageNoIndex
     * @param pageSize
     * @return
     */
    List<Clue> getClueListByPage(int pageNoIndex, Integer pageSize);

    /**
     * 条件过滤查询
     * @param pageNoIndex
     * @param pageSize
     * @param name
     * @param company
     * @param phone
     * @param source
     * @param owner
     * @param mphone
     * @param state
     * @return
     */
    List<Clue> getClueListByPageCondition(int pageNoIndex, Integer pageSize, String name, String company, String phone, String source, String owner, String mphone, String state);


    /**
     * 条件过滤查询的总数记录数
     * @param name
     * @param company
     * @param phone
     * @param source
     * @param owner
     * @param mphone
     * @param state
     * @return
     */
    long getTotalCountsCondition(String name, String company, String phone, String source, String owner, String mphone, String state);

    /**
     * 新增线索操作
     * @param clue
     * @return
     */
    boolean saveClue(Clue clue);

    /**
     * 根据clueId查询一条线索
     * @param id
     * @return
     */
    Clue getClueByClueId(String id);

    /**
     * 根据id更新
     * @param clue
     * @return
     */
    boolean updateClueById(Clue clue);

    /**
     * 批量删除操作
     * @param clueIds
     */
    void batchDeleteClueById(String[] clueIds) throws AjaxRequestException;

}
