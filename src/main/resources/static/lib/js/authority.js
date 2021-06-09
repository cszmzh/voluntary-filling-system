async function init() {
    LoginRequired();

    await getMainOrg();

    // 初始化Table
    let oTable = new TableInit();
    oTable.Init();

    // 初始化Button的点击事件
    let oButtonInit = new ButtonInit();
    oButtonInit.Init();
}

// $(async function () {
//     LoginRequired();
//
//     await getMainOrg();
//
//     // 初始化Table
//     let oTable = new TableInit();
//     oTable.Init();
//
//     // 初始化Button的点击事件
//     let oButtonInit = new ButtonInit();
//     oButtonInit.Init();
// });

var TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#tb_departments').bootstrapTable({
            url: 'user/getAll',                 //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 20],                 //可供选择的每页的行数（*）
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            // height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "userId",                //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'userId',
                title: '用户编号'
            }, {
                field: 'userName',
                title: '用户名',
            }, {
                field: 'realName',
                title: '持有人'
            }, {
                field: 'userPassword',
                title: '密码'
            }, {
                field: 'organization',
                title: '所属组织'
            }, {
                field: 'branch',
                title: '分支'
            }, {
                field: 'userStatus',
                title: '权限',
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return '管理员';
                    } else if (value == 2) {
                        return '超级管理员';
                    }
                }
            }, {
                field: 'createTime',
                title: '更新时间',
                // 获取日期列的值进行转换
                formatter: function (value, row, index) {
                    return changeDateFormat(value)
                }
            },],
            responseHandler: function (res) {
                return res.data;
            }
        });
    };

    // 得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   // 页面大小
            offset: params.offset,  // 页码
            departmentname: $("#txt_search_departmentname").val(),
            statu: $("#txt_search_statu").val()
        };
        return temp;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    var $table = $("#tb_departments");

    oInit.Init = function () {
        // 初始化页面上面的按钮事件
        var $editbtn = $("#btn_edit");
        var $addbtn = $("#btn_add");
        var $deletebtn = $("#btn_delete");

        $editbtn.click(async function () {
            var info = $table.bootstrapTable('getSelections')[0];
            if (!info) {
                alert("请选择数据");
            } else {
                $("#update_userid").val(info.userId)
                $("#update_username").val(info.userName)
                $("#update_realname").val(info.realName)
                $("#update_password").val(info.userPassword)
                $("#update_branchname").val(info.branch)

                // 初始化组织下拉列表
                $("#updateOrg").find("option[name='" + info.organization + "']").prop("selected", true);

                await changeUpdateBra();

                $("#updateBra").find("option[name='" + info.branch + "']").prop("selected", true);
                $("#update_authority").val(info.userStatus == 1 ? '管理员' : '超级管理员')
                $("#updateModal").modal();
            }
        });

        $deletebtn.click(function () {
            var info = $table.bootstrapTable('getSelections')[0];
            if (!info) {
                alert("请选择数据");
            } else {
                var msg = "您确定要删除该用户: [" + info.userName + "] 吗？该操作不可逆！";
                if (confirm(msg) == true) {
                    deleteUser(info.userId);
                } else {
                    return false;
                }
            }
        });

        $addbtn.click(function () {
            $("#create_authority").val('管理员');
            $("#createModal").modal();
        });

        // 模态框按钮
        var $saveUpdate = $("#update-edit-btn");
        $saveUpdate.click(function () {
            var msg = "您确定要更新该用户信息吗？";
            if (confirm(msg) == true) {
                updateUser();
            } else {
                return false;
            }
        });

        var $saveCreate = $("#create-edit-btn");
        $saveCreate.click(function () {
            var msg = "您确定要创建该用户吗？";
            if (confirm(msg) == true) {
                createUser();
            } else {
                return false;
            }
        });
    };

    return oInit;
};

// 转换日期格式(时间戳转换为datetime格式)
function changeDateFormat(cellval) {
    var dateVal = cellval + "";
    if (cellval != null) {
        var date = new Date(parseInt(dateVal.replace("/Date(", "").replace(")/", ""), 10));
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        return date.getFullYear() + "-" + month + "-" + currentDate + " " + hours + ":" + minutes + ":" + seconds;
    }
}

// 初始化志愿信息
function getMainOrg() {
    return new Promise(resolve => {
        $.ajax({
            url: backgroundURL + "org/getMain",
            type: "get",
            data: {},
            dataType: "json",
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

let createSelect = document.getElementById("createOrg");
createSelect.onchange = function () {
    changeCreateBra();
}

let updateSelect = document.getElementById("updateOrg");
updateSelect.onchange = function () {
    changeUpdateBra();
}

function changeCreateBra() {
    let value = createSelect.value;
    let val = document.getElementById("createBra");
    selectBelong(value, val);
};

function changeUpdateBra() {
    let value = updateSelect.value;
    let val = document.getElementById("updateBra");
    return new Promise(async resolve => {
        await selectBelong(value, val);
        resolve(true);
    })
};

let selectBelong = function (value, val) {

    val.innerHTML = '';

    if (value == '' || value == undefined) {
        let option = document.createElement("option");
        option.value = '';
        option.innerHTML = '<option value="">请选择</option>';
        val.appendChild(option);
        return;
    }

    return new Promise(resolve => {
        $.ajax({
            url: backgroundURL + "org/getBranch",
            type: "get",
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

function updateUser() {
    $.ajax({
        url: backgroundURL + 'user/update',
        type: "post",
        data: {
            userId: $('#update_userid').val(),
            userName: $('#update_username').val(),
            realName: $('#update_realname').val(),
            password: $('#update_password').val(),
            branchId: $('#updateBra').val(),
        },
        dataType: "json",
        success: function (result) {
            if (result.code == 0) {
                alert("更新成功！")
                $("#tb_departments").bootstrapTable('refresh');
                $("#closeUpdateModal").click();
            } else {
                console.log(result.msg);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("更新失败！" + XMLHttpRequest.responseJSON.message)
        }
    });
}

function createUser() {
    $.ajax({
        url: backgroundURL + 'user/createAdmin',
        type: "post",
        data: {
            userName: $('#create_username').val(),
            realName: $('#create_realname').val(),
            password: $('#create_password').val(),
            branchId: $('#createBra').val(),
        },
        dataType: "json",
        success: function (result) {
            if (result.code == 0) {
                alert("新增成功！")
                $("#tb_departments").bootstrapTable('refresh');
                $("#closeCreateModal").click();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("新增失败！" + XMLHttpRequest.responseJSON.message)
        }
    });
}

function deleteUser(userId) {
    $.ajax({
        url: backgroundURL + 'user/delete',
        type: "post",
        data: {
            userId: userId
        },
        dataType: "json",
        success: function (result) {
            if (result.code == 0) {
                alert("删除成功！");
                $("#tb_departments").bootstrapTable('refresh');
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("删除失败！" + XMLHttpRequest.responseJSON.message)
        }
    });
}

init();
