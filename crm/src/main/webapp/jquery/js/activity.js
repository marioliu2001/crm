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
                });
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
                });
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
        getActivityListByPageComponentCondition(1,2);
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
                    html += '<td><input type="checkbox" value="'+n.id+'" name="ck"/></td>';
                    html += '<td><a id="n_+'+n.id+'" style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/toDetail.do?id='+n.id+'\';">'+n.name+'</a></td>';
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
 * 打开新增模态窗口
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
                    getActivityListByPageComponentCondition(1,2);
                    $("#createActivityModal").modal("hide");
                }else {
                    alert("添加失败,请刷新后再试...");

                }
            }
        })
    })
}

/**
 * 修改操作-两次请求
 */
function openEditActivity() {
    //修改按钮
    $("#openEditActivityBtn").click(function () {
        //判断用户是否选中一个进行修改
        var cks = $("input[name=ck]:checked");
        if (cks.length != 1){
            alert("请选中一条进行修改...");
            return;
        }
        //选中一条
        //获取选中的唯一标识
        var activityId = cks[0].value;
        alert(activityId);
        //判断

        if (activityId == ""){
            alert("当前页面异常，请刷新后再试...");
            return;
        }

        //发送ajax请求
        $.ajax({
            url:"settings/user/getUserList.do",
            data:{

            },
            dataType:"json",
            type:"POST",
            success:function (data) {
                if (data.code==0){
                    //回显数据
                    var html = "";

                    $.each(data.data,function (i,n) {
                        //拼接字符串标签
                        html += "<option value='"+n.id+"'>"+n.name+"</option>"
                    });
                    $("#edit-owner").html(html);
                    console.log(html);


                    //再次发送请求，根据activityId查询此条，市场活动数据
                    $.ajax({
                        url:"workbench/activity/getActivityById.do",
                        data:{
                          "id":activityId
                        },
                        dataType:"json",
                        type:"POST",
                        success:function (data) {
                            if (data.code==0){
                                //查询成功
                                //回显数据
                                $("#edit-name").val(data.data.name);
                                $("#edit-startDate").val(data.data.startDate);
                                $("#edit-endDate").val(data.data.endDate);
                                $("#edit-cost").val(data.data.cost);
                                $("#edit-description").val(data.data.description);

                                //查询时,返回owner为32位的用户的id
                                //在select标签的用户列表中,根据value属性,默认选中所有者
                                $("#edit-owner").val(data.data.owner);

                                //将市场活动id,存入到隐藏域中,方便后续的修改操作
                                $("#edit-id").val(data.data.id);

                                //打开模态窗口
                                $("#editActivityModal").modal("show");
                            }else {
                                alert("操作异常，请刷新后重试...");
                            }
                        }
                    })
                }else {
                    alert("当前页面异常，请刷新后再试...");
                }
            }
        })

    })
}

/**
 * 更新操作
 */
function updateActivity() {
   $("#updateActivityBtn").click(function () {
       //获取信息
       var owner = $("#edit-owner").val();
       var name = $("#edit-name").val();
       var startDate = $("#edit-startDate").val();
       var endDate = $("#edit-endDate").val();
       var cost = $("#edit-cost").val();
       var description = $("#edit-description").val();

       //获取市场活动id
       var id = $("#edit-id").val();

       //校验信息
       if(id == ""){
           alert("当前市场活动数据异常,请刷新后再试...");
           return;
       }

       if (owner==""){
           alert("所有者不能为空...");
           return;
       }

       if (name==""){
           alert("名称不能为空...");
           return;
       }

       //发送ajax请求
       $.ajax({
           url:"workbench/activity/updateActivity.do",
           data:{
               "id":id,
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
               if (data.code==0) {
                   //添加成功,刷新列表
                   //停留在当前页面
                   getActivityListByPageComponentCondition(
                        $("#activityPage").bs_pagination("getOption","currentPage"),
                        $("#activityPage").bs_pagination("getOption","rowsPerPage")
                   )
                   //关闭模态窗口
                   $("#editActivityModal").modal("hide");
               } else {
                   alert("添加失败，请刷新后再试...");
               }
           }
       })
   })
}

