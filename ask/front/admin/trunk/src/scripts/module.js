/**
 * 恒企志文章档案 annals
 */
$.module("Izb.controller.annalsdetail",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '恒企志文章档案',
            itemName : '恒企志文章',
            key  : 'annalsdetail',
            autoLoad:true
        },
        //接口
        action:{
            list:'/annalsdetail/list',
            add:'/annalsdetail/add',
            edit:'/annalsdetail/edit',
            del:'/annalsdetail/del',
            to_unable:'/annalsdetail/to_unable',
            to_able:'/annalsdetail/to_able',
            annalsList:'/annalsdetail/annalsList'
        },
        //模板Id
        tpl:{
            content:'tpl-annalsdetail-list',
            header:'tpl-annalsdetail-header',
            input:'tpl-annalsdetail-input'
        },
        event:{
            onBeforeList : function(){
                //初始化下拉列表数据
                Izb.controller.annalsdetail.getAnnalsList(Izb.controller.annalsdetail.initListAnnalsList);
            },
            onAddRender : function(){
                //初始化下拉列表数据
                Izb.controller.annalsdetail.getAnnalsList(Izb.controller.annalsdetail.initInputAnnalsList);
                //初始化html编辑器
                Izb.controller.annalsdetail.editor = KindEditor.create('#content_html',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                    this.loadPlugin('autoheight');
                }});
            },
            onBeforeSaveEdit :function(data){
                data.content_html = Izb.controller.annalsdetail.editor.html();
            },
            onBeforeAdd :function(data){
                data.content_html = Izb.controller.annalsdetail.editor.html();
            },
            onEditRender : function(data){
                Izb.controller.annalsdetail.editor = KindEditor.create('#content_html',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                    this.loadPlugin('autoheight');
                }});
            },
            onBeforeEdit : function(){
                //初始化下拉列表数据
                Izb.controller.annalsdetail.getAnnalsList(Izb.controller.annalsdetail.initInputAnnalsList);
            }

        }
    },{
        //禁用
        to_unable : function(id){
            this.ableUpdate(id , this.action.to_unable);
        },
        //启用
        to_able : function(id){
            this.ableUpdate(id , this.action.to_able);
        },
        //启用禁用
        ableUpdate:function(id , url){
            Izb.common.getResult({
                type: 'POST',
                action:url,
                data: {"_id" : id},
                success:function(result){
                    Izb.ui.tips("操作成功！",'succeed');
                    Izb.main.refresh();
                }
            });
        },

        /**
         * 专题列表
         */
        annalsList: null,
        /**
         * 获取专题列表
         * @param callblack 回调方法
         */
        getAnnalsList : function(callblack){
            var that = this;
            if(null == that.annalsList || 0 == that.annalsList.length){
                Izb.common.getResult({
                    action: that.action.annalsList,
                    data: null,
                    async:false,
                    success: function (result) {
                        if(result != null) {
                            that.annalsList = result.data;
                            if($.isFunction(callblack)){
                                callblack(that.annalsList);
                            }
                        }
                    },
                    error: function (xhr, status, result) {}
                });
            }else if($.isFunction(callblack)){
                callblack(that.annalsList);
            }
            return that.annalsList;
        },
        /**
         * 初始化查询的专题列表
         * @param data
         */
        initListAnnalsList : function(data){
            if(null != data && 0 < data.length){
                var str = '<option value="" selected>==所有==</option>';
                $.each(data , function(i , v){
                    str += '<option value="'+v._id+'">'+ (v.status == 1 ? "(启用)" : "(禁用)") + v.title+'</option>';
                });
                $("#annals_id").empty().html(str);
            }
        },
        /**
         * 初始化编辑总裁列表
         * @param data
         */
        initInputAnnalsList : function(data){
            if(null != data && 0 < data.length){
                var str = '';
                $.each(data , function(i , v){
                    str += '<option value="'+v._id+'">'+ (v.status == 1 ? "(启用)" : "(禁用)") + v.title+'</option>';
                });
                $("#annals_id").empty().html(str);
            }
        }
    });
});

/**
 * 恒企志专题档案 annals
 */
$.module("Izb.controller.annals",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '恒企志封面档案',
            itemName : '恒企志封面',
            key  : 'annals',
            autoLoad:true
        },
        //接口
        action:{
            list:'/annals/list',
            add:'/annals/add',
            edit:'/annals/edit',
            del:'/annals/del',
            to_unable:'/annals/to_unable',
            to_able:'/annals/to_able',
        },
        //模板Id
        tpl:{
            content:'tpl-annals-list',
            header:'tpl-annals-header',
            input:'tpl-annals-input'
        },
        event:{
        }
    },{
        to_unable : function(id){
            this.ableUpdate(id , this.action.to_unable);
        },
        to_able : function(id){
            this.ableUpdate(id , this.action.to_able);
        },
        ableUpdate:function(id , url){
            Izb.common.getResult({
                type: 'POST',
                action:url,
                data: {"_id" : id},
                success:function(result){
                    Izb.ui.tips("操作成功！",'succeed');
                    Izb.main.refresh();
                }
            });
        }
    });
});


/**
 * 中级直播出勤档案:middlelivedetail
 */
$.module("Izb.controller.middlelivedetail",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '中级直播出勤档案',
            itemName : '中级直播出勤档案',
            key  : 'middlelivedetail',
            autoLoad:false
        },
        //接口
        action:{
            list:'/middlelivedetail/list',
            midCourseList:'/middlelivedetail/midCourseList',
            camidrecord:'/middlelivedetail/camidrecord',
        },
        //模板Id
        tpl:{
            content:'tpl-middlelivedetail-list',
            header:'tpl-middlelivedetail-header'
        },
        event:{
            onBeforeList : function(data){
                this.getMidPlanList(this.getMidPlanList);
//下拉初始化
//                 if (!data.stime) {
//                     data.stime = Izb.ui.getTodayDate();
//                     $("#create_day").val(data.stime);
//                 }
            }
        }
    },{
        ca_watchtime:function( comid,coursetime_id) {
            Izb.common.getResult({
                type: 'GET',
                action: this.action.camidrecord,
                data: {"com_id": comid, "course_time_id": coursetime_id},
                success: function (result) {

                    alert("计算完毕，请刷新页面");
                }
            });
        },
        // selCourseName : "",
        // selCourseTimeName : "",
        //中级排课计划列表
        midPlanList : null,
        getMidPlanList: function(callblack){//初始化排课计划的数据
            var that = this;
            if(null == that.midPlanList || 0 == that.midPlanList.length){
                Izb.common.getResult({
                    action: that.action.midCourseList,
                    data: null,
                    async:false,
                    success: function (result) {
                        if(result != null) {
                            that.midPlanList = result.data;
                            //初始化排课计划下拉列表
                            that.initCourseSel();
                        }
                    },
                    error: function (xhr, status, result) {
                    }
                });
            }
        },
        initCourseSel : function(){//初始化排课计划下拉列表
            var that = this;
            if(that.midPlanList){
                var str = "";
                $.each(that.midPlanList , function(i , v){
                    var sname = v.name + "--" + v.course_name ;
                    str += '<option value="'+v.nc_id+'">'+ sname +  '</option>';
                });
                $("#course_id").html(str);
                that.courseOnChange();
            }
        },
        courseOnChange:function(){//初始化排课计划的直播课下拉列表
            $("#pid").empty();
            var that= this;
            var midItem = that.midPlanList[$("#course_id ").get(0).selectedIndex];
            var timeList = midItem.courseList;
            var str = "";
            if(timeList){
                $.each(timeList , function(i , v){
                    var selName = v.content + "[" + (v.live_start_time || "~") + "]";
                    str += '<option value="'+v.nc_id+'" data-item="'+i+'">'+ selName +  '</option>';
                });
            }
            $("#pid").html(str);
            // that.courseTimeOnChange();
        }
    });

});

/**
 * 总裁语录:bossana
 */
$.module("Izb.controller.bossana",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '总裁语录',
            itemName : '总裁语录',
            key  : 'bossana'
        },
        //接口
        action:{
            list:'/bossana/list',
            add:'/bossana/add',
            edit:'/bossana/edit',
            del:'/bossana/del',
            bossList:'/bossana/bossList'
        },
        //模板Id
        tpl:{
            content:'tpl-bossana-list',
            header:'tpl-bossana-header',
            input:'tpl-bossana-input'
        },
        event:{
            onBeforeList : function(){
                // this.getBossList(this.initListBossList);
            },
            onAddRender:function(){
                Izb.controller.bossana.getBossList(Izb.controller.bossana.initInputBossList);
            },
            onBeforeEdit : function(){
                Izb.controller.bossana.getBossList(Izb.controller.bossana.initInputBossList);
            }
        }
    },{
        /**
         * 总裁列表
         */
        bossList: null,
        /**
         * 获取总裁列表
         * @param callblack 回调方法
         * @returns {MAP}
         */
        getBossList : function(callblack){
            var that = this;
            if(null == that.bossList || 0 == that.bossList.length){
                Izb.common.getResult({
                    action: that.action.bossList,
                    data: null,
                    async:false,
                    success: function (result) {
                        if(result != null) {
                            that.bossList = result.data;
                            if($.isFunction(callblack)){
                                callblack(that.bossList);
                            }
                        }
                    },
                    error: function (xhr, status, result) {
                    }
                });
            }else if($.isFunction(callblack)){
                callblack(that.bossList);
            }
            return that.bossList;
        },
        /**
         * 初始化查询的总裁列表
         * @param data
         */
        initListBossList : function(data){
            if(null != data && 0 < data.length){
                var str = '<option value="" selected>==所有==</option>';
                $.each(data , function(i , v){
                    str += '<option value="'+v._id+'">'+v.boss_name+'</option>';
                });
                $("#qboss_id").empty().html(str);
            }
        },
        /**
         * 初始化编辑总裁列表
         * @param data
         */
        initInputBossList : function(data){
            if(null != data && 0 < data.length){
                var str = '';
                $.each(data , function(i , v){
                    str += '<option value="'+v._id+'">'+v.boss_name+'</option>';
                });
                $("#boss_id").empty().html(str);
            }
        }
    });

});

/**
 * 总裁档案:servicehome
 */
$.module("Izb.controller.bossrecord",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '总裁档案',
            itemName : '总裁档案',
            key  : 'bossrecord'
        },
        //接口
        action:{
            list:'/bossrecord/list',
            add:'/bossrecord/add',
            edit:'/bossrecord/edit',
            del:'/bossrecord/del'
        },
        //模板Id
        tpl:{
            content:'tpl-bossrecord-list',
            header:'tpl-bossrecord-header',
            input:'tpl-bossrecord-input'
        },
        event:{
        }
    },{
    });

});


/**
 * 上门服务:servicehome
 */
$.module("Izb.controller.servicehome",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '上门服务',
            itemName : '上门服务',
            key  : 'servicehome'
        },
        //接口
        action:{
            list:'/servicehome/list'
        },
        //模板Id
        tpl:{
            content:'tpl-servicehome-list',
            header:'tpl-servicehome-header',
            input:'tpl-servicehome-input'
        },
        event:{
            onBeforeEdit : function(data){
                data.timestamp = new Date(data.timestamp).toLocaleString();
            }
        }
    },{
    });

});

/**
 * 文章管理:articles
 */
$.module("Izb.controller.articles",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '教师文章',
            itemName : '教师文章',
            key  : 'articles'
        },
        //接口
        action:{
            list:'/articles/list',
            add:'/articles/add',
            edit:'/articles/edit',
            del:'/articles/del'
        },
        //模板Id
        tpl:{
            content:'tpl-articles-list',
            header:'tpl-articles-header',
            input:'tpl-articles-input'
        },
        event:{
            onAddRender : function(){
                Izb.controller.articles.editor = KindEditor.create('#article_html',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                    this.loadPlugin('autoheight');
                }});
            },
            onBeforeSaveEdit :function(data){
                data.article_html = Izb.controller.articles.editor.html();
            },
            onBeforeAdd :function(data){
                data.article_html = Izb.controller.articles.editor.html();
            },
            onEditRender : function(data){
                Izb.controller.articles.editor = KindEditor.create('#article_html',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                    this.loadPlugin('autoheight');
                }});
            }
        }
    },{
    });

});


/**
 * 考试地址管理:provinceexam
 */
$.module("Izb.controller.provinceexam",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '考试查询地址管理',
            itemName : '考试查询地址',
            key  : 'provinceexam'
        },
        //接口
        action:{
            list:'/provinceexam/list',
            add:'/provinceexam/add',
            edit:'/provinceexam/edit',
            del:'/provinceexam/del',
            getprovince:'/provinceexam/getprovince'
        },
        //模板Id
        tpl:{
            content:'tpl-provinceexam-list',
            header:'tpl-provinceexam-header',
            input:'tpl-provinceexam-input'
        },
        event:{
            onBeforeList : function(){
                this.getProvince(this.initListProvince);
            },
            onAddRender:function(){
                Izb.controller.provinceexam.getProvince(Izb.controller.provinceexam.initInputProvince);
            },
            onBeforeEdit : function(){
                Izb.controller.provinceexam.getProvince(Izb.controller.provinceexam.initInputProvince);

                $("#code").attr("disabled","disabled");
                $("#type").attr("disabled","disabled");
            }
        }
    },{
        tname : function(type){
            var name = "";
            switch (type){
                case 0 : name = '会计从业考试'; break;
                case 1 : name = '初级会计师考试'; break;
                case 2 : name = '中级会计师考试'; break;
                case 3 : name = '注册会计师'; break;
                default : break;
            }
            return name;
        },
        /**
         * 省份数据
         */
        provinceData : null,
        /**
         * 获取省份数据
         * @param callblack 回调方法
         * @returns {MAP}   身份数据
         */
        getProvince : function(callblack){
            var that = this;
            if(null == that.provinceData || 0 == that.provinceData.length){
                Izb.common.getResult({
                    action: that.action.getprovince,
                    data: null,
                    async:false,
                    success: function (result) {
                        if(result != null) {
                            that.provinceData = result.data;
                            if($.isFunction(callblack)){
                                callblack(that.provinceData);
                            }
                        }
                    },
                    error: function (xhr, status, result) {
                    }
                });
            }else if($.isFunction(callblack)){
                callblack(that.provinceData);
            }
            return that.provinceData;
        },
        /**
         * 初始化查询的省份列表
         * @param data
         */
        initListProvince : function(data){
            if(null != data && 0 < data.length){
                var str = '<option value="" selected>==所有==</option>';
                $.each(data , function(i , v){
                    str += '<option value="'+v.code+'">'+v.name+'</option>';
                });
                $("#qcode").empty().html(str);
            }
        },
        /**
         * 初始化编辑省份列表
         * @param data
         */
        initInputProvince : function(data){
            if(null != data && 0 < data.length){
                var str = '';
                $.each(data , function(i , v){
                    str += '<option value="'+v.code+'">'+v.name+'</option>';
                });
                $("#code").empty().html(str);
            }
        }
    });

});



/**
 * 初始化数据
 */
$.module("Izb.controller.initdata",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '初始化数据',
            itemName : '初始化数据',
            key  : 'initdata'
        },
        //接口
        action:{
            list:'/initdata/list'
        },
        //模板Id
        tpl:{
            content:'tpl-initdata-list'
        },
        event:{
        }
    },{
        tomethod : function(url){
            Izb.common.getResult({
                type: 'POST',
                action:url,
                data: {},
                success:function(result){
                    Izb.ui.tips("操作成功！",'succeed');
                }
            });
        }
    });

});

/*
 * 用户详细信息
 */
$.module("Izb.controller.userds",function(){
    return new TabListController({
        pty:{
            name : '用户详情',
            itemName : '用户',
            key  : 'userds',
            menu  : 'user'
        },
        //接口
        action:{
            "show":"/user/show",
            "pay_log":"/finance/list",
            "send_gift": "/user/cost_log",
            "rec_gift": "/user/gift_rec",
            "song":"/user/cost_log",
            "broadcast":"/user/cost_log",
            "buy_vip":"/user/cost_log",
            "buy_car":"/user/cost_log",
            "grab_sofa":"/user/cost_log",
            "luck_log":"/user/luck_log",
            "exchange_log": "/user/exchange_log",
            "lottery_log": "/user/lottery_log",
            "label": "/user/cost_log",
            "special_gift": "/star/history_special_gift",
            "send_fortune": "/user/cost_log",
            "send_treasure": "/user/cost_log",
            "football_log": "/user/football_log",
            "egg_log": "/user/egg_log"
        },
        //模板Id
        tpl:{
            "header":"tpl-userds-header",
            "show":"tpl-user-show",
            "pay_log":"tpl-userds-pay_log",
            "send_gift": "tpl-userds-send_gift",
            "rec_gift":"tpl-userds-rec_gift",
            "song":"tpl-userds-song",
            "broadcast":"tpl-userds-broadcast",
            "buy_vip":"tpl-userds-buy_vip",
            "buy_car":"tpl-userds-buy_car",
            "grab_sofa":"tpl-userds-grab_sofa",
            "luck_log":"tpl-userds-luck_log",
            "exchange_log": "tpl-stards-exchange_log",
            "lottery_log": "tpl-userds-lottery_log",
            "label": "tpl-userds-lable_log",
            "special_gift": "tpl-userds-special_gift",
            "send_fortune": "tpl-userds-send_fortune",
            "send_treasure": "tpl-userds-send_treasure",
            "football_log": "tpl-userds-football_log",
            "egg_log": "tpl-userds-egg_log"
        },
        event: {
            onBeforeList: function (data) {

//                Izb.controller.medal.getAllMedals();

                if (!data.stime) {
                    data.stime = Izb.ui.getFirstDay();
                    $("#stime").val(data.stime);
                }
            }
        }
    }, {
        prizeCollection : {
            FaLali7Day : '法拉利座驾7天',
            AiChina10 : '我爱中国10个',
            MeiGui200 : '玫瑰200支',
            LaBa10 : '喇叭10个',
            GuoQing1 : '大阅兵1个',
            AiHuoShang1 : '爱的火山1个',
            ZhanDouJi7Day: '战斗机座驾7天',
            KouHong10: '10个口红',
            LaBa1: '1个喇叭',
            LanSe20: '20个蓝色妖姬',
            QinQin5: '5个亲亲',
            YanHua1: '1个浪漫烟花',
            Ferrari30Day: '1辆法拉利座驾(30天)',
            ZhangSheng5: '5个掌声',
            MeiGui10: '10个玫瑰',
            BingQiLin10: '10个冰淇淋',
            RedHeart1: '1个红心',
            ShuiJinXie1: '1个水晶鞋',
            Motorcycle30Day: '1辆摩托车座驾(30天)',
            Christmas15Day: '圣诞雪橇15天',
            Christmas30Day: '圣诞雪橇30天',

            DuoFuDuoShou: "多福多寿",
            GongXiFaCai: "恭喜发财",
            CaiYunHengTong: "财运亨通",
            XingFuMeiMan: "幸福美满",
            JiXiangRuYi: "吉祥如意",

            LongMaJingShen: "龙马精神",
            JunMaYingChun: "骏马迎春",
            WanMaBenTeng: "万马奔腾",
            MaDaoChengGong: "马到成功",
            JinMaNaFu: "金马纳福",
            "Car-NianShou": "年兽",
            "hongbao":"红包",

            "碎片1": "碎片1",
            "碎片2": "碎片2",
            "碎片3": "碎片3",
            "碎片4": "碎片4",
            "碎片5": "碎片5",
            "碎片6": "碎片6",
            "碎片7": "碎片7",
            "碎片8": "碎片8"

        }
    });
});




