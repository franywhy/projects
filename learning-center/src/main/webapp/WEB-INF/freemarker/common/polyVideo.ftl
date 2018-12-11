<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>恒企教育</title>
    <script type="text/javascript" src="../../../resources/lib/polyv/player.js"></script>
    <style>
        * {
            margin: 0 auto;
            padding: 0;
        }
        html, body {
            width: 100%;
            height: 100%;
        }
        #video {
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>

<div id='video'></div>

<script type="text/javascript">
    var player = polyvPlayer({
        wrap: '#video',
        width: '100%',
        height: '100%',
        speed: true,
        <#if redirectUrl??>
            'vid': '${redirectUrl!''}',
            'statistics': {
                'param1': '${param1}',
                'param2': '${param2}'
            }
        </#if>
    });
</script>
</body>
</html>