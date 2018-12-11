$(function () {
    detateTabBorder();

    $(".getBuy").click(function () {
        var productChildIds = $(this).attr("rel");
        var allPrice = $(this).attr("data-val");
        if (productChildIds != '') {
            $.ajax(
            {
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=8&productChildIds=" + productChildIds + "&allPrice=" + allPrice + "",
                beforeSend: function () {

                },
                success: function (data) {
                    if (data != "000") {
                        window.location.href = "/UserManage/order.html?ordertype=803";

                    }
                    else {
                        $(".btn-login").trigger("click");
                    }

                }
            })
        }
    });

    $(".getLive").click(function () {
        var courseID = $(this).attr("data-cid");
        var id = $(this).attr("data-id");
        var isopen = $(this).attr("data-type");
        $.ajax(
            {
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=888&courseID=" + courseID + "&isopen=" + isopen + "",
                beforeSend: function () {
                },
                success: function (data) {
                    if (data != "000") {
                        if (data == "111") {
                            window.location.href = "/live/" + id + ".html";
                        }
                        else {

                        }
                    }
                    else {
                        $(".btn-login").trigger("click");
                    }
                }
            })
    });
});

function detateTabBorder(){
	$(".include-course-cont table").each(function(){
		$(this).find("tr").first().find("th").css({"borderTop":"none"});
		$(this).find("tr").last().find("td").css({"borderBottom":"none"});
		$(this).find("tr").each(function(){
			$(this).find("td,th").first().css({"borderLeft":"none"});
			$(this).find("td,th").last().css({"borderRight":"none"});
		});
	});
}
