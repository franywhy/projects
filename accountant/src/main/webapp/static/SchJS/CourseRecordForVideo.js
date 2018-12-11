var vid = "cc_" + $("#h_vid").val();
var _isAdd;
var SetTimeoutId = 0;
var SetTimeoutId2 = 0;
var _beginTime = 0;
var _endTime = 0;
var _isTry = 0;
var _isFinish = 0;
var i = 0;
$(function () {
    vid = "#cc_" + $("#h_vid").val();
    $(vid).on("loadedmetadata", function () {
        var vTime = (this).duration;
       $("#t").html(vTime);
    });

    SetTimeoutId = setInterval(function GetTime() {
        var vTime = 0;
        $(vid).on("timeupdate", function () {
            vTime = (this).currentTime;
            $("#t").html(vTime);
            $(vid).unbind("timeupdate");
        });

    }, 5000);
})

