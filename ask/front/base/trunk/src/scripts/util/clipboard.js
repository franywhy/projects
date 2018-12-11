/*复制到剪贴板*/
$.copyToClipboard = function(txt, cbf) {
	var message = txt;
	if (window.clipboardData && window.clipboardData.setData) {
		if (!window.clipboardData.setData("Text", message)) {
			$.dialog.tips("复制文本内容失败!", 2);
			return false;
		}
	} else if ((navigator.userAgent.toLowerCase().indexOf("opera") != -1) && navigator.mimeTypes["application/x-shockwave-flash"]) {
		var d = document.createElement("div");
		document.getElementsByTagName("body")[0].appendChild(d);
		d.innerHTML = "<embed src='/scripts/base/util/clipboard.swf' FlashVars='clipboard=" + escape(message) + "' width='0' height='0' type='application/x-shockwave-flash'></embed>";
	} else if (window.netscape) {
		try {
			netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect")
		} catch (e) {
			if (confirm('您的firefox安全限制限制您进行剪贴板操作!\n您可以在地址栏中输入"about:config"将"signed.applets.codebase_principal_support"设置为"true"来开启此功能!\n\n您需要现在就开启此功能吗？'))
				window.location.href = "about:config";
			return false;
		}
		try {
			clip = Components.classes["@mozilla.org/widget/clipboard;1"].createInstance(Components.interfaces.nsIClipboard)
			trans = Components.classes["@mozilla.org/widget/transferable;1"].createInstance(Components.interfaces.nsITransferable)
		} catch (e) { return false; }
		trans.addDataFlavor("text/unicode");
		var oStr = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
		oStr.data = message;
		trans.setTransferData("text/unicode", oStr, message.length * 2);
		try { clipid = Components.interfaces.nsIClipboard } catch (e) { return false; }
		clip.setData(trans, null, clipid.kGlobalClipboard)
	} else {
		alert("该功能只支持MSIE，firefox和opera浏览器！");
		return false;
	}
	if (typeof (cbf) === "function") {
		cbf();
	} 

}