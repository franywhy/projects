
$(
    function () {
        $("a[name='closeMsg']").on("click", function () {
            $(this).parent().parent().slideUp();
        });
    }
);