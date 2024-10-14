var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;

$(document).ready(function () {
    fieldProductCost = $("#productCost")
    fieldSubtotal = $("#subtotal")
    fieldShippingCost = $("#shippingCost")
    fieldTax = $("#tax")
    fieldTotal = $("#total")

    formatOrderAmount()
    formatProductAmount()

    $("#productList").on("change", ".quantity-input", function (e) {
        updateSubtotalWhenQuantityChanged($(this))
        updateOrderAmounts();
    });

    $("#productList").on("change", ".price-input", function (e) {
        updateSubtotalWhenPriceChanged($(this))
        updateOrderAmounts();
    })

    $("#productList").on("change", ".cost-input", function (e) {
        updateOrderAmounts();
    })

    $("#productList").on("change", ".ship-input", function (e) {
        updateOrderAmounts();
    })

})

function updateOrderAmounts() {
    totalCost = 0.0;

    $(".cost-input").each(function () {
        costInputField = $(this)
        rowNumber = costInputField.attr("rowNumber")
        quantityValue = $("#quantity" + rowNumber).val()

        productCost = getNumberValueRemoveThousandSeparator(costInputField)
        productCost = getNumberValueFormatDecimalSeparator(productCost)
        totalCost += productCost * parseInt(quantityValue)


    })

    setAndFormatNumberForField("productCost", totalCost)

    orderSubtotal = 0.0;

    $(".subtotal-output").each(function (e) {
        productSubtotal = getNumberValueRemoveThousandSeparator($(this))
        productSubtotal = getNumberValueFormatDecimalSeparator(productSubtotal)

        orderSubtotal += productSubtotal;
    })

    setAndFormatNumberForField("subtotal", orderSubtotal)

    shippingCost = 0.0;

    $(".ship-input").each(function (e) {
        productShip = getNumberValueRemoveThousandSeparator($(this))
        productShip = getNumberValueFormatDecimalSeparator(productShip)

        shippingCost += productShip;
    })
    setAndFormatNumberForField("shippingCost", shippingCost)

    tax = getNumberValueRemoveThousandSeparator(fieldTax)
    tax = getNumberValueFormatDecimalSeparator(tax)

    orderTotal = orderSubtotal + tax + shippingCost
    setAndFormatNumberForField("total", orderTotal)
}

function setAndFormatNumberForField(fieldId, fieldValue) {
    formattedValue = $.number(fieldValue, 2);
    $("#" + fieldId).val(formattedValue);
}

function updateSubtotalWhenPriceChanged(input) {
    priceValue = getNumberValueRemoveThousandSeparator(input)
    rowNumber = input.attr("rowNumber")
    quantityField = $("#quantity" + rowNumber)
    priceValue = getNumberValueFormatDecimalSeparator(priceValue)
    quantityValue = quantityField.val();
    newSubtotal = parseFloat(quantityValue) * priceValue;

    setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal)

}

function getNumberValueRemoveThousandSeparator(fieldRef) {
    fieldValue = fieldRef.val().replace(".", "");
    return fieldValue;

}

function getNumberValueFormatDecimalSeparator(value) {
    fieldValue = parseFloat(value.replace(",", "."))

    return fieldValue;

}

function updateSubtotalWhenQuantityChanged(input) {
    quantityValue = input.val();
    rowNumber = input.attr("rowNumber")
    priceField = $("#price" + rowNumber)

    euroPrice = getNumberValueRemoveThousandSeparator(priceField)
    priceValue = getNumberValueFormatDecimalSeparator(euroPrice)
    newSubtotal = parseFloat(quantityValue) * priceValue;

    setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal)
}

function formatProductAmount() {
    $(".cost-input").each(function (e) {
        formatNumberForField($(this))
    });

    $(".price-input").each(function (e) {
        formatNumberForField($(this))
    });

    $(".ship-input").each(function (e) {
        formatNumberForField($(this))
    });

    $(".subtotal-output").each(function (e) {
        formatNumberForField($(this))
    });
}

function formatOrderAmount() {
    formatNumberForField(fieldProductCost)
    formatNumberForField(fieldSubtotal)
    formatNumberForField(fieldTax)
    formatNumberForField(fieldTotal)
    formatNumberForField(fieldShippingCost)

}

function formatNumberForField(fieldRef) {
    fieldRef.val($.number(fieldRef.val(), 2));
}

function processFormBeforeSubmit() {
    setCountryName()
    removeThousandAndDecimalSeparator(fieldProductCost)
    removeThousandAndDecimalSeparator(fieldSubtotal)
    removeThousandAndDecimalSeparator(fieldShippingCost)
    removeThousandAndDecimalSeparator(fieldTax)
    removeThousandAndDecimalSeparator(fieldTotal)


    $(".cost-input").each(function (e) {
        removeThousandAndDecimalSeparator($(this))
    })

    $(".price-input").each(function (e) {
        removeThousandAndDecimalSeparator($(this))
    })

    $(".ship-input").each(function (e) {
        removeThousandAndDecimalSeparator($(this))
    })

    $(".subtotal-output").each(function (e) {
        removeThousandAndDecimalSeparator($(this))
    })


}


function removeThousandAndDecimalSeparator(fieldRef) {
    value = fieldRef.val().toString()
    value = value.replace(".","");
    value = value.replace(",",".");

    fieldRef.val(value);


}

function setCountryName() {
    selectedCountry = $("#country option:selected")
    countryName = selectedCountry.text()
    $("#countryName").val(countryName)


}