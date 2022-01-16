//全选和全不选
function selectAll() {
    //设置全选框的点击事件
    $("#selectAllBtn").click(function () {
        //获取全选框的选中状态
        var flag = $("#selectAllBtn").prop("checked");
        console.log(flag);
        //判断选中状态
        if (flag){
            //获取name=ck的标签并设置选中
            $("input[name=ck]").prop("checked",true);
        } else {
            $("input[name=ck]").prop("checked",false);
        }
    });
}

//反选
function reverseAll() {
    $("input[name=ck]").click(function () {
        //获取下面复选框的个数
        var cks = $("input[name=ck]");
        //获取下面复选框选中的个数
        var ckcs = $("input[name=ck]:checked");
        //判断是否全选了
        if (cks.length == ckcs.length){
            //如果全选了就选中上面的
            $("#selectAllBtn").prop("checked",true);
        } else {
            $("#selectAllBtn").prop("checked", false);
        }
    });
}

//校验code编码是否能添加
function checkCode() {

    //添加code输入框的离焦事件
    $("#code").blur(function () {

        //检测code是否为空
        var code = $("#code").val().trim();
        if (code == "") {
            //提示不为空
            $("#msg").html("编码不能为空").css("color", "red");
            return;
        }

        //校验通过取消提示信息
        $("#msg").html("");

        //通过异步请求查询code是否重复
        $.ajax({
            url: "settings/dictionary/type/checkCode.do",
            data: {
                "code": code
            },
            type: "POST",
            dataType: "json",
            success: function (data) {
                if (data.code==0){
                    //不重复能插入，清空提示信息
                    $("#msg").html("");
                }else {
                    //重复不能插入
                    $("#msg").html(data.msg).css("color","red");
                }
            }
        })
    });
}

//新增字典类型操作
function saveDictionaryType() {

    //给保存按钮,添加点击事件
    $("#saveDictionaryTypeBtn").click(function () {

        //获取属性信息
        var code = $("#code").val();

        if(code == ""){
            $("#msg").html("编码能为不能为空...").css("color","#FF5555");
            return;
        }

        //校验通过,还需要校验
        var errMsg = $("#msg").html();

        if(errMsg != ""){
            return;
        }

        var name = $("#name").val();

        var description = $("#description").val();

        //校验全部通过,发送ajax请求
        $.ajax({
            url:"settings/dictionary/type/saveDictionaryType.do",
            data:{
                "code":code,
                "name":name,
                "description":description
            },
            type:"POST",
            dataType:"json",
            success:function(data) {
                if(data.code == 0){
                    alert("添加成功！");
                    //新增成功,跳转到字典类型首页面,加载列表数据
                    window.location.href = "settings/dictionary/type/toIndex.do";
                }else{
                    //给出提示信息
                    $("#msg").html(data.msg).css("color","#FF5555");
                }
            }
        })
    })
}

//修改操作
function typeEdit() {
    //给修改按钮,添加点击事件
    $("#typeEditBtn").click(function () {

        //获取选中的复选框,并且只能选中一个
        var cks = $("input[name=ck]:checked");
        if(cks.length != 1){
            //要么没有选中,要么选中了多个
            alert("请选中一条数据");
            return;
        }

        //选中了一条数据,获取它的value属性(类型编码)
        var code = cks[0].value;

        if(code == ""){
            alert("当前页面加载数据异常,请刷新后再试...");
            return;
        }
        //发送传统请求
        window.location.href = "settings/dictionary/type/toEdit.do?code="+code;
    })
}

//更新操作
function updateDictionaryType() {
    //给点击更新添加事件
    $("#updateDictionaryTypeBtn").click(function () {
        //获取三个属性值
        var code = $("#code").val();
        //校验code是否为空
        if (code == ""){
            alert("当前页面数据异常，请刷新后再试...");
        }
        //校验通过
        var name = $("#name").val();
        var description = $("#description").val();

        //发送ajax请求，进行跟新
        $.ajax({
            url:"settings/dictionary/type/updateDictionaryType.do",
            data:{
                "code":code,
                "name":name,
                "description":description
            },
            dataType:"json",
            type:"POST",
            success:function (data) {
                if (data.code == 0) {
                    //更新成功跳转到收页面
                    window.location.href = "settings/dictionary/type/toIndex.do";
                }else {
                    //更新失败
                    $("#msg").html(data.msg);
                }
            }
        })
    })
}

