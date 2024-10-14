
var iconNames ={
    'PICKED' : 'fa-people-carry',
    'SHIPPING' : 'fa-shipping-fast',
    'DELIVERED' : 'fa-box-open',
    'RETURNED' : 'fa-undo',

};

var confirmText;
var confirmModalDialog;
var yesButton;
var noButton;
$(document).ready(function () {
    confirmText = $("#confirmText")
    confirmModalDialog = $("#confirmModal")
    yesButton = $("#yesButton")
    noButton =$("#noButton")

    $(".linkUpdateStatus").on("click",function (e){
        e.preventDefault();
        link = $(this)
        showUpdateConfirm(link)
    })

    addEventHandlerForYesButton()
});


function addEventHandlerForYesButton(){
    yesButton.click(function (e){
        e.preventDefault()
        sendRequestToUpdateOrderStatus($(this))
    })
}



function sendRequestToUpdateOrderStatus(button) {

    url = button.attr("href");


    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        },
    }).done(function (response) {
       showMessageModal("Order updated successfully")
        updateStatusIconColor(response.orderId,response.status)
    }).fail(function (err) {

    })

}

function updateStatusIconColor(orderId, status) {
    link = $("#link"+status+orderId)
    link.replaceWith(" <i class='fas "+iconNames[status]+" fa-2x icon-green'></i>")

}

function showUpdateConfirm(link){
    noButton.text("No")
    yesButton.show()
    orderId= link.attr("orderId")
    orderStatus = link.attr("status")
    yesButton.attr("href",link.attr("href"));
    confirmText.text("Are you sure you want to update status of the order ID #"+orderId
    +" to "+orderStatus+"?")
    confirmModalDialog.modal();
}


function showMessageModal(message){
    noButton.text("Close")
    yesButton.hide()
    confirmText.text(message)
}
