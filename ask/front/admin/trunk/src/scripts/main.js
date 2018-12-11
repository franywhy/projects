/*
* Izb.main,启动入口
*/
$.module("Izb.main",function(){
	var _allController,//所有的导航项
        _allControllerJs,
		_lastMenuKey,//最后一个操作的导航Key
		_curController,//当前的Controller
		_allMenus = [],
		_sysMsgTimer = null,
		$main,
		$aside,
		$appHeader,
		$appContent;
		
	//初始化页面
	function _initPage(){
		$main = $("#main");
		$aside = $("#aside");
		$appHeader = $("#appHeader");
		$appContent = $("#appContent");
		_initMenu();
		Izb.main.gotoHome();
	}
	
	//初始化模块
	function _initModule() {
		Izb.core.setOptions({ dataFormat: false });

		//初始化ui配置
		Izb.ui.init();
		//Izb.controller.sys.init();
//		Izb.controller.giftcat.getAllCatList();
	}
	
	//初始化Controller
	function _initController(){

        //TODO 加载的js
        _allControllerJs = [
            "/scripts/tpl/ncteacher.js",
//            "/scripts/tpl/users.js",
            "/scripts/tpl/area.js",
            "/scripts/tpl/commoditys.js",
            "/scripts/tpl/classplan.js",
            "/scripts/tpl/banner.js",
            "/scripts/tpl/coursebanner.js",
            "/scripts/tpl/recommend.js",
            "/scripts/tpl/schoolpic.js",
            "/scripts/tpl/schoolactivity.js",
            "/scripts/tpl/discount.js",
            "/scripts/tpl/signs.js",
            "/scripts/tpl/attendances.js",
            "/scripts/tpl/orders.js",
            "/scripts/tpl/scoredetail.js",
            "/scripts/tpl/liveclass.js",
            "/scripts/tpl/timer.js",
            "/scripts/tpl/demo.js",
            "/scripts/tpl/mysql.js",
            "/scripts/tpl/courselist.js"
//            "/scripts/tpl/admin.js"
        ];

        $.each(_allControllerJs , function(index,cjs){
            $.ajax({
                url:cjs,
                cache:false,
                async:false,
                dataType:"script",
                success:function(result){
                    console.log("finished loading " +  cjs);
                }
            });
        });


        //TODO 目录
		_allController = [
            //*******************************************************************

            {'name': '定时器模块'},
            Izb.controller.timer,//定时器
            Izb.controller.initdata,//初始化数据
            {'name': 'NC基础档案'},
            Izb.controller.area,//校区
            Izb.controller.ncteacher,//NC教师
            Izb.controller.commoditys,//课程
            Izb.controller.courselist,//课程列表
            Izb.controller.classplan,//排课计划
            Izb.controller.advertising,//冷启动广告页
            Izb.controller.banner,//首页banner管理
            Izb.controller.coursebanner,//课程列表banner管理
            Izb.controller.recommend,//每日推送
            Izb.controller.applive,//App直播管理
            Izb.controller.articles,//文章管理
            Izb.controller.tkarticletype,//题库文章类型管理
            Izb.controller.provinceexam,//考试地址管理
            Izb.controller.schoolpic,//校区图片管理
            Izb.controller.schoolactivity,//校区公告管理
            Izb.controller.discount,//优惠券管理
            Izb.controller.signs,//报名表
            Izb.controller.attendances,//考勤表
            Izb.controller.orders,//订单列表
            Izb.controller.scoredetail,//积分明细
            Izb.controller.liveclass,//考前冲刺
            Izb.controller.middlelivedetail,//中级直播排课出勤档案
            Izb.controller.middlelivesumreport,//中级直播考勤汇总表
            {'name': '档案管理'},
            Izb.controller.annalsdetail,//恒企志文章档案
            Izb.controller.annals,//恒企志专题档案
            Izb.controller.bossrecord,//总裁档案
            Izb.controller.bossana,//总裁语录
            Izb.controller.servicehome,//上门服务
            Izb.controller.students,
            Izb.controller.topics,
            Izb.controller.teacher,
            Izb.controller.teachertopicdata,
            Izb.controller.studenttopicdata,
            Izb.controller.apply,
            Izb.controller.payment,
            Izb.controller.maintype,
            Izb.controller.user,
            Izb.controller.vipcard,  //vip会员
            Izb.controller.askdata,  //报表
            Izb.controller.bonusdata,  //奖金报表
            Izb.controller.currency,	//充值明细报表
            Izb.controller.redpacket,  //提现奖金报表
            Izb.controller.campusdata,  //校区数据表
            Izb.controller.teacherapply,  //教师申请单查询
            Izb.controller.schooluserreport,//校区报表
            Izb.controller.schoolfeedbackreport,//校区反馈报表
            Izb.controller.areauserreport,//大区校区报表
            Izb.controller.provinceuserreport,//省份校区报表


            {'name': '供应商结算'},
            Izb.controller.statement,  //供应商结算单
            //*******************************************************************

            //*******************************************************************
            {'name': '系统设置'},
            Izb.controller.admin,
            Izb.controller.oplog,
            {'name': '退出系统', fn : "Izb.user.logout()"},

			//1级菜单
			{'name' : '网站管理'},
			//2级功能菜单
			Izb.controller.notice,
			Izb.controller.mission,
			Izb.controller.sys,
			Izb.controller.client,
			Izb.controller.message,
			Izb.controller.feedback,
            //*******************************************************************
			{ 'name': '用户管理' },

			Izb.controller.star,
			Izb.controller.sign,
			//特殊权限配置
			{ 'name': '签约资料', menu: 'apply/show', visible: false },

            //*******************************************************************
            {'name': '奖金池'},
            Izb.controller.bonussetting,//奖金池模板档案
            Izb.controller.bonuspools,//奖金池生成
            //*******************************************************************
            {'name': '学分管理'},
            Izb.controller.creditstandard,//学分标准(credit used)
            Izb.controller.creditrecord,//学分记录表
            Izb.controller.creditrecordteacher,//学分记录-老师
            Izb.controller.creditlearningtip,//学习建议  
            Izb.controller.creditoperation,//学分运算  
            Izb.controller.creditpermission,//学分权限 
            Izb.controller.creditsubjectmon,//月度学分统计表
            Izb.controller.creditclassmon,//班型学分统计表 
            Izb.controller.creditteachermon,//老师学分报表
            Izb.controller.creditcompletion,//学分结业单
            Izb.controller.creditpercent, //学分完成率表
            Izb.controller.creditpaike //排课完成率表
            //*******************************************************************


		];


		$.each(_allController,function(index,controller){
			if(!$.isEmptyObject(controller.pty)){
				controller.pty.menu = controller.pty.menu || controller.pty.key;
				_allMenus.push({
					menu:controller.pty.menu,
				    name: controller.pty.name,
				    parentPower: controller.pty.parentPower
				});//后面做权限的时候用   选择子权限的时候  必选父权限
			} else if (controller.menu){
				_allMenus.push({
					menu:controller.menu,
			        name: controller.name,
			        parentPower: controller.parentPower
			    });
			} else {
				_allMenus.push({
					group: controller.name
				});
			}
		});
	}
	
	//初始化菜单
	function _initMenu(){
		var strMenu = '';
		$.each(_allController,function(index,item){
			if(!$.isEmptyObject(item.pty)){
				var menu = item.pty.menu;
				if(Izb.user.checkLimits(menu,Izb.enumList.menuType.list)){
					strMenu+='<dd data-menu="'+item.pty.menu+'"><a href="#a='+item.pty.key+'">'+item.pty.name+'</a></dd>';
				}
			} else if(item.url){
				//自定义地址
				if(Izb.user.checkLimits(item.menu,Izb.enumList.menuType.list)){
					strMenu+='<dd data-menu="'+item.menu+'"><a href="'+item.url+'">'+item.name+'</a></dd>';
				}
			} else if(item.fn){
				//自定义函数
				if(Izb.user.checkLimits(item.menu,Izb.enumList.menuType.list)){
					strMenu+='<dd><a href="javascript:'+item.fn+'">'+item.name+'</a></dd>';
				}
			} else {
				if(item.visible===false){
					return true;
				}
				if(index!=0){
					strMenu+='</dl>';
				}
				strMenu+='<dl class="export"><dt>'+item.name+'</dt>';
			}
		});
		strMenu+='</dl>';
		$aside.html(strMenu);
		
		//清理菜单
		$('dl',$aside).each(function(index, element) {
            if($(this).find('dd').length==0){
				$(this).remove();
			}
        });
		
		$('dd',$aside).click(function(event) {
               event.stopPropagation();
        });
		
		$('dl',$aside).click(function(){
			$(this).toggleClass('export');
		});
		
		//$('dl',$aside).first().removeClass('export');
		$('dl',$aside).last().removeClass('export');
		
	}
	
	//获取默认hash参数
	function _getHashParams(){
		// 请求参数采集
		var params = $.parseParam(location.hash);
		if(params.a){
			// 设置默认页面
			params.a = params.a.toLowerCase();
		}
		params.tab = params.tab || params.a;
		return params;
	
	}
	
	// 动态页面请求处理
	function _onAction(hash) {
		// 请求参数采集
		var params = _getHashParams();
		if(!params.a){
			return;
		}				
		var	curController = Izb.controller[params.a];
		if(curController && $.isFunction(curController.index)){
			_curController = curController;
			curController.index(params);
			document.title = curController.pty.name +' - '+ Izb.config.name;
			var $activedd = $('dd[data-menu="'+_curController.pty.menu+'"]',$aside);
			//自定义menu
			if(params.menu){
				var $paramsActivedd = $('dd[data-menu="'+params.menu+'"]',$aside);
				$activedd = $paramsActivedd.length > 0 ? $paramsActivedd : $activedd;
			}
			if($activedd.length>0){
				$('dd',$aside).removeClass('active');
				$activedd.addClass('active');
				$activedd.parents('dl').removeClass('export');
			}
		}
	}

	//初始化表单配置
	function _initFormConfig() {
		$.validator.setDefaults({
			onsubmit: false
			/*,
			errorPlacement: function (label, element) {
				var $formItem = element.closest(".form-item"),
                    $formTip = $formItem.find(".form-tip");
				if ($formTip.length == 0) {
					$formTip = '<div class="form-tip"></div>';
					$formItem.append($formTip);
				}
				$formTip.append(label);
			},
			highlight: function (element, errorClass) {
				$(element).closest(".form-item").removeClass("form-item-success").addClass("form-item-error");
			},
			unhighlight: function (element, errorClass) {
				$(element).closest(".form-item").removeClass("form-item-error").addClass("form-item-success");
			}*/
		});

		$.validator.addMethod("phone", function (value, element) {
			return this.optional(element) || /^(13|15|18)[0-9]{9}$/.test(value);
		}, "<span style='color: red'>请输入合法的手机号码</span>");

		$.validator.addMethod("qq", function (value, element) {
			return this.optional(element) || /^[1-9]*[1-9][0-9]*$/.test(value);
		}, "<span style='color: red'>请输入合法的QQ号码</span>");

		$.validator.addMethod("tel", function (value, element) {
			return this.optional(element) || /^(([0\\+]\\d{2,3}-)?(\\d{2,4})-)?(\\d{7,8})(-(\\d{3,}))?$/.test(value);
		}, "<span style='color: red'>请输入合法的号码</span>");

		$.validator.addMethod("nickname", function (value, element) {
			return this.optional(element) || /^[\u4E00-\u9FA5A-Za-z0-9_]+$/.test(value);
		}, "<span style='color: red'>请输入合法的昵称</span>");

        $.validator.addMethod("nickname", function (value, element) {
            return this.optional(element) || /^[\u4E00-\u9FA5A-Za-z0-9_]+$/.test(value);
        }, "<span style='color: red'>请输入合法的昵称</span>");
        $.validator.addMethod("idcard", function (value, element) {
            return this.optional(element) || /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        }, "<span style='color: red'>请输入合法的身份证号</span>");
		/*
		data-rule-required="true"
		data-msg-email="Please enter a valid email address"
		minlength="2"
		*/

		$.extend($.validator.messages, {
			required: "<span style='color: red'>不能为空</span>",
			remote: "<span style='color: red'>请修正该字段</span>",
			email: "<span style='color: red'>请输入正确格式的电子邮件</span>",
			url: "<span style='color: red'>请输入合法的网址</span>>",
			date: "<span style='color: red'>请输入合法的日期</span>",
			dateISO: "<span style='color: red'>请输入合法的日期 (ISO)</span>",
			number: "<span style='color: red'>请输入合法的数字</span>",
			digits: "<span style='color: red'>只能输入整数</span>",
			creditcard: "<span style='color: red'>请输入合法的信用卡号</span>",
			equalTo: "<span style='color: red'>请再次输入相同的值</span>",
			accept: "<span style='color: red'>请输入拥有合法后缀名的字符串</span>",
			maxlength: $.validator.format("最多{0}个的字符"),
			minlength: $.validator.format("最少{0}个的字符"),
			rangelength: $.validator.format("请输入介于{0}和{1}之间的字符"),
			range: $.validator.format("请输入一个介于{0}和{1}之间的值"),
			max: $.validator.format("请输入一个最大为 {0} 的值"),
			min: $.validator.format("请输入一个最小为 {0} 的值")
		});

	}
	
	return {
		init:function(){
			_initModule();
			_initController();
			_initFormConfig();
			_initPage();			
			$.history.registerAction(_onAction);
			$(function(){
				$.history.initActionManager();
			});
		},
		getHashParams:_getHashParams,
		/*
		* 跳转到首页
		*/
		gotoHome:function(text){			
			Izb.ui.closeTips();
			$appHeader.empty();
			//if (Izb.user.checkLimits("admin", Izb.enumList.menuType.all)) {
				$appContent.html('<div class="log-info"><h1>' + (text || '欢迎使用' + Izb.config.name + '！') +'</h1>'+ Izb.ui.template("tpl-log")+'</div>');
			//} else {
			//	$appContent.html('<p class="welcome">' + (text || '欢迎使用' + Izb.config.name + '！') + '</p>');
			//}
		},
		/*
		* 分页跳转列表页
		*/
		gotoList:function(page,size){
			if(!$.isEmptyObject(_curController) && $.isFunction(_curController.list)){
				_curController.list(page || 1,size);	
			}
		},
		/*
		* 刷新当前面板
		*/
		refresh:function(){
			Izb.ui.closeDialog('inputDialog');
			if(!$.isEmptyObject(_curController) && $.isFunction(_curController.list)){
				_curController.list(_curController.page || 1);
			}
		},
		setSysMsg:function(msg){
			$("#sysMsg").html('<marquee onmouseover="this.stop();" onmouseout="this.start();">'+msg+'</marquee>');
			clearTimeout(_sysMsgTimer);
			_sysMsgTimer = setTimeout(function(){
				$("#sysMsg").empty();
			},30*1000);
		},		
		getAllMenus:function(){
			return _allMenus;
		}
	};
});