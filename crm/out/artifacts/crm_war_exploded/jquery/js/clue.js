/**
 * 查询所有数据（未分页）
 */
function getClueList() {
    //发送ajax请求
    $.ajax({
        url:"workbench/clue/getClueList.do",
        data:{

        },
        dataType:"json",
        type:"POST",
        success:function (data) {
            if (data.code==0){
                var html = "";
                //动态加载数据
                $.each(data.data,function (i,n) {
                        html += '<tr class="'+(i%2==0?'active':'')+'">';
                        html += '<td><input type="checkbox" name="ck"/></td>';
                        html += '<td><a style="text-decoration: none; cursor: pointer;">'+n.fullname+'</a></td>';
                        html += '<td>'+n.company+'</td>';
                        html += '<td>'+n.phone+'</td>';
                        html += '<td>'+n.mphone+'</td>';
                        html += '<td>'+n.source+'</td>';
                        html += '<td>'+n.owner+'</td>';
                        html += '<td>'+n.state+'</td>';
                });
                $("#clueListBody").html(html);
            }else {
                alert("查询失败");
            }
        }
    })
}

/**
 * 全选
 */
function selectAll() {
    $("#selectAllBtn").click(function () {
        $("input[name=ck]").prop("checked",this.checked)
    })
}

/**
 * 反选
 */
function reverseAll() {
    $("#clueListBody").on("click","input[name=ck]",function () {
        $("#selectAllBtn").prop(
            "checked",
            $("input[name=ck]").length == $("input[name=ck]:checked").length
        )
    })
}

/**
 * 分页查询 + 分页组件加载
 * @param pageNo
 * @param pageSize
 */
function getClueListByPage(pageNo,pageSize) {
    $.ajax({
        url:"workbench/clue/getClueListByPage.do",
        data:{
            "pageNo":pageNo,
            "pageSize":pageSize
        },
        type:"POST",
        dataType:"json",
        success:function(data) {
            //未加载分页组件:
            //data:{code:0/1,msg:xxx,data:[{市场活动}]}
            //加载分页组件:
            //data:{pageNo:xxx,pageSize:xxx,data:[{市场活动}...]...}
            if(data.code == 0){
                //查询成功,异步加载
                var html = "";

                $.each(data.data,function (i, n) {
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="ck"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;">'+n.fullname+'</a></td>';
                    html += '<td>'+n.company+'</td>';
                    html += '<td>'+n.phone+'</td>';
                    html += '<td>'+n.mphone+'</td>';
                    html += '<td>'+n.source+'</td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.state+'</td>';
                });

                $("#clueListBody").html(html);

                //当异步数据加载完成后,初始化页面的分页组件
                $("#cluePage").bs_pagination({
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

                    //当在组件中点击了,分页按钮或页码的按钮或跳转到第几页,都会执行下面的回调方法
                    onChangePage : function(event, data){
                        //回调自己的分页加载组件的方法
                        getClueListByPage(data.currentPage , data.rowsPerPage);
                    }
                });
            }else{
                alert(data.msg);
            }
        }
    })
}

/**
 * 查询
 */
function searchCluList() {
    $("#searchCluListBtn").click(function () {
        //获取所有的查询条件
        var name = $("#search-fullname").val();
        var company = $("#search-company").val();
        var phone = $("#search-phone").val();
        var source = $("#search-source option:selected").val();
        var owner = $("#search-owner").val();
        var mphone = $("#search-mphone").val();
        var state = $("#search-state option:selected").val();

        //将条件封装到隐藏域中
        $("#hidden-name").val(name);
        $("#hidden-company").val(company);
        $("#hidden-phone").val(phone);
        $("#hidden-source").val(source);
        $("#hidden-owner").val(owner);
        $("#hidden-mphone").val(mphone);
        $("#hidden-state").val(state);

        //调用分页功能
        getClueListByPageCondition(1,2)
    })
}

/**
 * 分页+分页组件+条件过滤查询
 * @param pageNo
 * @param pageSize
 */
