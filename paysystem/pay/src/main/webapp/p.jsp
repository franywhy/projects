<script src="statics/libs/js/jquery.min.js"></script>
<script src="statics/libs/js/jquery.qrcode.js"></script>
<script src="statics/libs/js/qrcode.js"></script>
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
		var wxurl = "http://weixin.hqjy.com/pay";
		var zfburl = "http://pay.hqjy.com";
		if (flag != null) {
			 zfburl = "http://10.0.33.230:8080";
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