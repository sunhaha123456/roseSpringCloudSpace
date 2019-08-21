function isEmpty(str) {
    if (str == null) {
        return true;
    }
    if (str == "") {
        return true;
    }
    return false;
}
function isNotEmpty(str) {
    return !isEmpty(str);
}
function alertInfo(info) {
    parent.$.messager.alert('提示信息！', info, 'info');
}
function alertSuccess() {
    parent.$.messager.alert('提示信息！', "操作成功！", 'info');
}
function alertWarn(info) {
    parent.$.messager.alert('警告操作！', info, 'warning');
}
function loadTip() { // 弹出加载层
    $("<div class=\"datagrid-mask\"></div>").css({
        display: "block",
        width: "100%",
        height: $(window).height()
    }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("加载中。。。").appendTo("body").css({
        display: "block",
        left: ($(document.body).outerWidth(true) - 190) / 2,
        top: ($(window).height() - 45) / 2
    });
}
function disloadTip() { // 关闭加载层
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}
function ajaxSynchPostJson(conectUrl, params) {
    var res = {"code": 500, "msg": "数据传输错误！"};
    if (params == null) {
        params = {};
    }
    $.ajax({
        async: false,
        url: contextPath + conectUrl,
        type: "POST",
        contentType: 'application/json',
        cache: false,
        data: JSON.stringify(params),
        success: function (result) {
            res = result;
        },
        error: function (xhr, status, error) {},
        beforeSend: function(xhr) {
            xhr.setRequestHeader("token", interceptorToken);
            xhr.setRequestHeader("userId", interceptorUserId);
        }
    });
    return res;
}
function ajaxSynchGet(conectUrl, params) {
    var res = {"code": 500, "msg": "数据传输错误！"};
    if (params == null) {
        params = {};
    }
    $.ajax({
        async: false,
        url: contextPath + conectUrl,
        type: "GET",
        cache: false,
        data: params,
        success: function (result) {
            res = result;
        },
        error: function (xhr, status, error) {},
        beforeSend: function(xhr) {
            xhr.setRequestHeader("token", interceptorToken);
            xhr.setRequestHeader("userId", interceptorUserId);
        }
    });
    return res;
}
/*
入参格式如下：
var param = [
    {key:"myfile", value:"#excelFileUpload"},
    {key:"param1", value:"你好啊"},
    {key:"param2", value:"我好啊"}
];
并且第一个参数必须是文件，且要带#
*/
function ajaxSynchUploadFilePost(conectUrl, params) {
    var formData = new FormData();
    var res = {"code": 500, "msg": "数据传输错误！"};
    if (params != null && params.length > 0) {
        formData.append(params[0].key, $(params[0].value)[0].files[0]);
        if (params.length > 1) {
            for(var i = 1; i < params.length ;i++) {
                formData.append(params[i].key, params[i].value);
            }
        }
    }
    $.ajax({
        async: false,
        url: contextPath + conectUrl,
        type: "POST",
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function (result) {
            res = result;
        },
        error: function (xhr, status, error) {},
        beforeSend: function(xhr) {
            xhr.setRequestHeader("token", interceptorToken);
            xhr.setRequestHeader("userId", interceptorUserId);
        }
    });
    return res;
}
function urlHandle(url) {
    return url + '?token=' + interceptorToken + '&userId=' + interceptorUserId + '&randNum=' + Math.random();
}
function urlHandleContext(url) {
    return contextPath + url + '?token=' + interceptorToken + '&userId=' + interceptorUserId + '&randNum=' + Math.random();
}
