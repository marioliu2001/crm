package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.entity.RPage;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TranditionRequestException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2022/1/24 11:14
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/workbench/clue")
public class ClueController {

    @Autowired
    private ClueService clueService;

    @Autowired
    private ActivityService activityService;

    /**
     * 跳转线索主页面
     * @return
     */
    @RequestMapping("/toIndex.do")
    public String toIndex() {

        return "/workbench/clue/index";

    }

    /**
     * 加载clue数据
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping("/getClueList.do")
    @ResponseBody
    public R getClueList() throws AjaxRequestException {

        //查询数据
        List<Clue> clueList = clueService.getClueList();

        if (ObjectUtils.isEmpty(clueList))
            throw new AjaxRequestException("查询失败...");

        //返回数据
        return R.ok(0,"查询成功",clueList);
    }

    @RequestMapping("/getClueListByPage.do")
    @ResponseBody
    public RPage<List<Clue>> getClueListByPage(Integer pageNo,Integer pageSize) throws AjaxRequestException {
        //根据页数，查询出索引值位置
        int  pageNoIndex = (pageNo-1)*pageSize;

        //查询线索数据
        List<Clue> clueList = clueService.getClueListByPage(pageNoIndex, pageSize);

        //当前列表无数据
        if (ObjectUtils.isEmpty(clueList))
            throw new AjaxRequestException("当前列表无数据...");

        //查询当前线索总记录数
        long totalCounts = clueService.getTotalCounts();

        //计算当前总页数
        long totalPages = totalCounts % pageSize == 0 ? totalCounts / pageSize : (totalCounts / pageSize) + 1;

        //查询成功
        return new RPage<List<Clue>>()
                .setCode(0)
                .setMsg("查询成功")
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowPerPage(20)
                .setVisiblePages(3)
                .setTotalCounts(totalCounts)
                .setTotalPages(totalPages)
                .setData(clueList);
    }

    @RequestMapping("/getClueListByPageCondition.do")
    @ResponseBody
    public RPage<List<Clue>> getClueListByPageCondition(Integer pageNo,
                                                        Integer pageSize,
                                                        String  name,
                                                        String  company,
                                                        String  phone,
                                                        String  source,
                                                        String  owner,
                                                        String  mphone,
                                                        String  state ) throws AjaxRequestException {

        //根据页数，查询出索引值位置
        int  pageNoIndex = (pageNo-1)*pageSize;

        //查询线索数据
        List<Clue> clueList = clueService.getClueListByPageCondition(pageNoIndex,
                pageSize,
                name,
                company,
                phone,
                source,
                owner,
                mphone,
                state);
        System.out.println(clueList);

        //当前列表无数据
        if (ObjectUtils.isEmpty(clueList))
            throw new AjaxRequestException("当前列表无数据...");

        //查询当前线索总记录数
        long totalCounts = clueService.getTotalCountsCondition(name,
                company,
                phone,
                source,
                owner,
                mphone,
                state);

        //计算当前总页数
        long totalPages = totalCounts % pageSize == 0 ? totalCounts / pageSize : (totalCounts / pageSize) + 1;
        System.out.println("查询出来当前总记录数"+totalCounts);

        //查询成功
        return new RPage<List<Clue>>()
                .setCode(0)
                .setMsg("查询成功")
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowPerPage(20)
                .setVisiblePages(3)
                .setTotalCounts(totalCounts)
                .setTotalPages(totalPages)
                .setData(clueList);
    }

    /**
     *
     * id
     * fullname 联系人姓名 1
     * appellation 称呼 1
     * owner 外键 1
     * company 公司名称 1
     * job 工作 1
     * email 邮箱 1
     * phone 座机 1
     * website 网址 1
     * mphone 手机 1
     * state 线索状态 1
     * source 线索来源 1
     * createBy
     * createTime
     * editBy
     * editTime
     * description 描述 1
     * contactSummary 会议纪要 1
     * nextContactTime 下次联系时间 1
     * address 地址 1
     * @return 添加操作
     */
    @RequestMapping("/saveClue.do")
    @ResponseBody
    public R saveClue(Clue clue, HttpSession session) throws AjaxRequestException {

        //获取创建人和创建事件
        String createBy = ((User) session.getAttribute("user")).getName();
        //获取创建事件
        String createTime = DateTimeUtil.getSysTime();

        //给后台传过来的clue添加没有的属性
        clue.setId(UUIDUtil.getUUID())
            .setCreateBy(createBy)
            .setCreateTime(createTime)
            .setEditBy(createBy)
            .setEditTime(createTime);

        //调用新增方法,传入clue参数
        boolean flag = clueService.saveClue(clue);

        if (!flag)
            throw new AjaxRequestException("新增失败...");

        return R.ok(0,"新增成功");
    }

