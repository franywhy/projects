/**
 * 分类+商品
 * 保利威视视频-弹窗js
 * 2017-08-11
 * shihongjie
 * setting.data.key.name
 * @see https://tieba.baidu.com/p/2334961259
 */
var polyVideoLay = {
	init : function(){
		var that = this;
		$("#polyVideoLay_jqGridX").jqGrid({
			url: "../layerdata/polyvVideoList",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "vid",width: 10 , key: true , hidden: true},
			           { label: "视频名称", name: "title", width: 180	  },
			           { label: "上传时间", name: "ptime", width: 120	  },
			           { label: "时长", name: "duration", width: 120	  },
			           { label: "封面", name: "first_image", width: 180	  }
		           ],
		   viewrecords: true,
		   height: 400,
		   width : 950,
		   rowNum: 10,
		   rowList : [10,30,50,100],
		   rownumbers: true, 
		   rownumWidth: 45, 
		   autowidth:false,
		   multiselect: false,
		   pager: "#polyVideoLay_jqGridPagerX",
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
			   $("#polyVideoLay_jqGridPagerX").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
		});
	},
	courseId :"",//课程ID
	productId :null,//产品线ID
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "视频列表",
	layerHeigh : '400px',
	layerWeigh : '600px',
	show : function(courseId , scallback , productId){
		var that = this;
		that.courseId = courseId;
		that.productId = productId;
		that.scallback = scallback;
		//树
		that.getPolyVideoCataTree();
		
		that.layerIndex = layer.open({
			type : 1,//
			area : "auto",
//			area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#polyVideoLayDiv"),
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
		layer.full(that.layerIndex);  
		
	},
	reload:function(catatree , total){//加载视频
		var that = this;
		$("#polyVideoLay_jqGridX").jqGrid('setGridParam',{ 
			postData : {
				courseId : that.courseId,
				productId : that.productId,
				catatree : catatree,
				total : total
			},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){//选择
		var selectDetail = getJqGridSelectedRow("polyVideoLay_jqGridX");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#polyVideoLay_jqGridX").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	},
	//获取菜单树
	getPolyVideoCataTree: function() {
		var that = this;
		//加载菜单树
		hq.ajax({
			type: "GET",
		    url: "../layerdata/polyvVideoCataList",
		    data: {courseId : that.courseId , productId : that.productId},
		    success: function(r){
				if(r.code == 0){
					that.menu_setting.callback.onClick = that.zTreeOnClick;
					that.menu_ztree = $.fn.zTree.init($("#dataPolyVideoTree"), that.menu_setting, r.data);
					that.menu_ztree.expandAll(true);//展开
				}else{
					alert(r.msg);
				}
			}
		});
    },
	menu_ztree : null,//树
	menu_setting: {//树配置
		data: {
			simpleData: {
				idKey: "cataid",
				pIdKey: "parentid",
				rootPId: 0
			},
			key: {
				url:"nourl",
				children:"nodes",
				title:"text",
				name:"text"
			}
		},
		check:{
			enable:false,//设置 zTree 的节点上是否显示 checkbox / radio
			nocheckInherit:false//当父节点设置 nocheck = true 时，设置子节点是否自动继承 nocheck = true 
		},
		callback:{
			onClick: null
		}
	},
	zTreeOnClick :function (event, treeId, treeNode) {//视频分类树 点击事件
		var that = polyVideoLay;
		//加载视频列表
		that.reload(treeNode.catatree , treeNode.videos);
//	    console.log(treeNode.text + ", " + treeNode.text + ", " + treeNode.cataid + ", " + treeNode.parentid + ", " + treeNode.videos + ", " + treeNode.catatree);
	}
}
polyVideoLay.init();
