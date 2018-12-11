/**
 * 班级js
 */
var classLay = {
	init : function(){
		$("#classLayClassName").val("");
		var that = this;
		$("#classLay_jqGrid").jqGrid({
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
		   pager: "#classLay_jqGridPager",
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
			   $("#classLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
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
				content : $("#classLayDiv"),
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
    showRows : function(scallback){
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
            content : $("#classLayDiv"),
            scrollbar : true,//是否允许浏览器出现滚动条
            fixed : false,//固定
            shadeClose : false,// 是否点击遮罩关闭
            resize : true,//是否允许拉伸
            maxmin: true, //开启最大化最小化按钮
            zIndex : 19891014,
            btn : ['确认','取消' ],
            btn1: function (index) {
                that.selectRows();
            }
        });

    },
	reload:function(){
		$("#classLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				className : $("#classLayClassName").val() , //班级名称
                teacherName : $("#teacherName").val() , //班主任名称
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	//单选
	select:function(){
        var selectDetail = getJqGridSelectedRow("classLay_jqGrid");
        if(selectDetail == null){
            return ;
        }
        //行数据
        var rowData = $("#classLay_jqGrid").jqGrid('getRowData',selectDetail);
        if($.isFunction(this.scallback)){
            this.scallback(selectDetail , rowData);
        }
        //关闭浮层
        layer.close(this.layerIndex);
        $("#classLayClassName").val("");
    },
    //多选
    selectRows:function(){
        var selectArrays = getJqGridSelectedRows("classLay_jqGrid");
        if(selectArrays == null){
            return ;
        }
        var classIdList = new Array();
        var classNameList = new Array();
        for(var i=0; i<selectArrays.length; i++) {
            //行数据
            var rowData = $("#classLay_jqGrid").jqGrid('getRowData',selectArrays[i]);
            classIdList.push(rowData.classId);
            classNameList.push(rowData.className);
        }
        if($.isFunction(this.scallback)){
            this.scallback(classIdList,classNameList);
        }
        //关闭浮层
        layer.close(this.layerIndex);
        $("#classLayClassName").val("");
    }
}
classLay.init();
