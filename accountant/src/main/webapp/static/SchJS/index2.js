/// <reference path="comm.js" />

;
$(function () {
    $("[data-val='checkbox']").on("click", function () {
        if ($(this).hasClass("unchecked")) {
            $(this).removeClass("unchecked");
            $(this).addClass("checked");
            $(this).attr("data-checked", "true");


        } else {
            $(this).removeClass("checked");
            $(this).addClass("unchecked");
            $(this).attr("data-checked", "false");

        } getTotal($(this).attr("data-type"));
    });



    /*获取总数*/
    var getTotal = function (typeid) {
        var total = 0;
        $.each($("[data-val='checkbox']"), function (i, val) {
            if ($(val).attr("data-type") == typeid && $(val).attr("data-checked") == "true") {
                total += parseInt($(val).attr("data-price"));
            }
        });

        $("#txtPrice").text(total);
        $("#txtPrice").css(
            "color","red"
        );
    }

});