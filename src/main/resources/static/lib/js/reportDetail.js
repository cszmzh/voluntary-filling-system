async function init() {
    LoginRequired();
    await getMajor();
    await getMainOrg();
    getReportByStuId();
}


/**
 * 根据学号获取志愿信息
 */
function getReportByStuId() {

    // 对普通管理员屏蔽删除志愿按钮
    if (window.localStorage.getItem("status") == 1) {
        $('#deleteReport').attr("style", "display:none");
    }

    // 获取URL中传进来的学生学号
    var searchStuId = getQueryVariable("stuId");

    $.ajax({
        url: backgroundURL + 'report/getByStuId?stuId=' + searchStuId,
        type: 'post',
        datatype: 'json',
        success: function (res) {
            var data = res.data;
            if (data == undefined) {
                window.location.href = "https://www.515code.com/";
            }

            // 添加数据至HTML中
            $('#stuId').append(data.stuId);
            $('#stdName').append(data.stdName);

            // 专业与班级信息
            let majorName = data.major;
            let classNum = data.classNum;
            $("#major").find("option[name='" + majorName + "']").prop("selected", true);
            $("#major").trigger("change");
            $("#classNum").find("option[name='" + classNum + "']").prop("selected", true);

            // 学生手机与QQ号
            document.getElementById("stdPhone").value = data.stdPhone;
            document.getElementById("stdQQ").value = data.stdQQ;

            if (window.localStorage.getItem("status") == 1) {

                // 只显示管理员所属主组织名
                $("#firstOrg").find("option[name!='" + data.firstWill.organization + "']").remove();
                $("#secondOrg").find("option[name!='" + data.secondWill.organization + "']").remove();

                // 是否调剂（不可操作）
                $('#isDispensing').attr("disabled", "true");

                // 隐藏第一志愿选项框
                if (data.firstWill.organization == undefined) {
                    $('.firstWill').attr("style", "display:none");
                    $('#enrollFirst').attr("style", "display:none");
                }
            }

            // 隐藏第二志愿选项框
            if (data.secondWill.organization == undefined) {
                $('.secondWill').attr("style", "display:none")
                $('#enrollSecond').attr("style", "display:none");
            }

            // 加载第一志愿
            let orgFirstStr = data.firstWill.organization;
            let braFirstStr = data.firstWill.branch;

            $("#firstOrg").find("option[name='" + orgFirstStr + "']").prop("selected", true);
            if ($("#firstOrg").val() != 'undefined' && $("#firstOrg").val() != undefined) {
                $("#firstOrg").trigger("change");
                $("#firstBra").find("option[name='" + braFirstStr + "']").prop("selected", true);
                $('#firstReason').append(data.firstWill.reason);
            }

            // 加载第二志愿
            let orgSecondStr = data.secondWill.organization;
            let braSecondStr = data.secondWill.branch;
            $("#secondOrg").find("option[name='" + orgSecondStr + "']").prop("selected", true);
            if ($("#secondOrg").val() != 'undefined' && $("#secondOrg").val() != undefined) {
                $("#secondOrg").trigger("change");
                $("#secondBra").find("option[name='" + braSecondStr + "']").prop("selected", true);
                $('#secondReason').append(data.secondWill.reason);
            }

            // 是否接受调剂
            obj = document.getElementById("isDispensing");
            for (i = 0; i < obj.length; i++) {
                if (obj[i].value == data.isDispensing) {
                    obj[i].selected = true;
                }
            }

            $('#createTime').append(data.create_time);
            $('#updateTime').append(data.update_time);
            $('#reportStatus').append(data.isEnroll);
            if (data.isEnroll != "未被录取") {
                $('#enrollFirst').attr("style", "display:none");
                $('#enrollSecond').attr("style", "display:none");
                $('#updateInfo').attr("style", "display:none");
                $('#reportStatus').attr("style", "color:green");
            }
            $('#remark').append(data.remark);
        }
    })
}

/**
 * 更新志愿信息
 */
$('body').on("click", "#updateInfo", function (e) {
    var msg = "您确定要更新该学生的志愿信息吗？";
    if (confirm(msg) == true) {
        if (window.localStorage.getItem("status") == 2) {
            // 超级管理员身份
            updateReport("report/update_ROOT");
        } else if (window.localStorage.getItem("status") == 1) {
            // 普通管理员身份
            updateReport("report/update_ADMIN");
        }
    } else {
        return false;
    }
});

/**
 * 录取第一志愿
 */
