
(function($) {
    $.query = {};
	$.extend($.query,{
		parseURL : function(url) {
		    url = url || self.location.href;
		    var p = {}, m;
		    if (m = url.match(/((s?ftp|https?):\/\/)?([^\/:]+)?(:([0-9]+))?([^\?#]+)?(\?([^#]+))?(#(.+))?/)) {
		        p['scheme'] = (m[2] ? m[2] : 'http');
		        p['host'] = (m[3] ? m[3] : self.location.host);
		        p['port'] = (m[5] ? m[5] : 80);
		        p['path'] = (m[6] ? m[6] : null);
		        p['search'] = (m[8] ? m[8] : null);
		        p['anchor'] = (m[10] ? m[10] : null);
				p['origin'] = p.scheme+'://'+p.host+(p.port==80 ? '':':'+p.port);
		        return p;
		    }
		    return null;
		},
		 /**
		 * @description 获得浏览器地址#后面的字符串key值对应的value值
		 * @param {String} key  #name=value key为name
		 * @return {String} value 返回结果 value
		 */
		getHash : function(key, url) {
			var hash;
			if ($.exists(url)) {
				hash = url.replace(/^.*?[#](.+?)(?:\?.+)?$/, "$1");
				hash = (hash == url) ? "" : hash;
			} else {
				hash = self.location.hash;
			}			
			return this.getVal(key, hash);
		},
		 /**
		 * @description 在url上添加#key=value的功能,url不存在则在默认的浏览器地址上附加
		 * @param {String} key  #name=value key为name
		 * @param {String} value  #name=value 值为value
		 * @param {String} url [可选]默认为location.href
		 * @return {}返回结果 追加key value之后的url
		 */
		setHash : function(key, value, url) {
			if (!$.exists(url)) {
				url = self.location.hash;
			}
			var p = url.match(/([^\?#]+)?(\?([^#]+))?(#(.+))?/);
			var href = $.exists(p[1]) ? p[1] : "";
			var search = $.exists(p[2]) ? p[2] : ""; //带?
			var hash = $.exists(p[5]) ? p[5] : ""; //不带#
			var newHash = this.setVal(key, value, hash);
			if (newHash == "") {
				return url;
			} else {
				return href + search + "#" + newHash;
			}
		},
		/**
		* @description  获得地址 ? 后面的查询字符串
		* eg. Query.get("pageSize");
		* @param <String> key 
		* @param <String> defaultValue 
		* @param <String> url [可选]默认为location.href
		* @return <String> value如http://www.izhubo.com/a?b=c  返回的值是c key是b
		*/
		get : function(key, url, prefix) {
			var search;
			if ($.exists(url)) {
				search = url.replace(/^.*?[?](.+?)(?:#.+)?$/, "$1");
				search = (search == url) ? "" : search;
			} else {
				search = self.location.search;
			}
			return this.getVal(key, search);
		},
		/**
		* @description 设置URL查询字符串name=value;
		* @param <String> key 
		* @param <String> value 
		* @param <String> url 要设置的url [可选]默认为location.href
		* @return <Object> url 生成的url  如$.query.set("bb","ccc","http://www.izhubo.com?a=b"); 返回的结果http://www.izhubo.com?a=b&bb=ccc
		*/
		set : function(key, value, url) {
			if (!$.exists(url)) {
				url = self.location.href;
			}
			var p = url.match(/([^\?#]+)?(\?([^#]+))?(#(.+))?/);
			var href = $.exists(p[1]) ? p[1] : "";
			var search = $.exists(p[3]) ? p[3] : ""; //不带?
			var hash = $.exists(p[4]) ? p[4] : ""; //带#
			var newSearch = this.setVal(key, value, search);
			if (newSearch == "") {
				return href+hash;
			} else {
				return href + "?" + newSearch + hash;
			}
		},
		/**
		* @description 删除URL查询字符串name=value;
		* @param <String> key 
		* @param <String> url 要设置的url [可选],默认为location.href
		* @return <Object> url 生成的url  例如:$.query.clear("b","http://www.izhubo.com?a=c&b=f")  返回的url是http://www.izhubo.com?a=c
		*/
		clear : function(key, url) {
			return this.set(key,null,url);
			/*
			var url = url || window.location.href;			
			key = (key && key.constructor === Array) ? key : [key];
			for (var i = 0; i < key.length; i++) {
				url = url.replace(new RegExp('(^|\\?|&)' + key[i] + '=[^&]*(?=&|#|$)', 'g'), '');
			}
			url = url.replace(/^\s+/, '').replace(/\s+$/, '');
			return url;
			*/
		},
		/**
		* @description 获得指定字符串中的key对应的value
		* @param <String> key 
		* @param <String> q 指定的字符串
		* @return <String> value 例如 p1=1&p2=2&p3=3指定key为p2 返回 value=2
		*/
		getVal : function(key, q) {
			q = "" + q;
			q = q.replace(/^[?#]/, '').replace(/\&amp;/ig,'&');
			q = "&" + q;
			var val = q.match(new RegExp("[\&]" + key + "=([^\&]+)", "i"));
			if (val == null || val.length < 1) {
				return undefined;
			} else {
				return $.parseAny(decodeURIComponent(val[1]));
			}
		},
		/**
		* @description 拼接key=value的字符串
		* @param <Array> key  数组
		* @param <Array> val  数组
		* @return <String> 在该字符串上继续拼接  例如 key=[p1,p2,p3] val=[1,2,3] q="a=b"; 返回 q="a=b&p1=1&p2=2&p3=3";
		*/
		setVal : function(key, val, q) {
			var newkeyValue = "";
			key = (key && key.constructor === Array) ? key : [key];
			val = (val && val.constructor === Array) ? val : [val];
			for (var i = 0; i < key.length; i++) {
				if (i != 0) {
					newkeyValue += "&";
				}
				if ($.exists(val[i])) {
					newkeyValue += key[i] + "=" + encodeURIComponent(val[i]);
				}
				q = q.replace(new RegExp('(^|\\?|&)' + key[i] + '=[^&]*(?=&|#|$)', 'g'), '');
			}
			q = q.replace(/^\s+/, '').replace(/\s+$/, '');
			q = q.replace(/^&+/, '').replace(/&+$/, '');
			if (q == "") {
				return newkeyValue;
			}
			if (newkeyValue == "") {
				return q;
			}
			return q + "&" + newkeyValue;
		}
	});
})($);