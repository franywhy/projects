//转换时间为mm'MM"
var ConvertTime = function (second) {
    try {
        var tmpMin = parseInt(parseInt(second) / 60) + "'" + parseInt(second) % 60+'"';
        return tmpMin;
    } catch (e) {
        return 0;
    }

}