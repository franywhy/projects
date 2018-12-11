$(document).ready(function () {
    $(function () {
        document.onkeydown = function (e) {
            var ev = document.all ? window.event : e;
            if (ev.keyCode == 13) {
                var username = $("#txt_Username").val();
                var userpass = $("#txt_Password").val();
                var check = document.getElementById("cb_Check").checked;
                var timeout = 30;
                if (check == true) {
                    timeout = 5440;
                }
                $.ajax({
                    type: "POST",
                    url: "lib/loginHandler.ashx",
                    data: {
                        type: "1",
                        name: username,
                        pass: userpass,
                        timeout: timeout
                    },
                    cache: false,
                    beforeSend: function () {
                        $("#login").html("正在登录...");
                        $("#login-tip").empty();
                    },
                    success: function (result) {
                        alert(result);
                        if (result == '000') {
                            window.location.reload();
                        }
                        else {
                            $("#login-tip").html("该用户名不存在或密码不正确");
                            $("#login").html("登录");
                        }
                    }
                });

            }
        }
    });

    $("#login").live("click", function () {
        var username = $("#txt_Username").val();
        var userpass = $("#txt_Password").val();
        var check = document.getElementById("cb_Check").checked;
        var timeout = 30;
        if (check == true) {
            timeout = 5440;
        }
        $.ajax({
            type: "POST",
            url: "lib/loginHandler.ashx",
            data: {
                type: "1",
                name: username,
                pass: userpass,
                timeout: timeout
            },
            cache: false,
            beforeSend: function () {
                $("#login").html("正在登录...");
                $("#login-tip").empty();
            },
            success: function (result) {
                alert(result);
                if (result == '000') {
                    window.location.reload();
                }
                else {
                    $("#login-tip").html("该用户名不存在或密码不正确");
                    $("#login").html("登录");
                }
            }
        });
    });

})