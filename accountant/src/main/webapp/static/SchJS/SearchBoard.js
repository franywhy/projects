$(document).ready(function () {
    setInterval("onKeepLive()", 60000);

    $("#txtQueryWord").on("keypress", function () {
        if (event.keyCode == 13) {
            search();
        }
    }
    );
    $("#txtQueryWord1").on("keypress", function () {
        if (event.keyCode == 13) {
            search();
        }
    }
    );


});

function onKeepLive() {
    $.ajax({
        url: '/lib/member/UserKeepLive.ashx',
        type: 'post',
        dataType: 'text',
        timeout: 100000,
        error: function () {
        },
        success: function () {
        }

    });

}

var key;
var type;
function search(){
//    alert(window.location.href);
    key = $("#txtQueryWord").val();
 
    if (key == '输入课程关键字搜索' || key == '') {
        key = $("#txtQueryWord1").val();
    }
    type = 1;
    var myURL = parseURL(window.location.href);
  
    if (key == '输入课程关键字搜索' || key == ''|| key==undefined) {

       
        if (window.location.href.indexOf("search") < 0) {
            window.location.href = "/search.html?type=" + type + "";
        }
        else {
            var newUrl = replaceUrlParams(myURL, { key: "",type:type, page: 1 });
            window.location.href = newUrl;
        }
    }
    else {
        $.ajax({
            url: "/lib/searchHandler.ashx",
            type: "POST",
            async: false,
            data: "type=1&key=" + key + "",
            beforeSend: function () {

            },
            success: function (result) {
            }

        })
        if (window.location.href.indexOf("search") < 0) {
            window.location.href = "/search.html?type=" + type + "&key=" + escape(key) + "";
        }
        else {
            var newUrl = replaceUrlParams(myURL, { key: escape(key), type: type, page: 1 });
            window.location.href = newUrl;
        }
        
    }
}

$(".search a ").live("click", function () {

    var mykey = $(this).attr('title');
    if (window.location.href.indexOf("search") < 0) {
        window.location.href = "/search.html?key=" + escape(mykey) + "";
    }
    else {
        var myURL = parseURL(window.location.href);

        var newUrl = replaceUrlParams(myURL, { key: escape(mykey), page: 1 });
        window.location.href = newUrl;
    }

});