//删除操作
function batchDeleteDictionaryType() {
    //给删除按钮添加点击事件
    $("#batchDeleteDictionaryTypeBtn").click(function () {
        //拿到勾选的复选框
        var cks = $("input[name=ck]:checked");
        //判断是否选择
        if (cks != 0){
            //已选择
            //将codes用&拼接到一起，并放在地址的后面
            var params = "";
            //循环拼接
            for (var i = 0; i < cks.length; i++) {
                params += "codes="+cks[i].value;
                //追加&符号
                if (i<cks.length-1){
                    params += "&";
                }
            }
            //查看追加结果
            //console.log(params);

            //由于删除是危险操作，给出提示信息
            //发送ajax请求

            if (confirm("确定是否删除？")){
                $.ajax({
                    //批量删除，字典类型
                    //url:"settings/dictionary/type/batchDeleteDictionaryType.do?"+params,
                    //批量删除，考虑一对多的关联关系
                    url:"settings/dictionary/type/batchDeleteDictionaryTypeCondition.do?"+params,
                    date:{

                    },
                    dataType:"json",
                    type:"POST",
                    success:function (data) {
                        if (data.code==0){
                            //删除成功，跳转字典类型首页面
                            window.location.href = "settings/dictionary/type/toIndex.do";
                        }else {
                            //删除失败
                            //alert("删除失败，请刷新页面后重试...");
                            //提示用户哪些不能删除
                            alert("以下["+data.data+"]有关联关系,请先解除关联关系再删除...");
                            window.location.href = "settings/dictionary/type/toIndex.do";
                        }
                    }
                })
            }

        }else {
            alert("请选中至少一条数据...");
            return;
        }
    })
}

//加载字典值首页面
function getDictionaryValueList() {
    //发送ajax请求，获取字典值列表数据
    $.ajax({
        url:"settings/dictionary/value/getDictionaryValueList.do",
        data:{

        },
        dataType:"json",
        type:"POST",
        success:function (data) {
            //data{code:0/1,msg:xxx,data:({})}
            if (data.code == 0){
                //查询成功，异步加载
                //设置空字符串标签
                var html = "";
                //遍历数据
                //第一个参数是数据，第二个function是加载的方法
                //function里第一个参数是索引值从0开始，第二个参数是每个数据（一个对象）
                $.each(data.data,function (i,n) {
                       html += '<tr class="'+(i%2==0?'active':'')+'">';
                       html += '<td><input type="checkbox" name="ck"/></td>';
                       html += '<td>'+(i+1)+'</td>';
                       html += '<td>'+n.value+'</td>';
                       html += '<td>'+n.text+'</td>';
                       html += '<td>'+n.orderNo+'</td>';
                       html += '<td>'+n.typeCode+'</td>';
                       html += '</tr>';
                });
                console.log(html);
                //获取加载页面的容器
                $("#dictionaryValueList").html(html);
            } else {
                //查询失败，返回提示信息
                alert(data.msg);
            }
        }
    })
}

//Value全选
function selectValueAll() {
    $("#selectValueAllBtn").click(function () {
        //jQuery的方法
        //$("input[name=ck]").prop("checked", $(this).prop("checked"))
        //dom对象的方法
        $("input[name=ck]").prop("checked", this.checked);
    });
}

//Value反选
function reverseValueAll() {
    //之前我们通过给所有的复选框进行绑定点击事件来实现的
    //现在就无法实现了,因为这种方式需要通过页面中的标签来绑定
    // $("input[name=ck]").click(function () {
    //     alert("123")
    // })
    //但是现在标签,在js代码中进行异步加载完成
    //通过它的页面中的父标签来给子标签绑定事件完成
    //父标签通过on方法来给子标签绑定事件
    //参数1,事件名称
    //参数2,绑定的对象(子标签)
    //参数3,点击后执行的方法

    //通过父标签给子标签添加属性
    //第一个属性是：添加事件
    //第二个属性是：绑定的对象
    //第三个属性是：点击后执行的方法
    $("#dictionaryValueList").on("click","input[name=ck]",function () {
        $("#selectValueAllBtn").prop("checked",
            $("input[name=ck]").length == $("input[name=ck]:checked").length)
    })
}