// JavaScript Document
;(function(){
	var obj = {
		year : null,
		month : null,
		isShow : 'hide',								//是否隐藏日期
		userPlanId : hqTool.getUrlParam('userplanId'),	//获取URL参数
		oldTime : hqTool.getUrlParam("oldTime"),		//获取历史课程时间
		today: '',										//当天
		selectedDay : '',								//选中哪日
		oneLoad : true,									//加载一次
		prevDate : hqTool.getTime(2),					//上一个月
		currentFirstDate : new Date(),					//周历初始化时间
		isPrevNext : true,								//判断切换是上一个月还是下一个月
		businessId : pageParam.businessId,				//判断显示哪种加载gif
		init : function(){
			var self = obj;
			var dateNow = new Date();
			
			obj.year = dateNow.getFullYear();
			obj.month = dateNow.getMonth()+1;
			
			if(obj.oldTime != null){
				//初始化周历
				var formatDate = hqTool.getTime(2);
				var passDay = obj.DateDiff(formatDate,obj.oldTime);
				var intPassDay = parseInt('-'+passDay);
				
				obj.currentFirstDate = obj.addDate(dateNow,intPassDay);
				obj.setDate(obj.addDate(dateNow,0));
				//end
				
				obj.selectedDay = obj.oldTime;
				
				var year = obj.oldTime.split("-")[0];
				var month =  obj.oldTime.split("-")[1];
				
				$(".f-year").html(year);
				$(".f-month").html(month);
				
				obj.isShow = "show";
				obj.getWeekData();
				obj.oneLoad = false;
			}else{
				$(".f-year").html(obj.year);
				$(".f-month").html(obj.month);
				
				obj.setDate(dateNow);
			}
			
			obj.addStyle();
			self.bindEvent();
			return self;
		},
		//绑定事件
		bindEvent : function(){
			//点击查看更多
			$("#showMore").click(function(){
				if($(this).find(".black-arrow-icon").hasClass('down-arrow-icon')){
					obj.getCalendarData();
				}else{
					obj.getWeekData();
				}
			})
		},
		//可选区域选中效果
		TodaySelected : function(that){
			obj.isPrevNext = false;
			obj.selectedDay = that.find(".f-day").attr("data-id");
			
			if(!that.hasClass("f-on") && !that.hasClass("f-today-se")){
				obj.getContent();
				
				$(".f-rili-table .f-td").each(function(i,e){
					if($(this).hasClass("f-today-se")){
						$(this).removeClass("f-today-se");
						$(this).addClass("f-today");
					}
				});
				
				$(".f-rili-table .f-td").removeClass("f-on");
				that.addClass("f-on");
			}
			obj.initWeek(false,that);
		},
		//选中点击后的日期
		selectedDayStyle : function(){
			$(".f-rili-table .f-number").each(function(i,e){
				var day =  $(this).find(".f-day").attr("data-id");
				
				if($(this).hasClass("f-today-se")){
					$(this).removeClass("f-today-se");
					$(this).addClass("f-today");
				}
				
				if(obj.selectedDay == day){
					$(".f-rili-table .f-number").removeClass("f-on");
					$(this).addClass("f-on");
					obj.getContent();
				}
			});
		},
		//调整样式和选中当天
		addStyle : function(){
			//调整日历样式
			for(var y=0;y<$(".f-rili-table .f-tbody .f-td").length;y++){
				if(y%7 == 0){
					$(".f-rili-table .f-tbody .f-td").eq(y).addClass("ml-0px");
				}else if((y+1)%7 == 0){
					$(".f-rili-table .f-tbody .f-td").eq(y).addClass("mr-0px");
				}
			}
			
			var yyyy = parseInt($(".f-year").html());
			var mm = parseInt($(".f-month").html());
			
			//选中当天
			if(obj.oldTime == null){
				if(obj.year == yyyy){
					if(obj.month == mm){
						var d = new Date();
						var today = d.getDate();
						$(".f-rili-table .f-number").each(function(i,e){
							var selectedDay = $(this).find(".f-day").text();
							if(today == selectedDay){
								if(obj.oneLoad){
									obj.selectedDay = $(this).find(".f-day").attr("data-id");
									obj.today = obj.selectedDay;
								}
								$(".f-rili-table .f-number").removeClass("f-today-se");
								$(this).addClass("f-today-se");
								return false;
							}
						});
						if(obj.oneLoad){
							obj.getContent();
							obj.oneLoad = false;
						}
					}
				}
			}else{
				if(obj.year == yyyy){
					if(obj.month == mm){
						var d = new Date();
						var today = d.getDate();
						$(".f-rili-table .f-number").each(function(i,e){
							var selectedDay = $(this).find(".f-day").text();
							if(today == selectedDay){
								$(".f-rili-table .f-number").removeClass("f-today");
								$(this).addClass("f-today");
								return false;
							}
						});
					}
				}
			}
		},
		//上一个月
		prevMonth : function(that){
			var yy = parseInt($(".f-year").html());
			var mm = parseInt($(".f-month").html());
			var month = '';
			
			obj.selectedDay = that.find(".f-day").attr("data-id");
			
			if(!$("#showMore").find(".black-arrow-icon").hasClass('down-arrow-icon')){
				obj.isPrevNext = false;
				if(mm == 1){
					//返回12月
					$(".f-year").html(yy-1);
					$(".f-month").html(12);
				}else{
					//上一月
					$(".f-month").html(mm-1);
				}
					
				if(typeof that.find(".f-day").attr("data-id") == "undefined"){
					var y = $(".f-year").text();
					var m = $(".f-month").text();
					
					if(m.toString().length <= 1){
						month = "0"+m;
					}else{
						month = m;
					}
					obj.selectedDay = y+'-'+month+'-'+'01';
				}else{
					obj.selectedDay = that.find(".f-day").attr("data-id");
				}
				
				obj.getCalendarData(function(){
					obj.prevNextMonth();
				});
			}else{
				obj.isShow = "show";
				obj.isPrevNext = true;
				
				if(that.hasClass("f-lastMonth")){
					obj.initWeek(false,that);
				}else{
					obj.setDate(obj.addDate(obj.currentFirstDate,-7));
				}
				
				var year = obj.selectedDay.split("-")[0];
				var month = obj.selectedDay.split("-")[1];
				
				var ridZero ='';
				if(month < 10){
					ridZero = parseInt(month);
				}else{
					ridZero = month;
				}
				$(".f-year").text(year);
				$(".f-month").text(ridZero);
				
				obj.getWeekData();
			}
		},
		//下一个月
		nextMonth : function(that){
			var yy = parseInt($(".f-year").html());
			var mm = parseInt($(".f-month").html());
			var month = '';
			
			obj.selectedDay = that.find(".f-day").attr("data-id");
				
			if(!$("#showMore").find(".black-arrow-icon").hasClass('down-arrow-icon')){
				obj.isPrevNext = false;
				if(mm == 12){
					//返回12月
					$(".f-year").html(yy+1);
					$(".f-month").html(1);
				}else{
					//上一月
					$(".f-month").html(mm+1);
				}
				
				if(typeof that.find(".f-day").attr("data-id") == "undefined"){
					var y = $(".f-year").text();
					var m = $(".f-month").text();
					
					if(m.toString().length <= 1){
						month = "0"+m;
					}else{
						month = m;
					}
					obj.selectedDay = y+'-'+month+'-'+'01';
				}else{
					obj.selectedDay = that.find(".f-day").attr("data-id");
				}
				
				obj.getCalendarData(function(){
					obj.prevNextMonth();
				});
			}else{
				obj.isShow = "show";
				obj.isPrevNext = true;
				
				if(that.hasClass("f-nextMounth")){
					obj.initWeek(true,that);
				}else{
					obj.setDate(obj.addDate(obj.currentFirstDate,7));
				}
				
				var year = obj.selectedDay.split("-")[0];
				var month = obj.selectedDay.split("-")[1];
				var ridZero ='';
				if(month < 10){
					ridZero = parseInt(month);
				}else{
					ridZero = month;
				}
				$(".f-year").text(year);
				$(".f-month").text(ridZero);
				
				obj.getWeekData();
			}
		},
		//本月
		currentMonth : function(){
			var mydate = new Date();
			var year = mydate.getFullYear();
			var month = mydate.getMonth()+1;
		 
			$(".f-year").html(year);
			$(".f-month").html(month);
		},
		/**
		 * 初始化周历
		 * 注：由于点击上下周日期会跳过上下周的头几天或尾几天，所以需要做判断
		 **/
		initWeek : function(param,that){
			var intPassDay = '';
			var dateNow = new Date();
			var passDay = obj.DateDiff(obj.formatDate(dateNow),obj.selectedDay);
			
			var selected_day = that.find(".f-day").attr('data-id');
			var splitDay = selected_day.split('-');
			var joinDay = splitDay[0]+splitDay[1]+splitDay[2];
			
			var splitToday = obj.today.split('-');
			var joinToday = splitToday[0]+splitToday[1]+splitToday[2];
			
			if(param){
				if(joinDay > joinToday){
					intPassDay = parseInt(passDay);
				}else{
					intPassDay = parseInt('-'+passDay);
				}
			}else{
				if(joinDay > joinToday){
					intPassDay = parseInt(passDay);
				}else{
					intPassDay = parseInt('-'+passDay);
				}
			}
			obj.currentFirstDate = obj.addDate(dateNow,intPassDay);
			obj.setDate(obj.addDate(obj.currentFirstDate,0));
		},
		/**
		 * 月历点击日期切换切换上下月
		 * 注：点击月历切换上下月会使周历初始化日期错乱，所以需要做判断
		 **/
		prevNextMonth : function(){
			var dateNow = new Date();
			obj.currentFirstDate = dateNow;
			if(obj.formatDate(dateNow) > obj.selectedDay){
				var passDay = obj.DateDiff(obj.selectedDay,obj.formatDate(dateNow));
				var intPassDay = parseInt('-'+(passDay));
				obj.setDate(obj.addDate(obj.currentFirstDate,intPassDay));
			}else{
				var passDay = obj.DateDiff(obj.formatDate(dateNow),obj.selectedDay);
				obj.setDate(obj.addDate(obj.currentFirstDate,passDay));
			}
		},
		/**
		 * 日历数据请求
		 **/
		getCalendarData : function(callBack){
			hqTool.showLoad(obj.businessId);
			$.ajax({
				url:"/learningCenter/web/live/calendar/mon",  
	            type:"GET",  
				async:true,
				data:{
					"userplanId" : obj.userPlanId,
					"date" : obj.selectedDay
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	if(data.code == 200){
	            		var tmplData = '';
	            		for(var i=0;i<data.data.length;i++){
	            			var dataList = data.data[i];
	            			var date = data.data[i].date;
	            			var cutDate = parseInt(date.substring(8,10));
	            			if(dataList.monType < 0){
	            				tmplData += '<div class="f-td f-null f-lastMonth" onclick="calendar.prevMonth($(this))">'+
												'<span class="f-day" data-id="'+date+'">'+cutDate+'</span>';
	            				if(dataList.status != 0){
	            					tmplData += '<span class="f-ke f-d-ke">课</span>';
	            				}
	            				if(dataList.isAttend != 0){
	            					tmplData += '<div class="calendar-icon-p">'+
													'<div class="calendar-icon calendar-icon-1 bg-cover"></div>'+
												'</div>';
	            				}
	            				tmplData += '</div>';
	            			}else if(dataList.monType == 0){
	            				if(i >= 7){
	            					tmplData +='<div class="f-td f-number sign '+obj.isShow+'" onclick="calendar.TodaySelected($(this))">';
		        				}else{
		        					tmplData +='<div class="f-td f-number" onclick="calendar.TodaySelected($(this))">';
		        				}
	            					tmplData += '<span class="f-day" data-id="'+date+'">'+cutDate+'</span>';
	            				if(dataList.status != 0){
	            					tmplData += '<span class="f-ke">课</span>';
	            				}
	            				if(dataList.isAttend != 0){
	            					tmplData += '<div class="calendar-icon-p">'+
			        								'<div class="calendar-icon calendar-icon-2 bg-cover"></div>'+
			        							'</div>';
	            				}
	            				tmplData += '</div>';
	            			}else if(dataList.monType > 0){
	            				tmplData += '<div class="f-td f-null f-nextMounth '+obj.isShow+'" onclick="calendar.nextMonth($(this))">'+
												'<span class="f-day" data-id="'+date+'">'+cutDate+'</span>';
	            				if(dataList.status != 0){
	            					tmplData += '<span class="f-ke f-d-ke">课</span>';
	            				}
	            				if(dataList.isAttend != 0){
	            					tmplData += '<div class="calendar-icon-p">'+
													'<div class="calendar-icon calendar-icon-1 bg-cover"></div>'+
												'</div>';
	            				}
	            				tmplData += '</div>';
	            			}
	            		}
	            		$(".f-rili-table .f-tbody").empty();
	            		$(".f-rili-table .f-tbody").append(tmplData);
	            		
	            		//选中当天样式
	            		obj.addStyle();
	            		obj.selectedDayStyle();
	            		
	            		$("#showMore").find(".black-arrow-icon").removeClass("down-arrow-icon").addClass("top-arrow-icon");
	            		$("#showMore").find(".text").text('收起本月课程');
						$(".f-rili-table .f-tbody .sign,.f-rili-table .f-tbody .f-nextMounth").slideDown();
						obj.isShow = 'show';
						
						typeof callBack == "function" && callBack();
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
			});
		},
		//周历
		getWeekData : function(){
			hqTool.showLoad(obj.businessId);
			$.ajax({
				url:"/learningCenter/web/live/calendar/week",  
	            type:"GET",  
				async:true,
				data:{
					"userplanId" : obj.userPlanId,
					"date" : obj.selectedDay
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	if(data.code == 200){
	            		var tmplData = '';
	            		for(var i=0;i<data.data.length;i++){
	            			var dataList = data.data[i];
	            			var date = data.data[i].date;
	            			var cutDate = parseInt(date.substring(8,10));
	            			if(dataList.monType < 0){
	            				tmplData += '<div class="f-td f-null f-lastMonth" onclick="calendar.prevMonth($(this))">'+
												'<span class="f-day" data-id="'+date+'">'+cutDate+'</span>';
	            				if(dataList.status != 0){
	            					tmplData += '<span class="f-ke f-d-ke">课</span>';
	            				}
	            				if(dataList.isAttend != 0){
	            					tmplData += '<div class="calendar-icon-p">'+
													'<div class="calendar-icon calendar-icon-1 bg-cover"></div>'+
												'</div>';
	            				}
	            				tmplData += '</div>';
	            			}else if(dataList.monType == 0){
	            				if(i >= 7){
	            					tmplData +='<div class="f-td f-number sign '+obj.isShow+'" onclick="calendar.TodaySelected($(this))">';
		        				}else{
		        					tmplData +='<div class="f-td f-number" onclick="calendar.TodaySelected($(this))">';
		        				}
	            					tmplData += '<span class="f-day" data-id="'+date+'">'+cutDate+'</span>';
	            				if(dataList.status != 0){
	            					tmplData += '<span class="f-ke">课</span>';
	            				}
	            				if(dataList.isAttend != 0){
	            					tmplData += '<div class="calendar-icon-p">'+
			        								'<div class="calendar-icon calendar-icon-2 bg-cover"></div>'+
			        							'</div>';
	            				}
	            				tmplData += '</div>';
	            			}else if(dataList.monType > 0){
	            				tmplData += '<div class="f-td f-null f-nextMounth '+obj.isShow+'" onclick="calendar.nextMonth($(this))">'+
												'<span class="f-day" data-id="'+date+'">'+cutDate+'</span>';
	            				if(dataList.status != 0){
	            					tmplData += '<span class="f-ke f-d-ke">课</span>';
	            				}
	            				if(dataList.isAttend != 0){
	            					tmplData += '<div class="calendar-icon-p">'+
													'<div class="calendar-icon calendar-icon-1 bg-cover"></div>'+
												'</div>';
	            				}
	            				tmplData += '</div>';
	            			}
	            		}
	            		
	            		if(!$("#showMore").find(".black-arrow-icon").hasClass('down-arrow-icon')){
	            			var dayNum = 35;
							$(".f-rili-table .f-tbody .sign,.f-rili-table .f-tbody .f-nextMounth").slideUp(function(){
								if(!--dayNum){
									$(".f-rili-table .f-tbody").empty();
				            		$(".f-rili-table .f-tbody").append(tmplData);
				            		
				            		obj.addStyle();
				            		obj.selectedDayStyle();
								}
							});
							$("#showMore").find(".black-arrow-icon").removeClass("top-arrow-icon").addClass("down-arrow-icon");
							$("#showMore").find(".text").text('查看本月课程');
							obj.isShow = 'hide';
	            		}else{
	            			$(".f-rili-table .f-tbody").empty();
		            		$(".f-rili-table .f-tbody").append(tmplData);
		            		
		            		obj.addStyle();
		            		obj.selectedDayStyle();
	            		}
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
			});
		},
		//获取课程列表和希望列表
		getContent : function(){
			hqTool.showLoad(obj.businessId);
			$.ajax({
				url:"/learningCenter/web/live/detail",
	            type:"GET",  
				async:true,
				data:{
					"userplanId" : obj.userPlanId,
					"date" : obj.selectedDay
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	$("#myCurriculumEmpty").hide();
	            	if(data.code == 200){
	            		var liveListTmplData = '';
	            		var hopeTmplData = '';
	            		
	            		if(data.data.liveDetail.length > 0 || data.data.hope.length > 0){
	            			$("#liveBroadcast").empty();
	            			$("#otherContent").empty();
	            			if(data.data.liveDetail != ''){
		            			for(var j=0;j<data.data.liveDetail.length;j++){
		            				var dataList = data.data.liveDetail[j];
		            				liveListTmplData += '<div class="live-broadcast-bar">'+
		            								'<div class="live-broadcast-title" data-id="'+dataList.classplanId+'">'+dataList.classplanName+'</div>';
		            				for(var i=0;i<data.data.liveDetail[j].list.length;i++){
			            				var list = data.data.liveDetail[j].list[i];
                                        liveListTmplData += '<div class="live-broadcast-list">'+
			                            			'<div class="fl">'+
							                        	'<div class="curriculum-name text-overflow">'+
							                                '<span class="chapter" title="'+list.classplanLiveName+'">'+list.classplanLiveName+'</span>'+
							                            '</div>'+
							                        '</div>'+
							                        '<div class="right-content">'+
							                            '<span class="name-bar">'+
							                            	'<span class="icon-p">'+
							                                	'<span class="persion-icon bg-cover"></span>'+
							                                '</span>'+
							                                '<span class="teacher text-overflow">'+list.teacher+'</span>'+
							                            '</span>'+
							                            '<span class="time-bar">'+
							                                '<span class="curriculum-list-icon curriculum-list-icon-1 bg-cover"></span>'+
							                                '<span class="pl-5px">'+list.time+'</span>'+
							                            '</span>'+
							                            '<span class="attendance-progress">'+
							                            	'<span class="attendance-text">出勤</span>'+
								                            '<span class="progress-bar" style="width:78px;">'+
							                                    '<span class="progress-bg radius-8px" style="width:78px;">'+
							                                        '<span class="progress radius-8px" style="width:'+list.attendPer+'%">'+
							                                        	'<span class="progress-text">'+list.attendPer+'%</span>'+
							                                        '</span>'+
							                                    '</span>'+
							                                '</span>'+
							                            '</span>';
			            				if(list.classStatus == 0){
			            					liveListTmplData += '<a href="javascript:;" class="disabled-btn">未开始</a>';
			            				}else if(list.classStatus == 1){
			            					liveListTmplData += '<a href="javascript:;" class="btn radius-5px" onclick="pageScript.goVideo(\''+list.classplanLiveId+'\')">即将开始</a>';
			            				}else if(list.classStatus == 2){
			            					liveListTmplData += '<a href="javascript:;" class="btn radius-5px" onclick="pageScript.goVideo(\''+list.classplanLiveId+'\')">正在直播</a>';
			            				}else if(list.classStatus == 3){
			            					liveListTmplData += '<a href="javascript:;" class="disabled-btn">已结束</a>';
			            				}else if(list.classStatus == 4){
			            					liveListTmplData += '<a href="javascript:;" class="default-btn radius-5px" onclick="pageScript.goReplay(\''+list.classplanLiveId+'\')">观看回放</a>';
			            				}
			            				liveListTmplData += '</div>'+
							                    '</div>';
			            			}
		            				liveListTmplData += '</div>';
		            			}
		            			$("#liveBroadcast").append(liveListTmplData);
	            			}
	            			if(data.data.hope != ''){
		            			hopeTmplData += '<div class="other-content-title">你希望</div>';
		            			for(var k=0; k<data.data.hope.length; k++){
		            				var hopeListData = data.data.hope[k];

		            				if(hopeListData.type == 0){
										hopeTmplData += '<div class="other-content-list-box">' ;
										hopeTmplData += '<div class="other-content-list-title text-overflow">' + hopeListData.name + '</div>' ;
										hopeTmplData += '<div class="line"></div>' ;

										for (var x = 0; x < hopeListData.webData.length; x++){
											var secondLevelData = hopeListData.webData[x];

											hopeTmplData += '<div class="other-content-list">' ;
											hopeTmplData += '<div class="pr">' ;

											hopeTmplData += '<div class="icon-box">' ;
											if(secondLevelData.fileType == 1){
												hopeTmplData += '<span class="other-content-icon-2 bg-cover cut-icon-1"></span>';
											}else if(secondLevelData.fileType == 2){
												hopeTmplData += '<span class="other-content-icon-2 bg-cover cut-icon-2"></span>';
											}else if(secondLevelData.fileType == 3){
												hopeTmplData += '<span class="other-content-icon-2 bg-cover cut-icon-3"></span>';
											}else if(secondLevelData.fileType == 4){
												hopeTmplData += '<span class="other-content-icon-2 bg-cover cut-icon-4"></span>';
											}
											hopeTmplData += '</div>';

											hopeTmplData += '<div class="small-box" data-isShow="1" onclick="calendar.showMoreDownloads($(this))">' ;
											hopeTmplData += '<div class="fl">'+secondLevelData.name+'</div>' ;
											hopeTmplData += '<div class="fr">' +
																'<div class="btn-bar">'+
																	'<span class="color-999">共 '+ secondLevelData.num +' 个文件</span><span class="grey-arrow-icon down-arrow-icon ml-10px"></span>' +
																'</div>' +
															'</div>' ;
											hopeTmplData += '</div>' ;

											hopeTmplData += '</div>' ;

											if(typeof secondLevelData.fileData !== 'undefined'){
												for (var z = 0; z < secondLevelData.fileData.length; z++){
													var thirdLevelData = secondLevelData.fileData[z];
													hopeTmplData += '<div class="other-content-list-2 hide">' +
														'<div class="fl text-overflow" style="padding-right: 10px; width: 680px;">' +
														'<span class="green-dot circle"></span><span class="text">' + thirdLevelData.fileName + '</span>' +
														'</div>' +
														'<div class="fr">' +
														'<div class="btn-bar"><a href="' + thirdLevelData.fileUrl + '" class="btn radius-5px" target="_blank">下载</a></div>' +
														'</div>' +
														'</div>';
												}
											}

											hopeTmplData += '</div>' ;
											hopeTmplData += '</div>' ;
										}
										hopeTmplData += '</div>' ;
                                    }
                                    /*if(hopeListData.type == 1) {
                                        hopeTmplData += '<a href="javascript:;" class="btn radius-5px" data-id="'+secondLevelData.data+'" onclick="pageScript.preview($(this))">预览</a>';
									}*/
			            		}
		            			$("#otherContent").append(hopeTmplData);
	            			} else {
                                hopeTmplData += '<div class="other-content-title">你希望</div>';
                                hopeTmplData += '<p class="text-center" style="margin-top: 50px;">暂无资料哦~</p>' ;
                                $("#otherContent").append(hopeTmplData);
							}
	            			obj.progressAction();
	            		}else{
	            			$("#liveBroadcast").empty();
	            			$("#otherContent").empty();
	            			$("#myCurriculumEmpty").show();
	            		}
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
			});
		},

		//显示隐藏下载列表
        showMoreDownloads : function(that) {
		    var isShow = that.attr("data-isShow");

            if(isShow == "1"){
                that.attr("data-isShow", "2");
                that.find(".grey-arrow-icon").removeClass("down-arrow-icon").addClass("top-arrow-icon");
                that.parent().nextAll().show(300);
            }else{
                that.attr("data-isShow", "1");
                that.find(".grey-arrow-icon").removeClass("top-arrow-icon").addClass("down-arrow-icon");
                that.parent().nextAll().hide(300);
            }
        },

		//课程列表进度条动画效果
		progressAction : function(){
			$(".live-broadcast-list").hover(function(){
				var txt = parseInt($(this).find(".progress-text").text());
				if(txt == 0){
					$(this).find(".progress").animate({"width":"100%"},300);
				}else if(txt < 30){
					$(this).find(".progress").animate({"width":"35%"},300);
				}
			},function(){
				var txt = parseInt($(this).find(".progress-text").text());
				$(this).find(".progress").animate({"width":txt+"%"},300);
			});
		},
		//周历-格式转换
		formatDate : function(date){
            var year = date.getFullYear()+'-';
            var month = (date.getMonth()+1)+'-';
            var day = date.getDate();
            if(parseInt(month) < 10){
            	month = "0"+month;
            }
            if(day < 10){
            	day = "0"+day;
            }
            return year+month+day;
        },
        //周历-第几天
        addDate : function(date,n){
            date.setDate(date.getDate()+n);
            return date;
        },
        //周历-显示与选中第几天
        setDate : function(date){
        	obj.currentFirstDate = new Date(date);
        	
        	if($("#showMore .black-arrow-icon").hasClass("down-arrow-icon")){
        		if(obj.isPrevNext){
            		$(".f-rili-table .f-td").each(function(i,e){
            			if(i == 0){
                			obj.selectedDay = obj.formatDate(i==0 ? date : obj.addDate(date,1));
                		}
                	});
            	}
        	}
        	return false;
        },
        /**
         * 计算过去了多少天
         * sDate1和sDate2是2000-12-18格式
         * */
        DateDiff : function(sDate1, sDate2){
        	var aDate, oDate1, oDate2, iDays;
            aDate = sDate1.split("-");
            oDate1 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);	//转换为12-18-2006格式  
            aDate = sDate2.split("-");
            oDate2 = new Date(aDate[1]+'-'+aDate[2]+'-'+aDate[0]);
            iDays = parseInt(Math.abs(oDate1-oDate2) / 1000 / 60 / 60 /24);		//把相差的毫秒数转换为天数  
            return iDays;
        }
	}
	window.calendar = new obj.init();
})();