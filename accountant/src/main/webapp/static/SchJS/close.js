$(function(){
    var _hmt = _hmt || [];
    (function () {
        var hm = document.createElement("script");
        hm.src = "//hm.baidu.com/hm.js?aab2edb886e201b081b8399ace5b3cbf";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);

        setTimeout(function(){
			var offBtn = "<div class='offBtn' title='收起'><</div>";
	        var s = $(".qiao-icon-wrap").append(offBtn);
	        $(".qiao-icon-head").css({width:160,overflow:'hidden'})
	        $(".offBtn").css({
				position:'absolute',
				width:'15px',
				height:'25px',
				lineHeight:'26px',
				border:'1px solid #cccccc',
				fontFamily:'宋体',
				backgroundColor:'#ffffff',
				color:'#666666',
				fontWeight:'700',
				fontSize:'12px',
				textAlign:'center',
				cursor:'default',
				right:'-20px',
				marginTop:'-70px'
	        });
	        var p=1;
	        $(".offBtn").click(function(){
	        	if($(".qiao-icon-wrap").css('left')=='0px'){
	        		$(".qiao-icon-wrap").animate({left:-160});
	        		$(this).text('>');
	        		$(this).attr('title','展开');
	        	}
	        	if($(".qiao-icon-wrap").css('left')=='-160px'){
	        		$(".qiao-icon-wrap").animate({left:0});
	        		$(this).text('<');
	        		$(this).attr('title','收起');
	        	}
	        })
        },3000);
    })();
  })