function getClueListByPageCondition(pageNo,pageSize) {
    //获取所有的查询条件
    var name = $("#hidden-name").val();
    var company = $("#hidden-company").val();
    var phone = $("#hidden-phone").val();
    var source = $("#hidden-source").val();
    var owner = $("#hidden-owner").val();
    var mphone = $("#hidden-mphone").val();
    var state = $("#hidden-state").val();

    $.ajax({
        url:"workbench/clue/getClueListByPageCondition.do",
        data:{
            "pageNo":pageNo,
            "pageSize":pageSize,
            "name":name,
            "company":company,
            "phone":phone,
            "source":source,
            "owner":owner,
            "mphone":mphone,
            "state":state
        },
        type:"POST",
        dataType:"json",
        success:function(data) {
            //未加载分页组件:
            //data:{code:0/1,msg:xxx,data:[{市场活动}]}
            //加载分页组件:
            //data:{pageNo:xxx,pageSize:xxx,data:[{市场活动}...]...}
            if(data.code == 0){
                //查询成功,异步加载
                var html = "";

                $.each(data.data,function (i, n) {
                    html += '<tr class="active">';
                    html += '<td><input value="'+n.id+'" type="checkbox" name="ck"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;">'+n.fullname+'</a></td>';
                    html += '<td>'+n.company+'</td>';
                    html += '<td>'+n.phone+'</td>';
                    html += '<td>'+n.mphone+'</td>';
                    html += '<td>'+n.source+'</td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.state+'</td>';
                });

                $("#clueListBody").html(html);

                //当异步数据加载完成后,初始化页面的分页组件
                $("#cluePage").bs_pagination({
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

                    //当在组件中点击了,分页按钮或页码的按钮或跳转到第几页,都会执行下面的回调方法
                    onChangePage : function(event, data){
                        //回调自己的分页加载组件的方法
                        getClueListByPageCondition(data.currentPage , data.rowsPerPage);
                    }
                });
            }else{
                alert(data.msg);
            }
        }
    })
}
/**
 * 打开新增模态窗口
 */
function openCreateClueModal() {
    $("#openCreateClueModalBtn").click(function () {
        //发送ajax请求，动态加载数据
        $.ajax({
            url:"settings/user/getUserList.do",
            data:{

            },
            dataType:"json",
            type:"POST",
            success:function (data) {
                if (data.code==0){
                    var html = "";

                    $.each(data.data,function (i,n) {
                        html += "<option value="+n.id+">"+n.name+"</option>"
                    });

                    //回显数据
                    $("#create-owner").html(html);

                    //默认选择当前登陆用户
                    $("#create-owner").val($("#userId").val());

                    //打开模态窗口
                    $("#createClueModal").modal("show");
                }
            }
        })
    })
}

/**
 * 日历控件
 */
function initDateTimePicker() {
    $(".time").datetimepicker({
        minView: "month",
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        pickerPosition: "top-left"
    });
}

/**
 * 新增操作
 */
function saveClue() {
    $("#saveClueBtn").click(function () {
        //获取用户输入的值
        var owner = $("#create-owner option:selected").val();
        var company = $("#create-company").val();
        var appellation = $("#create-appellationList option:selected").val();
        var fullname = $("#create-fullname").val();
        var job = $("#create-job").val();
        var email = $("#create-email").val();
        var phone = $("#create-phone").val();
        var website = $("#create-website").val();
        var mphone = $("#create-mphone").val();
        var state = $("#create-state").val();
        var source = $("#create-source").val();
        var description = $("#create-description").val();
        var contactSummary = $("#create-contactSummary").val();
        var nextContactTime = $("#create-nextContactTime").val();
        var address = $("#create-address").val();

        //校验数据
        if (owner==""){
            alert("所有者不能为空...");
            return;
        }
        if (company==""){
            alert("公司不能为空...");
            return;
        }
        if (fullname==""){
            alert("姓名不能为空...");
            return;
        }


        //校验通过，发送ajax请求
        $.ajax({
            url:"workbench/clue/saveClue.do",
            data:{
                "owner":owner,
                "company":company,
                "appellation":appellation,
                "fullname":fullname,
                "job":job,
                "email":email,
                "phone":phone,
                "website":website,
                "mphone":mphone,
                "state":state,
                "source":source,
                "description":description,
                "contactSummary":contactSummary,
                "nextContactTime":nextContactTime,
                "address":address
            },
            dataType:"json",
            type:"POST",
            success:function (data) {
                if (data.code==0){
                    //添加成功
                    //刷新列表
                    getClueListByPageCondition(1,2);
                    //关闭模态窗口
                    $("#createClueModal").modal("hide");
                } else {
                    alert("添加失败...");
                }
            }
        })

    })
}

/**
 * 打开修改模态窗口(回显数据)
 */
