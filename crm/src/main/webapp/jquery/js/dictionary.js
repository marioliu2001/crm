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
                    alert("编码不存在可以插入");
                    $("#msg").html(data.msg);
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