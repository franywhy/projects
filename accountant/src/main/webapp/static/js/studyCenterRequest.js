var GetHtml = {
    //获取全部课程的单个li的html
    SingleCourse: function (school_name, last_study_time, progress, course_num, liveclass_num, course_down_num, type) {
        var strVar = '';
        strVar += '<div class="sc-fly-class" data-type="' + type + '" data-courseids="">';
        strVar += '    <h1><i>' + school_name + '</i></h1>';
        ////if (!last_study_time) {
        ////    strVar += '    <h2>未学习</h2>';
        ////}
        ////else {
        ////    strVar += '    <h2>上次学习时间：<i>' + last_study_time + '</i></h2>';
        ////}
        strVar += '    <div class="b-1 clearfix left">';
        strVar += '        <div class="b-1-l left">整体进度</div>';
        strVar += '        <div class="b-1-r right relative">';
        if (progress == 0) {
            strVar += '            <div class="sc-bar absolute sc-bar-0" style="width: ' + progress + '%;">';
        }
        else {
            strVar += '            <div class="sc-bar absolute" style="width: ' + progress + '%;">';
        }
        strVar += '                <p class="absolute">' + progress + '%<i class="absolute"></i></p>';
        strVar += '            </div>';
        strVar += '        </div>';
        strVar += '    </div>';
        strVar += '    <div class="b-2 right">';
        strVar += '        <ul class="clearfix">';
        strVar += '            <li class="left">';
        strVar += '                <h3>' + course_num + '</h3>';
        strVar += '                <h4>总课程</h4>';
        strVar += '            </li>';
        strVar += '            <i class="left"></i>';
        strVar += '            <li class="left">';
        strVar += '                <h3>' + liveclass_num + '</h3>';
        strVar += '                <h4>直播课</h4>';
        strVar += '            </li>';
        strVar += '            <i class="left"></i>';
        strVar += '            <li class="left">';
        strVar += '                <h3>' + course_down_num + '</h3>';
        strVar += '                <h4>录播课</h4>';
        strVar += '            </li>';
        strVar += '        </ul>';
        strVar += '    </div>';
        strVar += '    <a href="javascript:;" class="btnStartStudy">';
        strVar += '        <div class="main-btn absolute">开始学习</div>';
        strVar += '    </a>';
        strVar += '</div>';
        return strVar;
    },
    //获取直播课程列表的单个的html
    ZhiboCourse: function (name, teacher_name, time, state, live_url, livedetailid) {
        var strVar = '';
        strVar += '<div class="c-item clearfix">';
        strVar += '    <p class="t-p-1" title="' + name + '">《' + name + '》</p>';
        strVar += '    <p class="t-p-2">讲师：' + teacher_name + '</p>';
        if (isNaN(time)) {
            strVar += '    <p class="t-p-3">上课时间：' + time + '</p>';
        }
        else {
            strVar += '    <p class="t-p-3">时长：' + ConvertSecond(time) + '</p>';
        }
        //strVar += '    <p class="t-p-4">' + state + '</p>';
        var isliving = false;
        if (isNaN(time)) {
            if (new Date(Date.parse(time.replace(/-/g, "/"))) > new Date()) {
                strVar += '    <p class="t-p-4">未开始</p>';
            }
            else {
                strVar += '    <p class="t-p-4">直播中</p>';
                isliving = true;
            }
        }
        if (!!live_url && isliving) {
            strVar += '    <a target="_blank" href="' + live_url + '">';
            strVar += '        <p class="t-p-5 living-p hover-green">观看直播</p>';
            strVar += '    </a>';
        }
        else if (!!livedetailid && isliving) {
            strVar += '    <a target="_blank" href="/live/' + livedetailid + '.html">';
            strVar += '        <p class="t-p-5 living-p hover-green">观看直播</p>';
            strVar += '    </a>';
        }
        strVar += '</div>';
        return strVar;
    },
    //获取已结束课程列表的单个的html
    YijieshuCourse: function (name, teacher_name, time, state, live_url, livedetailid) {
        var strVar = '';
        strVar += '<div class="c-item clearfix">';
        strVar += '    <p class="t-p-1" title="' + name + '">《' + name + '》</p>';
        strVar += '    <p class="t-p-2">讲师：' + teacher_name + '</p>';
        if (isNaN(time)) {
            strVar += '    <p class="t-p-3">上课时间：' + time + '</p>';
        }
        else {
            strVar += '    <p class="t-p-3">时长：' + ConvertSecond(time) + '</p>';
        }
        strVar += '    <p class="t-p-4 has-ending">' + state + '</p>';
        if (!!live_url) {
            strVar += '    <a href="javascript:;" onclick="playVideo(\'' + live_url + '\')">';
            strVar += '        <p class="t-p-5 ending-p hover-green">课程回放</p>';
            strVar += '    </a>';
        }
        //if (!!livedetailid) {
        //    strVar += '    <a target="_blank" href="/live/' + livedetailid + '.html">';
        //    strVar += '        <p class="t-p-5 ending-p hover-green">课程回放</p>';
        //    strVar += '    </a>';
        //}
        strVar += '</div>';
        return strVar;
    },
    //获取在线录播中指定分类下的课程列表的单个的html
    LuboSingleCourse: function (course_name, courseid, list) {
        //console.info(course_name, courseid);
        var strVar = '';
        strVar += '<div class="section-course" style="display: block;">';
        strVar += '    <div class="c-head" title="' + course_name + '">' + course_name + '<i class="act"><img src="/images/icon/title-arrow.png"></i></div>';
        strVar += '    <div class="c-item-out">';
        $.each(list, function (idx, row) {
            strVar += '        <div class="c-item clearfix">';
            strVar += '            <p class="t-p-1" title="' + row.name + '">' + row.name + '</p>';
            strVar += '            <p class="t-p-2">讲师：' + row.teacher_name + '</p>';
            if (isNaN(row.time)) {
                strVar += '            <p class="t-p-3">上课时间：' + row.time + '</p>';
            }
            else {
                strVar += '            <p class="t-p-3">时长：' + ConvertSecond(row.time) + '</p>';
            }
            if (!!row.live_url) {
                strVar += '            <a target="_blank" href="' + row.live_url + '">';
                strVar += '                <p class="t-p-5 living-p hover-green">播放课程</p>';
                strVar += '            </a>';
            }
            else if (courseid > 0) {
                strVar += '            <a target="_blank" href="/UserManage/chapters_play_' + courseid + '_' + row.chapterid + '_.html">';
                strVar += '                <p class="t-p-5 living-p hover-green">播放课程</p>';
                strVar += '            </a>';
            }
            strVar += '        </div>';
        });
        strVar += '    </div>';
        strVar += '</div>';
        return strVar;
    },
    //获取在线录播中指定分类下的课程列表的单个的html（只有章没有节的情况）
    LuboSingleCourseByNoNode: function (course_name, courseid, row) {
        //console.info('LuboSingleCourseByNoNode', course_name, courseid, row);
        var strVar = '';
        strVar += '<div class="section-course" style="display: block;">';
        //strVar += '    <div class="c-head" title="' + course_name + '">' + course_name + '<i class="act"><img src="/images/icon/title-arrow.png"></i></div>';
        //strVar += '    <div class="c-item-out">';
        strVar += '    <div>';
        strVar += '        <div class="c-item clearfix">';
        strVar += '            <p class="t-p-1" title="' + row.name + '">' + row.name + '</p>';
        strVar += '            <p class="t-p-2">讲师：' + row.teacher_name + '</p>';
        if (isNaN(row.time)) {
            strVar += '            <p class="t-p-3">上课时间：' + row.time + '</p>';
        }
        else {
            strVar += '            <p class="t-p-3">时长：' + ConvertSecond(row.time) + '</p>';
        }
        if (!!row.live_url) {
            strVar += '            <a target="_blank" href="' + row.live_url + '">';
            strVar += '                <p class="t-p-5 living-p hover-green">播放课程</p>';
            strVar += '            </a>';
        }
        else if (courseid > 0) {
            strVar += '            <a target="_blank" href="/UserManage/chapters_play_' + courseid + '_' + row.chapterid + '_.html">';
            strVar += '                <p class="t-p-5 living-p hover-green">播放课程</p>';
            strVar += '            </a>';
        }
        strVar += '        </div>';
        strVar += '    </div>';
        strVar += '</div>';
        return strVar;
    }
};
//将秒数转换为分秒的显示形式
function ConvertSecond(s) {
    var time = parseInt(s);
    var m = parseInt(time / 60);
    var s = time % 60;
    return m + '分' + s + '秒';
};
//绑定课程的展开收缩事件
function bindExpandCourseListFunc() {
    scListSlide();
    dianSlide();
};
//在线录播的收缩
function bindExpandByLubo() {
    $(".sc-list-box .sc-body .course-box #LuboList .c-head").on("click", function () {
        if ($(this).find("i").hasClass("act")) {
            $(this).find("i").removeClass("act");
            $(this).siblings().stop(true, true).slideUp();
        } else {
            $(this).find("i").addClass("act");
            $(this).siblings().stop(true, true).slideDown();
        }
    });
};
//没有相关课程时的提示
function noCourseTips() {
    if ($('#studing_btn_zhibo').html() == '') {
        $('#studing_btn_zhibo').html('<div class="c-item-out" style="display: block;color:#999;"><div class="c-item clearfix"><p class="t-p-1 c-empty-class">暂无直播课程</p></div></div>');
    }
    if ($('#studing_btn_yijieshu').html() == '') {
        $('#studing_btn_yijieshu').html('<div class="c-item-out" style="display: block;color:#999;"><div class="c-item clearfix"><p class="t-p-1 c-empty-class">暂无直播回放</p></div></div>');
    }
};
//NC课程
function ShowNCCourse(NCSignListJson) {
    if (NCSignListJson.data.signData.length == 0 && NCSignListJson.data.liveList.length == 0 && NCSignListJson.data.endliveList.length == 0) {
        //alert('没有录播和直播课程');
    }
    else {
        //console.info('NCSignListJson', NCSignListJson);
        if (NCSignListJson.data.signData.length == 0) {
            return;
        }
        //课程显示新增一列
        var school_name = '【' + NCSignListJson.data.signData[0].school_name + '】空中教室'
        var progress = NCSignListJson.data.signData[0].progress;
        var course_num = 0, liveclass_num = 0, course_down_num = 0;
        $.each(NCSignListJson.data.signData, function (idx, row) {
            course_num += row.course_num;
            liveclass_num += row.liveclass_num;
            course_down_num += row.course_down_num;
        });
        var html = GetHtml.SingleCourse(school_name, '', progress, course_num, liveclass_num, course_down_num, 0)
        var $temp = $(html).appendTo('#AllCourse');
        //绑定"开始学习"事件
        $temp.find('.btnStartStudy').click(function () {
            //正在学习 - 顶部
            $('.main-bar-nav li:eq(0)').click();
            $('#studing_schoolname').html(school_name);
            $('#studing_progress').html(progress + '%').parent('.sc-bar').css('width', progress + '%');
            $('#studing_course_num').html(course_num);
            $('#studing_liveclass_num').html(liveclass_num);
            $('#studing_course_down_num').html(course_down_num);
            //正在学习 - 直播课程
            var html = '';
            $.each(NCSignListJson.data.liveList, function (idx, row) {
                html += GetHtml.ZhiboCourse(row.name, row.teacher_name, row.time, '', row.live_url);
            });
            $('#studing_btn_zhibo').html('').append(html);
            //正在学习 - 已结束课程
            var html = '';
            $.each(NCSignListJson.data.endliveList, function (idx, row) {
                html += GetHtml.YijieshuCourse(row.name, row.teacher_name, row.time, row.state, row.live_url);
            });
            $('#studing_btn_yijieshu').html('').append(html);
            //正在学习 - 在线录播
            $('#CommodityList').html('');
            $('#LuboList').html('<div class="c-item-out" style="display: block;color:#999;"><div class="c-item clearfix" style="border: none"><p class="t-p-1 c-empty-class">暂无课程</p></div></div>');
            $.each(NCSignListJson.data.signData, function (idx, row) {
                var $cur = $('<li><i><em></em></i>' + row.commodity_name + '</li>').appendTo('#CommodityList');
                $cur.click(function () {
                    var html = '';
                    $.each(row.downList, function (idx2, row2) {
                        //console.info(idx2, row2);
                        html += GetHtml.LuboSingleCourse(row2.course_name, '', row2.planList);
                    });
                    $('#LuboList').html(html);
                    bindExpandByLubo();//重新绑定事件
                });
            });
            //单击'在线录播'时默认点击第一个分类
            //$('.main-course-box-out .sc-nav .t-p-1').click();
            $('.main-course-box-out .sc-nav .t-p-2').click(function () {
                if ($('#CommodityList li.act').length == 0 && $('#CommodityList li').length > 0) {
                    $('#CommodityList li:eq(0)').addClass('act').click();
                }
            });
            $('.main-course-box-out .sc-nav .t-p-2').click();
            bindExpandCourseListFunc();//重新绑定事件
            noCourseTips();//没有相关课程时的提示

            $('p[data-online-zhibo]').text('考前冲刺');
            $('p[data-online-lubo]').text('面授课程');
        });
    }
};
//会计城课程
function showKjcityCourse(title, CourseListByLuboJson, CourseListByZhiboJson, course_down_num_count, liveclass_num_count, progress, type) {
    return showKjcityCourse2(title, CourseListByLuboJson.signData, CourseListByZhiboJson.liveclass_num, CourseListByZhiboJson.course_down_num, course_down_num_count, liveclass_num_count, progress, type);
};
function showKjcityCourse2(title, signData, liveclass_num_json, course_down_num_json, course_down_num_count, liveclass_num_count, progress, type) {
    if (signData.length == 0 && liveclass_num_json == null && course_down_num_json.length == 0) {
        //alert('没有录播和直播课程');
    }
    else {
        //课程显示新增一列
        var school_name = '【' + title + '】';
        var course_num = 0, liveclass_num = 0, course_down_num = 0;
        course_down_num = course_down_num_count;
        liveclass_num = liveclass_num_count;
        course_num = liveclass_num + course_down_num;
        var html = GetHtml.SingleCourse(school_name, '', progress, course_num, liveclass_num, course_down_num, type)
        var $temp = $(html).appendTo('#AllCourse');
        //绑定"开始学习"事件
        $temp.find('.btnStartStudy').click(function () {
            $(".sc-list-box .sc-body .course-box .c-head").unbind('click');
            //正在学习 - 顶部
            $('.main-bar-nav li:eq(0)').click();
            $('#studing_schoolname').html(school_name);
            $('#studing_progress').html(progress + '%').parent('.sc-bar').css('width', progress + '%');
            $('#studing_course_num').html(course_num);
            $('#studing_liveclass_num').html(liveclass_num);
            $('#studing_course_down_num').html(course_down_num);
            var html = '';
            $.each(liveclass_num_json, function (idx, row) {
                html += GetHtml.ZhiboCourse(row.name, row.teacher_name, row.time, '', row.live_url, row.chapterid);
            });
            $('#studing_btn_zhibo').html('').append(html);
            //正在学习 - 已结束课程
            var html = '';
            $.each(course_down_num_json, function (idx, row) {
                html += GetHtml.YijieshuCourse(row.name, row.teacher_name, row.time, '已结束', row.live_url, row.chapterid);
            });
            $('#studing_btn_yijieshu').html('').append(html);
            //正在学习 - 在线录播
            $('#CommodityList').html('');
            $('#LuboList').html('<div class="c-item-out" style="display: block;color:#999;"><div class="c-item clearfix" style="border: none"><p class="t-p-1 c-empty-class">暂无录播课程</p></div></div>');
            //console.info('signData', signData);
            $.each(signData, function (idx, row) {
                var $cur = $('<li><i><em></em></i>' + row.course_name + '</li>').appendTo('#CommodityList');
                $cur.click(function () {
                    var html = '';
                    $.each(row.coursechapter, function (idx2, row2) {
                        if (row2.coursenode.length > 0) {
                            html += GetHtml.LuboSingleCourse(row2.name, row.courseid, row2.coursenode);
                        }
                        else {
                            html += GetHtml.LuboSingleCourseByNoNode(row2.name, row.courseid, row2); //只有章没有节的情况
                        }
                    });
                    $('#LuboList').html(html);
                    bindExpandByLubo();//重新绑定事件
                });
            });
            //单击'在线录播'时默认点击第一个分类
            //$('.main-course-box-out .sc-nav .t-p-1').click();
            $('.main-course-box-out .sc-nav .t-p-2').click(function () {
                if ($('#CommodityList li.act').length == 0 && $('#CommodityList li').length > 0) {
                    $('#CommodityList li:eq(0)').addClass('act').click();
                }
            });
            $('.main-course-box-out .sc-nav .t-p-2').click();
            bindExpandCourseListFunc();//重新绑定事件
            noCourseTips();//没有相关课程时的提示

            $('p[data-online-zhibo]').text('在线直播');
            $('p[data-online-lubo]').text('在线录播');
        });
        return $temp;
    }
};