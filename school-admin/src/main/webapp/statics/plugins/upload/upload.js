/**
 *
 */
var hq = hq || {};
hq.core = hq.core || {};
//hq.core.upload = {
//		
//}

//单文件上传组件
hq.core.upload = function (inputId, vueName, titleName, fileType) {
    //this.inputId = inputId;
    //$("#cb_input_id").val(inputId);
    document.getElementById("uploadForm").action = hq.config.pic_upload_url;
    document.getElementById("file").value = "";
    layer.open({
        type: 1,
        skin: 'layui-layer-molv',
        title: titleName || '上传图片',
        area: ['291px', '233px'],
        shadeClose: false,
        content: jQuery("#div_iframe"),
        success: function () {
            if (fileType) {
                $('#file').attr('accept', fileType);
            }
        },
        btn: ['确定', '取消'],
        btn1: function (index) {
//			//参数
            var cb_params = "layer_index=" + index;
//			console.log("type:"+(typeof inputId));
            if (vueName != null && vueName != '') {
                cb_params += "&cb_vue=" + vueName;
            }
            if (inputId != null && inputId != '') {
                cb_params += "&cb_input_id=" + inputId;
            }
//			//参数
//			var cb_params = "cb_input_id="+inputId + "&layer_index=" + index;
            //当前站点回调页面地址
            var redirectUrl = "http://" + window.location.host + "/statics/plugins/upload/upload_callback.html?" + cb_params;
//			var redirectUrl= "http://localhost:8081/statics/plugins/upload/upload_callback.html?"+cb_params;
            $("#redirectUrl").val(redirectUrl);
            //提交form表单
            document.getElementById("uploadForm").submit();
        }
    });
}
hq.core.upload.close = function (layer_index) {
    //关闭浮层
    layer.close(layer_index);
}

//多文件上传组件
hq.core.mulit_upload = function (fileName, vueName, titleName, fileType) {
    //this.inputId = inputId;
    //$("#cb_input_id").val(inputId);
    document.getElementById("uploadForm").action = hq.config.mulit_upload_url;
    document.getElementById("file").value = "";
    layer.open({
        type: 1,
        skin: 'layui-layer-molv',
        title: titleName || '上传图片',
        area: ['291px', '233px'],
        shadeClose: false,
        content: jQuery("#div_iframe"),
        success: function () {
            if (fileType) {
                $('#file').attr('accept', fileType);
            }
        },
        btn: ['确定', '取消'],
        btn1: function (index) {
//			//参数
            var cb_params = "layer_index=" + index;
            //输出文件的原始名
            if (fileName != null && fileName != '') {
                cb_params += "&file_name=" + fileName;
            }
//			console.log("type:"+(typeof inputId));
            if (vueName != null && vueName != '') {
                cb_params += "&cb_vue=" + vueName;
            }
            var file = $('#file').get(0).files;
            var fileCount = file.length;
            if (fileCount > 5) {
                alert("上传的资料文件最多为5个！！");
                return;
            }
            for (var i = 0; i < fileCount; i++) {
                fileSuffix = file[i].name.substring(file[i].name.lastIndexOf('.'));
                if (/\.(xlsx|xls|ppt|pptx|docx|doc|pdf|txt)$/.test(fileSuffix)) {
                    continue;
                }
                else {
                    alert("请上传[.xlsx .xls .ppt .pptx .docx .doc .pdf .txt]格式结尾的文件！")
                    return;
                }
            }

            /*if (inputId != null && inputId != '') {
                cb_params += "&cb_input_id=" + inputId;
            }*/

//			//参数
//			var cb_params = "cb_input_id="+inputId + "&layer_index=" + index;
            //当前站点回调页面地址
            var redirectUrl = "http://" + window.location.host + "/statics/plugins/upload/mulit_upload_callback.html?" + cb_params;
//			var redirectUrl= "http://localhost:8081/statics/plugins/upload/upload_callback.html?"+cb_params;
            $("#redirectUrl").val(redirectUrl);
            //提交form表单
            document.getElementById("uploadForm").submit();
        }
    });
}
hq.core.mulit_upload.close = function (layer_index) {
    //关闭浮层
    layer.close(layer_index);
}



