window.pageScript=(new function(){var n=this;n.businessId=pageParam.businessId,this.init=function(){return"zikao"==n.businessId?processingbar.init("#ffda05"):"kuaiji"==n.businessId?processingbar.init("#18aa1f"):processingbar.init("#16a6e5"),n.bindEvent(),n},this.bindEvent=function(){$("#close").click(function(){$(".mask-layer").fadeOut(500)})},this.contactTeacher=function(e,a,n,t,i,s,o,r,c){var d,l,p,u,h;d=window,l=document,p="script",u="ud",d.UdeskApiObject=u,d.ud=d.ud||function(){(d.ud.d=d.ud.d||[]).push(arguments)},(h=l.createElement(p)).async=1,h.charset="utf-8",h.src="http://assets-cli.udesk.cn/im_client/js/udeskApi.js",(p=l.getElementsByTagName(p)[0]).parentNode.insertBefore(h,p),ud({code:"254kjhc5",link:"http://hqjy.udesk.cn/im_client/?web_plugin_id=41648",agent_id:a,session_key:n,language:"zh-cn",onlineText:"联系班主任",offlineText:"班主任不在线",mode:"inner",manualInit:!0,customer:{nonce:o,timestamp:r,c_phone:t,c_email:i,web_token:s,signature:c},selector:"#im_s",pop:{direction:"top",arrow:{top:0,left:"70%"}},onReady:function(){ud.init(),ud.init(),ud.showPanel()},onUnread:function(e){console.log("未读消息数"+e.count)}})},this.goVideo=function(e){hqTool.showLoad(n.businessId);var a=window.open();$.ajax({url:"/learningCenter/app/live/liveRoom",type:"GET",async:!0,data:{classplanLiveId:e},dataType:"json",success:function(e){return hqTool.hideLoad(),200==e.code?a.location.href=e.data.liveUrl:alert(e.message),!1}})},this.goReplay=function(e){hqTool.showLoad(n.businessId);var a=window.open();$.ajax({url:"/learningCenter/app/live/replay",type:"GET",async:!0,data:{classplanLiveId:e},dataType:"json",success:function(e){return hqTool.hideLoad(),200==e.code?a.location.href=e.data.replayUrl:alert(e.message),!1}})},this.preview=function(e){900<$(window).height()?($(".mask-layer .content-bar").css("margin-top","-375px"),$(".mask-layer .content-bar .content").css("height","750px"),$("#previewContent").css("height","750px")):($(".mask-layer .content-bar").css("margin-top","-310px"),$(".mask-layer .content-bar .content").css("height","620px"),$("#previewContent").css("height","620px"));var a=e.attr("data-id");$("#previewContent").empty(),hqTool.showLoad(n.businessId),$.ajax({url:"/learningCenter/web/live/hope/material",type:"GET",async:!0,data:{materialId:a},dataType:"json",success:function(e){hqTool.hideLoad();var a="";if(200!=e.code)return alert(e.message),!1;a+='<div class="nav-list"><ul class="outer-ul">';for(var n=0;n<e.data.type.length;n++){a+='<li class="outer-li"><div class="pr" onclick="pageScript.navAction($(this))"><span class="grey-arrow-icon top-arrow-icon bg-cover"></span><span class="pl-15px b">'+e.data.type[n].name+"</span></div>",""!=e.data.type[n].detail&&(a+='<ul class="inner-ul">');for(var t=0;t<e.data.type[n].detail.length;t++)a+='<li class="text-overflow" data-id="'+e.data.type[n].detail[t].detailId+'" onclick="pageScript.getPreviewContent($(this))">'+e.data.type[n].detail[t].name+"</li>";""!=e.data.type[n].detail&&(a+="</ul>"),a+="</li>"}a+="</div>",$("#previewNav").empty(),$("#previewNav").append(a),$("#previewNav .outer-ul:eq(0) .inner-ul li:eq(0)").trigger("click")}}),$(".mask-layer").fadeIn(500)},this.getPreviewContent=function(e){var a=e.attr("data-id");e.hasClass("active")||($("#previewContent").next(".loading-bar").show(),$.ajax({url:"/learningCenter/web/live/hope/material/content",type:"GET",async:!0,data:{detailId:a},dataType:"json",success:function(e){return $("#previewContent").next(".loading-bar").hide(),200==e.code?($("#previewContent").empty(),$("#previewContent").append(e.data.content)):alert(e.message),!1}})),e.parent().parent().parent().find("li").removeClass("active"),e.addClass("active")},this.navAction=function(e){e.find(".grey-arrow-icon").hasClass("top-arrow-icon")?(e.parent().find(".inner-ul li").slideUp(300),e.find(".grey-arrow-icon").removeClass("top-arrow-icon").addClass("down-arrow-icon")):(e.parent().find(".inner-ul li").slideDown(300),e.find(".grey-arrow-icon").removeClass("down-arrow-icon").addClass("top-arrow-icon"))}}).init();