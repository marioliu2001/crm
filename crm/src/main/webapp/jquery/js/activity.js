//加载数据
function getActivityList() {

    //ajax请求
    $.ajax({
        url:"workbench/activity/getActivityList.do",
        data:{

        },
        dataType:"json",
        type:"POST",
        success:function (data) {
            if (data.code==0){
                //加载成功
                //异步加载
                var html = "";

                $.each(data.data,function (i,n) {
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" /></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.jsp\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '</tr>';
                })
                $("#activityListBody").html(html);
            } else {
                //加载失败
                alert(data.msg);
            }
        }
    })
}

/**
 * 分页查询
 * @param pageNo当前页
 * @param PageSize每页条数
 */
//加载数据
function getActivityListByPage(pageNo,pageSize) {
    //ajax请求
    $.ajax({
        url:"workbench/activity/getActivityListByPage.do",
        data:{
            "pageNo":pageNo,
            "pageSize":pageSize
        },
        dataType:"json",
        type:"POST",
        success:function (data) {
            if (data.code==0){
                //加载成功
                //异步加载
                var html = "";

                $.each(data.data,function (i,n) {
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="ck"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.jsp\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '</tr>';
                })
                $("#activityListBody").html(html);
            } else {
                //加载失败
                alert(data.msg);
            }
        }
    })
}

/**
 * 全选和全部选
 */
function selectActivityAll() {

    //获取最上面的按钮的点击事件
    $("#selectActivityAllBtn").click(function () {
        //给子标签添加属性和事件
        $("input[name=ck]").prop("checked",this.checked);
    })
}

/**
 * 反选
 */
function reverseActivityAll() {
    //给子标签添加属性和事件
    $("#activityListBody").on("click","input[name=ck]",function () {
        //设置最上面复选框的状态（根据下面复选框的个数和选中个数）
        $("#selectActivityAllBtn").prop("checked",
            $("input[name=ck]").length == $("input[name=ck]:checked").length);
    });
}

/**
 * 分页查询 + 分页组件的加载
 * @param pageNo
 * @param pageSize
 */
function getActivityListByPageComponent(pageNo,pageSize) {
    //ajax请求
    $.ajax({
        url:"workbench/activity/getActivityListByPageComponent.do",
        data:{
            "pageNo":pageNo,
            "pageSize":pageSize
        },
        dataType:"json",
        type:"POST",
        success:function (data) {
            if (data.code==0){
                //加载成功
                //异步加载
                //加载分页组件
                //data:{pageNo:xxx,pageSize:xxx,data[{市场活动}...]...}
                var html = "";

                $.each(data.data,function (i,n) {
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="ck"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.jsp\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '</tr>';
                })
                $("#activityListBody").html(html);

                //当数据异步加载完成后，初始化页面分页组件

                $("#activityPage").bs_pagination({
                    currentPage: data.pageNo, // 页码
                    rowsPerPage: data.pageSize, // 每页显示的记录条数
                    maxRowsPerPage: data.maxRowsPerPage, // 每页最多显示的记录条数
                    totalPages: data.totalPages, // 总页数
                    totalRows: data.totalCounts, // 总记录条数

                    visiblePageLinks: data.visiblePageLinks, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    //当在组件中点击了，分页按钮或者页码按钮或跳转到第几页，都会执行下面的回调方法
                    onChangePage : function(event, data){
                        //回调自己的分页加载组件的方法
                        getActivityListByPageComponent(data.currentPage , data.rowsPerPage);
                    }
                });

            } else {
                //加载失败
                alert(data.msg);
            }
        }
    })
}

/**
 * 日期样式
 */
function initDateTimePicker() {
    $(".time").datetimepicker({
        minView: "month",
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left"
    });
}

/**
 * 查询按钮
 */
function searchActivityCondition() {
    //查询按钮
    $("#searchActivityBtn").click(function () {

        //将查询的条件封装到隐藏域
        var name = $("#search-name").val();
        var owner = $("#search-owner").val();
        var startDate = $("#search-startDate").val();
        var endDate = $("#search-endDate").val();

        $("#hidden-name").val(name);
        $("#hidden-owner").val(owner);
        $("#hidden-startDate").val(startDate);
        $("#hidden-endDate").val(endDate);

        //获取查询的条件，可以将它封装到分页查询的方法中
        //可以直接调用分页查询方法即可
        getActivityListByPageComponentCondition(1,1);
    })
}

/**
 * 分页查询 + 分页组件的加载 + 条件过滤查询
 * @param pageNo
 * @param pageSize
 */
