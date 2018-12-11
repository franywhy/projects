$(function () {
    $(".fb-yes-btn").click(function () {
        var mobile = $("#mobile").val();
        var name = $("#name").val();
        var memo = $("#memo").val();
        if (mobile != "" && mobile != "") {
            $.ajax({
                url: "http://www.hqjy.com/head/lib/registHandler.ashx",
                type: "GET",
                data: {
                    type: "1",
                    name: name,
                    mobile: mobile,
                    memo: memo
                },
                dataType: "jsonp",
                jsonp: "callbackfun",
                beforeSend: function () {

                },
                success: function (data) {
                    alert(data.code);
                    $(".fb-no-btn").trigger("click");
                }

            })
        }
    });

});