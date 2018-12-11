/**
 * 验证插件   
 * @author fulh
 * 2013-07-25
 =======使用说明=================
配置选项[data-options]：
emptyMsg        表单为空提示信息
invalidMsg        表单无效提示信息
validateType    验证规则  支持数组  eg:  validateType:'length[2,10]' ; validateType:['length[2,10]','email']
defaultVal        要移除的默认值


配置   
validateInput

.invalidInput  表示要验证的表单项
 ==============================
 */
$.module(function () {
    var defaultOptions = {
        emptyMsg: '该输入项为必输项',//invalidMsg
        validateType: null,//
        invalidMsg: '',
        defaultVal: null,//要移除的默认值

    },
        rules = {
            email: {
                validator: function (value) {
                    return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(value);
                }, message: "请输入有效的电子邮件地址"
            },
            url: {
                validator: function (value) {
                    return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
                }, message: "请输入有效的URL地址"
            },
            length: {
                validator: function (value, param) {
                    var len = $.trim(value).length;
                    return len >= param[0] && len <= param[1];
                },
                message: "输入内容长度必须介于{0}和{1}之间"
            },
            remote: {
                validator: function (_28, _29) {
                    var _2a = {};
                    _2a[_29[1]] = _28;
                    var _2b = $.ajax({ url: _29[0], dataType: "json", data: _2a, async: false, cache: false, type: "post" }).responseText;
                    return _2b == "true";
                }, message: "请修正该字段"
            },
            idcard: {// 验证身份证 
                validator: function (value) {
                    return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
                },
                message: '身份证号码格式不正确'
            },
            minLength: {
                validator: function (value, param) {
                    return value.length >= param[0];
                },
                message: '请输入至少{0}个字符.'
            },
            phone: {// 验证电话号码 
                validator: function (value) {
                    return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
                },
                message: '格式不正确,请使用下面格式:020-88888888'
            },
            mobile: {// 验证手机号码 
                validator: function (value) {
                    return /^(13|15|18)\d{9}$/i.test(value);
                },
                message: '手机号码格式不正确'
            },

        },
        _validateInput = 'input[data-options]:not(:disabled),select[data-options]:not(:disabled)';



    function _getOptions(dataOptions) {
        var s = $.trim(dataOptions), opt = {};
        if (s) {
            var _start = s.substring(0, 1);
            var _end = s.substring(s.length - 1, 1);
            if (_start != "{") {
                s = "{" + s;
            }
            if (_end != "}") {
                s = s + "}";
            }
            opt = (new Function("return " + s))();
        }
        return $.extend({}, defaultOptions, opt);
    }

    function _validate($input) {
        var opt = $input.data('validate'), val = $.trim($input.val());

        if (!opt) {
            opt = _getOptions($input.attr('data-options'));
            $input.data('validate', opt);
        }

        function _ruleValidate(validateType) {
            var ruleSet = /([a-zA-Z0-9_]+)(.*)/.exec(validateType);
            var rule = rules[ruleSet[1]];
            if (rule && val) {
                var param = eval(ruleSet[2]);
                if (!rule['validator'](val, param)) {
                    $input.addClass('validateErrorInput');
                    if (param) {
                        for (var i = 0; i < param.length; i++) {
                            rule.message = rule.message.replace(new RegExp("\\{" + i + "\\}", "g"), param[i]);
                        }
                    }
                    var msg = opt.validateMsg || rule.message;

                    $input.showTip(msg).focus();
                    return false;
                }
            }
            return true;
        }

        var _isPassDefault = false;
        if (val == '' || val == opt.defaultVal) {
            if (opt.required) {
                $input.addClass('validateErrorInput').showTip(opt.emptyMsg);
                return false;
            }
            _isPassDefault = true;
        }

        //默认值通过以后验证
        if (!_isPassDefault && opt.validateType) {
            if (typeof opt.validateType == "string") {
                if (!_ruleValidate(opt.validateType)) {
                    return false;
                }
            } else {
                for (var i = 0; i < opt.validateType.length; i++) {
                    if (!_ruleValidate(opt.validateType[i])) {
                        return false;
                    }
                }
            }
        }

        $input.removeClass('validateErrorInput');
        var tip = $input.data("tip");
        if (tip) {
            tip.hide();
        }
        return true;
    }


    $.fn.showTip = function (msg) {
        var $this = $(this);

        var dialog = $('.aui_state_lock');
        var inputOffset = $this.offset();

        var tip = $('<div tabindex="-1" class="tooltip">\
<div class="tooltip-content"></div>\
<div class="tooltip-arrow-outer"></div>\
<div class="tooltip-arrow"></div>\
</div>');

        var temTip = $this.data("tip");

        if (dialog) {//弹出框内
            var dialogOffset = dialog.offset();
            if (!temTip) {
                dialog.append(tip);
                $this.data("tip", tip);
                temTip = tip;
            }
            temTip.css({ top: inputOffset.top - dialogOffset.top + 6 + $this.outerHeight(), left: inputOffset.left - dialogOffset.left });
        } else {
            if (!temTip) {
                $('body').append(tip);
                $this.data("tip", tip);
                temTip = tip;
            }
            temTip.tip.css({ top: offset.top + 6 + $this.outerHeight(), left: offset.left });
        }
        temTip.show().find('.tooltip-content').html(msg);
        return $this;
    };
    
    $.fn.formValidate = function () {
        var _form = $(this);
        _form.find(_validateInput).each(function () {
            if (!_validate($(this))) {
                return false;
            }
        })
        var firstInput = _form.find(".validateErrorInput");
        firstInput.filter(":not(:disabled):first").focus();
        return firstInput.length == 0;
    };
    
    //验证表单初始化
    $.fn.formValidateInit = function () {
        var _form = $(this);
        _form.find(_validateInput).each(function () {
           // if (this.tagName == "select") {
                $(this).bind("blur keyup change", function myfunction() {
                    _validate($(this));
                });//.attr("autocomplete","off");
            
        });
        //全部清空
        $(".tooltip").remove();

        $("body").delegate(".tooltip", {
            "mouseover": function () {
                $(this).css("zIndex", 10001);
            },
            "mouseout": function () {
                $(this).css("zIndex", 10000);
            }
        })

    };

});