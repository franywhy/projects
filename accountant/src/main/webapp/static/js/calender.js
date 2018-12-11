/*
* 年月日三级联动
* tag: 年月日所在区域的父级标签jquery对象
*/

var calender = window.calender || {};
calender.init = function(tag){
    /*
     * 生成级联菜单
     */
    var $tag = tag || $('body');

    var date = new Date();
    year = date.getFullYear();//获取当前年份
    var i = year - 100;
    var dropList = '';
    for (i; i < year + 1; i++) {
        if (i == year) {
            dropList = dropList + "<option value='" + i + "' selected>" + i + "</option>";
        } else {
            dropList = dropList + "<option value='" + i + "'>" + i + "</option>";
        }
    }
    $tag.find('select[name=year]').html(dropList);//生成年份下拉菜单
    var monthly = '';
    for (month = 1; month < 13; month++) {
        monthly = monthly + "<option value='" + month + "'>" + month + "</option>";
    }
    $tag.find('select[name=month]').html(monthly);//生成月份下拉菜单
    var dayly = '';
    for (day = 1; day <= 31; day++) {
        dayly = dayly + "<option value='" + day + "'>" + day + "</option>";
    }
    $tag.find('select[name=day]').html(dayly);//生成天数下拉菜单
    /*
     * 处理每个月有多少天---联动
     */
    $tag.find('select[name=month]').change(function () {
        var currentDay;
        var Flag = $tag.find('select[name=year]').val();
        var currentMonth = $tag.find('select[name=month]').val();
        switch (currentMonth) {
            case "1":
            case "3":
            case "5":
            case "7":
            case "8":
            case "10":
            case "12": total = 31; break;
            case "4":
            case "6":
            case "9":
            case "11": total = 30; break;
            case "2":
                if ((Flag % 4 == 0 && Flag % 100 != 0) || Flag % 400 == 0) {
                    total = 29;
                } else {
                    total = 28;
                }
            default: break;
        }
        for (day = 1; day <= total; day++) {
            currentDay = currentDay + "<option value='" + day + "'>" + day + "</option>";
        }
        $tag.find('select[name=day]').html(currentDay);//生成日期下拉菜单
    });
    $tag.find('select[name=year]').change(function () {
        $tag.find('select[name=month]').val(1);
        $tag.find('select[name=day]').val(1);
        $tag.find('select[name=month]').change();
    });
    $tag.find('select[name=month]').change(function () {
        $tag.find('select[name=day]').val(1);
    });
};