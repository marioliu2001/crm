package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;

public interface ActivityService {
    /**
     * 未分页
     * @return
     */
    @Deprecated //代表当前方法已过时
    List<Activity> getActivityList();

    /**
     * 分页的市场查询列表
     * @param pageNoIndex
     * @param pageSize
     * @return
     */
    @Deprecated
    List<Activity> getActivityListByPage(int pageNoIndex, Integer pageSize);

    /**
     * 查询市场活动总记录数
     * @return
     */
    @Deprecated
    long findActivityTotalCount();

    /**
     * 分页查询-条件过滤查询
     * @param pageNoIndex
     * @param pageSize
     * @param activityName
     * @param username
     * @param startDate
     * @param endDate
     * @return
     */
    List<Activity> getActivityListByPageCondition(int pageNoIndex, Integer pageSize, String activityName, String username, String startDate, String endDate);

    /**
     * 查询总记录数-条件过滤查询
     * @param activityName
     * @param username
     * @param startDate
     * @param endDate
     * @return
     */
    long findActivityTotalCountCondition(String activityName, String username, String startDate, String endDate);

    /**
     * 添加市场活动操作
     * @param activity
     * @param createBy
     * @param createTime
     * @return
     */
    boolean saveActivity(Activity activity, String createBy, String createTime) throws AjaxRequestException;

}
