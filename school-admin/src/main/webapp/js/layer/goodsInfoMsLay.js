/**
 * 商品弹窗js
 */
var goodsInfoMsLay = {
	init : function(){
		$("#qcommodityId").val("");
		$("#qcommodityName").val("");
		var that = this;
		$("#goodsInfoMsLay_jqGrid").jqGrid({
			url: "../layerdata/goodsInfoList",
			datatype: "local",
			colModel: [
			           { label: "ID", name: "id",width: 100 , key: true},
			           { label: "专业ID", name: "professionId",width: 100,hidden:true},
			           { label: "层次ID", name: "levelId",width: 100,hidden:true},
			           { label: "班型ID", name: "classTypeId",width: 100,hidden:true},
			           { label: "产品ID", name: "productId",width: 100,hidden:true},
			           { label: "是否开通题库权限", name: "onlyOne",width: 100,hidden:true},
			           { label: "商品名称", name: "name", width: 240	  },
			           { label: "班型", name: "classTypeName", width: 80	  },
			           { label: "专业", name: "professionName", width: 80	  },
			           { label: "层次", name: "levelName", width: 80	  },
			           { label: "产品", name: "productName", width: 80	  },
			           { label: "小图", name: "thumbPath", width: 260	 ,hidden:true },
			           { label: "大图", name: "originPath", width: 260	,hidden:true  },
			           { label: "售价", name: "presentPrice", width: 260	,hidden:true  },
			           { label: "原价", name: "originalPrice", width: 260	  ,hidden:true},
			           {label : '上架状态', name : 'status', width : 80,
							formatter : function(value, options, row) {
								return value === 1 ? '<span class="label label-success">上架</span>'
										: '<span class="label label-danger">下架</span>';
							}
						},
						{label : '审核状态', name : 'isAudited', width : 80,
							formatter : function(value, options, row) {
								return value === 1 ? '<span class="label label-success">通过</span>'
										: '<span class="label label-danger">驳回</span>';
							}
						}
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 35, 
		   autowidth:true,
		   multiselect: true,
		   pager: "#goodsInfoMsLay_jqGridPager",
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
		   prmNames : {
				page : "page",
				rows : "limit",
				order : "order"
			},
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#goodsInfoMsLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
//	layerTitle : "商品列表",
//	layerHeigh : '700px',
//	layerWeigh : '790px',
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		var title = "商品列表";
		that.layerIndex = layer.open({
			type : 1,//
			//area : 'auto',
			area : ['900px', '700px'],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#goodsInfoMsLayDiv"),
			scrollbar : true,//是否允许浏览器出现滚动条
			fixed : false,//固定
			shadeClose : false,// 是否点击遮罩关闭
			resize : true,//是否允许拉伸
			maxmin: true, //开启最大化最小化按钮
			zIndex : 19891014,
			btn : ['确认','取消' ],
			btn1: function (index) {
			 that.select();
			}
		});
		
	},
	reload:function(){
		$("#goodsInfoMsLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				commodityId : $("#qcommodityId").val() , //商品Id
				commodityName : $("#qcommodityName").val() , //商品名称
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectIds = getJqGridSelectedRows("goodsInfoMsLay_jqGrid");
        if(selectIds == null){
            return ;
        }
		var selectDetails = [];
		selectIds.forEach(function(value, index, array) {
			selectDetails.push($("#goodsInfoMsLay_jqGrid").jqGrid('getRowData',value));
		});
		if($.isFunction(this.scallback)){
			this.scallback(selectIds , selectDetails);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qcommodityName").val("");
		$("#qcommodityId").val("");
	}
}
goodsInfoMsLay.init();
