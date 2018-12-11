/**
 * 班级js
 */
var classMsLay = {
	init : function(){
		$("#classMsLayClassName").val("");
		var that = this;
		$("#classMsLay_jqGrid").jqGrid({
			url: "../layerdata/classList",
			datatype: "local",
			colModel: [
			           { label: "班级id", name: "classId" , key: true , width: 80	  },
			           
			           { label: "省份", name: "areaId" , hidden: true},
			           { label: "专业", name: "professionId" , hidden: true},
			           { label: "学历", name: "levelId" , hidden: true},
			           { label: "班主任", name: "userId" , hidden: true},
			           { label: "产品线", name: "productId" , hidden: true},

			           { label: "班级", name: "className", width: 200	  },
			           { label: "省份", name: "areaName", width: 80	  },
			           { label: "专业", name: "professionName", width: 120	  },
			           { label: "班主任", name: "classTeacherName", width: 80	  },
			           { label: "学历", name: "levelName", width: 80	  },
			           
			           /*{ label: "默认班级", name: "defaultClass", width: 80	  }*/
			           { label: '默认班级', name: 'defaultClass', width: 120 ,formatter:function(value, options, row){
							return row.defaultClass === 0 ? 
									'<span class="label label-danger">正常</span>' : 
									'<span class="label label-success">默认班级</span>';
						}}, 
		           ],
		   viewrecords: true,
		   height: 450,
		   rowNum: 100,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 35, 
		   autowidth:true,
		   multiselect: true,
		   pager: "#classMsLay_jqGridPager",
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
			   $("#classMsLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
			var that = this;
			var title = "班级列表";
			that.layerIndex = layer.open({
				type : 1,//
				area : ['850px','700px'],
				title :title,
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#classMsLayDiv"),
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
		$("#classMsLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
                className : $("#classMsLayClassName").val() , //班级名称
                teacherName : $("#teacherName").val() , //班主任名称
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
        var selectIds = getJqGridSelectedRows("classMsLay_jqGrid");
        if(selectIds == null){
            return ;
        }
        var selectDetails = [];
        selectIds.forEach(function(value, index, array) {
            selectDetails.push($("#classMsLay_jqGrid").jqGrid('getRowData',value));
        });
        if($.isFunction(this.scallback)){
            this.scallback(selectIds , selectDetails);
        }

        //关闭浮层
		layer.close(this.layerIndex);
		$("#classMsLayClassName").val("");
	}
}
classMsLay.init();
