/*
* Izb.user
*/
$.module("Izb.user",function(){
	var _userInfo = null,
		_isInit = false,
		_loginCallback = [];
	
	return {
		init:function(){
			if(_isInit){
				return;
			}
			var that = this;
			Izb.common.getResult({
				action:'/session',
				success:function(result){
					_userInfo = result.data;					
					$('#hi').html('您好，<a href="javascript:Izb.controller.admin.modif_pwd();">' + _userInfo.nick_name + '</a> - <a href="http://ttwww.app@appid@.twsapp.com" target="_blank">主页</a> | <a href="javascript:Izb.main.gotoHome();">首页</a> - <a href="javascript:Izb.user.logout();">退出</a>');
					_isInit = true;
					for(var fun in _loginCallback){
						_loginCallback[fun]();
					}
				},
				error:function(){				
					that.login();
				}			
			});			
		},
		ready:function(callback){
			if($.isFunction(callback)){
				if(_isInit){
					callback();
				}else{
					_loginCallback.push(callback);
				}
			}
		},
		login:function(){
			top.location.href = 'index.html';
		},
		logout:function(){
			var that = this;
			Izb.ui.dialog({
				lock : true,
				icon : 'question',
				content : '<p>' + _userInfo.nick_name + ', 确定退出吗？</p>',
				ok : function () {
					Izb.common.getResult({
						action:'/logout',
						success:function(result){
							Izb.user.login();
						},
						error:function(){				
							Izb.user.login();
						}		
					});	
				},
				cancelVal : '取消',
				cancel : true
			});
		},
		getUserInfo:function(){
			return _userInfo;
		},
		//type=0|无权限 type=1|列表 type=2|全部
		checkLimits:function(key,type){
            if(!key) {
                return true;
            }
			var flag = false;
			if(key.indexOf('/')!=-1 && type==Izb.enumList.menuType.all){
				type=Izb.enumList.menuType.list;
			}

			if(type==undefined) {
				return _userInfo.menus[key] || Izb.enumList.menuType.none;
			} else {
				return _userInfo.menus[key] >= type;
			}			
		},
        //audit  key当前按钮权限名称， audit审核标识3
        audit:function(key,audit){
            if(audit==undefined) {
              return false;
            } else {
                return _userInfo.menus[key] == audit;
            }
        }/*,
        // key当前按钮权限名称， audit审核标识2
        audit:function(key,all){
            if(all==undefined) {
                return false;
            } else {
                return _userInfo.menus[key] == all;
            }
        }*/

	};
	
});