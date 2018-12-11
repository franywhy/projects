$(function () {
    $("#jqGrid").jqGrid({
        url: '../givingcourses/grouplist',
        datatype: "json",
        colModel: [		
			{ label: 'id', name: 'id', key: true },
			{ label: '创建者', name: 'createPerson', width: 80 }, 						
			{ label: '创建时间', name: 'createTime', width: 80 }, 						
			{ label: '修改者', name: 'modifyPerson', width: 80 }, 						
			{ label: '更新时间', name: 'modifyTime', width: 80 }, 						
			{ label: '赠送课程商品ID', name: 'mallGoodsId', width: 80 }, 						
			{ label: '商品名称', name: 'mallGoodsName', width: 80 }, 						
			{ label: 'NC商品ID', name: 'ncCommodityId', width: 80 }, 						
			{ label: 'NC商品名称', name: 'ncCommodityName', width: 80 }, 										
			{ label: '产品线', name: 'productId', width: 80 },		
			{ label: '赠送类型', name: 'givingType', width: 80 },		
 
			{ label: '备注', name: 'remark', width: 80 }						
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
	data : {
		showList : true,
		title : null,
		givingCourses : {
			mallGoodsId:"",
			mallGoodsName:"",
			givingType:"",
			productId:""
		}

	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.givingCourses = {
				mallGoodsId : "",
				mallGoodsName : "",
				givingType:"1",
				productId:""
			};
			  $("#ncCommodityId").attr("disabled",false);
			  $("#mallGoodsId").attr("disabled",false);
			  
			//  disabled
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id)
            $("#ncCommodityId").attr("disabled",true);
            $("#mallGoodsId").attr("disabled",true);
           /* $("#mallGoodsId").attr("style", "display:block");
			$("#mallGoodsName").attr("style", "display: none");*/
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../givingcourses/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../givingcourses/update";
		    }else
		    {
		       url = "";
		    }
		    confirm('提示：组合班型档案一旦保存之后不能再次修改NC商品ID的，保存前请认真核实NC商品ID是否正确。', function(){
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.givingCourses),
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
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../givingcourses/delete",
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
			    url: "/givingcourses/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
			     
					 	 vm.givingCourses = r.data;
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
                page:page
            }).trigger("reloadGrid");
		},
			/////////////编辑子表浮层//////////////////
		givingCoursesShow : function(){
			goodsInfoLay.show(function(index, opt) {
				vm.givingCourses.mallGoodsId = opt.id;
				vm.givingCourses.mallGoodsName = opt.name;
			});
		},
		
		productLayerShow : function(){//产品线
			productLay.show(function(index, opt) {
				vm.givingCourses.productId = opt.productId;
				/*vm.givingCourses.productName = opt.productName;*/
			});
		}
		
	}
});