$(function () {
    window.util = {
        init: function () {
            this.imgLazy();
            this.iePlaceholderFixed();
            this.xiaoneng();
        },
        // 创建script  
        createJs: function (src) {
            var script = document.createElement("script");
            script.setAttribute("type", "text/javascript");
            script.setAttribute("src", src);
            var heads = document.getElementsByTagName("head");
            if (heads.length) {
                heads[0].appendChild(script);
            }
            else {
                document.documentElement.appendChild(script);
            }
        },
        // 添加多个onload
        addLoadEvent: function (func) {
            var oldonload = window.onload; //把现在有window.onload事件处理函数的值存入变量oldonload。  
            if (typeof window.onload != 'function') { //如果这个处理函数还没有绑定任何函数，就像平时那样把新函数添加给它  
                window.onload = func;
            } else { //如果在这个处理函数上已经绑定了一些函数。就把新函数追加到现有指令的末尾  
                window.onload = function () {
                    oldonload();
                    func();
                }
            }
        },
        // 判断浏览器 级别
        brower: function () {
            var version;
            if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/8./i) == "8.") {
                version = 'ie8'
            } else if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/9./i) == "9.") {
                version = 'ie9'
            }
            return version;
        },
        /*图片懒加载*/
        imgLazy: function () {
            $('img.lazy,.bg-lazy').lazyload({
                effect: "fadeIn"
            });
        },
        // ie兼容placeholder
        iePlaceholderFixed: function () {
            if (!('placeholder' in document.createElement('input'))) {
                $('input[placeholder],textarea[placeholder]').each(function () {
                    var that = $(this),
                        text = that.attr('placeholder');
                    if (that.val() === "") {
                        that.val(text).addClass('placeholder');
                    }
                    that.focus(function () {
                        if (that.val() === text) {
                            that.val("").removeClass('placeholder');
                        }
                    })
                        .blur(function () {
                            if (that.val() === "") {
                                that.val(text).addClass('placeholder');
                            }
                        })
                        .closest('form').submit(function () {
                            if (that.val() === text) {
                                that.val('');
                            }
                        });
                });
            }
        },
        // 报名-带校区
        onroll_school: function (catname) {
            var catname = catname || '意向课程';
            /*现已有 5980 人预约报名！*/
            $("#sqty-btn").click(function () {
                var province = $('select[name=province] option:selected').text();
                var school_area = $('select[name=school_area] option:selected').text();
                if (school_area == '' || school_area == '校区' || school_area == '请选择校区') {
                    alert("请选择校区！");
                    return false;
                }
                var name = $('input[name="name"]').val();
                if (name == '请输入您的姓名' || name == '') {
                    alert("请填写你的姓名！");
                    return false;
                }

                var tel = $('input[name="mobil"]').val();

                if (tel == '请输入预约手机号' || tel == '') {
                    alert("电话号码不可以为空！");
                    return false;
                }
                var re = /^1[3,5,8,9,4,6,7]\d{9}$/;
                if (!re.test(tel)) {
                    alert("请输入正确的手机号(11位)！");
                    return false;
                }
                var param = {
                    tel: tel,
                    name: name,
                    province: province,
                    trueName: school_area,
                    thisUrl: window.location.href,
                    preUrl: document.referrer,
                    hslx: catname,
                    reason: catname,
                    code: 'nocode',
                    property: 0,
                    catname: catname,
                }
                $.post('http://www.hengqijiaoyu.cn/promotion/guestbook/checkCodedata', param, function (data) {
                    data = JSON.parse(data);
                    if (data.code == 0) {
                        alert("提交成功,稍后会有老师联系您!");
                        window.location.reload();
                    } else {
                        alert(data.msg);
                    }
                });
            });
        },
        // 报名-不带校区
        onroll: function (catname) {
            var catname = catname || '意向课程';
            /*现已有 5980 人预约报名！*/
            $("#sqty-btn").click(function () {
                // var province = $('select[name=province] option:selected').text();
                // var school_area = $('select[name=school_area] option:selected').text();
                // if (school_area == '' || school_area == '校区' || school_area == '请选择校区') {
                //     alert("请选择校区！");
                //     return false;
                // }
                var name = $('input[name="name"]').val();
                if (name == '请输入您的姓名' || name == '') {
                    alert("请填写你的姓名！");
                    return false;
                }

                var tel = $('input[name="mobil"]').val();

                if (tel == '请输入预约手机号' || tel == '') {
                    alert("电话号码不可以为空！");
                    return false;
                }
                var re = /^1[3,5,8,9,4,6,7]\d{9}$/;
                if (!re.test(tel)) {
                    alert("请输入正确的手机号(11位)！");
                    return false;
                }
                var param = {
                    tel: tel,
                    name: name,
                    // province: province,
                    // trueName: school_area,
                    thisUrl: window.location.href,
                    preUrl: document.referrer,
                    hslx: catname,
                    reason: catname,
                    code: 'nocode',
                    property: 0,
                    catname: catname,
                }
                $.post('http://www.hengqijiaoyu.cn/promotion/guestbook/checkCodedata', param, function (data) {
                    data = JSON.parse(data);
                    if (data.code == 0) {
                        alert("提交成功,稍后会有老师联系您!");
                        window.location.reload();
                    } else {
                        alert(data.msg);
                    }
                });
            });
        },
        // 小能
        xiaoneng: function () {
            // 小能
            this.createJs('//dl.ntalker.com/js/b2b/ntkfstat.js?siteid=hq_1000');
            window.NTKF_PARAM = {
                siteid: "hq_1000",                   //企业ID，为固定值，必填
                settingid: "hq_1000_1496721349464",  //接待组ID，为固定值，必填
                uid: "",                     //用户ID，未登录可以为空，但不能给null，uid赋予的值显示到小能客户端上
                uname: "",         //用户名，未登录可以为空，但不能给null，uname赋予的值显示到小能客户端上
                // isvip:"0",                          //是否为vip用户，0代表非会员，1代表会员，取值显示到小能客户端上
                // userlevel:"1",                       //网站自定义会员级别，0-N，可根据选择判断，取值显示到小能客户端上
                // erpparam:"abc" 关键词 访客地域 url ip                     //erpparam为erp功能的扩展字段，可选，购买erp功能后用于erp功能集成
            }
            window.NTKF_kf = function () {
                NTKF.im_openInPageChat('hq_1000_1496721349464');
            }
        }
    }
    util.init();
});


