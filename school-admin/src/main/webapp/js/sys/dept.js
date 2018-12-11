var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el:'#rrapp',
    data:{
        showList: true,
        title: null,
        sysDept:{
            parentName:null,
            parentId:0,
            orderNum:0
        }
    },
    methods: {
        getDept: function(){
            //加载部门树
        	$.get("../sysdept/select", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
                var node = ztree.getNodeByParam("deptId", vm.sysDept.parentId);
                ztree.selectNode(node);

                vm.sysDept.parentName = node.name;
            })
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.sysDept = {parentName:null,parentId:0,orderNum:0};
            vm.getDept();
        },
        update: function () {
            var deptId = getDeptId();
            if(deptId == null){
                return ;
            }

            $.get("../sysdept/info/"+deptId, function(r){
                vm.showList = false;
                vm.title = "修改";
                vm.sysDept = r.sysDept;

                vm.getDept();
            });
        },
        del: function () {
            var deptId = getDeptId();
            console.log(deptId);
            if(deptId == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../sysdept/delete",
//                    data: "deptId=" + deptId,
                    data: JSON.stringify(deptId),
                    success: function(r){
                        if(r.code === 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
      
        saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../sysdept/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../sysdept/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.sysDept),
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
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.sysDept.parentId = node[0].deptId;
                    vm.sysDept.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            Dept.table.refresh();
        }
    }
});

var Dept = {
    id: "deptTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Dept.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '部门ID', field: 'deptId', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: '部门名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: '上级部门', field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: 'NCID', field: 'ncId', visible: false, align: 'center', valign: 'middle', width: '80px'},
        {title: 'NC部门名称', field: 'ncName', align: 'center', valign: 'middle', sortable: true, width: '180px'},
        {title: 'NC上级部门', field: 'ncParentName', align: 'center', valign: 'middle', sortable: true, width: '100px'},
        {title: '排序号', field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '100px'}]
    return columns;
};

function getDeptId () {
    var selected = $('#deptTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return false;
    } else {
    	return selected[0].id;
    }
}

$(function () {
    $.get("../sysdept/info", function(r){
        var colunms = Dept.initColumn();
        var table = new TreeTable(Dept.id, "../sysdept/list", colunms);
        table.setRootCodeValue(r.deptId);//设置根节点code的值
        table.setExpandColumn(2);//设置在哪一列上面显示展开按钮，从0开始
        table.setIdField("deptId");//设置记录返回的id值
        table.setCodeField("deptId");//设置记录分级的字段
        table.setParentCodeField("parentId");//设置记录分级的父级字段
        table.setExpandAll(false);//设置是否默认全部展开
        table.init();
        Dept.table = table;
    });
});