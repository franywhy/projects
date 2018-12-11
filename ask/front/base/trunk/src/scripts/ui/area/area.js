// 基于JQ的地区三级联动
//region.js为城市数据文件，调用时需要给cache赋值,如$("#area").area({cache:region});	 
// 创建一个闭包     
(function($) {
	var opts = {};
	//插件主要内容     
	$.fn.area = function(options) {
		// 处理默认参数   
		opts = $.extend({}, $.fn.area.defaults, options);

		//ajax获取地区数据

		function getData() {
			//可以通过AJAX把地区数据读取到opts.cache中,数据格式为					        /*{"provinces2":{"region_id":"2","region_name":"\u5317\u4eac","city":{"cities52":{"region_id":"52","region_name":"\u5317\u4eac","county":{"counties500":{"region_id":"500","region_name":"\u4e1c\u57ce\u533a"}}}  是一个三维数组的json数据,provinces2为省的id加provinces前缀作为key值;每个省数据中有city城市数据数组,每个城市数据中有county县数据数组*/
		}

		//获取省数据
		　　
		function getProvinces() {　　　　　　
			var pro = "";
			$.each(opts.cache, function(i, item) {
				// pro += "<option value="+item.region_name+" data-id="+item.region_id+">" + item.region_name + "</option>";						  	
				pro += "<option value=" + item.region_id + ">" + item.region_name + "</option>";
			});
			$(opts.provinceId).empty().append(pro);　　　　　　　　　　　　
			getCities();　　
		}

		//获取城市数据

		function getCities() {
			// var proIndex = $(opts.provinceId).attr('data-id');
			var proIndex = $(opts.provinceId).val();
			showCities(proIndex);
			getCounties();
		}

		//显示城市数据
		　　　　
		function showCities(proIndex) {　　　　　　
			var cit = "";　　　　　　
			if (opts.cache["provinces" + proIndex].city == null) {　　　　　　　　
				$(opts.cityId).empty();　　　　　　　　
				return;　　　　　　
			}

			$.each(opts.cache["provinces" + proIndex].city, function(i, item) {
				// cit += "<option value="+item.region_name+" data-id="+item.region_id+">" + item.region_name+ "</option>";
				cit += "<option value=" + item.region_id + ">" + item.region_name + "</option>";
			});　　　　　　
			$(opts.cityId).empty().append(cit);　　　　
		}

		//获取县数据
		　
		function getCounties() {　　　　　　
			var proIndex = $(opts.provinceId).val();　　　　　　
			var citIndex = $(opts.cityId).val();　　　　　　
			showCounties(proIndex, citIndex);　　　　
		}

		//显示县数据	
		　　　　
		function showCounties(proIndex, citIndex) {　　　　　
			var cou = "";　　　　　　
			if (opts.cache["provinces" + proIndex].city["cities" + citIndex].county == null) {　　　　　　　　
				$(opts.countyId).empty();　　　　　　　　
				return;　　　　　　
			}
			$.each(opts.cache["provinces" + proIndex].city["cities" + citIndex].county, function(i, item) {
				// cou += "<option value="+item.region_name+" data-id="+item.region_id+">" + item.region_name+ "</option>";
				cou += "<option value=" + item.region_id + ">" + item.region_name + "</option>";
			});　　　　　　
			$(opts.countyId).empty().append(cou);　　　　
		}

		// 保存JQ的连贯操作    
		return this.each(function() {
			getProvinces();
			$(opts.provinceId).change(function() {
				getCities();
			});
			$(opts.cityId).change(function() {
				getCounties();
			});
		});
	};
	//插件主要内容结束
	
	$.area = {
		location2Ids : function (text) {
			var locAry = text.split('-'),
				res = {
					province : 0,
					city : 0,
					county : 0
				},
				cache = opts.cache;

			if (locAry.length == 0) return res;

			for (var key in cache) {
				var tempP = cache[key];

				if (tempP.region_name == locAry[0]) {
					res.province = tempP.region_id;

					if (locAry[1]) {
						var tempC = tempP.city;

						for (var cKey in tempC)  {
							var city = tempC[cKey];

							if (city.region_name == locAry[1]) {
								res.city = city.region_id;

								var tempCo = city.county;

								if (locAry[2]) {
									for (var coKey in tempCo) {
										var county = tempCo[coKey];

										if (county.region_name == locAry[2]) {
											res.county = county.region_id;

											break;
										}
									}

									break;
								}

							}
						}
					}

					break;
				}
			}

			return res;
		},
		ids2Location : function (pId, cId, coId) {
			var province = '',
				city = '',
				county = '',
				cache = opts.cache;

			for (var key in cache) {
				var tempP = cache[key];

				if (tempP.region_id == pId) {
					province = tempP.region_name;

					var tempC = tempP.city['cities'+cId];
					city = tempC.region_name;

					var tempCo = tempC.county['counties'+coId];
					county = tempCo.region_name;

					break;
				}
			}

			return province+'-'+city+'-'+county;
		}
	}

	// 插件的defaults     
	$.fn.area.defaults = {
		url: '',
		cache: '', //地区数据
		provinceId: '#province',
		cityId: '#city',
		countyId: '#county'
	};
	// 闭包结束     
})($);