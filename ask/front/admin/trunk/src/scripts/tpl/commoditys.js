/*
 * demo:mongodb例子
 */


    $.module("Izb.controller.commoditystab",function(){

        return new TabListController({
            pty:{
                name : '商品详情',
                itemName : '商品',
                key : 'commoditystab',
                menu:"commoditys"
            },
            //接口
            action:{
                detail:'/commoditys/detail',
                productedit:"/commoditys/productedit",
                saveproduct:"/commoditys/saveproduct",
                courselist:"/commoditys/courselist",
                courseedit:"/commoditys/courseedit",
                classify:"/commoditys/classify",
                pricelist:"/commoditys/pricelist",
                hmedit:"/commoditys/hmedit",
                apphmedit:"/commoditys/apphmedit",
                school:"/commoditys/school"
            },
            //模板Id

            tpl:{
                header:'tpl-commoditystab-header',
                detail:'tpl-commoditystab-detail',
                productedit:'tpl-commoditystab-productedit',
                courselist:'tpl-commoditystab-courselist',
                courseinput:'tpl-commoditystab-courseinput',
                classify:'tpl-commoditystab-classify',
                newclass:'tpl-commoditystab-newclass',
                newcourse:'tpl-commoditystab-newcourse',
                pricelist:'tpl-commoditystab-pricelist',
                hmedit:'tpl-commoditystab-hmedit',
                apphmedit:'tpl-commoditystab-apphmedit',
                school:'tpl-commoditystab-school'
            },
            event:{
                onBeforeList: function (data) {

                    Izb.common.getResult({
                        action: "/commoditys/getarea",
                        data: data,
                        success: function (result) {
                            if(result != null) {
                                area = result.data;
                                for(var index in area) {
                                    var html = $("#school_code").html();
                                    $("#school_code").html(html + '<option value ="'+area[index].nc_id+'">'+area[index].name+'</option>');
                                }
                            }
                        },
                        error: function (xhr, status, result) {
                        }
                    });
                },
                onAfterList: function (data) {
                    if(data != null){
                        if(data.commodity_type_list != null){
                            var that = this;
                            var _id = data._id;
                            var testJson = '{ "_id": "'+_id+'" }';
                            testJson = eval("(" + testJson + ")");
                            var map={
                                key1:'abc',
                                key2:'def'
                            };
                            Izb.common.getResult({
                                action: "/commoditys/courselist",
                                data: testJson,
                                async: false,
                                success: function (result) {
                                    var data = result.data;
                                    for(var i=0;i<data.length;i++){
                                        var key=data[i]._id;
                                        map[key]=data[i].course_code+' '+data[i].show_name;
                                    }
                                },
                                error: function (xhr, status, result) {
                                }
                            });
                            var commodity_type_list = new Array();
                            commodity_type_list = data.commodity_type_list;
                            for(var i=0;i<commodity_type_list.length;i++){
                                var tb_class_id = that.uuid();
                                var class_id = commodity_type_list[i]._id;
                                var name = commodity_type_list[i].name;
                                var course_list = new Array();
                                course_list = commodity_type_list[i].course_list;
                                $("#coursetable").append('<table id="'+tb_class_id+'"><tr>'
                                    +'<td class="auto" style="width:200px;" id="'+class_id+'">'+name+'</td>'
                                    +'<td class="auto"><button class="d-button" name="newcourse" value="newcourse" onclick="Izb.controller.commoditystab.newcourse('+tb_class_id+')">添加课程</button>'
                                    +'<button class="d-button" name="delate" value="delate" onclick="Izb.controller.commoditystab.moveTableUp(this)">上移</button>'
                                    +'<button class="d-button" name="delate" value="delate" onclick="Izb.controller.commoditystab.moveTableDown(this)">下移</button>'
                                    +'<button class="d-button" name="editclass" value="editclass" onclick="Izb.controller.commoditystab.editclass('+class_id+')">编辑</button>'
                                    +'<button class="d-button" name="deleteclass" value="deleteclass" onclick="Izb.controller.commoditystab.deleteclass('+tb_class_id+')">删除</button></td></tr></table>')
                                for(var j=0;j<course_list.length;j++){
                                    var tr_course_id = that.uuid();
                                    var course_id = course_list[j]._id;
                                    var commodity_courses_id = course_list[j].commodity_courses_id;
                                    $("#"+tb_class_id).append('<tr id="'+tr_course_id+'">'
                                        +'<td class="auto" style="width:200px;" data-course="'+commodity_courses_id+'" id="'+course_id+'">'+map[commodity_courses_id]+'</td>'
                                        +'<td class="auto">'
                                        +'<button class="d-button" name="delate" value="delate" onclick="Izb.controller.commoditystab.moveUp(this,'+tb_class_id+')">上移</button>'
                                        +'<button class="d-button" name="delate" value="delate" onclick="Izb.controller.commoditystab.moveDown(this,'+tb_class_id+')">下移</button>'
                                        +'<button class="d-button" name="deletecourse" value="deletecourse" onclick="Izb.controller.commoditystab.deletecourse('+tr_course_id+')">删除</button></td></tr>')
                                }
                            }
                        }
                        if(data.content != null){
                            var content = data.content;
                            var editor = KindEditor.create('#htmlcontent',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                                this.loadPlugin('autoheight');
                            },afterBlur: function(){this.sync();}});
                            editor.html(content);
                        }
                        if(data.appcontent != null){
                            var content = data.appcontent;
                            var editor = KindEditor.create('#apphtmlcontent',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                                this.loadPlugin('autoheight');
                            },afterBlur: function(){this.sync();}});
                            editor.html(content);
                        }
                        if(data.allmap != null){
//                        var firstmap = data.firstmap.data;
//                        var secondmap = data.secondmap.data;
//                        var thirdmap = data.thirdmap.data;
//                        var fourthmap = data.fourthmap.data;
//                        var fifthmap = data.fifthmap.data;
                            var allmap = data.allmap.data;
                            var checkschool = data.school_pks;
                            var schoolhtml;
                            var zTreeObj;
                            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
                            var setting = {
                                check: {
                                    enable: true
                                },
                                data: {
                                    simpleData: {
                                        enable: true,
                                        idKey: "nc_id",
                                        pIdKey: "parent_nc_id",
                                        rootPId: 0
                                    }
                                }
                            };
                            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, allmap);
                            for(var i=0;i<checkschool.length;i++){
                                var node = zTreeObj.getNodeByParam("nc_id", checkschool[i], null);
                                zTreeObj.checkNode(node, true, true);
                            }
                            var nodes = zTreeObj.getCheckedNodes(true);
                            for(var i=0;i<nodes.length;i++){
                                zTreeObj.expandNode(nodes[i]);
                            }
//                        for (var i = 0; i < fourthmap.length; i++) {
//                            for (var j = 0; j < thirdmap.length; j++) {
//                                if(thirdmap[j].nc_id == fourthmap[i].parent_nc_id){
//                                    for (var k = 0; k < secondmap.length; k++) {
//                                        if(secondmap[k].nc_id == thirdmap[j].parent_nc_id){
//                                            for (var z = 0; z < firstmap.length; z++) {
//                                                if(firstmap[z].nc_id == secondmap[k].parent_nc_id){
//                                                    schoolhtml += '<tr>'
//                                                        +'<td class="auto" style="width:200px;">'+firstmap[z].code+'</td>';
//                                                    break;
//                                                }
//                                            }
//                                            schoolhtml += '<td class="auto" style="width:200px;">'+secondmap[k].code+'</td>';
//                                            break;
//                                        }
//                                    }
//                                    schoolhtml += '<td class="auto" style="width:200px;">'+thirdmap[j].code+'</td>';
//                                    break;
//                                }
//                            }
//                            schoolhtml += '<td class="auto" style="width:200px;">'+fourthmap[i].code+'</td>';
//                            schoolhtml += '</tr>';
//                        }
//                        $("#schooltable").append(schoolhtml);
//                        //注：将表格按照第一级排序
//                        var table = document.getElementById("schooltable");
//                        var tbody = table.tBodies[0];
//                        var colRows = tbody.rows;
//                        var aTrs = new Array;
//                        var iCol = 2;
//                        var dataType = "String";
//
//                        //将将得到的列放入数组，备用
//                        for (var i=0; i < colRows.length; i++) {
//                            aTrs[i] = colRows[i];
//                        }
//
//
//                        //判断上一次排列的列和现在需要排列的是否同一个。
//                        if (table.sortCol == iCol) {
//                            aTrs.reverse();
//                        } else {
//                            //如果不是同一列，使用数组的sort方法，传进排序函数
//                            aTrs.sort(this.compareEle(iCol, dataType));
//                        }
//
//                        var oFragment = document.createDocumentFragment();
//
//                        for (var i=0; i < aTrs.length; i++) {
//                            oFragment.appendChild(aTrs[i]);
//                        }
//                        tbody.appendChild(oFragment);
//
//                        //记录最后一次排序的列索引
//                        table.sortCol = iCol;
//                        //注：将表格相同的列合并
//                        var tabObj = tbody;
//                        var colIndex = 0;
//                        var rowBeginIndex = 0;
//                        if (tabObj != null) {
//                            var i, j, m;
//
//                            var intSpan;
//                            var strTemp;
//                            m = 0;
//                            for (i = rowBeginIndex; i < tabObj.rows.length; i++) {
//                                intSpan = 1;
//                                m++;
//                                strTemp = tabObj.rows[i].cells[colIndex].innerText;
//                                for (j = i + 1; j < tabObj.rows.length; j++) {
//                                    if (strTemp == tabObj.rows[j].cells[colIndex].innerText) {
//                                        intSpan++;
//                                        tabObj.rows[i].cells[colIndex].rowSpan = intSpan;
//                                        tabObj.rows[j].cells[colIndex].style.display = "none";
//                                    }
//                                    else {
//                                        break;
//                                    }
//                                }
//
//                            }
//                            i = j - 1;
//                        }
                        }
                    }else{
                    }
                },
                convert: function (sValue, dataType) {
                    switch(dataType) {
                        case "int":
                            return parseInt(sValue);
                        case "float":
                            return parseFloat(sValue);
                        case "date":
                            return new Date(Date.parse(sValue));
                        default:
                            return sValue.toString();
                    }
                },
                compareEle: function (iCol, dataType) {
                    var that = this
                    return  function (oTR1, oTR2) {
                        var vValue1 = that.convert(oTR1.cells[iCol].firstChild.nodeValue, dataType);
                        var vValue2 = that.convert(oTR2.cells[iCol].firstChild.nodeValue, dataType);
                        if (vValue1 < vValue2) {
                            return -1;
                        } else if (vValue1 > vValue2) {
                            return 1;
                        } else {
                            return 0;
                        }
                    };
                },
                uuid: function () {
                    var s = [];
                    var hexDigits = "0123456789012345";
                    for (var i = 0; i < 12; i++) {
                        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
                    }
                    s[0] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
                    s[10] = hexDigits.substr((s[10] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
                    s[9] = s[6] = s[3] = s[1] ;

                    var uuid = s.join("");
                    return uuid;
                }
            }
        }, {
            //编辑
            courseedit: function (_id) {
                if (!this.action.courseedit || !this.tpl.courseinput) {
                    return;
                }
                var that = this,
                    title = "课程编辑";
                if (!_id) {
                    return false;
                }
                var data = this.get(_id), $form;
                Izb.core.out(data);

                var dialog = Izb.ui.showDialogByTpl(this.tpl.courseinput, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

                    //验证
                    if (!$form.valid()) {
                        return false;
                    }

                    var data = Izb.ui.getFormData("courseinputForm");

                    if ($.isFunction(that.event.onBeforeSaveEdit)) {
                        that.event.onBeforeSaveEdit(data);
                    }


                    Izb.core.out(data);
                    that.updatecourse(data);
                });

                if ($.isFunction(that.event.onBeforeEdit)) {
                    that.event.onBeforeEdit(data, dialog);
                }

                Izb.ui.setFormData("courseinputForm", data);

                if ($.isFunction(that.event.onEditRender)) {
                    that.event.onEditRender();
                }

                if ($.isFunction(that.event.onAfterEdit)) {
                    that.event.onAfterEdit(data, dialog);
                }

                //初始化验证
                $form = $('form[name=courseinputForm]');

                //if (!$.isFunction(that.event.onBindValidate)) {
                //	that.event.onBindValidate($form);
                //} else {
                //	$form.validate();
                //}
            },
            //更新课程
            updatecourse: function (data) {
                if (!this.action.courseedit || !data._id) {
                    return;
                }

                Izb.common.getResult({
                    type: 'POST',
                    action: this.action.courseedit,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });

            }
        });

}(Izb));
$.module("Izb.controller.commoditys",function(){

    return new Controller({
        pty:{
            name : '商品/课程序列',
            key : 'commoditys',
            itemName : '商品/课程序列'
        },
        //接口
        action:{
            list:'/commoditys/list',
            add:"/commoditys/add",
            edit:"/commoditys/edit",
            submit:"/commoditys/submit",
            unsubmit:"/commoditys/unsubmit",
            audit:"/commoditys/audit",
            unaudit:"/commoditys/unaudit",
            freeze:"/commoditys/freeze"
        },
        //模板Id

        tpl:{
            header:'tpl-commoditys-header',
            content:'tpl-commoditys-list',
            input:'tpl-commoditys-input'
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


        typeToString : function(type){
            var str = "未分类";
            if(type != undefined && type != null) {
                switch (type) {
                    case 1:
                        str = "会计上岗";
                        break;
                    case 2:
                        str = "会计考证";
                        break;
                    case 3:
                        str = "会计学历";
                        break;
                    case 4:
                        str = "经营会计";
                        break;
                    case 5:
                        str = "会计上岗(猎才计划)";
                        break;

                    default :
                        break;
                }
            }
            return str;
        }
    });
}(Izb));