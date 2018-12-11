		$(document).ready(function(){

	$("dd").hide();
			$("dt").on("click",function(){$("dd").hide();
				$(this).next().toggle("slow");
			})
		});
		