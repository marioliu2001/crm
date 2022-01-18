package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.entity.RPage;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2022/1/16 22:36
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/workbench/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 跳转到市场活动主页面
     * @return
     */
    @RequestMapping(value = "/toIndex.do")
    public String toIndex(){
        return "/workbench/activity/index";
    }

    /**
     * 加载市场活动主页面数据
     * @return
     *
     * Map<String , Object> resultMap = new HashMap<>();
     *         resultMap.put("code",0);
     *         resultMap.put("msg","查询成功");
     *         resultMap.put("data",activityList);
     *         return resultMap;
     */
    @RequestMapping(value = "/getActivityList.do")
    @ResponseBody
    public Map<String,Object> getActivityList() throws AjaxRequestException {
        //调用 service
        List<Activity> activityList = activityService.getActivityList();
        //判断
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("查询失败");
        //查询成功
        return R.ok(0,"查询成功...",activityList);
    }

    /**
     * 获取市场活动列表数据-分页查询
     *
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/getActivityListByPage.do")
    @ResponseBody
    public R getActivityListByPage(Integer pageNo, Integer pageSize) throws AjaxRequestException {

        //根据当前页，计算出查询的索引值位置
        int pageNoIndex = (pageNo-1)*pageSize;

        //调用 service 查询列表
        List<Activity> activityList = activityService.getActivityListByPage(pageNoIndex, pageSize);
        //判断
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("查询失败");
        //查询成功
        return R.ok(0, "查询成功...", activityList);
    }

    /**
     * 获取市场活动列表数据-分页查询-加载前端分页组件
     * @param pageNo
     * @param pageSize
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/getActivityListByPageComponent.do")
    @ResponseBody
    public RPage<List<Activity>> getActivityListByPageComponent(Integer pageNo, Integer pageSize) throws AjaxRequestException {

        //根据当前页，计算出查询的索引值位置
        int pageNoIndex = (pageNo-1)*pageSize;

        //调用 service 查询列表
        List<Activity> activityList = activityService.getActivityListByPage(pageNoIndex, pageSize);
        //判断
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("查询失败");

        //查询当前市场活动的列表的总记录数
        long totalCounts = activityService.findActivityTotalCount();

        //计算出总页数
        long totalPages = totalCounts%pageSize==0 ? totalCounts/pageSize:(totalCounts/pageSize)+1;

        //查询成功
        return new RPage<List<Activity>>().setCode(0)
                            .setMsg("查询成功")
                            .setData(activityList)
                            .setPageNo(pageNo)
                            .setPageSize(pageSize)
                            .setMaxRowPerPage(20)
                            .setVisiblePages(3)
                            .setTotalCounts(totalCounts)
                            .setTotalPages(totalPages);
    }

    /**
     * 获取市场活动列表数据-分页查询-加载前端分页组件-条件过滤查询
     * @param pageNo
     * @param pageSize
     * 四个参数可能有空的，在mybatis映射配置文件中
     * 进行动态sql的判断处理，如果不为空，则拼接sql，如果为空，则不拼接
     * @param activityName 市场活动名称，模糊查询
     * @param username      用户名称，等值查询
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/getActivityListByPageComponentCondition.do")
    @ResponseBody
    public RPage<List<Activity>> getActivityListByPageComponentCondition(Integer pageNo,
                                                                         Integer pageSize,
                                                                         String activityName,
                                                                         String username,
                                                                         String startDate,
                                                                         String endDate
                                                                         ) throws AjaxRequestException {

        //根据当前页，计算出查询的索引值位置
        int pageNoIndex = (pageNo-1)*pageSize;

        //调用service 查询市场活动列表-条件过滤查询
        List<Activity> activityList = activityService.getActivityListByPageCondition(
                pageNoIndex,
                pageSize,
                activityName,
                username,
                startDate,
                endDate
                );
        //判断
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("查询失败");
        System.out.println("我要模糊查询的条件是："+activityName);
        //查询当前市场活动的列表的总记录数
        long totalCounts = activityService.findActivityTotalCountCondition(
                activityName,
                username,
                startDate,
                endDate
        );
        System.out.println("当前查询到的有几个："+totalCounts);
        //计算出总页数
        long totalPages = totalCounts%pageSize==0 ? totalCounts/pageSize:(totalCounts/pageSize)+1;
        System.out.println("需要分出几页来呈现："+totalPages);
        //查询成功
        return new RPage<List<Activity>>()
                .setCode(0)
                .setMsg("查询成功")
                .setData(activityList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowPerPage(20)
                .setVisiblePages(3)
                .setTotalCounts(totalCounts)
                .setTotalPages(totalPages);
    }

    /**
     * 添加一条市场活动
     * @param activity
     * @param session
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/saveActivity.do")
    @ResponseBody
    public R saActivity(Activity activity, HttpSession session) throws AjaxRequestException {

        //所需要添加的属性还有：创建人，创建时间，修改时间，修改人
        String createBy = ((User) session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        //新增操作
        boolean flag = activityService.saveActivity(activity,createBy,createTime);

        //添加失败
        if (!flag)
            throw new AjaxRequestException("添加失败");

        //添加成功
        return R.ok();
    }

    /**
     * 根据id查询一条市场活动-回显数据
     * @param id
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/getActivityById.do")
    @ResponseBody
    public R getActivityById(String id) throws AjaxRequestException {
        //根据市场活动id查询一条市场活动
        Activity activity = activityService.getActivityById(id);

        //查询失败
        if (ObjectUtils.isEmpty(activity))
            throw new AjaxRequestException("查询失败...");

        //查询成功
        return R.ok(0,"查询成功",activity);
    }

    /**
     * 根据id更新市场活动
     * @param activity
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/updateActivity.do")
    @ResponseBody
    public R updateActivity(Activity activity,HttpSession session) throws AjaxRequestException {
        //获取修改人-就是登陆的用户
        String editBy = ((User) session.getAttribute("user")).getName();
        //获取修改时间
        String editTime = DateTimeUtil.getSysTime();

        //进行更新操作
        boolean flag = activityService.updateActivityById(activity,editBy,editTime);
        //更新失败
        if (!flag)
            throw new AjaxRequestException("更新失败...");
        //更新成功
        return R.ok(0,"更新成功");
    }

    @RequestMapping(value = "/batchDeleteActivity.do")
    @ResponseBody
    public R batchDeleteActivity(String[] activityIds, HttpSession session) throws AjaxRequestException {
        //获取编辑人
        String editBy = ((User) (session.getAttribute("user"))).getName();
        //获取编辑时间
        String editTime = DateTimeUtil.getSysTime();

        //进行逻辑删除
        activityService.batchDeleteActivityByIds(activityIds,editBy,editTime);

        return R.ok();
    }
}



