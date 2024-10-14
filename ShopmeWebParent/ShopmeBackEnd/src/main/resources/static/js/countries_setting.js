var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonDeleteCountry;
var buttonUpdateCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;


$(document).ready(function () {

    buttonLoad = $("#buttonLoadCountries");
    buttonDeleteCountry = $("#buttonDeleteCountry")
    buttonUpdateCountry = $("#buttonUpdateCountry")
    buttonAddCountry = $("#buttonAddCountry");
    dropDownCountry = $("#dropDownCountries")
    labelCountryName = $("#labelCountryName")
    fieldCountryName = $("#fieldCountryName")
    fieldCountryCode = $("#fieldCountryCode")

    buttonLoad.click(function () {
        console.log()
        loadCountries();
    });

    buttonAddCountry.click(function () {
        if (buttonAddCountry.val() == "Add")
            addCountry();
        else
            changeFormStatesToNew();
    })

    buttonUpdateCountry.click(function () {
        updateCountry()
    })

    buttonDeleteCountry.click(function () {
        deleteCountry();
    })

    dropDownCountry.on("change", function () {
        changeFormStatesToSelectedCountry();
    })
});

function deleteCountry() {
    optionValue = dropDownCountry.val()
    countryId = optionValue.split("-")[0];

    url = contextPath + "countries/delete/" + countryId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        }
    }).done(function () {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStatesToNew()
        showToastMessage("The Country have been deleted")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })
}

function updateCountry() {
    if (!validateFormCountry())
        return;

    url = contextPath + "countries/save";
    countryName = fieldCountryName.val()
    countryCode = fieldCountryCode.val()

    countryId = dropDownCountry.val().split("-")[0];
    jsonDate = {id: countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        },
        data: JSON.stringify(jsonDate),
        contentType: 'application/json'
    }).done(function (countryId) {
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode)
        $("#dropDownCountries option:selected").text(countryName)
        showToastMessage("The new country has been updated")
        changeFormStatesToNew();
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })

}

function validateFormCountry() {
    formCountry = document.getElementById("formCountry");
    if (!formCountry.checkValidity()) {
        formCountry.reportValidity()
        return false;
    }
    return true
}

function addCountry() {
    if (!validateFormCountry())
        return;

    url = contextPath + "countries/save";
    countryName = fieldCountryName.val()
    countryCode = fieldCountryCode.val()
    jsonDate = {name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        },
        data: JSON.stringify(jsonDate),
        contentType: 'application/json'
    }).done(function (countryId) {
        selectedNewAddedCountry(countryId, countryCode, countryName)
        showToastMessage("The new country has been added")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })
}

function selectedNewAddedCountry(countryId, countryCode, countryName) {
    optionValue = countryId + "-" + countryCode
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
    fieldCountryCode.val("")
    fieldCountryName.val("").focus();
}

function changeFormStatesToSelectedCountry() {
    buttonAddCountry.prop("value", "Add")
    buttonUpdateCountry.prop("disabled", false)
    buttonDeleteCountry.prop("disabled", false)


    selectedCountryName = $("#dropDownCountries option:selected").text()
    labelCountryName.text("Selected Country:")
    fieldCountryName.val(selectedCountryName);

    countryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode)

}

function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJson) {
        dropDownCountry.empty();
        $.each(responseJson, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
        })
    }).done(function () {
        buttonLoad.val("Refresh Country List")
        showToastMessage("All countries have been loaded")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}

function changeFormStatesToNew() {
    buttonAddCountry.val("Add")
    labelCountryName.text("Country Name:");
    fieldCountryCode.val("")
    fieldCountryName.val("").focus()
    buttonUpdateCountry.prop("disabled", true)
    buttonDeleteCountry.prop("disabled", true)

}


