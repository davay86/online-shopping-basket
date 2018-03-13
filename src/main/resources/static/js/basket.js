function addItem() {
    if ($('#itemSelect').val() != '') {
        var sendData={
            "name" : $('#itemSelect').val(),
            "price" : 0
        }
        $.ajax({
            type: "POST",
            url: "/items",
            async: true,
            data: sendData,
            success: function (result) {
                $('#selectedItems').load("getItems");
            }
        })
    }
}
function removeItem(e) {
    var sendData={
        "name" : e,
        "price" : 0
    }
    $.ajax({
        type: "POST",
        url: "/removeItem",
        async: true,
        data: sendData,
        success: function (result) {
            $('#selectedItems').load("getItems");
        }
    })
}