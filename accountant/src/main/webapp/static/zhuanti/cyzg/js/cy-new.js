/*Initialize Swiper */
var swiper1 = new Swiper('.cy-slide1 .cy-slide-main  .swiper-container', {
    pagination: '.swiper-pagination',
    paginationClickable: true,
    nextButton: '.swiper-button-next',
    prevButton: '.swiper-button-prev',
    spaceBetween: 30,
    autoplay : 3000
});
var swiper2 = new Swiper('.cy-slide2 .cy-slide-main  .swiper-container', {
    pagination: '.swiper-pagination',
    paginationClickable: true,
    nextButton: '.swiper-button-next',
    prevButton: '.swiper-button-prev',
    spaceBetween: 30,
    autoplay : 3000
});

var swiper3 = new Swiper('.cy-slide3 .cy-slide-main   .swiper-container', {
    pagination: '.swiper-pagination',
    paginationClickable: true,
    nextButton: '.swiper-button-next',
    prevButton: '.swiper-button-prev',
    spaceBetween: 30,
    autoplay : 3000
});
$(function(){
    $(".swiper-slide").hover(function() {
        /*var this_h=$(this).height()
        var b_img_w = $(this).siblings('img').width()
        console.log(this_h,this_w,b_img_h,b_img_w)*/
        var this_w=$(this).find('p').width()
        var b_img_h = $(this).find('p').siblings('img').height()
        $(this).find('p').stop().animate({height: b_img_h,lineHeight:b_img_h + 'px'},'fast',function(){
            $(this).find('b').animate({paddingLeft:this_w/5,fontSize:40+'px'})
        })
    }, function() {
        $(this).find('p').stop().animate({height: 45 + 'px',lineHeight:45+ 'px'},'fast',function(){
            $(this).find('b').animate({paddingLeft:18 + 'px',fontSize:16+'px'})
        })
    });
    $(".cy-new-banner .cy-new-banner-box .cy-banner-btn a").click(
        function(){
            var this_index=$(this).index()
            var this_index_st=$('#cy-main'+($(this).index()+1)).offset().top
            console.log()
            $('body,html').animate({
                scrollTop:this_index_st
                },
                 function() {
                /* stuff to do after animation is complete */
            });
        }
    )     
})