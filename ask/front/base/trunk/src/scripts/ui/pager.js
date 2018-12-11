/**
*=================================================================
*Name:			JavaScript分页
*RCSfile:		Pager.js
*Revision:		1.0
*Author:		asoiso-Lijian
*Released:		2009-06-24 23:00:15
Copyright:	    Lijian-19890706-20090706
*Description:	JavaScript全能终结版分页
*Contact:		QQ:82364562
*WebSite:		http://www.asoiso.cn=================================================================
pageWrapper
<div id="pageBasicTemplate" style="display:none">
共#{recordCount}记录
页次#{pageIndex}/#{pageCount}
每页#{pageSize}条
每页显示#{pageSizeSelect}条
#{pageFrist}
#{pagePrevious}
#{pageText}
#{pageNext}
#{pageLast}
#{pageSelect}
#{pageInput}
#{pageGo}
</div>
var myPager = new Pager();//初始化分页
myPager.setDataText();//设置分页数据模板
myPager.setDataInfo(1,10,10);//设置分页数据
myPager.setDataInfo({"pageIndex":1,"pageSize":10,"recordCount":10});
myPager.write();//输出分页
*/
function Pager() {
    this.initialize.apply(this, arguments);
};
Pager.prototype =
{
    initialize: function(sName, inputPageMode, outputPageMode, sPageUrl, sHtml) {
        this.sName = sName; //对象名称
        this.inputPageMode = inputPageMode; //分页参数接收模式[1-构造带参数的链接页面地址|Content.html?pageIndex=1&pageSize=10,2-构造带参数的JavaScript伪链接]
        this.outputPageMode = outputPageMode; //跳转方式:[1-刷新页面跳转,2-执行JavaScript伪链接]
        this.pageIndexArg = "pageIndex"; //设置查询字符串的默认pageIndex|可重新设置
        this.pageSizeArg = "pageSize"; //设置查询字符串的默认pageSize
        this.defaultHtml = "#{recordCount}#{pageIndex}#{pageSize}#{pageSizeSelect}#{pageCount}#{pageFrist}#{pagePrevious}#{pageText}#{pageNext}#{pageLast}#{pageSelect}#{pageInput}#{pageGo}";
        this.setUrl(sPageUrl); //设置分页跳转或执行的事件
        this.setHtml(sHtml); //设置分页输出模板
    },
    setDataInfo: function() {
        //设置分页数据
        var iPageIndex, iPageSize, iRecordCount;
        if (arguments.length == 1) {
            //设置当一个参数时候分页数据对象
            iPageIndex = arguments[0].pageIndex || this.pageIndex;
            iPageSize = arguments[0].pageSize || this.pageSize;
            iRecordCount = arguments[0].recordCount || this.recordCount;
        }
        else {
            iPageIndex = arguments[0] || this.pageIndex;
            iPageSize = arguments[1] || this.pageSize;
            iRecordCount = arguments[2] || this.recordCount;
        }
        switch (this.inputPageMode) {
            default:
            case 3:
                this.inputPageMode = 2; //第一次加载从查询参数中取值
            case 1: //[1-构造带参数的链接页面地址|Content.html?pageIndex=1&pageSize=10]
                _iPageIndex = this.getQuery(this.pageIndexArg);
                _iPageSize = this.getQuery(this.pageSizeArg);
                _iPageIndex = (!!_iPageIndex) ? _iPageIndex : iPageIndex;
                _iPageSize = (!!_iPageSize) ? _iPageSize : iPageSize;
                break;
            case 2: //[2-构造带参数的JavaScript伪链接]
                _iPageIndex = iPageIndex;
                _iPageSize = iPageSize;
                break;
            case 4: //支持模式Link:2005042155_1.html,2005042155_2.html...2005042155_10.html
                var _pageStr = this.pageUrl.split("#{pageIndex}");
                var _url = document.location.href;
                var start = _url.indexOf(_pageStr[0]) + _pageStr[0].length;
                var end = _url.indexOf(_pageStr[1]);
                _iPageIndex = _url.substring(start, end);
                _iPageSize = iPageSize;
                break;
        }
        this.recordCount = this.formatNum(iRecordCount, 1, 0, 0, 0); //总记录条数:recordCount>=0;
        this.pageSize = this.formatNum(_iPageSize, 1, 0, 1, 0); //页面大小:pageSize>=0;
        this.getPageCount(); //页码总数
        this.pageIndex = this.formatNum(_iPageIndex, 1, 1, 1, this.pageCount); //当前页码:pageIndex>=1&&pageIndex<=pageCount
    },
    getPageCount: function() {
        //获得页码总数
        this.recordCount = this.formatNum(this.recordCount, 1, 0, 0, 0); //总记录条数:recordCount>=0;
        this.pageSize = this.formatNum(this.pageSize, 1, 0, 1, 0); //页面大小:pageSize>=0;
        var iRC = this.recordCount, iPS = this.pageSize;
        var i = (iRC % iPS == 0) ? (iRC / iPS) : (this.formatNum((iRC / iPS), 1, 0, 0, 0) + 1);
        this.pageCount = i;
        return (i);
    },
    //设置分页显示的Html模板
    setHtml: function(sHtml) {
        this.Html = (!sHtml) ? this.defaultHtml : sHtml;
    },
    setDataText: function(sPageTextMode, sPageText, sPageFrist, sPagePrevious, sPageNext, sPageLast, sPageSelect, sPageGo) {
        //设置分页数据模板
        this.pageTextMode = sPageTextMode; //分页页码算法(1|2|3)
        this.pageText = sPageText; //页码[当前页码|其他页码]
        this.pageFrist = sPageFrist; //[首页激活|首页禁用]
        this.pagePrevious = sPagePrevious; //[上一页激活|上一页禁用]
        this.pageNext = sPageNext; //[下一页激活|下一页禁用]
        this.pageLast = sPageLast; //[尾页激活|尾页禁用]		
        this.pageSelect = sPageSelect; //[页面跳转当前页|页面跳转当其他页]	
        this.pageGo = sPageGo; //页面跳转的按钮文本。
    },
    setUrl: function(sUrl) {
        //设置分页Url的模板
        this.pageUrl = sUrl;
    },
    getUrl: function(iPageIndex) {
        //根据页码和模板生产分页Url
        var sUrl = this.pageUrl;
        sUrl = sUrl.replace(/#{pageIndex}/ig, iPageIndex);
        sUrl = sUrl.replace(/#{pageSize}/ig, this.pageSize);
        return sUrl;
    },
    setActive: function(text, title, className, iPageIndex) {
        //激活
        var s = text ? "<a pageindex=\"" + iPageIndex + "\" href=\"" + this.getUrl(iPageIndex) + "\" rel=\"async\" class=\"" + className + "\" title=\"" + title + "\">" + text + "</a>" : "";
        return s;
    },
    setDisable: function(text, title, className) {
        //禁用
        var s = text ? "<span class=\"" + className + "\" title=\"" + title + "\">" + text + "</span>" : "";
        return s;
    },
    toPage: function(iPageIndex, iPageSize) {
        //分页的跳转
        var strStript = "";
        if (!!iPageSize) {
            this.pageSize = this.formatNum(iPageSize, 1, 0, 1, 0); //页面大小:pageSize>=0;
            this.pageCount = this.getPageCount(); //页码总数
        }
        this.pageIndex = this.formatNum(iPageIndex, 1, 1, 1, this.pageCount); //当前页码:pageIndex>=1&&pageIndex<=pageCount
        var _url = this.getUrl(this.pageIndex);
        switch (this.outputPageMode) {
            default:
            case 1: //[1-跳转链接页面地址|Content.html?pageIndex=1&pageSize=10]
                strStript = "javascript:self.location.href='" + _url + "'";
                break;
            case 2: //[2-执行带参数的JavaScript伪链接]				
                strStript = _url;
                break;
        }
        eval(strStript);
    },
    write: function(id, mode, html) {
        //输出分页
        if (this.getPageCount() <= 0) {
            temp = "";
        }
        else {
            var sN = this.sName;
            var temp = this.Html;
            var pageTextMode = this.pageTextMode;
            var recordCount = this.recordCount;
            var pageIndex = this.pageIndex;
            var prevPage = pageIndex - 1;
            var nextPage = pageIndex + 1;
            var pageSize = this.pageSize;
            var pageCount = this.pageCount;
            var pageGo = this.pageGo;
            var pageSelect = this.pageSelect;
            var pageFrist = this.pageFrist;
            var pagePrevious = this.pagePrevious;
            var pageNext = this.pageNext;
            var pageLast = this.pageLast;
            var pageText = this.pageText;
            if (!!mode) { pageTextMode = mode; }
            if (!!html) { temp = html; }
            var sRecordCount = recordCount; //记录数
            var sPageIndex = pageIndex; //当前页码
            var sPageSize = pageSize; //页面大小
            var sPageCount = pageCount; //页码总数	   
            var sPageFrist = "";
            var sPagePrevious = "";
            var sPageNext = "";
            var sPageLast = "";
            var sPageInput = "";
            var sPageGo = "";
            var sPageSelect = "";
            var sPageSizeSelect = "";
            var sPageText = "";

            var isPageFrist = !!(temp.indexOf("#{pageFrist}") != -1);
            var isPagePrevious = !!(temp.indexOf("#{pagePrevious}") != -1);
            var isPageNext = !!(temp.indexOf("#{pageNext}") != -1);
            var isPageLast = !!(temp.indexOf("#{pageLast}") != -1);
            var isPageInput = !!(temp.indexOf("#{pageInput}") != -1);
            var isPageSizeSelect = !!(temp.indexOf("#{pageSizeSelect}") != -1);
            var isPageSelect = !!(temp.indexOf("#{pageSelect}") != -1);
            var isPageInput = !!(temp.indexOf("#{pageInput}") != -1);
            var isPageText = !!(temp.indexOf("#{pageText}") != -1);

            if (isPageFrist)
                sPageFrist = (pageIndex > 1 && pageIndex <= pageCount) ? this.setActive(pageFrist[0], "", "pageFrist", 1) : this.setDisable(pageFrist[1], "", "dispageFrist");
            if (isPagePrevious)
                sPagePrevious = (pageIndex > 1 && pageIndex <= pageCount) ? this.setActive(pagePrevious[0], "", "pagePrevious", prevPage) : this.setDisable(pagePrevious[1], "", "dispagePrevious");
            if (isPageNext)
                sPageNext = (pageIndex > 0 && pageIndex < pageCount) ? this.setActive(pageNext[0], "", "pageNext", nextPage) : this.setDisable(pageNext[1], "", "dispageNext");
            if (isPageLast)
                sPageLast = (pageIndex > 0 && pageIndex < pageCount) ? this.setActive(pageLast[0], "", "pageLast", pageCount) : this.setDisable(pageLast[1], "", "dispageLast");
            if (isPageInput) {
                sPageInput = "<input class=\"pageInput\" type=\"text\" id=\"" + id + "pageInput\" size=\"6\" maxlength=\"6\" onmouseover=\"this.focus()\" onfocus=\"this.select()\" onkeydown=\"if (event.keyCode==13) " + sN + ".toPage(this.value)\" value=\"" + pageIndex + "\" />";
                sPageGo = "<input class=\"pageGo\" type=\"button\" id=\"" + id + "pageGo\" onClick=\"" + sN + ".toPage(document.getElementById('" + id + "pageInput').value)\" value=\"" + pageGo[0] + "\" />";
            }
            if (isPageSizeSelect) {
                if (pageCount == 0) {
                    sPageSizeSelect = "<select class=\"pageSizeSelect\" id=\"" + id + "pageSizeSelect\" disabled=\"disabled\">";
                    sPageSizeSelect += "<option value=\"" + 0 + "\">0</Option>";
                }
                else {
                    sPageSizeSelect = "<select class=\"pageSizeSelect\" id=\"" + id + "pageSizeSelect\" onChange=\"" + sN + ".toPage(1,this.value)\">";
                    var minPg = 5;
                    if (pageSize <= 5) minPg = 1;
                    for (var i = minPg; i < 10; i++) {
                        if (pageSize == i) {
                            sPageSizeSelect += "<option selected=\"selected\" value=\"" + pageSize + "\">" + pageSize + "</Option>";
                        }
                        else {
                            sPageSizeSelect += "<option value=\"" + i + "\">" + i + "</Option>";
                        }
                    }
                    for (var i = 1; i <= 10; i++) {4
                        var _bps = 10 * (i - 1);
                        var _eps = 10 * (i);
                        if (pageSize == _eps) {
                            sPageSizeSelect += "<option selected=\"selected\" value=\"" + pageSize + "\">" + pageSize + "</Option>";
                        }
                        else if (_bps < pageSize && pageSize < _eps && pageSize > 10) {
                            sPageSizeSelect += "<option selected=\"selected\" value=\"" + pageSize + "\">" + pageSize + "</Option>";
                            sPageSizeSelect += "<option value=\"" + _eps + "\">" + _eps + "</Option>";
                        }
                        else {
                            sPageSizeSelect += "<option value=\"" + _eps + "\">" + _eps + "</Option>";
                        }
                    }
                }
                sPageSizeSelect += "</select>";
            }
            if (isPageSelect) {
                if (pageCount == 0) {
                    sPageSelect = "<select class=\"pageSelect\" id=\"" + id + "pageSelect\" disabled=\"disabled\">";
                    sPageSelect += "<option value=\"" + 0 + "\" disabled=\"disabled\">0</Option>";
                }
                else {
                    sPageSelect = "<select class=\"pageSelect\" id=\"" + id + "pageSelect\" onChange=\"" + sN + ".toPage(this.value)\">";
                    for (var i = 1; i <= pageCount; i++) {
                        if (i != pageIndex) {
                            var t = pageSelect[1];
                            t = t.replace(/#{pageIndex}/ig, i);
                            t = t.replace(/#{pageCount}/ig, sPageCount);
                            sPageSelect += "<option value=\"" + i + "\">" + t + "</Option>";
                        }
                        else {
                            var t = pageSelect[0];
                            t = t.replace(/#{pageIndex}/ig, i);
                            t = t.replace(/#{pageCount}/ig, sPageCount);
                            sPageSelect += "<option selected=\"selected\" value=\"" + i + "\">" + t + "</Option>";
                        }
                    }
                }
                sPageSelect += "<option  value=\"" + 5000 + "\">5000</Option>";
                sPageSelect += "</select>";
            }
            if (isPageText) {
                switch (pageTextMode) {
                    //十页几种分页效果一样                                        
                    default:
                    case 1: ////0-100页输出页码模式
                        var bPage = pageIndex - 2, ePage = pageIndex + 2;
                        if (bPage < 1) {
                            bPage = 1;
                        }
                        if (pageCount < ePage) {
                            ePage = pageCount;
                        }
                        if (pageIndex > 1 && bPage > 1) {
                            var _t = pageText[1].replace(/#{pageIndex}/ig, 1);
                            sPageText += this.setActive(_t, "" + 1, "pageNum", 1);
                        }
                        if (pageIndex > 2 && bPage > 2) {
                            var _t = pageText[1].replace(/#{pageIndex}/ig, 2);
                            sPageText += this.setActive(_t, "" + 2, "pageNum", 2);
                        }
                        if (pageIndex > 5) sPageText += '<span class="pageDot">...</span>';
                        for (var i = bPage; i <= ePage; i++) {
                            if (i == pageIndex) {
                                var _t = pageText[0].replace(/#{pageIndex}/ig, i);
                                sPageText += this.setDisable(_t, "" + i, "pageIndex");
                            } else {
                                var _t = pageText[1].replace(/#{pageIndex}/ig, i);
                                sPageText += this.setActive(_t, "" + i, "pageNum", i);
                            }
                        }
                        if (pageIndex + 4 < pageCount) sPageText += '<span class="pageDot">...</span>';
                        if (pageIndex < (pageCount - 1) && ePage < (pageCount - 1)) {
                            var _t = pageText[1].replace(/#{pageIndex}/ig, pageCount - 1);
                            sPageText += this.setActive(_t, "" + (pageCount - 1), "pageNum", pageCount - 1);
                        }
                        if (pageIndex < pageCount && ePage < pageCount) {
                            var _t = pageText[1].replace(/#{pageIndex}/ig, pageCount);
                            sPageText += this.setActive(_t, "" + pageCount, "pageNum", pageCount);
                        }
                        break;
                    case 2: //50-10000页输出页码模式
                        var bPage = 0;
                        if (pageIndex % 10 == 0) {
                            bPage = pageIndex - 9;
                        } else {
                            bPage = pageIndex - pageIndex % 10 + 1;
                        }
                        var ePage = bPage + 10;
                        if (pageIndex > 1 && bPage > 1) {
                            var _t = pageText[1].replace(/#{pageIndex}/ig, 1);
                            sPageText += this.setActive(_t, "" + 1, "pageNum", 1);
                        }
                        if (bPage > 10) {
                            sPageText += this.setActive("...", "", "pageDot", (bPage - 1));
                        }
                        for (var i = bPage; i < ePage; i++) {
                            if (i < 0 || i > pageCount) break;
                            if (i == pageIndex) {
                                var _t = pageText[0].replace(/#{pageIndex}/ig, i);
                                sPageText += this.setDisable(_t, "" + i, "pageIndex");
                            } else {
                                var _t = pageText[1].replace(/#{pageIndex}/ig, i);
                                sPageText += this.setActive(_t, "" + i, "pageNum", i);
                            }
                        }
                        if (pageCount >= bPage + 10) {
                            sPageText += this.setActive("...", "", "pageDot", (bPage + 10));
                        }
                        if (pageIndex < pageCount && ePage < pageCount) {
                            var _t = pageText[1].replace(/#{pageIndex}/ig, pageCount);
                            sPageText += this.setActive(_t, "" + pageCount, "pageNum", pageCount);
                        }
                        break;
                    case 3: //特殊:迭代输出页码模式
                        for (var i = 1; i <= pageCount; i++) {
                            if (i == pageIndex) {
                                var _t = pageText[0].replace(/#{pageIndex}/ig, i);
                                sPageText += this.setDisable(_t, "" + i, "pageIndex");
                            } else {
                                var _t = pageText[1].replace(/#{pageIndex}/ig, i);
                                sPageText += this.setActive(_t, "" + i, "pageNum", i);
                            }
                        }
                        break;

                }
            }
            temp = temp.replace(/#{recordCount}/ig, sRecordCount);
            temp = temp.replace(/#{pageIndex}/ig, sPageIndex);
            temp = temp.replace(/#{pageSize}/ig, sPageSize);
            temp = temp.replace(/#{pageCount}/ig, sPageCount);
            temp = temp.replace(/#{pageFrist}/ig, sPageFrist);
            temp = temp.replace(/#{pagePrevious}/ig, sPagePrevious);
            temp = temp.replace(/#{pageText}/ig, sPageText);
            temp = temp.replace(/#{pageNext}/ig, sPageNext);
            temp = temp.replace(/#{pageLast}/ig, sPageLast);
            temp = temp.replace(/#{pageInput}/ig, sPageInput);
            temp = temp.replace(/#{pageGo}/ig, sPageGo);
            temp = temp.replace(/#{pageSelect}/ig, sPageSelect);
            temp = temp.replace(/#{pageSizeSelect}/ig, sPageSizeSelect);
        }
        if (!id) {
            document.getElementById(this.sName).innerHTML = temp;
        } else {
            if (pageCount > 1) {
                $(id).html(temp);
                $(id).show();
            }
            else {
                $(id).html("");
                $(id).hide();
            }
        }
        return temp;
    },
    //TODO 心情好的时候对，心情不好的时候不对~
    getQuery: function(key, url) {
        url = url || window.location.href;
        var rts = [], rt;
        var queryReg = new RegExp('(^|\\?|&)' + key + '=([^&]*)(?=&|#|$)', 'g');
        while ((rt = queryReg.exec(url)) != null) {
            rts.push(decodeURIComponent(rt[2]));
        }
        if (rts.length == 0) return null;
        if (rts.length == 1) return rts[0];
        return rts;
    },
    formatNum: function(sNum, bMin, bMax, iMinNum, iMaxNum) {
        var i, iN, sN = "" + sNum, iMin = iMinNum, iMax = iMaxNum;
        if (sN.length > 0) {
            iN = parseInt(sN, 10);
            i = (isNaN(iN)) ? iMin : iN;
            i = (i < iMin && bMin == 1) ? iMin : i;
            i = (i > iMax && bMax == 1) ? iMax : i;
        }
        else {
            i = iMin;
        }
        return (i);
    }
};
//Ajax列表通用分页对象
//var CommonPager = new Web.Pager("CommonPager", 3, 2, '', "<span class='pageSizeSelectW'>每页显示#{pageSizeSelect}条</span>#{pageFrist}#{pagePrevious} #{pageText}#{pageNext}#{pageLast}<span class='pageinputgo'>#{pageInput}/#{pageCount}&nbsp;#{pageGo}</span>"); CommonPager.setDataText(2, ["#{pageIndex}", "#{pageIndex}"], ["", ""], ["<img src='/images/lts/prevPage.gif'>", ""], ["<img src='/images/lts/nextpager.gif'>", ""], ["", ""], ["Pages:#{pageIndex}/#{pageCount}", "Pages:#{pageIndex}/#{pageCount}"], ["Go"]);
//CommonPager.setDataInfo(pageData);
//CommonPager.write("Pager");
