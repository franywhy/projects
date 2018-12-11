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
            self.progressAction();
			return self;
		}
		this.bindEvent = function(){
			$("#curriculum .curriculum-inner-bar .curriculum-inner-title").click(function(){
				if(!$(this).nextAll('.curriculum-inner-list').is(":hidden")){
					$(this).nextAll('.curriculum-inner-list').slideUp(300);
					$(this).find('.black-arrow-icon-2').removeClass('top-arrow-icon').addClass('down-arrow-icon');
				}else{
					$(this).nextAll('.curriculum-inner-list').slideDown(300);
					$(this).find('.black-arrow-icon-2').removeClass('down-arrow-icon').addClass('top-arrow-icon');
				}
			});
		},
		//课程列表进度条动画效果
		this.progressAction = function(){
			$(".level-curriculum-list").hover(function(){
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
		}
	}
	window.pageScript = new pageScript().init();
}();