
$(function () {
    $('.sideBar li').last().hide();
    var $sideBar = $('.sideBar');
    var positionTop = ($(window).height() - $sideBar.innerHeight()) / 1;
    var positionLeft = ($(window).width() - $sideBar.innerWidth());
    $sideBar.css('top',110);
    $sideBar.css('left', positionLeft);
    
    $(window).scroll(function () {

        if ($(window).scrollTop() > 60) {

            $('.sideBar li').last().show();
        }

        if ($(window).scrollTop() <= 60) {

            $('.sideBar li').last().hide();
        }
        positionTop = ($(window).height() - $sideBar.innerHeight()) / 1 + $(window).scrollTop();
        positionLeft = ($(window).width() - $sideBar.innerWidth()) + $(window).scrollLeft();


        $sideBar.css('top', positionTop);
        $sideBar.css('left', positionLeft);
    });


});


/*--------------------返值---------*/
function pupopen(){
			document.getElementById("bg").style.display="block";	
			document.getElementById("value").style.display="block" ;
	}

function pupclose(){
document.getElementById("bg").style.display="none";
			document.getElementById("value").style.display="none" ;
}



/*----------------------------------我的账户切换------------------------------------*/
$(function(){
	var $tabTitleLi = $('.faq_top ul li');
	var $switch = $('.switch .boxesing');

	$tabTitleLi.click(function(){
		var _index = $(this).index(); 
		$(this).addClass('cur').siblings('li').removeClass('cur');
		$switch.hide().eq(_index).show();
	});
});

$(function () {
    $("#liWX").hover(
        function () { $("#imgWX").show(); },

        function () { $("#imgWX").hide(); }
    );
});