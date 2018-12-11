$(function () {
    $("#jqGrid").jqGrid({
        url: '../recordRefunds/list',
        datatype: "json",
        colModel: [		
					{ label: '学员ID', name: 'userId', width: 80 },
	                { label: '学员姓名', name: 'name', width: 80 },
	                { label: '订单编号', name: 'orderNo', width: 80 },	
					{ label: '退费申请时间', name: 'refundsDate', width: 80 },
	                { label: '退费原因', name: 'refundsReason', width: 80 },
					{ label: '退费处理结果', name: 'refundsResult', width: 80 },
					{ label: '是否存档相关申请', name: 'applyStatus', align : "center",width: 80 ,formatter : function(value, options, row){
						if(value == 0){
							return '是';
						}else if(value == 1){
							return '否';
						}else  {
							return '';
						}
					    },
	                },
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

var tableColumn = ['','学员ID','学员名字','订单编号','退费申请时间','退费原因','退费处理结果','是否存档相关申请','班主任']

var tablesToExcel = (function() {
    var uri = 'data:application/vnd.ms-excel;base64,'
        , tmplWorkbookXML = '<?xml version="1.0"?><?mso-application progid="Excel.Sheet"?><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">'
        + '<DocumentProperties xmlns="urn:schemas-microsoft-com:office:office"><Author>Axel Richter</Author><Created>{created}</Created></DocumentProperties>'
        + '<Styles>'
        + '<Style ss:ID="Currency"><NumberFormat ss:Format="Currency"></NumberFormat></Style>'
        + '<Style ss:ID="Date"><NumberFormat ss:Format="Medium Date"></NumberFormat></Style>'
        + '</Styles>'
        + '{worksheets}</Workbook>'
        , tmplWorksheetXML = '<Worksheet ss:Name="{nameWS}"><Table>{rows}</Table></Worksheet>'
        , tmplCellXML = '<Cell{attributeStyleID}{attributeFormula}><Data ss:Type="{nameType}">{data}</Data></Cell>'
        , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
        , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
    return function(tables, wsnames, wbname, appname) {
        var ctx = "";
        var workbookXML = "";
        var worksheetsXML = "";
        var rowsXML = "";
        
        for (var i = 0; i < tables.length; i++) {
            if (!tables[i].nodeType) tables[i] = document.getElementById(tables[i]);

//          控制要导出的行数
            for (var j = 0; j < tables[i].rows.length; j++) {
                rowsXML += '<Row>';

//                控制导出的列数（在本例中，最后一列为button,导出的文件会出错，所以导出到倒数第二列
                for (var k = 0; k < tables[i].rows[j].cells.length-1; k++) {
                    var dataType = tables[i].rows[j].cells[k].getAttribute("data-type");
                    var dataStyle = tables[i].rows[j].cells[k].getAttribute("data-style");
                    var dataValue = tables[i].rows[j].cells[k].getAttribute("data-value");
                    dataValue = (dataValue)?dataValue:tables[i].rows[j].cells[k].innerHTML;
                    //导出列名
                    if(j == 0){
                    	dataValue = tableColumn[k];
                    }
                    if(dataValue == '&nbsp;'){ 
                    	dataValue = "";
                    	console.log("dataValue" ,dataValue);
                    }
                    var dataFormula = tables[i].rows[j].cells[k].getAttribute("data-formula");
                    dataFormula = (dataFormula)?dataFormula:(appname=='Calc' && dataType=='DateTime')?dataValue:null;
                    ctx = {  attributeStyleID: (dataStyle=='Currency' || dataStyle=='Date')?' ss:StyleID="'+dataStyle+'"':''
                        , nameType: (dataType=='Number' || dataType=='DateTime' || dataType=='Boolean' || dataType=='Error')?dataType:'String'
                        , data: (dataFormula)?'':dataValue
                        , attributeFormula: (dataFormula)?' ss:Formula="'+dataFormula+'"':''
                    };
                    rowsXML += format(tmplCellXML, ctx);
                }
                rowsXML += '</Row>'
            }
            ctx = {rows: rowsXML, nameWS: wsnames[i] || 'Sheet' + i};
            worksheetsXML += format(tmplWorksheetXML, ctx);
            rowsXML = "";
        }

        ctx = {created: (new Date()).getTime(), worksheets: worksheetsXML};
        workbookXML = format(tmplWorkbookXML, ctx);
  
        var link = document.createElement("A");
        link.href = uri + base64(workbookXML);
        link.download = wbname || 'Workbook.xls';
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
})();

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		mallAdjustArea: {},
        selectData : {//下拉初始化列表
            //是否存档相关申请
            applystatusList : [{ value: -1 ,name: '全部'}, {value:0,name:'是'},{value:1,name:'否'} ]
        },
        queryData:{//查询条件
            applyStatus : -1,
            name:"",
            classTeacherName:"",
        }
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		importData: function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '导入Excel',
				area: ['300px', '200px'],
				shadeClose: false,
				content: jQuery("#uploadExcel"),
				btn: ['确定','取消'],
				btn1: function (index) {
					$.ajaxFileUpload({
						url:'../recordRefunds/importData',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						success:function(data){
							if(data.code == 0 && data.msg != null) {
								alert(data.msg, function(index){
									$("#jqGrid").trigger("reloadGrid");
								});
								layer.close(index);
							}else if(data.code == 0){
                                alert(data.msg, function(index){
                                    $("#jqGrid").trigger("reloadGrid");
                                });
                                layer.close(index);
                            }else if (data.code == 1) {
								alert(data.msg);
							}
						}
					});
	            }
			});
		},
		exportData: function(){
			tablesToExcel(['jqGrid'], ['ProductDay1'], '退费信息.xls', 'Excel');
		},
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData : vm.queryData,
                datatype: "json",
                page:page
            }).trigger("reloadGrid");
		},
        classTeacherLayerShow : function(){//班主任
            teacherLay.show(function(index,opt){
                vm.queryData.teacherId = opt.userId;
                vm.queryData.classTeacherName = opt.nickName;
            } , 2);
        },
        clearQueryData:function () {
            vm.queryData = {//查询条件
                applyStatus:-1,
                name:"",
                teacherId : "",
                classTeacherName:"",
            }
            vm.reload(null,1);
        }
		
	}
});