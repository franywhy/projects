$(function () {
    $("#jqGrid").jqGrid({
        url: '../manage/tipBonusRelation/list',
        datatype: "json",
        colModel: [			
			{ label: '序号(标签id)', name: 'tid', index: 'tid', width: 50, key: true ,hidden:true},
			{ label: '创建时间', name: 'createTime', index: 'createTime', width: 80 ,hidden:true},
			{ label: '标签分类', name: 'tidName', index: 'tidName', width: 80 },
            { label: '标签id', name: 'tid', index: 'tid', width: 80 ,hidden:true},
			{ label: '满意红包金额', name: 'bonus', index: 'bonus', width: 80, formatter: function(v, options, row){
                var text = row.min+'   至   '+row.max;
                return text;
            }},
			{ label: '很满意红包金额', name: 'veryBonus', index: 'veryBonus', width: 80 , formatter: function(v, options, row){
                var text1 = row.veryMin+'   至   '+row.veryMax;
                return text1;
            }}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10],
        rownumbers: true,
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    /*--------------------------------------------初始化记录表------------------------------------------*/
    $("#jqGridDetail").jqGrid({
        url: '../manage/loglabelbonusrelationrecord/listPOJO',
        datatype: "local",
        colModel : [
            { label : "id", name : 'id', editable : false,width : 20, key : true ,hidden:true},
            { label : "修改时间", name : 'modifyTime', width : 80 },
            { label : "修改人ID", name : 'modifier', width : 80 ,hidden:true},
            { label : "修改人", name : 'nickName', width : 80 },
            { label : "标签分类", name : 'tipName', width : 80 },
            { label : "满意金额", name : 'satisyMoney', width : 55 },
            { label : "很满意金额", name : 'verySatisyMoney', width : 55 },
            { label : "修改原因", name : 'modifyReason', width : 130 }
        ],
        height : 'auto',
        pager : "#jqGridDetailPager",
        rowNum:1000, //每页显示记录数
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page",
            rows:"limit",
            order: "order"
        },
        gridComplete:function(){
            //隐藏grid底部滚动条
            $("#jqGridDetail").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
            labelName:""
		},
		a:{
            minMoney:"",
            maxMoney:"",
            veryMinMoney:"",
            veryMaxMoney:"",
            laberId:"",
            laberNames:"",
            modifyReason:""
		},
		showList: true,
		title: null


	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			document.getElementById("id.labelNames").disabled = false;
			//隐藏
            document.getElementById("div.modifyReasono").style.display = "none";
            document.getElementById("table.record").style.display = "none";

			vm.title = "新增";
            vm.a.modifyReason="新增"
            vm.q.labelName = "";
            vm.tipBonusRelation = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			document.getElementById("id.labelNames").disabled = true;
            //显示
            document.getElementById("div.modifyReasono").style.display = "block";
            document.getElementById("table.record").style.display = "block";

			vm.showList = false;
            vm.title = "修改";
            vm.showAddOrUpdate = true;
            vm.q.labelName = "";
            vm.getInfo(id);
            vm.queryInfo(null,1);

		},
		saveOrUpdate: function (event) {
			var url = vm.a.id == null ? "../manage/tipBonusRelation/save" : "../manage/tipBonusRelation/update";
			if(!vm.a.laberNames){
                alert('请选择标签');
                return;
            }
			if(!vm.a.laberId){
                alert('请选择标签');
                return;
            }
            if(!vm.a.minMoney || !vm.a.maxMoney || !vm.a.veryMinMoney || !vm.a.veryMaxMoney){
                alert('红包金额不能为空');
                return;
            }
            if(vm.a.minMoney<1 || vm.a.maxMoney<1 || vm.a.veryMinMoney<1 || vm.a.veryMaxMoney<1){
                alert('红包金额不能小于1');
                return;
            }

            if(vm.a.maxMoney<=vm.a.minMoney){
                alert('满意红包最大金额不能等于小于最小金额');
                return;
            }
            if(vm.a.veryMaxMoney<=vm.a.veryMinMoney){
                alert('很满意红包最大金额不能等于小于最小金额');
                return;
            }

            if(!vm.a.modifyReason){
                alert("修改原因不能为空");
                return;
            }
            confirm('确认后、金额立即生效', function () {
                $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.a),
                    success: function (r) {
                        if (r.code === 0) {
                            alert(r.data || '操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
		},
		getInfo: function(id){
			$.get("../manage/tipBonusRelation/info/"+id, function(r){
                // vm.a = r.tipBonusRelation;
                vm.a.id=id;
                vm.a.minMoney=r.tipBonusRelation.min;
                vm.a.maxMoney=r.tipBonusRelation.max;
                vm.a.veryMinMoney=r.tipBonusRelation.veryMin;
                vm.a.veryMaxMoney=r.tipBonusRelation.veryMax;
                vm.a.laberNames=r.tipBonusRelation.tidName;
                vm.a.laberId=r.tipBonusRelation.tid;
                // vm.a.sysUserName = r.sysUserLaber.nickName;
            });
		},
		reload: function (event,p) {
			var vm = this;
			vm.showList = true;
			vm.showAddOrUpdate = false;
			vm.flag=true;
			vm.flagDvalidity=true;
			vm.showOrderList=true;
			vm.showOrderAttempt=true;
			vm.a = {
                minMoney:"",
                maxMoney:"",
                veryMinMoney:"",
                veryMaxMoney:"",
                laberId:"",
                laberNames:""
			};
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData : {
                    "labelName" : vm.q.labelName
				},
                datatype: "json",
                page:page
            }).trigger("reloadGrid");
		},
        queryInfo: function (event,p) {
            var id = getSelectedRow();
            var page = p || $("#jqGridDetail").jqGrid('getGridParam','page');
            $("#jqGridDetail").jqGrid('setGridParam',{
                postData : {
                    "labelId" : id
                },
                datatype: "json",
                page:page
            }).trigger("reloadGrid");
        },
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q.labelName = "";
		},
		//标签弹窗
		topicsFirstLaberLayerShowQuery : function(){
			topicsFirstLaberLay.show(function(opt,laberIdList,laberNameList){
				//alert(laberIdList+"=========="+laberNameList)
				if(laberIdList.length>1){
                    alert("只能选择一个标签");
                    return;
				}
                for(var i= 0;i<laberIdList.length;i++){
                    if(laberIdList[i]<0){
                        alert("只能选择三级标签！");
                        return;
                    }
                }
				vm.a.laberId = laberIdList[0];
				vm.a.laberNames = laberNameList[0];
			});
			
		}
	}
});