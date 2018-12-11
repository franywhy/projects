!function(){
	function pageScript(){
		var self = this;
		this.init = function(){
			return self;
		}
		this.close = function(){
			$(".mask-layer").fadeOut(500);
		}
		this.showLayer = function(){
			$(".mask-layer").fadeIn(500);
		}
	}
	window.pageScript = new pageScript().init();
}();