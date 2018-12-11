$(function() {
	//方法二：
	(function($) {
		$.getUrlParam = function(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r != null)
				return unescape(r[2]);
			return null;
		};
	})(jQuery);
	
	function log(msg){
		$log.html(msg);
	}
	
	try {
//		log("开始获取参数");
		var orderNo = $.getUrlParam("orderNo");
		alert(orderNo);
		var code = $.getUrlParam("code");
		var url ="/hqajax/pay/dev/wxpay";
		 $.ajax({
	          url: url,
	          type: "POST",
	          dataType: "json",
	          data: {
	            "orderNo":orderNo,
	            "code":code
	          },
	          async: false,
	          success: function(data) {
	  			if (data.code ==0) {
	  				function onBridgeReady(){
	  					   WeixinJSBridge.invoke(
	  					       'getBrandWCPayRequest', {
	  					           "appId" : data.appId,     //公众号名称，由商户传入     
	  					           "timeStamp":data.timeStamp,         //时间戳，自1970年以来的秒数     
	  					           "nonceStr" : data.nonceStr, //随机串     
	  					           "package" : data.packageStr,     
	  					           "signType" : data.signType,         //微信签名方式：     
	  					           "paySign" : data.paySign //微信签名 
	  					       },
	  					       function(res){     
	  					           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
	  					        	 //alert("支付成功");
									   window.location='/hqajax/pay/dev/paid?payOrderNo='+orderNo;
	  					           }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
	  					           else if (res.err_msg == "get_brand_wcpay_request:cancel")  {
	  					        	   alert("取消支付");
	  					           }else{
	  					        	   //支付失败
	  					        	   alert(res.err_msg + "==" + JSON.stringify(res));
	  					           }
	  					       }
	  					   ); 
	  					}
	  					if (typeof WeixinJSBridge == "undefined"){
	  					   if( document.addEventListener ){
	  					       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
	  					   }else if (document.attachEvent){
	  					       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
	  					       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
	  					   }
	  					}else{
	  					   onBridgeReady();
	  					}
	  				
	  			}else {
	  				 window.location='/hqajax/pay/dev/paid?payOrderNo='+data.payOrderNo;
	  			}
	  		
	          },
	          error: function() {
	            alert("系统异常");
	          }
	        });
	} catch (e) {
		log("错误:" + e.message);
	}

});