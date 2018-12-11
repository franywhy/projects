/*----------点击查看协议--------*/
$(function () {
    $("#iscashback").click(function () {
        openStaticPopup1();
    });

});

  function AddFavourite() {
      var pid = $("#hiddepid").val();
    $.ajax({
            type: "POST",
            url: "/lib/product/ProductDetail.ashx",
            data: "pid=" + pid + "&type=2",
            cache: false,
            success: function (result) {
            if(result!="000")
            {
                $("#mode_tips").html("<span class='gtl_ico_succ'></span>" + result + "");
                openStaticPopup();
                setTimeout("$.closePopupLayer('myStaticPopup')", 2000);
            }
            else
            {
            var url=window.location.href;
            window.location.href= "login.html?ReturnUrl="+escape(url)+"";
            }
            }

        });
    
//    this.className='city_nav city_hover';document.getElementById('city_link').className='city_link city_hovers';
}

function AddShipping() {
    var pid = $("#hiddepid").val();
    $.ajax({
        type: "POST",
        url: "/lib/product/ProductDetail.ashx",
        data: "pid=" + pid + "&type=1",
        cache: false,
        success: function (result) {
            //            $("#viewText").html(result);
            $("#mode_tips").html("<span class='gtl_ico_succ'></span>" + result + "");
            openStaticPopup();
            setTimeout("$.closePopupLayer('myStaticPopup')", 2000);
        }
    });
    
//    this.className='city_nav city_hover';document.getElementById('city_link').className='city_link city_hovers';
    }
    try {
        $.setupJMPopups({
            screenLockerBackground: "#003366",
            screenLockerOpacity: "0.5"
        });
    } catch (e) { }
    function openStaticPopup() {
        $.openPopupLayer({
            name: "myStaticPopup",
            width: 1000,
            target: "myHiddenDiv1"
        });
    }

    function openStaticPopup1() {
        $.openPopupLayer({
            name: "myStaticPopup",
            width: 1200,
            target: "myHiddenDiv"
        });
    }