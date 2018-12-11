var onmouse = document.getElementById("onmouse").getElementsByTagName("div");
var i = 0; try {
    for (i = 0; i < onmouse.length; i++) {

        onmouse[i].onmouseover = function () {
            //  this.getElementsByTagName("ul")[0].style.display = "block";
        }

        onmouse[i].onmouseout = function () {
            //     this.getElementsByTagName("ul")[0].style.display = "none";
        }


    } 
} catch (e) {

}