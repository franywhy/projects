$(function () {
    // 选项卡
    tab();
    function tab() {
        var tab = $('.tab');
        var nav = tab.find('.tab-nav');
        var boxDot = tab.find('.tab-box dt');
        nav.on('mouseenter', 'dt', function () {
            var i = $(this).index();
            $(this).addClass('act').siblings('dt').removeClass('act');
            boxDot.eq(i).stop().fadeIn(0).siblings().stop().fadeOut(0);
        });
    }
    // 商机 上传商机标题
    window.util.onroll('辉煌计划');
});