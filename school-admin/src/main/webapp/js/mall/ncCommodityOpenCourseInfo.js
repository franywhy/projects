$(function () {
	$("#jqGrid").jqGrid({
        url: '../ncCommodityOpenCourseInfo/list',
        datatype: "json",
        colModel: [		        	
			{ label: 'id', name: 'id', key: true,hidden:true },
			{ label: '创建用户ID', name: 'creator', width: 80 ,hidden:true},
			{ label: '创建时间', name: 'createTime', width: 80 ,hidden:true},
			{ label: '修改用户', name: 'modifier', width: 80 ,hidden:true},
			{ label: '修改时间', name: 'modifyTime', width: 80,hidden:true },
			{ label: '报读班型', name: 'ncCommodityId', width: 80 },
			{ label: '班型名称', name: 'ncCommodityName', width: 80 },
			{ label: '权限ID', name: 'authorityId', width: 80,hidden:true },
            { label: '权限', name: 'authorityName', width: 80 }
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
        ncCommodityOpenCourseInfo: {
            ncCommodityId:"",//课程id
            ncCommodityName: "",  //课程名称
            authorityName:"",   //权限名称
            authorityId:"",   //权限id
		},
		q: {//查询条件
            ncCommodityId:"",//课程id
            authorityId:"",   //权限id
            authorityName:"",//权限名称
		}
	},
		
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.ncCommodityOpenCourseInfo = {
                ncCommodityId:"",
                ncCommodityName:"",
                authorityId:"",
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
			if(vm.title == "新增")
		    {
		       url = "../ncCommodityOpenCourseInfo/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../ncCommodityOpenCourseInfo/update";
		    }else
		    {
		       url = "";
		    }

            if(!vm.ncCommodityOpenCourseInfo.ncCommodityId){
                alert('请输入NC报读班型');
                return;
            }else if(!vm.ncCommodityOpenCourseInfo.ncCommodityName){
                alert('请输入班型名称');
                return;
			}else if(!vm.ncCommodityOpenCourseInfo.authorityId){
                alert('请选择权限');
                return;
            }
			$.ajax({

				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.ncCommodityOpenCourseInfo),
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
				    url: "../ncCommodityOpenCourseInfo/delete",
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
			    url: "/ncCommodityOpenCourseInfo/infoPOJO/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.ncCommodityOpenCourseInfo = r.ncCommodityOpenCourseInfo;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (e , p) {
            var vm = this;
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                datatype: "json",
				postData: {
                    "ncCommodityId": vm.q.ncCommodityId,//报读班型
                    "authorityId": vm.q.authorityId //权限id
                },
				page:page
            }).trigger("reloadGrid");
		},
		clearQuery : function(){//清空查询条件
            var vm = this;
            vm.q.ncCommodityId = "";
            vm.q.authorityId = "";
            vm.q.authorityName = "";
        },
        //新增公开课权限
        courseOliveAuthorityShow : function(){
            courseOliveAuthorityLay.show(function(index, opt) {
            	vm.ncCommodityOpenCourseInfo.authorityId = opt.authorityId;
                vm.ncCommodityOpenCourseInfo.authorityName = opt.authorityName;
                $("#authorityName").val(opt.authorityName);
            });
        },
        //查询公开课权限
        qCourseOliveAuthorityShow : function(){
            courseOliveAuthorityLay.show(function(index, opt) {
                vm.q.authorityId = opt.authorityId;
                vm.q.authorityName = opt.authorityName;
            });
        }

	}
});