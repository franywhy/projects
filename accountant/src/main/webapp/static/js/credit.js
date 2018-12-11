$(document).ready(function (e) {
    var id = $("#hidMemberID").val();
    var key = $("#hidsecretKey").val();
    $.ajax({
        type: "GET",
        url: "http://axdd.kjcity.com/credit/getMyCredit.json?studentid=" + id + "&secretKey=" + key,
        //        dataType: "html",
        beforeSend: function () {
        },
        success: function (json) {
            $(".main-big-box").append(json);
        },
        complete: function () {
            $(".main-big-box a").click(function () {
                var classId = $(this).attr('rel');
                $.ajax({
                    type: "POST",
                    url: "http://axdd.kjcity.com/credit/getCashCoupon.json?studentid=" + id + "&secretKey=" + key + "&classId=" + classId,
                    dataType: "json",
                    beforeSend: function () {
                    },
                    success: function (json) {
                        if (json.code == 0) {
                            $.ajax({
                                url: "/lib/member/creditHandler.ashx",
                                type: "POST",
                                data: "type=1&number=" + json.number + "&classId=" + classId + "",
                                beforeSend: function () {

                                },
                                success: function (data) {
                                    $.ajax({
                                        type: "GET",
                                        url: "http://axdd.kjcity.com/credit/getMyCredit.json?studentid=" + id + "&secretKey=" + key,
                                        beforeSend: function () {
                                            $(".main-big-box").html("");
                                        },
                                        success: function (json) {
                                            $(".main-big-box").append(json);
                                        }
                                    });
                                }
                            })
                        }
                    },
                    complete: function () {

                    },
                    error: function (data) {

                    }
                });
            });
        },
        error: function (data) {

        }
    });

});