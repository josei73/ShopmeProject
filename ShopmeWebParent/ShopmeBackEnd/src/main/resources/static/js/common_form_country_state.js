var dropDownCountries
var dropDownStates

$(document).ready(function (){
    dropDownCountries = $("#country")
    dropDownStates = $("#listStates")


    dropDownCountries.on("change",function (){
        loadSt4Country();
        console.log("hier man ")
        $("#state").val("").focus();
    });

    loadSt4Country()


})

function loadSt4Country() {

    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val()

    url = contextPath+ "states/list_by_country/" + countryId;

    $.get(url, function (responseJson) {
        dropDownStates.empty();
        $.each(responseJson, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dropDownStates);
        })
    }).fail(function () {
        showErrorModal("ERROR: Could not load states/province");
    })
}