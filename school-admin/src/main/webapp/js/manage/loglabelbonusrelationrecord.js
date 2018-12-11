$(function () {
    $("#jqGrid").jqGrid({
        url: '../manage/loglabelbonusrelationrecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '修改者', name: 'modifier', index: 'modifier', width: 80 },
			{ label: '更新时间', name: 'modifyTime', index: 'modify_time', width: 80 }, 			
			{ label: '标签分类名称', name: 'tipName', index: 'tip_name', width: 80 }, 			
			{ label: '满意金额', name: 'satisyMoney', index: 'satisy_money', width: 80 }, 			
			{ label: '非常满意金额', name: 'verySatisyMoney', index: 'very_satisy_money', width: 80 }, 			
			{ label: '修改原因', name: 'modifyReason', index: 'modify_reason', width: 80 }, 			
			{ label: '备注', name: 'remark', index: 'remark', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		logLabelBonusRelationRecord: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.logLabelBonusRelationRecord = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.logLabelBonusRelationRecord.id == null ? "generator/loglabelbonusrelationrecord/save" : "generator/loglabelbonusrelationrecord/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.logLabelBonusRelationRecord),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		getInfo: function(id){
			$.get(baseURL + "generator/loglabelbonusrelationrecord/info/"+id, function(r){
                vm.logLabelBonusRelationRecord = r.logLabelBonusRelationRecord;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
		}
	}
});