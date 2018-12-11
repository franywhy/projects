/**
* ui jQuery工具插件扩展
*/
$.fn.extend({
    bindState: function(type, className) {
        var $this = this;
        if (type == undefined) {
            type = 1;
        }
        if (className == undefined) {
            className = "";
        } else {
            className = className + "-";
        }
        //绑定hover状态
        this.hover(function() {
            $(this).addClass(className + "hover");
        },
        function() {
            $(this).removeClass(className + "hover");
        });
        //点一下active,多个选择,点击选择,点击取消  复选
        if (type == 1 || type == "checkbox") {
            $(this).click(function() {
                $(this).toggleClass(className + "on");
            });
        } //点一下active,点击单个选择 单选
        else if (type == 2 || type == "radio") {
            $(this).click(function() {
                $this.removeClass(className + "on")
                $(this).addClass(className + "on");
            });
        } //点一下active,单个选择,再点一下取消 单选可取消选择
        else if (type == 3 || type == "toggle") {
            $(this).click(function() {
                var hasOn = $(this).hasClass(className + "on");
                $this.removeClass(className + "on");
                if (hasOn) {
                    $(this).removeClass(className + "on");
                } else {
                    $(this).addClass(className + "on");
                }
            });
        }
    },
    //$("#txtId").placeHolder();
    placeHolder: function(text) {
        if (this.length == 0) {
            return;
        }
        if (!!text) {
            $(this).attr("placeholder", text);
        }
        if ($(this).val().length == 0 || $(this).val() == $(this).attr("placeholder")) {
            $(this).val($(this).attr("placeholder"));
            $(this).addClass("placeholder");
        }
        else {
            $(this).removeClass("placeholder");
        }
        $(this).focus(function() {
            if ($(this).val() == $(this).attr("placeholder")) {
                $(this).val("");
                $(this).removeClass("placeholder");
            };
        });

        $(this).blur(function() {
            if ($(this).val() == "") {
                $(this).addClass("placeholder");
                $(this).val($(this).attr("placeholder"));
            };
        });
    },
    //描述：JQuery实例扩展，限制输入长度。1
    //$("#abc").maxLength(250);
    maxLength: function(maxLength) {
        var $textBox = this;
        $textBox.unbind("input propertychange change");
        $textBox.bind("input propertychange change", function() {
            var val = $textBox.val().toString();
            if (val.length > maxLength) {
                $textBox.val(val.substring(0, maxLength));
            }
        })
    }
});

