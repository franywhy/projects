/*$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/menu/list',
        datatype: "json",
        colModel: [			
			{ label: '菜单ID', name: 'menuId', width: 40, key: true },
			{ label: '菜单名称', name: 'name', width: 60 },
			{ label: '上级菜单', name: 'parentName', width: 60 },
			{ label: '菜单图标', name: 'icon', width: 50, formatter: function(value, options, row){
				return value == null ? '' : '<i class="'+value+' fa-lg"></i>';
			}},
			{ label: '菜单URL', name: 'url', width: 100 },
			{ label: '授权标识', name: 'perms', width: 100 },
			{ label: '类型', name: 'type', width: 50, formatter: function(value, options, row){
				if(value === 0){
					return '<span class="label label-primary">目录</span>';
				}
				if(value === 1){
					return '<span class="label label-success">菜单</span>';
				}
				if(value === 2){
					return '<span class="label label-warning">按钮</span>';
				}
			}},
			{ label: '排序号', name: 'orderNum', width: 50}                   
        ],
		viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
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
*/
var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "menuId",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	}
};
var ztree;

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		menu:{
			parentName:null,
			parentId:0,
			type:1,
			orderNum:0
		},
		q:{ //商品查询条件
			name : "",
			perms:""
}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		getMenu: function(menuId){
			//加载菜单树
			$.get("../sys/menu/select", function(r){
				ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
				var node = ztree.getNodeByParam("menuId", vm.menu.parentId);
				ztree.selectNode(node);
				
				vm.menu.parentName = node.name;
			})
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.menu = {parentName:null,parentId:0,type:1,orderNum:0};
			vm.getMenu();
		},
		update: function (event) {
			var menuId;
			var selected = $('#menuTable').bootstrapTreeTable('getSelections');
		    if (selected.length == 0) {
		        alert("请选择一条记录");
		        return false;
		    } else {
		    	menuId=selected[0].id
		    }
		    if (menuId == null || !menuId) {
				return;
			}
			$.get("../sys/menu/info/"+menuId, function(r){
				vm.showList = false;
                vm.title = "修改";
                vm.menu = r.menu;
            });
			
			vm.getMenu();
		},
		del: function (event) {
			var menuId;
			var selected = $('#menuTable').bootstrapTreeTable('getSelections');
		    if (selected.length == 0) {
		        alert("请选择一条记录");
		        return false;
		    } else {
		    	menuId=selected[0].id
		    }
		    if (menuId == null || !menuId) {
				return;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/menu/delete",
				    data: JSON.stringify(menuId),
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
			});
		},
		saveOrUpdate: function (event) {
			var url = vm.menu.menuId == null ? "../sys/menu/save" : "../sys/menu/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.menu),
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
		menuTree: function(){
			layer.open({
				type: 1,
				offset: '50px',
				skin: 'layui-layer-molv',
				title: "选择菜单",
				area: ['300px', '450px'],
				shade: 0,
				shadeClose: false,
				content: jQuery("#menuLayer"),
				btn: ['确定', '取消'],
				btn1: function (index) {
					var node = ztree.getSelectedNodes();
					//选择上级菜单
					vm.menu.parentId = node[0].menuId;
					vm.menu.parentName = node[0].name;
					
					layer.close(index);
	            }
			});
		},
		clearQuery : function(event) {
			vm.q = {//
					name : "",
					perms:""
			}
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				page:page
            }).trigger("reloadGrid");
		},
	}
});
var Menu = {
	    id: "menuTable",
	    table: null,
	    layerIndex: -1
	};

	/**
	 * 初始化表格的列
	 */
	Menu.initColumn = function () {
	    var columns = [
	        {field: 'selectItem', radio: true},
	        {title: '菜单ID', field: 'menuId', visible: false, align: 'center', valign: 'middle', width: '100px'},
	        {title: '菜单名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '100px'},
	        {title: '上级菜单', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
	        {title: '菜单图标', field: 'icon', visible: false, align: 'center', valign: 'middle', width: '100px'},
	        {title: '菜单URL', field: 'url', align: 'center', valign: 'middle', sortable: true, width: '100px'},
	        {title: '授权标识', field: 'perms', align: 'center', valign: 'middle', sortable: true, width: '100px'},
	        {title: '类型', field: 'type', align: 'center', valign: 'middle', sortable: true, width: '100px', formatter: function(value,row,index){if(value.type === 0){
				return '<span class="label label-primary">目录</span>';
			}
			if(value.type === 1){
				return '<span class="label label-success">菜单</span>';
			}
			if(value.type === 2){
				return '<span class="label label-warning">按钮</span>';
			}}},
	        {title: '排序号', field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '100px'}]
	    return columns;
	};

	function getMenuId () {
	    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
	    if (selected.length == 0) {
	        alert("请选择一条记录");
	        return false;
	    } else {
	    	return selected[0].id;
	    }
	}

	$(function () {
	    $.get("../sys/menu/info", function(r){
	        var colunms = Menu.initColumn();
	        var table = new TreeTable(Menu.id, "../sys/menu/list", colunms);
	        table.setRootCodeValue(r.menuId);//设置根节点code的值
	        table.setExpandColumn(2);//设置在哪一列上面显示展开按钮，从0开始
	        table.setIdField("menuId");//设置记录返回的id值
	        table.setCodeField("menuId");//设置记录分级的字段
	        table.setParentCodeField("parentId");//设置记录分级的父级字段
	        table.setExpandAll(false);//设置是否默认全部展开
	        table.init();
	        Menu.table = table;
	    });
	});