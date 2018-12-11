
$(function () {

    //获取商品列表
    var lstProduct = $("li[name='li_product']");
    //获取页显示数
    var productPageSize = parseInt($("#btnChanageProduct").attr("data-pagesize"));
    //获当前页数
    var productPageIndex = parseInt($("#btnChanageProduct").attr("data-pageindex"));
    //获取商品总数
    var productCount = parseInt($("#btnChanageProduct").attr("data-pagecount"));
    //获取页数
    var productPageCount = productCount / productPageSize + (productCount % productPageSize > 0 ? 1 : 0);
    var InitList = function (arg) {
        if (!isNaN(arg) && parseInt(arg) > 0) {
            $("li[name='li_product']").hide();
            $("li[name='li_product']");
           
            var tmpSize = (arg * productPageSize) > productCount ? productCount : arg * productPageSize;
            for (var i = (arg - 1) * productPageSize; i < tmpSize; i++) {
               
                $("#li_product_" + (i+1)).show();
            }
        }
    }

    $("#btnChanageProduct").on("click", function () {

        var tmpProductPageindex = parseInt($("#btnChanageProduct").attr("data-pageindex"));
        if (tmpProductPageindex + 1 > productPageCount) {
            tmpProductPageindex = 1;
        } else {
            tmpProductPageindex = tmpProductPageindex + 1;
        }
        InitList(tmpProductPageindex);
        $("#btnChanageProduct").attr("data-pageindex", tmpProductPageindex);

    });

    InitList(productPageIndex);
});