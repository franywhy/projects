$.module("hq.ui",function() {

	return {
        /**
         * 导出excel
         * @param jqGridId 表格id
         */
		exportData : function(jqGridId){//导出excel
            jqGridId = jqGridId || "jqGrid"
			if(getExplorer()=='ie')
            {
                var curTbl = document.getElementById(tableid);
                var oXL = new ActiveXObject("Excel.Application");
                var oWB = oXL.Workbooks.Add();
                var xlsheet = oWB.Worksheets(1);
                var sel = document.body.createTextRange();
                sel.moveToElementText(curTbl);
                sel.select();
                sel.execCommand("Copy");
                xlsheet.Paste();
                oXL.Visible = true;

                try {
                    //var fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
                    var fname = oXL.Application.GetSaveAsFilename("将table导出到excel.xls", "Excel Spreadsheets (*.xls), *.xls");
                } catch (e) {
                    print("Nested catch caught " + e);
                } finally {
                    oWB.SaveAs(fname);
                    oWB.Close(savechanges = false);
                    oXL.Quit();
                    oXL = null;
                    idTmr = window.setInterval("Cleanup();", 1);
                }

            }
            else
            {
                tableToExcel(jqGridId);
            }
		}
	}
});

var idTmr;
//获取当前浏览器类型
function getExplorer() {
    var explorer = window.navigator.userAgent ;
    //ie
    if (explorer.indexOf("MSIE") >= 0) {
        return 'ie';
    }
    //firefox
    else if (explorer.indexOf("Firefox") >= 0) {
        return 'Firefox';
    }
    //Chrome
    else if(explorer.indexOf("Chrome") >= 0){
        return 'Chrome';
    }
    //Opera
    else if(explorer.indexOf("Opera") >= 0){
        return 'Opera';
    }
    //Safari
    else if(explorer.indexOf("Safari") >= 0){
        return 'Safari';
    }
}
function Cleanup() {
    window.clearInterval(idTmr);
    CollectGarbage();
}

//判断浏览器后调用的方法，把table的id传入即可
var tableToExcel = (function(table) {
    var uri = 'data:application/vnd.ms-excel;base64,',
        template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',
        base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
        format = function(s, c) {
            return s.replace(/{(\w+)}/g,
                function(m, p) { return c[p]; }) }
    return function(table, name) {


        var tableThead = $('.ui-jqgrid-htable').html();
         table = document.getElementById(table)
        var elementType= document.createElement('table');
        elementType.id = 'commonExcelTable';
        document.body.appendChild(elementType);
        $("#commonExcelTable").attr('style',"display:none");
        $("#commonExcelTable").html(tableThead+table.innerHTML);
        if($("#commonExcelTable").find('input').length){
            for(var i=0;i<elementType.rows.length;i++){
                elementType.rows[i].deleteCell(1);
            }
        };
        var hidden_ths = $("#commonExcelTable").find('th');
        var hidden_tds = $("#commonExcelTable").find('td');
        $.each(hidden_ths,function (index,v) {
            if($(v).css("display")==='none'){
                $(v).remove();
            }
        })
        $.each(hidden_tds,function (index,v) {
            if($(v).css("display")==='none'){
                $(v).remove();
            }
        })
        var ctx = {worksheet: name || 'Worksheet', table: elementType.innerHTML}
        var link= $('<a id="commonExcelDownload" ></a>');
        link.get(0).href = uri + base64(format(template, ctx));
        link.get(0).download = "文件导出.xls";
        link.get(0).click();
        $("#commonExcelTable").remove();
    }
})()
