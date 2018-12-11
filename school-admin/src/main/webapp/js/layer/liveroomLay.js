/**
 * 直播间弹窗js
 */
var liveroomLay = {
	init : function(){
		$("#qliveroomName").val("");
		var that = this;
		$("#liveroomLay_jqGrid").jqGrid({
			url: "../layerdata/liveroomList",
			datatype: "local",
			colModel: [
						{ label: '直播间Id', name: 'liveRoomId', hidden: true, width: 50, key: true },
						{ label: '直播间名称', name: 'liveRoomName', width: 150 }, 
						{ label: '直播间频道号', name: 'liveRoomChannelId', width: 80 }, 			
						{ label: '直播间频道密码', name: 'liveRoomChannelPassword', width: 80 }, 
						{ label: '直播间描述', name: 'liveRoomRemake', width: 80 }, 
						{ label: '展示互动直播Id', name: 'genseeLiveId', width: 80 },
						{ label: '展示互动直播房间号', name: 'genseeLiveNum', width: 80 },
						{ label: '创建人', name: 'creationName', width: 80 }, 			
						{ label: '创建时间', name: 'creationTime', width: 80 }, 			
						{ label: '修改人', name: 'modifiedName', width: 80 }, 			
						{ label: '修改时间', name: 'modifiedTime', width: 80 },
						{ label: '是否禁用', name: 'status', width: 80, formatter: function(value, options, row){
							return value === 0 ? 
								'<span class="label label-danger">禁用</span>' : 
								'<span class="label label-success">正常</span>';
						}}
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#liveroomLay_jqGridPager",
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
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#liveroomLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "直播间列表",
	layerHeigh : '701px',
	layerWeigh : '910px',
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : ['850px','650px'],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#liveroomLayDiv"),
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
		$("#liveroomLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				liveRoomName : $("#qliveroomName").val() , //直播室名称
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("liveroomLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#liveroomLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qliveroomName").val("");
	}
}
liveroomLay.init();
