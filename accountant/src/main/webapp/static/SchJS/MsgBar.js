/// <reference path="jquery-1.8.3.min.js" />
$(function () {

    $("a[name='closeMsg']").on("click", function () {

        $(".meter").hide(1000);
        $.ajax({
            url: "/lib/member/MsgBar.ashx",
            type: "GET",
            data: { power: 0 },
            success:function(data){
              
            }
        });

    });
});