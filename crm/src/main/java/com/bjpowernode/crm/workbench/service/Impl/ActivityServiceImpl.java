package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @Date 2022/1/16 23:13
 * @Version 1.0
 */

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Override
    public List<Activity> getActivityList() {
        return activityDao.findAllActivityList();
    }

    @Override
    public List<Activity> getActivityListByPage(int pageNoIndex, Integer pageSize) {
        return activityDao.findAllByPage(pageNoIndex,pageSize);
    }

    @Override
    public long findActivityTotalCount() {
        return activityDao.findTotalCounts();
    }

    @Override
    public List<Activity> getActivityListByPageCondition(int pageNoIndex, Integer pageSize, String activityName, String username, String startDate, String endDate) {
        return activityDao.findAllByPageCondition(pageNoIndex,pageSize,activityName,username,startDate,endDate);
    }

    @Override
    public long findActivityTotalCountCondition(String activityName, String username, String startDate, String endDate) {
        return activityDao.findActivityTotalCountCondition(activityName,username,startDate,endDate);
    }

    @Override
    public boolean saveActivity(Activity activity, String createBy, String createTime) throws AjaxRequestException {
        activity.setId(UUIDUtil.getUUID())
                .setCreateBy(createBy)
                .setEditBy(createBy)
                .setCreateTime(createTime)
                .setEditTime(createTime);
        boolean flag = activityDao.insert(activity);
        if (!flag)
            throw new AjaxRequestException("添加失败...");
        return true;
    }
}
