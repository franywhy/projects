/*
 * Izb启动器
 * Izb.config设置的优先级:默认设置<全局设置<页面设置
 * <script src="seed.js" data-page="index" data-version="2.0.0"></script>
 * @return Izb.config
 */
var Izb = Izb || {};
window.__uri = function (uri) {
    return uri;
};
(function (Izb, config) {
    var EXP_ATTR = /^data-(\w+)-?(\w+)?/;
    var _scriptsTags = document.getElementsByTagName("script"),
        _curScript = _scriptsTags[_scriptsTags.length - 1];

    function _getArg(key, url, defaultVal) {
        return ((url || location.search).match(new RegExp("(?:\\?|&)" + key + "=(.*?)(?=&|$)")) || ["", defaultVal || ""])[1];
    }

    function _parseAny(val) {
        if (typeof val == 'string') {
            if (val !== "" && !isNaN(val)) {
                val = val - 0;
            } else if (val.toLowerCase() == "true") {
                val = true;
            } else if (val.toLowerCase() == "false") {
                val = false;
            }
        }
        return val;
    }

    function _capitalizeText(str) {
        if ("string" == typeof str) {
            return str.slice(0, 1).toUpperCase() + str.slice(1);
        }
        return "";
    }

    Izb.config = (function () {

        // config data-xxx => Izb.config.xxx; data-xxx-abc => Izb.config.xxxAbc
        for (var i = 0, m = _curScript.attributes.length; i < m; i++) {
            var attr = _curScript.attributes[i],
                akey = attr.name.toLowerCase().match(EXP_ATTR);
            if (akey) {
                config[akey[1] + _capitalizeText(akey[2])] = _parseAny(attr.value);
            }
        }

        // mode(auto|develop|release)
        if (!config.mode || "auto" == config.mode) {
            config.mode = (location.host == config.domain.main ? "release" : "develop");
        }

        // from
        config.from = _getArg("from");

        // pageClass
        var pageClass = "";
        if (config.page) {
            pageClass += " page-" + config.page;
        }
        if (config.from) {
            pageClass += " page-" + config.from;
        }
        if (pageClass) {
            (document.body || document.documentElement).className += pageClass;
        }

        //other

        // TODO:?
        if ("release" != config.mode) {
            config.domain.api = '@adminDomain@';
            config.domain.data = '@adminDomain@';
        }
        // TODO:?
        if (location.host == '@adminDomain@' || location.pathname.indexOf('@adminDomain@') > 0) {
            config.domain.main = '@adminDomain@';
            config.domain.data = '@adminDomain@';
        }

        return config;
    }());

}(Izb, {
    name: "恒企在线后台管理", //站点名称  会计城管理
    site: "admin", //站点标识
    isLog: true, //是否统计日志
    isProxy: false, //是否启用代理模式,启用后对于跨域的接口要做好服务器代理配置
    mode: "auto",
    version: "0.0.0",
    domain: {
        lib: "lib.kjcity.com",
        main: '@adminDomain@', //正式服务器域名
        app: '@adminDomain@', //正式服务器域名
        api: '@adminDomain@',
        data: '@adminDomain@'
    },
    path: {
        pic1: "/styles/images/asc-f8a194.gif",
        pic2: "/styles/images/asc.gif"
    },
    timeout: 60 * 10000,
    size: 15,
    defaultNav: "index",
    channelType: ["Web版", "Android版", "Android Pad版"], //对应的值 1，2，3
    channelMenu: ["qd_pc_log", "qd_mobile_log", "qd_anroidpad_log"],
    //payment_type: ["-", "支付宝", "信用卡", "储值卡", "移动卡充值", "联通卡充值", "电信卡充值", "快币支付", "MDO", "银联支付"],  //快播充值方式
    payment_type: {
        //图丽
        "kb": ["图丽"],
        //快播
        "km-": ["-", "支付宝", "信用卡", "储值卡", "移动卡充值", "联通卡充值", "电信卡充值", "快币支付", "MDO", "银联支付"],
        //3G
        "3g": ["-", "支付宝网页支付", "电信25元", "联通充值卡-易宝", "电信充值卡-易宝", "移动充值卡-易宝"]
    }
}));
