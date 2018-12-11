$(function () {
    $(".datetimepicker").datetimepicker({
        format: 'yyyy-mm-dd hh:ii:00',
        zIndex : 999999999,
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
    });
    $("#jqGrid").jqGrid({
        url: '../headline/list',
        datatype: "json",
        colModel: [
            { label: 'headlineId', name: 'headlineId', hidden: true, key: true },
            { label: 'pushFindMsgId', name: 'pushFindMsgId', hidden: true },
            { label: '标题', name: 'title', width: 80 },
            { label: '副标题', name: 'subtitle', width: 80 },
            { label: '封面', name: 'cardBannerUrl', width: 50 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}},
            { label: '封面类型', name: 'cardBannerType', width: 80, formatter: function(value, options, row){
                var text = '';
                if(value == 1)text='大图';
                else if(value == 2)text='小图';
                return text; }
            },
            { label: '内容类型', name: 'contentType', width: 80, formatter: function(value, options, row){
                var text = '';
                if(value == 0)text='<span class="label label-default">视频</span>';
                else if(value == 1)text='<span class="label label-success">语音</span>';
                else if(value == 2)text='<span class="label label-primary">观点</span>';
                else if(value == 3)text='<span class="label label-danger">文章</span>';
                return text; }
            },
            { label: '资源时长', name: 'contentTotalTime', width: 80 },
            { label: '消息状态', name: 'appStatus', width: 80, formatter: function(value, options, row){
                var text = '';
                if(value == 0) text='<span class="label label-default">未推荐</span>';
                else if(value == 1) text='<span class="label label-success">推荐</span>';
                return text;
            }},
            { label: '推送状态', name: 'pushStatus', width: 80, formatter: function(value, options, row){
                var text = '';
                if(value == 0)text='<span class="label label-default">未推送</span>';
                else if(value == 1) {
                    if(new Date().getTime() > new Date(row.pushTime).getTime()) {
                        text='<span class="label label-success">已推送</span>';
                    } else {
                        text='<span class="label label-primary">预推送</span>';
                    }
                }
                else if(value == 2)text='<span class="label label-danger">推送失败</span>';
                return text; }
            },
            { label: '推送时间', name: 'pushTime', width: 80 },
            { label: '产品线', name: 'productName', width: 80 },
            { label: '创建用户', name: 'creationName', width: 80 },
            { label: '创建时间', name: 'creationTime', width: 80 },
            { label: '最近修改用户', name: 'modifiedName', width: 80 },
            { label: '最近修改日期', name: 'modifiedTime', width: 80 }
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
            title:null
        },
        showList: true,
        title: null,
        headline: {
            title:"",
            subtitle:"",
            cardBannerUrl:"",
            contentType:"",
            contentHtml:"",
            contentUrl:"",
            labels:"",
            readNumber:"",
            productId:"",
            productName:"",
            pushStatus:"",
            pushTime:"",
            appStatus:"",
            cardBannerType:1,
            contentTotalTime:""
        },
        typeOptions: [
            { text: '视频', value: 0 },
            { text: '语音', value: 1 },
            { text: '观点', value: 2 },
            { text: '文章', value: 3 }
        ],
        cardBannerTypeOptions: [
            { text: '大图', value: 1 },
            { text: '小图', value: 2 },
        ]
    },
    methods: {
        selectTeacher: function(){
            teacherLay.show(function(index,opt){
//				console.log(opt);
                //授课老师id
                vm.headline.teacherId = opt.userId;
                //授课老师名称
                vm.headline.teacherName = opt.nickName;
            });
        },
        selectLiveroom: function(){
            liveroomLay.show(function(index,opt){
                console.log(opt);
                //直播间id
                vm.headline.liveRoomId = opt.liveRoomId;
                //直播间名称
                vm.headline.liveRoomName = opt.liveRoomName;
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
            vm.headline = {
                title:"",
                subtitle:"",
                cardBannerUrl:"",
                contentType:"",
                voiceTitle:"",
                contentHtml:"",
                contentUrl:"",
                labels:"",
                readNumber:"",
                productId:"",
                productName:"",
                pushStatus:"",
                pushTime:"",
                appStatus:"",
                cardBannerType:1,
                contentTotalTime:""
            };
            $("#detailStartTime").val("");//新增时让开始时间置空
            $("#detailEndTime").val("");//新增时让结束时间置空
            editor.html("");
        },
        update: function (event) {
            var headlineId = getSelectedRow();
            if(headlineId == null){
                return ;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(headlineId)
        },
        saveOrUpdate: function (event) {
            //取得HTML内容
            var contentHtml = editor.html();
            // 同步数据后可以直接取得textarea的value
            editor.sync();
            contentHtml = $('#editor_id').val(); // jQuery
            // 设置HTML内容
            /*editor.html('HTML内容');*/
            vm.headline.contentHtml = contentHtml;

            //做新增或保存时获取开始时间的值
            vm.headline.headlineStartTime = $("#detailStartTime").val();
            //做新增或保存时获取结束时间的值
            vm.headline.headlineEndTime = $("#detailEndTime").val();
            if($.isNull(vm.headline.title)){
                alert("请输入标题！！！")
                return;
            }
            if($.isNull(vm.headline.subtitle)){
                alert("请输入副标题！！！");
                return;
            }
            if($.isNull(vm.headline.cardBannerUrl)){
                alert("请选择封面！！！");
                return;
            }
            if($.isNull(vm.headline.labels)){
                alert("请选择标签！！！");
                return;
            }
            if(vm.headline.labels.split(",").length > 6){
                alert("最多只能选择5个标签！！！");
                return;
            }
            if($.isNull(vm.headline.productId)){
                alert("请选择产品线！！！");
                return;
            }
            if($.isNull(vm.headline.contentType)){
                alert("请选择文章类型！！！");
                return;
            }
            if(vm.headline.contentType == 1 && $.isNull(vm.headline.voiceTitle)) {
                alert("请输入语音标题！！！");
                return;
            }
            if(!$.isNull(vm.headline.title) && vm.headline.title.length > 100){
                alert("标题信息不得超过100个字！！！");
                return;
            }
            if(vm.title == "新增")
            {
                url = "../headline/save";
            }
            else if(vm.title == "修改")
            {
                url = "../headline/update";
            }else
            {
                url = "";
            }

            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.headline),
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
            var headlineIds = getSelectedRows();
            if(headlineIds == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../headline/delete",
                    data: JSON.stringify(headlineIds),
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
        getInfo: function(headlineId){
            //查询信息时获取并显示开始时间的值
            $("#detailStartTime").val(vm.headline.headlineStartTime);
            //查询信息时获取并显示结束时间的值
            $("#detailEndTime").val(vm.headline.headlineEndTime);
            $.ajax({
                type: "POST",
                url: "/headline/info/" + headlineId,
                success: function(r){
                    if(r.code === 0){
                        vm.headline = r.data;
                        editor.html(vm.headline.contentHtml);
                        //查询信息时获取并显示开始时间的值
                        $("#detailStartTime").val(vm.headline.headlineStartTime);
                        //查询信息时获取并显示结束时间的值
                        $("#detailEndTime").val(vm.headline.headlineEndTime);
                        /*if(vm.headline.contentType == 0){
                         $("#contentUrl-div").css('display','block');
                         } else {
                         $("#contentUrl-div").css('display','none');
                         }*/
                        if(vm.headline.contentType == 1){
                            $("#voiceTitle-div").css('display','block');
                            //$("#voiceUrl-div").css('display','block');
                        } else {
                            $("#voiceTitle-div").css('display','none');
                            // $("#voiceUrl-div").css('display','none');
                        }
                        if(vm.headline.contentType == 0 || vm.headline.contentType == 1 ){
                            $("#contentTotalTime-div").css('display','block');
                            $("#contentUrl-div").css('display','block');
                        } else {
                            $("#contentTotalTime-div").css('display','none');
                            $("#contentUrl-div").css('display','none');
                        }
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
                postData:{"title" : vm.q.title},
                page:page
            }).trigger("reloadGrid");
        },
        resume:function(event){
            var headlineIds = getSelectedRows();
            if(headlineIds == null){
                return ;
            }
            confirm('确定要上架选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../headline/resume",
                    data: JSON.stringify(headlineIds),
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
            var headlineIds = getSelectedRows();
            if(headlineIds == null){
                return ;
            }
            confirm('确定要下架选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../headline/pause",
                    data: JSON.stringify(headlineIds),
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
                vm.headline.productId = opt.productId;
                vm.headline.productName = opt.productName;
            });
        },
        pushRulesLayerShow : function(){//推送规则浮层
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            //行详情
            var row = $("#jqGrid").jqGrid('getRowData',id);
            /*消息类型 3：公开课 4：自定义卡片 9：系统消息 10：系统消息*/
            pushRulesLay.show(10, id, row.pushTime, function(index,opt){
            });
        },
        labelLayerShow : function(){//标签浮层
            appLabelMsLay.show(function(selectIds , selectDetails){
                vm.headline.labels = "";
                selectDetails.forEach(function(value, index, array) {
                    vm.headline.labels = vm.headline.labels + value.labelName + ",";
                });
            });
        },
        delMessage: function () {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            /*消息类型 3：公开课 4：自定义卡片 9：系统消息 10：会计头条*/
            //行详情
            var row = $("#jqGrid").jqGrid('getRowData',id);
            confirm('确定要取消推送选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../pushrules/delMessage",
                    data: JSON.stringify({
                        "messageType":10,
                        "messageId":id,
                        "pushFindMsgId":row.pushFindMsgId
                    }),
                    success: function(r){
                        if(r.code == 0){
                            alert(r.msg, function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        updateMsgRecommend: function (msgRecommend) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            //行详情
            var row = $("#jqGrid").jqGrid('getRowData',id);
            var text = "确定要推荐选中的记录？"
            if (msgRecommend == 0){
                text = "确定要取消推荐选中的记录?"
            }
            confirm(text, function(){
                $.ajax({
                    type: "POST",
                    url: "../pushrules/updateMsgRecommend",
                    data: JSON.stringify({
                        "messageType":10,
                        "messageId":id,
                        "pushFindMsgId":row.pushFindMsgId,
                        "msgRecommend":msgRecommend
                    }),
                    success: function(r){
                        if(r.code == 0){
                            alert(r.msg, function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        contentTypeChange : function(){
            //内容类型  0：视频，1：语音，2：观点，3：文章
            /*if(vm.headline.contentType == 0){
             $("#contentUrl-div").css('display','block');
             } else {
             $("#contentUrl-div").css('display','none');
             }*/
            if(vm.headline.contentType == 1){
                $("#voiceTitle-div").css('display','block');
                //$("#voiceUrl-div").css('display','block');
            } else {
                $("#voiceTitle-div").css('display','none');
               // $("#voiceUrl-div").css('display','none');
            }
            if(vm.headline.contentType == 0 || vm.headline.contentType == 1 ){
                $("#contentTotalTime-div").css('display','block');
                $("#contentUrl-div").css('display','block');
            } else {
                $("#contentTotalTime-div").css('display','none');
                $("#contentUrl-div").css('display','none');
            }
        }
    }
});