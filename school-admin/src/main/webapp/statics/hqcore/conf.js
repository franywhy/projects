$.module("hq",function(){
	return {
		ajax : function(param){
			if(null != param){
				param.contentType= "application/x-www-form-urlencoded";
			}
			$.ajax(param);
		},
		load : function(){
			var index = parent.layer.load(1, {
				shade: [0.35,'#fff'], //0.1透明度的白色背景
			});
			return index;
		},
		hide : function(index){
			if(jQuery.type(index) === "number"){
				parent.layer.close(index);
			}else{
				parent.layer.closeAll('loading');
			}
		}
	};
});
////正式线（弃用）
//$.module("hq.config",function(){
//	var pic_upload_url = "http://zikaofiletest.hqjy.com/file/singleDirectUpload";//图片上传地址
//	return {
//		pic_upload_url : pic_upload_url
//	}
//});

//正式线
$.module("hq.config",function(){
	var pic_upload_url = "http://file.hqjy.com/file/singleDirectUpload";//图片上传地址
    var mulit_upload_url = "http://file.hqjy.com/file/multiDirectUpload";
    return {
        pic_upload_url : pic_upload_url,
        mulit_upload_url : mulit_upload_url
    }
});
$.module("hq.ajaxFileUpload",function(){
	var pic_upload_url = "http://file.hqjy.com/file/singleDirectUpload";//图片上传地址
    var mulit_upload_url = "http://file.hqjy.com/file/multiDirectUpload";
    return {
        pic_upload_url : pic_upload_url,
        mulit_upload_url : mulit_upload_url
    }
});
/*$.module("hq.config",function(){
	var pic_upload_url = "http://localhost:8081/file/singleDirectUpload";//图片上传地址
    var mulit_upload_url = "http://localhost:8081/file/multiDirectUpload";
    return {
        pic_upload_url : pic_upload_url,
        mulit_upload_url : mulit_upload_url
    }
});
$.module("hq.ajaxFileUpload",function(){
	var pic_upload_url = "http://localhost:8081/file/singleDirectUpload";//图片上传地址
    var mulit_upload_url = "http://localhost:8081/file/multiDirectUpload";
    return {
        pic_upload_url : pic_upload_url,
        mulit_upload_url : mulit_upload_url
    }
});*/
//测试线
//$.module("hq.config",function(){
//	var pic_upload_url = "http://177.77.77.186:8081/file/singleDirectUpload";//图片上传地址
////	var pic_upload_url = "http://177.77.77.186:8081/file/singleDirectUpload";//图片上传地址
//	
////	var pic_upload_url = ${pc.upload.url};//图片上传地址
//	return {
//		pic_upload_url : pic_upload_url
//	}
//});


