// 初始化专业信息
let majorList = [];

getMainOrg();

$.ajax({
    url: backgroundURL + "major/getAll",
    type: "get",
    data: {},
    dataType: "json",
    success: function (res) {

        majorList = res.data;

        // 创建文档片段，文档片段的作用就是让for循环中创建的标签先放到文档片段中，待for循环结束后直接将文档片段插入制定的标签元素内，可以减少dom的操作
        let frag = document.createDocumentFragment();
        let major = document.getElementById("major");
        for (let i = 0; i < majorList.length; i++) {
            let option = document.createElement("option");
            option.value = majorList[i].majorId;
            option.setAttribute("name", majorList[i].majorName);
            option.innerHTML = majorList[i].majorName;
            frag.appendChild(option);
        }
        major.appendChild(frag);
    }
})

// 初始化班级信息
let major = document.getElementById("major");
major.onchange = function () {

    let classSel = document.getElementById("classNum");
    classSel.innerHTML = '';

    if (major.value == '') {
        let option = document.createElement("option");
        option.value = '';
        option.innerHTML = '<option value="">请选择</option>';
        classSel.appendChild(option);
        return;
    }

    let value = major.value;
    let classNum = 0;
    for (let i = 0; i < majorList.length; i++) {
        if (majorList[i].majorId == value) {
            classNum = majorList[i].classNum;
            break;
        }
    }

    let frag = document.createDocumentFragment();
    for (let i = 1; i <= classNum; i++) {
        let option = document.createElement("option");
        option.value = i;
        option.setAttribute("name", i);
        option.innerHTML = i + '班';
        frag.appendChild(option);
    }
    classSel.appendChild(frag);
};

// 初始化志愿信息
function getMainOrg() {
    return new Promise(resolve => {
        $.ajax({
            url: backgroundURL + "org/getMain",
            type: "get",
            data: {},
            dataType: "json",
            async: true,
            success: function (res) {
                let orgList = res.data;
                let orgElements = document.getElementsByClassName("org-plug");
                for (let j = 0; j < orgElements.length; j++) {
                    let frag = document.createDocumentFragment();
                    for (let i = 0; i < orgList.length; i++) {
                        let option = document.createElement("option");
                        option.value = orgList[i].orgId;
                        option.setAttribute("name", orgList[i].orgName);
                        option.innerHTML = orgList[i].orgName;
                        frag.appendChild(option);
                    }
                    orgElements[j].appendChild(frag);
                }
                resolve(true);
            }
        })
    })
}

// 选择第一志愿组织时进行联动
let selectFirst = document.getElementById("firstOrg");
selectFirst.onchange = async function () {
    let value = selectFirst.value;
    let val = document.getElementById("firstBra");
    await selectWill(value, val);
};

// 选择第二志愿组织时进行联动
let selectSecond = document.getElementById("secondOrg");
selectSecond.onchange = async function () {
    let value = selectSecond.value;
    let val = document.getElementById("secondBra");
    await selectWill(value, val);
};

let selectWill = function (value, val) {
    return new Promise(resolve => {
        val.innerHTML = '';

        if (value == '' || value == undefined) {
            let option = document.createElement("option");
            option.value = '';
            option.innerHTML = '<option value="">请选择</option>';
            val.appendChild(option);
            return;
        }

        $.ajax({
            url: backgroundURL + "org/getBranch",
            type: "get",
            async: false,
            data: {
                orgId: value
            },
            dataType: "json",
            success: function (res) {
                let branchList = res.data;
                let frag = document.createDocumentFragment();

                for (let i = 0; i < branchList.length; i++) {
                    let option = document.createElement("option");
                    option.value = branchList[i].branchId;
                    option.setAttribute("name", branchList[i].branchName);
                    option.innerHTML = branchList[i].branchName;
                    frag.appendChild(option);
                }
                val.appendChild(frag);
                resolve(true);
            }
        })
    })

};

/**
 * 点击注册按钮
 */
$("#regist").click(function () {
    // 判断调剂是否勾选
    var isAgreeItem = false;
    if (document.getElementById("isAgreeItem").checked) {
        isAgreeItem = true;
    }

    // 单击注册按钮时触发ajax事件 跳转至doRegistServlet
    $.ajax({
        url: backgroundURL + "report/insert",
        type: "post",
        data: {
            stuId: $('#stuId').val(),                        //获取学号
            stdName: $("input[name=stdName]").val(),         //获取姓名
            majorId: $('#major').val(),                      //获取专业id
            classNum: $('#classNum').val(),                  //获取班级
            stdQQ: $("input[name=stdQQ]").val(),             //获取QQ号
            stdPhone: $("input[name=stdPhone]").val(),       //获取手机号码
            firstWill: $('#firstBra').val(),                 //获取第一志愿分支id
            firstReason: $('#firstReason').val(),            //获取第一志愿理由
            secondWill: $('#secondBra').val(),               //获取第二志愿分支id
            secondReason: $('#secondReason').val(),          //获取第二志愿理由
            code: $("input[id=code]").val(),                 //使用id=... 和 name = ...均可
            isDispensing: isAgreeItem                        //是否调剂
        },
        dataType: "json",
        success: function (result) {
            var flag = result.code;

            //初始化错误信息
            $(".tips").text("");

            if (flag == 0) {
                window.alert("填报志愿成功！");
                window.location.href = "https://hao.515code.com/";
                return;
            }

            if (flag == 1 || $("input[id=code]").val() == "") {
                $("#kaptchaError").text("验证码错误");
            } else if (flag == 2) {
                $('#firstWillTip').text("请完整填写第一志愿组织信息");
            } else if (flag == 3) {
                alert("你已提交过志愿信息！若需修改请联系管理员QQ798998087");
            } else if (flag == 4) {
                alert("请输入姓名");
                $('#stuNameTip').text("请填写姓名");
            } else if (flag == 5) {
                alert("请填写专业与班级");
                $('#majorTip').text("请填写专业与班级");
            } else if (flag == 6) {
                $("#kaptchaError").text("请刷新验证码");
            }
        },
        // 失败回调，初始化错误信息
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // 服务端没处理的异常
            $(".tips").text("");
            if ($('#stuId').val() <= 20180000 || $('#stuId').val() >= 20219999 || $('#stuId').val() == "") {
                alert("请输入正确的学号");
                $('#stuIdTip').text("学号应为2018-2021开头8位");
            }
            // 检查正则表达式
            if (!isPhone($('#stdPhone').val()) || !isQQ($('#stdQQ').val())) {
                $('#phoneTip').text("请同时填写正确的手机号与QQ号，方便我们联系！");
            }
        }
    })
});
