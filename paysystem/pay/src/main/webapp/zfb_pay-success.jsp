<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="format-detection" content="telephone=no">
<link type="text/css" charset="UTF-8" rel="stylesheet" href="${flag}statics/libs/css/min_base.css" />
<link type="text/css" charset="UTF-8" rel="stylesheet" href="${flag}statics/libs/css/pay-state.css" />
<title>支付成功</title>
</head>
<body>
	<div class="pay-state-yes">
		<img src="${flag}images/pay-yes.png"/>
		支付成功
	</div>
	<div class="pay-state-show">&yen;<span>${totalAmount}</span></div>
	<div class="pay-state-infor">
		<div class="pay-state-infor-item">
			<p class="pay-state-p">商品</p>
			<p>${orderName}</p>
		</div>
		
		<div class="pay-state-infor-item">
			<p>成&nbsp;交&nbsp;时&nbsp;间</p>
			<p>${date}</p>
		</div>
		<div class="pay-state-infor-item">
			<p>当  前  状  态</p>
			<p>${state}</p>
		</div>
		<div class="pay-state-infor-item">
			<p>商品订单编号</p>
			<p>${outTradeNo}</p>
		</div>
		<div class="pay-state-infor-item">
			<p>交易订单编号</p>
			<p>${tradeNo}</p>
		</div>
	</div>
  <script>document.documentElement.style.fontSize = document.body.clientWidth/7.5 + "px";</script>
</body>
</html>