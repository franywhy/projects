$.fn.placeholder = function(text) {
	if($.browser.webkit || ($.browser.msie && $.browser.version>9)){
		return;	
	}
	if (this.length == 0) {
		return;
	}
	if (!!text) {
		$(this).attr("placeholder", text);
	}
	if ($(this).val().length == 0 || $(this).val() == $(this).attr("placeholder")) {
		$(this).val($(this).attr("placeholder"));
		$(this).addClass("placeholder");
	}
	else {
		$(this).removeClass("placeholder");
	}
	$(this).focus(function() {
		if ($(this).val() == $(this).attr("placeholder")) {
			$(this).val("");
			$(this).removeClass("placeholder");
		};
	});

	$(this).blur(function() {
		if ($(this).val() == "") {
			$(this).addClass("placeholder");
			$(this).val($(this).attr("placeholder"));
		};
	});
}