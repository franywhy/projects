$(document).ready(function (e) {
    var id = getParameter("id");
    //var url = window.location.pathname;
    //var id = url.substring(url.lastIndexOf('/') + 1, url.length - 5);
    $.ajax({
        type: "GET",
        url: "" + getApiUrl() + "/school/notice_detail?notice_id=" + id,
        dataType: "json",
        async: false,
        beforeSend: function () {
        },
        success: function (json) {
            $(document).attr("title", json.data.activity_title); //修改title值
            $(".school_name").append("<a href=\"schoolinfo.html?code=" + json.data.school_code + "\">" + json.data.school_name + "</a>");
            $("#tit").append(json.data.activity_title);
            $(".foo").append("更新时间：" + json.data.timestamp);
            $("#cont").append(json.data.activity_info);
        },
        complete: function () {
        },
        error: function (data) {

        }
    });
});