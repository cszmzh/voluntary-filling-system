//验证是否登录
LoginRequired();

/**
 * 1.获取所有志愿信息
 */
$(getAllReport = function () {

    //如果page参数不为空再赋值
    if (getQueryVariable("page")) {
        page = getQueryVariable("page");
    }

    //参数获取
    var selectFirstWill = getQueryVariable("selectFirstWill");
    var selectSecondWill = getQueryVariable("selectSecondWill");
    var selectNoEnroll = getQueryVariable("selectNoEnroll");

    if (selectFirstWill === 'true') {
        $('#firstWillButton').attr("class", "btn btn-success btn-lg");
    }
    if (selectSecondWill === 'true') {
        $('#secondWillButton').attr("class", "btn btn-success btn-lg");
    }
    if (selectNoEnroll === 'true') {
        $('#selectNoEnrollButton').attr("class", "btn btn-success btn-lg");
    }

    $.ajax({
        url: backgroundURL + 'report/getAll?page=' + page + '&pageSize=' + pageSize,
        type: 'get',
        datatype: 'json',
        success: function (res) {

            var list = res.data.report_list;

            //将数据显示在页面上
            var str = "";
            var strPage = "";
            str += "<table class='table table-hover'><thead><tr><th>序号</th><th>学号</th><th>姓名</th><th>专业</th><th>班级</th><th>QQ</th><th>手机号</th>" +
                "<th>志愿一</th><th></th>" +
                "<th>志愿二</th><th></th><th>是否调剂</th>" +
                "<th>录取状态</th></thead><tbody>";

            strPage += "<div style=\"text-align:center\"><div class=\"btn-group\" role=\"group\" aria-label=\"get\">"
            //遍历数据

            var total_skip = 0;

            for (var i = 0; i < list.length; i++) {

                //过滤已录取
                if (selectNoEnroll === 'true' && list[i].isEnroll !== "未被录取") {
                    total_skip++;
                    continue;
                }

                //根据第一志愿和第二志愿过滤
                var firstOrg = list[i].firstWill.organization === undefined ? " " : list[i].firstWill.organization;
                var firstBra = list[i].firstWill.branch === undefined ? " " : list[i].firstWill.branch;
                var secondOrg = list[i].secondWill.organization === undefined ? " " : list[i].secondWill.organization;
                var secondBra = list[i].secondWill.branch === undefined ? " " : list[i].secondWill.branch;

                if (selectSecondWill === 'true') {
                    firstOrg = " ";
                    firstBra = " ";
                } else if (selectFirstWill === 'true') {
                    secondOrg = " ";
                    secondBra = " ";
                }

                if (firstOrg === " " && secondOrg === " ") {
                    total_skip++;
                    continue;
                }

                var enrollInfo = list[i].isEnroll === "未被录取" ? ">" + list[i].isEnroll : " style='color:green' >" + list[i].isEnroll;

                str += "<tr><td>" + (pageSize * (page - 1) + i + 1) + "</td><td style=\"cursor:pointer\" class='detail_info' >" + list[i].stuId + "</td><td>" + list[i].stdName + "</td><td>" + list[i].major + "</td><td>" +
                    list[i].classNum + "</td><td>" + list[i].stdQQ + "</td><td>" + list[i].stdPhone + "</td><td>" +
                    firstOrg + "</td><td>" + firstBra + "</td><td>" +
                    secondOrg + "</td><td>" + secondBra + "</td><td>" +
                    list[i].isDispensing + "</td><td" + enrollInfo + "</td></tr>";

            }

            // 上一页
            if (page > 1) {
                strPage += "<button style='margin:0 auto' type=\"button\" class=\"btn btn-default btn-page\">上一页</button>";
            }

            for (var i = 1; i <= Math.ceil(res.data.total / pageSize); i++) {
                if (page == i) {
                    strPage += "<button style='margin:0 auto;background-color: #357ebd; color: white;' type=\"button\" class=\"btn btn-default btn-page\">" + i + "</button>";
                    continue;
                }
                strPage += "<button style='margin:0 auto' type=\"button\" class=\"btn btn-default btn-page\">" + i + "</button>";
            }

            // 下一页
            if (page < Math.ceil(res.data.total / pageSize)) {
                strPage += "<button style='margin:0 auto' type=\"button\" class=\"btn btn-default btn-page\">下一页</button>";
            }

            //遍历完成之后
            str += "</tbody></table>";
            strPage += "</div></div>";

            //将表格添加到body中
            $('#table-info').append(str);
            $(".page-info").append("当前页数：" + page + "  总条数：" + res.data.total);
            $('#table-info').append(strPage);

            if (selectFirstWill === 'true' || selectSecondWill === 'true' || selectNoEnroll === 'true') {
                $('.page-info').append(" 当前页面已过滤：" + total_skip + "条");
            }
        }
    })
});


/**
 * 2.分页查询按钮
 */
$('body').on("click", ".btn-page", function () {
    if (this.innerHTML == '上一页') {
        page = getQueryVariable("page");
        window.location.href = changeURLArg(window.location.href, "page", Number(page) - 1);
        return;
    } else if (this.innerHTML == '下一页') {
        page = getQueryVariable("page");
        if (!page) {
            page = 1;
        }
        window.location.href = changeURLArg(window.location.href, "page", Number(page) + 1);
        return;
    }
    page = this.innerHTML;
    window.location.href = changeURLArg(window.location.href, "page", page);
});

