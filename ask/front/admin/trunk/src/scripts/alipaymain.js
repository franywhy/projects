/**
 * Created by Administrator on 2015/10/8.
 */

$.module("Izb.alipaymain",function(){
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }


    function _init(){
        Izb.common.getResult({
            action:"/payment/applyHtml",
            type: 'POST',
            data : {"id" : GetQueryString("id")},
            success:function(result){
                console.log(result);
                document.write(result.data);
            },
            error: function (msg) {
                console.log(msg);
            }
        });
    }


    return {
        init:_init
    };
});

Izb.alipaymain.init();


