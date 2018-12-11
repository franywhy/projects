$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/user/list',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'userId', width: 45, key: true },
			{ label: '账号', name: 'username', width: 75 },
			{ label: '昵称', name: 'nickName', width: 75 },
			{ label: '所属部门', name: 'deptName', width: 75 },
			{ label: '邮箱', name: 'email', width: 90 },
			{ label: '小能分组ID', name: 'wxCode', width: 90},
			{ label: '手机号', name: 'mobile', width: 100 },
			{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? '<span class="label label-danger">禁用</span>' : '<span class="label label-success">正常</span>';
			}},
			{ label: '班主任', name: 'classTeacher', width: 80, formatter: function(value, options, row){
				return value === 1 ? '<span class="label label-success">是</span>' : '<span class="label label-danger">否</span>';
			}},
			{ label: '教学老师', name: 'teacher', width: 80, formatter: function(value, options, row){
				return value === 1 ? '<span class="label label-success">是</span>' : '<span class="label label-danger">否</span>';
			}},
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

var setting = {
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
var ztree;

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			username: "",
			mobile: "",
			nickName: "",
			classTeacher:"",
			teacher:""
		},
		showList: true,
		title:null,
		roleList:{},
		user:{
			username:"",
			nickName:"",
			password:"",
			status:1,
			deptId:null,
            deptName:null,
			roleIdList:[]
		},
		options1: [//是否班主任  
		      { text: '不是', value: '0' },  
		      { text: '是', value: '1' }  
		    ],
	    options2: [//是否教学老师  
		      { text: '不是', value: '0' },  
		      { text: '是', value: '1' }  
		    ],
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.roleList = {};
			vm.user = {
					username:"",
					nickName:"",
					status : 1 , 
					deptId:null,
		            deptName:null,
					roleIdList : [] , 
					classTeacher : 0 , 
					teacher : 0
			};
			
			//获取角色信息
			this.getRoleList();
			//获取部门
			vm.getDept();
		},
		getDept: function(){
            //加载部门树
            $.get("../sysdept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
				}
            })
        },
		update: function () {
			var userId = getSelectedRow();
			if(userId == null){
				return ;
			}
			
			vm.showList = false;
            vm.title = "修改";
			
			vm.getUser(userId);
			//获取角色信息
			this.getRoleList();
		},
		del: function () {
			var userIds = getSelectedRows();
			if(userIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/user/delete",
				    data: JSON.stringify(userIds),
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
		saveOrUpdate: function (event) {
			if($.isNull(vm.user.username)){
				alert("请输入用户名！！！");
				return;
			}
			if($.isNull(vm.user.nickName)){
				alert("请输入昵称！！！");
				return;
			}
			if($.isNull(vm.user.deptId)){
				alert("请选择部门！！！");
				return;
			}
//			if($.isNull(vm.user.password)){
//				alert("请输入密码！！！");
//				return;
//			}
			var url = vm.user.userId == null ? "../sys/user/save" : "../sys/user/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.user),
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
		getUser: function(userId){
			$.get("../sys/user/info/"+userId, function(r){
				vm.user = r.user;
				
				vm.getDept();
			});
		},
		getRoleList: function(){
			$.get("../sys/role/select", function(r){
				vm.roleList = r.list;
			});
		},
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
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;

                    layer.close(index);
                }
            });
        },
		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData : vm.q,
                page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var userIds = getSelectedRows();
			if(userIds == null){
				return ;
			}
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/user/resume",
				    data: JSON.stringify(userIds),
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
		pause:function(event){
		    var userIds = getSelectedRows();
			if(userIds == null){
				return ;
			}
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/user/pause",
				    data: JSON.stringify(userIds),
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
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q.username = "";
			vm.q.mobile = "";
			vm.q.nickName = "";
			vm.q.classTeacher = "";
			vm.q.teacher = "";
		},
	}
});