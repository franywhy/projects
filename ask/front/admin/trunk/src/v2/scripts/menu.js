/**
 * 菜单模块
 * @author 	fulh
 * @date    2014-06-04 10:06:42
 */
$.module("Izb.menu", function () {
	var _menus = {
		website: {
			name: "网站管理",
			menus: {
				"poster": {
					name: "海报管理",
					basepower: "poster"
				},
				notice: {
					name: "公告管理",
					basepower: "notice"
				}
			}
		},
		usersys: {
			name: "用户管理",
			menus: {
				"user": {
					name: "用户信息",
					modules: [
					{ key: "user_info", name: "查看用户信息", powers: ["user"] },
					{ key: "user_info", name: "用户信息查看", powers: ["user"] },
					{ key: "donate_vip", name: "赠送Vip", powers: ["finance/change_vip"] },
					{ key: "donate_car", name: "赠送座驾", powers: [] },
					{ key: "donate_horn", name: "赠送喇叭", powers: [] },
					{ key: "", name: "", powers: [] }
					]
				},
				"star": {
					name: "主播信息",
					modules: [
					{ key: "star_info", name: "查看主播信息", powers: ["star", "user", "apply"] },
					{ key: "", name: "", powers: [] },
					{ key: "", name: "", powers: [] },
					{ key: "", name: "", powers: [] }
					]
				}
			}
		}
	};


	return {
		getAllMenu: function name(argument) {
			//show.

		}
	};
});
