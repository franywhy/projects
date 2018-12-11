(function($) {
	$.fn.svgCircle = function(i) {
		i = $.extend({
			parent: null,
			w: 64,
			R: 30,
			sW: 4,
			color: ["#000", "#000"],
			perent: [64, 64],
			speed: 0,
			delay: 1000
		}, i);
		return this.each(function() {
			var e = i.parent;
			if (!e) return false;
			var w = i.w;
			var r = Raphael(e, w, w),
				R = i.R,
				init = true,
				param = {
					stroke: "#0088cc"
				},
				hash = document.location.hash,
				marksAttr = {
					fill: hash || "#444",
					stroke: "none"
				};
			r.customAttributes.arc = function(b, c, R) {
				var d = 360 / c * b,
					a = (90 - d) * Math.PI / 180,
					x = w / 2 + R * Math.cos(a),
					y = w / 2 - R * Math.sin(a),
					color = i.color,
					path;
				if (c == b) {
					path = [
						["M", w / 2, w / 2 - R],
						["A", R, R, 0, 1, 1, w / 2 - 0.01, w / 2 - R]
					]
				} else {
					path = [
						["M", w / 2, w / 2 - R],
						["A", R, R, 0, +(d > 180), 1, x, y]
					]
				}
				return {
					path: path
				}
			};
			var f = r.path().attr({
				stroke: "#e5e5e5",
				"stroke-width": i.sW
			}).attr({
				arc: [110, 110, R]
			});
			var g = r.path().attr({
				stroke: "#ff0000",
				"stroke-width": i.sW
			}).attr(param).attr({
				arc: [0.01, i.speed, R]
			});
			var h;
			if (i.perent[1] > 0) {
				setTimeout(function() {
					g.animate({
						stroke: i.color[1],
						arc: [i.perent[1], 100, R]
					}, 900, ">")
				}, i.delay)
			} else {
				g.hide()
			}
		})
	}
})(jQuery);

!function(){
	function processingbar(){
		var self = this;
			self.color = "#18aa1f";
		
		this.init = function(color){
			self.color = color;
			this.eprogressingbar(self.color);
			this.dprogressingbar();
			this.bindEvent();
			return self;
		}
		this.bindEvent = function(){
			$(window).scroll(function() {
				self.eprogressingbar(self.color);
				self.dprogressingbar();
			});
		}
		this.eprogressingbar = function(){
			var c = $('.processingbar');
			var b = {
				top: $(window).scrollTop(),
				bottom: $(window).scrollTop() + $(window).height()
			};
			c.each(function() {
				if (b.top <= $(this).offset().top && b.bottom >= $(this).offset().top && !$(this).data('bPlay')) {
					$(this).data('bPlay', true);
					var a = $(this).find('span').text().replace(/\%/, '');
					if ($(this).find("span").text() !== "0%") {
						$(this).svgCircle({
							parent: $(this)[0],
							w: 66,
							R: 30,
							sW: 4,
							color: [self.color, self.color, self.color],
							perent: [66, a],
							speed: 150,
							delay: 400
						})
					}
					if ($(this).find("span").text() == "0%") {
						$(this).svgCircle({
							parent: $(this)[0],
							w: 66,
							R: 30,
							sW: 4,
							color: ["#ccc", "#ccc", "#ccc"],
							perent: [66, a],
							speed: 150,
							delay: 400
						})
					}
				}
			});
		}
		this.dprogressingbar = function(){
			var c = $('.dprocessingbar');
			var b = {
				top: $(window).scrollTop(),
				bottom: $(window).scrollTop() + $(window).height()
			};
			c.each(function() {
				if (b.top <= $(this).offset().top && b.bottom >= $(this).offset().top && !$(this).data('bPlay')) {
					$(this).data('bPlay', true);
					var a = $(this).find('span').text().replace(/\%/, '');
					if ($(this).find("span").text() !== "0%") {
						$(this).svgCircle({
							parent: $(this)[0],
							w: 66,
							R: 30,
							sW: 4,
							color: ["#999", "#999", "#999"],
							perent: [66, a],
							speed: 150,
							delay: 400
						})
					}
					if ($(this).find("span").text() == "0%") {
						$(this).svgCircle({
							parent: $(this)[0],
							w: 66,
							R: 30,
							sW: 4,
							color: ["#999", "#999", "#999"],
							perent: [66, a],
							speed: 150,
							delay: 400
						})
					}
				}
			});
		}
	}
	window.processingbar = new processingbar();
}();