/**
 *逻辑删除操作
 */
function batchDeleteActivity() {
    //获取按钮
    $("#batchDeleteActivityBtn").click(function () {
        //获取当前选中的复选框
        var cks = $("input[name=ck]:checked");
        //校验
        if (cks.length==0){
            alert("请选中所要删除的信息");
            return;
        }
        //获取当前选中的id,拼接参数
        var activityIds = "";
        var activityNames = "";

        for (var i = 0; i < cks.length; i++) {

            //拼接当前选中的id
            activityIds += "activityIds=" + cks[i].value;

            //拼接提示信息
            activityNames += $("#n_"+cks[i].value).html();
            alert(activityNames);
            if (i<cks.length-1){
                activityIds += "&";
                activityNames += " , "
            }
        }

        if (confirm("你确定要删除数据吗")){
            //发送ajax请求
            $.ajax({
                url:"workbench/activity/batchDeleteActivity.do?"+activityIds,
                data:{

                },
                dataType:"json",
                type:"POST",
                success:function (data) {
                    if (data.code==0){
                        //删除成功，刷新数据
                        getActivityListByPageComponentCondition(1,2)
                    } else {
                        alert("删除失败，请刷新后重试...");
                    }
                }
            })
        }
    });
}

/**
 * 文件上传
 */
function importActivity() {
    $("#importActivityBtn").click(function () {

        //给导入按钮添加点击事件，点击后提交表单数据
        $("#uploadForm").submit();
    })
}

/**
 * 批量导出
 */
function exportActivityAll() {
    $("#exportActivityAllBtn").click(function () {

        if (confirm("您确定到全部导出吗？")){
            //点击确定，发送传统请求
            window.location.href = "workbench/activity/exportActivityAll.do"
        }
    })
}

/**
 * 选择导出
 */
function exportActivityXz() {
    $("#exportActivityXzBtn").click(function () {
        //选中的复选框
        var cks = $("input[name=ck]:checked");
        //判断
        if (cks.length == 0) {
            alert("请选中要导出的数据");
            return;
        }

        //校验通过
        //拼接参数
        var params = "";
        for (var i = 0; i < cks.length; i++) {
            params += "activityIds="+cks[i].value;
            if (i<cks.length-1)
                params+="&";
        }



        window.location.href="workbench/activity/exportActivityXz.do?"+params;
    })
}

/**
 * 加载备注信息
 */
function getActivityRemarkList() {

    //根据市场活动的id,进行获取列表操作
    var id = $("#activityId").val();

    if(id == ""){
        alert("当前页面数据加载异常,请刷新后再试...");
        return;
    }

    //校验通过
    $.ajax({
        url:"workbench/activity/getActivityRemarkList.do",
        data:{
            "activityId":id
        },
        type:"POST",
        dataType:"json",
        success:function(data) {
            //data : {code:0/1,msg:xxx,data:[{市场活动备注信息}]}
            if(data.code == 0){
                //异步加载市场活动备注信息列表
                var html = "";

                $.each(data.data,function (i, n) {
                    html += '<div class="remarkDiv" style="height: 60px;">';
                    html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                    html += '<div style="position: relative; top: -40px; left: 40px;" >';
                    html += '<h5 id="n_'+n.id+'">'+n.noteContent+'</h5>';
                    html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>'+$("#activityName").val()+'</b> <small style="color: gray;"> '+(n.editFlag==0?n.createTime:n.editTime)+' 由 '+(n.editFlag==0?n.createBy:n.editBy)+'</small>';
                    html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                    html += '<a onclick="openEditRemarkModal(\''+n.id+'\')" class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>';
                    html += '&nbsp;&nbsp;&nbsp;&nbsp;';
                    html += '<a onclick="deleteRemark(\''+n.id+'\')" class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>';
                    html += '</div>';
                    html += '</div>';
                    html += '</div>';
                });
                $("#activityRemarkListBody").html(html);
            }else {
                alert("数据加载异常，请刷新后再试...");
            }
        }
    })
}

