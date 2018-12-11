/// <reference path="jquery-1.8.3.min.js" />
/*-----------------------------首页透明---------------------------*/
$(function () {
    $(".hq").hover(function () {
        $(".bg", this).show();
        $(".hqA", this).show();

    },
	function () {
	    $(".bg").hide();
	    $(".hqA").hide();

	});

});
/*-------------------------------------------------热门名师图片缩放-------------------*/

$(function () {
    function AutoResizeImage(maxWidth, maxHeight, objImg) {
        var img = new Image();
        img.src = objImg.src;
        var hRatio;
        var wRatio;
        var Ratio = 1;
        var w = img.width;
        var h = img.height;
        wRatio = maxWidth / w;
        hRatio = maxHeight / h;
        if (maxWidth == 0 && maxHeight == 0) {
            Ratio = 1;
        }
        else if (maxWidth == 0) {
            Ratio = hRatio;
        } else if (maxHeight == 0) {
            Ratio = wRatio;
        } else if (wRatio < 1 || hRatio < 1) {
            Ratio = (wRatio <= hRatio ? wRatio : hRatio);
        }

        w = w * Ratio;
        h = h * Ratio;

        objImg.height = h;
        objImg.width = w;
    }
    $.each($(".jsBig a img"), function (i, val) {
        AutoResizeImage(150, 0, this);
    });
});