<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>支付</title>
    <style type="text/css">
        * {
            margin: 0 auto;
            padding: 0;
        }

        body {
            line-height: 1;
            background: url("./images/bg.jpg") left top no-repeat;
            font-size: 14px;
        }

        .ml-1em {
            margin-left: 1em;
        }

        .logo-box {
            text-align: center;
            margin-top: 143px;
        }

        .logo-box .logo {
            width: 140px;
            height: 26px;
            background: url("./images/logo.png") left top no-repeat;
        }

        .layer-box {
            position: absolute;
            top: 50%;
            left: 50%;
            margin: -286.5px 0 0 -500px;
        }

        .layer-box .layer {
            width: 1000px;
            height: 573px;
            background: #fff;
        }

        .layer-box .content {
            padding: 33px 0 35px 24px;
            overflow: hidden;
            position: relative;
        }

        .layer-box .content-left {
            width: 511px;
            height: 471px;
            border: 1px dashed #d9d9d9;
            float: left;
            padding: 16px;
        }

        .layer-box .content-left .title {
            width: 488px;
            height: 107px;
            background: #f5f5f5;
            color: #999999;
            padding: 0 13px;
            font-size: 14px;
        }

        .layer-box .content-left .title .order-num {
            padding-top: 18px;
        }

        .layer-box .content-left .title .val {
            color: #000;
            font-size: 16px;
        }

        .layer-box .content-left .title .major {
            margin-top: 11px;
        }

        .layer-box .content-left .title .education {
            margin-left: 35px;
        }

        .layer-box .content-left .title .class-type {
            margin-top: 11px;
        }

        .layer-box .price-box {
            margin-top: 18px;
            padding: 0 13px;
        }

        .layer-box .price-box .price-title {
            color: #999;
            font-size: 14px;
        }

        .layer-box .price-box .num,
        .layer-box .price-box .num-1 {
            font-size: 18px;
            font-weight: bold;
            color: #333;
        }

        .layer-box .price-box .num-1 {
            text-decoration: line-through;
        }

        .layer-box .price-box .num-2 {
            font-size: 26px;
            color: #ff4d44;
            font-weight: bold;
        }

        .layer-box .price-box .discounts {
            margin-left: 88px;
        }

        .layer-box .price-box .residue {
            margin-left: 48px;
        }

        .layer-box .price-box .price {
            margin-top: 9px;
        }

        .layer-box .code-box {
            margin-top: 36px;
            text-align: center;
        }

        .layer-box .code-box img {
            width: 194px;
            height: 194px;
            display: inline-block;
            border: 1px solid #eee;
            padding: 7px;
            vertical-align: top;
        }

        .layer-box .code-prompt {
            width: 210px;
            height: 50px;
            background: #ffda05;
            overflow: hidden;
        }

        .layer-box .code-prompt .scan-icon {
            width: 25px;
            height: 22px;
            background: url("./images/scan_icon.png") left top no-repeat;
            float: left;
            margin: 13px 0 0 12px;
        }

        .layer-box .code-prompt .text {
            color: #333;
            margin: 6px 0 0 10px;
            float: left;
        }

        .layer-box .code-prompt .text p {
            line-height: 18px;
        }

        .layer-box .code-prompt .text p span {
            font-size: 16px;
            font-weight: bold;
        }

        .layer-box .content-right {
            width: 397px;
            float: right;
        }

        .layer-box .content-right .phone {
            width: 286px;
            height: 505px;
            background: url("./images/phone.png") left top no-repeat;
        }

        .footer {
            width: 100%;
            position: absolute;
            bottom: 80px;
        }

        .footer p {
            text-align: center;
            color: #fff;
        }
    </style>
</head>
<body>
<div class="logo-box">
    <div class="logo"></div>
</div>
<div class="layer-box">
    <div class="layer">
        <div class="content">
            <div class="content-left">
                <div class="title">
                    <p class="order-num">订单号：<span class="val">${(orderNo)!}</span></p>
                    <div class="major">
                        <span>专<span class="ml-1em"></span>业：<span class="val">${(coursemajor)!}</span></span>
                        <span class="education">学<span class="ml-1em"></span>历：<span class="val">${(educlevel)!}</span></span>
                    </div>
                    <div class="class-type">班<span class="ml-1em"></span>型：<span class="val">${(classtypename)!}</span></div>
                </div>
                <div class="price-box">
                    <span class="price-title">课程原价：</span>
                    <span class="num-1">¥${(courseprice)!}</span>
                    <span class="discounts">
                            <span class="price-title">优惠折扣：</span>
                            <span class="num">-¥${(discount)!}</span>
                        </span>
                    <div class="price">
                            <span>
                                <span class="price-title">本次支付：</span>
                                <span class="num-2">${(currentpaymoney)!}</span>
                            </span>
                        <span class="residue">
                                <span class="price-title">剩余支付：</span>
                                <span class="num">¥${(overpaymoney)!}</span>
                            </span>
                    </div>
                </div>
                <div class="code-box">
                    <div class="qrcode-img" id="qrcode"></div>
                </div>
                <div class="code-prompt">
                    <div class="scan-icon"></div>
                    <div class="text">
                        <p>请使用<span>微信</span>或<span>支付宝</span></p>
                        <p>扫一扫扫描二维码支付</p>
                    </div>
                </div>
            </div>
            <div class="content-right">
                <div class="phone"></div>
            </div>
        </div>
    </div>
</div>
<div class="footer">
    <p>Copyright © 2016-2018 恒企教育 Inc. All rights reserved</p>
</div>
</body>
<script src="/js/jquery.min.js"></script>
<script src="/js/jquery.qrcode.js"></script>
<script src="/js/qrcode.js"></script>
<script type="text/javascript">

    <!--获取地址栏中的链接参数-->
    var o='${(payOrderNo)!}';
    var flag='${(flag)!}';
    var payUrl='${(payUrl)!}';
    var url=payUrl+"/p?o="+o;
    <!--清空div内容，重新生成二维码-->
    <!--生成二维码-->
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