<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>贷款页面</title>
    <style type="text/css">
        * {
            margin: 0 auto;
            padding: 0;
        }

        body {
            line-height: 1;
            background: url("./images/bg.jpg") left top no-repeat;
            font-size: 14px;
            font-family: "Microsoft Yahei";
        }

        .ml-1em {
            margin-left: 1em;
        }

        .mr-0px {
            margin-right: 0 !important;
        }

        .logo-box {
            text-align: center;
            margin-top: 143px;
        }

        .logo-box .logo {
            width: 143px;
            height: 38px;
            background: url("./images/logo_icon.png") left top no-repeat;
        }

        .layer-box {
            position: absolute;
            left: 50%;
            margin: 18px 0 0 -500px;
        }

        .layer-box .layer {
            width: 1000px;
            height: 580px;
            background: #fff;
        }

        .layer-box .content {
            padding: 40px 0 0 49px;
            overflow: hidden;
            position: relative;
        }

        .layer-box .content-left {
            width: 580px;
            height: 484px;
            border: 1px dashed #d9d9d9;
            float: left;
            padding: 9px 10px;
        }

        .layer-box .content-left .title {
            height: 108px;
            background: #f5f8fa;
            color: #999999;
            padding: 0 15px;
            font-size: 12px;
        }

        .layer-box .content-left .title .order-num {
            padding-top: 19px;
        }

        .layer-box .content-left .title .val {
            color: #333;
            font-size: 14px;
        }

        .layer-box .content-left .title .major {
            margin-top: 12px;
            overflow: hidden;
        }

        .layer-box .content-left .title .education {
            float: right;
            margin-right: 90px;
        }

        .layer-box .content-left .title .class-type {
            margin-top: 16px;
        }

        .layer-box .price-box {
            margin-top: 18px;
            padding: 0 11px;
            overflow: hidden;
        }

        .layer-box .price-box .list {
            float: left;
            margin-right: 30px;
        }

        .layer-box .price-box .price-title {
            color: #999;
            font-size: 12px;
        }

        .layer-box .price-box .num,
        .layer-box .price-box .num-1 {
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        .layer-box .price-box .num-1 {
            text-decoration: line-through;
        }

        .layer-box .price-box .num-2 {
            font-size: 20px;
            color: #ff4d44;
            font-weight: bold;
            line-height: 14px;
        }

        .layer-box .price-box .price {
            margin-top: 17px;
        }

        .layer-box .code-box {
            margin-top: 28px;
            text-align: center;
        }

        .qrcode-logo-box{
            position:absolute;
            top:45%;
            left:48%;
            margin:90px 0 0 -155px;
        }

        .layer-box .code-box img {
            width: 100%;
            display: inline-block;
            vertical-align: top;
        }

        .layer-box .code-prompt {
            width: 210px;
            height: 48px;
            background: #f5f8fa;
            overflow: hidden;
        }

        .layer-box .code-prompt p {
            line-height: 48px;
            font-weight: bold;
            font-size: 12px;
            color: #333;
            text-align: center;
        }

        .layer-box .code-prompt p span {
            font-size: 16px;
            padding: 0 5px;
        }

        .layer-box .content-right {
            width: 349px;
            float: right;
        }

        .layer-box .content-right .phone {
            width: 257px;
            height: 449px;
            background: url("./images/phone_img.png") left top no-repeat;
            margin-top: 37px;
        }

        .footer {
            width: 100%;
            margin-top: 705px;
        }

        .footer p {
            text-align: center;
            color: #fff;
            line-height: 20px;
            font-size: 16px;
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
                    <p class="order-num">订单号：<span class="val">${(loanEntity.orderNo)!}</span></p>
                    <div class="major">
                        <span>专<span class="ml-1em"></span>业：<span class="val">${(loanEntity.courseMajor)!}</span></span>
                        <span class="education">学<span class="ml-1em"></span>历：<span class="val">${(loanEntity.educLevel)!}</span></span>
                    </div>
                    <div class="class-type">班<span class="ml-1em"></span>型：<span class="val">${(loanEntity.classTypeName)!}</span></div>
                </div>
                <div class="price-box">
                    <div class="list">
                        <span class="price-title">课程原价：</span>
                        <span class="num-1">￥${(loanEntity.coursePrice)!}</span>
                        <div class="price">
                            <span class="price-title">本次支付：</span>
                            <span class="num-2">￥${(loanEntity.tradeMoney)!}</span>
                        </div>
                    </div>

                    <div class="list">
                        <span class="price-title">优惠折扣：</span>
                        <span class="num">￥-${(loanEntity.discount)!}</span>
                        <div class="price">
                            <span class="price-title">已付金额：</span>
                            <span class="num">￥${(loanEntity.paidMoney)!}</span>
                        </div>
                    </div>

                    <div class="list mr-0px">
                        <span class="price-title">折后价格：</span>
                        <span class="num">￥${(loanEntity.discountMoney)!}</span>
                        <div class="price">
                            <span class="price-title">剩余支付：</span>
                            <span class="num">￥${(loanEntity.overpayMoney)!}</span>
                        </div>
                    </div>
                </div>
                <div class="code-box">
                    <div class="qrcode-logo-box">
                        <img src="/images/kzlogo.png" class="qrcode-logo">
                    </div>
                    <#--<img src="images/code.png" alt="qr-code" id="qrcode"/>-->
                        <div class="qrcode-img" id="qrcode"></div>
                </div>
                <div class="code-prompt">
                    <p>请使用<span>课栈APP</span>扫码分期</p>
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
<script src="/js/jquery.min.js"></script>
<script src="/js/jquery.qrcode.js"></script>
<script src="/js/qrcode.js"></script>
<script>
    //生成二维码
    $("#qrcode").qrcode({
        render: "canvas", //table方式 canvas
        typeNumber  : -1,      //计算模式
        correctLevel    : QRErrorCorrectLevel.H,//纠错等级
        background      : "#ffffff",//背景颜色
        foreground      : "#000000", //前景颜色
        width: 210, //宽度
        height:210, //高度
        text: "kezhanwang://coursedetail?id=${(courseId)!}"
    });
</script>
</body>
</html>