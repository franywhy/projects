//标题滚动
(function(){
	var _isStart = false;
		_delay = 500,
		_timer = null,
		_original = "",
		_current = "";
		
	$.ui.scrollTitle = {
		isScroll:false,
		setTitle:function(title,isAppend){
			if(isAppend){
				title = title +" - "+ document.title;
			}
			document.title = title;
			_original = title; 
			_current = " "+$.trim(title)+" ";
		},
		start:function(delay){
			var that = this;
			if(_isStart){
				return;
			}
			_isStart = true;
			this.setTitle(document.title);
			_timer = setInterval(function(){
				var msg = _current;
				_current = msg.substring(1,msg.length)+msg.substring(0,1); 
				document.title = _current;				
			}, delay || _delay);
		},
		stop:function(){
			_isStart = false;
			clearInterval(_timer);
			document.title = _original;
		}
	};	
}());