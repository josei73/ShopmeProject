dropdownBrands = $("#brand");
dropDownCategories = $("#category");


$(document).ready(function () {

    $("#shortDescription").richText();
    $("#fullDescription").richText();
    dropdownBrands.change(function () {
        dropDownCategories.empty()
        getCategories();

    });
    getCategoriesForNewForm();
})

function getCategoriesForNewForm(){
    catIdField = $("#categoryId");
    editMode = false;

    if(catIdField.length){
        editMode = true
    }

    if(!editMode) getCategories();
}


function checkUniqueProduct(form) {
    productId = $("#id").val();
    productName = $("#name").val()

    csrfValue = $("input[name='_csrf']").val(); // Ein token der wir brauchen wir mit SPRING SECURITY arbeiten und so nicht unseren Rest Controller
    params = {id: productId, name: productName, _csrf: csrfValue}

    $.post(checkUniqueURL, params, function (response) {
        if (response === "OK") {
            form.submit();
        } else if (response === "Duplicate") {
            showWarningModal("There is another product having the same name " + productName);
        } else {
            showErrorModal("Unknown response from server");
        }
    }).fail(function () {
        showErrorModal("Could not connect to Server ");
    });
    return false;
}

function getCategories() {
    brandId = dropdownBrands.val();
    url = brandModuleURl + "/" + brandId + "/categories";
    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropDownCategories);
        })
    })
}
