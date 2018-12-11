$(function(){
	mouseSelectsf();
});
function mouseSelectsf(){
	var btn = $(".select-sf");
	var pop = $(".shengfen-pop");
	btn.hover(function(){
		pop.fadeIn(200);
	},function(){
		pop.fadeOut(200);
	});
}
