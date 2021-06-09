// 初始化验证码图片
$("#changeCode").attr("src", backgroundURL + "img/code?d=" + new Date().getTime());

// 更换验证码图片
$(function () {
    $("#changeCode").on("click", function () {
        $(this).attr("src", backgroundURL + "img/code?d=" + new Date().getTime());
    });
});
