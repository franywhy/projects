$(function () {
    $.ajax({
        url: "http://www.hqjy.com/api/handler.ashx",
        type: "GET",
        data: {
            type: "1"
        },
        dataType: "jsonp",
        jsonp: "callbackfun",
        beforeSend: function () {

        },
        success: function (data) {
        }

    })

});