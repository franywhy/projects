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
			$(".level-curriculum-bar .level-curriculum-title").click(function(){
				if($(this).find(".black-arrow-icon-2").hasClass("top-arrow-icon")){
					$(this).find(".black-arrow-icon-2").removeClass("top-arrow-icon").addClass("down-arrow-icon");
					$(this).parent().find(".level-curriculum-list").slideUp(300);
				}else{
					$(this).find(".black-arrow-icon-2").removeClass("down-arrow-icon").addClass("top-arrow-icon");
					$(this).parent().find(".level-curriculum-list").slideDown(300);
				}
			});
			
			$(".level-curriculum-list").hover(function(){
				$(this).find(".progress-text").css("display","block");
				var txt = parseInt($(this).find(".progress-text").text());
				if(txt == 0){
					$(this).find(".progress").animate({"width":"100%"},300);
				}else if(txt < 30){
					$(this).find(".progress").animate({"width":"35%"},300);
				}
			},function(){
				$(this).find(".progress-text").css("display","none");
				var txt = parseInt($(this).find(".progress-text").text());
				$(this).find(".progress").animate({"width":txt+"%"},300);
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
		
		this.goBackCurriculumDetail = function(that){
			var time = that.attr('data-time');
			var splitTime = time.split(" ")[0];
			window.location.href = '/learningCenter/web/live?userplanId='+pageParam.userplanId+'&title='+pageParam.title+'&isNoClass='+pageParam.isNoClass+'&oldTime='+splitTime;
		}
		
		this.goVideo = function(that){
			event.stopPropagation();
			var classplanLiveId = that.attr("data-id");
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
	            		newWindow.location.href = data.data.liveUrl+'&timestamp='+Math.random();
	            		return false;
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
            });
		}
		
		this.goReplay = function(that){
			event.stopPropagation();
			var classplanLiveId = that.attr("data-id");
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
	            		newWindow.location.href = data.data.replayUrl+'&timestamp='+Math.random();
	            		return false;
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
            });
		}
	}
	window.pageScript = new pageScript().init();
}();