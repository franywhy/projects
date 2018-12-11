/*
只有名称和电话号码的数据
ymxx 对应的是 catname
new_hslx 对应的是  hslx 话术类型 
csw_type  对应的是  填写是  ban_csw_type    在提交按钮的  val(ban_csw_type)
new_typeid  73
tu  当前链接  pu 前一个链接  ymxx 栏目名称 phone 手机号码 reason  备注  xuyuanshu 当前方法  xuyuanshu  正式  xuyuanshu_test  测试数据
*/
function new_Check_Data(new_Data){
        var tu=new_Data.tu;
        var pu=new_Data.pu;
        var ymxx =new_Data.ymxx;
        var new_hslx=new_Data.new_hslx;
        // 默认
        var csw_type = new_Data.csw_type;
        var name=new_Data.name;
        var phone = new_Data.phone;
        var reason=new_Data.reason;
        var new_typeid=new_Data.new_typeid;
        var xuyuanshu=new_Data.xuyuanshu;
        if(name == '' ) {
            alert("请完善信息");
            return false;
        }
        var re = /^1[3,5,8,9,4,6,7]\d{9}$/;
        if(!re.test(phone) || phone==''){
             alert("手机号码不正确");
             return false;
        }    
        var dataall={
            'phone':phone,
            'name':name,
            'csw_type':csw_type,
            'tu':tu,
            'pu':pu,
            'ymxx':ymxx,
            'new_hslx':new_hslx,
            'new_typeid':new_typeid
        };
        $.ajax({
            url:"http://www.hengqijiaoyu.cn/?m=guestbook&c="+xuyuanshu+"&a=ban",
            data:dataall,
            type:"POST",
            dataType:'json',
            success:function(data){
                if(data==1){
                    alert('提交成功！');
                }else if(data==-1){
                    alert("你已经提交了！");
                }
            }
        }) 
}