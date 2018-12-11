$(function () {
    $("#jqGrid").jqGrid({
        url: '../sysversion/list',
        datatype: "json",
        colModel: [

            { label: 'id', name: 'id', key: true ,hidden:true},
            { label: 'APP类型', name: 'schoolId', width: 75,formatter: function(v, options, row){
                var value = row.schoolId;
                var text = '';
                if(value == 'zikao')text= '自考APP';
                else if(value == 'kuaiji')text= '会计APP';
                else if(value == 'teacher')text= '员工端APP';
                return text;
            } },
            { label: '版本类型', name: 'clientType', width: 80 },
            { label: '最小版本编码', name: 'minVersionCode', width: 80 },
            { label: '版本编码', name: 'versionCode', width: 80 },
            { label: '版本名称', name: 'versionName', width: 80 },
            { label: '更新意愿', name: 'updateStrategy', width: 80 ,hidden:true},
            { label: '更新意愿', name: 'updateStrategyName', width: 80 ,formatter: function(v, options, row){
                var value = row.updateStrategy;
                var text = '';
                if(value == 0)text= '推荐升级';
                else if(value == 1)text= '强制升级';
                return text;
            }},
            { label: '下载地址', name: 'downloadUrl', width: 80 },
            { label: '更新详情', name: 'updateDetail', width: 80 },
            { label: 'MD5值', name: 'md5', width: 80 },
            { label: '是否过期', name: 'isActive', width: 80 ,hidden:true},
            { label: '是否过期', name: 'isActiveName', width: 80 ,hidden:true,formatter: function(v, options, row){
                var value = row.isActive;
                var text = '';
                if(value == 0)text= '过期版本';
                else if(value == 1)text= '当前版本';
                return text;
            }},
            { label: '是否需要灰度升级', name: 'isGreyUpdate', width: 80 ,hidden:true},
            { label: '是否需要灰度升级', name: 'isGreyUpdateName', width: 80 ,hidden:true ,formatter: function(v, options, row){
                var value = row.isGreyUpdate;
                var text = '';
                if(value == 0)text= '不需要灰度升级';
                else if(value == 1)text= '需要灰度升级';
                return text;
            }},
            { label: '状态', name: 'status', width: 80,hidden:true },
            { label: '状态', name: 'statusName', width: 80 ,formatter: function(v, options, row){
                var value = row.status;
                var text = '';
                if(value == '0,0')text= '下线';
                else if(value =='1,0' )text= '正式线';
                else if(value == '1,1')text= '灰度升级';
                return text;
            }},
            { label: '灰度用户列表', name: 'updateUserList', width: 80 },
            { label: '业务线标识', name: 'schoolId', width: 80 ,hidden:true},
            { label: '记录创建者名', name: 'createPersonName', width: 80 },
            { label: '记录创建者', name: 'createPerson', width: 80 ,hidden:true},
            { label: '记录修改者名', name: 'modifyPersonName', width: 80 },
            { label: '记录修改者', name: 'modifyPerson', width: 80 ,hidden:true},
            { label: '创建时间', name: 'createTime', width: 80 },
            { label: '修改时间', name: 'modifyTime', width: 80 }

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
    data:{
        showList: true,
        title: null,
        sysVersion: {
            status:"",
            minVersionCode:1
        },
        appTypeOptions: [
            { text: '自考APP',value: 'zikao'},
            { text: '会计APP',value: 'kuaiji'},
            { text: '员工端APP',value: 'teacher'}
        ],
        clientTypeOptions: [
            { text: 'ios' },
            { text: 'android'}
        ],
        updateStrategyOptions: [
            { text: '推荐升级' ,value: '0'},
            { text: '强制升级',value: '1'}
        ],
        q : {//查询条件
            clientType:"",
            appType:""
        },
        qClientTypeOptions: [
            { text: 'ios' },
            { text: 'android'}
        ],
        statusOptions: [
            { text: '下线',value:'0,0'},
            { text: '上线',value:'1,0'},
            { text: '灰度升级',value:'1,1'}
        ]
    },
    methods: {
        query: function () {
            vm.reload(1);
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.sysVersion = {
                minVersionCode:1
            };
        },
        update: function (event) {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            if(vm.title == "新增")
            {
                url = "../sysversion/save";
            }
            else if(vm.title == "修改")
            {
                url = "../sysversion/update";
            }else
            {
                url = "";
            }
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.sysVersion),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(index){
                            vm.reload(1);
                        });
                    }else{
                        alert(r.msg);
                    }
                }
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
                    url: "../sysversion/delete",
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
                url: "/sysversion/info/" + id,
                success: function(r){
                    if(r.code === 0){
                        vm.sysVersion = r.data;
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        clearQuery : function(){//清空查询条件
            var vm = this;
            vm.q.clientType = "";
            vm.q.appType = "";
        },
        reload: function (p) {
            vm.showList = true;
            var page = p || $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData : vm.q,
                page:page
            }).trigger("reloadGrid");
        }

    }
});