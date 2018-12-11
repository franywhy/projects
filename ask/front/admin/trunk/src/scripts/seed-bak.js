/*
 * Izb启动器
 * Izb.config设置的优先级:默认设置<全局设置<页面设置
 * <script src="scripts/seed.js?v=@VERSION@" data-page="index" data-style="base,index" data-script="jquery,core,app" data-is-log="true"></script>
 * @return Izb.config
 */
var Izb = Izb || {};
(function(Izb, opt) {
    var SEED_VER = '1.2.1',
        EXP_HTTP = /^http/,
        EXP_ASSET = /^asset\.(\w*)/,
        EXP_ATTR = /^data-(\w+)-?(\w+)?/;
    var _scriptsTags = document.getElementsByTagName("script"),
        _curScript = _scriptsTags[_scriptsTags.length - 1],
        _baseUrl, //基础路径      
        _style, //样式库
        _script, //脚本库        
        _config;

    //初始化配置
    (function() {

        //默认设置
        _config = {
            isCombo: true, //是否动态合并JS,需要Tengine服务器支持;调试模式下为false
            isConcat: false, //是否合并成一个js
            isLog: true, //是否统计日志
            isProxy: true //是否启用代理模式,启用后对于跨域的接口要做好服务器代理配置
        };

        //全局设置
        var config = _resolveConfig(opt.config);
        for (var key in config) {
            _config[key] = config[key];
        }

        //页面设置 data-xxx => Izb.config.xxx; data-xxx-abc => Izb.config.xxxAbc
        for (var i = 0, m = _curScript.attributes.length; i < m; i++) {
            var attr = _curScript.attributes[i],
                key = attr.name.toLowerCase().match(EXP_ATTR);
            if (key) {
                _config[key[1] + _capitalizeText(key[2])] = _parseAny(attr.value);
            }
        }

        _config.assetVersion = _getArg("v", _curScript.src, _config.assetVersion);

        //站点模式(@MODE@|auto|develop|release)        
        _config.mode = _config.mode || ((location.host == _config.domain.app) ? "release" : "develop");

        //站点的资源
        _config.asset = _getAsset(_config.domain);

        //站点来源
        _config.from = _getArg("from");

        _resolvePath(_config.path);

        _baseUrl = _resolvePath(_resolveConfig(opt.baseUrl));

        _style = _resolveConfig(opt.style);

        _script = _resolveConfig(opt.script);

    }());

    /**
     * 配置转换
     */

    function _resolveConfig(conf) {
        if ("function" == typeof conf) {
            return conf(_config);
        }
        return conf;
    }

    /**
     * 路径转换
     */

    function _resolvePath(obj) {
        if ("object" == typeof obj) {
            for (var key in obj) {
                obj[key] = _getPath(obj[key]);
            }
        }
        return obj;
    }

    /**
     * 资源路径
     */

    function _getAsset(domain) {

        var asset = {};

        for (var key in domain) {
            if (key != "base" && key != "app") {
                asset[key] = "http://" + domain[key];
            }
        }

        if (domain.app && "release" == _config.mode) {
            //http://www.izhubo.com/
            asset.base = "http://" + domain.base;
            asset.app = "http://" + domain.base + "/" + _config.type + "/" + _config.site;
        } else if (domain.app && 0 == location.pathname.indexOf("/" + domain.app + "/")) {
            //http://localhost/www.izhubo.com/
            asset.base = "/" + domain.base;
            asset.app = "/" + domain.app;
        } else {
            //http://localhost/
            //http://localhost/test/
            //http://www.izhubo.com/
            asset.base = "http://" + domain.base;
            asset.app = ((_curScript.src.match(/^(.*)scripts\/seed\.js/) || ["", ""])[1] || ".").replace(/\/$/, "");
        }

        return asset;
    }

    /**
     * 获取地址栏参数
     */

    function _getArg(key, url, defaultVal) {
        return ((url || location.search).match(new RegExp("(?:\\?|&)" + key + "=(.*?)(?=&|$)")) || ["", defaultVal || ""])[1];
    }

    /**
     * 代码转换
     */

    function _parseAny(val) {
        if (typeof val == 'string') {
            if (val != "" && !isNaN(val)) {
                val = val - 0;
            } else if (val.toLowerCase() == "true") {
                val = true;
            } else if (val.toLowerCase() == "false") {
                val = false;
            }
        }
        return val;
    }

    /**
     * 文件路径转换
     */

    function _getPath(url) {
        var type = (url.match(EXP_ASSET) || ['', ''])[1];
        if (type && _config.asset[type]) {
            return url.replace(EXP_ASSET, _config.asset[type]);
        }
        return url;
    }

    function _capitalizeText(str) {
        if ("string" == typeof str) {
            return str.slice(0, 1).toUpperCase() + str.slice(1);
        }
        return "";
    }

    /**
     * 获取文件源路径
     */

    function _getSrc(src, hasVer) {
        if (EXP_HTTP.test(src)) {
            // 绝对地址不转换
            return src;
        }
        if (hasVer && _config.assetVersion) {
            if (src.indexOf("?") == -1) {
                src += "?v=" + _config.assetVersion;
            } else {
                src += "&v=" + _config.assetVersion;
            }
        }
        return src;
    }

    /**
     * 输出资源
     */

    function _outRes(type, src) {
        var resContent = "";
        if ("css" == type) {
            resContent = '<link href="' + src + '" rel="stylesheet" />';
        } else if ("js" == type) {
            resContent = '<script src="' + src + '" ></sc' + 'ript>';
        }
        document.write(resContent);
    }

    /**
     * 输出样式或脚本
     */

    function _renderRes(res, type) {
        if (0 == res.length) {
            return;
        }
        type = (type == "style") ? "css" : "js";
        if (_config.mode == "release" && _config.isConcat) {
            return _outRes(type, _config.asset.app + "/" + (type == "css" ? "styles" : "scripts") + "/app." + type);
        }
        var resArr = {};
        for (var i = 0; i < res.length; i++) {
            var resArrKey = res[i];
            var origin = "css" == type ? _style[resArrKey] : _script[resArrKey];
            if (!origin) {
                continue;
            }
            var item = {
                key: resArrKey,
                type: type,
                combo: false,
                origin: origin
            };
            var resArrKey = item.key;
            /*
             * 路径的2种形式
             * pathKey/ 相对于pathKey,=>combo
             * http://  绝对路径
             */
            if (/^http/.test(item.origin)) {
                item.src = item.origin;
            } else if (/^\w/.test(item.origin)) {
                var index = item.origin.indexOf("/");
                item.combo = true;
                resArrKey = item.basePathKey = item.origin.substring(0, index);
                item.basePath = _baseUrl[item.basePathKey];
                item.comboFile = item.origin.substring(index + 1);
                item.src = item.basePath + _getSrc(item.comboFile, true);
            } else {
                continue;
            }

            if (!resArr[resArrKey]) {
                resArr[resArrKey] = [];
                resArr[resArrKey].combo = item.combo;
                resArr[resArrKey].basePath = item.basePath || "";
            }
            resArr[resArrKey].push(item);
        }
        for (var key in resArr) {
            var resItem = resArr[key],
                isCombo = _config.mode == "release" && _config.isCombo && resItem.combo && resItem.length > 1,
                path = resItem.basePath + "??";
            for (var i = 0, len = resItem.length; i < len; i++) {
                var res = resItem[i];
                if (isCombo && res.type == "js") {
                    path += res.comboFile;
                    if (i < (len - 1)) {
                        path += ",";
                    } else {
                        path += "?v=" + _config.assetVersion;
                        _outRes(type, path);
                    }
                } else {
                    _outRes(res.type, res.src);
                }
            }
        }
    }

    /**
     * 输出
     */

    function _render(type) {
        _renderRes(_config[type].split(","), type);
    }

    Izb.config = _config;

    Izb.seed = {
        init: function() {

            var pageClass = "";
            if (_config.page) {
                pageClass += " page-" + _config.page;
            }
            if (_config.from) {
                pageClass += " page-" + _config.from;
            }
            document.body.className += pageClass;

            _render("script");

            delete Izb.seed;
        }
    };

    _render("style");

}(Izb, {
    //Izb.config:默认设置<全局设置<页面设置
    config: function() {
        var conf = {
            name: "爱主播秀场管理系统", //站点名称
            site: "admin", //站点标识
            type: "admin", //站点类型(app|wap|admin)
            mode: "release",
            siteVersion: "v2.5", //项目版本号
            assetVersion: "@VERSION@", //资源版本号
            domain: {
                base: 'ttasset.app@appid@.twsapp.com', //资源服务器域名,cdn加速
                app: 'ttadmin.app@appid@.twsapp.com', //正式服务器域名
                api: 'ttapi.app@appid@.twsapp.com',
                data: 'ttadmin.app@appid@.twsapp.com'
            },
            isProxy: false,
            path: {},
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
        };
        if ("release" != conf.mode) {
            conf.domain.api = 'ttestapi.app@appid@.twsapp.com';
            conf.domain.data = 'tttstadm.app@appid@.twsapp.com';
        }

        if (location.host == "localhost") {
            //conf.isProxy = true;
        }

        if (location.host == "ttadmin.app@appid@.twsapp.com" || location.pathname.indexOf('ttadmin.app@appid@.twsapp.com') > 0) {
            //conf.domain.app = 'ttadmin.app@appid@.twsapp.com';
            //conf.domain.data = 'www.izhubo.com';
            conf.domain.app = 'ttadmin.app@appid@.twsapp.com';
            conf.domain.data = "ttadmin.app@appid@.twsapp.com";
        }



        return conf;
    },
    //基础Url
    baseUrl: {
        "baseStyles": "asset.base/base/styles/",
        "baseScripts": "asset.base/base/scripts/",
        "proScripts": "asset.base/pro/",
        "appStyles": "asset.app/styles/",
        "appScripts": "asset.app/scripts/"
    },
    //样式库
    style: {
        "base": "baseStyles/base.css",
        "artDialog": "baseScripts/ui/artDialog/skins/green.css",
        "manage": "appStyles/manage.css",
        "login": "appStyles/login.css"
    },
    //脚本库
    script: {
        //基础库
        "jquery": "baseScripts/jquery.js",
        "standard": "baseScripts/util/standard.js",
        "oo": "baseScripts/util/oo.js",
        "util": "baseScripts/util/util.js",
        "form": "baseScripts/util/jquery.form.js",
        "baiduTemplate": "baseScripts/util/baiduTemplate.js",
        "query": "baseScripts/util/query.js",
        "history": "baseScripts/util/history.js",
        "json2": "baseScripts/util/json2.js",
        "cookie": "baseScripts/util/cookie.js",
        "pager": "baseScripts/ui/pager.js",
        "tablesorter": "baseScripts/ui/tablesorter.js",
        "validate": "baseScripts/util/validate.js",
        "artDialog": "baseScripts/ui/artDialog/jquery.artDialog.js",
        "wdatePicker": "baseScripts/ui/My97DatePicker/WdatePicker.js",
        "uploadify": "baseScripts/ui/uploadify/uploadify.js",
        //业务库
        "core": "proScripts/core/1.0.0/core.js",
        //"core": "appScripts/core.js",
        "ui": "appScripts/ui.js",
        "common": "appScripts/common.js",
        "user": "appScripts/user.js",
        "controller": "appScripts/controller.js",
        "module": "appScripts/module.js",
        "main": "appScripts/main.js",
        "login": "appScripts/login.js",
        "union": "appScripts/union.js"
        //"formValidate": "appScripts/formValidate.js"
    }
}));

/*
config.dataRoot = 'http://www.izhubo.com';
config.apiDataRoot = 'http://ttapi.app@appid@.twsapp.com';
config.dataRoot = 'http://ttestshow.app@appid@.twsapp.com';
config.apiDataRoot = 'http://ttestapidt.app@appid@.twsapp.com';
*/
