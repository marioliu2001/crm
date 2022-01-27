package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.entity.RPage;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TranditionRequestException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebParam;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    /**
     * 批量删除数据（逻辑删除）
     * @param activityIds
     * @param session
     * @return
     * @throws AjaxRequestException
     */
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

    /**
     * 批量导入功能
     * @param activityFile
     * @param session
     * @return
     * @throws TranditionRequestException
     * @throws IOException
     */
    @RequestMapping(value = "/importActivity.do")
    public String importActivity(MultipartFile activityFile,HttpSession session) throws TranditionRequestException, IOException {

        //读取文件的原始文件名称
        String originalFilename = activityFile.getOriginalFilename();

        //获取文件的后缀名
        String suffix = originalFilename.substring(
                originalFilename.lastIndexOf(".") + 1);

        //校验文件是否时xls或xlsx文件格式
        if (!(StringUtils.endsWithIgnoreCase(suffix,"xls") || StringUtils.endsWithIgnoreCase(suffix,"xlsx"))){
            throw new TranditionRequestException("格式错误...");
        }
        //if(!(suffix.equals("xls") || suffix.equals("xlsx"))){
        //    throw new TranditionRequestException("文件格式错误,请合适后再上传...");
        //}

        //准备新的文件名称
        //可以使用UUID进行重命名,也可以使用时间戳进行重命名
        //String uuidFileName = UUIDUtil.getUUID() + ".xls";
        //String timeFileName = DateTimeUtil.getSysTimeForUpload() + ".xls";
        String timeFileName = DateTimeUtil.getSysTimeForUpload() + suffix;

        //创建保存上传文件的磁盘路径
        String url = "D:\\CrmProject\\crm\\src\\main\\webapp\\dataDir";

        File file = new File(url + "/" + timeFileName);

        //如果文件路径不存在，则需要创建出来
        if (!file.exists()){
            //创建不存在的文件夹
            file.mkdirs();
        }

        //文件上传操作
        activityFile.transferTo(file);

        //文件上传
        String owner = ((User) (session.getAttribute("user"))).getId();
        String createBy = ((User) (session.getAttribute("user"))).getName();
        String createTime = DateTimeUtil.getSysTime();

        //批量导入
        //经过分析,前端在Excel文件中指定的参数:
        //name startDate endDate cost description

        //通过上传后的路径，读取指定的文件读取到内存中
        InputStream in = new FileInputStream(file);

        //通过工作簿对象，读取文件中的内容
        Workbook workbook = null;
        if ("xls".equals(suffix)){
            //创建低版本的工作簿对象
            workbook = new HSSFWorkbook(in);
        }else {
            //创建高版本的工作簿对象
            workbook = new XSSFWorkbook(in);
        }

        //获取页码对象,通过工作簿对象
        Sheet sheet =  workbook.getSheetAt(0);

        //获取行对象,读取到最后的行号,读取的是索引值,从0开始
        int lastRowNum = sheet.getLastRowNum();

        //System.out.println(lastRowNum);
        List<Activity> activityList = new ArrayList<>();

        /*
        在number类型转化为String类型的过程中造成了
        Cannot get a STRING value from a NUMERIC cell这样的问题
        ，因此需要在读取excel单元格数据转化之前设置单元格类型为String，代码如下。

            //获取单元格
            XSSFCell cell = row.getCell(0);
            //设置单元格类型
            cell.setCellType(CellType.STRING);
            //获取单元格数据
            String cellValue = cell.getStringCellValue();
         */

        for (int i = 0; i < lastRowNum; i++) {

            Row row = sheet.getRow(i + 1);


            String name = row.getCell(0).getStringCellValue();
            String startDate =  row.getCell(1).getStringCellValue();
            String endDate = row.getCell(2).getStringCellValue();
            String cost = row.getCell(3).getStringCellValue();
            String description = row.getCell(4).getStringCellValue();

            //读取并封装到市场活动对象中
            Activity activity = new Activity()
                    .setName(name)
                    .setStartDate(startDate)
                    .setEndDate(endDate)
                    .setDescription(description)
                    .setCost(cost)
                    .setIsDelete("0")
                    .setId(UUIDUtil.getUUID())
                    .setCreateBy(createBy)
                    .setEditBy(createBy)
                    .setCreateTime(createTime)
                    .setEditTime(createTime)
                    .setOwner(owner);

                activityList.add(activity);

        }
        System.out.println("lastRowNum : "+lastRowNum);

        System.out.println("activityList : "+activityList);

        //完成批量导入操作
        activityService.saveActivityList(activityList);


        //批量导入
        return "redirect:/workbench/activity/toIndex.do";
    }

    /**
     * 批量导出
     * @param response
     * @throws TranditionRequestException
     * @throws IOException
     */
    @RequestMapping(value = "/exportActivityAll.do")
    public void exportActivityAll(HttpServletResponse response) throws TranditionRequestException, IOException {
        //将所有的未删除的市场活动数据,查询出来
        List<Activity> activityList = activityService.findActivityList();

        if (activityList.size()==0){
            throw new TranditionRequestException("当前市场活动无数据");
        }

        System.out.println(activityList);

        exportActivity(response,activityList);

    }

    /**
     * 封装的导出功能
     * @param response
     * @param activityList
     * @throws IOException
     */
    public void exportActivity(HttpServletResponse response, List<Activity> activityList) throws IOException {
        //将市场活动数据,封装到工作簿对象中,然后封装到页码和行还有单元格中的数据
        //创建工作簿对象

        Workbook workbook = new HSSFWorkbook();

        //创建页码对象
        Sheet sheet = workbook.createSheet("市场活动列表");

        //创建行对象,第一行表头数据
        Row row = sheet.createRow(0);

        /*
        id
        owner
        name
        startDate
        endDate
        cost
        description
        createTime
        createBy
        editTime
        editBy
     */
        row.createCell(0).setCellValue("唯一标识");
        row.createCell(1).setCellValue("所有者");
        row.createCell(2).setCellValue("名称");
        row.createCell(3).setCellValue("开始时间");
        row.createCell(4).setCellValue("结束时间");
        row.createCell(5).setCellValue("成本");
        row.createCell(6).setCellValue("描述");
        row.createCell(7).setCellValue("创建人");
        row.createCell(8).setCellValue("创建时间");
        row.createCell(9).setCellValue("修改人");
        row.createCell(10).setCellValue("修改时间");

        for(int i=0; i<activityList.size(); i++){

            Row r = sheet.createRow(i + 1);

            Activity a = activityList.get(i);

            r.createCell(0).setCellValue(a.getId());
            r.createCell(1).setCellValue(a.getOwner());
            r.createCell(2).setCellValue(a.getName());
            r.createCell(3).setCellValue(a.getStartDate());
            r.createCell(4).setCellValue(a.getEndDate());
            r.createCell(5).setCellValue(a.getCost());
            r.createCell(6).setCellValue(a.getDescription());
            r.createCell(7).setCellValue(a.getCreateBy());
            r.createCell(8).setCellValue(a.getCreateTime());
            r.createCell(9).setCellValue(a.getEditBy());
            r.createCell(10).setCellValue(a.getEditTime());

        }

        //设置响应头数据,告诉浏览器,我现在进行的是下载操作,并且已经指定好了文件名称了
        //设置响应头,响应的数据类型为流类型
        response.setContentType("octets/stream");
        //设置响应的文件名称
        response.setHeader("Content-Disposition","attachment;filename=Activity-"+DateTimeUtil.getSysTime()+".xls");

        //将数据封装到工作簿对象中后,通过response对象响应到浏览器中
        ServletOutputStream out = response.getOutputStream();

        workbook.write(out);
    }

    /**
     * 选择导出
     * @param activityIds
     * @param response
     * @throws TranditionRequestException
     * @throws IOException
     */
    @RequestMapping(value = "/exportActivityXz.do")
    public void exportActivityXz(String[] activityIds,HttpServletResponse response) throws TranditionRequestException, IOException {

        List<Activity> activityList = activityService.exportActivityByIds(activityIds);

        if (activityList.size()==0){
            throw new TranditionRequestException("当前市场活动无数据");
        }

        exportActivity(response,activityList);
    }

    /**
     * 市场活动详情页面
     * @param id
     * @param model
     * @return
     * @throws TranditionRequestException
     */
    @RequestMapping(value = "/toDetail.do")
    public String toDetail(String id,Model model) throws TranditionRequestException {
        //根据id查询市场活动数据
        Activity activity = activityService.findActivityById(id);

        if (ObjectUtils.isEmpty(activity))
            throw new TranditionRequestException("查询异常...");

        model.addAttribute("activity",activity);
        return "/workbench/activity/detail";
    }

    /**
     * 加载备注信息
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/getActivityRemarkList.do")
    @ResponseBody
    public R getActivityRemarkList(String activityId){
        //查询备注信息列表
        List<ActivityRemark> activityRemarkList = activityService.findActivityRemarkList(activityId);

        return R.ok(
                0,
                ObjectUtils.isEmpty(activityRemarkList)?"当前市场活动没有备注信息列表":"查询成功",
                activityRemarkList
                    );
    }

    /**
     * 新增备注信息
     * @param activityId
     * @param noteContent
     * @param session
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/remark/saveActivityRemark.do")
    @ResponseBody
    public R saveActivityRemark(String activityId,String noteContent,HttpSession session) throws AjaxRequestException {
        //获取登陆用户
        String createBy = ((User)session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        //添加操作
        boolean flag = activityService.saveActivityRemark(new ActivityRemark()
                .setId(UUIDUtil.getUUID())
                .setNoteContent(noteContent)
                .setCreateBy(createBy)
                .setCreateTime(createTime)
                .setEditBy(createBy)
                .setEditTime(createTime)
                .setEditFlag("0")
                .setActivityId(activityId)
        );

        if (!flag)
            throw new AjaxRequestException("新增失败，请刷新后再试...");

        return R.ok();
    }

    /**
     * 更新备注信息
     * @param remarkId
     * @param noteContent
     * @param session
     * @return
     * @throws AjaxRequestException
     */
    @RequestMapping(value = "/remark/updateRemark.do")
    @ResponseBody
    public R updateRemark(String remarkId,String noteContent,HttpSession session) throws AjaxRequestException {
        //获取当前用户
        String editBy = ((User) session.getAttribute("user")).getName();

        String editTime = DateTimeUtil.getSysTime();

        //更新操作
        boolean flag = activityService.updateRemark(remarkId, noteContent, editBy, editTime);

        if (!flag)
            throw new AjaxRequestException("更新失败...");

        return R.ok();
    }

    /**
     * 删除备注信息
     *
     * @param remarkId
     * @return
     */
    @RequestMapping(value = "/remark/deleteRemarkById.do")
    @ResponseBody
    public R deleteRemarkById(String remarkId) throws AjaxRequestException {

        boolean flag = activityService.deleteRemarkById(remarkId);

        if (!flag)
            throw new AjaxRequestException("删除失败");

        return R.ok();
    }
}




