$(function(){
	var mdata = null;
	$.ajax({
		url : '/sharecon/sharePicUrl.json',// 跳转到 发送验证码
		data : {
			"share_id" : getUrlParam("share_id")
		},
		type : 'post',
		cache : false,
		dataType : 'json',
		success : function(data) {
			if (data.code == "1") {
				
				mdata = data.data;
				if(mdata != null){
					$("#share_img").attr({"src" : mdata.share_url});
				}
				
			} else {
				alert(data.msg);
			}
		},
		error : function() {
			// view("异常！");    
			alert("网络异常！");
		}

	});
});

function getUrlParam(name){  
    //构造一个含有目标参数的正则表达式对象  
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
    //匹配目标参数  
    var r = window.location.search.substr(1).match(reg);  
    //返回参数值  
    if (r!=null) return unescape(r[2]);  
    return null;  
}  
