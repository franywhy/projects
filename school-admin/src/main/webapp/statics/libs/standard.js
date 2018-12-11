/**
 * 原型拓展
 * @author     LiuZhaoHui <hi.liuzhaoxin@gmail.com>
 * @link       http://www.eatbean.com
 * Object
 * String
 * Array
 * Date
**/
if (!("bind" in Function.prototype)){
	Function.prototype.bind = function (_this)
	{
		var fn = this, args = Array.prototype.slice.call(arguments, 1);
		
		return function ()
		{
			return fn.apply(_this, args.concat(Array.prototype.slice.call(arguments)));
		};
	};
}

if (!("trim" in String.prototype)){
	(function () {
		var whitespace = "\n\r\t\f\x0b\xa0\x20\u2000\u2001\u2002\u2003\
		\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u200b\u2028\u2029\u3000";
		
		String.prototype.trim = function ()
		{
			var str = this;
			var i = 0, iLen = str.length - 1;
			var k, kLen = whitespace.length;
			var l = false, r = false;
			var c1, c2, c3;
			
			while (i < iLen)
			{
				c1 = str.charAt(i);
				c2 = str.charAt(iLen);
				
				for (k = 0; k < kLen; k++)
				{
					c3 = whitespace.charAt(k);
					
					if (!l && c1 === c3)
					{
						l = true;
					}
					
					if (!r && c2 === c3)
					{
						r = true;
					}
				}
				
				if (!l && !r)
				{
					break;
				}
				
				if (l)
				{
					i++;
					l = false;
				}
				
				if (r)
				{
					iLen--;
					r = false;
				}
			}
			
			return i === iLen ? "" : str.slice(i, ++iLen);
		};
	})();
}

/*
var parent = {};
var child = 
{
	__proto__ : parent
}

var obj = Object.create(child);
obj.attr = "";

obj.__proto__ = {};
var obj2 = Object.create(obj);
*/

if (!("create" in Object)){
	Object.create = function (obj)
	{
		if (undefined === obj) return {};
		
		var fn = new Function ();
		
		if (obj.hasOwnProperty("__proto__"))
		{
			var f = new Function ();
			f.prototype = obj.__proto__;
			
			fn.prototype = new f();
			var proto = fn.prototype;
			
			for (var key in obj)
			{
				proto[key] = obj[key];
			}
		}
		else
		{
			fn.prototype = obj;
		}
		
		return new fn();
	};
}

if (!("keys" in Object)){
	Object.keys = function (obj)
	{
		var key, ret = [];
		
		if (obj !== Object(obj))
		{
			return ret;
		}
		
		for (key in obj)
		{
			if (obj.hasOwnProperty(key))
			{
				ret.push(key);
			}
		}
	
		return ret;
	};
}


Date.prototype.format = function(format) {
	var o = {
		"M+": this.getMonth() + 1, //month
		"d+": this.getDate(), //day
		"h+": this.getHours(), //hour
		"m+": this.getMinutes(), //minute
		"s+": this.getSeconds(), //second
		"q+": Math.floor((this.getMonth() + 3) / 3), //quarter
		"S": this.getMilliseconds() //millisecond
	}
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};

/*
*eg: new Date().toLocaleString();
*@return 2011-01-01 12:00:00
*/
Date.prototype.toLocaleString = function() {
	if(this.toString()=="Invalid Date"){
		return '';
	} else {
    	return this.format("yyyy-MM-dd hh:mm:ss");
	}
};


/*
TODO:Array暂不拓展
if (!("isArray" in Array)){
	Array.isArray = function (v)
	{
		return "[object Array]" === Object.prototype.toString.call(v);
	};
}

if (!("indexOf" in Array.prototype)){
	Array.prototype.indexOf = function (data, pos)
	{
		var a = this;
		var i = undefined === pos ? 0 : pos | 0;
		var len = a.length;
		
		if (i >= len)
		{
			return -1;
		}
		
		if (i < 0)
		{
			i += len;
		}
		
		for (; i < len; i++)
		{
			if (a.hasOwnProperty(i) && a[i] === data)
			{
				return i;
			}
		}
		
		return -1;
	};
}

if (!("lastIndexOf" in Array.prototype)){
	Array.prototype.lastIndexOf = function (data, pos)
	{
		var a = this;
		var i = undefined === pos ? 0 : pos | 0;
		var len = a.length;
		
		if (i >= len)
		{
			return -1;
		}
		
		if (i < 0)
		{
			i += len;
		}
		else
		{
			i = len - i;
		}
		
		for (; i > -1; i--)
		{
			if (a.hasOwnProperty(i) && a[i] === data)
			{
				return i;
			}
		}
	
		return -1;
	};
}

if (!("every" in Array.prototype)){
	Array.prototype.every = function (cb, _this)
	{
		var arr = this;
		var i = 0, len = arr.length;
			
		for (; i < len; i++)
		{
			if (arr.hasOwnProperty(i) && !cb.call(_this, arr[i], i, arr))
			{
				return false;
			}
		}
	
		return true;
	};
}

if (!("some" in Array.prototype)){
	Array.prototype.some = function (cb, _this)
	{
		var arr = this;
		var i = 0, len = arr.length;
		
		for (; i < len; i++)
		{
			if (arr.hasOwnProperty(i) && !cb.call(_this, arr[i], i, arr))
			{
				return true;
			}
		}
	
		return false;
	};
}

if (!("forEach" in Array.prototype)){
	Array.prototype.forEach = function (cb, _this)
	{
		var arr = this;
		var i = 0, len = arr.length;
			
		for (; i < len; i++)
		{
			if (arr.hasOwnProperty(i))
			{
				cb.call(_this, arr[i], i, arr);
			}
		}
	};
}

if (!("map" in Array.prototype)){
	Array.prototype.map = function (cb, _this)
	{
		var arr = this;
		var i = 0, len = arr.length;
		var result = new Array(len);
			
		for (; i < len; i++)
		{
			if (arr.hasOwnProperty(i))
			{
					result[i] = cb.call(_this, arr[i], i, arr);
			}
		}
		
		return result;
	};
}

if (!("filter" in Array.prototype)){
	Array.prototype.filter = function (cb, _this)
	{
		var arr = this;
		var i = 0, len = arr.length;
		var ret = [];
			
		for (; i < len; i++)
		{
			if (arr.hasOwnProperty(i) && cb.call(_this, arr[i], i, arr))
			{
				ret.push(arr[i]);
			}
		}
		return ret;
	};
}

if (!("reduce" in Array.prototype)){
	Array.prototype.reduce = function (cb, initVal)
	{
		var arr = this;
		var i = 0, len = arr.length;
		var curr = initVal || arr[i++];
			
		for (; i < len; i++)
		{
			if (arr.hasOwnProperty(i))
			{
				curr = cb(curr, arr[i], i, arr);
			}
		}
		
		return curr;
	};
}

if (!("reduceRight" in Array.prototype)){
	Array.prototype.reduceRight = function (cb, initVal)
	{
		var arr = this;
		var i = arr.length - 1;
		var curr = initVal || arr[i--];
			
		for (; i > -1; i--)
		{
			if (arr.hasOwnProperty(i))
			{
				curr = cb(curr, arr[i], i, arr);
			}
		}
	
		return curr;
	};
}
*/