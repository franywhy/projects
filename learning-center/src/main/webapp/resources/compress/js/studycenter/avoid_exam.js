window.pageScript=(new function(){var s=this;s.classType="",s.userPlanId="",s.course="",s.courseId="",s.startTime="",s.endTime="",s.cause="",s.businessId=pageParam.businessId,s.attendanceId=null,this.init=function(){return s.datetimepicker(".datetimepicker"),s.bindEvent(),s},this.bindEvent=function(){$("#applyBtn").click(function(){$("#attFormLayer .mask-layer").show(300)}),$("#attFormLayer .grey-close-p").click(function(){$("#attFormLayer .mask-layer").hide(300)}),$("#classType").click(function(){s.sliderAction($(this))}),$("#course").click(function(){if(""==$.trim($("#classType input").val()))return $("#promptText .text-se").text("请先选择班型"),$("#promptText").css("display","inline-block"),!1;s.sliderAction($(this))}),$("#selectList > .select-list").click(function(){var e=$(this).text();$(this).parent().parent().find("input").val(e),s.userPlanId=$(this).attr("data-id"),$(this).parent().slideUp(),$(this).parent().parent().find("input").css("background","#fff"),s.getCourseList()}),$("#cancelBtn,#cancelConfirmLayer .grey-close-p").click(function(){$("#cancelConfirmLayer .mask-layer").hide(300)}),$("#confirmBtn").click(function(){s.cancelRequset()}),$("#form").submit(function(){return s.checkForm()&&s.submitForm(),!1})},this.datetimepicker=function(e){$.datetimepicker.setLocale("ch"),$(e).datetimepicker({value:"",format:"Y-m-d",maxDate:!1,maxTime:!1,minDate:new Date,minTime:"0",yearStart:2017,yearEnd:"2100",allowBlank:!1,allowTimes:[],inline:!1,todayButton:!0,datepicker:!0,weeks:!1,timepicker:!1})},this.getCourseList=function(){hqTool.showLoad(s.businessId),$.ajax({url:"/learningCenter/web/courseAbnormalFreeAssessment/courseListByOrderId",type:"GET",async:!0,data:{orderId:s.userPlanId},dataType:"json",success:function(e){if(hqTool.hideLoad(),200!=e.code)return alert(e.message),!1;if($("#course input").val(""),$("#selectList2").empty(),null!=e.data)for(var t=0;t<e.data.length;t++)$("#selectList2").append('<div class="select-list" data-id="'+e.data[t].courseId+'" onclick="pageScript.selectList($(this))">'+e.data[t].courseName+"</div>");return!1}})},this.checkForm=function(){return s.classType=$.trim($("#classType input").val()),s.course=$.trim($("#course input").val()),s.startTime=$.trim($("#startTime input").val()),s.endTime=$.trim($("#endTime input").val()),s.cause=$.trim($("#cause").val()),""==s.classType?($("#promptText .text-se").text("请输入完整的免考核申请内容，不能为空。"),$("#promptText").css("display","inline-block"),!1):""==s.course?($("#promptText .text-se").text("请输入完整的免考核申请内容，不能有为空项。"),$("#promptText").css("display","inline-block"),!1):""==s.startTime?($("#promptText .text-se").text("请输入完整的免考核申请内容，不能有为空项。"),$("#promptText").css("display","inline-block"),!1):""==s.endTime?($("#promptText .text-se").text("请输入完整的免考核申请内容，不能有为空项。"),$("#promptText").css("display","inline-block"),!1):hqTool.replaceNumber(s.startTime)>hqTool.replaceNumber(s.endTime)?($("#promptText .text-se").text("开始时间不能大于结束时间"),$("#promptText").css("display","inline-block"),!1):""!=s.cause||($("#promptText .text-se").text("请输入完整的免考核申请内容，不能有为空项。"),$("#promptText").css("display","inline-block"),!1)},this.submitForm=function(){hqTool.showLoad(s.businessId),$.ajax({url:"/learningCenter/web/courseAbnormalFreeAssessment/save",type:"GET",async:!0,data:{orderId:s.userPlanId,courseId:s.courseId,startTime:s.startTime,endTime:s.endTime,abnormalReason:s.cause},dataType:"json",success:function(e){return hqTool.hideLoad(),$("#promptText").hide(),200==e.code?window.location.reload():alert(e.message),!1}})},this.cancel=function(e){s.attendanceId=e,$("#cancelConfirmLayer .mask-layer").show(300)},this.cancelRequset=function(){if(null==s.attendanceId)return!1;hqTool.showLoad(s.businessId),$.ajax({url:"/learningCenter/web/courseAbnormalFreeAssessment/updateCancel",type:"GET",async:!0,data:{id:s.attendanceId},dataType:"json",success:function(e){return hqTool.hideLoad(),200==e.code?window.location.reload():alert(e.message),!1}})},this.sliderAction=function(e){$(".select-bar .select").slideUp(),$(".select-bar input").css("background","#fff"),e.parent().find(".select").is(":hidden")&&(e.parent().find("input").css("background","#e6e6e6"),e.parent().find(".select").slideDown())},this.selectList=function(e){var t=e.text();e.parent().parent().find("input").val(t),s.courseId=e.attr("data-id"),e.parent().slideUp(),e.parent().parent().find("input").css("background","#fff"),$("#promptText").hide()}}).init();