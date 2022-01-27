package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TranditionRequestException;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

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

    /**
     * 根据id查询市场活动
     * @param id
     * @return
     */
    Activity getActivityById(String id);

    /**
     * 根据id更新市场活动
     * @param activity
     * @param editBy
     * @param editTime
     * @return
     */
    boolean updateActivityById(Activity activity, String editBy, String editTime);

    /**
     *逻辑删除操作
     * @param activityIds
     */
    void batchDeleteActivityByIds(String[] activityIds,String editBy,String editTime) throws AjaxRequestException;

    /**
     * 批量导入excle文件
     * @param activityList
     */
    void saveActivityList(List<Activity> activityList) throws TranditionRequestException;

    /**
     * 批量导出
     * @return
     */
    List<Activity> findActivityList();

    /**
     * 选择导出
     * @param activityIds
     * @return
     */
    List<Activity> exportActivityByIds(String[] activityIds);

    /**
     * 根据id查询市场活动并显示在详情页面
     * @param id
     * @return
     */
    Activity findActivityById(String id);

    /**
     * 查询备注信息
     * @return
     */
    List<ActivityRemark> findActivityRemarkList(String activityId);

    /**
     * 新增备注信息
     * @param activityRemark
     * @return
     */
    boolean saveActivityRemark(ActivityRemark activityRemark);

    /**
     * 更新备注信息
     * @param remarkId
     * @param noteContent
     * @param editBy
     * @param editTime
     * @return
     */
    boolean updateRemark(String remarkId, String noteContent, String editBy, String editTime);

    boolean deleteRemarkById(String remarkId);
}
