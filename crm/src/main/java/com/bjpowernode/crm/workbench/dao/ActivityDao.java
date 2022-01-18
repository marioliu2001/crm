package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
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

}