    /**
     * 根据选中id回显数据（修改操作）
     * @param id
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping("/getClueByClueId.do")
    @ResponseBody
    public R getClueByClueId(String id) throws AjaxRequestException {

        //根据clueId一条查询数据
        Clue clue = clueService.getClueByClueId(id);

        if (ObjectUtils.isEmpty(clue))
            throw new AjaxRequestException("查询失败...");

        return R.ok(0,"查询成功",clue);

    }

    /**
     * 更新clue操作
     * @param clue
     * @param session
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping("/editClue.do")
    @ResponseBody
    public R editClue(Clue clue,HttpSession session) throws AjaxRequestException {
        //获取当前用户
        String editBy = ((User) session.getAttribute("user")).getName();
        //获取当前时间
        String editTime = DateTimeUtil.getSysTime();

        clue.setEditBy(editBy)
            .setEditTime(editTime);

        //更新操作
        boolean flag = clueService.updateClueById(clue);

        if (!flag)
            throw new AjaxRequestException("更新失败...");

        return R.ok();
    }

    /**
     * 批量删除操作
     * @param clueIds
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping("/batchDeleteClueById.do")
    @ResponseBody
    public R batchDeleteClueById(String[] clueIds) throws AjaxRequestException {
        //删除操作
         clueService.batchDeleteClueById(clueIds);

         return R.ok();
    }

    /**
     * 跳转到备注页面
     * @param id
     * @return
     */
    @RequestMapping("/toDetail.do")
    public String toDetail(String id, Model model) throws AjaxRequestException {

        //根据id查询数据
        Clue clue = clueService.toDetail(id);

        if (ObjectUtils.isEmpty(clue))
            throw new AjaxRequestException("查询失败...");
        //封装对象到model中

        model.addAttribute("clue",clue);

        return "/workbench/clue/detail";

    }

    /**
     * 获得线索的备注信息
     * @param clueId
     * @return
     */
    @RequestMapping("/getClueRemarkList.do")
    @ResponseBody
    public R getClueRemarkList(String clueId){

        //根据clueId查询备注信息
        List<ClueRemark> clueRemarkList = clueService.getClueRemarkList(clueId);

        //返回
        return R.ok(0,"查询成功",clueRemarkList);
    }

    /**
     * 查询关联的市场活动列表
     * @param clueId
     * @return
     */
    @RequestMapping("/getActivityRelationList.do")
    @ResponseBody
    public R getActivityRelationList(String clueId) {

        //查询数据
        List<Map<String, String>> activityRelationListMap = clueService.getActivityRelationListMap(clueId);

        //返回
        return R.ok(0,
                    ObjectUtils.isEmpty(activityRelationListMap)?"查询失败":"查询成功",
                    activityRelationListMap
                );

    }

    /**
     * 删除关联关系
     * @param carId
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping("/deleteClueActivityRelation.do")
    @ResponseBody
    public R deleteClueActivityRelation(String carId) throws AjaxRequestException {

        //删除操作
        boolean flag = clueService.deleteClueActivityRelation(carId);

        if (!flag)
            throw new AjaxRequestException("删除关联关系失败...");

        return R.ok();
    }

    /**
     * 得到未关联的市场活动数据
     * @param clueId
     * @return
     */
    @RequestMapping("/getActivityUnRelationList.do")
    @ResponseBody
    public R getActivityUnRelationList(String clueId){
        //查询未关联的市场活动数据
        List<Activity> activityList = clueService.getActivityUnRelationList(clueId);

        return R.ok(0,
                    ObjectUtils.isEmpty(activityList)?"没有未关联的":"查询成功",
                    activityList
                    );
    }

