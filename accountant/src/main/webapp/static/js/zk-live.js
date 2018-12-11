$.module('zk.live.liveController', function () {
	$("#gsVideo").css({"top":0});
    var nickName = "";
    var _id;
    var live_id;
    //初始化直播属性
    var site = "hqzk.gensee.com:80";
    var order_interval = null, // 循环定时器
        order_interval_time = 20000  //循环时间

    var Livedata = {};
    var live_state = 0;//0 停止 1 直播

    var headerKeyCode = "play";
    var isDocMain = true;
    //  音量调节条
    var vol=70;    //70%音量值  需要赋初值
    var volume=0.0;
    var statu = false;
    var $box = $(".live-speak .tips ul");
    var $btn = $(".live-speak .tips ul em");
    var $bar = $(".live-speak .tips ul h5");
    var ox = 0;
    var lx = 0;
    var top = 56-(56*(vol/100));
    $btn.css('top',top);
    $bar.css("height",56-top);
    /*展示互动相关JS*/
    var Channel;
    /*在线人数*/
    var onLineNum = 0;
    var genseeIconList = [];
    var genseeIconUrl = "";
    var customEmoji = {
        "[扣1]":{"file":"a12x.png"},
        "[扣2]":{"file":"a22x.png"},
        "[666]":{"file":"a12x.png"},
        "【赞同】":{"file":"brow_zt.png"},
        "【再见】":{"file":"brow_zj_ui.png"},
        "【你好】":{"file":"brow_nh_ui.png"},
        "【无聊】":{"file":"brow_wl_ui.png"},
        "【太慢了】":{"file":"brow_tml.png"},
        "【太快了】":{"file":"brow_tkl.png"},
        "【礼物】":{"file":"brow_lw.png"},
        "【值得思考】":{"file":"brow_zdsk.png"},
        "【流汗】":{"file":"brow_lh_ui.png"},
        "【鄙视】":{"file":"brow_bs_ui.png"},
        "【鼓掌】":{"file":"brow_gz.png"},
        "【伤心】":{"file":"brow_sx_ui.png"},
        "【愤怒】":{"file":"brow_fn_ui.png"},
        "【高兴】":{"file":"brow_gx_ui.png"},
        "【疑问】":{"file":"brow_yw_ui.png"},
        "【鲜花】":{"file":"brow_xh.png"},
        "【凋谢】":{"file":"brow_dx.png"},
        "【反对】":{"file":"brow_fd_ui.png"}

    };
    /*屏蔽字*/
    var keywords = "中共,中公,加我QQ,加我一下,我们一起买一个,合买一个,加我群号,谁领到发我一下,没用,打水漂,大爷,婊子,cao,LOL,猪,jb,正事,吊,屌,闲话,不回,外挂,微信,weixin,啰嗦,好假,传销,购物,罗索,390,讨论群,590,三百九,五百九,讲重点,废话,不太好,扣扣,加群,举报,##JOIN##,优惠,qq,别报,垃圾,日结,淘宝,无需费用,加Q,不好,奇葩,差,二逼,566,233,育德,华图,屏蔽,戈比,个比,犊子,鳖,王八,玩意,尼玛,脑残,宣传手段,营销,不是免费吗,屁,脸,tuo,滚,听腻,煞笔,洗脑,炮,臭,做爱,擦,sb,shabi,拖,托,狐逻,胡洛,广告,妹的,丫,马扁,吹牛,仁和,中华会,投诉,态度不好,东奥,huluo,菊花,太差,课不好,神经,麻痹,fuck,中华网,骗子,无忧考吧,推销,群,q,分期,老湿,喜感,自己学,通过率,自学,骗人,贱人,瘫痪,恶心,搞笑,找不到工作,卖身,坐台,股票,君,羊,吹捧,傻逼,傻逼老师,拉生源,几把,1450,550,1250,不讲,贵,坑,没钱,不要钱,卡,宣传,糊弄,快讲课,赶紧讲课,聊天,轻松过关,梦想成真,中华,生意,吹,岛,dao,会计岛,什么鬼,招生,系统消息,W0E0L0C0O0M0E,骗";
    var validKeywords = null;
    if (keywords != null && keywords != "") {
        validKeywords = keywords.split(",");
    }
    /*最终声音百分比*/
    var finalVoice = 1;

    /*读取emoji表情*/
    var initIconData = function () {
        var dataPath = "http://static.gensee.com/webcast/static/emotion/icon.json";
        $.getJSON(dataPath, function (data) {
            genseeIconUrl = data.prefix;
            genseeIconList = data.list;
        });
    };

    var tongjiNum = 0;
    setInterval(function () {
        tongjiNum++;
    }, 1000);

    var liveObject = {
        property: {
            //slTeacher: $("#slTeacher"),
            onLineNum: $("#onLineNum"),
            charMsgForm: $("#msgForm")
        }
    };

    /*在线人数增加随机数*/
    var onLineNumAdd = [-2, -1, 1, 3, 7, 10];
    //liveObject.property.onLineNum.text(800 + Math.ceil(Math.random() * 600));
    var genseeNum;
    //var index = Math.ceil(Math.random() * 5);
    liveObject.property.onLineNum.text(genseeNum);
    /*在线人数随机增加*/
    /*setInterval(function () {
        onLineNum = Channel.send("onUserOnline");
        var genseeNum = onLineNum;
        var index = Math.ceil(Math.random() * 5);
        liveObject.property.onLineNum.text(liveObject.property.onLineNum.text() * 1 + onLineNumAdd[index] * 1 + genseeNum * 1);
    }, 60 * 1000);*/
    setInterval(function () {
        onLineNum = Channel.send("onUserOnline");
        var genseeNum = onLineNum;
        //var index = Math.ceil(Math.random() * 5);
        liveObject.property.onLineNum.text(genseeNum);
    }, 60 * 1000);

    var Util = {
        trim: function (str) {
            if (typeof str !== "string") {
                return str;
            }
            if (typeof str.trim === "function") {
                return str.trim();
            } else {
                return str.replace(/^(\u3000|\s|\t|\u00A0)*|(\u3000|\s|\t|\u00A0)*$/g, "");
            }
        },
        isEmpty: function (obj) {
            if (obj === undefined) {
                return true;
            } else if (obj == null) {
                return true;
            } else if (typeof obj === "string") {
                if (this.trim(obj) == "") {
                    return true;
                }
            }
            return false;
        },
        isNotEmpty: function (obj) {
            return !this.isEmpty(obj);
        },
        currentTime: function () {
            return this.formatDate(new Date());
        },
        calcPercent: function (value, total) {
            if (isNaN(value) || Number(value) == 0)return "0";
            if (isNaN(total) || Number(total) == 0)return "0";
            return Math.round(Number(value) * 100 / Number(total));
        },
        round: function (number, fractionDigits) {
            fractionDigits = fractionDigits || 2;
            with (Math) {
                return round(number * pow(10, fractionDigits)) / pow(10, fractionDigits);
            }
        },
        timeDuration: function (second) {
            if (!second || isNaN(second))return;
            second = parseInt(second);
            var time = '';
            var hour = second / 3600 | 0;
            if (hour != 0) {
                time += checkTime(hour) + ':';
            } else {
                time += '00:';
            }
            var min = (second % 3600) / 60 | 0;
            time += checkTime(min) + ':';
            var sec = (second - hour * 3600 - min * 60) | 0;
            time += checkTime(sec);
            return time;
        },
        formatDate: function (date) {
            var h = date.getHours();
            var m = date.getMinutes();
            var s = date.getSeconds();
            return checkTime(h) + ":" + checkTime(m) + ":" + checkTime(s);
        },
        formatTime: function (time) {
            var date = new Date();
            date.setTime(time);
            var h = date.getHours();
            var m = date.getMinutes();
            var s = date.getSeconds();
            return checkTime(h) + ":" + checkTime(m) + ":" + checkTime(s);
        },
        formatText: function (text) {
            text = text.replace(" ", "&nbsp;");
            text = text.replace(/\n/g, "<br/>");
            return text;
        },
        formatUrl: function (content) {
            var reg = /(?:<img.+?>)|(http[s]?|(www\.)){1}[\w\.\/\?=%&@:#;\*\$\[\]\(\){}'"\-]+([0-9a-zA-Z\/#])+?/ig,
                content = content.replace(reg, function (content) {
                    if (/<img.+?/ig.test(content)) {
                        return content;
                    } else {
                        return '<a class="msg-url" target="_blank" href="' + content.replace(/^www\./, function (content) {
                            return "http://" + content;
                        }) + '">' + content + '</a>'
                    }

                });
            return content;
        },
        //占位符替换
        replaceholder: function (str, values) {
            return str.replace(/\{(\d+)\}/g, function (m, i) {
                return values[i];
            });
        },
        pasteHtmlAtCaret: function (html) {
            var sel, range;
            if (window.getSelection) {
                sel = window.getSelection();
                if (sel.getRangeAt && sel.rangeCount) {
                    range = sel.getRangeAt(0);
                    range.deleteContents();
                    var el = document.createElement("div");
                    el.innerHTML = html;
                    var frag = document.createDocumentFragment(), node, lastNode;
                    while ((node = el.firstChild)) {
                        lastNode = frag.appendChild(node);
                    }
                    range.insertNode(frag);
                    // Preserve the selection
                    if (lastNode) {
                        range = range.cloneRange();
                        range.setStartAfter(lastNode);
                        range.collapse(true);
                        sel.removeAllRanges();
                        sel.addRange(range);
                    }
                }
            } else if (document.selection && document.selection.type != "Control") {
                // IE < 9
                document.selection.createRange().pasteHTML(html);
            }
        }
    };
    var i18n = function (key) {
        return lang[key] || key;
    };

    /*处理回放表情*/
    var replaceIcon = function (content) {
        for (var i = 0; i < genseeIconList.length; i++) {
            if (content.indexOf(genseeIconList[i].zh_CN) != -1) {
                return i;
            }
            if (content.indexOf(genseeIconList[i].en) != -1) {
                return i;
            }
            if (content.indexOf(genseeIconList[i].zh_TW) != -1) {
                return i;
            }
            if (content.indexOf(genseeIconList[i].ja) != -1) {
                return i;
            }
            if (content.indexOf(genseeIconList[i].st) != -1) {
                return i;
            }
            if (content.indexOf(genseeIconList[i].other) != -1) {
                return i;
            }
        }
        return "false";
    }

    var getWapChatContent = function (content) {
        /*var re = /\[([^\]\[]*)\]/g;
         var contentNew = content.match(re) || [];
         var emojiContent = customEmoji[contentNew];
         alert("emojiContent:"+content);
         if(!isNotNull(emojiContent)){
         var reg = /\【([^\]\[]*)\】/g;
         contentNew = content.match(reg) || [];
         emojiContent = customEmoji[contentNew];
         }
         if(isNotNull(emojiContent)){

         emojiContent = path+"/resources/course/wap/face/"+emojiContent.file;
         content = '<img src="'+emojiContent+'" width="25" height="25" style="margin-top:4px;" />';
         }*/
        return forReplaceIcon(content);
    }

    /*循环替换wap表情*/
    var forReplaceWap = function (content) {
        for(var key in customEmoji){
            if(content.indexOf(key)!=-1){
                var emojiContent = path+"/resources/course/wap/face/"+customEmoji[key].file;
                emojiContent = '<img src="'+emojiContent+'" width="25" height="25" style="vertical-align:top;" />';
                content = content.split(key).join(emojiContent);
            }
        }
        return content;
    }

    /*循环替换表情*/
    var forReplaceIcon = function (content) {
        var genseeIconListIndex = replaceIcon(content);
        if (genseeIconListIndex != "false") {
            var genseeIcon = genseeIconUrl + genseeIconList[genseeIconListIndex].url;
            genseeIcon = '<img src="' + genseeIcon + '" style="vertical-align:top;" />';
            content = content.split(genseeIconList[genseeIconListIndex].zh_CN).join(genseeIcon);
            content = content.split(genseeIconList[genseeIconListIndex].en).join(genseeIcon);
            content = content.split(genseeIconList[genseeIconListIndex].zh_TW).join(genseeIcon);
            content = content.split(genseeIconList[genseeIconListIndex].js).join(genseeIcon);
            content = content.split(genseeIconList[genseeIconListIndex].st).join(genseeIcon);
            content = content.split(genseeIconList[genseeIconListIndex].other).join(genseeIcon);
            content = forReplaceIcon(content);
        }
        return content;
    }


    function _initLive(livedata)
    {
        Livedata = livedata;
       var  _id = zk.user.getUserInfo()._id;
        var uid = _id * 100;
        var uname = zk.user.getUserInfo().nick_name;

        var ownerid = livedata.liveinfo.live_id;
        document.getElementById("showlive").innerHTML = '<div class="new-live-doc" style="background: #ccc; " id="gsDoc" state="doc">'
            +'<gs:doc site="'+site+'"'
            +'ctx="webcast"'
            +'uname="'+uname+'"'
            +'uid="'+uid+'"'
            +'ownerid="'+ownerid+'"'
            +'lang="zh_CN"'
            +'style="height: 110%;background-color: #fff;"'
            +'/>'
            +'</div>'
            +'<div id="gsVideo" class="new-live-sp"'
            +'style="height:113px; width: 150px;right: 0px;position: absolute;z-index: 2;">'
            +'<gs:video-live id="objVideo"'
            +'httpMode="false" ver="4.0"'
            +'bar="false"'
            +'site="'+site+'"'
            +'ctx="webcast"'
            +'uname="'+uname+'"'
            +'uid="'+uid+'"'
            +'k="123456"'
            +'ownerid="'+ownerid+'"'
            +'lang="zh_CN"'
            +'style="height: 100%;"/>'
            +'</div>';
    }

    function _initNoLive(livedata)
    {
        var _id = zk.user.getUserInfo()._id;
        var nickName = zk.user.getUserInfo().nick_name;

        var uid = _id * 100;
        var uname = zk.user.getUserInfo().nick_name;
        var ownerid;
        if(livedata.liveinfo.warm_up_id) {
             ownerid = livedata.liveinfo.warm_up_id;
        }
        else
        {
            ownerid = "1ad56da122434ad683f886681a0c480d";
        }

        document.getElementById("showlive").innerHTML = '<div class="new-live-doc" style="background: #ccc; " id="gsDoc" state="doc">'
            +'<gs:doc site="'+site+'"'
            +'ctx="webcast"'
            +'uname="'+uname+'"'
            +'uid="'+uid+'"'
            +'ownerid="'+ownerid+'"'
            +'lang="zh_CN"'
            +'style="height: 110%;background-color: #fff;"'
            +'/>'
            +'</div>'
            +'<div id="gsVideo" class="new-live-sp"'
            +'style="height:113px; width: 150px;right: 0px;position: absolute;z-index: 2;">'
            +'<gs:video-vod id="videoComponent"'
            +'site="'+site+'"'
            +'ctx="webcast"'
            +'uname="'+uname+'"'
            +'uid="'+uid+'"'
            +'ownerid="'+ownerid+'"'
            +'lang="zh_CN"'
            +'style="height: 100%;"/>'
            +'</div>';




    }

    function _initLiveControl(live_id)
    {
        $.ajax({
            type: "get",
            url: "/ajax/live/getliveinfo/"+zk.user.getToken()+"?live_id="+live_id,
            cache:false,
            async:false,
            dataType : "json",
            success: function(data){
                if(data.code == "1")
                {
                    $("#live_teacher").html(data.data.liveinfo.live_teacher_name);
                    if(data.data.liveinfo.is_live)
                    {
                        live_state =1;
                        $("#live_title").html(data.data.liveinfo.name+"---直播中");
                          _initLive(data.data);
                    }
                    else
                    {
                        live_state =0;
                        $("#live_title").html(data.data.liveinfo.name+"---直播尚未开始");
                        startOrderInterval();
                        _initNoLive(data.data);
                    }
                }
                else
                {
                   
                    self.init("当前直播间无权限，暂时无法观看，请购买相关课程再观看",function(){window.location = "/index.html";},false,"提示");
                    $("body").on("mousedown",".state-pop-p4,.state-pop-p5",function(){window.location = "/index.html";});
                }
            }

        });


    }

    //校验订单详情 循环定时器
    function orderInterval(){

        zk.core.getResult({
            url: zk.config.apiurl+"live/getlivestate/{access_token}?live_id={live_id}",
            requireToken: true,//是否需要access_token
            dataType : "json",
            type: "GET",
            data:{"live_id": $.query.get("live_id")},
            success: function(data) {
                if(data.data.liveinfo.is_live)
                {
                   location.reload();
                }
                else
                {
                    console.log("check live state");
                }

            },
            resolveError : function (data) {

            }
        });

    }

    //启动循环定时器
    function startOrderInterval(){
        if( order_interval == null){
            order_interval = window.setInterval(orderInterval,order_interval_time);
        }
    }

    //停止定时器
    function clearOrderInterval(){
        //去掉定时器的方法
        window.clearInterval(order_interval);
        order_interval = null;
    }

    //模块初始化
    function _init () {
        live_id = $.query.get("live_id");
        Channel =  GS.createChannel();
        onLineNum = Channel.send("onUserOnline");
        genseeNum = onLineNum;
        if(zk.user.isLogin()){
            nickName = zk.user.getUserInfo().nick_name;
            _id = zk.user.getUserInfo()._id * 100;
        }
        else
        {
            zk.user.popupLogin();
            return;
        }
        scrollBottom();
        _initLiveControl(live_id);
        //初始化注册绑定事件
        _registerEvents();

        $btn.mousedown(function(e){
            lx = $btn.offset().top;
            ox = e.pageY - top;
            statu = true;
            return false;
        });
        $(document).mouseup(function(){
            statu = false;
        });
        $box.mousemove(function(e){
            if(statu){
                top = e.pageY - ox;
                if(top < 0){
                    top = 0;
                }
                if(top > 56){
                    top = 56;
                }
                $btn.css('top',top);
                $bar.css("height",56-top);
                vol = 100-parseInt(100/56*top);
                volume = vol / 100;
                finalVoice = volume;
                Channel.send("submitVolume", {"value": volume});
                //$(document).trigger("hqyzx.web.gensee.voice.volume", volume);
                //console.log(vol);
            }
        });
        $box.mousedown(function(e){
            if(!statu){
                bgTop = $box.offset().top;
                top = e.pageY - bgTop - 7;
                if(top < 0){
                    top = 0;
                }
                if(top > 56){
                    top = 56;
                }
                $btn.css('top',top);
                $bar.css("height",56-top);
                vol = 100-parseInt(100/56*top);
                volume = vol / 100;
                finalVoice = volume;
                Channel.send("submitVolume", {"value": volume});
                //$(document).trigger("hqyzx.web.gensee.voice.volume", volume);
                //console.log(vol);
            }
            return false;
        });
    }
    //绑定事件
    function _registerEvents(){
        //onLineInit();

        onLineInitWap();
        chatEventInit();
        /*Channel.bind("onUserJoin", function (e) {
            liveObject.property.onLineNum.text(genseeNum);
        });
        Channel.bind("onUserLeave", function (e) {
            liveObject.property.onLineNum.text(genseeNum);
        });*/
        Channel.bind("onUserOnline", function (e) {
            liveObject.property.onLineNum.text(e.data.count);
        });
        /*----------------------直播视频部分控制开始--------------------------*/
        /**
         * 插件初始化
         */
        function checkTime(num) {
            var n = Number(num);
            if (n < 10)n = "0" + n;
            return n;
        }

        function pluginInit() {
            /**
             * 工具类插件
             */
            ;
            (function (window) {
                window.Util = Util;
                window.i18n = i18n;
            })(window);
        }

        /**
         * 获取Div纯文本内容
         */
        function getDivTextContent(divHtml) {
            return $("<p>" + divHtml + "</p>").text();
        }

        /**
         * 取消当前窗口中已选中的文本
         */
        function removeSelectTextRange() {
            document.selection && document.selection.empty && ( document.selection.empty(), 1)
            || window.getSelection && window.getSelection().removeAllRanges();
        }

        /**
         * 音量调节滑条移动处理
         * @param e
         */
        /*function moveVoiceVolumeBox(e) {
            var flag = $(".changeVoiceImg").attr("flag");
            if (flag == "no") {
                $(".changeVoiceImg").attr("class", "live-sy");
                $(".changeVoiceImg").attr("flag", "yes");
                $(".voiceClick").text("静音");
            }
            var $bg = $(".video-progre");
            var $bgcolor = $(".video-jd");
            var $btn = $(".video-jdbut");
            var left = 0;
            var bgleft = $bg.offset().left;
            left = e.pageX - bgleft;
            if (left < 0) {
                left = 0;
            }
            if (left > 80) {
                left = 80;
            }
            $btn.css("left", left - 5);
            $bgcolor.stop().animate({width: left}, 80);
            return left / 80;
        }*/

        /**
         * 音量调节滑条移动处理
         * @param e
         */
        /*function moveVoiceVolumeBox1(e) {
            var flag = $(".changeVoiceImg1").attr("flag");
            if (flag == "no") {
                $(".changeVoiceImg1").attr("class", "live-sy");
                $(".changeVoiceImg1").attr("flag", "yes");
                $(".voiceClick1").text("静音");
            }
            var $bg = $(".video-progre1");
            var $bgcolor = $(".video-jd1");
            var $btn = $(".video-jdbut1");
            var left = 0;
            var bgleft = $bg.offset().left;
            left = e.pageX - bgleft;
            if (left < 0) {
                left = 0;
            }
            if (left > 80) {
                left = 80;
            }
            $btn.css("left", left - 5);
            $bgcolor.stop().animate({width: left}, 80);
            return left / 80;
        }*/

        /**
         * 聊天相关事件处理
         */
        var tarceMsg = true;

        function onLineInitWap () {
            Channel.bind("onUserOnline", function (e) {
            });
            Channel.bind("onUserList", function (e) {
            });
            Channel.bind("onPublicChat", function (e) {
                var chatContent = e.data.content;
                var chatRichText = e.data.richtext;
                chatRichText=getWapChatContent(chatContent);
                if(chatRichText.indexOf('W0E0L0C0O0M0E')!=-1 || chatContent.indexOf('W0E0L0C0O0M0E')!=-1){
                    return;
                }
                var reg=/headImg=\"([^\"]*?)\"/gi;
                var headPic=chatContent.match(reg);
                if(isNotNull(headPic)) {
                    headPic = headPic.toString();
                    if(headPic.indexOf("=")!=-1){
                        headPic = imageServicePath + headPic.substring(headPic.indexOf("=") + 2, headPic.length - 1);
                    }else{
                        headPic = imageServicePath+"headpic/live_wap_headPic/head5.png";
                    }
                    headPic = '<img src="' + headPic + '" width="25" height="25" />';
                }else{
                    headPic = imageServicePath+"headpic/live_wap_headPic/head5.png";
                    headPic = '<img src="' + headPic + '" width="25" height="25" />';
                }
                /*老师头像问题*/
                /*发送信息方法*/
                sendWapMsg(e.data.sender,headPic,chatContent,chatRichText);
                /*if (e.data != null && e.data.senderRole.indexOf("1") != -1 && e.data.senderRole.indexOf("2") != -1 && e.data.senderRole.indexOf("4") != -1) {
                 showReceiveMsg(e.data.sender, e.data.content, e.data);
                 } else {
                 showReceiveMsg(e.data.sender, e.data.richtext, e.data);
                 }*/
            });
            Channel.bind("onPriChat", function (e) {
                /*
                 showReceiveMsg(e.data.sender, e.data.richtext, e.data);
                 */
            });
            Channel.bind("onStart", function (e) {


                console.log("Start");
            });
            Channel.bind("onPause", function (e) {

                console.log("onPause");
            });
            Channel.bind("onStop", function (e) {

                location.reload();
                console.log("onStop");
            });


        }

        function isNotNull(value){
            if(value!=null && value!="" && typeof value!='undefined'){
                return true;
            }
            return false;
        }

        function sendWapMsg(userName,headPic,content,richText){
            if(isNotNull(richText)){
                content = richText;
            }
            content = content.split("/n").join("");
            var div = '<div class="chatList">' +
                '<a>' + headPic + '</a>' +
                '<div>' + userName+':' + '<span>' + content + '</span>' + '</div>' +
                '</div>';
            $("#slider>div").append(div);
            var h = $("#sliderCon").height() - $("#slider").height();
            $("#slider").animate({
                scrollTop: h
            }, 500);
        }

        function onLineInit() {
            Channel.bind("onUserOnline", function (e) {
            });
            Channel.bind("onUserList", function (e) {
            });
            Channel.bind("onPublicChat", function (e) {
                if (e.data != null && e.data.senderRole.indexOf("1") != -1 && e.data.senderRole.indexOf("2") != -1 && e.data.senderRole.indexOf("4") != -1) {
                    var content = e.data.content;
                    if(content.indexOf("apptype")!=-1){
                        content = getWapChatContent(content);
                    }
                    showReceiveMsg(e.data.sender,content, e.data);
                } else {
                    var content = e.data.richtext;
                    if(content.indexOf("apptype")!=-1){
                        content = getWapChatContent(content);
                    }
                    showReceiveMsg(e.data.sender,content , e.data);
                }
            });
            Channel.bind("onPriChat", function (e) {
                showReceiveMsg(e.data.sender, e.data.richtext, e.data);
            });
        }

        function chatEventInit() {
            Channel.bind("onPublicChat", function (e) {
                if (e.data != null && e.data.senderRole.indexOf("1") != -1 && e.data.senderRole.indexOf("2") != -1 && e.data.senderRole.indexOf("4") != -1) {
                    var content = e.data.content;
                    showReceiveMsg(e.data.sender,content, e.data);
                } else {
                    var content = e.data.richtext;
                    showReceiveMsg(e.data.sender,content , e.data);
                }
            });
            Channel.bind("onPriChat", function (e) {
                showReceiveMsg(e.data.sender, e.data.content, e.data);
            });
            Channel.bind("onUserJoin", function (e) {
                showJoinUserMsg(e.data.list);
            });
            Channel.bind("onUserList", function (e) {
            });

            Channel.bind("onModuleFocus", function (e) {
                var focusStatus = e.data.focus;
                //文档为主 0
                if (focusStatus == 0) {
                    if (!isDocMain) {
                        videoChange();
                    }
                }
                //视频为主 3
                if (focusStatus == 3) {
                    if (isDocMain) {
                        videoChange();
                    }
                }
                //文档最大化 2
                /*if (focusStatus == 2) {
                    if (!isDocMain) {
                        videoChange();
                    }
                    changeDocAreaMode();
                }
                //视频最大化 1
                if (focusStatus == 1) {
                    if (isDocMain) {
                        videoChange();
                    }
                    changeDocAreaMode();
                }*/
            });
        }

        /**
         * 去除聊天显示前缀
         */
        function getLastName(uname) {
            if (uname.indexOf("Y-") >= 0) {
                uname = uname.replace("Y-", "");
            }
            if (uname.indexOf("rq-") >= 0) {
                uname = uname.replace("rq-", "");
            }
            if (uname.indexOf("r7-") >= 0) {
                uname = uname.replace("r7-", "");
            }
            if (uname.indexOf("r0-") >= 0) {
                uname = uname.replace("r0-", "");
            }
            if (uname.indexOf("Vq-") >= 0) {
                uname = uname.replace("Vq-", "");
            }
            if (uname.indexOf("V7-") >= 0) {
                uname = uname.replace("V7-", "");
            }
            if (uname.indexOf("V0-") >= 0) {
                uname = uname.replace("V0-", "");
            }
            if (uname.indexOf("@@M@@") >= 0) {
                uname = uname.replace("@@M@@", "");
            }
            if (uname.indexOf("self-") >= 0) {
                uname = uname.replace("self-", "");
            }
            return uname;
        }

        /**
         * 显示接收到的消息
         * @param sender 发送人
         * @param content 发送内容
         */
        function showReceiveMsg(sender, content, msg) {
            if (content.indexOf("##JOIN##") == -1) {//排除掉用户加入直播的消息
                var charMsgForm = $("#msgForm");
                var msgFormSelf = $("#msgFormSelf");
                var newMsg = appendMsgForm(sender, content, msg);
                newMsg = newMsg.replace("<br>", "");
                //charMsgForm.html(charMsgForm.html() + newMsg);
                /*获取自己和老师的信息*/
                var isTeacher = false;
                if (sender.indexOf("self-") >= 0) {
                    charMsgForm.html(charMsgForm.html() + newMsg);
                }

                if (sender == "hqyzx") {
                    charMsgForm.html(charMsgForm.html() + newMsg);
                    var isTeacher = true;
                }
                if (msg != 1 && !isTeacher) {
                    if (msg.senderRole != null) {
                        charMsgForm.html(charMsgForm.html() + newMsg);
                    }
                }
                alertMsg();

                //$(document).trigger("duia.web.danmu.msg.receive", {"content": getDivTextContent(content)});
            }
        }

        /**
         * 当有用户加入的时候显示加入消息
         * @param sender 发送人
         * @param content 发送内容
         */
        function showJoinUserMsg(list){
            if(list != null){
                var msgContent = '<span class="live-chat-text"></span><span class="live-chat-text">用户已加入直播</span>';
                showReceiveMsg("self-" + list[list.length].name, msgContent, "web");
                Channel.send("submitChat", {"richtext": msgContent});
            }
        }

        function alertMsg() {
            var charMsgForm = liveObject.property.charMsgForm;
            //var msgFormDiv = liveObject.property.msgFormDiv;
            var contentH = charMsgForm.get(0).scrollHeight;
            var viewH = charMsgForm.height();
            var subHeight = contentH * 1 - viewH * 1;
            var scrollTop = charMsgForm.scrollTop();//滚动高度
            if (subHeight >= scrollTop * 1 && subHeight <= 400 + scrollTop * 1) {
                charMsgForm.scrollTop(scrollTop + contentH);
                /*$("#msgFormDiv").animate({
                 scrollTop: scrollTop + contentH
                 });*/
                /*未读消息*/
                /*    addChatNum(false);
                 liveObject.property.newChatMes.hide();*/
            }
            /*else if (subHeight > 0 && subHeight != scrollTop * 1) {
             chatNum++;
             addChatNum(true);
             liveObject.property.newChatMes.show();
             }*/
        }




        /*----------------------视频回放部分控制开始--------------------------*/
        function playbackInit() {
            playbackSeekBarChange();
        }

        /**
         * 回放视频播放进度处理
         */
        function playbackSeekBarChange() {
            var $box = $(".left-pptjd");
            var $bgcolor = $(".left-jdbg");
            var $btn = $(".left-jdbut");
            var statu = false;
            var ox = 0;
            var lx = 0;
            var left = 0;
            var bgleft = 0;
            var seekPoint = 0.0;

            $(".left-zt").click(function () {
                var btnImg = $(".left-zt img");
                if (btnImg.attr("playState") === "stop") {
                    $(document).trigger("duia.web.gensee.playback.play", {});
                } else {
                    $(document).trigger("duia.web.gensee.playback.pause", {});
                }
            });

            //直播回放进度条滑动处理
            $btn.mousedown(function (e) {
                lx = $btn.offset().left;
                ox = e.pageX - left - 13;
                statu = true;
                isUpdateSeekPoint = false;
                removeSelectTextRange();
            }).mouseout(function (e) {
                if (statu) {
                    seekPoint = playbackSeekBarBox(e);
                    $(document).trigger("duia.web.gensee.vedio.seek", {"seekPoint": seekPoint * videoLength});
                    if ($(".left-zt").attr("playState") === "stop") {
                        $(document).trigger("duia.web.gensee.playback.play", {});
                    }
                    statu = false;
                }
            }).mousemove(function (e) {
                if (statu) {
                    seekPoint = playbackSeekBarBox(e);
                }
            }).mouseup(function (e) {
                if (statu) {
                    seekPoint = playbackSeekBarBox(e);
                    $(document).trigger("duia.web.gensee.vedio.seek", {"seekPoint": seekPoint * videoLength});
                    if ($(".left-zt").attr("playState") === "stop") {
                        $(document).trigger("duia.web.gensee.playback.play", {});
                    }
                    statu = false;
                    isUpdateSeekPoint = false;
                }
                removeSelectTextRange();
            });

            //直播回放进度条鼠标点击处理
            $box.click(function (e) {
                if (!statu) {
                    seekPoint = playbackSeekBarBox(e);
                    $(document).trigger("duia.web.gensee.vedio.seek", {"seekPoint": seekPoint * videoLength});
                    statu = false;
                    isUpdateSeekPoint = false;
                }
            });
        }

        /**
         * 直播回放进度条移动处理
         * @param e
         */
        function playbackSeekBarBox(e) {
            var $bg = $(".left-pptbg");
            var $bgcolor = $(".left-jdbg");
            var $btn = $(".left-jdbut");
            var left = 0;
            var bgleft = $bg.offset().left;
            left = e.pageX - bgleft;
            if (left < 0) {
                left = 0;
            }
            if (left > $bg.width()) {
                left = $bg.width();
            }
            $btn.css("left", left - 13);
            $bgcolor.stop().animate({width: left}, 800);
            return left / $bg.width();
        }

        function updatePlaybackSeekPoint() {
            if (videoCurrentPoint > 0) {
                var $bg = $(".left-pptbg");
                var $btn = $(".left-jdbut");
                var $bgcolor = $(".left-jdbg");
                //var bgWidth = $(".left-pptbg").width();
                var vedioProgress = videoCurrentPoint / videoLength;
                leftPptjd = $bg.width();
                $btn.stop().animate({left: leftPptjd * vedioProgress - 10}, 800);
                $bgcolor.stop().animate({width: leftPptjd * downloadProgress / 100}, 1000);
                $(".left-time").html(Util.timeDuration(videoCurrentPoint / 1000) +
                    "/" + Util.timeDuration(videoLength / 1000));
            }
        }

        /*----------------------视频回放部分控制结束--------------------------*/

        //聊天发送消息屏蔽字符
        var validSendMsg = function (msg) {
            var upperMsg = msg.toUpperCase();
            upperMsg = obj2string(upperMsg);
            if (validKeywords != null && validKeywords != "") {
                for (var i = 0; i < validKeywords.length; i++) {
                    var validStr = validKeywords[i];
                    validStr = validStr.toUpperCase();
                    validStr = obj2string(validStr);
                    validStr = trim(validStr);
                    if (upperMsg.indexOf(validStr) >= 0) {
                        return false;
                    }
                }
            }
            return true;
        }



        function obj2string(o) {
            var r = [];
            if (typeof o == "string") {
                return o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t");
            }
            return o.toString();
        }

        function trim(str) {
            return str.replace(/(^\s*)|(\s*$)/g, "");
        }

        function setCookie(name, value, seconds) {
            var seconds = seconds;
            var exp = new Date();
            exp.setTime(exp.getTime() + seconds * 1000);
            document.cookie = name + "=" + escape(value) + ";path=/;expires=" + exp.toGMTString();
        }

        //读取cookies
        function getCookie(name) {
            var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
            if (arr = document.cookie.match(reg)) {
                return (arr[2]);
            } else {
                return null;
            }
        }

        function sysLiveConfig(mode, link, sku, key) {
            if (mode == 1) {
                xiaoneng(sku, key);
            } else if (mode == 2) {
                var newHref = link;
                if (newHref != null && newHref != "") {
                    if (newHref.indexOf("http://") < 0) {
                        newHref = "http://" + newHref;
                    }
                    window.open(newHref);
                }
            } else {

            }
        }

        /*表情链接转换汉字*/
        var conversionIcon = function () {
            var msgContent = $("#msgContent").html();
            var imgs = $("#msgContent").find("img");
            for (var i = 0; i < imgs.length; i++) {
                var img = imgs[i];
                msgContent = msgContent.split(img.outerHTML).join(img.getAttribute("alt"));
            }
            return msgContent;
        }

        /*发送聊天点击事件*/
        $("#sendMsg").click(function (e) {
            if ($("#msgContent").html().indexOf("在这里和大家一起聊聊学习") >= 0) {
                $("#msgContent").html("在这里和大家一起聊聊学习");
                return false;
            }
            var msgContent = conversionIcon();
            msgContent = msgContent.replace(/&nbsp;/g, "");
            msgContent = msgContent.replace(/(^\s+)|(\s+$)/g, "");
            var vipFlag = "";
            /*if ($("#courseDirectory").attr("flag") != "open") {
                $("#msgFormDiv").scrollTop($("#msgFormDiv").scrollTop() + 100000);
            }*/
            if (!validSendMsg(msgContent) || msgContent.indexOf("http://") >= 0 || msgContent.indexOf("www.") >= 0) {
                msgContent = '<span class="live-chat-text" >***</span>';
                msgContent = msgContent.replace("<br>", "");
                showReceiveMsg("self-" + nickName, msgContent, "web");
                $("#msgContent").html("在这里和大家一起聊聊学习");
                return false;
            }
            msgContent = Util.trim(msgContent);
            msgContent = msgContent.replace("<br>", "");
            if (msgContent != "" && msgContent != "undefined" && msgContent != "在这里和大家一起聊聊学习") {
                /*            msgContent = '<span class="live-chat-text" vipFlag="' + vipFlag + '"></span><span class="live-chat-text">' + $("#msgContent").html() + '</span>';*/
                /*手机端显示头像*/
                msgContent = '<span class="live-chat-text"></span><span class="live-chat-text">' + $("#msgContent").html() + '</span>';
                showReceiveMsg("self-" + nickName, msgContent, "web");
                Channel.send("submitChat", {"richtext": msgContent});

            } else {

            }
            $("#msgContent").css("color", "#aaa").html("在这里和大家一起聊聊学习");
            return false;
        });
        
        $(document).on("keydown",function(event){
        	if(event.ctrlKey&&event.keyCode==13){
        		console.log(123);
        		if ($("#msgContent").html().indexOf("在这里和大家一起聊聊学习") >= 0) {
	                $("#msgContent").html("在这里和大家一起聊聊学习");
	                return false;
	            }
	            var msgContent = conversionIcon();
	            msgContent = msgContent.replace(/&nbsp;/g, "");
	            msgContent = msgContent.replace(/(^\s+)|(\s+$)/g, "");
	            var vipFlag = "";
	            /*if ($("#courseDirectory").attr("flag") != "open") {
	                $("#msgFormDiv").scrollTop($("#msgFormDiv").scrollTop() + 100000);
	            }*/
	            if (!validSendMsg(msgContent) || msgContent.indexOf("http://") >= 0 || msgContent.indexOf("www.") >= 0) {
	                msgContent = '<span class="live-chat-text" >***</span>';
	                msgContent = msgContent.replace("<br>", "");
	                showReceiveMsg("self-" + nickName, msgContent, "web");
	                $("#msgContent").html("在这里和大家一起聊聊学习");
	                return false;
	            }
	            msgContent = Util.trim(msgContent);
	            msgContent = msgContent.replace("<br>", "");
	            if (msgContent != "" && msgContent != "undefined" && msgContent != "在这里和大家一起聊聊学习") {
	                /*            msgContent = '<span class="live-chat-text" vipFlag="' + vipFlag + '"></span><span class="live-chat-text">' + $("#msgContent").html() + '</span>';*/
	                /*手机端显示头像*/
	                msgContent = '<span class="live-chat-text"></span><span class="live-chat-text">' + $("#msgContent").html() + '</span>';
	                showReceiveMsg("self-" + nickName, msgContent, "web");
	                Channel.send("submitChat", {"richtext": msgContent});
	
	            } else {
	
	            }
	            $("#msgContent").css("color", "#aaa").html("在这里和大家一起聊聊学习");
	            return false;
        	}
        });

        $("#sendflower").click(function (e) {
            var msgContent = '<span class="live-chat-text"></span><span class="live-chat-text"><img alt="【鲜花】" src="http://static.gensee.com/webcast/static/emotion/rose.up.png" class="addIconFlag" style="vertical-align:top;"></span>';
            showReceiveMsg("self-" + nickName, msgContent, "web");
            Channel.send("submitChat", {"richtext": msgContent});
        });

        $("#msgContent").keydown(function (event) {
            event.stopPropagation();
            //event.preventDefault();
        });

        /*拼接发送的消息*/
        var appendMsgForm = function (sender, content, msg) {
            var isTeacher = false;
            var isSelf = false;
            if (sender.indexOf("self-") >= 0) {
                isSelf = true;
            }
            /*直播*/
            if (msg != 1) {
                if (msg.senderRole != null && (msg.senderRole.indexOf("1") != -1 || msg.senderRole.indexOf("2") != -1 || msg.senderRole.indexOf("4") != -1 )) {
                    isTeacher = true;
                }
            }
            if (sender == "hqyzx") {
                isTeacher = true;
            }
            var msgFormContent = '<p class="clearfloat">';
            /*判断手机用户*/
            if ((content.toString().indexOf('apptype') != -1) && !isTeacher) {
                msgFormContent += '<em class="live-iphone"></em>';
            }
            /*判断是否为老师*/
            if (isTeacher) {
                msgFormContent += '<div class="live-tea-pink clearfloat">';
            }
            /*判断是否为自己*/
            if (isSelf) {
                msgFormContent += '<div class="live-self-red clearfloat">';
            }
            if (!isTeacher && !isSelf) {
                msgFormContent += '<div class="clearfloat">';
            }

            if (isSelf) {
                msgFormContent += '<span style="margin-top: 3px;color: #e96c67;">&nbsp;' + getLastName(sender) + '：</span>';
            } else {
                msgFormContent += '<span style="margin-top: 3px;">&nbsp;' + getLastName(sender) + '：</span>';
            }
            //msgFormContent += '</a>';
            /*http链接*/
            content = Util.formatUrl(content);
            /*替换表情*/
            if (isTeacher || msg == "govod") {
                content = forReplaceIcon(content);
            }
            if (content.indexOf('class="live-chat-text"') != -1) {
                msgFormContent += content;
            } else {
                msgFormContent += '<i class="live-chat-text">' + content + '</i>';
            }
            msgFormContent += '</div></p>';
            return msgFormContent;
        }



        /*视频和doc文档切换*/
        var cutVideoAndDoc = function () {
            /*判断ppt是否为主*/
            var state = $("#gsDoc").attr("state");
            var pptClass = $("#gsDoc").attr("class");
            var pptStyle = $("#gsDoc").attr("style");
            var videoClass = $("#gsVideo").attr("class");
            var videoStyle = $("#gsVideo").attr("style");
            if (state != "doc") {
                /*ppt为主*/
                $("#gsDoc").attr("class", videoClass);
                $("#gsDoc").attr("style", videoStyle);
                $("#gsVideo").attr("class", pptClass);
                $("#gsVideo").attr("style", pptStyle);
                $("#gsDoc").attr("state", "doc");
                //$("#gsVideo").css();
                $("#gsDoc").css({"filter": "alpha(opacity=1)", "-moz-opacity": "1", "-khtml-opacity": "1", "opacity": "1"});
            } else {
                /*视频为主*/
                $("#gsDoc").attr("class", videoClass);
                $("#gsDoc").attr("style", videoStyle);
                $("#gsVideo").attr("class", pptClass);
                $("#gsVideo").attr("style", pptStyle);
                $("#gsDoc").attr("state", "video");
                $("#gsDoc").css({"filter": "alpha(opacity=1)", "-moz-opacity": "1", "-khtml-opacity": "1", "opacity": "1"});
            }
            //$(".closeVideo").click();
        }

        $("#cutVideo").click(function () {
            cutVideoAndDoc();
        });



        /*监听展示客户端切换视频和ppt*/
        var initListening = function () {
            Channel.bind("onModuleFocus", function (e) {
                var focusStatus = e.data.focus;
                //文档为主 0
                if (focusStatus == 0) {
                    var state = $("#gsDoc").attr("state", "video");
                    cutVideoAndDoc();
                }
                //视频为主 3
                if (focusStatus == 3) {
                    var state = $("#gsDoc").attr("state", "doc");
                    cutVideoAndDoc();
                }

            });
        }

		$("#openbq").on("click",function (){
			var display =$(".new-livexz").css("display");
			if(display == "block"){
				$(".new-livexz").hide(0);
			}else{
				$(".new-livexz").show(0);
			}
            return false;
        });
		$("body").on("click",function(){
			$(".new-livexz").hide(0);
		});
		
        
        var textImg = "";
        /*发送表情*/
        $(".new-live-ul li").bind("click", function (e) {
            //$("#msgContent").focus();
            if (Util.trim($("#msgContent").text()) == "在这里和大家一起聊聊学习") {
                $("#msgContent").text("");
            }
            $(this).find("img").attr("class", "addIconFlag");
            $(this).find("img").attr("style", "vertical-align:top;");
            $("#msgContent").html($("#msgContent").html() + $(this).find("img")[0].outerHTML);
            $("#msgContent").focus();
        });

        $("#msgContent").focus(function (event) {
            if (Util.trim($(this).text()) == "在这里和大家一起聊聊学习") {
                $(this).text("");
                $(this).css({"color": "#000"});
            }
        });
        $("#msgContent").blur(function () {
            if (Util.trim($(this).html()) == "" || Util.trim($(this).html()) == "null") {
                $(this).html("在这里和大家一起聊聊学习");
                $(this).css({"color": "#aaa"});
            } else {
                $(this).css({"color": "#000"});
            }
            $("#header").keydown(function (e) {
                var e = e || window.event;
                if (e.keyCode == 32) {
                    if (headerKeyCode == "pause") {
                        $(document).trigger("hqyzx.web.gensee.playback.pause", {});
                        headerKeyCode = "play";
                    } else {
                        $(document).trigger("hqyzx.web.gensee.playback.play", {});
                        headerKeyCode = "pause";
                    }
                }
            });
        });




        $(".live-speak").on("mousedown",function(){
            if(!$(this).hasClass("live-speak-no")){
                $(this).addClass("live-speak-no");
                Channel.send("submitMute", {"mute": true});
                //$(document).trigger("hqyzx.web.gensee.voice.volume", 0);
            }else{
                $(this).removeClass("live-speak-no");
                Channel.send("submitMute", {"mute": false});
                //$(document).trigger("hqyzx.web.gensee.voice.volume", finalVoice);
            }
        });
    }

//	移入显示提示
	(function hoverShowTips(){
	    $(".live-player dd span").hover(function(e){
	        $(this).find(".tips").show(0);
	    },function(){
	  		$(this).find(".tips").hide(0);
	    });
    })();
//	打开关闭视频
 	$(".live-video").on("click",function(){
    	if(!$(this).hasClass("live-video-no")){
    		$(this).addClass("live-video-no").find("p").text("打开视频");
    	}else{
    		$(this).removeClass("live-video-no").find("p").text("关闭视频");
    	}
        var flag = $(this).attr("flag");
        if (flag == "close") {
            //$("#objVideo").css("opacity", "0");
            $("#gsVideo").css({
                "filter": "alpha(opacity=100)",
                "-moz-opacity": "1",
                "-khtml-opacity": "1",
                "opacity": "1"
            });
            //$(".live-video").attr("class", "closeVideo live-ovio");
            $(".live-video").attr("flag", "open");
            Channel.send("submitOpenVideo", {"openvideo": "true"});
        } else {
            //$("#objVideo").css("opacity", "1");
            $("#gsVideo").css({
                "filter": "alpha(opacity=0)",
                "-moz-opacity": "0",
                "-khtml-opacity": "0",
                "opacity": "0"
            });
            //$(".live-video").attr("class", "closeVideo live-cvio");
            $(".live-video").attr("flag", "close");
            Channel.send("submitOpenVideo", {"openvideo": "false"});
        }
    });   

//  聊天窗滚动条在底部
    function scrollBottom(){
    	$(".live-charBox").scrollTop($(".live-charBox")[0].scrollHeight);
    }

	
    return {
        init : _init,
        initLiveControl:_initLiveControl
    };
});


$(function () {
    zk.live.liveController.init();

});
