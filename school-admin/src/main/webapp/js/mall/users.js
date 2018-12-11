$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/users/list',
        datatype: "local",
        colModel: [			
			{ label: '学员Id', name: 'userId', width: 50, key: true },
//			{ label: '头像', name: 'pic', width: 80 },
			{ label: '头像', name: 'pic', width: 50 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}},
			{ label: '登录账号', name: 'mobile', width: 80 }, 
			{ label: '昵称', name: 'nickName', width: 80 }, 			
			{ label: '性别', name: 'sex', hidden: true, width: 80 }, 
			{ label: '性别', name: 'sexName', width: 50 ,formatter: function(v, options, row){
				var value = row.sex;
				var text = '';
				if(value == 0)text= '女';
				else if(value == 1)text= '男';
				else if(value == 2)text= '保密';
				return text;
			}},
			{ label: '是否禁用', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},			
			{ label: '创建时间', name: 'creationTime', width: 80 },
			{ label: '邮箱', name: 'email', width: 80 },
			{ label: '最近登录时间', name: 'lastLoginTime', width: 80 }, 			
			{ label: '备注', name: 'remake', width: 80 }			
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		users: {
			pic:"http://zikao.hqjy.com/Avatar.png",
			status:1,
			sex:0,
		},
		selectData : {//下拉初始化列表
			professionList :[],//专业
		},
		options: [  
	      { text: '女', value: '0' },  
	      { text: '男', value: '1' },  
	      { text: '保密', value: '2' }  
	    ],
	    q:{
			nickName:"",
			userId:"",
			mobile:""
		}
	},
	methods: {
		initProfessionList: function(){//初始化专业列表
			var that = this;
			$.ajax({
				type: "GET",
			    url: "../common/professionList",
//			    data: JSON.stringify(userPlanIds),
			    success: function(r){
					if(r.code == 0){
						that.selectData.professionList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.users = {
					pic:"",
					status:1,
					sex:0,
					};
		},

		update: function (event) {
			var userId = getSelectedRow();
			if(userId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(userId)
		},
		saveOrUpdate: function (event) {
			if($.isNull(vm.users.mobile)){
		    	alert("请输入登录账号(手机号)！！！");
		    	return;
		    }else{
                var reg = /^1\d{10}$/;
                if (!reg.test(vm.users.mobile)) {
                   alert("登录账号格式不正确，请重新输入登录账号(手机号)！！！");
                    return;
                }
            }
            if(!$.isNull(vm.users.email)){
                var reg=new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
                if (!reg.test(vm.users.email)) {
                    alert("邮箱格式不正确，请重新输入！！！");
                    return;
                }
            }
			/*if($.isNull(vm.users.password)){
		    	alert("请输入登录密码！！！");
		    	return;
		    }*/
			if($.isNull(vm.users.nickName)){
		    	alert("请输入昵称！！！");
		    	return;
		    }
			if(!$.isNull(vm.users.remake) && vm.users.remake.length > 50){
				alert("备注信息不得超过50个字！！！");
		    	return;
			}
			if(vm.title == "新增")
		    {
		       url = "../mall/users/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/users/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.users),
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
			var userIds = getSelectedRows();
			if(userIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/users/delete",
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
		restPsw: function (event) {
			var userId = getSelectedRow();
			if(userId == null){
				return ;
			}
			
			confirm('确定要重置密码？', function(){
				$.ajax({
					type: "POST",
					url: "../mall/users/restPsw/"+userId,
//					data: JSON.stringify(userIds),
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
		getInfo: function(userId){
			$.get("../mall/users/info/"+userId, function(r){
                vm.users = r.users;
            });
		},
		reload: function (event , p) {
			var vm = this;
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				datatype: "json",
                page:page
            }).trigger("reloadGrid");
		},
		clearQuery : function(event) {//重置查询条件
			vm.q = {
					nickName:"",
					userId:"",
					mobile:""
			}
		},
		resume:function(event){
		   var userIds = getSelectedRows();
			if(userIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/users/resume",
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
				    url: "../mall/users/pause",
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
		}
	},
	created : function(){
		var vm = this;
		//初始化专业列表
		vm.initProfessionList();
	}
});