/**
 * 3.监听点击学号
 */
$('body').on("click", ".detail_info", function (e) {
    window.location.href = 'reportDetail.html?stuId=' + $(e.target).context.innerHTML;
});

/**
 * 4.监听根据学号查找按钮
 */
$('body').on("click", "#findByIdButton", function (e) {

    $.ajax({
        url: backgroundURL + 'report/getByStuId?stuId=' + $('#findByIdContent').val(),
        type: 'post',
        datatype: 'json',
        success: function (res) {

            var data = res.data;

            if (data == undefined) {
                alert("没有找到结果！");
                return;
            }

            // 将数据显示在页面上
            var str = "";
            var strPage = "";
            str += "<table class='table table-hover'><thead><tr><th>序号</th><th>学号</th><th>姓名</th><th>专业</th><th>班级</th><th>QQ</th><th>手机号</th>" +
                "<th>志愿一</th><th></th>" +
                "<th>志愿二</th><th></th><th>是否调剂</th>" +
                "<th>录取状态</th></thead><tbody>";

            strPage += "<div style=\"text-align:center\"><div class=\"btn-group\" role=\"group\" aria-label=\"get\">"

            // 遍历数据
            var firstOrg = data.firstWill.organization == undefined ? " " : data.firstWill.organization;
            var firstBra = data.firstWill.branch == undefined ? " " : data.firstWill.branch;
            var secondOrg = data.secondWill.organization == undefined ? " " : data.secondWill.organization;
            var secondBra = data.secondWill.branch == undefined ? " " : data.secondWill.branch;
            var enrollInfo = data.isEnroll == "未被录取" ? ">" + data.isEnroll : " style='color:green' >" + data.isEnroll;

            str += "<tr><td>1</td><td style=\"cursor:pointer\" class='detail_info' >" + data.stuId + "</td><td>" + data.stdName + "</td><td>" + data.major + "</td><td>" +
                data.classNum + "</td><td>" + data.stdQQ + "</td><td>" + data.stdPhone + "</td><td>" +
                firstOrg + "</td><td>" + firstBra + "</td><td>" +
                secondOrg + "</td><td>" + secondBra + "</td><td>" +
                data.isDispensing + "</td><td" + enrollInfo + "</td></tr>";

            // 遍历完成之后
            str += "</tbody></table>";
            strPage += "</div></div>";

            // 将表格添加到body中
            $('#table-info').text("");
            $('#table-info').append(str);
            $(".page-info").text("当前页数：1  总条数：1");
            $('#table-info').append(strPage);
        },
        // 失败回调
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("没有找到结果！");
        }
    });
});

function changeURLArg(url, arg, arg_val) {
    var pattern = arg + '=([^&]*)';
    var replaceText = arg + '=' + arg_val;
    if (url.match(pattern)) {
        var tmp = '/(' + arg + '=)([^&]*)/gi';
        tmp = url.replace(eval(tmp), replaceText);
        return tmp;
    } else {
        if (url.match('[\?]')) {
            return url + '&' + replaceText;
        } else {
            return url + '?' + replaceText;
        }
    }
    return url + '\n' + arg + '\n' + arg_val;
}

$('body').on("click", "#firstWillButton", function (e) {
    if ($('#firstWillButton').attr("class") === "btn btn-success btn-lg") {
        $('#firstWillButton').attr("class", "btn btn-primary btn-lg");
        window.location.href = changeURLArg(window.location.href, "selectFirstWill", false);
    } else if ($('#firstWillButton').attr("class") === "btn btn-primary btn-lg") {
        // 设置为绿色
        // 同时要把第二志愿设置为蓝色
        $('#secondWillButton').attr("class", "btn btn-primary btn-lg");
        var link = changeURLArg(window.location.href, "selectFirstWill", true);
        window.location.href = changeURLArg(changeURLArg(link, "page", '1'), "selectSecondWill", false);
    }
});

$('body').on("click", "#secondWillButton", function (e) {
    if ($('#secondWillButton').attr("class") === "btn btn-success btn-lg") {
        $('#secondWillButton').attr("class", "btn btn-primary btn-lg");
        window.location.href = changeURLArg(window.location.href, "selectSecondWill", false);
    } else if ($('#secondWillButton').attr("class") === "btn btn-primary btn-lg") {
        // 设置为绿色
        // 同时要把第一志愿设置为蓝色
        $('#firstWillButton').attr("class", "btn btn-primary btn-lg");
        var link = changeURLArg(window.location.href, "selectSecondWill", true);
        window.location.href = changeURLArg(changeURLArg(link, "page", '1'), "selectFirstWill", false);
    }
});

$('body').on("click", "#selectNoEnrollButton", function (e) {
    if ($('#selectNoEnrollButton').attr("class") === "btn btn-success btn-lg") {
        $('#selectNoEnrollButton').attr("class", "btn btn-primary btn-lg");
        window.location.href = changeURLArg(window.location.href, "selectNoEnroll", false);
    } else if ($('#selectNoEnrollButton').attr("class") === "btn btn-primary btn-lg") {
        // 设置为绿色
        $('#selectNoEnrollButton').attr("class", "btn btn-success btn-lg");
        window.location.href = changeURLArg(window.location.href, "selectNoEnroll", true);
    }
});
