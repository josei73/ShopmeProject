decimalSeparator = decimalPointType == 'COMMA' ?  "," : ".";
thousandsSeparator = thousandPointType == 'COMMA' ?  "," : ".";

$(document).ready(function () {
    $('.linkMinus').on("click", function (evt) {
        evt.preventDefault();
        decreaseQuantity($(this))

    });


    $('.linkPlus').on("click", function (evt) {
        evt.preventDefault();
        increaseQuantity($(this))
    });


    $('.linkRemove').on("click", function (evt) {
        evt.preventDefault();
        removeProduct($(this))
    });


})

function decreaseQuantity(link) {
    productId = link.attr("pid")
    quantityInput = $("#quantity" + productId)
    newQuantity = parseInt(quantityInput.val()) - 1;
    if (newQuantity > 0) {
        quantityInput.val(newQuantity)
        updateQuantity(productId, newQuantity)
    } else
        showWarningModal("Minimum quantity is 1")
}


function increaseQuantity(link) {
    productId = link.attr("pid")
    quantityInput = $("#quantity" + productId)
    newQuantity = parseInt(quantityInput.val()) + 1;
    if (newQuantity <= 5) {
        quantityInput.val(newQuantity)
        updateQuantity(productId, newQuantity)
    } else
        showWarningModal("Maximum quantity 5")
}


function updateQuantity(productId, quantity) {

    url = contextPath + "cart/update/" + productId + "/" + quantity;


    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        }
    }).done(function (UpdatedResult) {
        updateSubtotal(UpdatedResult, productId);
        updateTotal()
    }).fail(function () {
        showErrorModal("Error while updating product to shopping cart.");
    })
}

function updateTotal() {
    total = 0.0;
    test = 0.0;
    productCount = 0;
    $(".subtotal").each(function (index, element) {
        total += parseFloat(clearCurrencyFormat(element.innerHTML))
        ++productCount;
    });

    if (productCount < 1) {
        showEmptyShoppingCart();
    } else {
       // formatedTotal = $.number(total, 2, '.', ',')
        $("#total").text(formatCurrency(total));

    }


}
function showEmptyShoppingCart() {
    $("#sectionTotal").hide();
    $("#sectionEmptyCartMessage").removeClass("d-none");

}

function updateSubtotal(UpdatedResult, productId) {
   // formatedSubtotal = $.number(UpdatedResult, 2, '.', ',')
    $("#subtotal" + productId).text(formatCurrency(UpdatedResult))
}


function removeProduct(link) {
    url = link.attr("href")

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        }
    }).done(function (response) {
        rowNumber = link.attr("rowNumber")
        removeProductHTML(rowNumber)
        updateTotal()
        updateCountNumbers()
        showWarningModal("Shopping cart", response)

    }).fail(function () {
        showErrorModal("Error while removing product to shopping cart.");
    })
}


function removeProductHTML(rowNumber) {
    $("#row" + rowNumber).remove();
    $("#blankLine" + rowNumber).remove()
}

function updateCountNumbers() {
    $(".divCount").each(function (index, element) {
        element.innerHTML = "" + (index + 1)
    })
}


function formatCurrency(amount){
    return $.number(amount,decimalDigits,decimalSeparator,thousandsSeparator);
}


function clearCurrencyFormat(numberString){
    result = numberString.replaceAll(thousandsSeparator,"");
    return result.replaceAll(decimalSeparator,".")
}
