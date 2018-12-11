/*
* 构造分页html
* 显示示例（前5后4）：首页 < 5 6 7 8 9 [10] 11 12 13 14 > 尾页
* hrefFormat: href的格式，如：/Web/Course/List?pn={0}
*/
function getPagerHtml2(pn, ps, count, hrefFormat) {

    function hrefReplace(href, pn) {
        return href.replace('{0}', pn);
    };

    var sb = '';
    var pages = parseInt(count / ps + (count % ps == 0 ? 0 : 1)); //页数
    
    //页数大于2才显示分页
    if (pages == 0 || pages == 1) {
        return '';
    }
    else if (pages <= 10 || pn <= 5) {
        var _pages = pages > 10 ? 10 : pages; //要显示的页数
        for (var i = 0; i < _pages; i++) {
            if (pn == (i + 1)) {
                sb += "<em class='act'><a style='color:inherit;' href=\"" + hrefReplace(hrefFormat, (i + 1)) + "\">" + (i + 1) + "</a></em>";
            }
            else {
                sb += "<em><a style='color:inherit;' href=\"" + hrefReplace(hrefFormat, (i + 1)) + "\">" + (i + 1) + "</a></em>";
            }
        }
    }
    else {
        var right = pages - pn > 4 ? 4 : pages - pn; //当前页距离最后一页之间相差多少页（当前页的右侧计数）
        var left = 10 - right - 1; //当前页的左侧计数
        var idx = pn - left; //左侧第一个数（开始索引）
        for (var i = idx; i < 10 + idx; i++) {
            if (pn == i) {
                sb += "<em class='act'><a style='color:inherit;' href=\"" + hrefReplace(hrefFormat, i) + "\">" + i + "</a></em>";
            }
            else {
                sb += "<em><a style='color:inherit;' href=\"" + hrefReplace(hrefFormat, i) + "\">" + i + "</a></em>";
            }
        }
    }

    var prevPn = pn > 1 ? pn - 1 : pn; //上一页
    var nextPn = pn < pages ? pn + 1 : pages; //下一页
    var leftHtml = "<span><a href=\"" + hrefReplace(hrefFormat, 1) + "\" style='color:inherit;'>首页</a></span><i><a href=\"" + hrefReplace(hrefFormat, prevPn) + "\" style='color:inherit;'><</a></i>";
    var rightHtml = "<i><a href=\"" + hrefReplace(hrefFormat, nextPn) + "\" style='color:inherit;'>></a></i><span><a href=\"" + hrefReplace(hrefFormat, pages) + "\" style='color:inherit;'>尾页</a></span>";

    return '<div class="main-page-footer">' + leftHtml + sb + rightHtml + '</div>';
};