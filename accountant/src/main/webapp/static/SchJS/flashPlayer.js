/// <reference path="jquery-1.8.3.min.js" />
function tryVidio(data) {
    var url = "http://p.bokecc.com/player?vid=" + data + "&siteid=FE7A65E6BE2EA539&autoStart=auto&width=600&height=450&playerid=CED4B0511C5D4992&playertype=1";
    var strScript = document.createElement("script");
    strScript.src = url;
    strScript.type = "text/javascript";
    $(".playback_img").empty();

    var a = new Dialog2("Test", "#dvTest", {
        "btnClose": function () { a.CloseDialog(); }
    }, true);
    //$(".playback_img").html(str);
    document.getElementById("playback_img").appendChild(strScript);
    a.ShowDialog();
}

function initMain2(flashUrl, contentID, width, height) {

    if (document.getElementById(contentID) == undefined || contentID == '') {
        return false;
    } else {
        if (width == undefined || width == 0) {
            width = 954;
        }
        if (height == undefined || height == 0) {
            height = 545;
        }
    }

    var fls = flashChecker();
    var s = "";
    if (!fls.f) {
        if (confirm("您的浏览器还没有安装Flash插件，现在安装？")) {
            window.location.href = "http://get.adobe.com/cn/flashplayer/";
        }
    }
    else {
        var tryUrl = flashUrl;

        var flashvars = {
            userid: "25CCD0665D668BCE",
            videoid: tryUrl,
            mode: "api",
            autostart: "true",
            jscontrol: "true"
        };
        var params = {
            allowFullscreen: "true",
            allowScriptAccess: "always",
            wmode: "transparent"
        };
        var attributes = {};
      
        swfobject.embedSWF("http://union.bokecc.com/flash/player.swf", contentID,
        width, height, "10.0.0", "expressInstall.swf", flashvars, params, attributes);
    }
};

function initMain(flashUrl, contentID, width, height,isAuto) {
try{
    if (isAuto == undefined || isAuto == null) {
        isAuto = true;
    } else {
   
    }}catch(e){
    isAuto = false;
    }
     if (document.getElementById(contentID) == undefined || contentID == '') {
        return false;
    } else {
        if (width == undefined || width == 0) {
            width = 954;
        }
        if (height == undefined || height == 0) {
            height = 545;
        }
    }

//    var fls = flashChecker();
//    var s = "";
//    if (!fls.f) {
//        if (confirm("您的浏览器还没有安装Flash插件，现在安装？")) {
//            window.location.href = "http://get.adobe.com/cn/flashplayer/";
//        }
//    }
   // else {
    var strScript = document.createElement("script");
        strScript.src = 'http://p.bokecc.com/player?vid=' + flashUrl + '&siteid=FE7A65E6BE2EA539&autoStart='+isAuto+'&width=' + width + '&height=' + height + '&playerid=CED4B0511C5D4992&playertype=1';
        strScript.type = 'text/javascript';
        $("#" + contentID).empty();
        document.getElementById(contentID).appendChild(strScript);
  //  }
}

function flashChecker() {
    var hasFlash = 0; //是否安装了flash  
    var flashVersion = 0; //flash版本  

    if (document.all) {
        var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
        if (swf) {
            hasFlash = 1;
            VSwf = swf.GetVariable("$version");
            flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
        }
    } else {
        if (navigator.plugins && navigator.plugins.length > 0) {
            var swf = navigator.plugins["Shockwave Flash"];
            if (swf) {
                hasFlash = 1;
                var words = swf.description.split(" ");
                for (var i = 0; i < words.length; ++i) {
                    if (isNaN(parseInt(words[i]))) continue;
                    flashVersion = parseInt(words[i]);
                }
            }
        }
    }
    return { f: hasFlash, v: flashVersion };
};

var customSeek = function (flashId, seek) {
  //  alert(flashId+"----"+seek);
    var player = getSWF(flashId);
 
    player.seek(seek);
};

var getPosition = function (flashId) {
    var player = getSWF(flashId);
    if (player.getPosition() == undefined) { return 0; }
    return player.getPosition();
};
var getDuration = function (flashId) {
    var player = getSWF(flashId);
    if (player.getDuration() == undefined) {
        return 0;
    }
    return player.getDuration();
};

function getSWF(swfID) {
    if (window.document[swfID]) {
        return window.document[swfID];
    } else if (navigator.appName.indexOf("Microsoft") == -1) {
        if (document.embeds && document.embeds[swfID]) {
            return document.embeds[swfID];
        }
    } else {
        return document.getElementById(swfID);
    }
}