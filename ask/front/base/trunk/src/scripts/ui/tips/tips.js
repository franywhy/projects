(function($) {
    var $uiMask,
        $uiTips,
        _uiTipsTimer = null;
    $.tips = {
        /**
         * [显示提示框]
         * @param  {[type]} msg  [消息]
         * @param  {[type]} type [|text|loading|info|error|succeed|] 
         * @param  {[type]} time [关闭时间]
         */
        show: function(msg, type, time) {
            var that = this;
            if (!$uiTips) {
                $uiTips = $('<div id="uiTips" class="ui-tips"><span class="ui-tips-i"><span class="ui-tips-l"></span><span class="ui-tips-icon"></span><span class="ui-tips-text"></span><span class="ui-tips-r"></span></span></div>');
                $uiTips.appendTo("body");
                $uiTips.bind("click",function(){
                    that.closeTips();
                });
            }
            type = type || "text";
            $uiTips.attr("class", "ui-tips ui-tips-" + type);
            $(".ui-tips-text", $uiTips).html(msg);
            $uiTips.show();
            clearTimeout(_uiTipsTimer);
            if (!isNaN(time)) {
                _uiTipsTimer = setTimeout(function() {
                    that.hide();
                }, time * 1000);
            }
        },
        hide: function() {
            if ($uiTips) {
                $uiTips.hide();
                $(".text", $uiTips).empty();
            }
        }
    };
}($));
