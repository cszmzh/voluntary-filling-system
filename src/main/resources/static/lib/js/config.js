// 服务器地址
var curWwwPath = window.document.location.href;
var pathName = window.document.location.pathname;
var pos = curWwwPath.indexOf(pathName);
var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
var localhostPath = curWwwPath.substring(0, pos) + projectName + "/";

// 服务器地址，前后端不分离
var frontendURL = localhostPath;
var backgroundURL = localhostPath;

// 登录页面地址
var loginURL = "login";

// 登录后主页地址
var indexURL = "index";

// 默认显示第一页
var page = 1;

// 默认情况下一页显示多少条
var pageSize = 20;

// 欢迎语
var welcomeStr = "\n" +
    "    ______ ___ ______                  __    \n" +
    "   / ____/<  // ____/_____ ____   ____/ /___ \n" +
    "  /___ \\  / //___ \\ / ___// __ \\ / __  // _ \\ \n" +
    " ____/ / / /____/ // /__ / /_/ // /_/ //  __/\n" +
    "/_____/ /_//_____/ \\___/ \\____/ \\__,_/ \\___/\n" + "我在这里，静静地看着你。"

console.log(welcomeStr)
