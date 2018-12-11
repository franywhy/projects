function getParameter(param) {
    var locString = String(window.document.location.href);
    var rs = new RegExp("(^|)" + param + "=([^&]*)(&|$)", "gi").exec(locString), tmp;
    if (tmp = rs) {
        return tmp[2];
    }
    return "";
}
function getApiUrl() {
    return "http://api.kjcity.com";
}
function getApiUrl1() {
    return "http://api.kjcity.com";
}
function reinitIframe() {
    try {
        var ifm = document.getElementById("iframepage");
        var subWeb = document.frames ? document.frames["iframepage"].document : ifm.contentDocument;
        if (ifm != null && subWeb != null) {
            ifm.height = subWeb.body.scrollHeight;
            ifm.width = subWeb.body.scrollWidth;
        }
    }
    catch (ex) { }
}