/// <reference path="flashPlayer.js" />
var vid = "cc_" + $("#h_vid").val();
var _isAdd;
var SetTimeoutId = 0;
var SetTimeoutId2 = 0;
var _beginTime = 0;
var _endTime = 0;
var _isTry = 0;
var _isFinish = 0;
var GetEndInfo = function () {

    if ($("object").attr("id") != undefined) {
        if (getDuration(vid) > 0 && parseFloat(getDuration(vid)) <= (parseFloat(getPosition(vid)) + 2)) {

            var tmpCourseId = $("#h_courseid").val();
            var tmpChapterId = $("#h_chapterid").val();
            _beginTime = 0;
            _endTime = getDuration(vid);
            var tmpDml = 3;
            var tmpIsfinsh = 1;
            var tmpIstry = $("#h_isTry").val();
          
            $.ajax({
                cache: false,
                url: "/lib/member/CourseProgress.ashx",
                type: "POST",
                data: {
                    dml: tmpDml,
                    cid: tmpCourseId,
                    cpid: tmpChapterId,
                    istry: tmpIstry,
                    isfinish: tmpIsfinsh,
                    begintime: _beginTime,
                    endtime: _endTime
                },
                success: function (data) {
                    if (data == "1") {
                
                    } else {
                      //  alert(data);
                    }
                }
            });
        }
    }
}
function VideoRefresh() {

    var tmpCourseId = $("#h_courseid").val();
    var tmpChapterId = $("#h_chapterid").val();
    _beginTime = $("#h_endTime").val();
    _endTime = getPosition(vid);
    
    var tmpDml = 4;
    var tmpIsfinsh = 1;
    var tmpIstry = $("#h_isTry").val();
    $.ajax({
        cache: false,
        url: "/lib/member/CourseProgress.ashx",
        type: "POST",
        data: {
            dml: tmpDml,
            cid: tmpCourseId,
            cpid: tmpChapterId,
            istry: tmpIstry,
            begintime: _beginTime,
            endtime: _endTime
        },
        success: function (data) {
            if (data == "1") {
                $("#h_endTime").val(_endTime);
            } else {
                //  alert(data);
            }
        }
    });
}

function onPlayPaused() {
    //alert("onPlayPaused");
    if (SetTimeoutId != 0) {
        window.clearInterval(SetTimeoutId); SetTimeoutId = 0;

    }
    if (SetTimeoutId2 != 0) {
        window.clearInterval(SetTimeoutId2); SetTimeoutId2 = 0;
    }
  
}

function onPlayStop() {

    //alert("onPlayStop");
    if (SetTimeoutId != 0) {
        window.clearInterval(SetTimeoutId); SetTimeoutId = 0;

    }
    if (SetTimeoutId2 != 0) {
        window.clearInterval(SetTimeoutId2); SetTimeoutId2 = 0;
    }

    if (getPosition(vid) == getDuration(vid)) {
        if ($("#h_next_href").val() != "") {
            location.href = $("#h_next_href").val();
        }
    }
    // 已暂停播放
}

function onplayerready() {
 
}

function onPlayStart() {
   
    if ($("#h_seek").val() != "0" && $("#h_seek").val()!='' && $("#h_seek").val() != undefined) {
       
        customSeek(vid, $("#h_seek").val());
    }

    VideoRefresh();
    GetEndInfo();
    $("#h_endTime").val(getPosition(vid));
 
    if (SetTimeoutId == 0) {
        SetTimeoutId = setInterval(GetEndInfo, 1000);
    }

    if (SetTimeoutId2 == 0) {
        SetTimeoutId2 = setInterval(VideoRefresh, 3000);
    }
}

function onPlayResume() {
    //alert(" onPlayStop");
    if (SetTimeoutId == 0) {
        SetTimeoutId = setInterval(GetEndInfo, 1000);
    }
    if (SetTimeoutId2 == 0) {
        SetTimeoutId2 = setInterval(VideoRefresh, 3000);
    }
}

function on_cc_player_init(vid, objectID) {
    var config = {};
    config.on_player_pause = "onPlayPaused"; //设置当暂停播放时的回调函数的名称
    config.on_player_ready = "onplayerready";
    config.on_player_resume = "onPlayResume";
    config.on_player_stop = "onPlayStop";
    config.on_player_start = "onPlayStart";
    var player = getSWF(objectID);
    player.setConfig(config);
}
;