//ui TODO:
(function($){
	$.ui = $.ui || {};
	$.extend($.ui,{
	/**
	 * 图片加载出错,修复图片
	 * @method fixImg
	 * @param <String> target 目标图片
	 */
	fixImg: function(target, url) {
		target.src = url;
	},
	/**
    * @method 多级菜单
    * Description
    * e.g menu('#nav');
    * @param string 导航的ID或者class表达式
    * @returns {param2} the value of the field, as set in the className
    */
    menu : function(nav, level) {
        if (!level) level = "ul";
        $('li:has(> ' + level + ')', nav).addClass('parent');
        $('li:has(> .child-extra)', nav).addClass('parent');
        $('li:has(> ' + level + ') > a', nav).addClass('hasSubnav');
        $("li.parent", nav).hover(function() {
            $(this).addClass('on'); $('> a', this).addClass('hover');
            $(this).children(level).fadeIn();
        }, function() {
            $(this).children(level).hide();
            $(this).removeClass('on'); $('> a', this).removeClass('hover');
        });
    },
    /**
    * @method 选项卡
    * Description
    * e.g tab(".nav",".content","on","mouseover");
    * @nav string 选项卡切换按钮外层的css表达式
    * @content string 选项卡切换内容外层的css表达式
    * @on string 当前选项的class名称
    * @type string 事件类型
    * @extra string 不要绑定事件的元素
    * @moreOpt string 更多的选项
    */
    tab : function($nav, content, on, type, extra) {
        $nav = $($nav);
        if (extra) {
             extra = ":not(" + extra + ")";
        } else {
           extra = "";
        }
        //绑定li的点击事件
        $nav.children(extra).bind(type, (function() {
            var tab = $(this);
            var tab_index = tab.prevAll().length;
            $nav.children().removeClass(on);
            tab.addClass(on);
            $(content).children().hide();
            $(content).children().eq(tab_index).show();
        }));
    },
    /**
    * @method 选项卡
    * Description
    * e.g focusTab(".nav","",""); 默认聚焦第一个
    * e.g focusTab(".nav","","action=1&name=1"); 聚焦<li><a href="#action=1">Text</a></li>

    * e.g focusTab(".nav","",0); 聚焦第1个

    * e.g focusTab(".nav","",1); 聚焦第2个

    * e.g focusTab(".nav","tabkey",0); 聚焦<li tabkey="0"><a href="#action=1">Text</a></li>
    * e.g focusTab(".nav","tabkey",1); 聚焦<li tabkey="1"><a href="#action=1">Text</a></li>
    * e.g focusTab(".nav","tabkey","key"); 聚焦<li tabkey="key"><a href="#action=1">Text</a></li>

    * @nav string 选项卡切换按钮外层的css表达式
    * @content string 要切换内容的css表达式
    * @aHref string 选项卡切换按钮a的href 或者 按钮的index，或者 默认是第一个
    */
    focusTab : function(nav, content, aHref, className) {
        var tab;
        nav = $(nav);
        className = !className ? "on" : className + "-on";
        $("li", nav).removeClass(className);
        //根据li上的tabkey来聚焦
        if (content == "tabkey") {
            nav.children().removeClass(className);
            var tabkey = aHref;
            if (!tabkey) {
                tab = nav.children().eq(0).addClass(className);
            } else {
                nav.children("[tabkey=" + tabkey + "]").addClass(className);
            }
        }
        //根据a的href来聚焦
        else {
            //根据Index聚焦
            if (typeof aHref === "number") {
                tab = nav.children(":eq(" + aHref + ")").addClass(className);
            }
            //根据href聚焦
            else if (typeof aHref === "string" && aHref != "") {
                tab = nav.find(">li>a[href$='#" + aHref + "']").parent();
                if (tab.length == 0) {
                    tab = nav.find(">ul>li>a[href$='#" + aHref + "']").parent();
                }
                tab.addClass(className);
            }
            //默认聚焦第一个
            else {
                tab = nav.children(":first-child").addClass(className);
            }
            content = $(content);
            if (tab.length > 0 && content.length > 0) {
                content.children().hide();
                content.children().eq(tab.prevAll().length).show();
            }
        }
    },
	/**
    * Window查看方式的 图片加载中自适应
    * e.g autoSize(imgD,W,H,M)
    * <img src="util.js" onload ="autoSize(this,88,88,0)" />
    * <img src="util.js" onload ="autoSize(this,88,88,0)" />
    * @method
    * @param <String|Object> ImgD
    * @param <int> W 最大宽度
    * @param <int> H 最大高度
    * @param <int> M 
    *  M==undefined 上下自动居中,左右不自动居中 需要在外层设置text-align:center;
    *  M==-1 不自动居中
    *  M== 0 上下左右自动居中,四边边距为0
    *   M>0  上下左右自动居中,四边边距为M, 注意2m+w=外框宽度|2m+h=外框高度    
    * @returns <Image> ImgD 图片对象
    */
    autoSize:function(imgD, W, H, M) {
        if (typeof (imgD) != "object" && typeof (imgD) == "string") {
            imgD = document.getElementById(imgD);
        }
        var tImg = new Image();
        tImg.src = imgD.src;
        var w = tImg.width;
        var h = tImg.height;
        var wn = 0, hn = 0;
        if (w > 0 && h > 0 && W > 0 && H > 0) {
            if (w / h >= W / H) {
                if (w > W) {
                    wn = W;
                    hn = (h * W) / w;
                }
                else {
                    wn = w;
                    hn = h;
                }
            }
            else {
                if (h > H) {
                    wn = (w * H) / h;
                    hn = H;
                }
                else {
                    wn = w;
                    hn = h;
                }
            }
            if (typeof M == "undefined") {
                imgD.style.marginTop = (H - hn) / 2 + 'px';
            } else if (M > -1) {
                imgD.style.marginTop = M + (H - hn) / 2 + 'px';
                imgD.style.marginLeft = M + (W - wn) / 2 + 'px';
            }
            imgD.style.width = wn + "px";
            imgD.style.height = hn + "px";
        }
        return imgD;
    }   
	});
}($));