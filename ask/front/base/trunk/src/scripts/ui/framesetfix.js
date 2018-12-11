/*
 *  framesetfix({option})
 * -option:
 * window_minHeight: 框架 除需调整对象外其他部分所占的高度
 * fixElements: 需调整的对象集合（需要在同一水平上的）
 * example:
 * ---------------------------------------
	var _top = document.getElementById("topBar");
	var _head = document.getElementById("head");
	var _foot = document.getElementById("foot");

	var minHeight = _top.offsetHeight + _head.offsetHeight + _foot.offsetHeight;
	framesetfix({window_minHeight:minHeight,fixElements:["#menu","#iframe"]}).init();
 * ---------------------------------------
 */


$.framesetfix = function(){
	var option = {
		//框架 除需调整对象外其他部分所占的高度
		window_minHeight:0,
		//需调整的对象集合（需要在同一水平上的）
		fixElements:[]
	}
	
	var _op;
	arguments[0]? _op = arguments[0]:"";
	
	//简单选择器
	function easy_switch(elm){
		if(typeof elm == "string"){
			var strs = elm.split(" ");
			var targetElements = [document.body];
			for(var i in strs){
				if(!targetElements){return false;}
				var flagGroups = new Array();
				
				for(var j in targetElements){
					
					var datasource = typeSwitcher(targetElements[j],strs[i]);
					if(!datasource){return false;}
					if(!datasource.length){
						if(datasource){
							flagGroups.push(datasource);
						}
					}
					else{
						
						for(var k = 0; k < datasource.length; k++){
							if(datasource[k]){
								flagGroups.push(datasource[k])
							}
						}	
					}
					
				}
				targetElements = flagGroups;
			}
			if(targetElements.length ==1){return targetElements[0]}
			else {return targetElements;}
		}
		else{
			return elm;	
		}
		function typeSwitcher(elm,str){
			
			if(str.substring(0,1) == "#"){
				return document.getElementById( str.substring(1,str.length) )	
			}
			else if(str.substring(0,1) == "."){
				var flag = elm.getElementsByTagName("*");
				var results = [];
				for(var i in flag){
					if(flag[i].className == str.substring(1,str.length)){
						results.push(flag[i]);	
					}	
				}
				return results;
			}
			else{
				var result = elm.getElementsByTagName(str)	
				if(result.length >0){
					return result;	
				}
				else{
					return false;	
				}
			}
		}
	}	
	
	function areaReset(){
		
		//为对象集合赋值 minheight 并将不存在的对象移除
		for( var i = 0; i < option.fixElements.length;){
			option.fixElements[i] = easy_switch(option.fixElements[i]);
			if(!option.fixElements[i]){
				option.fixElements.splice(i,1);
			}
			else{
				//option.fixElements[i].minHeight = option.fixElements[i].offsetHeight;
				 i++;
			}
			
		}
		
		frameFix();
		window.onresize = function(){			
			frameFix();			
		}
		
		
	}
	
	//主函数
	function frameFix(){
		var windowHeight = document.documentElement.clientHeight;
		var frameHeight = (windowHeight - option.window_minHeight) > 0? (windowHeight - option.window_minHeight):0;
		
		for( var i = 0; i < option.fixElements.length; i++ ){
			
			option.fixElements[i].style.height = frameHeight + "px";
			
		}
	}
	
	return{
		//属性设置
		setOption:function(op){
			op.window_minHeight?option.window_minHeight = op.window_minHeight:"";
			op.fixElements?option.fixElements = op.fixElements:"";
			
			return this;
		},
		//初始化
		init:function(){
			if(_op){ this.setOption(_op);}
			areaReset();
		}
	}
};