/*
* Izb.ui
*/
$.module("Izb.ui",function(){
	var _tmplFn = {},
		$main,	$aside,	$appHeader, $appContent;
	
	uploadCallback  = function(result) {
		if(result.code==1) {				
			var inputName = $("#uploadForm [name=picInput]").val();
			$("form input[name='" + inputName + "']").val(result.data.pic_url);
            //课件管理
            if(result.data.type != undefined && result.data.type=="courseware"){
                $("form input[name='file_size']").val(result.data.file_size);
                $("form input[name='file_sizeMB']").val((result.data.file_size / (1024 * 1024)).toFixed(2));
                $("form input[name='ext']").val(result.data.ext);
                //文件名
                result.data.file_name = result.data.file_name.substring(0 , result.data.file_name.lastIndexOf("."));
                $("form[name='inputForm'] input[name='courseware_name']").val(result.data.file_name);
            }
			art.dialog.list['upload'].close();				
		} else {
			document.getElementById('tmp_iframe').contentWindow.document.body.innerHTML = result.msg;
		}
        //关闭遮盖层
        Izb.ui.hideLoad();
	};
	
	return {
		init:function(){
			art.dialog.defaults['title'] = Izb.config.name;
			art.dialog.defaults['okValue'] = '确定';
			art.dialog.defaults['cancelValue'] = '取消';
			art.dialog.defaults['path'] = 'http://ttasset.app@appid@.twsapp.com/base/scripts/ui/artDialog/';
			$main = $("#main");
			$aside = $("#aside");
			$appHeader = $("#appHeader");
			$appContent = $("#appContent");
		},		
		closeTips : function () {
			$('#tips').html('');
		},
		closeDialog : function(dialogId){
			art.dialog.list[dialogId] && art.dialog.list[dialogId].close();
		},
        upload: function (picInput, type , url) {
            var type = type || 'tmp';
            var html = '<form id="uploadForm" enctype="multipart/form-data" name="tmp_upload" method="post" action="' + Izb.core.getURI(url ? url : "/upload.json", "data") + '" target="tmp_iframe">\
							<input type="hidden" value="uploadCallback" name="icallback"/>\
							<input type="hidden" value="' + type + '" name="type"/>\
							<input type="hidden" value="' + picInput + '" name="picInput"/>\
							<input name="file" type="file" />\
						</form>\
						<iframe name="tmp_iframe" id="tmp_iframe" style="display:none"></iframe>';
            this.dialog({
                lock : true,
                title : '文件上传',
                id : 'upload',
                content : html,
                ok : function () {
                    if($("input[name='file']").val() == ''){
                        alert("请选择文件!");
                        return false;
                    }
                    //打开这开层
                    Izb.ui.showLoad();
                    document.forms['tmp_upload'].submit();
                    return false;
                }
            });
        },
		uploadCallback : uploadCallback,
		bindUserNameText:function($txt,$tip){
			$txt = $($txt);
			$tip = $($tip);
			$txt.blur(function(){
				if(!this.value){
					$tip.html("*");	
				}
			});
			$txt.keyup(function(){
				if(this.value){
					var url = Izb.core.resolveAction({
						path: "/public/id_name/" + this.value,
						domain: Izb.config.domain.api
					});
					Izb.common.getResult({
						url:url,
						success:function(result){
							if(result.data && result.data.nick_name){
								$txt.attr("data-success",true);
								$tip.html("("+result.data.nick_name+")");
							}else{
								$txt.attr("data-success",false);
								$tip.html("请输入正确的用户Id");
							}
						}
					});
				}else{
					$tip.html("*");	
				}
			});
		},
		dialog : function (config) {
			return art.dialog(config);
		},
		/*
		* icon:error|face-sad|face-smile|question|succeed|warning
		*/
		alert:function(msg,title,icon){
			this.dialog({
				title : title || "",
				lock : true,
				drag : true,
				icon : icon,
				content : msg.toString(),
				ok : true
			});
		},
		confirm:function(msg, fn, title){
			this.dialog({
				title : title || '系统提示',
				id: 'confirmDialog',
				lock: true,
				content : msg.toString(),
				ok : fn,
				cancel : true
			});
		},
		confirm1:function(msg, fn, title){
			this.dialog({
				title : title || '系统提示',
				id: 'confirmDialog',
				lock: true,
				content : msg.toString(),
				ok : fn
			});
		},
		prompt:function(title,defaultVal,fn){
			this.dialog({
				title : title,
				lock : true,
				content : '<input id="aui_Prompt" class="aui_Prompt" name="artPrompt" type="text" value="" />',
				ok : function(){
					var val = this.DOM.main.find("input[name=artPrompt]").val();
					if($.isFunction(fn)){
						fn(val);
					}
				},
				cancel : true
			});			
			$("#aui_Prompt").val(defaultVal);
		},
		tips:function(msg, icon, time){
			this.dialog({
				title : "",
				icon : icon,
				lock : false,
				fixed : true,
				content : msg.toString(),
				time:time || 3
			});
		},
		//输出main
		render:function(headerTpl,contentTpl,result){
			Izb.core.out(result);
			this.renderHeader(headerTpl,result);
			this.renderContent(contentTpl,result);
		},
		//输出头
		renderHeader : function(tpl,result){
			if(tpl){
				$appHeader.html(this.template(tpl,result));			
			} else {
				$appHeader.empty();
			}
		},
		/*
		* 输出内容
		* tpl
		* result
		* pageStyle 
		*/
		renderContent : function(tpl,result,pageStyle){
			Izb.core.out(result);
			if(tpl){
				if(typeof(result) == "undefined"){
					$appContent.html(tpl);
				} else{
					$appContent.html(this.template(tpl,result));					
					pageStyle = pageStyle || 1;
					if(result.params && result.params.page){
						var sPageUrl = '';
						if(pageStyle==1){
							sPageUrl = 'javascript:Izb.main.gotoList(#{pageIndex},#{pageSize})';
						}else{
							sPageUrl = 'javascript:$.history.setAction(\'page\',#{pageIndex},#{pageSize});';
						}
						
						if(result.count==0){
							$appContent.append('<div class="listEmpty">暂无记录！</div>');	
						}
						Izb.ui.renderPager({context:"#appContent .appPager",pageIndex:result.params.page,pageSize:result.params.size,recordCount:result.count,sPageUrl:sPageUrl});
					}
				}
			} else {
				$appContent.empty();
			}
		},
		//动态模板替换
		template:function(tpl,result){
			if(!tpl){
				return "";
			}
			
			if(tpl.indexOf("tpl-")!=0 || tpl.length>30){
				//直接编译
				return $.template(tpl,result);
			}
			
			var fn = _tmplFn[tpl];
			if(!$.isFunction(fn)){				
				var element = document.getElementById(tpl);
				if(element){
					fn = $.template(tpl);
				} else {					
					var url = tpl.replace(/-/g,"/")+".html";
					$.ajax({
						url:url,
						cache:false,
						async:false,
						dataType:"html",
						success:function(result){
							fn = $.template(result);
						}
					});	
				}
			}
			_tmplFn[tpl] = fn;
			return fn(result);
		},
		// 添加模块Izb.addHtml(html, '添加新版本', function)
		showDialog : function (html, title, callback) {	
			this.dialog({
				lock : true,
				id : 'inputDialog',
				title : title || XY.name,
				content : html,
				ok : function () {
					if(callback) {
						callback();
					}
					return false;
				},
				cancel : true
			});
		},
		// 添加模块Izb.addHtml(tplId, '添加新版本', result, function)
		showDialogByTpl : function (tplId, title, result, callback) {

            return this.dialog({
                lock : true,
                id: tplId || 'inputDialog',
                title : title || Izb.config.name,
                content : this.template(tplId, result || {}),
                ok : function () {
                    if($.isFunction(callback)) {
                        return	callback();
                    }
                    return false;
                },
                cancel : true
            });
        },
		/**
		* Ajax分页
		* Izb.ui.renderPager({context:"#appFooter",pageIndex:10,pageSize:10,recordCount:1000,sPageUrl:"javascript:setMyPager(#{pageIndex},#{pageSize})"});
		* eg. 
		* RS.UI.pager({});
		* @param <Array> list
		*/
		renderPager:function(o){			
			//var AjaxPager = new Pager("AjaxPager", 2, 1, o.sPageUrl, "#{pageFrist}#{pagePrevious} #{pageText}#{pageNext}#{pageLast}");
			
			
			AjaxPager = new Pager("AjaxPager",2,1,o.sPageUrl,"共#{recordCount}记录 每页#{pageSizeSelect}条 #{pageFrist} #{pagePrevious} #{pageText} #{pageNext} #{pageLast} #{pageSelect} #{pageInput} #{pageGo}");
			
			AjaxPager.setDataText(1, ["#{pageIndex}", "#{pageIndex}"], ["", ""], ["<<上一页", ""], ["下一页>>", ""], ["", ""], ["Pages:#{pageIndex}/#{pageCount}", "Pages:#{pageIndex}/#{pageCount}"], ["Go"]);

			AjaxPager.setDataInfo(o.pageIndex, o.pageSize, o.recordCount);
			AjaxPager.write(o.context);
		},
		//导出数据
		exportData:function(){			
			var iWidth = 800,
				iHeight = 500,
				iTop=(window.screen.height-iHeight)/2,
				iLeft=(window.screen.width-iWidth)/2;
			
			var csvWindow = window.open("","Detail","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
			
			var	$listTable = $('#appContent .listTable'),
				strContent = '';
			
			var $ths = $('thead th:not(.action)',$listTable),len = $ths.length;
			$ths.each(function(index, th) {
				strContent += $(th).text();			
				if(index<len-1){
					strContent += ',';
				}
			});
			
			$('tbody tr',$listTable).each(function(i, tr) {
				strContent += '\n';
				var $tds = $('td:not(.action)',$(this)),
					len = $tds.length;
				$tds.each(function(j, td) {
					strContent += $(td).text();
					if(j<len-1){
						strContent += ',';
					}
				});
			});
			
			//写成一行 
			csvWindow.document.write('<p style="text-align:center; font-size:12px; color:red;">新建1个文本文档(.txt)，把下面内容拷贝到文本文件中，再把文件的.txt改为.csv</p><textarea style="width:100%;height:100%;">'+strContent+'</textarea>');
			csvWindow.document.close();
		},
		getFormData:function(formName){
			var that = this;
			var $form = $("form[name="+formName+"]");
			var data = $.serializeForm($form);
			$('textarea.J_editor',$form).each(function(index, element) {
                var $this = $(this);
				data[$this.attr('name')] = that.getEditorVal($this.attr('name'));
            });
			return data;
		},
		setFormData:function(formName,data){
			$.setFormData($("form[name="+formName+"]"),data);
		},
		getEditorVal:function(name){
			if(document.getElementById('editor')){
				return document.getElementById('editor').contentWindow.editor.html();	
			} else {
				return $('textarea[name='+name+']').val();
			}
		},
        getToday:function(){
            var myDate = new Date();
            var myMonth = (myDate.getMonth()+1);
            var myday = myDate.getDate();
            myMonth = myMonth>9 ? myMonth:'0'+myMonth;
            myday = myday>9 ? myday:'0'+myday;
            return myDate.getFullYear()+'-'+myMonth+'-'+myday+' 00:00:00';
        },
        getTodayEnd:function(){
            var myDate = new Date();
            var myMonth = (myDate.getMonth()+1);
            var myday = myDate.getDate();
            myMonth = myMonth>9 ? myMonth:'0'+myMonth;
            myday = myday>9 ? myday:'0'+myday;
            return myDate.getFullYear()+'-'+myMonth+'-'+myday+' 23:59:59';
        },
		getTodayDate: function () {
			var myDate = new Date();
			var myMonth = (myDate.getMonth() + 1);
			var myday = myDate.getDate();
			myMonth = myMonth > 9 ? myMonth : '0' + myMonth;
			myday = myday > 9 ? myday : '0' + myday;
			return myDate.getFullYear() + '-' + myMonth + '-' + myday ;
		},
		getYesterday:function(){
			var myDate = new Date();			
			var myMonth = (myDate.getMonth()+1);
			var myday = myDate.getDate()-1;
			myMonth = myMonth>9 ? myMonth:'0'+myMonth;
			myday = myday>9 ? myday:'0'+myday;
			return myDate.getFullYear()+'-'+myMonth+'-'+myday+' 00:00:00';
		},
		getYesterdayEnd:function(){
			var myDate = new Date();
			var myMonth = (myDate.getMonth()+1);
			var myday = myDate.getDate()-1;
			myMonth = myMonth>9 ? myMonth:'0'+myMonth;
			myday = myday>9 ? myday:'0'+myday;
			return myDate.getFullYear()+'-'+myMonth+'-'+myday+' 23:59:59';
		},
		getFirstDay:function(){
			var myDate = new Date();
			var myMonth = (myDate.getMonth()+1);
			myMonth = myMonth>9 ? myMonth:'0'+myMonth;
			return myDate.getFullYear()+'-'+myMonth+'-01 00:00:00';
		},
		getLastDay:function(isEnd)
		{     
			 var date = new Date();
			 var month = date.getMonth()+1;
			 
			 var new_year = date.getFullYear();//取当前的年份        
			 var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）        
			 if(month>12)            //如果当前大于12月，则年份转到下一年        
			 {        
				  new_month -=12;        //月份减        
				  new_year++;            //年份增        
			 }        
			 var new_date = new Date(new_year,new_month,1);                //取当年当月中的第一天        
			 var timex = isEnd ? 1000 : (1000*60*60*24);
			 return (new Date(new_date.getTime()-timex)).toLocaleString();//获取当月最后一天日期    
		},
		formatTime: function (timestamp) {
			var time = new Date(parseInt(timestamp, 10));
			var result = {
				year: time.getFullYear(),
				month: time.getMonth() + 1,
				date: time.getDate(),
				hour: time.getHours(),
				min: time.getMinutes(),
				sec: time.getSeconds(),
				msec: time.getMilliseconds()
			};

			for (var key in result) {
				var value = result[key];
				result[key] = value < 10 ? '0' + value : value;
			}

			return result.year + '-' + result.month + '-' + result.date + ' ' + result.hour + ':' + result.min + ':' + result.sec
		},
		//x：数值，d：小数位(默认:2)
		toDecimal: function (x,d) {
			var f = parseFloat(x),
				d=d||2;
			if (isNaN(f)) {
				return false;
			}
			var f = Math.round(x * 100) / 100;
			var s = f.toString();
			var rs = s.indexOf('.');
			if (rs < 0) {
				rs = s.length;
				s += '.';
			}
			while (s.length <= rs +d ) {
				s += '0';
			}
			return s;
		}
        ,showLoad:function(){
            document.getElementById('choose_accept').style.display="block"; //打开登陆页面；
            document.getElementById('coverid').style.display="block";   //打开遮罩层；
        }
        ,hideLoad:function(){
            document.getElementById('choose_accept').style.display="none";  //关闭登陆页面；
            document.getElementById('coverid').style.display="none";    //关闭遮罩层；
        },
        initSelectData:function(data){
            var province_schooldata =data;
            var allobj = {
                code:"all",
                name:"全国",
                schoollist:[{
                    code:"all",
                    name:"全部校区"
                }]
            }

            province_schooldata.push(allobj);
            for(var i=0;i<province_schooldata.length;i++)
            {
                var item = province_schooldata[i];
                $("#province_select").append("<option value="+item.code+">"+item.name+"<option>");
            }
            var datalist=province_schooldata[0].schoollist;
            for(var i=0;i<datalist.length;i++)
            {
                var item = datalist[i];
                $("#school_code").prepend("<option value="+item.code+">"+item.name+"<option>");
            }
            $("#province_select").find("option").each(function(i){
                if($(this).html() == "")
                {
                    $(this).remove();
                }
            });
            $("#school_code").find("option").each(function(i){
                if($(this).html() == "")
                {
                    $(this).remove();
                }
            });


            $("#province_select").change(function(){
                // 先清空第二个
                var nc_id = $("#province_select").val();
                $("#school_code").empty();
                var datalist =[];
                for(var i=0;i<province_schooldata.length;i++)
                {
                    var item = province_schooldata[i];
                    if(nc_id==item.code)
                        datalist = item.schoollist;
                }
                for(var i=0;i<datalist.length;i++)
                {
                    var item = datalist[i];
                    $("#school_code").prepend("<option value="+item.code+">"+item.name+"<option>");
                }
                $("#school_code").find("option").each(function(i){
                    if($(this).html() == "")
                    {
                        $(this).remove();
                    }
                });
            });
        },
        initSelectbyValue:function(province_schooldata,schoolcode){
            var provincecode = schoolcode.substring(0,5);
            var datalist = [];
            for(var i=0;i<province_schooldata.length;i++)
            {
                var item = province_schooldata[i];
                if(provincecode==item.code) {
                    datalist = item.schoollist;
                    break;
                }
            }

            $("#school_code").empty();
            for(var i=0;i<datalist.length;i++)
            {
                var item = datalist[i];
                $("#school_code").prepend("<option value="+item.code+">"+item.name+"<option>");
            }
            $("#school_code").find("option").each(function(i){
                if($(this).html() == "")
                {
                    $(this).remove();
                }
            });
            $("#province_select").val(provincecode);
            $("#school_code").val(schoolcode);

        },
        initschoolselect:function(){
            Izb.common.getResult({
                action: "/schoolpic/getprovince",
                success: function (result) {
                    if(result != null) {

                        Izb.ui.initSelectData(result.data);
                    }
                },
                error: function (xhr, status, result) {
                }
            });
        },
        initschoolselect_edit:function(school_code){
            Izb.common.getResult({
                action: "/schoolpic/getprovince",
                success: function (result) {
                    if(result != null) {
                        Izb.ui.initSelectData(result.data);
                        Izb.ui.initSelectbyValue(result.data,school_code);
                    }
                },
                error: function (xhr, status, result) {
                }
            });
        }

	};
	
});





