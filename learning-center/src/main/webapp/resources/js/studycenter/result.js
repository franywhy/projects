!function() {
	function pageScript() {
		var self = this;
		self.businessId = pageParam.businessId;
		self.registrationId = '';
		self.course = '';
		self.ordername = '';
		self.registerpk = '';
		self.registerproinve = '';
		self.scheduledate = '';
		self.score = '';
		self.img = '';
		this.init = function() {
			self.bindEvent();
			return self;
		}
		this.bindEvent = function() {
			$("#chooseCourse").click(function() {
				if($.trim($("#chooseList").text()) != ''){
					if ($("#chooseList").is(":hidden")) {
						$("#chooseCourse .solid-down-arrow-icon").css('transform', 'rotate(180deg)');
						$("#chooseList").slideDown();
					} else {
						$("#chooseCourse .solid-down-arrow-icon").css('transform', 'rotate(0deg)');
						$("#chooseList").slideUp();
					}
				}else{
					alert('暂无报考课程');
					return false;
				}
			});

			$("#chooseList .select-list").click(function() {
				self.registrationId = $(this).attr('data-id');
				self.ordername = $(this).attr('data-ordername');
				self.registerpk = $(this).attr('data-registerpk');
				self.registerproinve = $(this).attr('data-registerproinve');
				self.scheduledate = $(this).attr('data-scheduledate');
				self.course = $(this).text();

				$("#chooseCourse .ipt-placeholder").text(self.course);
				$("#chooseCourse .ipt-placeholder").css('color', '#535353');
				$("#num").val(self.registerpk);
				$("#province").val(self.registerproinve);
				$("#time").val(self.scheduledate);
				$("#classType").val(self.ordername);

				$("#chooseList .select-list").removeClass("active");
				$(this).addClass("active");

				$("#chooseCourse .solid-down-arrow-icon").css('transform', 'rotate(0deg)');
				$("#chooseList").slideUp();
			});

			$("#uploadImg").change(function() {
				var val = $(this).val();
				var extStart = val.lastIndexOf(".");
				var ext = val.substring(extStart, val.length).toUpperCase();
				var file_size = 0;

				if (ext != ".BMP" && ext != ".PNG" && ext != ".JPG" && ext != ".JPEG") {
					$(this).val('');
					alert("只支持jpg、jpeg、png、bmp等格式的图片");
					return false;
				}

				if (self.isIE()) {	
					var img = new Image();
					img.src = val;

					if (img.readyState == "complete") {// 已经load完毕，直接打印文件大小
						var size = parseInt(img.fileSize / 1024);
						if (size > 5 * 1024) {
							alert("上传的图片大小不能超过5M！");
							return false;
						}
						return false;
					} else {
						img.onreadystatechange = function() {
							if (img.readyState == 'complete') {// 当图片load完毕
								var size = parseInt(img.fileSize / 1024);
								if (size > 5 * 1024) {
									alert("上传的图片大小不能超过5M！");
									return false;
								}
								return false;
							}
						}
					}
				} else {
					file_size = this.files[0].size;
					var size = file_size / 1024;
					if (size > 5 * 1024) {
						$(this).val('');
						alert("上传的图片大小不能超过5M！");
						return false;
					}
				}
				self.uploadImg();
			});
			
			$("#uploadImg").hover(function(){
				var type = $("#uploadImgIcon").attr("data-type");
				if(type == 1){
					$("#uploadImgMaskBox").show();
				}
			}, function(){
				var type = $("#uploadImgIcon").attr("data-type");
				if(type == 1){
					$("#uploadImgMaskBox").hide();
				}
			});

			$("#sub").click(function() {
				if(!self.checkForm()){
					return false;
				}
				
				$("#previewCourse").val(self.course);
				$("#previewNumber").val(self.registerpk);
				$("#previewProvince").val(self.registerproinve);
				$("#previewTime").val(self.scheduledate);
				$("#previewClassType").val(self.ordername);
				$("#previewScore").val(self.score);

				if(self.img != ''){
					$("#previewImg").attr('src', self.img);
				}else{
					$("#previewImg").parent().hide();
				}

				$("#resultFormLayer .mask-layer").show(300);
			});
			
			$("#resultFormLayer .grey-close-p").click(function(){
				$("#resultFormLayer .mask-layer").hide(300);
			});
			
			$("#confirmSubmit").click(function(){
				if(!self.checkForm()){
					return false;
				}
				self.submitForm();
			});
			
			$("#cancel").click(function(){
				$("#resultFormLayer .mask-layer").hide(300);
			});
		}
		
		/**
		 * 上传图片
		 * 正式线请求地址：http://file.hqjy.com/file/singleDirectUpload
		 * 测试线请求地址：http://10.0.98.80:8081/file/singleDirectUpload
		 */
		this.uploadImg = function() {
			var params = new FormData();
				params.append('file', $('#uploadImg')[0].files[0]);
				params.append('sourceId',1);
				
			hqTool.showLoad(self.businessId);
			$.ajax({
				url: "http://file.hqjy.com/file/singleDirectUpload",
		        type: 'POST',
		        cache: false,
		        data: params,
		        processData: false,
		        contentType: false,
		        dataType:"json",
		        success : function(data) {
		        	hqTool.hideLoad();
		        	if(data.code == 200){
		        		self.img = data.data;
		        		$("#uploadImgIcon").attr("src", self.img);
		        		$("#uploadImgIcon").attr("data-type", 1);
		        		return false;
		        	}else{
		        		alert(data.message);
		        		return false;
		        	}
		        },
		        error: function(data, status, e) {  //提交失败自动执行的处理函数。
		        	hqTool.hideLoad();
                    console.error(e);
                }
		    });
		}
		
		//表单校验
		this.checkForm = function(){
			var isNum = /^[0-9]*$/;
			self.score = $.trim($("#score").val());
			
			if(self.registrationId == ''){
				alert('请选择课程');
				return false;
			}else if(self.score == ''){
				alert('请输入统考成绩');
				return false;
			}else if(!isNum.test(self.score)){
				alert('统考成绩只能输入纯数字');
				return false;
			}else if(self.score <= -1 || self.score >= 101){
				alert('分数范围请控制在0~100之间');
				return false;
			}
			return true;
		}
		
		/**
		 * 提交表单请求
		 * @param self.registrationId 报考ID
		 * @param self.score 分数
		 * @param self.img 图片
		 */
		this.submitForm = function(){
			hqTool.showLoad(self.businessId);
			$.ajax({
				url:"/learningCenter/web/examinationResult/save",  
	            type:"GET",  
				async:true,
				data:{
					"registrationId" : self.registrationId,
					"score" : self.score,
					"img" : self.img
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	if(data.code == 200){
	            		window.location.reload();
	            		return false;
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
            });
		};

		this.isIE = function() {
			if (!!window.ActiveXObject || "ActiveXObject" in window) {
				return true;
			} else {
				return false;
			}
		}
	}
	window.pageScript = new pageScript().init();
}();