/**
 * 恢复备注信息按钮事件
 */
function resetBindingEvent() {
    /*
        鼠标悬停事件
        $(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
        鼠标移出事件
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
        鼠标悬停事件
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
        鼠标移出事件
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
     */

    //通过父标签给子标签添加事件
    $("#activityRemarkListBody").on("mouseover",".remarkDiv",function () {
        $(this).children("div").children("div").show();
    });

    $("#activityRemarkListBody").on("mouseout",".remarkDiv",function () {
        $(this).children("div").children("div").hide();
    });
    $("#activityRemarkListBody").on("mouseover",".myHref",function () {
        $(this).children("span").css("color","red");
    });
    $("#activityRemarkListBody").on("mouseout",".myHref",function () {
        $(this).children("span").css("color","#E6E6E6");
    });
}

/**
 * 新增备注信息
 */
function saveActivityRemark() {
    $("#saveActivityRemarkBtn").click(function () {
        //获得activityId值
        var activityId = $("#activityId").val();

        //获取用户输入备注信息
        var noteContent =  $("#remark").val();

        //校验
        if (activityId==""){
            alert("当前数据异常，请刷新后再试...");
            return;
        }

        if (noteContent==""){
            alert("请输入备注信息再添加...");
            return;
        }

        //校验通过，发送ajax请求
        $.ajax({
            url: "workbench/activity/remark/saveActivityRemark.do",
            data: {
                "activityId": activityId,
                "noteContent":noteContent
            },
            dataType: "json",
            type:"POST",
            success:function (data) {
                if (data.code==0){
                    //新增成功，刷新页面
                    getActivityRemarkList();
                    //清空文本域中的内容
                    $("#remark").val("");
                }else {
                    alert("添加失败，请刷新后再试...");
                }
            }
        });
    })
}

/**
 * 备注信息修改
 */
function openEditRemarkModal(remarkId) {

    if (remarkId==""){
        alert("当前数据加载异常，请刷新后再试...");
        return;
    }

    //存入到隐藏域中
    $("#remarkId").val(remarkId);

    //回显数据
    var noteContent =  $("#n_"+remarkId).html();

    $("#noteContent").val(noteContent);

    //打开模态窗口
    $("#editRemarkModal").modal("show");
}

/**
 * 修改操作
 */
function updateRemark() {
    $("#updateRemarkBtn").click(function () {
        //从隐藏域中获取id
        var remarkId = $("#remarkId").val();

        //获取用户输入的内容
        var newNoteContent = $("#noteContent").val();

        //原来的备注信息
        var oldNoteContent = $("#n_"+remarkId).html();

        //校验用户是否更改信息
        if (newNoteContent == oldNoteContent){
            alert("无信息更改，请检查后再提交");
            return;
        }
        //校验通过，发送ajax请求
        $.ajax({
            url:"workbench/activity/remark/updateRemark.do",
            data:{
                "remarkId":remarkId,
                "noteContent":newNoteContent
            },
            type:"POST",
            dataType:"json",
            success:function (data) {
                if (data.code == 0){
                    //更新成功，刷新列表
                    getActivityRemarkList();
                    //关闭模态窗口
                    $("#editRemarkModal").modal("hide");
                }else {
                    alert("更新失败，刷新后再试...");
                }
            }
        })
    })
}

function deleteRemark(remarkId) {
    if (remarkId==""){
        alert("当前数据异常，请刷新后再试...");
        return;
    }
    if (confirm("您确定要删除这个数据吗？")){
        $.ajax({
            url:"workbench/activity/remark/deleteRemarkById.do",
            data:{
                "remarkId":remarkId
            },
            dataType:"json",
            type:"POST",
            success:function (data) {
                if (data.code==0){
                    //删除成功,刷新列表
                    getActivityRemarkList();
                }else {
                    alert("删除失败，请刷新后再试...");
                }
            }
        })
    }
}

