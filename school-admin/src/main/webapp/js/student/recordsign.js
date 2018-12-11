$(function () {
    $("#jqGrid").jqGrid({
        url: '../record/recordSign/list',
        datatype: "json",
        contentType: "application/x-www-form-urlencoded; charset=utf-8", 
        colModel: [		
        	
					{ label: 'id', name: 'registrationId', key: true, hidden: true },
					{ label: '学员ID', name: 'userId', width: 80 }, 						
					{ label: '姓名', name: 'name', width: 80 }, 						
					{ label: '报读班型', name: 'courseName', width: 80,formatter : function(value, options, row){
						if(value == null){
							return '--';
						}else  {
							return value;
						}
					} }, 					
					{ label: '报读时间', name: 'regDate', width: 80,formatter : function(value, options, row){
						if(value == null){
							return '--';
						}else  {
							return value;
						}
					} },						
					{ label: '报读院校', name: 'bdyx', width: 80 ,formatter : function(value, options, row){
						if(value == null||value=='~'){
							return '--';
						}else  {
							return value;
						}
					}}, 						
					{ label: '报读专业', name: 'zy', width: 80,formatter : function(value, options, row){
						if(value == null){
							return '--';
						}else  {
							return value;
						}
					}
					}, 						
					{ label: '报读班级', name: 'className', width: 80, formatter : function(value, options, row){
						if(value == null||value=='~'){
							return '--';
						}else  {
							return value;
						}
					}}, 	
					{ label: '报读层次', name: 'level', width: 80, formatter : function(value, options, row){
						if(value == null){
							return '--';
						}else  {
							return value;
						}
					}},	
					{ label: '学习状态', name: 'status', align : "center",width: 80 ,formatter : function(value, options, row){
							if(value == 1){
								return '<span class="label label-success">在读</span>';
							}else if(value == 2){
								return '<span class="label label-success">休学</span>';
							}else if(value == 3){
								return '<span style="background-color: #C0C0C0">退学</span>';
							}
						}
					},
					  
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {
			userId:'',
			name:'',
			className:'',
 			sysUserName:'',
 			sysUserId:'',
		}, 

	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},  
		exportData: function(){
			hq.ui.exportData(null);
		},
		queryClear: function(){
			vm.q.userId = ''; 
			vm.q.name = ''; 
			vm.q.className = '';
			vm.q.classId = '';
		},  		
		classLayerShow : function(){//班级浮层
			classLay.show(function(index,opt){
				vm.q.classId = opt.classId;
				vm.q.className = opt.className;
			});
		}, 
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			var q = vm.q; 
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page,
                postData: { 
                'name':q.name,
                'classId':q.classId,
                'sysUserId': q.sysUserId
                }
            }).trigger("reloadGrid");
		}
		
	}
});