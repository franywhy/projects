<#compress>
	<#include "/common/top.ftl" encoding="UTF-8" parse=true>
<link rel="stylesheet" type="text/css" href="/resources/lib/boostrap/css/bootstrap.min.css">
		<body class="bg-f2">
			<#include "/common/header.ftl" encoding="UTF-8" parse=true>

        <div class="xxzx-container">
            <div class="con-width">
						<#include "../common/nav.ftl" encoding="UTF-8" parse=true>

                <div class="container-right">
                    <div class="curriculum-list">
                        <div class="fl">
                            <p class="curriculum-list-title" title="${RequestParameters.title}">${RequestParameters.title}</p>
                            <ul class="curriculum-list-prompt">
                                <li>
                                    <span class="curriculum-list-icon curriculum-list-icon-4"></span>
                                    <span class="ml-8px" title="${hearResult.recordName}">${hearResult.recordName}</span>
                                </li>
                                <li>
                                    <span class="curriculum-list-icon curriculum-list-icon-1"></span>
                                    <span class="ml-8px">${hearResult.learnTime}</span>
                                </li>
                            </ul>
                        </div>
                        <div class="fr">
                            <ul class="processingbar-list">
                                <li>
                                    <div class="processingbar">
                                        <span>${rateResult.progressRate}%</span>
                                    </div>
                                    <p class="text-center color-999">课程进度</p>
                                </li>
                            </ul>
                        </div>
                    </div>

								<#if recordCourseList??>
									<div class="curriculum mt-20px" id="curriculum">
										<#list 0..(recordCourseList!?size-1) as i>
                                            <div class="accordion">
                                                <div class="level-curriculum-title">${recordCourseList[i].courseName}</div>
												<#list 0..(recordCourseList[i].courseRecordDetailList!?size-1) as j>
													<div class="curriculum-inner-bar">
                                                        <div class="level-curriculum-list curriculum-inner-title">
															<#if recordCourseList[i].courseRecordDetailList[j].list??>
																<div class="fl">
                                                                    <div class="curriculum-name text-overflow b" style="width:450px;">
                                                                        <span class="book-icon bg-cover"></span>
                                                                        <span class="ml-11px" title="${recordCourseList[i].courseRecordDetailList[j].name}">${recordCourseList[i].courseRecordDetailList[j].name}</span>
                                                                    </div>
                                                                </div>
															<#else>
																	<div class="fl">
                                                                        <div class="curriculum-name text-overflow b">
                                                                            <span class="book-icon bg-cover"></span>
                                                                            <span class="ml-11px" title="${recordCourseList[i].courseRecordDetailList[j].name}">${recordCourseList[i].courseRecordDetailList[j].name}</span>
                                                                        </div>
                                                                    </div>
																	<div class="right-content">
																		<span class="name-bar">
																			<#if recordCourseList[i].courseRecordDetailList[j].teacherName??>
																				<span class="icon-p">
																					<span class="persion-icon bg-cover"></span>
																				</span>
																				<span class="teacher text-overflow">${recordCourseList[i].courseRecordDetailList[j].teacherName}</span>
																			<#else>
																					<span class="teacher text-overflow"></span>
																			</#if>
                                                                        </span>

                                                                        <span class="time-bar text-overflow">
																			<span class="curriculum-list-icon curriculum-list-icon-1 bg-cover"></span>
																			<span class="pl-5px">${recordCourseList[i].courseRecordDetailList[j].timeMin}</span>
																		</span>

                                                                        <span class="attendance-progress">
																			<span class="attendance-text">出勤</span>
																			<span class="progress-bar mr-40px" style="width: 78px">
																				<span class="progress-bg radius-8px" style="width: 78px">
																					<span class="progress radius-8px" style="width: ${recordCourseList[i].courseRecordDetailList[j].attentPer *100}%">
																						<span class="progress-text">${recordCourseList[i].courseRecordDetailList[j].attentPer *100}%</span>
																					</span>
																				</span>
																			</span>
																		</span>

																		<#assign detail=recordCourseList[i].courseRecordDetailList[j] />
																		<#if (detail.vid?? && detail.vid !='' ) || (detail.ccId?? && detail.ccId !='' )>
																			<a href="/learningCenter/web/record/play?recordId=${recordCourseList[i].courseRecordDetailList[j].recordId}&timestamp=${.now?string["
																			 hhmmSSsss"]}" class="btn-text" target="_blank">观看课程</a>
																		<#else>
																				<a href="javascript:;" class="btn-text disabled">观看课程</a>
																		</#if>
                                                                    </div>
															</#if>
															<#if recordCourseList[i].courseRecordDetailList[j].list??>
																<div class="right-content">
                                                                    <span class="black-arrow-icon-2 bg-cover down-arrow-icon"></span>
                                                                </div>
															</#if>
                                                        </div>
														<#list 0..(recordCourseList[i].courseRecordDetailList[j].list!?size-1) as k>
															<#if recordCourseList[i].courseRecordDetailList[j].list??>
																<div class="level-curriculum-list curriculum-inner-list hide">
                                                                    <div class="fl">
                                                                        <div class="curriculum-name text-overflow">
                                                                            <span class="ml-28px" title="${recordCourseList[i].courseRecordDetailList[j].list[k].name}">${recordCourseList[i].courseRecordDetailList[j].list[k].name}</span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="right-content">
																		<span class="name-bar">
																			<#if recordCourseList[i].courseRecordDetailList[j].teacherName??>
																				<span class="icon-p">
																					<span class="persion-icon bg-cover"></span>
																				</span>
																				<span class="teacher text-overflow">${recordCourseList[i].courseRecordDetailList[j].list[k].teacherName}</span>
																			<#else>
																					<span class="teacher text-overflow"></span>
																			</#if>
                                                                        </span>
                                                                        <span class="time-bar text-overflow">
																			<span class="curriculum-list-icon curriculum-list-icon-1 bg-cover"></span>
																			<span class="pl-5px">${recordCourseList[i].courseRecordDetailList[j].list[k].timeMin}</span>
																		</span>
                                                                        <span class="attendance-progress">
																			<span class="attendance-text">出勤</span>
																			<span class="progress-bar mr-40px" style="width: 78px">
																				<span class="progress-bg radius-8px" style="width: 78px">
																					<span class="progress radius-8px" style="width: ${recordCourseList[i].courseRecordDetailList[j].list[k].attentPer *100}%">
																						<span class="progress-text">${recordCourseList[i].courseRecordDetailList[j].list[k].attentPer*100}%</span>
																					</span>
																				</span>
																			</span>
																		</span>
																		<#assign listDetail=recordCourseList[i].courseRecordDetailList[j].list[k] />
																		<#if (listDetail.vid?? && listDetail.vid !='' ) || (listDetail.ccId?? && listDetail.ccId !='' )>
																			<a href="/learningCenter/web/record/play?recordId=${recordCourseList[i].courseRecordDetailList[j].list[k].recordId}&timestamp=${.now?string["
																			 hhmmSSsss"]}" class="btn-text" target="_blank" style="margin-right:20px" recordId="${listDetail.recordId}">观看课程</a>
																			<a href="" class="btn-text" target="_blank" style="margin: 0" data-toggle="modal" data-target="#downLoad" onclick="showFiles(${listDetail.recordId})">课堂资料</a>
																		<#else>
																				<a href="javascript:;" class="btn-text disabled" style="margin-right:20px">观看课程</a>
																				<a href="javascript:;" class="btn-text disabled">课堂资料</a>
																		</#if>
                                                                    </div>
                                                                </div>
															</#if>
														</#list>
                                                    </div>
												</#list>
                                            </div>
										</#list>
                                    </div>
								</#if>
                </div>

            </div>
            <!-- 模态框（Modal） -->
            <div class="modal fade" id="downLoad" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-container">
                    <img src="/resources/images/common/delete.png" class="close-icon" data-dismiss="modal" aria-hidden="true">
                    <p class="title">下载资料</p>
                    <div id="file-list-title">
                    </div>
                </div>
            </div>
        </div>

        <style>
            .modal-container {
                width: 557px;
                height: 650px;
                padding: 0 30px;
            }
            .modal-container .close-icon {
                width: 25px;
                height: 25px;
                cursor: pointer;
                position: absolute;
                top: 20px;
                right: 20px;
            }
            .modal-container .title{
                margin-top: 51px;
                font-family: MicrosoftYaHei;
                font-size: 22px;
                text-align: center;
                color: #333;
            }
            .modal-container .item{
                text-align: left;
                height: 48px;
                line-height: 48px;
                margin-top: 20px;
                position: relative;
                border-bottom: 1px solid #eeeeee;
            }
            .modal-container .item > span{
                display: inline-block;
                vertical-align: middle;
            }
            .modal-container .item .button{
                position: absolute;
                top: 0;
                bottom: 0;
                right: 0;
                margin: auto;
                width: 120px;
                height: 30px;
                line-height: 30px;
                font-size: 14px;
                color: #333;
                border: solid 1px #cccccc;
                text-align: center;
                border-radius: 2px;
                cursor: pointer;
            }
            .modal-container .item .name{
                overflow:hidden;
                text-overflow:ellipsis;
                white-space:nowrap;
                width: 360px;
            }
            .modal-container .item .button a {
                text-decoration: none;
                color: #18aa1f;
            }
            .modal-container .item .ball{
                height: 8px;
                width: 8px;
                border-radius: 50%;
                background-color: #18aa1f;
                margin-right: 3px;
            }
            .modal-container .blank_title {
                color: #999;
                margin-top: 30px;
                font-size: 16px;
                text-align: center;
            }
        </style>
        <script type="text/javascript">
            var pageParam = {
                businessId: "${businessId}"
            }
            function getCookie(name)
            {
                var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
                if(arr=document.cookie.match(reg)) return unescape(arr[2]);
                else return null;
            }
            function showFiles (event) {
                console.log(event)
                $.ajax({
                    url: '/learningCenter/web/getRecordCourseFile',
                    type: 'post',
                    dataType: 'json',
                    timeout: 1000,
                    data: {
                        token: getCookie('SSOTOKEN'),
                        recordId: event,
                        fileType: 5,
                    },
                    success: function (res, status) {
                        var couseListHtml = ''
                        $('#file-list-title').children().remove();
                        if (!res.data || res.data.length === 0) {
                            $('#file-list-title').append('<div class="blank_title">暂无文件下载</div>')
							$('.modal-container').css({height: '300px'})
                            return
                        }
                        for (var i in res.data) {
                            var item = res.data[i]
                            $('#file-list-title').append("<div class='item'><span class='ball'></span><span class='name'>" + item.fileName + "</span><span class='button'>" +
                                    "<a style=\"display:inline-block; width: 100%;\" href='" + item.fileUrl + "'>下载</a></span></div>")
                        }
                    },
                    fail: function (err, status) {
                        console.log(err)
                    }
                })
            }
        </script>
        <script type="text/javascript" src="/resources/compress/lib/raphaeljs/raphael.js"></script>
        <script type="text/javascript" src="/resources/compress/lib/raphaeljs/cycle.js"></script>
        <script type="text/javascript" src="/resources/compress/js/studycenter/recording_list.js"></script>
        </body>

		</html>
</#compress>