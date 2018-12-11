//jqGrid的配置信息
$.jgrid.defaults.width = 1000;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;

//全局配置
$.ajaxSetup({
	dataType: "json",
	contentType: "application/json",
	cache: false,
	error: function(jqXHR, textStatus, errorMsg){ // 出错时默认的处理函数
        // jqXHR 是经过jQuery封装的XMLHttpRequest对象
        // textStatus 可能为： null、"timeout"、"error"、"abort"或"parsererror"
        // errorMsg 可能为： "Not Found"、"Internal Server Error"等

        // 提示形如：发送AJAX请求到"/index.html"时出错[404]：Not Found
        alert( '发送AJAX请求到"' + this.url + '"时出错[' + jqXHR.status + ']：' + errorMsg );
    },
    beforeSend: function() {
    	hq.load();
	},
	complete: function(xhr,status){
		hq.hide();
	}
});
// jQuery.extend({
//     handleError: function( s, xhr, status, e )      {
// 		/*var errorMsg = "";
//         try {
//             var data = eval('(' + xhr.responseText + ')');
//             errorMsg = data.data;
//         } catch (e) {
//             errorMsg = xhr;
//         }
//         alert( '请求错误!' + errorMsg);*/
//
//         if (s.error) {
//             s.error.call(s.context || s, xhr, status, e);
//         }
//         if (s.global) {
//             (s.context ? jQuery(s.context) : jQuery.event).trigger("ajaxError", [xhr, s, e]);
//         }
//     },
//     httpData: function (xhr, type, s) {
//         var ct = xhr.getResponseHeader("content-type"),
//             xml = type == "xml" || !type && ct && ct.indexOf("xml") >= 0,
//             data = xml ? xhr.responseXML : xhr.responseText;
//         if (xml && data.documentElement.tagName == "parsererror")
//             throw "parsererror";
//         if (s && s.dataFilter)
//             data = s.dataFilter(data, type);
//         if (typeof data === "string") {
//             if (type == "script")
//                 jQuery.globalEval(data);
//             if (type == "json")
//                 data = window["eval"]("(" + data + ")");
//         }
//         return data;
//     }
//
// });

function getParameter(param) {
    var locString = String(window.document.location.href);
    var rs = new RegExp("(^|)" + param + "=([^&]*)(&|$)", "gi").exec(locString), tmp;
    if (tmp = rs) {
        return tmp[2];
    }
    return "";
}

//alert(isNull(null));    //true
//alert(isNull(''));      //true
//alert(isNull());       //true
//var aa={};          
//alert(isNull(aa.a));   //true
//alert(isNull(0));        //false
//alert(isNull('0'));    //false
//alert(isNull(true));   //false
//alert(isNull("undefined"));  //false
//alert(isNull(undefined));     //true
//alert(isNull([]));       //false
//alert(isNull({}));       //false
//为空判断函数
$.isNull=function(arg){
 return !arg && arg!==0 && typeof arg!=="boolean"?true:false;
}
$.isNotNull=function(arg){
	return !$.isNull(arg);
}


//重写alert
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}
//datetimepicker.language="zh-CN";

//重写confirm式样框
window.confirm = function(msg, callback , isClose){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(index){//确定事件
		if(typeof(callback) === "function"){
			if(callback("ok"))parent.layer.close(index)
		}
	});
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
  //如果可以多选
  	if(grid.getGridParam("multiselect")){
  		var selectedIDs = grid.getGridParam("selarrrow");
  		if(selectedIDs.length > 1){
  			alert("只能选择一条记录");
  			return ;
  		}
  		
  		return selectedIDs[0];
  	}else{
  		return rowKey;
  	}
//    var selectedIDs = grid.getGridParam("selarrrow");
//    if(selectedIDs.length > 1){
//    	alert("只能选择一条记录");
//    	return ;
//    }
//    
//    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}
//选择一条记录
function getJqGridSelectedRow(jqId) {
	var grid = $("#" + jqId);
	var rowKey = grid.getGridParam("selrow");
	if(!rowKey){
		alert("请选择一条记录");
		return ;
	}
	
	//如果可以多选
	if(grid.getGridParam("multiselect")){
		var selectedIDs = grid.getGridParam("selarrrow");
		if(selectedIDs.length > 1){
			alert("只能选择一条记录");
			return ;
		}
		
		return selectedIDs[0];
	}else{
		return rowKey;
	}
}

//选择多条记录
function getJqGridSelectedRows(jqId) {
	var grid = $("#" + jqId);
	var rowKey = grid.getGridParam("selrow");
	if (!rowKey) {
		alert("请选择一条记录");
		return;
	}
	//如果可以多选
	if(grid.getGridParam("multiselect")){
		return grid.getGridParam("selarrrow");
	}else{
		var arr = new Array();
		arr.push(rowKey);
		return arr;
	}

}

