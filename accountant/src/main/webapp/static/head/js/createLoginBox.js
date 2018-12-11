/*******测试环境********/
// var accountant_domain = "http://kjpcweb.ljtest.hqjy.com";

/*******生产环境********/
var accountant_domain = "http://www.hqjy.com";


function createLoginPopup(){
	$("body").append("<div class=\"mask-login\"  style=\"display: none\"></div>\
	<div class=\"login-box tologin\"  style=\"display: none\">\
		<div class=\"close\"></div>\
		<div class=\"red-alert\"></div>\
		<div class=\"login-main\">\
			<div class=\"login-title-tab clearfix\">\
				<div class=\"login-tab-title login-tab-title-left act\">登录</div>\
				<div class=\"login-tab-title \">注册</div>\
			</div>\
			<div class=\"login-title-main clearfix\" >\
				<div class=\"login-tab-main\"  style=\"display: none\">\
					<input placeholder=\"手机号/邮箱\" type=\"text\" class=\"input-t input-login\" id=\"sMobile\">\
					<input placeholder=\"密码\" type=\"password\" class=\"input-pw input-login\" id=\"sPwd\">\
					<div class=\"input-btn validate\">立即登录</div>\
					<a href=\"javascript:;\" class=\"disremember disrememberpw\">忘记密码？</a>\
				</div>\
				<div class=\"login-tab-main\"   style=\"display: none\">\
					<input placeholder=\"请输入手机号\" type=\"text\" class=\"input-t input-login\" id=\"rMobile\">\
					<div class=\"input-register-box clearfix\">\
						<input placeholder=\"图形验证\" type=\"text\" class=\"input-img-yz input-login\" id=\"userCode\">\
						<script type=\"text/javascript\">$(document).ready(function() {\
			              var imgCode = $(\"#imgCode\");\
			              imgCode.attr(\"src\", \""+accountant_domain+"/user/verifyCode?\" + Math.random());\
			              imgCode.css('display', '');\
			              imgCode.click(function() {\
			               imgCode.attr(\"src\", \""+accountant_domain+"/user/verifyCode?\" + Math.random());\
			              });\
 						});</script>\
 						<a id=\"recode\" class=\"recode\" onclick=\"javascript:document.getElementById('imgCode').src='"+accountant_domain+"/user/verifyCode?id='+Math.random();\" style=\"cursor: hand;\">换一个</a>\
 						<img class=\"input-img imgCode\" id=\"imgCode\" title=\"看不清？点击换一个\">\
 					</div>\
					<div class=\"input-register-box\">\
						<input placeholder=\"短信验证码\" type=\"text\" class=\"input-msg input-login\" id=\"sCode\">\
						<div class=\"input-btn\" id=\"getCode\">获取验证码</div>\
					</div>\
					<input placeholder=\"请输入密码\" type=\"password\" class=\"input-pw input-login\" id=\"r_sPwd\">\
					<input placeholder=\"请输入确认密码\" type=\"password\" class=\"input-pw input-login\" id=\"u_sPwd\">\
					<div class=\"cast-p\">\
							<input type=\"checkbox\" id='checkbox-cast'>\
							<span class=\"checkbox-cast-p\"><label for=\"checkbox-cast\">同意恒企教育</label>《<b>服务条款</b>》</span>\
					</div>\
					<div class=\"input-btn register\">立即注册</div>\
					<a href=\"http://looyuoms7811.looyu.com/chat/chat/p.do?_server=0&c=20001784&f=10068290&g=10066933\" target=\"_blank\" class=\"disremember cantregister\">无法注册？</a>\
				</div>\
			</div>\
		</div>\
	</div>\
	<div class=\"login-box toreact\"  style=\"display: none\">\
		<div class=\"close\"></div>\
		<div class=\"return-icon\"></div>\
		<div class=\"red-alert\"></div>\
		<div class=\"login-main\">\
			<div class=\"react-p\">重置密码</div>\
			<div class=\"login-title-main clearfix\">\
				<div class=\"login-tab-main\">\
					<input placeholder=\"请输入手机号\" type=\"text\" class=\"input-t input-login\" id=\"rMobile1\">\
					<div class=\"input-register-box clearfix\">\
						<input placeholder=\"图形验证\" type=\"text\" class=\"input-img-yz input-login\" id=\"userCode1\">\
						<script type=\"text/javascript\">$(document).ready(function() {\
			              var imgCode = $(\"#imgCode1\");\
			              imgCode.attr(\"src\", \""+accountant_domain+"/user/verifyCode?\" + Math.random());\
			              imgCode.css('display', '');\
			              imgCode.click(function() {\
			               imgCode.attr(\"src\", \""+accountant_domain+"/user/verifyCode?\" + Math.random());\
			              });\
 						});</script>\
 						<a id=\"recode1\" class=\"recode\" onclick=\"javascript:document.getElementById('imgCode1').src='"+accountant_domain+"/user/verifyCode?id='+Math.random();\" style=\"cursor: hand;\">换一个</a>\
 						<img class=\"input-img imgCode\" id=\"imgCode1\" title=\"看不清？点击换一个\">\
 					</div>\
					<div class=\"input-register-box\">\
						<input placeholder=\"短信验证码\" type=\"text\" class=\"input-msg input-login\" id=\"sCode1\">\
						<div class=\"input-btn\" id=\"getCode1\">获取验证码</div>\
					</div>\
					<input placeholder=\"请输入新密码\" type=\"password\" class=\"input-pw input-login\" id=\"r_sPwd1\">\
					<input placeholder=\"请输入确认密码\" type=\"password\" class=\"input-pw input-login\" id=\"u_sPwd1\">\
					<div class=\"input-btn repass\">提交</div>\
					<a href=\"javascipt:;\" class=\"disremember cantregister\" style=\"visibility: hidden;\">无法注册？</a>\
				</div>\
			</div>\
		</div>\
	</div>\
	<div class=\"login-box login-txt\"  style=\"display: none\">\
		<div class=\"close\"></div>\
		<div class=\"red-alert\"></div>\
		<div class=\"login-main\">\
			<div class=\"login-txt-title\">恒企教育服务条款</div>\
			<div class=\"login-txt-p  login-txt-p-big\"><b>一、注册账号服务条款的确认和接受</b></div>\
			<div class=\"login-txt-p\">恒企/恒企教育网(以下亦称“本网站”)同意按照本服务条款的规定及其不定时发布的操作规则提供基于互联网和移动互联网的相关服务(以下称“网络服务”)。</div>\
			<div class=\"login-txt-p\">为获得网络服务, 申请人应当认真阅读、充分理解本《服务条款》中各条款, 包括免除或者限制恒企责任的免责条款及对用户的权利限制条款。审慎阅读并选择接受或不接受本《服务条款》(未成年人应在法定监护人陪同下阅读)。</div>\
			<div class=\"login-txt-p\">同意接受本服务条款的全部条款的, 申请人应当按照页面上的提示完成全部的注册程序, 并在注册程序过程中点击“同意”按钮, 否则视为不接受本《服务条款》全部条款, 申请人应当终止并退出申请。限制、免责条款可能以加粗形式提示您注意。</div>\
			<div class=\"login-txt-p\">同意接受本服务条款的全部条款的, 申请人应当按照页面上的提示完成全部的注册程序, 并在注册程序过程中点击“同意”按钮, 否则视为不接受本《服务条款》全部条款, 申请人应当终止并退出申请。限制、免责条款可能以加粗形式提示您注意。</div>\
			<div class=\"login-txt-p  login-txt-p-big\"><b>二、服务内容</b></div>\
			<div class=\"login-txt-p\">1、网络服务的具体内容由恒企根据实际情况提供, 包括学习资讯、学习工具、恒企社群、恒企网校等服务。</div>\
			<div class=\"login-txt-p\">2、恒企提供的网络服务包括收费和免费。收费服务包括但不限于恒企网校收费课程, 用户使用收费网络服务需要支付约定费用。对于收费服务, 恒企会在用户使用之前给予用户明确的提示, 只有用户根据提示确认同意支付相关费用, 用户才能使用该等收费服务。如用户未支付相关费用, 则恒企有权不向用户提供该等收费服务。</div>\
			<div class=\"login-txt-p\">2、恒企提供的网络服务包括收费和免费。收费服务包括但不限于恒企网校收费课程, 用户使用收费网络服务需要支付约定费用。对于收费服务, 恒企会在用户使用之前给予用户明确的提示, 只有用户根据提示确认同意支付相关费用, 用户才能使用该等收费服务。如用户未支付相关费用, 则恒企有权不向用户提供该等收费服务。</div>\
			<div class=\"login-txt-p\">4、用户理解，恒企提供并提请用户在购买课程前先完整试听。用户可按《退班政策说明》，在从购买课程之日起7天之内且未产生听课记录（不含试听）的，可无理由退班。除特别说明外，恒企网校包括但不限于PC端和移动端下载课件在内的所有服务均附期限，到期终止。用户应在截止日期前享受其购买的服务。</div>\
			<div class=\"login-txt-p  login-txt-p-big\"><b>三、用户帐号</b></div>\
			<div class=\"login-txt-p\">1、经恒企注册系统完成注册程序并通过身份认证的用户即为正式用户。</div>\
			<div class=\"login-txt-p\">2、如发现用户帐号中含有不雅文字或不恰当名称的, 恒企保留注销其用户帐号的权利。 </div>\
			<div class=\"login-txt-p\">3、用户帐号的所有权归恒企, 用户完成申请注册手续后, 用户享有使用权。3个月未使用的用户账号, 恒企保留收回的权利。 </div>\
			<div class=\"login-txt-p\">4、用户有义务保证密码和帐号的安全, 用户利用该帐号所进行的一切活动引起的任何损失或损害, 由用户自行承担全部责任, 恒企不承担任何责任。如用户发现帐号遭到未授权的使用或发生其他任何安全问题, 应立即修改账号密码并妥善保管。因黑客行为或用户的保管疏忽导致帐号非法使用, 恒企不承担任何责任。 </div>\
			<div class=\"login-txt-p\"><b>5、用户帐号和密码仅由初始申请注册人使用，用户不得将自己用户账户或密码有偿或无偿以转让、出借、授权等方式提供给第三人操作和使用, 否则用户应当自行承担因违反此要求而遭致的任何损失。违反本项约定的，恒企并保留收回账号的权利。若因违反本约定对他人造成损害的，用户应与实际使用人承担连带赔偿责任，同时恒企保留追究用户违约责任的权利。 </b></div>\
			<div class=\"login-txt-p\">6、用户帐号在丢失、遗忘密码及因合用产生使用权归属纠纷后, 须遵照恒企的申诉途径请求找回帐号。用户可以凭初始注册资料向恒企申请找回帐号。恒企的账户恢复机制仅负责识别申请用户所提资料与系统记录资料是否一致, 而无法识别申诉人是否系帐号的真正使用权人。对用户因被他人冒名申请而致的任何损失, 恒企不承担任何责任, 用户知晓帐号及密码保管责任在于用户, 恒企并不承诺帐号丢失或遗忘密码后用户一定能通过申诉找回帐号。用户应当谨慎填写初始注册手机作为确认接收争议帐号的指定手机。 </div>\
			<div class=\"login-txt-p\">7、恒企<b>建议用户应当使用本人名义为用户账户充值或行使付款行为。若用户存在使用第三人的名义进行充值或付款，或委托第三人代为充值或付款之行为的，则以上行为被视作本人的行为，若由于该第三人行为导致充值或付款行为失败或成功后又被撤销的，均被认为是用户本人真实意思的表示和用户本人实施的行为，由此所造成的一切法律后果均由用户自行承担。</b></div>\
			<div class=\"login-txt-p  login-txt-p-big\"><b>四、使用规则</b></div>\
			<div class=\"login-txt-p\">1、用户在使用恒企网络服务过程中, 必须遵循国家的相关法律法规, 不得发布危害国家安全、色情、暴力、侵犯版权等任何合法权利等非法内容; 也不得利用恒企平台发布含有虚假、有害、胁迫、侵害他人隐私、骚扰、侵害、中伤、粗俗、或其它道德上令人反感的内容。</div>\
			<div class=\"login-txt-p\">1、用户在使用恒企网络服务过程中, 必须遵循国家的相关法律法规, 不得发布危害国家安全、色情、暴力、侵犯版权等任何合法权利等非法内容; 也不得利用恒企平台发布含有虚假、有害、胁迫、侵害他人隐私、骚扰、侵害、中伤、粗俗、或其它道德上令人反感的内容。</div>\
			<div class=\"login-txt-p\">2、恒企可依其合理判断, 对违反有关法律法规或本服务条款约定; 或侵犯、妨害、威胁任何人权利或安全的内容, 或者假冒他人的行为, 恒企有权停止传输任何前述内容, 并有权依其自行判断对违反本条款的任何用户采取适当的法律行动, 包括但不限于, 删除具有违法性、侵权性、不当性等内容, 阻止其使用恒企全部或部分网络服务, 并且依据法律法规保存有关信息并向有关部门报告等。</div>\
			<div class=\"login-txt-p\">3、对于经由恒企网络服务而传送的内容, 恒企不保证前述内容的正确性、完整性或品质。用户在接受本服务时, 有可能会接触到令人不快、不适当或令人厌恶的内容。在任何情况下, 恒企均不对任何内容负责, 包括但不限于任何内容发生任何错误或纰漏以及衍生的任何损失或损害。恒企有权(但无义务)自行拒绝或删除经由恒企网络服务提供的任何内容。用户使用上述内容, 应自行承担风险。</div>\
			<div class=\"login-txt-p\">4、对于用户通过恒企网络服务(包括但不限于恒企社群、网校等服务)上传到恒企上可公开获取区域的任何内容, 用户同意恒企在全世界范围内具有免费的、永久性的、不可撤销的、非独家的和完全再许可的权利和许可, 以使用、复制、修改、改编、出版、翻译、据以创作衍生作品、传播、表演和展示此等内容(整体或部分), 和/或将此等内容编入当前已知的或以后开发的其他任何形式的作品、媒体或技术中。</div>\
			<div class=\"login-txt-p\">5、用户通过恒企网络服务所发布的任何内容并不反映恒企的观点或政策, 恒企对此不承担任何责任。用户须对上述内容的真实性、合法性、无害性、有效性等全权负责, 与用户所发布信息相关的任何法律责任由用户自行承担, 与恒企无关。</div>\
			<div class=\"login-txt-p  login-txt-p-big\"><b>五、版权声明</b></div>\
			<div class=\"login-txt-p\">恒企提供的网络服务中包含的任何文本、图片、图形、音频和/或视频资料均受版权、商标和/或其它财产所有权法律的保护, 未经相关权利人同意, 上述资料均不得在任何媒体直接或间接发布、播放、出于播放或发布目的而改写或再发行, 或者被用于其他任何商业目的。所有以上资料或资料的任何部分仅可作为私人和非商业用途保存。恒企不就由上述资料产生或在传送或递交全部或部分上述资料过程中产生的延误、不准确、错误和遗漏或从中产生或由此产生的任何损害赔偿, 以任何形式, 向用户或任何第三方负责。</div>\
			<div class=\"login-txt-p login-txt-p-big\">六、隐私保护</div>\
			<div class=\"login-txt-p\">1、保护用户隐私是恒企的一项基本政策, 恒企保证不对外公开或向第三方提供用户的注册资料及用户在使用网络服务时存储在恒企内的非公开内容, 但下列情况除外:</div>\
			<div class=\"login-txt-p\">（1）事先获得用户的书面明确授权; </div>\
			<div class=\"login-txt-p\">（2）根据有关的法律法规要求; </div>\
			<div class=\"login-txt-p\">（3）按照相关政府主管部门的要求; </div>\
			<div class=\"login-txt-p\">（4）为维护社会公众的利益; </div>\
			<div class=\"login-txt-p\">（5）为维护恒企的合法权益;</div>\
			<div class=\"login-txt-p\"><b>2、为了更好地为用户提供全面服务，用户同意恒企将用户注册资料及使用信息提供恒企关联公司使用。恒企保证前述关联公司同等级地严格遵循本服务条款第六条第1款之隐私保护责任。</b></div>\
			<div class=\"login-txt-p\"><b>3、用户同意：恒企或恒企运营商的关联公司在必要时有权根据用户注册时或接受服务时所提供的联系信息（包括但不限于电子邮件地址、联系电话、联系地址、即时聊天工具账号等），通过电子邮件、电话、短信、邮寄、即时聊天、弹出页面等方式向用户发送如下信息：</b></div>\
			<div class=\"login-txt-p\"><b>（1）各类重要通知信息，可能包括但不限于订单、交易单、修改密码提示等重要信息。此类信息可能对用户的权利义务产生重大的有利或不利影响，用户务必及时关注。 </b></div>\
			<div class=\"login-txt-p\"><b>（2）商品和服务广告、促销优惠等商业性信息。若用户不愿意接收此类信息，则可通过告知（口头或书面）的方式通知恒企或恒企运营商的关联公司取消发送，亦可通过恒企或恒企运营商关联公司所提供的相应退订功能（若有）进行退订。</b></div>\
			<div class=\"login-txt-p login-txt-p-big\"><b>七、服务条款的用途、更新和效力</b></div>\
			<div class=\"login-txt-p\"><b>1、本服务条款用以规范用户使用恒企提供的服务,本服务条款与恒企社区行为准则构成完整的服务条款。</b></div>\
			<div class=\"login-txt-p\"><b>2、鉴于国家法律法规</b>不时变化及恒企运营之需要，恒企有权对本服务条款不时地进行修改，修改后的服务条款一旦被公布于恒企上即告生效，并替代原来的服务条款。用户有义务不时关注并阅读最新版的服务条款及网站公告。如用户不同意更新后的服务条款，则应立即停止接受恒企依据本服务条款提供的服务；若用户继续使用恒企提供的服务的，即视为同意更新后的服务条款。如果本服务条款中任何一条被视为废止、无效或因任何理由不可执行，该条应视为可分的且并不影响任何其余条款的有效性和可执行性。</div>\
			<div class=\"iagree-btn\">我同意</div>\
		</div>\
	</div>\
	");
}
createLoginPopup();