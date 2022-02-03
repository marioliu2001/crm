package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TranditionRequestException;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据id查询线索数据（消除笛卡尔积）
     * @param id
     * @return
     */
    Clue toDetail(String id);

    /**
     * 根据clueId查询备注信息
     * @param clueId
     * @return
     */
    List<ClueRemark> getClueRemarkList(String clueId);

    /**
     * 查询关联的市场活动列表
     * @param clueId
     * @return
     */
    List<Map<String, String>> getActivityRelationListMap(String clueId);

    /**
     * 删除关联关系
     * @param carId
     * @return
     */
    boolean deleteClueActivityRelation(String carId);

    /**
     * 查询未关联的市场活动
     * @param clueId
     * @return
     */
    List<Activity> getActivityUnRelationList(String clueId);

    /**
     * 模糊查询未关联的市场活动
     * @param clueId
     * @param activityName
     * @return
     */
    List<Activity> searchLikeActivityUnRelationList(String clueId, String activityName);

    /**
     * 新增关联关系
     * @param activityIds
     * @param clueId
     * @return
     */
    boolean saveClueActivityRelation(String[] activityIds, String clueId);

    void saveConvert(String clueId, String flag, String owner, String createBy, String createTime, Tran tran) throws TranditionRequestException;

}
