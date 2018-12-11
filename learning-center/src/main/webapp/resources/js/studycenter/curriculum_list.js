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
			$("#prompt").slideDown(2000);
			$("#promptCloseBtn").click(function(){
				$("#prompt").slideUp(2000);
			});
		}
	}
	window.pageScript = new pageScript().init();
}();