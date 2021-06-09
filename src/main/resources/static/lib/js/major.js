async function init() {
    LoginRequired();

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
            url: 'major/getAll',                 //请求后台的URL（*）
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
            uniqueId: "majorId",                //每一行的唯一标识，一般为主键列
            showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'majorId',
                title: '专业编号'
            }, {
                field: 'majorName',
                title: '专业名',
            }, {
                field: 'classNum',
                title: '班级数'
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
                $("#update_majorid").val(info.majorId)
                $("#update_majorname").val(info.majorName)
                $("#update_classnum").val(info.classNum)

                $("#updateModal").modal();
            }
        });

        $deletebtn.click(function () {
            var info = $table.bootstrapTable('getSelections')[0];
            if (!info) {
                alert("请选择数据");
            } else {
                var msg = "您确定要删除专业: [" + info.majorName + "] 吗？该操作不可逆！";
                if (confirm(msg) == true) {
                    deleteMajor(info.majorId);
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
            var msg = "您确定要更新该专业信息吗？";
            if (confirm(msg) == true) {
                updateMajor();
            } else {
                return false;
            }
        });

        var $saveCreate = $("#create-edit-btn");
        $saveCreate.click(function () {
            var msg = "您确定要创建该专业吗？";
            if (confirm(msg) == true) {
                createMajor();
            } else {
                return false;
            }
        });
    };
    return oInit;
};

function updateMajor() {
    $.ajax({
        url: backgroundURL + 'major/update',
        type: "post",
        data: {
            majorId: $('#update_majorid').val(),
            majorName: $('#update_majorname').val(),
            classNum: $('#update_classnum').val()
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

function createMajor() {
    $.ajax({
        url: backgroundURL + 'major/create',
        type: "post",
        data: {
            majorId: $('#create_majorid').val(),
            majorName: $('#create_majorname').val(),
            classNum: $('#create_classnum').val()
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

function deleteMajor(majorId) {
    $.ajax({
        url: backgroundURL + 'major/delete',
        type: "post",
        data: {
            majorId: majorId
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
