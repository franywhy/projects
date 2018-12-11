/**
 * 通用的工具扩展 
 */
$.module(function(){
	//绑定数字控件
	$.fn.bindNumeric = function(){
		$(this).keyup(function(){	
			if(!$.isNumeric(this.value)){
				this.value = $(this).attr('old') || '';
			} else {
				$(this).attr('old',this.value);
			}
		});	
	}

});

/*
* Izb.enumList
*/
$.module("Izb.enumList",function(){
	return {
		menuType : {
			'none':0,
			'list':1,
			'all':2,
            'audit':3
		}
	};
});


/*
* Izb.resultMsg   接口返回code信息
*/
$.module("Izb.resultMsg", function () {
	return {
		30442:"ID重复"
	};
});


/*
* Izb.common
*/
$.module("Izb.common", function (exprots) {


	//通用处理错误
	Izb.core.setErrorHandler({
		0: function (error) {
			try {
				if ('service' == error.errorThrown) {
					Izb.ui.alert(JSON.stringify(error.msg), '服务异常-' + error.code, 'error');
				} else {
					Izb.ui.alert("网络异常,请稍候访问!", '网络异常', 'error');
				}
			} catch (ex) { }
		},
		30001: {
			msg: "xxx",
			handler: function (error) {
				alert("");
			}
		},
		30405: function () {
			Izb.user.login();
		}
	});

	//处理数据

	function _resolveResultOpt(opt, type) {
		if (!opt.url) {
			//测试数据
			if (Izb.config.isTest || opt.isTest) {
				if (type == "get") {
					opt.url = "/data/" + opt.action.replace('.', '/') + ".json?v=" + new Date().getTime();
				} else {
					opt.url = "/data/default.json";
				}
			} else {
				opt.url = Izb.core.resolveAction({
					path: opt.action + ".json",
					domain: Izb.config.domain.data
				});
			}			
		}
		return opt;
	}
	
	return {
		/*
	   * 获取数据
	   */
		getResult: function (opt) {
			Izb.core.getResult(_resolveResultOpt(opt, 'get'));
		},
		/**
         * 修改数据
         */
		setResult: function (opt) {
			Izb.core.setResult(_resolveResultOpt(opt, 'set'));
		},
		//人民币换算
		getMoney:function(xb){
			return (xb/100).toFixed(2);
		},
		getRMB:function(rmb){
			return (rmb||0).toFixed(2)
		},		
		/**
		 * 将星币转换为等级
		 * @param  {Number}  coin   星币数
		 * @param  {Boolean} isRich 是否是转换富豪等级
		 * @return {Object}         返回对应的等级数据
		 */		
		getLevel : function (coin,isRich) {
			coin = coin || 0;

			var flag = true,
				num = 0,
				extraNum = 0,
				currNum = 0,
				i = 0,
				base = isRich ? 100 : 1000;

			while (flag) {
				extraNum = coin - num;

				if (isRich) {
					if (i < 21) {
						currNum = ((i+1)*(i+1)*(i+1)+2*i)*base;
					} else if (i < 25) {
						switch (i) {
							case 21 :
								currNum = 6421900;
								break;
							case 22 :
								currNum = 14000000;
								break;
							case 23 :
								currNum = 26000000;
								break;
							case 24 :
								currNum = 52600000;
								break;
						}
					} else {
						extraNum = 0;
						currNum = 0;
						flag = false;
						break;
					}
				} else {
					if (i < 55) {
						currNum = ((i+1)*(i+1) + i)*base;
					} else {
						extraNum = 0;
						currNum = 0;
						flag = false;
						break;	
					}
				}

				// currNum = (isRich ? ((i+1)*(i+1)*(i+1)+2*i) : ((i+1)*(i+1) + i))*base;

				num += currNum;

				if (coin < num || currNum === 0) {
					flag = false;
					break;
				}

				i++;
			}

			return $.extend({
				level : i,
				extraCoin : extraNum,
				currLvCoin : currNum,
				percent : currNum !== 0 ? ((extraNum/currNum)*100).toFixed(1)+'%' : '100%'
			}, (isRich ? this.getRichIcon(0, i) : this.getAnchorIcon(0, i)));
		},
		getLevel2Coin : function (level, isRich) {
			level = (level-1);
			var coin = 0,
				base = isRich ? 100 : 1000;

			while (level >= 0) {
				coin += (isRich ? ((level+1)*(level+1)*(level+1)+2*level) : ((level+1)*(level+1) + level))*base;

				level--;
			}
			return coin;
		},
		getRichIcon : function (coin, level) {
			var currLV = $.isNumeric(level) ? level : this.getLevel(coin, true).level;

			currLV = currLV > 25 ? 25 : currLV;

			return {
				icon : '@appDomain@/styles/images/level/rich/'+currLV+'.gif',
				title : currLV + '级富豪'
			}
		},
		getAnchorIcon : function (bean, level) {
			var currLV = $.isNumeric(level) ? level : this.getLevel(bean).level;
			currLV = currLV > 55 ? 55 : currLV;
			return {
			    icon: '@appDomain@/styles/images/level/anchor/' + currLV + (currLV<16?'.png':'.gif'),
				title : currLV + '级主播'
			}
		},
		getAuthCode: function () {
			return Izb.core.resolveAction({
				path: '/authcode.json?v=' + new Date().getTime(),
				domain: Izb.config.domain.data
			});
		},
		refreshAuthCode:function(target,inputId){
			target.src = this.getAuthCode();
			$("#"+inputId).focus();
		}
	};
	
});