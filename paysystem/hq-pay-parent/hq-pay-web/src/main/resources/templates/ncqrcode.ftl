<html>
<head>
<title>生成二维码</title>
<style>
*{
	margin: 0 auto;
	padding:0;
}

ol,ul{
	list-style:none;
}

.container .bg{
	width:100%;
	height:100%;
	posiiton:fixed;
	background: url(images/by.png) no-repeat;
	opacity:0.6;
	filter:Alpha(opacity=60);
}

.container .content{
	width:736px;
	height:484px;
	background:#fff;
	position:absolute;
	top:50%;
	left:50%;
	margin:-242px 0 0 -368px;
	z-index:9;
	box-shadow: 6px 0 20px rgba(0,0,0,0.3);
}

.container .content .title{
	height:60px;
	line-height:60px;
	font-size:18px;
	color:#333;
	border:1px solid #eee;
	padding-left:20px;
}

.container .content .section{
	padding:20px 50px 0 100px;
	position:relative;
}

.container .content .section .text{
	font-size:14px;
	color:#333;
}

.container .content .section .text span{
	color:#f00;
}

.container .content .section .qrcode{
	width:210px;
	height:210px;
	border:1px solid #ddd;
	margin:10px 0 10;
}

.container .content .section .qrcode-img{
	width:180px;
	height:180px;
	display:inline-block;
	margin-top:15px;
}

.container .content .section .prompt-text-box{
	width:210px;
	height:60px;
	background:#ffda05;
	margin-left:0;
	border:1px solid #ffda05;
	overflow:hidden;
}

.container .content .section .prompt-text{
	margin: 14px 0 0 15px;
}

.container .content .section .prompt-text .saoma-icon{
	width:25px;
	height:22px;
	background:url("./images/saoma_icon.png") left top no-repeat;
	margin:5px 10px 0 0;
}

.container .content .section .qrcode-logo-box{
	position:absolute;
	top:45%;
	left:48%;
	margin:10px 0 0 -183px;
}

.container .content .section .qrcode-logo-box .qrcode-logo{
	width:110%;
}

.container .content .section .prompt-text li{
	float:left;
	font-size:12px;
}

.container .content .section .section-right{
	position:absolute;
	right:100px;
	top:50px;
}

.container .content .section .section-right .phone-icon{
	width:221px;
	height:325px;
	background:url("./images/phone_icon.png") left top no-repeat;
}

/* 
.div {
	margin: 320 auto;
	text-align: center;
	
} */
</style>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<script src="/js/jquery.min.js"></script>
<script src="/js/jquery.qrcode.js"></script>
<script src="/js/qrcode.js"></script>
</head>
<body>
	<div class="container">
		<div class="bg"></div>
		<div class="content">
			<p class="title">扫码支付</p>
			<div class="section">
				<div class="section-left">
					<p class="text">交易金额：<span>${(tradeMoney)!}</span>元</p>
					<p class="text">订单名称：${(orderName)!}</p>
					<p class="text">订单单号：${(orderNo)!}</p>
					<div class="qrcode">
					    <div class="qrcode-logo-box">
							<img src="/images/qrcode_logo.png" class="qrcode-logo">
						</div>
						 <div class="qrcode-img" id="qrcode"></div> 
						<!--<img src="./images/qrcode_img.png" class="qrcode-img">-->
					</div>
					<div class="prompt-text-box">
						<div class="prompt-text">
							<ul>
								<li>
									<div class="saoma-icon"></div>
								</li>
								<li>
									<p>请使用微信或支付宝扫一扫</p>
									<p>扫描二维码支付</p>
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="section-right">
					<div class="phone-icon"></div>
				</div>
			</div>
		</div>
	</div>
</body>

<script type="text/javascript">

<!--获取地址栏中的链接参数-->
	var o='${(payOrderNo)!}';
	var flag='${(flag)!}';
	var payUrl='${(payUrl)!}';
	var url=payUrl+"/p?o="+o;
	<!--清空div内容，重新生成二维码-->
	<!--生成二维码-->
	<!--'http://weixin.hqjy.com/pay/p.jsp?o='+o//任意内容-->
	$("#qrcode").qrcode({ 
		render: "canvas", //table方式 canvas
		typeNumber  : -1,      //计算模式  
		correctLevel    : QRErrorCorrectLevel.H,//纠错等级  
		background      : "#ffffff",//背景颜色  
		foreground      : "#000000", //前景颜色
		width: 210, //宽度 
		height:210, //高度
		text:url
	}); 
    </script>
</html>