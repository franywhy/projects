$(function () {
    $("#jqGrid").jqGrid({
        url: '../mallAdjustProfession/list',
        datatype: "json",
        colModel: [		
        	
														{ label: '学员ID', name: 'userId', width: 80 },
                                                        { label: '学员姓名', name: 'userName', width: 80 },
														{ label: '原来专业', name: 'lastProfessionName', width: 80 },
                                                        { label: '现转专业', name: 'professionName', width: 80 },
														{ label: '转入时间', name: 'createTime', width: 80 },
														{ label: '是否存档相关申请', name: 'applystatusStr', width: 80 },
                                                        { label: '班主任', name: 'teacherName', width: 80 }
							
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: false,
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
		mallAdjustProfession: {},
        selectData : {//下拉初始化列表
            //是否存档相关申请
            applystatusList : [{ value: -1 ,name: '全部'}, {value:1,name:'是'},{value:0,name:'否'} ]
        },
        queryData:{//查询条件
            applystatus : -1,
            userName:"",
            classTeacherName:"",
        }
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			console.log(vm.queryData);
			$("#jqGrid").jqGrid('setGridParam',{
                postData : vm.queryData,
                datatype: "json",
                page:page
            }).trigger("reloadGrid");
		},
        classTeacherLayerShow : function(){//班主任
            teacherLay.show(function(index,opt){
                vm.queryData.userId = opt.userId;
                vm.queryData.classTeacherName = opt.nickName;
            } , 2);
        },
        clearQueryData:function () {
            vm.queryData = {//查询条件
                applystatus:-1,
                userName:"",
                userId : "",
                classTeacherName:"",
            }
            vm.reload(null,1);
        }
		
	}
});