$(function () {
    $("#jqGrid").jqGrid({
        url: '../classgoodsdepts/list',
        datatype: "json",
        colModel: [		
					{ label: 'id', name: 'id', key: true , hidden:true },
					{ label: '班级名称', name: 'className', width: 80 },
					{ label: '班主任', name: 'teacherName', width: 80 },
					{ label: '班级id', name: 'classId', width: 80 },
					{ label: '商品名称', name: 'goodName', width: 80 },
					{ label: '商品id', name: 'goodId', width: 80 }, 						
					{ label: '校区名称', name: 'deptName', width: 80 },
					{ label: '校区（部门）id', name: 'deptId', width: 80 }, 						
					{ label: '默认班级', name: 'defaultClass', width: 80 ,formatter:function(value, options, row){
						return row.defaultClass === 0 ? 
								'<span class="label label-danger">正常</span>' : 
								'<span class="label label-success">默认班级</span>';
					}}, 
					/*{ label: '创建者', name: 'creator', width: 80 }, 						
					{ label: '创建时间', name: 'creationTime', width: 80 }, 						
					{ label: '修改者', name: 'modifier', width: 80 }, 						
					{ label: '修改时间', name: 'modifyTime', width: 80 }*/						
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
		q:{
			className:"",
			teacherName:"",
			goodName:"",
			deptName:""
		},
		showList: true,
		title: null,
		classGoodsDepts: {
			classId:null,
			className:null,
			goodId:null,
			goodName:null,
			deptId:null,
			deptName:null,
		}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.classGoodsDepts = {
					classId:null,
					className:null,
					goodId:null,
					goodName:null,
					deptId:null,
					deptName:null,
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
			if($.isNull(vm.classGoodsDepts.classId)){
				alert("请选择班级！！！");
				return;
			}
			if($.isNull(vm.classGoodsDepts.goodId)){
				alert("请选择商品！！！");
				return;
			}
			if($.isNull(vm.classGoodsDepts.deptId)){
				alert("请选择部门！！！");
				return;
			}
			
			if(vm.title == "新增")
		    {
		       url = "../classgoodsdepts/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../classgoodsdepts/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.classGoodsDepts),
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
				    url: "../classgoodsdepts/delete",
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
			    url: "../classgoodsdepts/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.classGoodsDepts = r.data;
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
					"className" : vm.q.className,
					"teacherName":vm.q.teacherName,
					"deptName":vm.q.deptName,
					"goodName":vm.q.goodName,
				},
				page:page
            }).trigger("reloadGrid");
		},
		selectClass : function(){
			classLay.show(function(index,opt){
				vm.classGoodsDepts.classId = opt.classId;
				vm.classGoodsDepts.className = opt.className;
			});
		},
		selectGood : function(){
			goodsInfoLay.show(function(index,opt){
				vm.classGoodsDepts.goodId = opt.id;
				vm.classGoodsDepts.goodName = opt.name;
			});
		},
		selectDept : function(){//部门
			deptLay.show(function(id , name ,opt){
				vm.classGoodsDepts.deptId = id;
				vm.classGoodsDepts.deptName = name;
			});
		},
	}
});