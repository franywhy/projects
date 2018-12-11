/**
 * FLY
 * @param options[object]
 * @param callback[function]
 * @exp
 * @author zzx
 * @date 2012-12-13
 *	"startObj":开始移动的元素(必须[obj]),	
 *  "endObj":要到达的元素(必须[obj]),
 *	"flyObj":要飞的元素（默认是startObj[string|object]）,	
 *	"endOpacity":到达时的透明度(默认0),	
 *	"endWidth":到达时的宽度(默认不变),	
 *	"endHeight":到达时的高度(默认不变),	
 *	"fadeOutSpeed":消失的时间(默认500),	
 *	"flySpeed":移动的时间(默认800),	
 *	"remove":到达后是否消失(默认true),
 *	"x":"x轴位置偏移",
 *  "y":"y轴位置偏移"
 * */
(function($){
	$.fn.fly = function(options,callback){
		var setting = {
			startObj : "",
			endObj :"",
			endOpacity:0.7,
			flySpeed:800,
			fadeOutSpeed:500,
			endWidth:null,
			endHeight:null,
			remove:true,
			flySrc:'this',
			x:0,
			y:0
		},
		start = {},end = {};
		var opts = $.extend(setting,options);
		if(opts.startObj==""||opts.endObj==""){
			return;
		};
		var fly = {
			init:function(){
				this.getEle(start,opts.startObj);
				this.getEle(end,opts.endObj);
				this.addAnimate();
			},
			getEle:function(obj,obj2){
				obj.ele = typeof obj2==="object"?obj2:$(obj2);
				obj.left = obj.ele.offset().left;
				obj.top = obj.ele.offset().top;
			},
			addAnimate:function(){
				var clone,
				_this = this;
				end.endWidth = opts.endWidth||start.ele.width();
				end.endHeight = opts.endHeight || start.ele.height();
				clone = $(document.createElement("img"));
				clone.attr("src",opts.flySrc);				
				$("body").append(clone);
				clone.load(function(){
					clone.css({
						"position":"absolute",
						"top":start.top+parseInt(opts.y),
						"left":start.left+parseInt(opts.x),
						"margin":0,
						"opacity":0.1,
						"z-index":1000
					});
					//先向上动画
					clone.animate({
						"top":start.top+parseInt(opts.y)-120,
						"opacity":1
					},400,function(){
					//再飞过去
						clone.animate({
							"left":end.left+22,
							"top":end.top+16,
							"width":end.endWidth,
							"height":end.endHeight,
							"opacity":opts.endOpacity
						},
						opts.flySpeed,
						"",
						function(){
							if(opts.remove){
								clone.fadeOut(opts.fadeOutSpeed,function(){						
									clone.remove();
									if(typeof callback==="function"){
										callback();
									}else{
										_this.addNumber();
									}
								});
							}else{
								if(typeof callback==="function"){
									callback();
								}else{
									_this.addNumber();
								}
							}
						});	
					});
				});
			},
			//+1效果（默认回调方法）
			addNumber:function(){
				if($("#numberAdd").length==0){
					$("body").append('<div id="numberAdd">+1</div>');
				}
				var _stX = end.left+25,
				_stY = end.top+5,
				_pos = 30,
				_speed=800,
				_endY = _stY-_pos;
				$("#numberAdd").stop()
							   .show()
							   .css({"left":_stX,"top":_stY,"opacity":1})
							   .animate({"top":_endY,"opacity":0},_speed,"",function(){
								   $("#numberAdd").remove();
							   });
			}
		};
		fly.init();
	}
}($));

