!function(){
	function pageScript(){
		var self = this;
			self.businessId = pageParam.businessId;
		this.init = function(){
			if(self.businessId == 'zikao'){
				processingbar.init("#ffda05");
			}else if(self.businessId == 'kuaiji'){
				processingbar.init("#18aa1f");
			}else{
				processingbar.init("#16a6e5");
			}
				
			self.bindEvent();
			return self;
		}
		
		this.bindEvent = function(){
			$("#close").click(function(){
				$(".mask-layer").fadeOut(500);
			});
		}
		
		this.contactTeacher = function(that, agent_id, session_key, phone, email, web_token, nonce, timestamp, sign){
//			var url = that.attr("data-url");
//			var newWindow = window.open('','_blank','width=650,height=700,menubar=no,toolbar=no, location=no,directories=no,status=no,scrollbars=yes,resizable=yes');
//				newWindow.location.href = url;
			
			(function(a,h,c,b,f,g){a["UdeskApiObject"]=f;a[f]=a[f]||function(){(a[f].d=a[f].d||[]).push(arguments)};g=h.createElement(c);g.async=1;g.charset="utf-8";g.src=b;c=h.getElementsByTagName(c)[0];c.parentNode.insertBefore(g,c)})(window,document,"script","http://assets-cli.udesk.cn/im_client/js/udeskApi.js","ud");
			ud({
			    "code": "254kjhc5",
				"link": "http://hqjy.udesk.cn/im_client/?web_plugin_id=41648",
				"agent_id":agent_id,
				"session_key": session_key,
				"language": "zh-cn", //语言-英文
				"onlineText": "联系班主任",
				"offlineText": "班主任不在线",
				"mode":"inner",
				"manualInit": true,
				"customer": {
				    "nonce": nonce,
				    "timestamp": timestamp,
				    "c_phone": phone,
				    "c_email": email,
				    "web_token": web_token,
				    "signature": sign
				},
				"selector":"#im_s",
			    "pop": {
			    "direction": "top",
			    "arrow": {
			        "top": 0,
			        "left": "70%"
			        }
			    },
			    "onReady": function() {
			    	ud.init();
//			        console.log('初始化完成');
//			    	ud.hidePanel();
			    	ud.init();
			        ud.showPanel();
			    },
			    "onUnread": function(data) {
			        console.log('未读消息数'+data.count);
			    }
			});
		}
		
		this.goVideo = function(classplanLiveId){
			hqTool.showLoad(self.businessId);
			var newWindow = window.open();
			$.ajax({
				url:"/learningCenter/app/live/liveRoom",  
	            type:"GET",  
				async:true,
				data:{
					"classplanLiveId" : classplanLiveId
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	if(data.code == 200){
	            		newWindow.location.href = data.data.liveUrl;
	            		return false;
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
            });
		}
		
		this.goReplay = function(classplanLiveId){
			hqTool.showLoad(self.businessId);
			var newWindow = window.open();
			$.ajax({
				url:"/learningCenter/app/live/replay",  
	            type:"GET",  
				async:true,
				data:{
					"classplanLiveId" : classplanLiveId
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	if(data.code == 200){
	            		newWindow.location.href = data.data.replayUrl;
	            		return false;
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
            });
		}
		
		this.preview = function(that){
			var winHeight = $(window).height();
			if(winHeight > 900){
				$(".mask-layer .content-bar").css('margin-top','-375px');
				$(".mask-layer .content-bar .content").css('height','750px');
				$("#previewContent").css('height','750px');
			}else{
				$(".mask-layer .content-bar").css('margin-top','-310px');
				$(".mask-layer .content-bar .content").css('height','620px');
				$("#previewContent").css('height','620px');
			}
			
			var id = that.attr("data-id");
			$("#previewContent").empty();
			
			hqTool.showLoad(self.businessId);
			$.ajax({
				url:"/learningCenter/web/live/hope/material",  
	            type:"GET",  
				async:true,
				data:{
					"materialId" : id
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	var tmplData = '';
	            	if(data.code == 200){
	            		tmplData += '<div class="nav-list">'+
				                        '<ul class="outer-ul">';
				        for(var i=0;i<data.data.type.length;i++){
				        	tmplData += '<li class="outer-li">'+
				        					'<div class="pr" onclick="pageScript.navAction($(this))">'+
			                                    '<span class="grey-arrow-icon top-arrow-icon bg-cover"></span>'+
			                                    '<span class="pl-15px b">'+data.data.type[i].name+'</span>'+
			                                '</div>';
			                                
							if(data.data.type[i].detail != ''){						
			                	tmplData += '<ul class="inner-ul">';
			                }
			                
				        	for(var j=0;j<data.data.type[i].detail.length;j++){
				        		tmplData += '<li class="text-overflow" data-id="'+data.data.type[i].detail[j].detailId+'" onclick="pageScript.getPreviewContent($(this))">'+data.data.type[i].detail[j].name+'</li>';
				        	}
				        	
				        	if(data.data.type[i].detail != ''){
				        		tmplData += '</ul>';
				        	}
				        	
                        	tmplData += '</li>';
				        }
				        tmplData += '</div>';
				        $("#previewNav").empty();
	            		$("#previewNav").append(tmplData);
	            		$("#previewNav .outer-ul:eq(0) .inner-ul li:eq(0)").trigger("click");
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
			});
			
			$(".mask-layer").fadeIn(500);
		}
		
		this.getPreviewContent = function(that){
			var id = that.attr("data-id");
			if(!that.hasClass("active")){
				$("#previewContent").next(".loading-bar").show();
				$.ajax({
					url:"/learningCenter/web/live/hope/material/content",  
		            type:"GET",  
					async:true,
					data:{
						"detailId" : id
					},
					dataType:"json",
		            success:function(data){
		            	$("#previewContent").next(".loading-bar").hide();
		            	if(data.code == 200){
		            		$("#previewContent").empty();
		            		$("#previewContent").append(data.data.content);
		            		return false;
		            	}else{
		            		alert(data.message);
		            		return false;
		            	}
		            }
				});
			}
			that.parent().parent().parent().find('li').removeClass("active");
			that.addClass("active");
		}
		
		this.navAction = function(that){
			if(that.find(".grey-arrow-icon").hasClass("top-arrow-icon")){
				that.parent().find(".inner-ul li").slideUp(300);
				that.find(".grey-arrow-icon").removeClass("top-arrow-icon").addClass("down-arrow-icon");
			}else{
				that.parent().find(".inner-ul li").slideDown(300);
				that.find(".grey-arrow-icon").removeClass("down-arrow-icon").addClass("top-arrow-icon");
			}
		}
	}
	window.pageScript = new pageScript().init();
}();