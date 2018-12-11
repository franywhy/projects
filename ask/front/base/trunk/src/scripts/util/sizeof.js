
// 计算字节长度
$.sizeof = function (str, charset) {
	if (undefined === charset) {
		charset = "utf8";
	}
	
	var i = 0, len = str.length;
	var n = 0, c;
	
	// utf-16 是 javascript 本地字符集
	if ("utf16" === charset) {
		for (; i < len; i++) {
			c = str.charCodeAt(i);
			
			if (c < 65536) {
				n += 2;
			} else {
				n += 4;
			}
		}
	} else if ("utf8" === charset) {
		for (; i < len; i++) {
			c = str.charCodeAt(i);
			
			if (c < 128) {
				n++;
			} else if (c < 2048) {
				n += 2;
			} else if (c < 65536) {
				n += 3;
			} else {
				n += 4;
			}
		}
	} else if ("gbk" === charset) {
		for (; i < len; i++) {
			c = str.charCodeAt(i);
			
			if (c < 128) {
				n++;
			} else {
				n += 2;
			}
		}
	}
	
	return n;
}