/**
 * 推送弹窗js
 */
var pushRulesLay = {
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "推送规则",
	layerHeigh : '600px',
	layerWeigh : '600px',
	show : function(message_type, message_id, row_pushTime, scallback){
        /*消息类型 3：公开课 4：自定义卡片 9：系统消息 10：会计头条-发现 11：公开课-发现*/
        $("#recommend-div").css('display','none');
        if(message_type == 3 || message_type == 10) {
            $("#recommend-div").css('display','block');
        }
        /*消息类型 只有系统消息才可以显示员工端按钮*/
        $("#staff-label").css('display','none');
        if(message_type == 9 ) {
            $("#staff-label").css('display','block');
        }
        $("#cityInput").val("");
        $("#labelInput").val("");
        $("#goodsInfoInput").val("");
        $("#classplanInput").val("");
        $("#classInput").val("");
        $("#classplanLivesInput").val("");
        pushVm.pushRules.cityIds = [];
        pushVm.pushRules.labelIds = [];
        pushVm.pushRules.goodsInfoIds = [];
        pushVm.pushRules.classplanIds = [];
        pushVm.pushRules.classIds = [];
        pushVm.pushRules.classplanLivesIds = [];

		var that = this;
		that.scallback = scallback;
		that.layerIndex = layer.open({
			type : 1,//
            area : 'auto',
            area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#pushRulesLayDiv"),
			scrollbar : true,//是否允许浏览器出现滚动条
			fixed : false,//固定
			shadeClose : false,// 是否点击遮罩关闭
			resize : true,//是否允许拉伸
			maxmin: true, //开启最大化最小化按钮
			zIndex : 19891014,
			btn : ['确认','取消' ],
			btn1: function (index) {
			 that.select(message_type, message_id, row_pushTime);
			}
		});
        getCity();//加载地区树
	},
    select:function(message_type, message_id, row_pushTime){
	    var rulesArray = [];
	    rulesArray = pushVm.pushRules.cityIds.concat(pushVm.pushRules.labelIds, pushVm.pushRules.goodsInfoIds, pushVm.pushRules.classplanIds, pushVm.pushRules.classIds, pushVm.pushRules.classplanLivesIds,pushVm.pushRules.active,pushVm.pushRules.teacher);
	    if(rulesArray.length==0) {
            alert("至少选择一种推送规则！！！");
            return;
        }
        if($.isNull($("#pushTimeInput").val())) {
            alert("请选择推送时间！！！");
            return;
        }
        confirm('确定要推送选中的记录？', function(){
            $.ajax({
                type: "POST",
                url: "../pushrules/push",
                data: JSON.stringify({
                    "messageType":message_type,
                    "messageId":message_id,
                    "channelIds":rulesArray,
                    "pushTime":$("#pushTimeInput").val(),
                    "msgRecommend":pushVm.pushRules.msgRecommend,
                    "isVisitor":pushVm.pushRules.isVisitor,
                    "rules":pushVm.pushRules.rules
                }),
                success: function(r){
                    if(r.code == 0){
                        alert(r.msg, function(index){
                            $("#jqGrid").trigger("reloadGrid");
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        });
        //关闭浮层
        layer.close(this.layerIndex);
    }
}

//地区树
var city_ztree;
var city_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "cityId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            name:"cityName",
            url:"nourl"
        }
    },
    check:{
        enable:true
    }
};

var pushVm = new Vue({
    el:'#pushRulesLayDiv',
    data:{
        pushRules: {
            msgRecommend:0,
            isVisitor:false,
            rules:"",
            cityIds:[],
            cityName:"",
            labelIds:[],
            labelName:"",
            goodsInfoIds:[],
            goodsInfo:"",
            classplanIds:[],
            classplanName:"",
            classIds:[],
            className:"",
            classplanLivesIds:[],
            classplanLivesName:"",
            active:[],
            teacher:[]
        }
    }
});

//获取地区结构树
function getCity(){
    //加载地区树
    $.get("../layerdata/appCityList", function(r){
        city_ztree = $.fn.zTree.init($("#cityTree"), city_setting, r);
    })
}
//地区树弹窗
function cityTree(cityInput){
    layer.open({
        type: 1,
        offset: '50px',
        skin: 'layui-layer-molv',
        title: "选择地区",
        area: ['300px', '450px'],
        shade: 0,
        shadeClose: false,
        content: jQuery("#cityLayer"),
        btn: ['确定', '取消'],
        btn1: function (index) {
            pushVm.pushRules.cityIds = [];
            pushVm.pushRules.cityName = "";
            $("#cityInput").val("");
            var nodes = city_ztree.getCheckedNodes(true);
            nodes.forEach(function(value, index, array) {
                if(!value.isParent) {
                    pushVm.pushRules.cityIds.push("city"+value.cityCode);
                    pushVm.pushRules.cityName += value.cityName + "，";
                }
            });
            $("#cityInput").val(pushVm.pushRules.cityName);
            layer.close(index);
        }
    });
}
function appLabelLayerShow(){//用户标签浮层
    appLabelMsLay.show(function(selectIds , selectDetails){
        pushVm.pushRules.labelIds = [];
        pushVm.pushRules.labelName = "";
        selectDetails.forEach(function(value, index, array) {
            pushVm.pushRules.labelIds.push("label"+value.id);
            pushVm.pushRules.labelName += value.labelName + "，";
        });
        $("#labelInput").val(pushVm.pushRules.labelName);
    });
}
function goodsInfoLayerShow(){//商品浮层
    goodsInfoMsLay.show(function(selectIds , selectDetails){
        pushVm.pushRules.goodsInfoIds = [];
        pushVm.pushRules.goodsInfo = "";
        selectDetails.forEach(function(value, index, array) {
            pushVm.pushRules.goodsInfoIds.push("goodsInfo"+value.id);
            pushVm.pushRules.goodsInfo += value.name + "，";
        });
        $("#goodsInfoInput").val(pushVm.pushRules.goodsInfo);
    });
}
function classplanLayerShow(){//排课浮层
    classplanMsLay.show(function (selectIds , selectDetails) {
        pushVm.pushRules.classplanIds = [];
        pushVm.pushRules.classplanName = "";
        selectDetails.forEach(function(value, index, array) {
            pushVm.pushRules.classplanIds.push("classplan"+value.classplanId);
            pushVm.pushRules.classplanName += value.classplanName + "，";
        });
        $("#classplanInput").val(pushVm.pushRules.classplanName);
    });
}
function classLayerShow(){//班级浮层
    classMsLay.show(function(selectIds , selectDetails){
        pushVm.pushRules.classIds = [];
        pushVm.pushRules.className = "";
        selectDetails.forEach(function(value, index, array) {
            pushVm.pushRules.classIds.push("class"+value.classId);
            pushVm.pushRules.className += value.className + "，";
        });
        $("#classInput").val(pushVm.pushRules.className);
    });
}
function isRecommendClick(msgRecommend) {
    pushVm.pushRules.msgRecommend = msgRecommend;
}
function isVisitorClick(isVisitor) {
    pushVm.pushRules.isVisitor = isVisitor;
}
function pushRulesClick(rules) {
    $("#cityInput").val("");
    $("#labelInput").val("");
    $("#goodsInfoInput").val("");
    $("#classplanInput").val("");
    $("#classInput").val("");
    $("#classplanLivesInput").val("");
    pushVm.pushRules.cityIds = [];
    pushVm.pushRules.labelIds = [];
    pushVm.pushRules.goodsInfoIds = [];
    pushVm.pushRules.classplanIds = [];
    pushVm.pushRules.classIds = [];
    pushVm.pushRules.classplanLivesIds = [];
    pushVm.pushRules.active = [];
    pushVm.pushRules.teacher = [];

    $("#cityTree-div").css('display','none');
    $("#appLabelLayerShow-div").css('display','none');
    $("#goodsInfoLayerShow-div").css('display','none');
    $("#classplanLayerShow-div").css('display','none');
    $("#classLayerShow-div").css('display','none');
    if(rules == 'cityTree') {
        $("#cityTree-div").css('display','block');
    } else if(rules == 'appLabelLayerShow') {
        $("#appLabelLayerShow-div").css('display','block');
    } else if(rules == 'goodsInfoLayerShow') {
        $("#goodsInfoLayerShow-div").css('display','block');
    } else if(rules == 'classplanLayerShow') {
        $("#classplanLayerShow-div").css('display','block');
    } else if(rules == 'classLayerShow') {
        $("#classLayerShow-div").css('display','block');
    }else if (rules == 'active'){
        pushVm.pushRules.active.push("public");
    }else if (rules == 'teacher'){
        pushVm.pushRules.teacher.push("teacher");
    }


    pushVm.pushRules.rules = rules;
}
