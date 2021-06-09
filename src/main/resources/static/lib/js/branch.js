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

var TableInit = function () {
    var oTableInit = new Object();
    // 初始化Table
    oTableInit.Init = function () {
        $('#tb_departments').bootstrapTable({
            url: 'org/getBranchResult',        //请求后台的URL（*）
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
            uniqueId: "branchId",                //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'branchId',
                title: '分支编号'
            }, {
                field: 'orgName',
                title: '所属组织',
            }, {
                field: 'branchName',
                title: '分支名'
            }, {
                field: 'branchDes',
                title: '简介'
            }, {
                field: 'managerName',
                title: '负责人'
            }],
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
                $("#update_branchid").val(info.branchId)
                $("#update_branchname").val(info.branchName)
                $("#update_branchdes").val(info.branchDes)
                $("#update_branchmanager").val(info.managerName)

                // 初始化组织下拉列表
                $("#updateOrg").find("option[name='" + info.orgName + "']").prop("selected", true);
                $("#updateModal").modal();
            }
        });

        $deletebtn.click(function () {
            var info = $table.bootstrapTable('getSelections')[0];
            if (!info) {
                alert("请选择数据");
            } else {
                var msg = "您确定要删除分支: [" + info.orgName + "-" + info.branchName + "] 吗？该操作不可逆！";
                if (confirm(msg) == true) {
                    deleteBranch(info.branchId);
                } else {
                    return false;
                }
            }
        });

        $addbtn.click(function () {
            $("#createModal").modal();
        });

        // 模态框按钮
        var $saveUpdate = $("#update-edit-btn");
        $saveUpdate.click(function () {
            var msg = "您确定要更新该分支信息吗？";
            if (confirm(msg) == true) {
                updateBranch();
            } else {
                return false;
            }
        });

        var $saveCreate = $("#create-edit-btn");
        $saveCreate.click(function () {
            var msg = "您确定要创建该分支吗？";
            if (confirm(msg) == true) {
                createBranch();
            } else {
                return false;
            }
        });
    };

    return oInit;
};


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


function updateBranch() {
    $.ajax({
        url: backgroundURL + 'org/updateBranch',
        type: "post",
        data: {
            branchId: $('#update_branchid').val(),
            orgId: $('#updateOrg option:selected').val(),
            branchName: $('#update_branchname').val(),
            branchDes: $('#update_branchdes').val(),
            managerName: $('#update_branchmanager').val()
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

function createBranch() {
    $.ajax({
        url: backgroundURL + 'org/createBranch',
        type: "post",
        data: {
            branchId: $('#create_branchid').val(),
            orgId: $('#createOrg option:selected').val(),
            branchName: $('#create_branchname').val(),
            branchDes: $('#create_branchdes').val(),
            managerName: $('#create_branchmanager').val()
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

function deleteBranch(branchId) {
    $.ajax({
        url: backgroundURL + 'org/deleteBranch',
        type: "post",
        data: {
            branchId: branchId
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
