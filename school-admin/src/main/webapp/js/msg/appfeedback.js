$(function () {
    $("#jqGrid").jqGrid({
        url: '../appfeedback/list',
        datatype: "json",
        colModel: [		
			{ label: 'feedbackId', name: 'feedbackId', hidden: true, key: true },
			{ label: '反馈内容', name: 'content', width: 80 }, 						
			{ label: '反馈时间', name: 'timestamp', width: 80 }, 						
			{ label: '前台用户id', name: 'userId', hidden: true, width: 80 }, 		
			{ label: '前台用户', name: 'nickName', width: 80 }, 
			{ label: '手机号码', name: 'mobile', width: 80 },
			{ label: '客户端类型', name: 'clientType', width: 80, formatter: function(value, options, row){
				var text = '';
				if(value == 0)text='<span class="label label-danger">web</span>';
				else if(value == 1)text='<span class="label label-danger">andriod</span>';
				else if(value == 2)text='<span class="label label-danger">ios</span>';
				return text; }
			}, 						
			{ label: '客户端版本', name: 'clientVersion', width: 80 }						
        ],
		viewrecords: true,
        height: 495,
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
			content:"",
			nickName:"",
			mobile:""
		},
		showList: true,
		title: null,
		appFeedback: {}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.appFeedback = {};
		},
		update: function (event) {
			var feedbackId = getSelectedRow();
			if(feedbackId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "查看";
            
            vm.getInfo(feedbackId)
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../appfeedback/save";
		    }
		    else if(vm.title == "查看")
		    {
		       url = "../appfeedback/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.appFeedback),
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
			var feedbackIds = getSelectedRows();
			if(feedbackIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../appfeedback/delete",
				    data: JSON.stringify(feedbackIds),
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
		getInfo: function(feedbackId){
            $.ajax({
				type: "POST",
			    url: "/appfeedback/info/" + feedbackId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.appFeedback = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {
					"content" : vm.q.content,
					"nickName" : vm.q.nickName,
					"mobile" : vm.q.mobile
				},
				page:page
            }).trigger("reloadGrid");
		}
		
	}
});