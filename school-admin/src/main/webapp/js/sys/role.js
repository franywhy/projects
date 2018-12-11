$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/role/list',
        datatype: "json",
        colModel: [			
			{ label: '角色ID', name: 'roleId', width: 45, key: true },
			{ label: '角色名称', name: 'roleName', width: 75 },
			{ label: '所属部门', name: 'deptName', width: 75 },
			{ label: '备注', name: 'remark', width: 100 },
			{ label: '创建时间', name: 'createTime', width: 80}                   
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

//部门结构树
var dept_ztree;
var dept_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};

//菜单树
var menu_ztree;
var menu_setting = {
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
	},
	check:{
		enable:true,
		nocheckInherit:true
	}
};

//数据权限-部门树
var dataDept_ztree;
var dataDept_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    },
    check:{
		enable:true,
		nocheckInherit:true
	}
};
	
var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			roleName: null,
			deptName: null
		},
		showList: true,
		title:null,
		productList:{},
		role:{
			deptName:null, 
			deptId:null,
			productIdList:[]
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.productList = {};
			vm.role = {
					deptName:null, 
					deptId:null,
					productIdList:[]
			};
			vm.getMenuTree(null);//加载功能菜单树
			vm.getDept();//加载部门树
			vm.getDataDeptTree();//加载数据权限-部门树
			
			//获取产品信息
			this.getProductList();
		},
		//获取产品列表信息
		getProductList: function(){
			$.get("../sysproduct/select", function(r){
				vm.productList = r.list;
			});
		},
		update: function () {
			var roleId = getSelectedRow();
			if(roleId == null){
				return ;
			}
			
			vm.showList = false;
            vm.title = "修改";
            vm.getRole(roleId);
            
            vm.getMenuTree(roleId);
            vm.getDept();//加载部门树
			vm.getDataDeptTree();//加载数据权限-部门树
			
			//获取产品信息
			this.getProductList();
		},
		del: function (event) {
			var roleIds = getSelectedRows();
			if(roleIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/role/delete",
				    data: JSON.stringify(roleIds),
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
		getRole: function(roleId){
            $.get("../sys/role/info/"+roleId, function(r){
            	vm.role = r.role;
                
                //勾选角色所拥有的菜单
            	var menuIds = vm.role.menuIdList;
    			for(var i=0; i<menuIds.length; i++) {
    				var node1 = menu_ztree.getNodeByParam("menuId", menuIds[i]);
    				menu_ztree.checkNode(node1, true, false);
    			}
    			
    			//勾选角色所拥有的部门数据权限-部门
                var deptIds = vm.role.deptIdList;
                for(var i=0; i<deptIds.length; i++) {
                    var node = dataDept_ztree.getNodeByParam("deptId", deptIds[i]);
                    dataDept_ztree.checkNode(node, true, false);
                }
                
                vm.getDept();
    		});
		},
		saveOrUpdate: function (event) {
			//获取选择的菜单
			var nodes = menu_ztree.getCheckedNodes(true);
			var menuIdList = new Array();
			for(var i=0; i<nodes.length; i++) {
				menuIdList.push(nodes[i].menuId);
			}
			vm.role.menuIdList = menuIdList;
			
			//获取选择的数据权限-部门
            var nodes = dataDept_ztree.getCheckedNodes(true);
            var deptIdList = new Array();
            for(var i=0; i<nodes.length; i++) {
                deptIdList.push(nodes[i].deptId);
            }
            vm.role.deptIdList = deptIdList;
            
			var url = vm.role.roleId == null ? "../sys/role/save" : "../sys/role/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.role),
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
		//获取菜单树
		getMenuTree: function(roleId) {
			//加载菜单树
			$.get("../sys/menu/perms", function(r){
				menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r.menuList);
				//展开所有节点
				menu_ztree.expandAll(true);
				
				if(roleId != null){
					vm.getRole(roleId);
				}
			});
	    },
	  //获取数据权限-部门树
        getDataDeptTree: function(roleId) {
            //加载部门树
            $.get("../sysdept/list", function(r){
                dataDept_ztree = $.fn.zTree.init($("#dataDeptTree"), dataDept_setting, r);
                //展开所有节点
                dataDept_ztree.expandAll(true);
            });
        },
	    
	    //获取部门结构树
	    getDept: function(){
            //加载部门树
            $.get("../sysdept/list", function(r){
            	dept_ztree = $.fn.zTree.init($("#deptTree"), dept_setting, r);
                var node = dept_ztree.getNodeByParam("deptId", vm.role.deptId);
                if(node != null){
                	dept_ztree.selectNode(node);

                    vm.role.deptName = node.name;
				}
            })
        },
        
        //部门树弹窗
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = dept_ztree.getSelectedNodes();
                    //选择上级部门
                    vm.role.deptId = node[0].deptId;
                    vm.role.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
	    reload: function (event) {
	    	vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:vm.q,
                page:page
            }).trigger("reloadGrid");
		}
	    
	}
});