/*
 * poster:海报管理
 */
$.module("Izb.controller.poster",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '海报管理',
            itemName : '海报',
            key  : 'poster'
        },
        //接口
        action:{
            list:'/poster/list',
            add:"/poster/add",
            edit:"/poster/edit",
            del:"/poster/del"
        },
        //模板Id
        tpl:{
            header:'tpl-poster-header',
            content:'tpl-poster-list',
            input:'tpl-poster-input'
        },
        event:{
            onAddRender:function(){
            var params = $.parseParam(location.hash);
            $("#posterType").val(params.type||0);
        }
    }
    });

});




/*
 * notice:公告管理
 */
$.module("Izb.controller.notice",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '公告管理',
            itemName : '公告',
            key  : 'notice'
        },
        //接口
        action:{
            list:'/notice/list',
            add:"/notice/add",
            edit:"/notice/edit",
            del:"/notice/del"
        },
        //模板Id
        tpl:{
            header:'tpl-notice-header',
            content:'tpl-notice-list',
            input:'tpl-notice-input'
        },
        event:{
            onAddRender:function(){
                var params = $.parseParam(location.hash);
                $("#noticeType").val(params.type||0);
            }
        }
    });

});



/*
 * mission:任务管理
 */
$.module("Izb.controller.mission",function(){

    return new Controller({
        pty:{
            name : '任务管理',
            itemName : '任务',
            key  : 'mission'
        },
        //接口
        action:{
            list:'/mission/list',
            add:"/mission/add",
            edit:"/mission/edit",
            del:"/mission/del"
        },
        //模板Id
        tpl:{
            header:'tpl-common-header',
            content:'tpl-mission-list',
            input:'tpl-mission-input'
        }
    });

});


/*
 * gift:礼物管理
 */
$.module("Izb.controller.gift",function(){

    return new Controller({
        pty:{
            name : '礼物管理',
            itemName : '礼物',
            key  : 'gift'
        },
        //接口
        action:{
            list:'/gift/list',
            add:"/gift/add",
            edit:"/gift/edit",
            del:"/gift/del",
            fill_bag:"/sys/fill_bag" //发放礼物
        },
        //模板Id
        tpl:{
            header:'tpl-gift-header',
            content:'tpl-gift-list',
            input:'tpl-gift-input',
            fill_bag:'tpl-gift-fill_bag'
        },
        event: {
            onBeforeEdit: function (data) {
                if (data) {
                    data.star = data.star ? 1 : 0;
                    data.sale = data.sale ? 1 : 0;
                    data.isNew = data.isNew ? 1 : 0;
                    data.isHot = data.isHot ? 1 : 0;
                }
            }
        }

    },{
        //发放礼物
        fill_bag:function(giftId,giftName){
            var that = this;
            Izb.ui.showDialogByTpl(that.tpl.fill_bag, "发放礼物", null, function() {
                var data = Izb.ui.getFormData(that.formName);
                if(data.priv=="" && (data._id=="" || !$.parseBoolean($("#txtUserId").attr("data-success")))){
                    $("#txtUserId").focus();
                    return false;
                }
                var gifts = {},
                    pData={};
                gifts[data.gift_id]= parseInt(data.num);

                pData.gifts = JSON.stringify(gifts);
                pData.priv = data.priv;
                pData._id  = data._id;

                Izb.ui.confirm("确认要发放礼物吗？",function(){
                    Izb.common.getResult({
                        type: 'POST',
                        action:that.action.fill_bag,
                        data: pData,
                        success:function(result){
                            Izb.ui.tips("操作成功！",'succeed');
                            Izb.main.refresh();
                        }
                    });
                });
                return false;
            });

            Izb.ui.setFormData(that.formName,{gift_id:giftId,gift_name:giftName});
            Izb.ui.bindUserNameText("#txtUserId","#txtUserIdTip");

            $("#gift_priv").change(function(){
                if($(this).val()==""){
                    $("#customUserId").show();
                }else{
                    $("#customUserId").hide();
                }
            });


        }
    });

}(Izb));

/*
 * gift:礼物分类管理
 */
$.module("Izb.controller.giftcat",function(){

    return new Controller({
        pty:{
            name : '礼物分类',
            itemName : '礼物分类',
            key  : 'giftcat',
            menu  : 'giftcat'
        },
        //接口
        action:{
            list:'/giftcat/list',
            add:"/giftcat/add",
            edit:"/giftcat/edit",
            del:"/giftcat/del"
        },
        //模板Id
        tpl:{
            header:'tpl-gift-header',
            content:'tpl-giftcat-list',
            input:'tpl-giftcat-input'
        },
        event:{
            onBeforeEdit:function(data){
                $("#giftcat_id").attr("readonly",true);
                data.lucky = $.parseBoolean(data.lucky) ? 1:0;
                data.vip = $.parseBoolean(data.vip) ? 1:0;
            }
        }
    }, {
        allCateList:{},
        getAllCatList : function(){
            if($.isEmptyObject(this.allCateList)){
                var that = this;
                Izb.common.getResult({
                    action:that.action.list,
                    async:true,
                    data: {page:1,size:1000},
                    success:function(result){
                        var allCateList = {};
                        $.each(result.data, function(index,item){
                            allCateList[item._id] = item.name;
                        });
                        that.allCateList = allCateList;
                    }
                });
            } else {
                return this.allCateList;
            }
        }
    });

}(Izb));



/*
 * teacherapply:教师申请管理
 */
$.module("Izb.controller.teacherapply",function(){

    //初始化Controller
    return new Controller({
        size:200,
        pty:{
            name : '教师申请管理',
            itemName : '教师申请管理',
            key  : 'teacherapply'
        },
        //接口
        action:{
            list:'/teacherapply/list'
        },
        //模板Id
        tpl:{
            content:'tpl-teacherapply-list'
        },
        event:{

        }
    });

}(Izb));


/*
 * mission:任务管理
 */
$.module("Izb.controller.tkarticletype",function(){

    return new Controller({
        pty:{
            name : '题库文章类型管理',
            itemName : '题库文章类型',
            key  : 'tkarticletype'
        },
        //接口
        action:{
            list:'/tkarticletype/list',
            add:"/tkarticletype/add",
            edit:"/tkarticletype/edit",
            del:"/tkarticletype/del"
        },
        //模板Id
        tpl:{
            header:'tpl-common-header',
            content:'tpl-tkarticletype-list',
            input:'tpl-tkarticletype-input'
        }
    });

});




/*
 * sys:系统消息
 */
$.module("Izb.controller.sys", function () {
    return new Controller({
        size: 200,
        pty: {
            name: '系统消息管理',
            itemName: '系统消息',
            key: 'sys'
        },
        //接口
        action: {
            list: '/sys/list_inform',
            add: "/sys/add_inform",
            edit: "/sys/edit_inform",
            del: "/sys/del_inform"
        },
        //模板Id
        tpl: {
            header: 'tpl-common-header',
            content: 'tpl-inform-list',
            input: 'tpl-inform-input'
        },
        event: {
            onAddRender: function () {
                var params = $.parseParam(location.hash);
                //$("#blackwordType").val(params.type || 0);
            },
            onBeforeEdit: function (data, dialog) {
                data.stime = Izb.ui.formatTime(data.stime);
                data.etime = Izb.ui.formatTime(data.etime);
            }
        }
    });

}(Izb));



$.module("Izb.controller.client",function(){


    return new Controller({
        pty:{
            name : '客户端管理',
            itemName : '客户端信息',
            key : 'client',
            menu:'sys/show_download_info'
        },
        action:{
            list : '/sys/show_download_info', //查看
            edit : '/sys/save_download_info'  //保存
        },
        tpl:{
            header:'tpl-client-header',
            content:'tpl-client-list',
            input : 'tpl-client-input'
        }
    });

}(Izb));

/*
 * poster:通知消息管理
 */
$.module("Izb.controller.message", function () {

    //初始化Controller
    return new Controller({
        pty: {
            name: '消息通知管理',
            itemName: '消息通知',
            key: 'message'
        },
        //接口
        action: {
            list: '/message/list',
            add: "/message/add",
            edit: "/message/edit",
            del: "/message/del",
            send: "/message/send",
            selectuser:"/message/user_list.json",
            selectqc:"/message/qc_list.json",
            selectvideo:"/message/video_list.json"
        },
        //模板Id
        tpl: {
            header: 'tpl-common-header',
            content: 'tpl-message-list',
            input: 'tpl-message-input',
            selectuserform:"tpl-message-selectuser",
            selectqcform:"tpl-message-selectqc",
            selectvideoform:"tpl-message-selectvideo"
        },
        event: {
            onAddRender: function () {
                Izb.controller.message.selectState();
            },
            onEditRender: function () {
                Izb.controller.message.selectState();
            }
        }
    }, {
        send: function (data) {
            var that = this;
            Izb.ui.confirm("确认要发送消息吗？", function () {
                Izb.common.getResult({
                    action: that.action.send,
                    data: data,
                    success: function (result) {
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
            return false;
        },
        selectUserArray: [],
        inSumbitArray : function(val){//值是否存在sumbitArray
            //是否存在
            var item_id = -1;
            var sumArray = Izb.controller.message.selectUserArray;
            if(sumArray != null && sumArray.length > 0){
                for(var i = 0 ; i < sumArray.length ; i ++){
                    if(val == sumArray[i] ){
                        item_id = i;
                        break;
                    }
                }
            }
            return item_id;
        },
        selectqcId: "",
        selectvideoId: "",
        selectState:function(){
            $("#type").change(function () {
                if ($(this).val() == "0") {//用户组
                    $("#user_type").show();
                    $("#pnl_users").hide();
                    $("#message_adduser").hide();

                } else {
                    $("#pnl_users").show();
                    $("#message_adduser").show();
                    $("#user_type").hide();

                    //弹出用户选择框

                }
            }).change();
            $("#msg_action").change(function () {
                $("#message_content").show();
                if ($(this).val() == "tourl") {
                    $("#content_type_name").text("推广链接页面").show();
                    $("#content_type_name2").hide();
                    $("#message_addquestion").hide();
                    $("#message_content").show();
                    $("#message_video").hide();
                    $("#message_addvideo").hide();

                } else if($(this).val() == "donothing"){
                    $("#content_type_name").text("消息内容").hide();
                    $("#content_type_name2").hide();
                    $("#message_addquestion").hide();
                    $("#message_content").hide();
                    $("#message_video").hide();
                    $("#message_addvideo").hide();
                }
                else if($(this).val() == "mytopices_topic")
                {
                    $("#content_type_name").text("问题id").show();
                    $("#content_type_name2").hide();
                    $("#message_addquestion").show();
                    $("#message_content").show();
                    $("#message_video").hide();
                    $("#message_addvideo").hide();
                }
                else if($(this).val() == "mytest")
                {
                    $("#content_type_name").text("消息内容").hide();
                    $("#content_type_name2").hide();
                    $("#message_addquestion").hide();
                    $("#message_content").hide();
                    $("#message_video").hide();
                    $("#message_addvideo").hide();
                }
                else if($(this).val() == "live_detail")
                {
                    $("#content_type_name").text("直播间链接").show();
                    $("#content_type_name2").hide();
                    $("#message_addquestion").hide();
                    $("#message_content").show();
                    $("#message_video").hide();
                    $("#message_addvideo").hide();
                }
                else if($(this).val() == "video")
                {
                    $("#content_type_name").hide();
                    $("#content_type_name2").text("微课id").show();
                    $("#message_addquestion").hide();
                    $("#message_content").hide();
                    $("#message_video").show();
                    $("#message_addvideo").show();
                }
            }).change();

            $("#send").change(function () {
                if ($(this).val() == "2") {//用户组
                    $("#send_time_box").show();

                } else {
                    $("#send_time_box").hide();
                }
            }).change();

        },
        initSelectqc: function () {

            var dialog = Izb.ui.showDialogByTpl(this.tpl.selectqcform, "请选择推送的精彩问题", {  }, function () {

                $("#message_content").val(Izb.controller.message.selectqcId);
            });
            function operateFormatter(value, row, index) {
                return [
                    '<a class="remove ml10" href="javascript:void(0)"  title="Remove">',
                    '<i class="glyphicon glyphicon-remove">查看明细</i>',
                    '</a>'
                ].join('');
            }

            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.payment.delDetail(value, row, index);
                }
            },
                //添加付款单栏目
                $('#message-selectqc').bootstrapTable({
                    method: 'get',
                    url: Izb.controller.message.action.selectqc,
                    cache: false,
                    height: 400,
                    striped: true,
                    pagination: true,
                    pageSize: 20,
                    sidePagination: 'server',
                    pageList: [10, 25, 50, 100, 200],
                    showColumns: true,
                    search: true,
                    showRefresh: true,
                    minimumCountColumns: 2,
                    singleSelect: true,
                    clickToSelect: true,
                    columns: [
                        {
                            field: 'state',
                            checkbox: true
                        },
                        {
                            field: '_id',
                            title: '问题id',
                            align: 'right',
                            valign: 'bottom',
                            sortable: true
                        },
                        {
                            field: 'author_id',
                            title: '学生id',
                            align: 'center',
                            valign: 'middle',
                            sortable: true
                        },
                        {
                            field: 'author_name',
                            title: '学生昵称',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'teach_id',
                            title: '老师id',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'teach_name',
                            title: '老师昵称',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'content',
                            title: '问题内容',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'last_reply_content',
                            title: '最后一条回复',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'timestamp',
                            title: '提问时间',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        }

                    ],
                    onLoadSuccess: function (data) {


                    }
                })//添加的点击事件 并且清空之前点击的栏目
                    .on('check.bs.table', function (e, row) {
                        Izb.controller.message.selectqcId = row._id;
                    })
                    .on('uncheck.bs.table', function (e, row) {
                        Izb.controller.message.selectqcId = "";
                    })

        },
        initSelectUsers: function () {

            var dialog = Izb.ui.showDialogByTpl(this.tpl.selectuserform, "选择发送推送的用户", {  }, function () {
                $("#t_users").val("");
                var sumArray = Izb.controller.message.selectUserArray;
                var text = "";
                for(var i=0;i<sumArray.length;i++)
                {
                    if(i == sumArray.length-1)
                    {
                        text = text + sumArray[i];
                    }
                    else {
                        text = text + sumArray[i] + ",";
                    }
                }
                $("#t_users").val(text);


            });
            function operateFormatter(value, row, index) {
                return [
                    '<a class="remove ml10" href="javascript:void(0)"  title="Remove">',
                    '<i class="glyphicon glyphicon-remove">查看明细</i>',
                    '</a>'
                ].join('');
            }

            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.payment.delDetail(value, row, index);
                }
            },
                //添加付款单栏目
                $('#message-selectuser').bootstrapTable({
                    method: 'get',
                    url: Izb.controller.message.action.selectuser,
                    cache: false,
                    height: 400,
                    striped: true,
                    pagination: true,
                    pageSize: 20,
                    sidePagination: 'server',
                    pageList: [10, 25, 50, 100, 200],
                    showColumns: true,
                    search: true,
                    showRefresh: true,
                    minimumCountColumns: 2,
                    singleSelect: false,
                    clickToSelect: true,
                    columns: [
                        {
                            field: 'state',
                            checkbox: true
                        },
                        {
                            field: '_id',
                            title: '用户编号',
                            align: 'right',
                            valign: 'bottom',
                            sortable: true
                        },
                        {
                            field: 'nick_name',
                            title: '真实姓名',
                            align: 'center',
                            valign: 'middle',
                            sortable: true
                        },
                        {
                            field: 'priv',
                            title: '用户类型',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'province',
                            title: '省份',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'city',
                            title: '城市',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'vlevel',
                            title: '是否VIP',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        }

                    ],
                    onLoadSuccess: function (data) {

//                        var sumArray = Izb.controller.message.selectUserArray;
//                        var showData = data.data;
//                        if(sumArray != null && sumArray.length > 0 && showData != null && showData.length > 0){
//                            //循环数据源
//                            for(var i = 0 ; i < showData.length ; i++){
//                                //每行
//                                var row = showData[i];
//                                //判断applyFlowId是否在本地选中过
//                                var item_id = Izb.controller.message.inSumbitArray(row._id);
//                                //选中后默认选择
//                                if (item_id > -1) {
//                                    //数据中默认选中
//                                    row.state = true;
//
//                                    //视图中默认选中
//                                    $(".aui_border").find("input[name='btSelectItem']")[i].checked = true;
//                                }
//                            }
//                        }


                    }
                })//添加的点击事件 并且清空之前点击的栏目
                    .on('check.bs.table', function (e, row) {
                        var iscontain = false;
                        var sumArray = Izb.controller.message.selectUserArray;
                        var item_id = Izb.controller.message.inSumbitArray(row._id);
                        if (item_id == -1) {
                            sumArray.push(row._id);
                        }
                    })
                    .on('uncheck.bs.table', function (e, row) {
                        var sumArray = Izb.controller.message.selectUserArray;
                        var item_id = Izb.controller.message.inSumbitArray(row._id);
                        if (item_id > -1) {
                            sumArray.remove1(item_id);
                        }

                    }).on('check-all.bs.table', function (e) {
                        var sumArray = Izb.controller.message.selectUserArray;
                        var allData = $('#payment-detail').bootstrapTable('getData', null);
                        if (allData != null) {
                            for (var i = 0; i < allData.length; i++) {

                                var val = allData[i]._id;

                                var item_id = Izb.controller.message.inSumbitArray(val);

                                if (item_id == -1) {
                                    sumArray.push(val);
                                }
                            }
                        }
                    }).on('uncheck-all.bs.table', function (e) {
                        var sumArray = Izb.controller.message.sumbitArray;
                        var allData = $('#payment-detail').bootstrapTable('getData', null);
                        if (allData != null) {
                            for (var i = 0; i < allData.length; i++) {

                                var val = allData[i]._id;


                                var item_id = Izb.controller.message.inSumbitArray(val);

                                if (item_id > -1) {
                                    sumArray.remove1(item_id);
                                }
                            }
                        }

                    })
            ;

        },
        initSelectVideo: function () {
            var dialog = Izb.ui.showDialogByTpl(this.tpl.selectvideoform, "请选择微课", {  }, function () {
                $("#message_video").val(Izb.controller.message.selectvideoId);
            });
            function operateFormatter(value, row, index) {
                return [
                    '<a class="remove ml10" href="javascript:void(0)"  title="Remove">',
                    '<i class="glyphicon glyphicon-remove">查看明细</i>',
                    '</a>'
                ].join('');
            }

            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.payment.delDetail(value, row, index);
                }
            },
                //添加付款单栏目
                $('#message-selectvideo').bootstrapTable({
                    method: 'get',
                    url: Izb.controller.message.action.selectvideo,
                    cache: false,
                    height: 400,
                    striped: true,
                    pagination: true,
                    pageSize: 20,
                    sidePagination: 'server',
                    pageList: [10, 25, 50, 100, 200],
                    showColumns: true,
                    search: true,
                    showRefresh: true,
                    minimumCountColumns: 2,
                    singleSelect: true,
                    clickToSelect: true,
                    columns: [
                        {
                            field: 'state',
                            checkbox: true
                        },
                        {
                            field: 'recommend_video_id',
                            title: '微课id',
                            align: 'right',
                            valign: 'bottom',
                            sortable: true
                        },
                        {
                            field: 'recommend_title',
                            title: '微课标题',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        }

                    ],
                    onLoadSuccess: function (data) {


                    }
                })//添加的点击事件 并且清空之前点击的栏目
                    .on('check.bs.table', function (e, row) {
                        Izb.controller.message.selectvideoId = row.recommend_video_id;
                    })
                    .on('uncheck.bs.table', function (e, row) {
                        Izb.controller.message.selectvideoId = "";
                    })

        }
    });

});


