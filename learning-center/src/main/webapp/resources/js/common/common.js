// JavaScript Document
var hqTool = {
	showLoad:function(type){
		if(type == 'zikao'){
			$.blockUI({message: "<img src='/resources/images/common/yellow_loading_icon.gif' width='44'/>"});
		}else if(type == 'kuaiji'){
			$.blockUI({message: "<img src='/resources/images/common/green_loading_icon.gif' width='44'/>"});
		}else{
			$.blockUI({message: "<img src='/resources/images/common/blue_loading_icon.gif' width='44'/>"});
		}
	},
	hideLoad:function(){
		$.unblockUI();
	},
	defaultImg:function(type){
		var img=event.srcElement;
		switch(type){
			case 0:
				img.src= "/resources/images/common/default_person_icon.png";
				break;
			case 1:
				img.src= "/resources/images/common/default_person_icon.png";
			default:
			break;
		}
		img.onerror=null; //控制不要一直跳动
	},
	//获取url静态参数
	getUrlParam:function(name){  
		//构造一个含有目标参数的正则表达式对象  
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
		//匹配目标参数  
		var r = window.location.search.substr(1).match(reg);  
		//返回参数值  
		if (r!=null) return unescape(r[2]);  
		return null;  
	},
	//设置cookie
    setCookie:function (name, value, seconds) {
        seconds = seconds || 0;   //seconds有值就直接赋值，没有为0，这个跟php不一样。
        var expires = "";
        if (seconds != 0) {      //设置cookie生存时间
            var date = new Date();
            date.setTime(date.getTime() + (seconds * 1000));
            expires = "; expires=" + date.toGMTString();
        }
        document.cookie = name + "=" + escape(value) + expires + "; path=/";   //转码并赋值
    },
	//获取cookie
	getCookie:function (name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {          //判断一下字符串有没有前导空格
                c = c.substring(1, c.length);      //有的话，从第二位开始取
            }
            if (c.indexOf(nameEQ) == 0) {       //如果含有我们要的name
                return unescape(c.substring(nameEQ.length, c.length));    //解码并截取我们要值
            }
        }
    },
    //清除cookie
    clearCookie:function (name) {
        cnTool.setCookie(name, "", -1);
    },
    //格式化时间格式
    getTime:function(type) {
        var d = new Date();
        var y = d.getFullYear();
        var month = d.getMonth() + 1;
        var date = d.getDate();
        var h = d.getHours();
        var m = d.getMinutes();
        var s = d.getSeconds();
        var m_s = d.getMilliseconds();
        if (month < 10) {
            month = '0' + month;
        }  if (date < 10) {
            date = '0' + date;
        }  if (h < 10) {
            h = '0' + h;
        }  if (m < 10) {
            m = '0' + m;
        }  if (s < 10) {
            s = '0' + s;
        }
        if (type == 1) {
            var now_time = y.toString() + month.toString() + date.toString();
        } else if(type == 2){
        	var now_time = y.toString() + '-' + month.toString() + '-' + date.toString();
        }else if (type == 3) {
            var now_time = y.toString() + '-' + month.toString() + '-' + date.toString() + ' ' + h.toString() + ':' + m.toString() + ':' + s.toString();
        } else if(type == 4){
        	var now_time = h.toString() + ':' + m.toString();
        }else {
            var now_time = y.toString() + month.toString() + date.toString() + h.toString() + m.toString() + s.toString() + m_s.toString();
        }
        return now_time;
    },
    //获取字符串纯数字
    replaceNumber: function(str){
		var replaceStr = str.replace(/[^0-9]+/g,'');
		return replaceStr;
	},
    showLayer:function(target,tmplData,bgColor,clickBg,callBack){
    	if(target != null){
    		var targetTmpl = $(target).html();
    		tmplData = targetTmpl;
    	}
    	$("html body").append(
			'<div class="mask-layer">'+
				'<div class="mask"></div>'+
				tmplData+
			'</div>'
        );
    	if(bgColor){
    		$(".mask").addClass("opacity-05");
    	}
    	if(clickBg){
    		$(".mask").one("click",function(){
        		hqTool.hideLayer();
        	});
    	}else{
    		$(".grey-close-p").one("click",function(){
        		hqTool.hideLayer();
        	});
    	}
    	
    	$(".mask-layer").show(300);
    	typeof callBack == "function" && callBack();
    },
    hideLayer:function(){
    	$(".mask-layer").hide(300,function(){
    		$(".mask-layer").remove();
    	});
    },
	form: {
		/*
		 * 判断是否是正确的手机号，以及手机的运营商
		 * @param {String} num
		 * 返回值:
		 *      0 不是手机号码
		 *      1 移动
		 *      2 联通
		 *      3 电信
		 */
		isPhoneNum: function (num) {
			var flag = 0;
			var phoneRe = /^1\d{10}$/;
			var dx = [133, 153, 177, 180, 181, 189, 170];
			/*电信*/
			var lt = [130, 131, 132, 145, 155, 156, 185, 186];
			/*联通*/
			var yd = [134, 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188];
			/*移动*/
			function inArray(val, arr) {
				for (i in arr) {
					if (val == arr[i]) return true;
				}
				return false;
			}
		
			if (phoneRe.test(num)) {
				var temp = num.slice(0, 3);
				if (inArray(temp, yd)) return 1;
				if (inArray(temp, lt)) return 2;
				if (inArray(temp, dx)) return 3;
				return 4;
			}
			return flag;
		},
		isEmail: function(val){
			var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
			if(!reg.test(val)){
				return false;
			}else{
				return true;
			}
		}
	}
}