$('body').on("click", "#enrollFirst", function (e) {
    var msg = "您确定要录取该学生到：" + $('#firstOrg').find("option:selected").text() + "-"
        + $('#firstBra').find("option:selected").text() + " 吗？";
    if (confirm(msg) == true) {
        // 超级管理员身份
        if (window.localStorage.getItem("status") == 2) {
            enrollReport(1, "report/enroll_ROOT");
        } else if (window.localStorage.getItem("status") == 1) {
            enrollReport(1, "report/enroll_ADMIN");
        }
    } else {
        return false;
    }
});

/**
 * 录取第二志愿
 */
$('body').on("click", "#enrollSecond", function (e) {

    var msg = "您确定要录取该学生到：" + $('#secondOrg').find("option:selected").text() + " "
        + $('#secondBra').find("option:selected").text() + " 吗？";
    if (confirm(msg) == true) {
        if (window.localStorage.getItem("status") == 1) {
            // 普通管理员身份
            enrollReport(2, "report/enroll_ADMIN");
        } else if (window.localStorage.getItem("status") == 2) {
            enrollReport(2, "report/enroll_ROOT");
        }
    } else {
        return false;
    }
});

/**
 * 删除学生志愿信息
 */
$('body').on("click", "#deleteReport", function (e) {
    var msg = "您确定要删除该学生志愿信息吗？警告：该操作不可逆！";
    if (confirm(msg) == true) {
        // 超级管理员身份
        if (window.localStorage.getItem("status") == 2) {
            $.ajax({
                url: backgroundURL + "report/delete",
                type: "post",
                data: {
                    stuId: $('#stuId').html(),             // 获取学号
                },
                dataType: "json",
                success: function (result) {
                    alert("删除志愿成功！");
                    window.location = frontendURL + indexURL;
                },
                // 失败回调
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    // 初始化错误信息
                    alert("删除失败");
                }
            });
        }
    } else {
        return false;
    }
});

/**
 * 更新志愿
 * @param url 请求链接
 */
function updateReport(url) {
    $.ajax({
        url: backgroundURL + url,
        type: "post",
        data: {
            stuId: $('#stuId').html(),                        //获取学号
            stdName: $("input[name=stdName]").html(),         //获取姓名
            majorId: $('#major').val(),                       //获取专业
            classNum: $('#classNum').val(),                   //获取班级
            stdQQ: $("input[name=stdQQ]").val(),              //获取QQ号
            stdPhone: $("input[name=stdPhone]").val(),        //获取手机号码
            firstWill: $('#firstBra').val(),                  //获取第一志愿id
            firstReason: $('#firstReason').val(),             //获取第一志愿理由
            secondWill: $('#secondBra').val(),
            secondReason: $('#secondReason').val(),           //获取第一志愿理由
            code: $("input[id=code]").html(),                 //使用id=... 和 name = ...均可
            isDispensing: $('#isDispensing').val(),           //是否调剂
            remark: $('#remark').val()
        },
        dataType: "json",
        success: function (result) {
            var flag = result.code;
            // 初始化错误信息
            $(".tips").text("");

            if (flag == 0) {
                window.alert("信息更新成功！");
                window.location.reload();
            }

        },
        // 失败回调
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // 初始化错误信息
            $(".tips").text("");
            // 检查正则表达式
            if (!isPhone($('#stdPhone').val()) || !isQQ($('#stdQQ').val())) {
                alert("提示：请同时填写正确的手机号与QQ号");
                $('#phoneTip').text("请同时填写正确的手机号与QQ号");
            }
        }
    });
}

/**
 * 录取志愿
 * @param order 1/2 第一志愿或第二志愿
 * @param url 请求链接
 */
function enrollReport(order, url) {
    let id = '';
    id = order == 1 ? 'first' : 'second';
    $.ajax({
        url: backgroundURL + url,
        type: "post",
        data: {
            stuId: $('#stuId').html(),                     // 获取学号
            willId: $('#' + id + 'Bra').val()              // 志愿分支id
        },
        dataType: "json",
        success: function (result) {
            var flag = result.code;
            // 初始化错误信息
            $(".tips").text("");
            if (flag == 0) {
                window.alert("录取成功！");
                window.location.reload();
            } else if (flag == 1) {
                window.alert("请先提交数据更新！");
            }
        }
    });
}

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

/**
 * 初始化专业信息
 */
let majorList = [];

function getMajor() {
    return new Promise(resolve => {
        $.ajax({
            url: backgroundURL + "major/getAll",
            type: "get",
            data: {},
            dataType: "json",
            success: function (res) {

                majorList = res.data;

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
                resolve(true);
            }
        })
    })
}


/**
 * 初始化班级信息
 */
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

/**
 * 所处组织联动逻辑
 */
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

init();
