/**
 * 通用ajax数据处理
 * @method $.iframeRequest
 * @param {Object} 
 	opt  和$.ajax的参数一样
	url
	type:GET|POST
	redirect:是否跳转
	data:{},			
	callback:function(){}
 */
$.iframeRequest = function(opt) {
	opt = $.extend({
		type: "POST"
	},opt);

	if (!opt.url) {
		return;
	}

	var form = document.createElement("form");
	form.action = opt.url;
	form.method = opt.type;

	function createInput(key, val){
		var input = document.createElement("input");
		input.type = "hidden";
		input.name = key;
		input.value = value;
		form.appendChild(input);
	}

	for (var key in opt.data) {
		var value = opt.data[key];
		if (Object.prototype.toString.call(value) == '[object Array]') {
			for (var i = 0; i < value.length; i++) {
				createInput(key, value[i]);
			}
		} else {
			createInput(key, value);
		}
	}			

	if (!opt.redirect) {
		var iframe = document.createElement("iframe"),
			uniqueString = 'iframe' + new Date().getTime();
		
		document.body.appendChild(iframe);
		iframe.style.display = "none";
		iframe.name = iframe.contentWindow.name = form.target = uniqueString;
		
		if (iframe.readyState) {
			iframe.onreadystatechange = function() {
				if (iframe.readyState && iframe.readyState == 'complete') {
					callback();
				}
			};
		} else {
			iframe.onload = callback;
		}


		function callback() {
			iframe.onload = null;
			if("function" == typeof opt.callback){
				opt.callback();
			}					
			document.body.removeChild(iframe);
			document.body.removeChild(form);
		}											
	}

	document.body.appendChild(form);
	try {
		form.submit();
	} catch (ex) {

	}
};