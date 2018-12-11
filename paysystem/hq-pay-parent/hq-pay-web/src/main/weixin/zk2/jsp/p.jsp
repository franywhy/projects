<script src="/js/jquery.min.js"></script>
<script src="/js/jquery.qrcode.js"></script>
<script src="/js/qrcode.js"></script>
<script type="text/javascript">
	window.onload = function() {
		(function($) {
			$.getUrlParam = function(name) {
				var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
				var r = window.location.search.substr(1).match(reg);
				if (r != null)
					return unescape(r[2]);
				return null;
			};
		})(jQuery);
		var order_no = $.getUrlParam("o");
		var flag = $.getUrlParam("flag");
		var payUrl = $.getUrlParam("payUrl");
		var zfburl;
		var wxurl;
		var dev=payUrl;
		var pro=payUrl;
		var test="";
		if (flag=="dev") {
			 zfburl =dev;
			 wxurl =dev;
		}else if(flag=="test"){
			 zfburl =test;
			 wxurl =test;
		}else{
			 wxurl =pro;
			 zfburl =pro;
		}
		if (isWeiXin()) {
			var url = wxurl + '/redirectUrl?orderNo=' + order_no;
			window.location = url;
		} else if (isZFB()) {
			var url = zfburl + '/clientpay?orderNo=' + order_no;
			window.location = url;
		}
	}
	function isWeiXin() {
		var ua = window.navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == 'micromessenger') {
			return true;
		} else {
			return false;
		}
	}
	function isZFB() {
		var ua = window.navigator.userAgent.toLowerCase();
		if (ua.match(/AlipayClient/i) == 'alipayclient') {
			return true;
		} else {
			return false;
		}
	}
</script>