/*******************************************************************
 *        用户管理
 *
 ********************************************************************/


/*
 * user:用户信息
 */
$.module("Izb.controller.user",function(){
    var ip4Regx = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";
    return new Controller({
        pty:{
            name : '用户信息',
            itemName : '用户',
            key: 'user',
            autoLoad:false
        },
        //接口
        action:{
            list:'/user/list',
            freeze:"/user/freeze",//帐号冻结
            show:"/user/show",
            gm:"/user/gm", //设置角色
            ban:"/user/ban", //封杀用户
            unban:"/user/unban", //解封用户
            change_vip:"/finance/change_vip", //变更VIP
            donate_horn: "/finance/donate_horn",
            edit_name_pic: "/user/edit",
            add:"/user/add_user",
            del:"/user/del",
            allCompany:"/user/getAllCompany",
            edit_pic:"/user/edit_pic",
            editRemark:"/user/editRemark",//修改备注
            resetpsw:"/user/resetpass",
            editPXY:"/user/editPXY",          //学员权限
            editPQD:"/user/editPQD",          //抢答权限
            editPZS:"/user/editPZS"          //招生权限


        },
        //模板Id
        tpl:{
            header:'tpl-user-header',
            content:'tpl-user-list',
            show:'tpl-user-show',
            ban:'tpl-user-ban',
            priv:'tpl-user-priv',
            change_vip:'tpl-user-change_vip',
            donate_horn: 'tpl-user-donate_horn',
            edit_name_pic: 'tpl-user-edit_name_pic',
            edit_remark: 'tpl-user-edit_remark',
            input:'tpl-user-input'
        },
        tipTitle:{
            priv:'设置用户角色',
            donate_horn:'赠送喇叭'
        },
        event: {
            onBeforeList: function (data) {
                if (!data.stime) {
                    data.stime = "2013-01-01 00:00:00";
                    $("#stime").val(data.stime);
                };
            },
            onSearch:function(searchParams){
                if($.isNumeric(searchParams.sbeanLevel)){
                    searchParams.sbean = Izb.common.getLevel2Coin(searchParams.sbeanLevel,false);
                }

                if($.isNumeric(searchParams.ebeanLevel)){
                    searchParams.ebean = Izb.common.getLevel2Coin((parseInt(searchParams.ebeanLevel)+1),false);
                }

                if($.isNumeric(searchParams.scoinLevel)){
                    searchParams.scoin = Izb.common.getLevel2Coin(searchParams.scoinLevel,true);
                }

                if($.isNumeric(searchParams.ecoinLevel)){
                    searchParams.ecoin = Izb.common.getLevel2Coin((parseInt(searchParams.ecoinLevel)+1),true);
                }

                if (searchParams._id) {
                    searchParams.stime = "2013-01-01 00:00:00";
                    delete searchParams.sbean;
                    delete searchParams.sbeanLevel;
                }
            },
            //cueet..
            onAddRender:function(){
                var selectMainType2 = '<%for(var i=0;i<data.length;i++){var item=data[i];{%><option value="<%=item._id%>"><%=item.company_name%></option><%}}%>'
//                $("#companyId").html($.template(selectMainType2,{data:Izb.controller.user.getAllCompany()}));
            }

        }
    },{

        selectTeacher:function(optionVal){
            switch(optionVal) {
                case '1':
                    $('#rooms').hide();
                    break;
                case '2':
                    $('#rooms').show();
                    break;
                case '3':
                    $('#rooms').hide();
                    break;
                case '4':
                    $('#rooms').show();
                    break;
                case '5':
                    $('#rooms').hide();
                    break;
            }
        },

        //解约
        freeze:function(id,status){
            var that = this;
            Izb.ui.confirm("确认要"+(status == 0 ? '冻结':'解冻')+"该账户？", function() {
                Izb.common.getResult({
                    action:that.action.freeze,
                    data: {_id:id,status:status},
                    success:function(result){
                        Izb.main.refresh();
                    }
                });
            });

        },
        //设置用户角色
        setPriv:function(id,priv){
            var that = this,
                title = this.tipTitle.priv;
            if(id){
                Izb.ui.confirm("确认要进行该操作吗？", function() {
                    Izb.common.getResult({
                        action:that.action.gm,
                        data: {_id:id,priv:priv},
                        success:function(result){
                            Izb.main.refresh();
                        }
                    });
                });
            }else{
                Izb.ui.showDialogByTpl(that.tpl.priv, title, null, function() {
                    var data = Izb.ui.getFormData(that.formName);
                    if(data._id=="" || !$.parseBoolean($("#txtUserId").attr("data-success"))){
                        $("#txtUserId").focus();
                        return false;
                    }
                    Izb.common.getResult({
                        action:that.action.gm,
                        data:data,
                        success:function(result){
                            Izb.main.refresh();
                        }
                    });
                });

                var $priv = $("select[name=priv]", $("form[name=" + that.formName + "]")).attr("disabled", false);
                Izb.ui.setFormData(that.formName, { _id: id, priv: priv });//表单属性为 disabled 的时候设置值失败
                $priv.attr("disabled", true);

                Izb.ui.bindUserNameText("#txtUserId","#userIdTip");
            }


        },
        edit_name_pic: function (id) {
            var that = this;
            Izb.ui.showDialogByTpl(that.tpl.edit_name_pic, "用户信息编辑", null, function () {
                var data = Izb.ui.getFormData(that.formName);

                var temdata={_id:data._id,nick_name:data.nick_name , pic:data.pic};
//                if(data["pic_default"]){
//                    temdata["pic"]="http://ttwww.app1101168695.twsapp.com/styles/images/index/avatar.png";
//                }
                Izb.core.out(temdata);
                //return;
                Izb.common.getResult({
                    action: that.action.edit_name_pic,
                    data: temdata,
                    success: function (result) {
                        Izb.main.refresh();
                    }
                });
            });
            var data = this.get(id);
            Izb.ui.setFormData(that.formName, data);
        },
        editRemark: function (id) {//修改备注
            var that = this;
            Izb.ui.showDialogByTpl(that.tpl.edit_remark, "用户信息编辑", null, function () {
                var data = Izb.ui.getFormData(that.formName);

                var temdata={_id:data._id,remark:data.remark};
                Izb.core.out(temdata);
                Izb.common.getResult({
                    action: that.action.editRemark,
                    data: temdata,
                    success: function (result) {
                        Izb.main.refresh();
                    }
                });
            });
            var data = this.get(id);
            Izb.ui.setFormData(that.formName, data);
        },
        reset_psw: function (tuid) {
            var that = this;
                var temdata = {tuid:tuid};
                //return;
                Izb.common.getResult({
                    action: that.action.resetpsw,
                    data: temdata,
                    success: function (result) {
                        Izb.ui.alert('已经重置密码为123456');
                    }
                });

            var data = this.get(id);
            Izb.ui.setFormData(that.formName, data);
        },
        editPXY:function (id , p) {//学员权限
            var that = this;
            var confimMsg = p == 1 ? "您确认开通[学员权限]吗?(开通学员权限后,用户将可以登录学生端)":"您确认关闭[学员权限]吗?(关闭学员权限后,用户将不能登录学生端)";
            Izb.ui.confirm(confimMsg, function() {
                Izb.common.getResult({
                    action: that.action.editPXY,
                    data: {"_id" : id , "p" : p},
                    success: function (result) {
                        if(p == 1){
                            Izb.ui.alert('成功开通[学员权限]!');
                        }else{
                            Izb.ui.alert('成功关闭[学员权限]!');
                        }
                        Izb.main.refresh();
                    }
                });
            });
        },editPQD:function (id , p) {//抢答权限
            var that = this;
            var confimMsg = p == 1 ? "您确认开通[抢答权限]吗?(开通抢答权限后,用户将可以在教师端抢答学员提问)":"您确认关闭[抢答权限]吗?(关闭抢答权限后,用户将不能在教师端抢答学员提问)";
            Izb.ui.confirm(confimMsg, function() {
                Izb.common.getResult({
                    action: that.action.editPQD,
                    data: {"_id": id, "p": p},
                    success: function (result) {
                        if (p == 1) {
                            Izb.ui.alert('成功开通[抢答权限]!');
                        } else {
                            Izb.ui.alert('成功关闭[抢答权限]!');
                        }
                        Izb.main.refresh();
                    }
                });
            });
        },
        editPZS:function (id , p) {//招生权限
            var that = this;
            var confimMsg = p == 1 ? "您确认开通[招生权限]吗?(开通招生权限后,用户将可以在教师端查看商机)":"您确认关闭[招生权限]吗?(关闭抢答权限后,用户将不能在教师端查看商机)";
            Izb.ui.confirm(confimMsg, function() {
                Izb.common.getResult({
                    action: that.action.editPZS,
                    data: {"_id" : id , "p" : p},
                    success: function (result) {
                        if(p == 1){
                            Izb.ui.alert('成功开通[招生权限]!');
                        }else{
                            Izb.ui.alert('成功关闭[招生权限]!');
                        }
                        Izb.main.refresh();
                    }
                });
            });
        }

    });

}(Izb));






$.module("Izb.controller.blacklist", function () {

    return new Controller({
        pty: {
            name: '黑/白名单管理',
            itemName: '黑/白名单',
            key: 'blacklist',
            menu: 'blacklist'
        },
        //接口
        action: {
            list: '/blacklist/list',
            add: '/blacklist/add',
            del: '/blacklist/del'
        },
        //模板Id
        tpl: {
            header: 'tpl-blacklist-header',
            content: 'tpl-blacklist-list',
            input: 'tpl-blacklist-input'
        },
        event: {
            onAddRender: function () {
                Izb.ui.bindUserNameText("#txtAddcoinId", "#addcoinIdTip");
            }
        }
    });

}(Izb));




/*
 * star:主播信息
 */
$.module("Izb.controller.star",function(){

    return new Controller({
        pty:{
            name : '主播信息',
            itemName : '主播',
            key: 'star',
            autoLoad: false
        },
        //接口
        action:{
            list:'/star/list', //查询 //列表,此处需要详细校验字段
            del:'/star/terminate', //主播解约
            show:'/user/show', //查看,此处需要详细校验字段
            edit:'/star/edit',
            handle:'/apply/list',
            change_broker: '/star/change_broker',
            batch_pic:'/star/batchPic',

        },
        //模板Id
        tpl:{
            header:'tpl-star-header',
            content:'tpl-star-list',
            show:'tpl-star-show',
            input:'tpl-star-input',
            change_broker: 'tpl-star-change_broker'
        },
        tipTitle:{
            del:"确定要解约主播吗?"
        },
        event: {
            onBeforeList: function (data) {
                if (!data.stime) {
                    data.stime = Izb.ui.getFirstDay();
                    $("#stime").val(data.stime);
                }
            },
            onSearch: function (searchParams) {
                if ($.isNumeric(searchParams.sbeanLevel)) {
                    searchParams.sbean = Izb.common.getLevel2Coin(searchParams.sbeanLevel, false);
                }

                if ($.isNumeric(searchParams.ebeanLevel)) {
                    searchParams.ebean = Izb.common.getLevel2Coin((parseInt(searchParams.ebeanLevel) + 1), false);
                }

            },
            onBeforeEdit: function (params) {
                Izb.common.getResult({
                    action: Izb.controller.star.action.handle,
                    async: true,
                    data: { page: 1, size: 1, xy_user_id: params._id },
                    success: function (result) {
                        var data = result.data[0];
                        data._id = data.xy_user_id;
                        data.test = data.test ? 1 : 0;
                        Izb.ui.setFormData(Izb.controller.star.formName, data);
                    }
                });
            }
        }
    }, {
        //变更经纪人
        changeBroker:function(id,broker){
            var  that = this;
            var dialog=Izb.ui.showDialogByTpl(this.tpl.change_broker, "变更经纪人", null, function() {
                var data = Izb.ui.getFormData(that.formName);
                if(!data.broker){
                    $("#txtBroker").focus();
                    return false;
                }
                if(!data.auth_code){
                    $("#auth_code").focus();
                    return false;
                }

                if(data.broker == data.old_broker){
                    return true;
                }

                Izb.ui.confirm("确认要变更经纪人吗？", function() {
                    Izb.common.getResult({
                        action:that.action.change_broker,
                        data: data,
                        success:function(result){
                            Izb.ui.tips("变更经纪人成功！", 'face-smile');
                            Izb.main.refresh();
                        }
                    });
                    dialog.close();
                });

                return false;
            });

            Izb.ui.setFormData(this.formName,{
                _id : id,
                old_broker : broker
            });

        },
        //主播推广图片批量处理
        batchPic: function () {
            var that = this;
            Izb.common.getResult({
                action: that.action.batch_pic,
                success: function (result) {
                    Izb.ui.tips("操作成功！", 'face-smile');
                    //Izb.main.refresh();
                }
            });
        }

    });

}(Izb));


/*
 * sign:签约申请
 */
$.module("Izb.controller.sign",function(){

    return new Controller({
        pty:{
            name : '签约申请',
            itemName : '签约',
            key  : 'sign',
            menu : 'apply'
        },
        //接口
        action:{
            list:'/apply/list',
            handle:'/apply/handle',//签约处理
            show:'/apply/show' //签约详情
        },
        //模板Id
        tpl:{
            header:'tpl-sign-header',
            content:'tpl-sign-list',
            show:'tpl-sign-show'
        },
        event: {
            onBeforeList: function (data) {
                if (!data.stime) {
                    data.stime = Izb.ui.getFirstDay();
                    $("#stime").val(data.stime);
                }
            }
        }
    }, {
        //签约处理
        setStatus:function(id,status){
            Izb.common.getResult({
                action:this.action.handle,
                data: {_id:id,status:status},
                success:function(result){
                    Izb.main.refresh();
                }
            });
        },
        sfzShow:function(id){
            Izb.common.getResult({
                action:this.action.show,
                data: {_id:id},
                success:function(result){
                    var sfz = result.sfz_pic || ''
                    if(sfz==''){
                        Izb.ui.tips('此人还未上传身份证!','warning');
                    } else if (sfz.indexOf('http')==0){
                        window.open(sfz);
                    } else if (sfz.length>30){
                        window.open('data:image/png;base64,'+sfz);
                    }
                }
            });

        }
    });



}(Izb));

/*
 * company 公司档案
 */
