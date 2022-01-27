package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.entity.RPage;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

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

    @RequestMapping("/batchDeleteClueById.do")
    @ResponseBody
    public R batchDeleteClueById(String[] clueIds) throws AjaxRequestException {
        //删除操作
         clueService.batchDeleteClueById(clueIds);

         return R.ok();
    }
}
