/// <reference path="jquery-1.8.3.min.js" />
;$(function () {

    $("[name='ul_change']").on("click", function () {
     
        post($("#posturl").val(), { ischangestu: $(this).attr("data-val") });

    });

    var post = function (URL, pars) {

        var temp = document.createElement("form");
        temp.action = URL;
        temp.method = "post";
        temp.style.display = "none";
        for (var x in pars) {
            var opt = document.createElement("textarea");
            opt.name = x;
            opt.value = pars[x];
            // alert(opt.name)        
            temp.appendChild(opt);
        }
        document.body.appendChild(temp);

        temp.submit();
        return temp;
    }
}
);