    /**
     * 模糊查询
     * @param clueId
     * @param activityName
     * @return
     */
    @RequestMapping("/searchLikeActivityUnRelationList.do")
    @ResponseBody
    public R searchLikeActivityUnRelationList(String clueId,String activityName){
        //模糊查询
        List<Activity> activityList = clueService.searchLikeActivityUnRelationList(clueId, activityName);

        return R.ok(0,
                    ObjectUtils.isEmpty(activityList)?"未查询到...":"查询成功",
                    activityList
                    );
    }

    /**
     * 新增关联关系
     * @param activityIds
     * @param clueId
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping("/saveClueActivityRelation.do")
    @ResponseBody
    public R saveClueActivityRelation(String[] activityIds,String clueId) throws AjaxRequestException {

        //添加操作
        boolean flag = clueService.saveClueActivityRelation(activityIds, clueId);

        if (!flag)
            throw new AjaxRequestException("新增失败...");

        return R.ok();
    }

    /**
     * 跳转转换页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/toConvert.do")
    public String toConvert(String id,Model model){

        //根据id,查询线索数据
        Clue clue = clueService.getClueByClueId(id);

        //携带数据
        model.addAttribute("clue",clue);

        return "/workbench/clue/convert";
    }

    /**
     * 获取市场活动源
     * @param clueId
     * @return
     */
    @RequestMapping("/getActivityResourceList.do")
    @ResponseBody
    public R getActivityResourceList(String clueId){

        //有关联的数据
        List<Map<String, String>> activityRelationListMap = clueService.getActivityRelationListMap(clueId);

        if (ObjectUtils.isEmpty(activityRelationListMap)){
            //当前没有关联的市场活动，所以查询所有市场活动
            List<Activity> activityList = activityService.findActivityList();

            return R.ok(0,"所有的市场活动...",activityList);
        }

        return R.ok(0,"能查询出来已关联的市场活动...",activityRelationListMap);
    }

    /**
     * 线索转换操作
     *      业务逻辑
     *          1. 根据线索id,查询出线索数据
     *          2. 根据线索中的客户名称,查看是否有当前的客户信息
     *          3. 新增客户数据
     *          4. 新增联系人数据
     *
     *          1-4 完成 线索 转换成 联系人和客户 数据
     *
     *          5. 根据线索id,查询出线索的备注信息列表,完成转换
     *              将线索备注信息列表转换为联系人和市场活动的备注信息列表
     *          6. 根据线索id,查询出线索和市场活动的中间表数据,完成转
     *              将线索和市场活动的关联关系转换为联系人和市场活动的关联关系
     *
     *              5-6 完成 线索一对多和多对多的关系
     *
     *          7. 判断是否创建交易
     *              flag = a,代表创建了交易
     *          8. 新增交易和交易历史记录
     *          9. 删除线索相关数据,根据线索id进行删除操作
     *              删除线索和市场活动的关联关系
     *              删除线索备注信息列表
     *              删除线索
     *      表关系
     *          tbl_clue_activity_relation -> tbl_contacts_activity_relation
     *          tbl_clue_remark            -> tbl_contacts_remark 和 tbl_customer_remark
     *          tbl_clue                   -> tbl_contacts 和 tbl_customer
     *                                     -> tbl_tran 和 tbl_tran_history
     *
     * @param clueId
     * @param flag
     * @param tran
     * @param session
     * @return
     */
    @RequestMapping("/convert.do")
    public String convert (String clueId, String flag, Tran tran,HttpSession session) throws TranditionRequestException {

        //准备数据,owner,createBy,createTime
        String owner = ((User) session.getAttribute("user")).getId();
        String createBy = ((User) session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        //线索转换
        clueService.saveConvert(clueId,flag,owner,createBy,createTime,tran);

        //线索转换成功，跳转到线索首页面
        return "redirect:/workbench/clue/toIndex.do";
    }
}
