$(function () {


    $("[name='stu_li']").on("click", function () {
        var that_stuLi = $(this);
        $("[name = 'stu_li']").removeClass("action");
        that_stuLi.addClass("action");

        $(".learnesing").hide();
        $("#" + that_stuLi.attr("data-val")).show();

    });



    var pathname = window.location.pathname;
    var that_learnbzz = $(".learnbzz");
    if (pathname.indexOf("/personal") != -1) {
        
    } else {
        $(".learnbzz").width(962);
    }

    $.each(that_learnbzz, function (i, val) {
        /*初始化LI begin*/
        var that_li = $($(val).find(".collec li"));
        that_li.hide();
        for (var i = 0; i < that_li.length; i++) {
            if (i < 3)
                $(that_li[i]).show();
            else
                continue;
        }
        /*初始化LI end*/
        var that_spilter_li = $($(val).find(".gd li"));
        //  alert(that_spilter_li.length);
        $.each(that_spilter_li, function (i, val) {
            $(val).on("click", function () {
                var that1 = $(this);
                that_li.hide();
                that_spilter_li.removeClass("bright");
                that1.addClass("bright");
                var index = parseInt(that1.attr("data-val"));
                var length = index * 3;
                length = length > that_li.length ? that_li.length : length;
                for (var i = (index - 1) * 3; i < length; i++) {
                    $(that_li[i]).show();
                    if (i == (index - 1) * 3) {
                        $($(that_li[i]).find(".de")).hide();
                    }
                }
            });
        });

    });
});