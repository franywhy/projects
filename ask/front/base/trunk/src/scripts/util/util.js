/**
 * Izb Script 架构
 * http://www.izhubo.com
 */

/**
 * 工具扩展
 */
(function($) {
    //公用方法  
    $.extend($, {

        /**
         * @description 判断变量是否存在,或者是否以某种类型存在
         * @param {object} o  判断变量是否存在
         * @param {object} type 数据类型  Number,Boolean等
         * @return {Boolean} nResult 返回结果 true或者false
         */
        exists: function(o, type) {
            return o != undefined && o !== null && (type ? o.constructor == type : true);
        },
        /**
         * @description 把任意类型转成Boolean
         * @param {object} o  任意对象
         * @return {Boolean} nResult 返回结果 true或者false
         */
        parseBoolean: function(o) {
            var flag = !! o;
            return (flag && typeof(o) == "string" && (o
                .toLowerCase() == "false" || o.toLowerCase() == "null" || o.toLowerCase() == "undefined" || o == "0")) ? false : flag;
        },
        /**
         * @description 把字符串自动转换成它原来的类型
         * @param val o  任意对象
         * @return val any
         */
        parseAny: function(val) {
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
        },
        /**
         * @description 将?key1=value1&key2=value2&...转换成一个对象{key1:value1,key2:value2....}
         * @param {String} string  String字符串
         * @return {Object} obj 返回结果 {key1:value1,key2:value2....}
         */
        parseParam: function(str) {
            var obj = {};
            if (str == undefined || str == null) {
                return obj;
            }

            if (typeof str == 'object') {
                return str;
            }

            if (typeof str == 'string') {
                str = decodeURIComponent(str);
            }

            //踢出前缀#或者？
            str = str.replace(/^[\?\#]/, "");
            //分割参数
            var params = str.split("&");

            for (var i = 0; i < params.length; i++) {
                var item = params[i].split("=");
                var key = item[0];
                var val = item[1];

                if (!key) {
                    continue;
                }

                //类型转换
                if (val == undefined) {
                    val = true;
                } else {
                    val = this.parseAny(val);
                }
                obj[key] = val;
            }
            return obj;
        },
        /**
         * @description 占位符格式化 $.format("http://{0}/{1}/{2}","www.meizz.com", "web", "abc.htm"));
         * @return {String} obj 返回结果 http://www.meizz.com/web/abc.html
         */
        format: function() {
            if (arguments.length <= 1) {
                return arguments[0];
            }
            var str = arguments[0];
            for (var i = 1; i < arguments.length; i++) {
                var t = arguments[i];
                if (!this.exists(t, false)) {
                    t = '';
                }
                str = str.replace(new RegExp("\\{" + (i - 1) + "\\}", "g"), t);
            }
            return str;
        },
        /**
         * @description 占位符格式化
         * @param {String} str 需要替换的模板
         * @param {Object} params 参数
         * @param {Boolean} isEncode 是否编码
         * @eg demo("http://www.baidu.com?name={name}&age={age}&chanelid={chanelid}",{name:"demo",age:23,chanelid:100},false)
         * @return {String} str 返回结果 http://www.baidu.com?name=demo&age=23&chanelid=100
         */
        formatByVal: function(str, params, isEncode) {
            if (typeof params == "object") {
                for (var key in params) {
                    if (!$.exists(params[key]) || params[key] == "undefined" || params[key] == "null") {
                        params[key] = "";
                    }
                    str = str.replace(new RegExp("\\{" + key + "\\}", "ig"), isEncode ? encodeURIComponent(params[key]) : params[key]);
                }
            }
            return str.replace(/\{\w*\}/ig, "");
        },
        //过滤集合中重复的值
        distinctArray: function(arr, pty) {
            var isEqual = function(obj1, obj2) {
                //两个对象地址相等，必相等
                if (obj1 === obj2) {
                    return true;
                }
                if (typeof(obj1) == typeof(obj2)) {
                    if (pty && typeof(obj1) == "object" && typeof(obj2) == "object") {
                        if (!isEqual(obj1[pty], obj2[pty])) {
                            return false;
                        }
                        return true;
                    } else if (typeof(obj1) == "object" && typeof(obj2) == "object") {
                        var pcount = 0;
                        for (var p in obj1) {
                            pcount++;
                            if (!isEqual(obj1[p], obj2[p])) {
                                return false;
                            }
                        }
                        for (var p in obj2) {
                            pcount--;
                        }
                        return pcount == 0;
                    } else if (typeof(obj1) == "function" && typeof(obj2) == "function") {
                        if (obj1.toString() != obj2.toString()) {
                            return false;
                        }
                    } else {
                        if (obj1 != obj2) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
                return true;
            }
            var temp = arr.slice(0); //数组复制一份到temp
            for (var i = 0; i < temp.length; i++) {
                for (j = i + 1; j < temp.length; j++) {
                    if (isEqual(temp[j], temp[i])) {
                        temp.splice(j, 1); //删除该元素
                        j--;
                    }
                }
            }
            return temp;
        },
        /**
         * 去掉字符串起始的制定字符
         * @method trimStart
         * @param <String> text 指定字符串
         * @param <String> trimStr 替换字符串
         */
        trimStart: function(text, trimStr) {
            return (text || "").replace(new RegExp("^" + trimStr + "*", "g"), "");
        },

        /**
         * 去掉字符串结尾的制定字符
         * @method trimEnd
         * @param <String> text 指定字符串
         * @param <String> trimStr 替换字符串
         */
        trimEnd: function(text, trimStr) {
            return (text || "").replace(new RegExp(trimStr + "*$", "g"), "");
        },
        startWith: function(s, d) {
            return new RegExp("^" + d).test(s);
        },
        /** 
         * 获取标准表单数据，转换为JSON数据格式
         * @method serializeForm
         * @example $.serializeForm("#panelId");
         * isParse是否转化类型
         * @return <Object> data
         */
        serializeForm: function(panel, isParse) {
            var data = {};
            panel = $(panel);
            if (panel.length == 0) {
                return data;
            }

            $("input", panel).each(function() {
                var item = $(this);
                var name = item.attr("name");
                if (!name || $.exists(data[name])) {
                    return;
                }
                switch (item.attr("type")) {
                    case "radio":
                        // DOTO: 当 input[type="radio"][disabled="disabled"] 为禁用
                        if (!this.disabled && this.checked) {
                            data[name] = item.val();
                        }
                        break;
                    case "checkbox":
                        // DOTO: 当 input[type="checkbox"][disabled="disabled"] 为禁用
                        if (this.disabled) {
                            break;
                        }

                        data[name] = "";
                        $("input[type='checkbox'][name='" + name + "']:checked", panel).each(function() {
                            data[name] += this.value + ",";
                        })
                        data[name] = $.trimEnd(data[name], ',');
                        break;
                    case "button":
                    case "reset":
                    case "submit":
                        break;
                    default:
                        // DOTO: 当 input[type="text"][disabled="disabled"] 为禁用
                        if (!this.disabled) {
                            data[name] = $.trim(item.val());
                        }
                        break;
                }
                data[name] = isParse ? $.parseAny(data[name]) : data[name];
            });
            $("textarea", panel).each(function() {
                if (!this.name) {
                    return;
                }
                data[this.name] = $.parseAny(this.value);
            });
            $("select", panel).each(function() {
                if (!this.name) {
                    return;
                }
                data[this.name] = isParse ? $.parseAny(this.value) : this.value;
            });
            return data;
        },
        setFormData: function($form, data) {
            $form = $($form);
            if ($form.length == 0 || !$.isPlainObject(data)) {
                return;
            }
            var nodes = $($form).get(0).elements;
            var i = 0,
                iLen = nodes.length;
            var j, jLen, item, name, type, select, value, existsVal;

            for (; i < iLen; i++) {
                item = nodes[i];
                name = item.name;
                type = item.type;
                value = data[name];

                if (!name || item.disabled || !$.exists(value) || "file"==type) {
                    continue;
                }

                if ("radio" == type) {
                    item.checked = item.value == (value + "");
                } else if ("checkbox" == type || "select-multiple" == type) {

                    if ($.isArray(value)) {
                        //把值转化成字符串
                        for (var n = 0; n < value.length; n++) {
                            value[n] += "";
                        }
                    } else {
                        value = (value + "").split(',');
                    }

                    if ("checkbox" == type) {
                        item.checked = $.inArray(item.value, value) != -1;
                    } else {
                        select = item.options;
                        for (j = 0, jLen = select.length; j < jLen; j++) {
                            item = select[j];
                            item.selected = $.inArray(item.value, value) != -1;
                        }
                    }
                } else {
                    if (undefined != value) {
                        item.value = value;
                    }
                }
            }
        },
        /**
         * 重置表单
         * @method resetForm
         * @example $.resetForm("#myFormId");
         * @param formId #+表单Id
         */
        resetForm: function(formId) {
            $(formId).get().reset();
        },
        /**
         * 将 url后面的查询字符串转成对象表
         */
        queryToObject: function(){
            var search = location.search;
            var nn = {};
            if(search != ''){
                search = search.substr(1);
                $.each(search.split('&'), function(i, n){
                    var mm;
                    var key = null, value = null;
                    $.each(n.split('='), function(j, m){
                        if(j == 0){
                            key = m;
                        }
                        else if(j == 1){
                            value = m;
                        }
                    });
                    if(key != null && value != null){
                        mm = nn[key] != null ? nn[key] : [];
                        mm[mm.length] = value;
                        nn[key] = mm;
                    }
                });
            }
            return nn;
        }
    });
}($));

/**
 * Util组件
 * Uitl $工具包扩展
 */
(function($) {
    //公用方法  
    $.extend($, {
        /**
         * 准确的获得浏览器内核  和版本
         *  获得浏览器外壳和版本
         * @author kevin
         */
        UA: (function() {
            //浏览器外壳类型（国内常见的浏览器有：360浏览器、傲游、腾讯QQ\TT浏览器、世界之窗、彗星浏览器、绿色浏览器、传统IE、谷歌Chrome、网景netscape、火狐、Opera、苹果Safari等等）
            var engine = {
                type: "",
                version: $.browser.version
            },
                ua = navigator.userAgent.toLowerCase(),
                shell = {
                    type: "",
                    version: ""
                },
                shellArr = ["se360", "se", "maxthon", "qq", "tt", "theworld", "cometbrowser", "greenbrowser", "ie", "chrome", "netscape", "firefox", "opera", "safari", "konq"];

            for (var i in shellArr) {
                var type = shellArr[i];
                if (typeof type === "string") {
                    var regexp = null;
                    switch (type) {
                        case "se360":
                            regexp = /360se(?:[ \/]([\w.]+))?/;
                            break;
                        case "se":
                            regexp = /se ([\w.]+)/;
                            break;
                        case "qq":
                            regexp = /qqbrowser\/([\w.]+)/;
                            break;
                        case "tt":
                            regexp = /tencenttraveler ([\w.]+)/;
                            break;
                        case "safari":
                            regexp = /version\/([\w.]+)/;
                            break;
                        case "konq":
                            regexp = /konqueror\/([\w.]+)/;
                            break;
                        case "netscape":
                            regexp = /navigator\/([\w.]+)/;
                            break;
                        default:
                            regexp = RegExp(type + "(?:[ \\/]([\\w.]+))?");
                    }
                    if (regexp.test(ua)) {
                        shell.version = window.opera ? window.opera.version() : RegExp.$1 ? RegExp.$1 : "unknown"; //浏览器外壳版本
                        shell.type = type; //浏览器外壳类型
                        break;
                    }
                }
            }

            for (var key in $.browser) {
                if ($.browser[key] == true) {
                    engine.type = key;
                    break;
                }
            }

            if ($.browser.safari && $.browser.webkit) {
                engine.type = "webkit";
            }
            //返回浏览器内核与外壳的类型和版本：engine为内核,shell为外壳
            return {
                shell: shell,
                engine: engine
            };
        }()),
        /**
         * Helper function to parse the user agent.  Sets the following
         * .os.webkit
         * .os.android
         * .os.ipad
         * .os.iphone
         * .os.webos
         * .os.touchpad
         * .os.blackberry
         * .os.opera
         * .os.fennec
         * @api private
         */
        os: (function() {
            var os = {}, userAgent = navigator.userAgent,
                type = "";
            os.webkit = userAgent.match(/WebKit\/([\d.]+)/) ? true : false;
            os.android = userAgent.match(/(Android)\s+([\d.]+)/) || userAgent.match(/Silk-Accelerated/) ? true : false;
            os.ipad = userAgent.match(/(iPad).*OS\s([\d_]+)/) ? true : false;
            os.iphone = !os.ipad && userAgent.match(/(iPhone\sOS)\s([\d_]+)/) ? true : false;
            os.webos = userAgent.match(/(webOS|hpwOS)[\s\/]([\d.]+)/) ? true : false;
            os.touchpad = os.webos && userAgent.match(/TouchPad/) ? true : false;
            os.ios = os.ipad || os.iphone;
            os.blackberry = userAgent.match(/BlackBerry/) || userAgent.match(/PlayBook/) ? true : false;
            os.opera = userAgent.match(/Opera Mobi/) ? true : false;
            os.fennec = userAgent.match(/fennec/i) ? true : false;
            os.desktop = !(os.ios || os.android || os.blackberry || os.opera || os.fennec);

            for (var key in os) {
                if (os[key]) {
                    type += key + ",";
                }
            }
            os.type = $.trimEnd(type, ",");
            return os;
        }()),
        /**
         * CRC加密
         * @param  lrcid+title+artist
         */
        CRC32: function(str) {
            //str = encodeURIComponent(str);
            var Crc32Table = new Array(256);
            var i, j;
            var Crc;
            for (i = 0; i < 256; i++) {
                Crc = i;
                for (j = 0; j < 8; j++) {
                    if (Crc & 1)
                        Crc = ((Crc >> 1) & 0x7FFFFFFF) ^ 0xEDB88320;
                    else
                        Crc = ((Crc >> 1) & 0x7FFFFFFF);
                }
                Crc32Table[i] = Crc;
            }
            if (typeof str != "string") str = "" + str;
            Crc = 0xFFFFFFFF;
            for (i = 0; i < str.length; i++)
                Crc = ((Crc >> 8) & 0x00FFFFFF) ^ Crc32Table[(Crc & 0xFF) ^ str.charCodeAt(i)];
            Crc ^= 0xFFFFFFFF;
            return (Crc >> 3).toString(16);
        },
        /**
         * 计算一个字符串包含的单双字节个数
         * @param  string  字符串
         * @return {s:0,d:5}
         */
        sdouble: function(str) {
            if (!str) {
                return {
                    s: 0,
                    d: 0
                };
            }
            // s是单字节 d是双字节
            var s = 0,
                d = 0;
            // 循环计数
            var i;
            for (i = 0; i < str.length; i++) {
                if (str.charCodeAt(i) > 255) {
                    // 表示的是双字节
                    d += 1;
                } else {
                    s += 1;
                }
            }
            // 如果全部是单字节字符，单字节长度就是.length
            return {
                s: s,
                d: d
            };
        },
        /**
         * 清除选中文本
         */
        clearSelection: ('getSelection' in window ? function() {
            window.getSelection().removeAllRanges();
        } : function() {
            try {
                document.selection.empty();
            } catch (e) {};
        }),
        encodeHTML: function(source) {
            return String(source)
                .replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;')
                .replace(/\\/g, '&#92;')
                .replace(/"/g, '&quot;')
                .replace(/'/g, '&#39;');
        }
    });
}($));