$.module("Izb.controller.company",function(){

    return new Controller({
        pty: {
            name: '公司档案',
            itemName: '公司档案',
            key: 'company',
            menu: 'company'
        },
        //接口
        action: {
            list: '/company/list', //查询显示
            add: "/company/add",   //新增
            edit: "/company/edit", //编辑
            del: "/company/del", //删除
            submit: "/company/submit", //提交
            recovery:"/company/recovery", // 收回
            audit:"/company/audit", // 审核
            reaudit:"/company/reaudit"// 反审核
        },
        //模板Id
        tpl: {
            header: 'tpl-company-header',
            content: 'tpl-company-list',
            input: 'tpl-company-input'
        },
        event: {
            onBeforeList: function (data) {
                if (!data.stime) {
                    data.stime = Izb.ui.getFirstDay();
                    $("#stime").val(data.stime);
                }
            }
        }
    } ,{

        //提交处理
        changestates:function(id){
            var that = this;
            Izb.ui.confirm("您确定要提交吗？", function () {
                Izb.common.getResult({
                    action:that.action.submit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //收回处理
        recovery:function(id){
            var that = this;
            Izb.ui.confirm("您确定要收回吗？", function () {
                Izb.common.getResult({
                    action:that.action.recovery,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //审核处理
        audit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.audit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //反审核处理
        reaudit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要反审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.reaudit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
//                    success:function(result){
//                        var info=result.data?result.data.info:0;
//                        if(info==1){
//                            Izb.ui.tips("数据已经被使用，不能进行反审核！", 'succeed');
//                        } else{
//                            Izb.ui.tips("操作成功！", 'succeed');
//                            Izb.main.refresh();
//                        }
//                    }
                });
            });
        }
    });
}(Izb));



/*
 * answertype 答疑类型
 */
$.module("Izb.controller.answertype",function(){

    return new Controller({
        pty:{
            name : '答疑类型',
            itemName : '类型',
            key  : 'answertype',
            menu : 'answertype'
        },
        //接口
        action:{
            list:'/answertype/list', //查询显示
            add : "/answertype/add",   //新增
            edit : "/answertype/edit", //编辑
            del : "/answertype/del"  //删除
        },
        //模板Id
        tpl:{
            header:'tpl-answertype-header',
            content:'tpl-answertype-list',
            input:'tpl-answertype-input'
        },
        event: {
            onBeforeList: function (data) {
                if (!data.stime) {
                    data.stime = Izb.ui.getFirstDay();
                    $("#stime").val(data.stime);
                }
            }
        }
    });
}(Izb));

/**
 *   questionnairetype 问卷调查类型档案
 */
$.module("Izb.controller.questionnairetype",function(){

    return new Controller({
        pty:{
            name : '问卷调查管理',
            itemName : '问卷调查',
            key  : 'questionnairetype',
            menu : 'questionnairetype'
        },
        //接口
        action:{
            list:'/questionnairetype/list', //查询显示
            add : "/questionnairetype/add",   //新增
            edit : "/questionnairetype/edit", //编辑
            del : "/questionnairetype/del"  //删除
        },
        //模板Id
        tpl:{
            header:'tpl-questionnairetype-header',
            content:'tpl-questionnairetype-list',
            input:'tpl-questionnairetype-input'
        },
        event: {
            onBeforeList: function (data) {
                if (!data.stime) {
                    data.stime = Izb.ui.getFirstDay();
                    $("#stime").val(data.stime);
                }
            }
        }
    });
}(Izb));





/*
 * feedback:反馈管理
 */
$.module("Izb.controller.feedback",function(){

    //初始化Controller
    return new Controller({
        size:10,
        pty:{
            name : '反馈管理',
            itemName : '反馈',
            key  : 'feedback'
        },
        //接口
        action:{
            list:'/feedback/list'
        },
        //模板Id
        tpl:{
            header:'tpl-feedback-header',
            content:'tpl-feedback-list'
        }
    },{
        process: function(sHtml) {
            return sHtml.replace(/[<>&"]/g,function(c){return {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'}[c];});
        }
    });


}(Izb));

/*
 * pc:反馈管理
 */
$.module("Izb.controller.pc", function () {

    //初始化Controller
    return new Controller({
        size: 10,
        pty: {
            name: '直播伴侣下载',
            itemName: '直播伴侣下载',
            key: 'pc'
        },
        //接口
        action: {
            list: '/pc/show_download_info',
            save: "/pc/save_download_info"
        },
        //模板Id
        tpl: {
            header: 'tpl-common-header',
            content: 'tpl-pc-list'
        },
        event: {
            "onAfterList": function (res) {
                var that = this;
                Izb.ui.setFormData(that.formName, res.data);
            }
        }
    }, {
        save: function () {
            var that = this;
            //j-stars
            Izb.common.getResult({
                type: 'POST',
                action: that.action.save,
                data: {
                    version: $("#j-download-version").val(),
                    down_url: $("#j-download-url").val()
                },
                success: function (result) {
                    Izb.ui.tips("操作成功！", 'succeed');
                    Izb.main.refresh();
                }
            });
        }
    });


}(Izb));




/*
 * admin:账号管理
 */
$.module("Izb.controller.admin",function(){

    function _resolveData(data){
        var menus = {};
        for(var key in data){
            if(key=='name' || key=='_id' || key=='nick_name' || key=='password' || key=='company_id'|| key=='post_name'){
                continue;
            }
            menus[key] = data[key];
            delete data[key];
        }
        data.menus = JSON.stringify(menus);
        if(data.password==""){
            delete data.password;
        }
    }

    return new Controller({
        pty:{
            name : '帐号管理',
            key : 'admin',
            itemName : '帐号'
        },
        //接口
        action:{
            list:'/admin/list',
            add:"/admin/add",
            edit:"/admin/edit",
            del:"/admin/del",
            modif_pwd:"/modif_pwd"
        },
        //模板Id

        tpl:{
            header:'tpl-common-header',
            content:'tpl-admin-list',
            input:'tpl-admin-input',
            modif_pwd:'tpl-admin-modif_pwd'
        },
        event:{
            onBeforeAdd:function(data){
                _resolveData(data);
            },
            onBeforeSaveEdit:function(data){
                _resolveData(data);
            },
            onBeforeEdit:function(data,dialog){
                var menus = {};
                try{
                    menus = JSON.parse(data.menus)
                }catch(ex){}

                $.extend(data,data.menus);
                delete data.menus;

                var _panel=$(dialog.content());
                _panel.delegate(".lblItem select", "change", function () {//权限关联  主要针对上级权限的限制
                    var parentPower = $(this).data("parentpower");
                    if (parentPower) {
                        var $select = _panel.find("select[name=" + parentPower + "]");
                        if ($select && $select.val() != 2) {
                            $select.val($(this).val());
                        }
                    } else {
                        var $select = _panel.find("select[data-parentpower=" + $(this).attr("name") + "]");
                        if ($select && $(this).val() == 0) {
                            $select.val(0);
                        }
                    }
                });
                var selectMainType2 = '<%for(var i=0;i<data.length;i++){var item=data[i];{%><option value="<%=item._id%>"><%=item.company_name%></option><%}}%>'
                // $("#company_id").html($.template(selectMainType2,{data:Izb.controller.user.getAllCompany()}));
            },
            onBeforeList:function(){
                // Izb.controller.user.getAllCompany()
            },
            onAddRender:function(){
                var selectMainType2 = '<%for(var i=0;i<data.length;i++){var item=data[i];{%><option value="<%=item._id%>"><%=item.company_name%></option><%}}%>'
                // $("#company_id").html($.template(selectMainType2,{data:Izb.controller.user.getAllCompany()}));
            }
        }
    }, {
        modif_pwd:function(){
            var arr1 = [];
            var arr2= new Array();
            var that = this;
            Izb.ui.showDialogByTpl(this.tpl.modif_pwd, '修改密码', null, function() {
                var data = Izb.ui.getFormData('inputForm');

                Izb.common.getResult({
                    type: 'POST',
                    action : that.action.modif_pwd,
                    data : data,
                    success : function(result){
                        Izb.ui.tips('修改成功！','face-smile');
                    }
                });
            });
        }
    });

}(Izb));


/*
 * oplog:管理日志
 */
$.module("Izb.controller.oplog",function(){

    return new Controller({
        pty:{
            name : '管理日志',
            key : 'oplog',
            itemName : '记录',
            menu:'sys/oplog_list'
        },
        //接口
        action:{
            list:'/sys/oplog_list'
        },
        //模板Id
        tpl:{
            header:'tpl-oplog-header',
            content:'tpl-oplog-list'
        },
        event: {
            onBeforeList: function (data) {
                if (!data.stime) {
                    data.stime = Izb.ui.getFirstDay();
                    $("#stime").val(data.stime);
                }
            }
        }
    });

}(Izb));

/*
 * maintype:频道档案
 */
$.module("Izb.controller.maintype",function(){

    return new Controller({
        pty:{
            name : '频道档案',
            key : 'maintype',
            itemName : '频道',
            menu:'maintype'
        },
        //接口 groovy
        action:{
            list:'/maintype/list',
            add:"/maintype/add",
            edit:"/maintype/edit",
            del:"/maintype/del"
        },
        //模板Id html
        tpl:{
            header:'tpl-maintype-header',
            content:'tpl-maintype-list',
            input:'tpl-maintype-input'
        },
        event: {
            onAfterDel:function(data){
                if(data==null){
                    Izb.ui.tips('专业档案已经引用不能删除','warning');
                }
            }
        }
    });

}(Izb));


/*
 * statement 供应商结算单
 */
$.module("Izb.controller.statement",function(){

    return new Controller({
        pty:{
            name : '供应商结算单',
            key : 'statement',
            itemName : '供应商结算单'
            // menu:
        },
        //接口
        action:{
            list:"/statement/list",
            count:"/statement/selectYM",
            submit: "/statement/submit",
            audit: "/statement/audit",
            rollbackSubmit:"/statement/rollbackSubmit",
            rollbackAudit:"/statement/rollbackAudit",
            query_company:"/statement/query_company"
        },
        //模板Id
        tpl:{
            header:'tpl-statement-header',
            content:'tpl-statement-list',
            input:'tpl-statement-input'
        },
        event: {
            onAfterList:function(result) {
                if(result.data.info==1){
                    Izb.ui.alert("没有查询结果，请尝试结算");
                }
            },
            onBeforeList: function (data) {
                Izb.controller.statement.getAllcompany();
            }

        }
    },{
        selectYear_Month:function(val){
            if(val==0){
                $("#conutMonth").css("display","table-row");
                $(".conutYear").css("display","none");
            }
            if(val==1){
                $(".conutYear").css("display","table-row");
                $("#conutMonth").css("display","none");
            }
        },

        //结算
        count:function(FormName){
            var that = this;
            var dialog = Izb.ui.showDialogByTpl(this.tpl.input,"结算方式",null, function(){
                var data = Izb.ui.getFormData("inputForm");
                $form = $("form[name='inputForm']")
                if (!$form.valid()) {
                    return false;
                }
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.count,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                        switch(result.data.flg)
                        {
                            case 1:
                                Izb.ui.alert("没有年结算单");
                                break;
                            case 2:
                                Izb.ui.alert("没有月结算单");
                                break;
                            case 3:
                                Izb.ui.alert("没有找到月提成比率");
                                break;
                            case 4:
                                Izb.ui.alert("结算成功");
                                break;
                            case 5:
                                Izb.ui.alert("订单商品类型没有对应的结算比率，不能结算");
                                break;
                        }
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });
            var selectMainType = '<%for(var i=0;i<data.length;i++){var item=data[i];{%><option value="<%=item._id%>"><%=item.company_name%></option><%}}%>'
            $("#company_id").html($.template(selectMainType,{data:Izb.controller.statement.getAllcompany()}));
        },
        //选择结算公司
        allcompany:[],
        getAllcompany:function(){
            var that = this;
            if(that.allcompany.length>0){
                return that.allcompany;
            }
            Izb.common.getResult({
                type : 'GET',
                action: that.action.query_company,
                success: function (result) {
                    console.log(result);
                    return that.allcompany = result.data;
                }
            });
            return that.allcompany;
        },

        //提交处理
        changestates:function(id){
            var that = this;
            Izb.ui.confirm("您确定要提交吗？", function () {
                Izb.common.getResult({
                    action:that.action.submit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //回收处理
        rollbackSubmit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要回收吗？", function () {
                Izb.common.getResult({
                    action:that.action.rollbackSubmit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("回收成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //提交审核
        audit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.audit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //反审核
        rollbackAudit:function(id){
            var that = this;
            Izb.ui.confirm("您确定反审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.rollbackAudit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("反审核成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        }
    });
}(Izb));


//member vip
$.module("Izb.controller.vipcard",function(){

    return new Controller({
        pty:{
            name : 'VIP卡片档案',
            key : 'vipcard',
            itemName : 'VIP卡片档案'
        },
        //接口
        action:{
            list:"/vipcard/list",
            add:"/vipcard/add",
            edit:"/vipcard/edit",
            del:"/vipcard/del",
            submit: "/vipcard/submit",
            audit:"/vipcard/audit",
            rollbackSubmit:"/vipcard/rollbackSubmit",
            rollbackAudit:"/vipcard/rollbackAudit"
        },
        //模板Id
        tpl:{
            header:'tpl-vipcard-header',
            content:'tpl-vipcard-list',
            input:'tpl-vipcard-input'
        },
        event: {

        }
    },{
        //提交处理
        changestates:function(id){
            var that = this;
            Izb.ui.confirm("您确定要提交吗？", function () {
                Izb.common.getResult({
                    action:that.action.submit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //回收处理
        rollbackSubmit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要回收吗？", function () {
                Izb.common.getResult({
                    action:that.action.rollbackSubmit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("回收成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //审核处理
        audit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.audit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //反审核
        rollbackAudit:function(id){
            var that = this;
            Izb.ui.confirm("您确定反审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.rollbackAudit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("反审核成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        }
    });
}(Izb));

//BonusSetting
//member vip
$.module("Izb.controller.bonussetting",function(){

    return new Controller({
        pty:{
            name : '奖金池模板',
            key : 'bonussetting',
            itemName : '奖金池模板档案'
        },
        //接口
        action:{
            list:"/bonussetting/list",
            add:"/bonussetting/add",
            edit:"/bonussetting/edit",
            del:"/bonussetting/del",
            submit: "/bonussetting/submit",
            audit:"/bonussetting/audit",
            rollbackSubmit:"/bonussetting/rollbackSubmit",
            rollbackAudit:"/bonussetting/rollbackAudit"
        },
        //模板Id
        tpl:{
            header:'tpl-bonussetting-header',
            content:'tpl-bonussetting-list',
            input:'tpl-bonussetting-input'
        },
        event: {
            //点击新增按钮的时候
            onAddRender:function(_data){
                //var teacherlist = '<%for(var i=0;i<data.length;i++){var item=data[i];{%><option value="<%=item._id%>"><%=item.nick_name%></option><%}}%>'
                // $(".J_teacher_id").html($.template(teacherlist,{data:Izb.controller.employmentregistration.getAllTeachers()}));
                //定义表结构
                Izb.controller.bonussetting.initDetail();
            },
            onBeforeSaveEdit:function(_data)
            {
                //Izb.controller.bonussetting.initDetail();
               Izb.controller.bonussetting.saveJson(_data);
            },
            onBeforeAdd:function(_data){
                //这里要判断明细的总金额是否大于表头的总金额

                Izb.controller.bonussetting.saveJson(_data);
            },
            onBeforeEdit:function(data,dialog)
           {
               if(data!=null)
               {
                   Izb.controller.bonussetting.initDetail();
                   $('#bonus-detail').bootstrapTable('load',data.bonusTypeList);
               }
            },
            onBeforeShow:function(data)
            {
                if(data!=null)
                {    Izb.controller.bonussetting.initDetail();
                    $('#bonus-detail').bootstrapTable('load',data.bonusTypeList);
                }

            }
        }
    },{
        initDetail:function(){
            function templateFormatter(value, row) {
                var icon = (value  == 0 ? '红色模板' : '蓝色模板')

                return  icon;
            }
            function operateFormatter(value, row, index) {
                return [
                    '<a class="remove ml10" href="javascript:void(0)"  title="Remove">',
                    '<i class="glyphicon glyphicon-remove">删除</i>',
                    '</a>'
                ].join('');
            }
            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.bonussetting.delDetail(value,row,index);
                    console.log(value, row, index);
                }
            };
            $('#bonus-detail').bootstrapTable({
                method: 'get',
                url: 'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'id',
                    title: 'id',
                    align: 'right',
                    valign: 'bottom',
                    sortable: true
                }, {
                    field: 'mlevel',
                    title: '奖项名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'mmoney',
                    title: '金额',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'mtemplate',
                    title: '模板类型',
                    align: 'left',
                    valign: 'top',
                    sortable: true,
                    formatter:templateFormatter
                }, {
                    field: 'mweight',
                    title: '权重',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'vweight',
                    title: 'VIP权重',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'quantity',
                    title: '数量',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: false,
                    events: operateEvents,
                    formatter:operateFormatter
                }]
            });
        },
        delDetail:function(value,row,index){
            var selects =  $('#bonus-detail').bootstrapTable('getSelections');
            var ids = Array();
            ids.push(row._id);
            $('#bonus-detail').bootstrapTable('remove', {
                field: '_id',
                values: ids
            });
        },
        //点击明细新增按钮
        addDetail:function(){

            var data = {_id:$('#bonus-detail').bootstrapTable('getData').length,
                         mlevel:$("#bonus-detail-add-table").find("#mlevel").val(),
                         mmoney:$("#bonus-detail-add-table").find("#mmoney").val(),
                         mtemplate:$("#bonus-detail-add-table").find("#mtemplate").val(),
                         mweight:$("#bonus-detail-add-table").find("#mweight").val(),
                         quantity:$("#bonus-detail-add-table").find("#quantity").val()}
            $('#bonus-detail').bootstrapTable('append',data );
        },
        saveJson: function (_data) {
             var json =  $('#bonus-detail').bootstrapTable('getData');


            _data.detailjson = JSON.stringify(json);
        },
        //提交处理
        changestates:function(id){
            var that = this;
            Izb.ui.confirm("您确定要提交吗？", function () {
                Izb.common.getResult({
                    action:that.action.submit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //回收处理
        rollbackSubmit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要回收吗？", function () {
                Izb.common.getResult({
                    action:that.action.rollbackSubmit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("回收成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //审核处理
        audit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.audit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //反审核
        rollbackAudit:function(id){
            var that = this;
            Izb.ui.confirm("您确定反审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.rollbackAudit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("反审核成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        }
    });
}(Izb));



$.module("Izb.controller.bonuspools",function(){

    return new Controller({
        pty:{
            name : '奖金池生成',
            key : 'bonuspools',
            itemName : '奖金池生成'
        },
        //接口
        action:{
            list:"/bonuspools/list",
            add:"/bonuspools/add",

            edit:"/bonuspools/edit",
            del:"/bonuspools/del",
            submit: "/bonuspools/submit",
            audit:"/bonuspools/audit",
            rollbackSubmit:"/bonuspools/rollbackSubmit",
            rollbackAudit:"/bonuspools/rollbackAudit",

            referlist:"/bonussetting/auditlist",
            detaillist:"/bonuspools/getdetaillist.json"
        },
        //模板Id
        tpl:{
            header:'tpl-bonuspools-header',
            content:'tpl-bonuspools-list',
            input:'tpl-bonuspools-input'
        },
        event: {
            //点击新增按钮的时候
            onAddRender:function(_data){
                //var teacherlist = '<%for(var i=0;i<data.length;i++){var item=data[i];{%><option value="<%=item._id%>"><%=item.nick_name%></option><%}}%>'
                // $(".J_teacher_id").html($.template(teacherlist,{data:Izb.controller.employmentregistration.getAllTeachers()}));
                //定义表结构
                Izb.controller.bonuspools.getAllRefer();
            },
            onBeforeSaveEdit:function(_data)
            {
                //Izb.controller.bonuspools.initDetail();
                Izb.controller.bonuspools.saveJson(_data);
            },
            onBeforeAdd:function(_data){
                Izb.controller.bonuspools.saveJson(_data);
            },
            onBeforeEdit:function(data,dialog)
            {
//                if(data!=null)
//                {
//                    Izb.controller.bonuspools.initDetail();
//                    $('#bonus-detail').bootstrapTable('load',data.bonusTypeList);
//                }
            },
            onBeforeShow:function(data)
            {
//                if(data!=null)
//                {    Izb.controller.bonuspools.initDetail();
//                  //  $('#bonus-detail').bootstrapTable('load',data.bonusTypeList);
//                }
                Izb.controller.bonuspools.initDetails(data._id);
            }
        }
    },{
        selectReferId:0,
        initRefer:function(){
            function templateFormatter(value, row) {
                var icon = (value  == 0 ? '红色模板' : '蓝色模板')

                return  icon;
            }
            function operateFormatter(value, row, index) {
                return [
                    '<a class="remove ml10" href="javascript:void(0)"  title="Remove">',
                    '<i class="glyphicon glyphicon-remove">查看明细</i>',
                    '</a>'
                ].join('');
            }
            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.bonuspools.delDetail(value,row,index);
                    console.log(value, row, index);
                }
            },
            $('#bonuspools-detail').bootstrapTable({
                method: 'get',
                url: null,
                cache: false,
                height: 400,
                striped: true,
                pagination: true,
                pageSize: 50,
                pageList: [10, 25, 50, 100, 200],
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                singleSelect:true,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'id',
                    title: 'id',
                    align: 'right',
                    valign: 'bottom',
                    sortable: true
                }, {
                    field: 'mainTemplateName',
                    title: '模板名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'allMoney',
                    title: '总金额',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'remark',
                    title: '备注',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: false,
                    events: operateEvents,
                    formatter:operateFormatter
                }]
            }).on('check.bs.table', function (e, row) {
               // $result.text('Event: check.bs.table, data: ' + JSON.stringify(row));
                Izb.controller.bonuspools.selectReferId = row._id;
            });


        },
        initDetails:function(id){

            $('#bonuspools-detail-list').bootstrapTable({
                method: 'get',
                url: Izb.controller.bonuspools.action.detaillist+"?main_id="+id,
                cache: false,
                height: 400,
                striped: true,
                pagination: true,
                sidePagination: 'server',
                pageSize: 50,
                pageList: [10, 25, 50, 100, 200],
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                singleSelect:true,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'id',
                    title: 'id',
                    align: 'right',
                    valign: 'bottom',
                    sortable: true
                }, {
                    field: 'mlevel',
                    title: '奖项',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },{
                    field: 'mtemplate',
                    title: '模板',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'mmoney',
                    title: '金额',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                },
                    {
                    field: 'yearMon',
                    title: '月份',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'remark',
                    title: '用户',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                },{
                    field: 'remark',
                    title: '打开时间',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
    },
        delDetail:function(value,row,index){
            var selects =  $('#bonus-detail').bootstrapTable('getSelections');
            var ids = Array();
            ids.push(row._id);
            $('#bonus-detail').bootstrapTable('remove', {
                field: '_id',
                values: ids
            });
        },
        saveJson: function (_data) {

            _data.referid = Izb.controller.bonuspools.selectReferId;
            _data.select_money =$("#select_months").val();
        },
        allrefer:[],
        getAllRefer:function(){
            var that = this;
            if(that.allrefer.length>0){

                that.initRefer();
                $('#bonuspools-detail').bootstrapTable("load",that.allrefer);
                return that.allrefer;
            }
            Izb.common.getResult({
                type : 'GET',
                action: that.action.referlist,
                data:{page:1,size:2000,audit:true},
                success: function (result) {
                    console.log(result);
                    that.allrefer = result.data;
                    that.initRefer();
                    $('#bonuspools-detail').bootstrapTable("load",that.allrefer);
                    return that.allrefer;
                }
            });
            return that.allrefer;
        },
        //提交处理
        changestates:function(id){
            var that = this;
            Izb.ui.confirm("您确定要提交吗？", function () {
                Izb.common.getResult({
                    action:that.action.submit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //回收处理
        rollbackSubmit:function(id){
            var that = this;
            Izb.ui.confirm("您确定要回收吗？", function () {
                Izb.common.getResult({
                    action:that.action.rollbackSubmit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("回收成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },
        //审核处理
        audit:function(id){
            var that = this;
            Izb.ui.confirm("确定审核吗？审核成功之后，将生成红包，进入后台操作模式，请勿重复点击审核，请耐心等待5-10分钟，中途可以点击查询。刷新页面的时候，如果状态为“已经审”核，则代表审核成功", function () {
                Izb.common.getResult({
                    action:that.action.audit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });

        },
        //反审核
        rollbackAudit:function(id){
            var that = this;
            Izb.ui.confirm("您确定反审核吗？", function () {
                Izb.common.getResult({
                    action:that.action.rollbackAudit,
                    data: {_id:id},
                    success:function(result){
                        Izb.ui.tips("反审核成功！", 'succeed');
                        Izb.main.refresh();;
                    }
                });
            });
        }
    });
}(Izb));



//提现付款单据
$.module("Izb.controller.apply",function(){

    return new Controller({
        pty:{
            name : '提现付款单据',
            key : 'apply',
            itemName : '提现付款单据'
        },
        //接口
        action:{
            list:"/apply/list",
            cancle:"/apply/cancle"//取消

        },
        //模板Id
        tpl:{
            header:'tpl-apply-header',
            content:'tpl-apply-list',
            input:'tpl-apply-input'
        },
        event: {
            onListTmpAfter : function( data ){
                $("#page_sum_money").html(data.page_sum_money + "&nbsp;&nbsp;&nbsp;&nbsp;");
                $("#sum_money").html(data.sum_money + "&nbsp;&nbsp;&nbsp;&nbsp;");
            }
        }
    },{
        cancle:function(id){
            var that = this;
            Izb.ui.confirm("您确认打回该用户的申请单吗", function () {
                Izb.common.getResult({
                    action:that.action.cancle,
                    data: {id:id},
                    success:function(result){
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        }
    } );
}(Izb));

//payment 付款单
$.module("Izb.controller.payment",function() {

    return new Controller({
        pty: {
            name: '付款单',

            key: 'payment',
            itemName: '付款单'
        },
        //接口
        action: {
            list: "/payment/list",
            add: "/payment/add",
            edit: "/payment/edit",
            del: "/payment/del",
            submit: "/payment/submit",
            audit: "/payment/audit",
            rollbackSubmit: "/payment/rollbackSubmit",
            rollbackAudit: "/payment/rollbackAudit",
            referlist: "/apply/list",
            applylist: "/payment/applylist",
            newapplylist: "/payment/applylist.json",
            detaillist:"/payment/payment_flowlist.json",
            editlist:"/payment/edit_list.json",
            editItemRemove:"/payment/edit_item_remove.json"



        },
        //模板Id
        tpl: {
            header: 'tpl-payment-header',
            content: 'tpl-payment-list',
            input: 'tpl-payment-input'
        },
        event: {
            //点击新增按钮的时候
            onAddRender: function (_data) {

                Izb.controller.payment.getAllRefer();
                Izb.controller.payment.sumbitArray = [];
            },
            onBeforeSaveEdit: function (_data) {
                _data.item_remove_list = Izb.controller.payment.item_remove_list.unique1().toString();
            },
            onBeforeAdd: function (_data) {

                Izb.controller.payment.saveJson(_data);
            },
            onBeforeEdit: function (data, dialog) {
                Izb.controller.payment.item_remove_list = [];
                Izb.controller.payment.initEditDetails(data._id);
            },
            onBeforeShow: function (data) {

              //  Izb.controller.payment.initDetails(data._id);
            },
            onBeforeList: function (data) {
//                if (!data.begin_date) {
//                    data.stime = "2013-01-01 00:00:00";
//                    $("#stime").val(data.stime);
//                };
//                Izb.controller.user.getAllCompany();
            }

        }
    }, {
        MAX_APPLY_NUMBER : 1000,//新增时申请单最大数量

        add:function(){
            if (!this.action.add || !this.tpl.input) {
                return;
            }

            if (!this.checkInputLimits(this.tipTitle.add)) {
                return;
            }

            var that = this,
                title = this.tipTitle.add,
                $form
                ;
            var dialog = Izb.ui.showDialogByTpl(this.tpl.input, title, { params: that.hashParams, data: {} }, function () {

                //表单校验
                if (!$form.valid()) {
                    return false;
                }
                //申请单数量校验
                var sumArray = Izb.controller.payment.sumbitArray;
                if(sumArray ==null || sumArray.length == 0){
                    Izb.ui.tips('请选择申请单！','warning');
                    return false;
                }
                if(sumArray.length > that.MAX_APPLY_NUMBER){
                    Izb.ui.tips('申请单数量不能超过' + that.MAX_APPLY_NUMBER + '！当前数量为:' + sumArray.length,'warning');
                    return false;
                }

                var data = Izb.ui.getFormData("inputForm");
                if ($.isFunction(that.event.onBeforeAdd)) {
                    that.event.onBeforeAdd(data);
                }

                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.add,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                        if ($.isFunction(that.event.onAfterAdd)) {
                            that.event.onAfterAdd(result);
                        }
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });


            });

            if ($.isFunction(that.event.onAddRender)) {
                that.event.onAddRender();

            }

            $form = $('form[name=' + that.formName + ']');


        },





        showDetailStart: function (_id, deleteBtnShow) {
            Izb.controller.payment.itemDeleteBtnShow = deleteBtnShow;
            Izb.controller.payment.showDetail(_id);
        }, editStart: function (_id, deleteBtnShow) {
            Izb.controller.payment.itemDeleteBtnShow = deleteBtnShow;
            Izb.controller.payment.edit(_id);
        },

        itemDeleteBtnShow : false,
        sumbitArray: [],

        initRefer: function () {


            function operateFormatter(value, row, index) {
                return [
                    '<a class="remove ml10" href="javascript:void(0)"  title="Remove">',
                    '<i class="glyphicon glyphicon-remove">查看明细</i>',
                    '</a>'
                ].join('');
            }

            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.payment.delDetail(value, row, index);
                }
            },
                //添加付款单栏目
                $('#payment-detail').bootstrapTable({
                    method: 'get',
                    url: Izb.controller.payment.action.newapplylist,
                    cache: false,
                    height: 400,
                    striped: true,
                    pagination: true,
                    pageSize: 20,
                    sidePagination: 'server',
                    pageList: [10, 25, 50, 100, 200],
                    showColumns: true,
                    showRefresh: true,
                    minimumCountColumns: 2,
                    singleSelect: false,
                    clickToSelect: true,
                    columns: [
                        {
                            field: 'state',
                            checkbox: true
                        },
                        {
                            field: 'userId',
                            title: '用户编号',
                            align: 'right',
                            valign: 'bottom',
                            sortable: true
                        },
                        {
                            field: 'realName',
                            title: '真实姓名',
                            align: 'center',
                            valign: 'middle',
                            sortable: true
                        },
                        {
                            field: 'alipayAccount',
                            title: '支付宝账号',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'applyFlowId',
                            title: '申请单流水号',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'applyMoney',
                            title: '申请金额',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        },
                        {
                            field: 'applyYearMonth',
                            title: '提交申请单月份',
                            align: 'left',
                            valign: 'top',
                            sortable: true
                        }

                    ],
                    onLoadSuccess: function (data) {

                        var sumArray = Izb.controller.payment.sumbitArray;
                        var showData = data.data;
                        if(sumArray != null && sumArray.length > 0 && showData != null && showData.length > 0){
                            //循环数据源
                            for(var i = 0 ; i < showData.length ; i++){
                                //每行
                                var row = showData[i];
                                //判断applyFlowId是否在本地选中过
                                var item_id = Izb.controller.payment.inSumbitArray(row.applyFlowId);
                                //选中后默认选择
                                if (item_id > -1) {
                                    //数据中默认选中
                                    row.state = true;

                                    //视图中默认选中
                                    $(".aui_border").find("input[name='btSelectItem']")[i].checked = true;
                                }
                            }
                        }


                    }
                })//添加的点击事件 并且清空之前点击的栏目
                    .on('check.bs.table', function (e, row) {
                        var iscontain = false;
                        var sumArray = Izb.controller.payment.sumbitArray;
                        var item_id = Izb.controller.payment.inSumbitArray(row.applyFlowId);
                        if (item_id == -1) {
                            sumArray.push(row.applyFlowId);
                        }
                    })
                    .on('uncheck.bs.table', function (e, row) {
                        var sumArray = Izb.controller.payment.sumbitArray;
                        var item_id = Izb.controller.payment.inSumbitArray(row.applyFlowId);
                        if (item_id > -1) {
                            sumArray.remove1(item_id);
                        }

                    }).on('check-all.bs.table', function (e) {
                        var sumArray = Izb.controller.payment.sumbitArray;
                        var allData = $('#payment-detail').bootstrapTable('getData', null);
                        if (allData != null) {
                            for (var i = 0; i < allData.length; i++) {

                                var val = allData[i].applyFlowId;

                                var item_id = Izb.controller.payment.inSumbitArray(val);

                                if (item_id == -1) {
                                    sumArray.push(val);
                                }
                            }
                        }
                    }).on('uncheck-all.bs.table', function (e) {
                        var sumArray = Izb.controller.payment.sumbitArray;
                        var allData = $('#payment-detail').bootstrapTable('getData', null);
                        if (allData != null) {
                            for (var i = 0; i < allData.length; i++) {

                                var val = allData[i].applyFlowId;


                                var item_id = Izb.controller.payment.inSumbitArray(val);

                                if (item_id > -1) {
                                    sumArray.remove1(item_id);
                                }
                            }
                        }

                    })
            ;

        },
        inSumbitArray : function(val){//值是否存在sumbitArray
            //是否存在
            var item_id = -1;
            var sumArray = Izb.controller.payment.sumbitArray;
            if(sumArray != null && sumArray.length > 0){
                for(var i = 0 ; i < sumArray.length ; i ++){
                    if(val == sumArray[i] ){
                        item_id = i;
                        break;
                    }
                }
            }
            return item_id;
        },
        initEditDetails:function(id){
            function operateFormatter(value, row, index) {
                return [
                        '<a class="d-button" href="javascript:void(0);" onclick="Izb.controller.payment.item_remove(\''+row.id+'\');">删 </a>'
                ].join('');
            }
//
//            '<a class="like" href="javascript:void(0)" title="Like">',
//                '<i class="glyphicon glyphicon-heart"></i>',
//                '</a>',
//                '<a class="edit ml10" href="javascript:void(0)" title="Edit">',
//                '<i class="glyphicon glyphicon-edit"></i>',
//                '</a>',
//                '<a class="remove ml10" href="javascript:void(0)" title="Remove">',
//                '<i class="glyphicon glyphicon-remove"></i>',
//                '</a>'

            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.payment.delDetail(value, row, index);
//                    console.log(value, row, index);
                }
            }
            //修改付款单栏目
            $('#payment-detail-list').bootstrapTable({
                method: 'get',
                url: Izb.controller.payment.action.detaillist+"?paymentId="+id,
                cache: false,
                height: 400,
                striped: true,
                pagination: true,
                sidePagination: 'server',
                pageSize: 20,
                pageList: [10, 25, 50, 100, 200],
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                singleSelect: true,
                clickToSelect: true,
                columns: [
                    {
                        field: 'userId',
                        title: '用户编号',
                        align: 'right',
                        valign: 'bottom',
                        sortable: true
                    },
                    {
                        field: 'realName',
                        title: '真实姓名',
                        align: 'center',
                        valign: 'middle',
                        sortable: true
                    },
                    {
                        field: 'alipayAccount',
                        title: '支付宝账号',
                        align: 'left',
                        valign: 'top',
                        sortable: true
                    },
                    {
                        field: 'applyFlowId',
                        title: '申请单流水号',
                        align: 'left',
                        valign: 'top',
                        sortable: true
                    },
                    {
                        field: 'applyMoney',
                        title: '申请金额',
                        align: 'left',
                        valign: 'top',
                        sortable: true
                    },
                    {
                        field: 'applyMoney',
                        title: '申请金额',
                        align: 'left',
                        valign: 'top',
                        sortable: true
                    },
                    {
                        field: 'applyYearMonth',
                        title: '提交申请单月份',
                        align: 'left',
                        valign: 'top',
                        sortable: true
                    },
                    {
                        field: 'errCode',
                        title: '支付结果',
                        align: 'center',
                        valign: 'top',
                        sortable: true
                    },
                    {
                        field: 'errMemo',
                        title: '错误信息',
                        align: 'left',
                        valign: 'top',
                        sortable: true
                    },
                    {
                        field: 'operate',
                        title: '操作',
                        align: 'center',
                        valign: 'middle',
                        clickToSelect: false,
                        formatter: operateFormatter,
                        events: operateEvents
                    }

                ],
                onLoadSuccess: function (data) {
                    if(!Izb.controller.payment.itemDeleteBtnShow){
//                        $(".aui_border").find("a").remove();
                        $(".aui_border").find(".d-button").remove();
                    }
                }
            });
        },
        item_remove_list : null,//修改时删除子集合
        item_remove:function(row_id){
            //删除行
            $("#" + row_id).remove();
            //修改时子集合删除事件
            Izb.controller.payment.item_remove_list.push(row_id);

        },
        delDetail: function (value, row, index) {
            var selects = $('#payment-detail').bootstrapTable('getSelections');
            var ids = Array();
            ids.push(row._id);
            $('#payment-detail').bootstrapTable('remove', {
                field: '_id',
                values: ids
            });
        },

        saveJson: function (_data) {

            //_data.referid = Izb.controller.payment.selectReferId;
            _data.listjson =JSON.stringify( Izb.controller.payment.sumbitArray);

            Izb.controller.payment.sumbitArray = [];



        },
        allrefer: [],
        getAllRefer: function () {
            var that = this;

            that.initRefer();
//            if (that.allrefer.length > 0) {
//
//                that.initRefer();
//                $('#payment-detail').bootstrapTable("load", that.allrefer);
//                return that.allrefer;
//            }
//            Izb.common.getResult({
//                type: 'GET',
//                action: that.action.applylist,
//                data: {page: 1, size: 20},
//                success: function (result) {
//                    console.log(result);
//                    that.allrefer = result.data;
//                    that.initRefer();
//                    $('#payment-detail').bootstrapTable("load", that.allrefer);
//                    return that.allrefer;
//                }
//            });
//
//           //$('#payment-detail').bootstrapTable('load', null);
            return that.allrefer;
        },




        //提交处理
        changestates:function (id) {
            var that = this;
            Izb.ui.confirm("您确定要提交吗？", function () {
                Izb.common.getResult({
                    action: that.action.submit,
                    data: {_id: id},
                    success: function (result) {
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();

                    }
                });
            });
        },
        //回收处理
        rollbackSubmit:function (id) {
            var that = this;
            Izb.ui.confirm("您确定要回收吗？", function () {
                Izb.common.getResult({
                    action: that.action.rollbackSubmit,
                    data: {_id: id},
                    success: function (result) {
                        Izb.ui.tips("回收成功！", 'succeed');
                        Izb.main.refresh();
                    }
                });
            });
        },

        //审核处理
        audit:function (id) {
            var that = this;
            Izb.ui.confirm("您确定要审核吗？", function () {
                Izb.common.getResult({
                    action: that.action.audit,
                    data: {_id: id},
                    success: function (result) {
                        Izb.ui.tips("操作成功！", 'succeed');
                        Izb.main.refresh();

//                        //跳转到批量支付页面
//                        window.open('alipaymain.html?id=' + id);
//                        //支付完成后刷新页面
//                        Izb.ui.confirm1("支付完成,刷新页面!", function() {
//                            Izb.main.refresh();
//                        });

                        Izb.controller.payment.toAlipay(id);

                    }
                });
            });
        },
           //跳转到支付宝
        toAlipay : function(id){
            //跳转到批量支付页面
            window.open('alipaymain.html?id=' + id);
            //支付完成后刷新页面
            Izb.ui.confirm1("支付完成,刷新页面!", function() {
                Izb.main.refresh();
            });
        }

    });

}(Izb));


//member vip
$.module("Izb.controller.askdata",function(){

    return new Controller({
        pty:{
            name : '问答数据表',
            key : 'askdata',
            itemName : '问答数据表'
        },
        //接口
        action:{
            list:"/askdata/list"

        },
        //模板Id
        tpl:{
            header:'tpl-askdata-header',
            content:'tpl-askdata-list',
            input:'tpl-askdata-input'
        },
        event: {
            onBeforeList: function (data) {

                if (!data.stime) {
                    data.stime = Izb.ui.getToday();
                    $("#stime").val(data.stime);
                }

                if (!data.etime) {
                    data.etime = Izb.ui.getTodayEnd();
                    $("#etime").val(data.etime);
                }
            }

        }
    } );
}(Izb));

//currency vip
$.module("Izb.controller.currency",function(){

    return new Controller({
        pty:{
            name : '充值明细表',
            key : 'currency',
            itemName : '充值明细表'
        },
        //接口
        action:{
            list:"/currency/list"

        },
        //模板Id
        tpl:{
            header:'tpl-currency-header',
            content:'tpl-currency-list'
        },
        event: {
            onBeforeList: function (data) {

                if (!data.stime) {
                    data.stime = Izb.ui.getYesterday();
                    $("#stime").val(data.stime);
                }

                if (!data.etime) {
                    data.etime = Izb.ui.getYesterdayEnd();
                    $("#etime").val(data.etime);
                }
            }

        }
    } );
}(Izb));

//bonus vip
$.module("Izb.controller.bonusdata",function(){

    return new Controller({
        pty:{
            name : '红包数据表',
            key : 'bonusdata',
            itemName : '红包数据表'
        },
        //接口
        action:{
            list:"/bonusdata/list"

        },
        //模板Id
        tpl:{
            header:'tpl-bonusdata-header',
            content:'tpl-bonusdata-list'
        },
        event: {
            onBeforeList: function (data) {

                if (!data.stime) {
                    data.stime = Izb.ui.getYesterday();
                    $("#stime").val(data.stime);
                }

                if (!data.etime) {
                    data.etime = Izb.ui.getYesterdayEnd();
                    $("#etime").val(data.etime);
                }
            }

        }
    } );
}(Izb));

//bonus vip
$.module("Izb.controller.redpacket",function(){

    return new Controller({
        pty:{
            name : '提现奖金表',
            key : 'redpacket',
            itemName : '提现奖金表'
        },
        //接口
        action:{
            list:"/redpacket/list"

        },
        //模板Id
        tpl:{
            header:'tpl-redpacket-header',
            content:'tpl-redpacket-list',
            input:'tpl-redpacket-input'
        },
        event: {
            onBeforeList: function (data) {

                if (!data.stime) {
                    data.stime = Izb.ui.getYesterday();
                    $("#stime").val(data.stime);
                }

                if (!data.etime) {
                    data.etime = Izb.ui.getYesterdayEnd();
                    $("#etime").val(data.etime);
                }
            }

        }
    } );
}(Izb));


//bonus vip
$.module("Izb.controller.campusdata",function(){

    return new Controller({
        pty:{
            name : '校区数据表',
            key : 'campusdata',
            itemName : '校区数据表'
        },
        //接口
        action:{
            list:"/campusdata/list"

        },
        //模板Id
        tpl:{
            header:'tpl-campusdata-header',
            content:'tpl-campusdata-list',
            input:'tpl-campusdata-input'
        },
        event: {
            onBeforeList: function (data) {

                if (!data.stime) {
                    data.stime = Izb.ui.getYesterday();
                    $("#stime").val(data.stime);
                }

                if (!data.etime) {
                    data.etime = Izb.ui.getYesterdayEnd();
                    $("#etime").val(data.etime);
                }
            }

        }
    } );
}(Izb));

//topics 提问 2015-12-28 add by shihongjie
$.module("Izb.controller.topics",function(){

    return new Controller({
            pty:{
                name : '提问',
                key : 'topics',
                itemName : '提问'
            },
            //接口
            action:{
                list:"/topics/list",
                retroactive_bunus:"/topics/retroactive_bunus",
                topics_reply_list : "/topics/topics_reply_list"
            },
            //模板Id
            tpl:{
                header:'tpl-topics-header',
                content:'tpl-topics-list',
                topic_reply_list:'tpl-topics-topic_reply_list'
            },
            event: {
                onBeforeList: function (data) {

                    if (!data.stime) {
                        data.stime = Izb.ui.getToday();
                        $("#stime").val(data.stime);
                    }

                    if (!data.etime) {
                        data.etime = Izb.ui.getTodayEnd();
                        $("#etime").val(data.etime);
                    }
                }
            }
        }, {
            topicReplyInfo: function (pmap) {
                var topic_id = pmap._id;
                var that = this;
                //获取聊天内容
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.topics_reply_list,
                    data: {"topic_id" : topic_id},
                    success: function (result) {
                        if(result.code == 1){
                            //标题
                            var title = "聊天内容";
                            //参数
                            var params = {"data" : result.data};
                            //聊天模板
                            var dialog = Izb.ui.showDialogByTpl(that.tpl.topic_reply_list, title, params, function() {

                                return false;
                            });

                            Izb.ui.setFormData(that.formName,params);
                        }else{
                            Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                        }

                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            },
            retroactive_bunus : function(topic_id){
                var that = this;
                Izb.ui.confirm("确认补发红包吗？",function(){
                    Izb.common.getResult({
                        type: 'POST',
                        action:that.action.retroactive_bunus,
                        data: {"topic_id" : topic_id},
                        success:function(result){
                            Izb.ui.tips("操作成功！",'succeed');
                            Izb.main.refresh();
                        }
                    });
                });
            }
        }
    );
}(Izb));


//teacher 教师 2016-01-18 add by shihongjie
$.module("Izb.controller.teacher",function(){

    return new Controller({
            pty:{
                name : '教师信息',
                key : 'teacher',
                itemName : '教师信息'
            },
            //接口
            action:{
                list:"/teacher/list"
            },
            //模板Id
            tpl:{
                header:'tpl-teacher-header',
                content:'tpl-teacher-list'
            },
            event: {
                onBeforeList: function (data) {

                    if (!data.stime) {
                        data.stime = Izb.ui.getToday();
                        $("#stime").val(data.stime);
                    }

                    if (!data.etime) {
                        data.etime = Izb.ui.getTodayEnd();
                        $("#etime").val(data.etime);
                    }
                }
            }
        }, {

        }
    );
}(Izb));



/*
 * mission:校区管理
 */
$.module("Izb.controller.schooluserreport",function(){

    return new Controller({
        pty:{
            name : '校区注册情况报表',
            itemName : '校区注册情况报表',
            key  : 'schooluserreport'
        },
        //接口
        action:{

            list:'/schooluserreport/list'
        },
        //模板Id
        tpl:{
            header:'tpl-schooluserreport-header',
            content:'tpl-schooluserreport-list'
        }
    });

});

/*
 * mission:校区反馈
 */
$.module("Izb.controller.schoolfeedbackreport",function(){

    return new Controller({
        pty:{
            name : '校区反馈情况报表',
            itemName : '校区反馈情况报表',
            key  : 'schoolfeedbackreport'
        },
        //接口
        action:{
            list:'/schoolfeedbackreport/list'
        },
        //模板Id
            tpl:{
            header:'tpl-schoolfeedback-header',
            content:'tpl-schoolfeedback-list'
        }
    });

});

/*
 * mission:大区管理
 */
$.module("Izb.controller.areauserreport",function(){

    return new Controller({
        pty:{
            name : '大区注册情况报表',
            itemName : '大区注册情况报表',
            key  : 'areauserreport'
        },
        //接口
        action:{

            list:'/areauserreport/list'
        },
        //模板Id
        tpl:{

            content:'tpl-areauserreport-list'
        }
    });

});

/*
 * mission:省份管理
 */
$.module("Izb.controller.provinceuserreport",function(){

    return new Controller({
        pty:{
            name : '省份注册情况报表',
            itemName : '省份注册情况报表',
            key  : 'provinceuserreport'
        },
        //接口
        action:{

            list:'/provinceuserreport/list'
        },
        //模板Id
        tpl:{

            content:'tpl-provinceuserreport-list'
        }
    });

});




/*
 * students;学员信息
 */
$.module("Izb.controller.students",function(){

    return new Controller({
        pty:{
            name : '学员信息',
            itemName : '学员信息',
            key  : 'students'
        },
        //接口
        action:{
            list:'/students/list'
        },
        //模板Id
        tpl:{
            header:'tpl-students-header',
            content:'tpl-students-list'
        }
    });

});


/*
 * teachertopicdata;教师抢答报表
 */
$.module("Izb.controller.teachertopicdata",function(){

    return new Controller({
        pty:{
            name : '教师抢答报表',
            itemName : '教师抢答报表',
            key  : 'teachertopicdata'
        },
        //接口
        action:{
            list:'/teachertopicdata/list'
        },
        //模板Id
        tpl:{
            header:'tpl-teachertopicdata-header',
            content:'tpl-teachertopicdata-list'
        },
        event: {
            onBeforeList: function (data) {

                if (!data.stime) {
                    data.stime = Izb.ui.getToday();
                    $("#stime").val(data.stime);
                }

                if (!data.etime) {
                    data.etime = Izb.ui.getTodayEnd();
                    $("#etime").val(data.etime);
                }
            }
        }
    });

});
/*
 * teachertopicdata;学生提问报表
 */
$.module("Izb.controller.studenttopicdata",function(){

    return new Controller({
        pty:{
            name : '学生提问报表',
            itemName : '学生提问报表',
            key  : 'studenttopicdata'
        },
        //接口
        action:{
            list:'/studenttopicdata/list'
        },
        //模板Id
        tpl:{
            header:'tpl-studenttopicdata-header',
            content:'tpl-studenttopicdata-list'
        },
        event: {
            onBeforeList: function (data) {

                if (!data.stime) {
                    data.stime = Izb.ui.getToday();
                    $("#stime").val(data.stime);
                }

                if (!data.etime) {
                    data.etime = Izb.ui.getTodayEnd();
                    $("#etime").val(data.etime);
                }
            }
        }
    });

});
/*
 * 校区公告管理:schoolactivity
 */
$.module("Izb.controller.schoolactivity",function(){

    return new Controller({
        pty:{
            name : '校区公告管理',
            key : 'schoolactivity',
            itemName : '校区公告'
        },
        //接口
        action:{
            list:'/schoolactivity/list',
            add:'/schoolactivity/add',
            edit:'/schoolactivity/edit',
            del:'/schoolactivity/del',
            enable:'/schoolactivity/enable',
            disable:'/schoolactivity/disable'

        },
        //模板Id

        tpl:{
            header:'tpl-schoolactivity-header',
            input:'tpl-schoolactivity-input',
            content:'tpl-schoolactivity-list'
        },
        event:{
            onAddRender : function(){
                Izb.ui.initschoolselect();
                Izb.controller.articles.editor = KindEditor.create('#activity_info',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                    this.loadPlugin('autoheight');
                }});
            },
            onBeforeSaveEdit :function(data){
                data.activity_info = Izb.controller.articles.editor.html();
            },
            onBeforeAdd :function(data){
                data.activity_info = Izb.controller.articles.editor.html();
            },
            onEditRender : function(data){
                Izb.controller.articles.editor = KindEditor.create('#activity_info',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                    this.loadPlugin('autoheight');
                }});
            },onBeforeEdit : function (data) {
                Izb.ui.initschoolselect_edit(data.school_code);
            }
        }
    }, {
        enable:function(_id){

            var that = this;
            Izb.ui.confirm("您确定要启用吗？", function () {
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.enable,
                    data: {"_id":_id},
                    success: function (result) {
                        Izb.main.refresh();
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });
        },
        disable:function(_id){

            var that = this;

            Izb.ui.confirm("您确定要停用吗？", function () {
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.disable,
                    data: {"_id":_id},
                    success: function (result) {
                        Izb.main.refresh();
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });
        }
    });

}(Izb));

//学分标准(credit used)
$.module("Izb.controller.creditstandard",function(){
    return new Controller({
        pty:{
            name : '学分标准',
            key : 'creditstandard',
            itemName : '学分标准列表'
        },
        //接口
        action:{
            list:"/creditstandard/list",
            update:"/creditstandard/update",
            add:"/creditstandard/add",
            del:"/creditstandard/del"
        },
        //模板Id
        tpl:{
            header:'tpl-creditstandard-header',
            content:'tpl-creditstandard-list',
            input:'tpl-creditstandard-input'
        },
        event:{
        	//点击新增按钮的时候
            onAddRender:function(_data){
            	//定义表结构
            	Izb.controller.creditstandard.initDetail();
            },
            onBeforeAdd:function(_data){
                Izb.controller.creditstandard.saveJson(_data);
            },
        	onClassBindClick : function(){
        		var cjs = "/scripts/tpl/creditstandard.js"; 
        		$.ajax({
                    url: cjs,
                    cache:false,
                    async:false,
                    dataType:"script",
                    success:function(result){
                        console.log("finished loading " +  cjs);
                    }
                });
            },
            onAfterDel : function () {
               Izb.main.refresh();
            }
        }
    }, {
    	initDetail:function(){
            $('#creditstandard-detail').bootstrapTable({
                method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'course_code',
                    title: '科目编码',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'nc_name',
                    title: '科目名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'nc_id',
                    title: 'id',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
        },
         //点击查询按钮
        queryDetail:function(){
			var q_subject_name = $("#creditstandard-detail-query-table").find("#q_subject_name").val();
			var q_subject_code = $("#creditstandard-detail-query-table").find("#q_subject_code").val();
			
			  Izb.common.getResult({
	                    type: 'POST',
	                    action: '/creditstandard/query',
	                    data:{ subject_name: q_subject_name,subject_code: q_subject_code},
	                    success: function (result) {
	                         $('#creditstandard-detail').bootstrapTable('load',result.data);
	                    },
	                    error: function (xhr, status, result) {
	                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
	                    }
	                });
        },
         saveJson: function (_data) {
             var selets =  $('#creditstandard-detail').bootstrapTable('getSelections');
            _data.detailjson = JSON.stringify(selets);
        },
        //保存
    	update:function(){
        	console.log(credits);
            var that = this;
            var data;
            Izb.ui.confirm("您确定要保存吗？", function () {
	           Izb.common.getResult({
	                    type: 'POST',
	                    action: that.action.update,
	                    data: JSON.stringify(credits),
	                    success: function (result) {
	                        Izb.main.refresh();
	                    },
	                    error: function (xhr, status, result) {
	                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
	                    }
	                });
            });
        }
    });
}(Izb));


// 学分学习建议
$.module("Izb.controller.creditlearningtip",function(){
    return new Controller({
        pty:{
            name : '学习建议',
            key : 'creditlearningtip',
            itemName : '学习建议'
        },
        //接口
        action:{
            list:"/creditlearningtip/list",
            update:"/creditlearningtip/update",
            add:"/creditlearningtip/add",
            editTip:"/creditlearningtip/editTip",
            del:"/creditlearningtip/del"
        },
        //模板Id
        tpl:{
            header:'tpl-creditlearningtip-header',
            content:'tpl-creditlearningtip-list',
            input:'tpl-creditlearningtip-input'
        },
        event:{
        	//点击新增按钮的时候
            onAddRender:function(_data){
            	//定义表结构
            	Izb.controller.creditlearningtip.initDetail();
            },
            onBeforeAdd:function(_data){
                Izb.controller.creditlearningtip.saveJson(_data);
            }
        }
    }, {
    	initDetail:function(){
            $('#creditlearningtip-detail').bootstrapTable({
                method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'code',
                    title: '班级编码',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'nc_name',
                    title: '班级名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'nc_id',
                    title: 'id',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
        },
         //点击查询按钮
        queryDetail:function(){
			var q_class_name = $("#creditlearningtip-detail-query-table").find("#q_class_name").val();
			  Izb.common.getResult({
	                    type: 'POST',
	                    action: '/creditlearningtip/query',
	                    data:{ class_name: q_class_name},
	                    success: function (result) {
	                         $('#creditlearningtip-detail').bootstrapTable('load',result.data);
	                    },
	                    error: function (xhr, status, result) {
	                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
	                    }
	                });
        },
         saveJson: function (_data) {
             var selets =  $('#creditlearningtip-detail').bootstrapTable('getSelections');
            _data.detailjson = JSON.stringify(selets);
        },
        //保存
    	update:function(){
        	console.log(credits);
            var that = this;
            var data;
            Izb.ui.confirm("您确定要保存吗？", function () {
	           Izb.common.getResult({
	                    type: 'POST',
	                    action: that.action.update,
	                    data: JSON.stringify(credits),
	                    success: function (result) {
	                        Izb.main.refresh();
	                    },
	                    error: function (xhr, status, result) {
	                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
	                    }
	                });
            });
       },
	 	editTip : function(id,tip){
	 	var that = this;
	 	var tips_msg ='<table width="100%"><tr>';
	 	tips_msg +='<td valign="middle">';
	 	tips_msg += "学习建议:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	 	tips_msg +='</td>';
	 	tips_msg +='<td row="5">';
	 	tips_msg +="<textarea name='learnlingtip'  id='learnlingtip' style='width:400px;height:200px'>"+tip+"</textarea>";
	 	tips_msg +='</td>';
	 	tips_msg +='</tr></table>';
        Izb.ui.confirm(tips_msg ,function(){
        	var learnlingtip =  $("#learnlingtip").val();
            Izb.common.getResult({
                type: 'POST',
                action:that.action.editTip,
                data: {"id" :id,"learnlingtip" :learnlingtip},
                success:function(result){
                    //Izb.ui.tips("修改成功！",'succeed');
					Izb.main.refresh();
                },
                error: function (result) {
                	//Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                	//Izb.ui.alert("启动失败", "信息提示", "error")
                	Izb.ui.tips("修改失败！",'error');
                }
            });
        })
    }
    	 
    });
}(Izb));



// 学分记录-经理以上【卡片】 creditrecordcard by yzc 2017年2月16日16:30:41
$.module("Izb.controller.creditrecordcard",function(){
    return new Controller({
        pty:{
            name : '学分记录-经理以上卡片',
            key : 'creditrecordcard',
            itemName : '学分记录-经理以上卡片',
            menu:"creditrecord"
        },
        //接口
        action:{
        	list:"/creditrecord/show"
        },
        //模板Id
        tpl:{
            header:'tpl-creditrecordcard-header',
            content:'tpl-creditrecordcard-list',
            input:'tpl-creditrecordcard-input'
        },
        event:{
        
        }
    }, {
    });
}(Izb));


// 学分记录-经理以上   creditrecord by yzc 2017年2月16日16:30:41
$.module("Izb.controller.creditrecord",function(){
    return new Controller({
        pty:{
            name : '学分记录-经理以上',
            key : 'creditrecord',
            itemName : '学分记录-经理以上',
            autoLoad:false
        },
        //接口
        action:{
            list:"/creditrecord/list",
            add:"/creditrecord/add"
        },
        //模板Id
        tpl:{
            header:'tpl-creditrecord-header',
            content:'tpl-creditrecord-list',
            input:'tpl-creditrecord-input',
            showper:'tpl-creditrecord-showper'
        },
        event:{
          onAddRender:function(_data){
                //定义表结构
                Izb.controller.creditrecord.initDetail();
            }
        }
    }, {

      initDetail:function(studentName,phone,orgName,nc_user_id,class_id,class_name,signId){

                  
           $('#creditmessage-detail').bootstrapTable({
                method: 'POST',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'subjectName',
                    title: '科目',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, 
                {
                    field: 'attendanceActualScore',
                    title: '出勤实修',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, 
                {
                    field: 'attendanceClaimScore',
                    title: '出勤应修',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, 
                {
                    field: 'workActualScore',
                    title: '作业实修',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, 
                {
                    field: 'workClaimScore',
                    title: '作业应修',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, 
                {
                    field: 'examActualScore',
                    title: '结课考核实修',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, 
                {
                    field: 'examClaimScore',
                    title: '结课考核应修',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, 
                {
                    field: 'passRemark',
                    title: '是否合格',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, 
                {
                    field: 'checkRemark',
                    title: '是否考核',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }
                ]
            });  

                      Izb.common.getResult({
                    type: 'POST',
                    action: '/creditrecord/show',
                    data:{ "nc_user_id":nc_user_id,"signId":signId},
                    success: function (result) {
                      if(signId != null) {
                               
                         $('#creditmessage-detail').bootstrapTable('load',result.data);
                      }
              
                        
                    } 
                });

      }

    
    });
}(Izb));






// 学分运算  by yzc 2017年2月20日19:54:49
$.module("Izb.controller.creditoperation",function(){
    return new Controller({
        pty:{
            name : '学分运算',
            key : 'creditoperation',
            itemName : '学分运算'
        },
        //接口
        action:{
           list:"/creditoperation/list",
           start:"/creditoperation/start",
           executed:"/creditoperation/executed",
             runprocess:"/creditoperation/runprocess",
                        xfpercent:"/creditoperation/xfpercent"
           
        },
        //模板Id
        tpl:{
            header:'tpl-creditoperation-header',
            content:'tpl-creditoperation-list',
            input:'tpl-creditoperation-input',
            executed:'tpl-creditoperation-executed',
            runprocess:'tpl-creditoperation-runprocess',
            xfpercent:'tpl-creditoperation-xfpercent'
        },
     event:{
          //点击立即运算按钮的时候
            onExecutedRender:function(){
            	//定义表结构
            	Izb.controller.creditoperation.initDetail();

            }, 

             //点击立即运算按钮的时候
            onRunprocess:function(){
            	//定义表结构
            	Izb.controller.creditoperation.initDetailNew();

            }, 
             //点击生成学分单据按钮的时候
            OnXfpercent:function(){
                //定义表结构
                Izb.controller.creditoperation.initDetailxf();

            }, 

        }
    }, {
    	start : function(id,aBDate,aEDate,wBDate,wEDate,eBDate,eEDate,executeDate){
    		 var that = this;
    		var tips_msg ='<table width="100%"><tr>';
		 	tips_msg +='<td valign="middle">';
		 	tips_msg += "出勤取数日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上月";
		 	tips_msg +='</td>';
		 	tips_msg +='<td row="5">';
		 	tips_msg +="<input type='text' style='width:50px;' id = 'aBDate' value='"+aBDate+"'/>到" ;
		 	tips_msg +="<input type='text' style='width:50px;' id = 'aEDate' value='"+aEDate+"'/>" ;
		 	tips_msg +='</td>';
		 	tips_msg +='</tr>';
		 	
		 	tips_msg +='<tr>';
		 	tips_msg +='<td valign="middle">';
		 	tips_msg += "作业取数日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上上月";
		 	tips_msg +='</td>';
		 	tips_msg +='<td row="5">';
		 	tips_msg +="<input type='text' style='width:50px;' id = 'wBDate' value='"+wBDate+"'/>到上月" ;
		 	tips_msg +="<input type='text' style='width:50px;' id = 'wEDate' value='"+wEDate+"'/>" ;
		 	tips_msg +='</td>';
		 	tips_msg +='</tr>';
		 	
		 	tips_msg +='<tr>';
		 	tips_msg +='<td valign="middle">';
		 	tips_msg += "考试取数日期:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上上月";
		 	tips_msg +='</td>';
		 	tips_msg +='<td row="5">';
		 	tips_msg +="<input type='text' style='width:50px;' id = 'eBDate' value='"+eBDate+"'/>到上月" ;
		 	tips_msg +="<input type='text' style='width:50px;' id = 'eEDate' value='"+eEDate+"'/>" ;
		 	tips_msg +='</td>';
		 	tips_msg +='</tr>';
		 	
		 	tips_msg +='<tr>';
		 	tips_msg +='<td valign="middle">';
		 	tips_msg += "学分运算:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每月";
		 	tips_msg +='</td>';
		 	tips_msg +='<td row="5">';
		 	tips_msg +="<input type='text' id = 'executeDate' value='"+executeDate+"'/>";

            tips_msg +='</td>';
		 	tips_msg +='</tr>';


             tips_msg +='<tr>';
            tips_msg +='<td valign="middle">';
            tips_msg += "运算类型:&nbsp; ";
          
            tips_msg +='</td>';
           
                tips_msg +='<td row="5">';
                   tips_msg +="<input type='checkbox' id = 'ncsync'  checked='checked' />" ;
               tips_msg +="<input type='checkbox' id = 'tksync'   />" ;
                 tips_msg +="<input type='checkbox' id = 'allsync'  />" ;

            tips_msg +='</td>';
            tips_msg +='</tr>';


 
    	 	tips_msg +='</table>';


 
            Izb.ui.confirm(tips_msg ,function(){
            	var executeDate =  $("#executeDate").val();
            	var aBDate =  $("#aBDate").val();
		    	var aEDate =  $("#aEDate").val();
		    	var wBDate =  $("#wBDate").val();
		    	var wEDate =  $("#wEDate").val();
		    	var eBDate =  $("#eBDate").val();
		    	var eEDate =  $("#eEDate").val();
                Izb.common.getResult({
                    type: 'POST',
                    action:that.action.start,
                    data: {"execute_date" :executeDate,"id":id,"aBDate" :aBDate,"aEDate" :aEDate,"wBDate" :wBDate,"wEDate" :wEDate,"eBDate" :eBDate,"eEDate" :eEDate},
                    success:function(result){
                        Izb.ui.tips("启动成功！",'succeed');
						Izb.main.refresh();
                    },
                    error: function (result) {
                    	Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    	Izb.ui.alert("日期格式不对", "信息提示", "error")
	                }
                });
            })
        },
    	 
	    executed : function(){
   	 	 var that = this;
        Izb.ui.showDialogByTpl(this.tpl.executed, '立即执行', null, function() {
        	var _data = Izb.ui.getFormData("inputForm");
			var selets =  $('#q-standard-creditoperation-detail').bootstrapTable('getSelections');
        	_data.detailjson = JSON.stringify(selets);
            Izb.common.getResult({
                type: 'POST',
                action : that.action.executed,
                data : _data,
                timeout:600000,
                success : function(result){
                    Izb.ui.tips('执行成功！','face-smile');
                }
            });
        });
        if ($.isFunction(that.event.onExecutedRender)) {
            	that.event.onExecutedRender();
        	};
    },
 

//新的执行
  runprocess : function(){
   	 	 var that = this;
        Izb.ui.showDialogByTpl(this.tpl.runprocess, '新的执行', null, function() {
        	var _data = Izb.ui.getFormData("inputForm");
			var selets =  $('#q-standard-creditoperation-runprocess').bootstrapTable('getSelections');
        	_data.detailjson = JSON.stringify(selets);
            Izb.common.getResult({
                type: 'POST',
                action : that.action.runprocess,
                data : _data,
                timeout:600000,
                success : function(result){
                    Izb.ui.tips('执行成功！','face-smile');
                }
            });
        });
        if ($.isFunction(that.event.onRunprocess)) {
            	that.event.onRunprocess();
        	};
    },
    //学分中的生成学分完成表
    xfpercent : function(){
         var that = this;
        Izb.ui.showDialogByTpl(this.tpl.xfpercent, '生成学分数据单', null, function() {
            var _data = Izb.ui.getFormData("inputForm");
            var selets =  $('#q-standard-creditoperation-detail-xfpercent').bootstrapTable('getSelections');
            _data.detailjson = JSON.stringify(selets);
            Izb.common.getResult({
                type: 'POST',
                action : that.action.xfpercent,
                data : _data,
                timeout:600000,
                success : function(result){
                    Izb.ui.tips('执行成功！','face-smile');
                }
            });
        });
        if ($.isFunction(that.event.OnXfpercent)) {
                that.event.OnXfpercent();
            };
    },


    //点击查询按钮
    query_subject:function(){
		var q_subject_name = $("#standard-detail-query-table").find("#q_subject_name").val();
		var q_subject_code = $("#standard-detail-query-table").find("#q_subject_code").val();
		  Izb.common.getResult({
                    type: 'POST',
                    action: '/creditoperation/query_subject',
                    data:{ subject_name: q_subject_name,subject_code: q_subject_code},
                    success: function (result) {
                         $('#q-standard-creditoperation-detail').bootstrapTable('load',result.data);
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
    },
    initDetail:function(){
            $('#q-standard-creditoperation-detail').bootstrapTable({
                method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'course_code',
                    title: '科目编码',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'subject_name',
                    title: '科目名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'id',
                    title: 'id',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
        },

initDetailNew:function(){
            $('#q-standard-creditoperation-detail-runprocess').bootstrapTable({
                method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true 
            });
        },

 initDetailxf:function(){
            $('#q-standard-creditoperation-detail-xfpercent').bootstrapTable({
                method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true 
            });
        }



    });
}(Izb));



// 学分运算日志  by yzc 2017年2月20日19:54:49
$.module("Izb.controller.creditoperationlog",function(){
    return new Controller({
        pty:{
            name : '学分运算列表',
            key : 'creditoperationlog',
            itemName : '学分运算列表',
            menu:"creditoperation"
        },
        //接口
        action:{
        	 list:"/creditoperation/query_log"
        },
        //模板Id
        tpl:{
            header:'tpl-creditoperationlog-header',
            content:'tpl-creditoperationlog-list',
            input:'tpl-creditoperationlog-input'
        },
        event:{
         
        }
    }, {
    });
}(Izb));

//学分权限  by yzc 2017年2月20日19:54:49
$.module("Izb.controller.creditpermission",function(){
    return new Controller({
        pty:{
            name : '学分权限',
            key : 'creditpermission',
            itemName : '学分权限列表',
        },
        //接口
        action:{
            list:"/creditpermission/list",
            add_area:"/creditpermission/add_area",
            edit_area:"/creditpermission/edit_area",
            add_teacher:"/creditpermission/add_teacher",
            edit_teacher:"/creditpermission/edit_teacher",
            query_area_mongo:"/creditpermission/query_area_mongo",
            query_teacher_mongo:"/creditpermission/query_teacher_mongo"
        },
        //模板Id
        tpl:{
            header:'tpl-creditpermission-header',
            content:'tpl-creditpermission-list',
            input:'tpl-creditpermission-input',
            add_area:'tpl-creditpermission-add_area',
            edit_area:'tpl-creditpermission-edit_area',
            add_teacher:'tpl-creditpermission-add_teac',
            edit_teacher:'tpl-creditpermission-edit_teac'
        },
        event:{
        //点击添加校区按钮的时候
            onAdd_areaRender:function(_data){
            	//定义表结构
            	Izb.controller.creditpermission.initQueryAreaDtail(null);
            }, 
            
           //点击修改校区按钮的时候
            onEdit_areaRender:function(id){
            	//定义表结构
            	Izb.controller.creditpermission.initEditAreaDtail();
            	Izb.controller.creditpermission.query_area_permission(id);
            	
            }, 
            //点击添加老师按钮的时候
            onAdd_teacherRender:function(_data){
            	//定义表结构
            	Izb.controller.creditpermission.initQueryTeacherDtail(null);
            }, 
            
           //点击修改老师按钮的时候
            onEdit_teacherRender:function(id){
            	//定义表结构
            	Izb.controller.creditpermission.initEditTeacherDtail();
            	Izb.controller.creditpermission.query_teacher_permission(id);
            	
            }, 
        }
    }, {
    	//------------------------------------校区 begin--------------------------------------------//
    	add_area:function(id){
    	
    		 var that = this;
	        if (!id) {
	            return false;
	        }
    		var data = this.get(id), $form;
		     Izb.core.out(data);
            Izb.ui.showDialogByTpl(this.tpl.add_area, '添加校区权限',{ params: that.hashParams, pty: that.pty, data: data }, function() {
            	var _data = Izb.ui.getFormData("inputForm");
				var selets =  $('#q-area-permission-detail').bootstrapTable('getSelections');
            	_data.detailjson = JSON.stringify(selets);
                Izb.common.getResult({
                    type: 'POST',
                    action : that.action.add_area,
                    data : _data,
                    success : function(result){
                        Izb.ui.tips('修改成功！','face-smile');
                    }
                });
            });
            if ($.isFunction(that.event.onAdd_areaRender)) {
            	that.event.onAdd_areaRender();
        		};
        	Izb.ui.setFormData(that.formName, data);
        	$(".aui_border").find("input[name='name']").attr({"disabled":true});
        	$(".aui_border").find("input[name='nick_name']").attr({"disabled":true});
        }, 
        edit_area:function(id){
             var that = this;
	        if (!id) {
	            return false;
	        }
    		var data = this.get(id), $form;
		     Izb.core.out(data);
            Izb.ui.showDialogByTpl(this.tpl.edit_area, '修改校区权限', { params: that.hashParams, pty: that.pty, data: data }, function() {
            	var _data = Izb.ui.getFormData("inputForm");
				var alldata =  $('#e-area-permission-detail').bootstrapTable('getData');
            	_data.detailjson = JSON.stringify(alldata);
                Izb.common.getResult({
                    type: 'POST',
                    action : that.action.edit_area,
                    data : _data,
                    success : function(result){
                        Izb.ui.tips('修改成功！','face-smile');
                    }
                });
            });
            if ($.isFunction(that.event.onEdit_areaRender)) {
            		that.event.onEdit_areaRender(id);
        		}
            Izb.ui.setFormData(that.formName, data);
        	$(".aui_border").find("input[name='name']").attr({"disabled":true});
        	$(".aui_border").find("input[name='nick_name']").attr({"disabled":true});
        }, 
       //点击校区查询按钮
        queryAreaDetail:function(){
        	var that = this;
			var org_code = $("#area-detail-query-table").find("#q_org_code").val();
			var org_name = $("#area-detail-query-table").find("#q_org_name").val();
			var _id = $("#area-detail-query-table").find("#_id").val();
			  Izb.common.getResult({
	                    type: 'POST',
	                    action: that.action.query_area_mongo,
	                    data:{ org_code: org_code,org_name: org_name,_id:_id},
	                    success: function (result) {
	                         $('#q-area-permission-detail').bootstrapTable('load',result.data);
	                    },
	                    error: function (xhr, status, result) {
	                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
	                    }
	                });
        },
         //点击校区权限查询按钮
        query_area_permission:function(id){
        	var that = this;
        	Izb.core.out(id);
			  Izb.common.getResult({
	                    type: 'POST',
	                    action: '/creditpermission/query_area_permission',
	                    data:{ user_id: id},
	                    success: function (result) {
	                         $('#e-area-permission-detail').bootstrapTable('load',result.data);
	                    },
	                    error: function (xhr, status, result) {
	                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
	                    }
	                });
        },
        initQueryAreaDtail:function(){
        	var that = this;
            $('#q-area-permission-detail').bootstrapTable({
                method: 'get',
                url: '',//"/creditpermission/query_area_permission.json?user_id="+id,//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'code',
                    title: '校区编码',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'name',
                    title: '校区名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'nc_id',
                    title: '校区id',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
        },  
    	
    	delDetail:function(value,row,index){
           // var selects =  $('#e-area-permission-detail').bootstrapTable('getSelections');
            var ids = Array();
            ids.push(row.id);
            $('#e-area-permission-detail').bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
        },
    	
    	initEditAreaDtail:function(){
    		 function operateFormatter(value, row, index) {
                return [
                    '<a class="remove ml10" href="javascript:void(0)"  title="Remove">',
                    '<i class="glyphicon glyphicon-remove">删除</i>',
                    '</a>'
                ].join('');
            }
            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.creditpermission.delDetail(value,row,index);
                    console.log(value, row, index);
                }
            };
        	var that = this;
            $('#e-area-permission-detail').bootstrapTable({
                method: 'get',
                url: '',//"/creditpermission/query_area_permission.json?user_id="+id,//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'id',
                    title: '主键',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'orgCode',
                    title: '校区编码',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'orgName',
                    title: '校区名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'orgId',
                    title: '校区id',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                },{
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: false,
                    events: operateEvents,
                    formatter:operateFormatter
                }]
            });
        }, 
        //------------------------------------校区end--------------------------------------------//
        //------------------------------------老师 begin--------------------------------------------//
        add_teacher:function(id){
            var that = this;
	        if (!id) {
	            return false;
	        }
    		var data = this.get(id), $form;
		     Izb.core.out(data);
            Izb.ui.showDialogByTpl(this.tpl.add_teacher, '添加老师权限', { params: that.hashParams, pty: that.pty, data: data }, function() {
            	var _data = Izb.ui.getFormData("inputForm");
				var selets =  $('#q-teacher-permission-detail').bootstrapTable('getSelections');
            	_data.detailjson = JSON.stringify(selets);
                Izb.common.getResult({
                    type: 'POST',
                    action : that.action.add_teacher,
                    data : _data,
                    success : function(result){
                        Izb.ui.tips('修改成功！','face-smile');
                    }
                });
            });
            if ($.isFunction(that.event.onAdd_teacherRender)) {
            	that.event.onAdd_teacherRender();
        		}
             Izb.ui.setFormData(that.formName, data);
        	$(".aui_border").find("input[name='name']").attr({"disabled":true});
        	$(".aui_border").find("input[name='nick_name']").attr({"disabled":true});
        }, 
         edit_teacher:function(id){
              var that = this;
	        if (!id) {
	            return false;
	        }
    		var data = this.get(id), $form;
		     Izb.core.out(data);
            Izb.ui.showDialogByTpl(this.tpl.edit_teacher, '修改老师权限',  { params: that.hashParams, pty: that.pty, data: data }, function() {
            	var _data = Izb.ui.getFormData("inputForm");
				var alldata =  $('#e-teacher-permission-detail').bootstrapTable('getData');
            	_data.detailjson = JSON.stringify(alldata);
                Izb.common.getResult({
                    type: 'POST',
                    action : that.action.edit_teacher,
                    data : _data,
                    success : function(result){
                        Izb.ui.tips('修改成功！','face-smile');
                    }
                });
            });
            if ($.isFunction(that.event.onEdit_teacherRender)) {
            		that.event.onEdit_teacherRender(id);
        		}
             Izb.ui.setFormData(that.formName, data);
        	$(".aui_border").find("input[name='name']").attr({"disabled":true});
        	$(".aui_border").find("input[name='nick_name']").attr({"disabled":true});
        }, 
       //点击老师查询按钮
        queryTeacherDetail:function(){
        	var that = this;
			var teacher_name = $("#teaher-detail-query-table").find("#q_teacher_name").val();
			var teacher_phone = $("#teaher-detail-query-table").find("#q_teacher_phone").val();
			var _id = $("#teaher-detail-query-table").find("#_id").val();
			  Izb.common.getResult({
	                    type: 'POST',
	                    action: that.action.query_teacher_mongo,
	                    data:{ teacher_phone: teacher_phone,teacher_name: teacher_name,_id:_id},
	                    success: function (result) {
	                         $('#q-teacher-permission-detail').bootstrapTable('load',result.data);
	                    },
	                    error: function (xhr, status, result) {
	                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
	                    }
	                });
        },
         //点击校区权限查询按钮
        query_teacher_permission:function(id){
        	var that = this;
			  Izb.common.getResult({
	                    type: 'POST',
	                    action: '/creditpermission/query_teacher_permission',
	                    data:{ user_id: id},
	                    success: function (result) {
	                         $('#e-teacher-permission-detail').bootstrapTable('load',result.data);
	                    },
	                    error: function (xhr, status, result) {
	                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
	                    }
	                });
        },
        initQueryTeacherDtail:function(){
        	var that = this;
            $('#q-teacher-permission-detail').bootstrapTable({
                method: 'get',
                url: '',//"/creditpermission/query_area_permission.json?user_id="+id,//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'name',
                    title: '老师名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'telephone',
                    title: '老师电话',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'nc_id',
                    title: '老师id',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
        },  
    	
    	  delTeacherDetail:function(value,row,index){
           // var selects =  $('#e-area-permission-detail').bootstrapTable('getSelections');
            var ids = Array();
            ids.push(row.id);
            
            $('#e-teacher-permission-detail').bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
        },
    	
    	initEditTeacherDtail:function(){
    		 function operateFormatter(value, row, index) {
                return [
                    '<a class="remove ml10" href="javascript:void(0)"  title="Remove">',
                    '<i class="glyphicon glyphicon-remove">删除</i>',
                    '</a>'
                ].join('');
            }
            window.operateEvents = {
                'click .remove': function (e, value, row, index) {
                    Izb.controller.creditpermission.delTeacherDetail(value,row,index);
                    console.log(value, row, index);
                }
            };
        	var that = this;
            $('#e-teacher-permission-detail').bootstrapTable({
                method: 'get',
                url: '',//"/creditpermission/query_area_permission.json?user_id="+id,//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'id',
                    title: '主键',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'teacherPhone',
                    title: '老师电话',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'teacherName',
                    title: '老师名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'teacherId',
                    title: '老师id',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                },{
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: false,
                    events: operateEvents,
                    formatter:operateFormatter
                }]
            });
        }, 
          //------------------------------------老师 end--------------------------------------------//
        //更新全国权限
        edit_area_admin:function(id,type){
	        if (!id) {
	            return false;
	        }
      	Izb.common.getResult({
                 type: 'POST',
                 action: '/creditpermission/edit_area_admin',
                 data:{ _id: id,type:type},
                 success: function (result) {
                 	if(type == 1){
                 		Izb.ui.tips('添加成功！','face-smile');
                 	}else if(type == 0){
                 		Izb.ui.tips('取消成功！','face-smile');
                 	}
                      Izb.main.refresh();
                 },
                 error: function (xhr, status, result) {
                     Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                 }
             });
     }
    });
}(Izb));


//学分记录-教学老师 creditrecordteacher
$.module("Izb.controller.creditrecordteacher",function(){
    return new Controller({
        pty:{
            name : '学分记录-教学老师',
            key : 'creditrecordteacher',
            itemName : '学分记录-教学老师',
            autoLoad:false
        },
        //接口
        action:{
            list:"/creditrecordteacher/list"
        },
        //模板Id
        tpl:{
            header:'tpl-creditrecordteacher-header',
            content:'tpl-creditrecordteacher-list',
            input:'tpl-creditrecordteacher-input'
        },
        event:{
        
        }
    }, {
    });
}(Izb));


//月度学分统计表
$.module("Izb.controller.creditsubjectmon",function(){
    return new Controller({
        pty:{
            name : '月度学分统计表',
            key : 'creditsubjectmon',
            itemName : '月度学分统计表',
            autoLoad:false
        },
        //接口
        action:{
            list:"/creditsubjectmon/list"
        },
        //模板Id
        tpl:{
            header:'tpl-creditsubjectmon-header',
            content:'tpl-creditsubjectmon-list',
            input:'tpl-creditsubjectmon-input'
        },
        event:{
        
        }
    }, {
    });
}(Izb));

$.module("Izb.controller.creditclassmon",function(){
    return new Controller({
        pty:{
            name : '班型学分统计表 ',
            key : 'creditclassmon',
            itemName : '班型学分统计表 ',
            autoLoad:false
        },
        //接口
        action:{
            list:"/creditclassmon/list"
        },
        //模板Id
        tpl:{
            header:'tpl-creditclassmon-header',
            content:'tpl-creditclassmon-list',
            input:'tpl-creditclassmon-input'
        },
        event:{
        
        }
    }, {
    });
}(Izb));

$.module("Izb.controller.creditteachermon",function(){
    return new Controller({
        pty:{
            name : '老师学分统计表',
            key : 'creditteachermon',
            itemName : '老师学分统计表',
            autoLoad:false
        },
        //接口
        action:{
            list:"/creditteachermon/list"
        },
        //模板Id
        tpl:{
            header:'tpl-creditteachermon-header',
            content:'tpl-creditteachermon-list',
            input:'tpl-creditteachermon-input'
        },
        event:{
        
        }
    }, {
    });
}(Izb));
 
 $.module("Izb.controller.creditpercent",function(){
    return new Controller({
        pty:{
            name : '学分完成率报表',
            key : 'creditpercent',
            itemName : '学分完成率报表',
               autoLoad:false
       
        },
        //接口
        action:{
            list:"/creditpercent/list",
               detail:"/creditpercent/query_detail",
            add:"/creditpercent/add"
         
            
        },
        //模板Id
        tpl:{
            header:'tpl-creditpercent-header',
            content:'tpl-creditpercent-list',
            input:'tpl-creditpercent-input',
            showper:'tpl-creditpercent-showper'
        },
        event:{
//点击新增按钮的时候
            onAddRender:function(_data){
                //定义表结构
                Izb.controller.creditpercent.initDetail();
            },
        }
    }, {

    initDetail:function(){
            $('#creditpercent-detail').bootstrapTable({
                method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'dbilldate',
                    title: '年月',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'months',
                    title: '报名日期',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'mbpercent',
                    title: '目标完成率',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
        },

  //点击查询按钮
        queryDetail:function(){
            var dbilldate = $("#creditpercent-detail-query-table").find("#dbilldate").val();
              Izb.common.getResult({
                        type: 'POST',
                        action: '/creditpercent/query_permon',
                        data:{ dbilldate: dbilldate},
                        success: function (result) {
                             $('#creditpercent-detail').bootstrapTable('load',result.data);
                        },
                        error: function (xhr, status, result) {
                            Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                        }
                    });
        } ,
           details : function(orgName,xfPercent,mbpercent,remark,orgid,dbilldate,months,pid){
     
     

                    var href = "#a=creditpercentcard";
                    href += "&orgName="+orgName;
                    href += "&xfPercent="+xfPercent.replace("%","");
                    href += "&mbpercent="+mbpercent.replace("%",""); 
                    href += "&remark="+remark;
                    href += "&orgid="+orgid;
                    href += "&dbilldate="+dbilldate;
                    href += "&months="+months;
                       href += "&pid="+pid;
                    href += "&menu=creditpercent&tab=detail";
                    window.location.href = href;
                   
  
    } 







 
        
     
 
    });
}(Izb));


//学分排课完成率表
$.module("Izb.controller.creditpaike",function(){
    return new Controller({
        pty:{
            name : '学分排课率报表',
            key : 'creditpaike',
            itemName : '学分排课率报表',
               autoLoad:false
       
        },
        //接口  用的是跟学分完成率报表同一个接口
        action:{
            list:"/creditpercent/list",
               detail:"/creditpercent/query_detail",
            add:"/creditpercent/add"
         
            
        },
        //模板Id
        tpl:{
            header:'tpl-creditpaike-header',
            content:'tpl-creditpaike-list',
            input:'tpl-creditpaike-input',
            showper:'tpl-creditpaike-showper'
        },
        event:{
//点击新增按钮的时候
            onAddRender:function(_data){
                //定义表结构
                Izb.controller.creditpaike.initDetail();
            },
        }
    }, {

    initDetail:function(){
            $('#creditpaike-detail').bootstrapTable({
                method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'dbilldate',
                    title: '年月',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'months',
                    title: '报名日期',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'pkpercent',
                    title: '排课目标完成率',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
        },

  //点击查询按钮
        queryDetail:function(){
            var dbilldate = $("#creditpaike-detail-query-table").find("#dbilldate").val();
              Izb.common.getResult({
                        type: 'POST',
                        action: '/creditpercent/query_permon',
                        data:{ dbilldate: dbilldate},
                        success: function (result) {
                             $('#creditpaike-detail').bootstrapTable('load',result.data);
                        },
                        error: function (xhr, status, result) {
                            Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                        }
                    });
        },
         
           details : function(orgName,pkpercent,pkmbpercent,pkremark,orgid,dbilldate,months,pid,ts){
     
 

                    var href = "#a=creditpaikecard";
                    href += "&orgName="+orgName;
                    href += "&pkpercent="+pkpercent.replace("%","");
                    href += "&pkmbpercent="+pkmbpercent.replace("%",""); 
                    href += "&pkremark="+pkremark;
                    href += "&orgid="+orgid;
                    href += "&dbilldate="+dbilldate;
                    href += "&months="+months;
                       href += "&pid="+pid;
                          href += "&ts="+ts.replace(" ","_");
                    href += "&menu=creditpaike&tab=detail";
                    window.location.href = href;
             
  
    } 




 
        
     
 
    });
}(Izb));




// 学分制报表中的 学分完成率表的卡片
$.module("Izb.controller.creditpercentcard",function(){
    return new Controller({
        pty:{
            name : '学分完成率表明细',
            key : 'creditpercentcard',
            itemName : '学分完成率表明细'
           
   
        },
        //接口
        action:{
            list:"/creditpercent/show",
            add:"/creditpercent/querydefnot"
        },
        //模板Id
        tpl:{
            header:'tpl-creditpercentcard-header',
            content:'tpl-creditpercentcard-list',
            input:'tpl-creditpercentcard-input'
        },
        event:{
        //点击查看不考核的科目
            onAddRender:function(_data){
                //定义表结构
                Izb.controller.creditpaikecard.initDetail();
                Izb.controller.creditpaikecard.queryDetail();
            },
        }
    }, {
        
    });
}(Izb));

 
// 学分制报表中的 排课完成率表的卡片
$.module("Izb.controller.creditpaikecard",function(){
    return new Controller({
        pty:{
            name : '学分排课率表明细',
            key : 'creditpaikecard',
            itemName : '学分排课率表明细'
           
   
        },
        //接口
        action:{
            list:"/creditpercent/show",
            add:"/creditpercent/querydefnot"
        },
        //模板Id
        tpl:{
            header:'tpl-creditpaikecard-header',
            content:'tpl-creditpaikecard-list',
            input:'tpl-creditpaikecard-input'
        },
        event:{
        //点击查看不考核的科目
            onAddRender:function(_data){
                //定义表结构
                Izb.controller.creditpaikecard.initDetail();
                Izb.controller.creditpaikecard.queryDetail();
            },
        
        }
    }, {
 
 queryDetail:function(){
       
                     Izb.common.getResult({
                        type: 'POST',
                        action: '/creditpercent/querydefnot',
                        data:{},
                        success: function (result) {
                             $('#creditpaike-notass-detail').bootstrapTable('load',result.data);
                        },
                        error: function (xhr, status, result) {
                            Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                        }
                    });
        } , 



        //查看不算考核的科目
            initDetail:function(){
                 
            $('#creditpaike-notass-detail').bootstrapTable({
                   method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'defName',
                    title: '科目名称',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'ncCode',
                    title: '科目编码',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                } ]
            });
        }
    });
}(Izb));




//学分标准(credit used)
$.module("Izb.controller.creditcompletion",function(){
    return new Controller({
        pty:{
            name : '学分标准',
            key : 'creditcompletion',
            itemName : '学分标准列表'
        },
        //接口
        action:{
            list:"/creditcompletion/list",
            update:"/creditcompletion/update",
            add:"/creditcompletion/add",
            del:"/creditcompletion/del"
        },
        //模板Id
        tpl:{
            header:'tpl-creditcompletion-header',
            content:'tpl-creditcompletion-list',
            input:'tpl-creditcompletion-input'
        },
        event:{
            //点击新增按钮的时候
            onAddRender:function(_data){
                //定义表结构
                Izb.controller.creditcompletion.initDetail();
            },
            onBeforeAdd:function(_data){
                Izb.controller.creditcompletion.saveJson(_data);
            },
            onClassBindClick : function(){
                var cjs = "/scripts/tpl/creditcompletion.js"; 
                $.ajax({
                    url: cjs,
                    cache:false,
                    async:false,
                    dataType:"script",
                    success:function(result){
                        console.log("finished loading " +  cjs);
                    }
                });
            },
            onAfterDel : function () {
               Izb.main.refresh();
            }
        }
    }, {
        initDetail:function(){
            $('#creditcompletion-detail').bootstrapTable({
                method: 'get',
                url: 'data2.json',//'data2.json',
                cache: false,
                height: 400,
                striped: true,
                pagination: false,
                showColumns: true,
                showRefresh: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                columns: [{
                    field: 'state',
                    checkbox: true
                }, {
                    field: 'course_code',
                    title: '科目编码',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }, {
                    field: 'nc_name',
                    title: '科目名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'nc_id',
                    title: 'id',
                    align: 'left',
                    valign: 'top',
                    sortable: true
                }]
            });
        },
         //点击查询按钮
        queryDetail:function(){
            var q_subject_name = $("#creditcompletion-detail-query-table").find("#q_subject_name").val();
            var q_subject_code = $("#creditcompletion-detail-query-table").find("#q_subject_code").val();
            
              Izb.common.getResult({
                        type: 'POST',
                        action: '/creditcompletion/query',
                        data:{ subject_name: q_subject_name,subject_code: q_subject_code},
                        success: function (result) {
                             $('#creditcompletion-detail').bootstrapTable('load',result.data);
                        },
                        error: function (xhr, status, result) {
                            Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                        }
                    });
        },
         saveJson: function (_data) {
             var selets =  $('#creditcompletion-detail').bootstrapTable('getSelections');
            _data.detailjson = JSON.stringify(selets);
        },
        //保存
        update:function(){
            console.log(credits);
            var that = this;
            var data;
            Izb.ui.confirm("您确定要保存吗？", function () {
               Izb.common.getResult({
                        type: 'POST',
                        action: that.action.update,
                        data: JSON.stringify(credits),
                        success: function (result) {
                            Izb.main.refresh();
                        },
                        error: function (xhr, status, result) {
                            Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                        }
                    });
            });
        }
    });
}(Izb));










$.module("Izb.controller.applive",function(){

    return new Controller({
        pty:{
            name : 'app直播管理',
            key : 'applive',
            itemName : 'app直播管理'
        },
        //接口
        action:{
            list:'/applive/list',
            add:'/applive/add',
            edit:'/applive/edit',
            del:'/applive/del',
            submit:"/applive/submit",
            unsubmit:"/applive/unsubmit",
            audit:"/applive/audit",
            unaudit:"/applive/unaudit",
            stand:"/applive/stand"
        },
        //模板Id

        tpl:{
            header:'tpl-applive-header',
            input:'tpl-applive-input',
            content:'tpl-applive-list'
        },
        event:{
            onBeforeList: function (data) {
            },
            onBeforeAdd:function(data){
            },
            onAdd:function(){
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            },
            onAfterEdit:function(data,dialog){
            }
        }
    }, {
    });

}(Izb));


/**
 * 中级直播考勤汇总表:servicehome
 */
$.module("Izb.controller.middlelivesumreport",function(){

    //初始化Controller
    return new Controller({
        pty:{
            name : '中级直播考勤汇总表',
            itemName : '中级直播考勤汇总表',
            key  : 'middlelivesumreport'
        },
        //接口
        action:{
            list:'/middlelivesumreport/list'
        },
        //模板Id
        tpl:{
            content:'tpl-middlelivesumreport-list',
            header:'tpl-middlelivesumreport-header'
        },
        event:{
        }
    },{
    });

});


/*
 * 冷启动广告advertising
 */
$.module("Izb.controller.advertising",function(){

    return new Controller({
        pty:{
            name : '冷启动广告页',
            key : 'advertising',
            itemName : '冷启动广告页管理'
        },
        //接口
        action:{
            list:'/advertising/list',
            add:'/advertising/add',
            edit:'/advertising/edit',
            /*del:'/advertising/del'*/
        },
        //模板Id

        tpl:{
            header:'tpl-advertising-header',
            input:'tpl-advertising-input',
            content:'tpl-advertising-list'
        },
        event:{

        }
    }, {
    });
}(Izb));


//去重方法
Array.prototype.unique1 = function()
{
    var n = []; //一个新的临时数组
    for(var i = 0; i < this.length; i++) //遍历当前数组
    {
        //如果当前数组的第i已经保存进了临时数组，那么跳过，
        //否则把当前项push到临时数组里面
        if (n.indexOf(this[i]) == -1) n.push(this[i]);
    }
    return n;
}

/*
 *  方法:Array.remove1(dx) 通过遍历,重构数组
 *  功能:删除数组元素.
 *  参数:dx删除元素的下标.
 */
Array.prototype.remove1=function(dx)
{
    if(isNaN(dx)||dx>this.length){return false;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=this[dx])
        {
            this[n++]=this[i]
        }
    }
    this.length-=1
}