function getActivityListByPageComponentCondition(pageNo,pageSize) {
    //获取条件过滤查询的属性
    //var activityName = $("#search-name").val();
    //var username = $("#search-owner").val();
    //var startDate = $("#search-startDate").val();
    //var endDate = $("#search-endDate").val();

    //从隐藏域中获得条件 查询的数据
    var activityName = $("#hidden-name").val();
    var username = $("#hidden-owner").val();
    var startDate = $("#hidden-startDate").val();
    var endDate = $("#hidden-endDate").val();

    //alert("getActivityListByPageComponentCondition加载成功");

    //ajax请求
    $.ajax({
        url:"workbench/activity/getActivityListByPageComponentCondition.do",
        data:{
            "pageNo":pageNo,
            "pageSize":pageSize,
            "activityName":activityName,
            "username":username,
            "startDate":startDate,
            "endDate":endDate
        },
        dataType:"json",
        type:"POST",
        success:function (data) {
            if (data.code==0){
                //加载成功
                //异步加载
                //加载分页组件
                //data:{pageNo:xxx,pageSize:xxx,data[{市场活动}...]...}
                var html = "";

                $.each(data.data,function (i,n) {
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="ck"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.jsp\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '</tr>';
                })
                $("#activityListBody").html(html);

                //当数据异步加载完成后，初始化页面分页组件

                $("#activityPage").bs_pagination({
                    currentPage: data.pageNo, // 页码
                    rowsPerPage: data.pageSize, // 每页显示的记录条数
                    maxRowsPerPage: data.maxRowsPerPage, // 每页最多显示的记录条数
                    totalPages: data.totalPages, // 总页数
                    totalRows: data.totalCounts, // 总记录条数

                    visiblePageLinks: data.visiblePageLinks, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    //当在组件中点击了，分页按钮或者页码按钮或跳转到第几页，都会执行下面的回调方法
                    onChangePage : function(event, data){
                        //回调自己的分页加载组件的方法
                        getActivityListByPageComponentCondition(data.currentPage , data.rowsPerPage);
                    }
                });

            } else {
                //加载失败
                alert("无此数据");
            }
        }
    })
}

/**
 * 打开模态窗口
 */
function openCreateActivityModal() {
    //点击创建按钮，加载所有用户下拉表，打开模态窗口
    $("#openCreateActivityModalBtn").click(function () {
        $.ajax({
            url:"settings/user/getUserList.do",
            data:{

            },
            dataType:"json",
            type:"POST",
            success:function (data) {
                //返回用户
                if (data.code==0){
                    //异步加载，所有者下拉列表数据
                    var html = "";

                    $.each(data.data,function (i, n) {
                        html += "<option value='"+n.id+"'>"+n.name+"</option>"
                    });

                    $("#create-owner").html(html);

                    console.log(html);

                    //设置默认选中当前登录的用户
                    $("#create-owner").val($("#userId").val());

                    //打开模态窗口
                    //页面定义打开模态窗口: data-toggle="modal" data-target="#createActivityModal"
                    //页面定义关闭模态窗口: data-dismiss="modal"
                    //js代码打开模态窗口:
                    $("#createActivityModal").modal("show");
                    //js代码关闭模态窗口:
                    //$("#createActivityModal").modal("hide");

                }else {
                    alert("查询失败")
                }
            }
        })
    });
}

/**
 * 新增操作
 * @constructor
 */
function saveActivity() {
    $("#saveActivityBtn").click(function () {
        //获取数据
        var owner = $("#create-owner").val();
        if (owner==""){
            alert("请选择所有者...");
            return;
        }
        var name = $("#create-name").val();
        if (name==""){
            alert("请选择名称...");
            return;
        }
        var startDate = $("#create-startDate").val();
        var endDate = $("#create-endDate").val();
        var cost = $("#create-cost").val();
        var description = $("#create-description").val();

        //校验通过发送ajax请求

        $.ajax({
            url:"workbench/activity/saveActivity.do",
            data:{
                "owner":owner,
                "name":name,
                "startDate":startDate,
                "endDate":endDate,
                "cost":cost,
                "description":description,
            },
            dataType:"json",
            type:"POST",
            success:function (data) {
                if (data.code==0){
                    //添加成功
                    getActivityListByPageComponentCondition(1,1);
                    $("#createActivityModal").modal("hide");
                }else {
                    alert("添加失败,请刷新后再试...");

                }
            }
        })
    })
}