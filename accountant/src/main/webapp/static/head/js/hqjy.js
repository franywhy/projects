$(document).ready(function () {
$.ajax({
    type: "GET",
    url: "http://www.hqjy.com/head/lib/foot.ashx",
    data: "type=1",
    dataType: "jsonp",
    jsonp: "callbackfun",
    beforeSend: function () {
    },
    success: function (json) {
        $.each(json.list, function (i, model) {
            $("#cont").append("<a href=\"" + model.url + "\" target=\"_" + model.target + "\">" + model.name + "</a>");
        });
    },
    complete: function () {
    }
})

})