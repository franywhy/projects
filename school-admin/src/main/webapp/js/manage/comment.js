$(function () {
    $(".datetimepicker").datetimepicker({
        format: 'yyyy-mm-dd hh:ii:00',
        zIndex : 999999999,
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
    });
    $("#jqGrid").jqGrid({
        url: '../comment/list',
        datatype: "json",
        colModel: [
            { label: 'commentId', name: 'commentId', hidden: true, key: true },
            { label: '评论对象ID', name: 'commentObject', width: 80 },
            { label: '评论类型', name: 'commentType', width: 80, formatter: function(value, options, row){
                var text = '';
                if(value == 0)text='<span class="label label-success">会计头条</span>';
                return text; }
            },
            { label: '内容', name: 'content', width: 80 },
            { label: '用户ID', name: 'userId', width: 80 },
            { label: '用户昵称', name: 'name', width: 80 },
            { label: '用户头像', name: 'avatar', width: 50 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}},
            { label: '点赞数量', name: 'likeNumber', width: 80 },
            { label: '上架状态', name: 'appStatus', width: 80, formatter: function(value, options, row){
                return value === 0 ?
                    '<span class="label label-danger">下架</span>' :
                    '<span class="label label-success">上架</span>';
            }}
        ],
        viewrecords: true,
        height : 500,
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
        q:{
            content:null
        },
        showList: true,
        title: null,
        comment: {
            commentId:"",
            commentObject:"",
            content:"",
            userId:"",
            name:"",
            avatar:"",
            likeNumber:""
        }
    },
    methods: {
        selectTeacher: function(){
            teacherLay.show(function(index,opt){
//				console.log(opt);
                //授课老师id
                vm.comment.teacherId = opt.userId;
                //授课老师名称
                vm.comment.teacherName = opt.nickName;
            });
        },
        selectLiveroom: function(){
            liveroomLay.show(function(index,opt){
                console.log(opt);
                //直播间id
                vm.comment.liveRoomId = opt.liveRoomId;
                //直播间名称
                vm.comment.liveRoomName = opt.liveRoomName;
            });
        },
        /*selectVideo: function(){
         polyvVideoLay.show(function(index,opt){
         console.log(opt);
         });
         },*/
        query: function () {
            vm.reload(null , 1);
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.comment = {
                commentId:"",
                commentObject:"",
                content:"",
                userId:"",
                name:"",
                avatar:"",
                likeNumber:""
            };
            $("#detailStartTime").val("");//新增时让开始时间置空
            $("#detailEndTime").val("");//新增时让结束时间置空
        },
        update: function (event) {
            var commentId = getSelectedRow();
            if(commentId == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(commentId)
        },
        saveOrUpdate: function (event) {
            //做新增或保存时获取开始时间的值
            vm.comment.commentStartTime = $("#detailStartTime").val();
            //做新增或保存时获取结束时间的值
            vm.comment.commentEndTime = $("#detailEndTime").val();
            if($.isNull(vm.comment.orderNum)){
                alert("请输入排序！！！")
                return;
            }
            if($.isNull(vm.comment.title)){
                alert("请输入标题！！！")
                return;
            }
            if($.isNull(vm.comment.subtitle)){
                alert("请输入副标题！！！");
                return;
            }
            if($.isNull(vm.comment.cardUrl)){
                alert("请输入卡片地址！！！");
                return;
            }
            if($.isNull(vm.comment.cardBannerUrl)){
                alert("请选择封面！！！");
                return;
            }
            if($.isNull(vm.comment.labels)){
                alert("请选择标签！！！");
                return;
            }
            if($.isNull(vm.comment.productId)){
                alert("请选择产品线！！！");
                return;
            }
            if(!$.isNull(vm.comment.title) && vm.comment.title.length > 100){
                alert("标题信息不得超过100个字！！！");
                return;
            }
            if(vm.title == "新增")
            {
                url = "../comment/save";
            }
            else if(vm.title == "修改")
            {
                url = "../comment/update";
            }else
            {
                url = "";
            }

            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.comment),
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
        del: function (event) {
            var commentIds = getSelectedRows();
            if(commentIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../comment/delete",
                    data: JSON.stringify(commentIds),
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
        getInfo: function(commentId){
            //查询信息时获取并显示开始时间的值
            $("#detailStartTime").val(vm.comment.commentStartTime);
            //查询信息时获取并显示结束时间的值
            $("#detailEndTime").val(vm.comment.commentEndTime);
            $.ajax({
                type: "POST",
                url: "/comment/info/" + commentId,
                success: function(r){
                    if(r.code === 0){
                        vm.comment = r.data;
                        //查询信息时获取并显示开始时间的值
                        $("#detailStartTime").val(vm.comment.commentStartTime);
                        //查询信息时获取并显示结束时间的值
                        $("#detailEndTime").val(vm.comment.commentEndTime);
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        reload: function (event , p) {
            vm.showList = true;
            var page = p||$("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{
                    "content" : vm.q.content,
                    "appStatus" : vm.q.appStatus
                },
                page:page
            }).trigger("reloadGrid");
        },
        resume:function(event){
            var commentIds = getSelectedRows();
            if(commentIds == null){
                return ;
            }

            confirm('确定要上架选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../comment/resume",
                    data: JSON.stringify(commentIds),
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
        pause:function(event){
            var commentIds = getSelectedRows();
            if(commentIds == null){
                return ;
            }

            confirm('确定要下架选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../comment/pause",
                    data: JSON.stringify(commentIds),
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
        selectProduct : function(){
            productLay.show(function(index,opt){
                vm.comment.productId = opt.productId;
                vm.comment.productName = opt.productName;
            });
        }
    }
});