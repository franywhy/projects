
/*===学习中心远程接口访问地址===*/
/*====开发线域名====*/
//var port = {
//	checkInPort       :		"http://183.63.120.222:8010",
//}
/*====正式线域名====*/
var port = {
	checkInPort       :		"http://api.kjcity.com",
	ssoPort:                "http://passport.kjcity.com"
}

window.Global = window.Global || {};
Global.KaoqianRemoteUrl = 'http://webapi.hqjy.com/api/kaoqian';
Global.MianshouRemoteUrl = 'http://webapi.hqjy.com/api/mianshou';
Global.WangluoRemoteUrl = 'http://webapi.hqjy.com/api/wangluo';
Global.KaoqinRemoteUrl = port.checkInPort + '/checkinnologin/student_checkin';  //中级职称直播考勤
Global.KaoqinRemoteUrlHui = port.checkInPort + '/checkinnologin/mid_live_rep_login_t';  //中级职称回放考勤
Global.ssoidRemoteUrl = port.ssoPort + '/getuserid';  //中级职称获取用户id