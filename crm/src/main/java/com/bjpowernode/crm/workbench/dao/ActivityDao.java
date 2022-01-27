package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityDao {
    List<Activity> findAllActivityList();

    List<Activity> findAllByPage(@Param("pageNo") int pageNoIndex,
                                 @Param("pageSize") Integer pageSize);

    long findTotalCounts();

    List<Activity> findAllByPageCondition(@Param("pageNo") int pageNoIndex,
                                          @Param("pageSize") Integer pageSize,
                                          @Param("name") String activityName,
                                          @Param("username") String username,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate);

    long findActivityTotalCountCondition(@Param("name") String activityName,
                                         @Param("username") String username,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate);

    boolean insert(Activity activity);

    Activity findActivityById(String id);

    boolean updateActivityById(Activity activity);

    boolean updateIsDeleteById(String activityId,String editBy,String editTime);

    List<Activity> findAllByIsDelete();


    List<Activity> exportActivityByIds(String[] activityIds);

    Activity findOneActivityById(String id);

    List<ActivityRemark> findActivityRemarkList(String activityId);

    boolean saveActivityRemark(ActivityRemark activityRemark);

    boolean updateRemark(@Param("remarkId") String remarkId,
                         @Param("noteContent") String noteContent,
                         @Param("editBy") String editBy,
                         @Param("editTime") String editTime);

}
