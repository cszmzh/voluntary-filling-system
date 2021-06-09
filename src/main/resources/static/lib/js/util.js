

/**
 * 1.获取Cookie
 * @param cname
 * @returns {string}
 */
//判断是否登录,获取Cookie
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
    }
    return "";
}

/**
 * 2.获取URL传值变量
 * @param variable 变量名
 * @returns {string|boolean} 变量值
 */
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) { return pair[1]; }
    }
    return (false);
}

/**
 * 3.正则表达式验证
 */
function isPhone(phone) {
    var phonereg = /^1[34578]\d{9}$/;
    if (!phonereg.test(phone)) {
        return false;
    }
    return true;
}

/**
 * 4.qq验证
 * @param qq
 * @returns {boolean}
 */
function isQQ(qq) {
    var qqreg = /^[1-9][0-9]{4,9}$/gim;
    if (!qqreg.test(qq)) {
        return false;
    }
    return true;
}

