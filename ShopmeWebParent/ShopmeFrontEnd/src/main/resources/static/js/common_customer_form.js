

var dropDownCountry;
var dataListState;
var fieldState;
$(document).ready(function () {
    dropDownCountry = $("#country");
    dataListState = $("#listStates");
    fieldState = $("#state");

    dropDownCountry.on("change", function () {
        loadStatesForCountry()
        fieldState.val("").focus()
    })
});

function loadStatesForCountry() {
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val()
    url = contextPath + "settings/list_states_by_country/" + countryId;

    $.get(url, function (responseJson) {
        dataListState.empty();
        $.each(responseJson, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dataListState);
        })
    }).fail(function () {
        alert("Failed to connect to the server")
    })


}

function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value != $('#password').val())
        confirmPassword.setCustomValidity("Password do not match!")
    else {
        confirmPassword.setCustomValidity("")
    }
}

