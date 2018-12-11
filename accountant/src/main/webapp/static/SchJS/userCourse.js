/// <reference path="jquery-1.8.3.min.js" />
$(function () {
    $("[data-type='lispliterpage']").on('click', function () {



        var dataVal = $(this).attr("data-val");
        $("[name='" + dataVal + "']").hide();
        $("[data-val='" + dataVal + "']").removeClass("bright");
        $(this).addClass("bright");
        var pageIndex = $(this).attr("alt");
        var liItems = $("[name='" + dataVal + "']");

        var pageSize = 3;


        var tmpLenth = parseInt(pageIndex) * parseInt(pageSize) > liItems.length ? liItems.length : parseInt(pageIndex) * parseInt(pageSize);

        for (var i = (parseInt(pageIndex) - 1) * parseInt(pageSize), j = 0; i < tmpLenth; i++, j++) {
            $(liItems[i]).show();
            if (j == 0) {
                try {
                    $(liItems[i]).find(".de").hide();
                } catch (ex) { }
            }
        }

    });

    
    var gd_ul = $(".gd");
    $.each(gd_ul, function (i, val) {
        $(val).find("li").first().addClass("bright");
    });
});


/*------------------------------激活课程卡-------------------*/
$(function () {
    $("#btnSubmit").on("click", function () {

        var a = new Dialog2("激活新的课程卡", "#details1", {
            "btnClose": function () { a.CloseDialog(); }
        }, false);
        a.ShowDialog();
    });
});

$(function () {
    $("#addLearnCourse").click(function () {
        var code = $("#codeByCounrse").val();
        var coupass = $("#coupassByCounrse").val();
        $.ajax({
            url: "/lib/member/member.ashx",
            type: "POST",
            data: "type=10&code=" + code + "&coupass=" + coupass + "&coutype=2",
            beforeSend: function () {
                $("#loading_kechengka").html("数据提交中,请稍候...");
            },
            success: function (data) {
                
                if (data == "课程卡已激活") {
                    window.location.reload();
                }
                else {
                    $("#loading_kechengka").html(data);
                }

            }

        })

    });

});