function openEditClueModal() {
    $("#openEditClueModalBtn").click(function () {
        //判断用户是否点击复选框
        var cks = $("input[name=ck]:checked");

        //校验
        if (cks.length != 1) {
            alert("请选中一条进行修改...");
            return;
        }

        //读取用户点击的id
        var clueId = cks[0].value;
        alert(clueId);

        if (clueId==""){
            alert("当前页面异常，请刷新后再试...");
        }

        //校验通过
        //发送ajax请求，动态加载数据
        $.ajax({
            url: "settings/user/getUserList.do",
            data: {},
            dataType: "json",
            type: "POST",
            success: function (data) {
                if (data.code == 0) {
                    var html = "";

                    $.each(data.data, function (i, n) {
                        html += "<option value='"+n.id+"'>"+n.name+"</option>";
                    });

                    //回显数据
                    $("#edit-owner").html(html);

                    //再次发送ajax请求，根据clueId查询线索数据
                    $.ajax({
                        url:"workbench/clue/getClueByClueId.do",
                        data:{
                            "id":clueId
                        },
                        dataType:"json",
                        type:"POST",
                        success:function (data) {

                            if (data.code==0){
                                //查询成功

                                //回显数据
                                $("#edit-owner").val(data.data.owner);
                                $("#edit-company").val(data.data.company);
                                $("#edit-appellation").val(data.data.appellation);
                                $("#edit-fullname").val(data.data.fullname);
                                $("#edit-job").val(data.data.job);
                                $("#edit-email").val(data.data.email);
                                $("#edit-phone").val(data.data.phone);
                                $("#edit-website").val(data.data.website);
                                $("#edit-mphone").val(data.data.mphone);
                                $("#edit-state").val(data.data.state);
                                $("#edit-source").val(data.data.source);
                                $("#edit-description").val(data.data.description);
                                $("#edit-contactSummary").val(data.data.contactSummary);
                                $("#edit-nextContactTime").val(data.data.nextContactTime);
                                $("#edit-address").val(data.data.address);

                                //将id存储到隐藏域中
                                $("#clueId").val(clueId);

                                //打开模态窗口
                                $("#editClueModal").modal("show");
                            }

                        }
                    });

                }
            }
        })
    });
}

/**
 * 修改线索功能(按钮)
 */
function editClue() {
    $("#editClueBtn").click(function () {
        //读取用户输入的值

        //获取用户输入的值
        var owner = $("#edit-owner ").val();
        var company = $("#edit-company").val();
        var appellation = $("#edit-appellation option:selected").val();
        var fullname = $("#edit-fullname").val();
        var job = $("#edit-job").val();
        var email = $("#edit-email").val();
        var phone = $("#edit-phone").val();
        var website = $("#edit-website").val();
        var mphone = $("#edit-mphone").val();
        var state = $("#edit-state").val();
        var source = $("#edit-source").val();
        var description = $("#edit-description").val();
        var contactSummary = $("#edit-contactSummary").val();
        var nextContactTime = $("#edit-nextContactTime").val();
        var address = $("#edit-address").val();
        var clueId = $("#clueId").val();

        alert(clueId);

        //校验数据
        if (owner==""){
            alert("所有者不能为空...");
            return;
        }
        if (company==""){
            alert("公司不能为空...");
            return;
        }
        if (fullname==""){
            alert("姓名不能为空...");
            return;
        }


        //校验通过，发送ajax请求
        $.ajax({
            url:"workbench/clue/editClue.do",
            data:{
                "owner":owner,
                "company":company,
                "appellation":appellation,
                "fullname":fullname,
                "job":job,
                "email":email,
                "phone":phone,
                "website":website,
                "mphone":mphone,
                "state":state,
                "source":source,
                "description":description,
                "contactSummary":contactSummary,
                "nextContactTime":nextContactTime,
                "address":address,
                "id":clueId
            },
            dataType:"json",
            type:"POST",
            success:function (data) {
                if (data.code==0){
                    //添加成功
                    //刷新列表
                    getClueListByPageCondition(1,2);

                    alert("更新完成");
                    //关闭模态窗口
                    $("#editClueModal").modal("hide");
                } else {
                    alert("添加失败...");
                }
            }
        })
    })
}

/**
 * 删除操作
 *
 */
function batchDeleteClueById() {
    $("#batchDeleteClueBtn").click(function () {
        //根据复选框获取id
        var cks = $("input[name=ck]:checked");

        //校验
        if (cks.length==0){
            alert("请选中所要删除的数据");
            return;
        }

        //校验通过,拼接参数
        var clueIds = "";

        for (var i = 0; i <cks.length ; i++) {
            clueIds += "clueIds="+cks[i].value;

            if (i<cks.length-1)
                clueIds += "&";
        }
        console.log(clueIds);

        if (confirm("您确定要删除吗？")){
            //发送ajax请求
            $.ajax({
                url:"workbench/clue/batchDeleteClueById.do?"+clueIds,
                data:{

                },
                dataType:"json",
                type:"POST",
                success:function (data) {
                    if (data.code==0){
                        alert("删除成功...");
                        //刷新页面
                        getClueListByPageCondition(1,2);
                    } else {
                        alert("删除失败,请刷新页面后再试...");
                    }
                }
            })
        }

    })
}

function ff() {
    //发送ajax请求
    $.ajax({
        url:"",
        data:{

        },
        dataType:"json",
        type:"POST",
        success:function (data) {

        }
    })
}