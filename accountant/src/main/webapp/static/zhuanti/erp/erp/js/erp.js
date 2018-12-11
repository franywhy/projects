function hover(obj) {
    $(obj).hover(function () {
        $(this).addClass("act1").siblings().removeClass("act1");
    },function(){
        $(this).siblings().removeClass("act1")
    });
};
