$(function () {
    $("#jqGrid").jqGrid({
        url: '../sysschool/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', width: 50, hidden:true, key: true },
			{ label: '校区名称', name: 'name', width: 80 }, 			
//			{ label: '图片', name: 'pic', width: 80 },
			{ label: '图片', name: 'pic', width: 80 , align : "center" ,formatter : function(value, options, row){return '<img height="50px" width="50px"  src="'+value+'"/>';}},
			{ label: '电话号码', name: 'telephone', width: 80 }, 			
			{ label: '详细地址', name: 'address', width: 80 }, 			
			{ label: '经度', name: 'longitude', width: 80 }, 			
			{ label: '纬度', name: 'latitude', width: 80 }, 			
			{ label: '校区编号', name: 'code', width: 80 }, 			
			{ label: '校区nc_id', name: 'ncId', width: 80 }, 			
			{ label: '业务线id', name: 'businessId', width: 80 }			
        ],
		viewrecords: true,
        height: 500,
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
		q:{
			schoolName:null
		},
		showList: true,
		title: null,
		sysSchool: {
			name:"",
			pic:"",
            longitude:"",
            latitude:""
		}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysSchool = {
					name:"",
					pic:"",
                    longitude:"",
                    latitude:""
			};
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
			var url = vm.sysSchool.id == null ? "../sysschool/save" : "../sysschool/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.sysSchool),
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
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sysschool/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
            $.ajax({
				type: "POST",
			    url: "/sysschool/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.sysSchool = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {
                	"schoolName" : vm.q.schoolName
                },
				page:page
            }).trigger("reloadGrid");
		}
	}
});