

$(function () {
    //切换
    $("li[name='stu_li']").on("click", function () {
        $("li[name='stu_li']").removeClass("action");
        $(this).addClass("action");
        $("div.learnesing").hide();
        $("#" + $(this).attr("data-val")).show();
    });
    //分页
    var SpiltPage = function (packageId, index, pre) {

        var li = $("[data-type='" + pre + packageId + "']");
        var itemCount = li.length;

        var length = parseInt(index) * 3; // ( > itemCount ? itemCount : (parseint(index) * 3));
        length = length > itemCount ? itemCount : length;
        $(li).hide();

        for (var i = (parseInt(index) - 1) * 3; i < length; i++) {
            if (i == (parseInt(index) - 1) * 3) {
                $(li[i]).find(".de").hide();
            }
            $(li[i]).show();
        }
    }
    /*------------------------我的课程-----------------------------*/

    var packageLearn = $("[data-type='package-learn']")
    $.each(packageLearn, function (i, val) {
        var li = $(val).find(".collection li");
        $(li).hide();
        $.each($(li), function (j, val2) {
            if (j < 3) {
                $(val2).show();
            }
        });
    });

    var li_spilter = $("[data-type='package-learn-spilter']");
    $.each(li_spilter, function (i, val) {

        var li = $(val).find("li");
        $.each(li, function (j, val2) {
            if (j == 0) {
                $(val2).removeClass("bright");
                $(val2).addClass("bright");
            }

            $(val2).on("click", function () {
                var that = $(this);
                var packageId = that.attr("data-packageid");
               $(packageLearn).find( $("[data-packageid='" + packageId + "']")).removeClass("bright");
                that.addClass("bright");
                SpiltPage(packageId, that.attr("data-val"), "li_package_learn_");
            });
            /*------------------------*/
        });
    });
   /****************我的收藏******************/
    var packageFavorites = $("[data-type='package-favorites']")
    $.each(packageFavorites, function (i, val) {
        var li = $(val).find(".collection li");
        $(li).hide();
        $.each($(li), function (j, val2) {
            if (j < 3) {
                $(val2).show();
            }
        });
    });

    var li_spilter = $("[data-type='package-favorites-spilter']");
    $.each(li_spilter, function (i, val) {

        var li = $(val).find("li");
        $.each(li, function (j, val2) {
            if (j == 0) {
                $(val2).removeClass("bright");
                $(val2).addClass("bright");
            }

            $(val2).on("click", function () {
                var that = $(this);
                var packageId = that.attr("data-packageid");
                $(packageFavorites).find($("[data-packageid='" + packageId + "']")).removeClass("bright");
                that.addClass("bright");
                SpiltPage(packageId, that.attr("data-val"), "li_package_favorites_");
            });
            /*------------------------*/
        });
    });
});