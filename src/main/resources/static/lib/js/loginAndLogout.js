// 获取身份信息并显示
if (window.localStorage.getItem("status") == 1) {
    $('.userTab').append("面试官 " + window.localStorage.getItem("username"));
} else if (window.localStorage.getItem("status") == 2) {
    $('.userTab').append("管理员 " + window.localStorage.getItem("username"));
}

/**
 * 需要登录
 */
function LoginRequired() {
    if (window.getCookie("uid") == '') {
        // 跳转到登录界面
        window.location = frontendURL + loginURL;
    }
}

/**
 * 对于登录界面，若登录了则重定向至主页
 */
function redirectToIndex() {
    if (window.getCookie("uid") != '') {
        // 跳转到后台界面
        window.location = frontendURL + indexURL;
    }
}

/**
 * 1.点击登录
 */
$(".loginButton").click(function () {
    // 判断checkbox是否勾选
    var isCookie = false;
    if (document.getElementById("isUseCookie").checked) {
        isCookie = true;
    }
    if (isCookie == false) {
        alert('同意我们的用户及隐私协议才能使用！');
        return;
    }

    // 单击登录按钮触发ajax事件
    $.ajax({
        url: backgroundURL + "user/login",
        type: "post",
        data: {
            username: $("input[name=username]").val(),
            password: $("input[name=password]").val()
        },
        dataType: "json",
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true, // 发送Ajax时，Request header 中会包含跨域的额外信息，但不会含cookie
        success: function (result) {
            var flag = result.code;
            var data = result.data;
            if (flag == 0) {
                //设置用户名及身份到缓存
                var storage = window.localStorage;
                storage["username"] = data.username;
                storage["status"] = data.status;

                // 跳转登录成功界面
                window.location = "index";
            } else {
                alert("用户名或密码错误，请重新输入");
            }
        }, error: function () {
            alert("用户名或密码错误，请重新输入");
        }
    });
});

/**
 * 2.点击注销
 */
$('.logoutButton').click(function () {
    $.ajax({
        url: backgroundURL + "user/logout",
        type: 'post',
        dataType: 'json',
        data: {
            username: window.localStorage.getItem("username")
        },
        success: function (res) {
            // 清除用户信息并跳转
            window.localStorage.removeItem('username');
            window.localStorage.removeItem('status')
            window.location = frontendURL + loginURL;
        }
    });
});

// 隐藏模块
if (window.localStorage.getItem("status") == 1) {
    $("#authority-tab").hide();
    $("#major-tab").hide();
    $("#org-tab").hide();
    $("#branch-tab").hide();
}
