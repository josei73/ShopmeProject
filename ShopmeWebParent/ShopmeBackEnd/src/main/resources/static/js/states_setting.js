var buttonLoad4State;
var dropDownCountry4State;
var buttonAddState;
var dropDownStates
var buttonDeleteState;
var buttonUpdateState;
var labelStateName;
var fieldStateName;


$(document).ready(function () {
    buttonLoad4State = $("#buttonLoadCountriesForStates");
    buttonDeleteState = $("#buttonDeleteState")
    buttonUpdateState = $("#buttonUpdateState")
    buttonAddState = $("#buttonAddState");
    dropDownStates = $("#dropDownStates")
    dropDownCountry4State = $("#dropDownCountriesForStates")
    labelStateName = $("#labelStateName")
    fieldStateName = $("#fieldStateName")


    buttonLoad4State.click(function () {
        loadCountries4State();
    });

    dropDownCountry4State.on("change", function () {
        loadStates4Country()
    })

    buttonAddState.click(function () {
        if (buttonAddState.val() == "Add")
            addState();
        else
            changeFormStateToNew();
    })

    buttonUpdateState.click(function () {
        updateState()
    })

    buttonDeleteState.click(function () {
        deleteState();
    })

    dropDownStates.on("change", function () {
        changeFormStatesToSelectedState();
    })
});

function deleteState() {
    stateId = dropDownStates.val()


    url = contextPath + "states/delete/" + stateId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        }
    }).done(function () {
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeFormStateToNew()
        showToastMessage("The State have been deleted")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })
}

function updateState() {
    if(!validateFormState()) return;
    url = contextPath + "states/save";
    stateId = dropDownStates.val()
    countryId = selectedCountry.val()
    countryName = selectedCountry.text()
    stateName = fieldStateName.val()


    jsonDate = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        },
        data: JSON.stringify(jsonDate),
        contentType: 'application/json'
    }).done(function (stateId) {
        $("#dropDownStates option:selected").text(stateName)
        showToastMessage("The new state has been updated")
        changeFormStateToNew();
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })

}

function validateFormState() {
    formState = document.getElementById("formState");
    if (!formState.checkValidity()) {
        formState.reportValidity()
        return false;
    }
    return true
}

function addState() {

    if(!validateFormState()) return;

    url = contextPath + "states/save";
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val()
    countryName = selectedCountry.text()
    stateName = fieldStateName.val()


    jsonDate = {name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        },
        data: JSON.stringify(jsonDate),
        contentType: 'application/json'
    }).done(function (stateId) {
        selectedNewAddedState(stateId, stateName)
        showToastMessage("The new state has been added")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })
}

function selectedNewAddedState(stateId, stateName) {
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

    $("#dropDownStates option[value='" + stateId + "']").prop("selected", true);
    fieldStateName.val("").focus();
}

function loadCountries4State() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJson) {
        dropDownCountry4State.empty();
        $.each(responseJson, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountry4State);
        })
    }).done(function () {
        buttonLoad.val("Refresh Country List")
        showToastMessage("All countries have been loaded")
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })

}

function loadStates4Country() {

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val()

    url = contextPath + "states/list_by_country/" + countryId;

    $.get(url, function (responseJson) {
        dropDownStates.empty();
        $.each(responseJson, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        })
    }).done(function () {
        changeFormStateToNew();
        showToastMessage("All states have been loaded for country " + selectedCountry.text())
    }).fail(function () {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    })
}

function changeFormStatesToSelectedState() {
    buttonAddState.prop("value", "Add")
    buttonUpdateState.prop("disabled", false)
    buttonDeleteState.prop("disabled", false)


    selectedStaeteName = $("#dropDownStates option:selected").text()
    labelStateName.text("Selected State:")
    fieldStateName.val(selectedStaeteName);


}


function changeFormStateToNew() {
    buttonAddState.val("Add")
    labelStateName.text("State/Province Name:")
    buttonUpdateState.prop("disabled", true)
    buttonDeleteState.prop("disabled", true)
    fieldStateName.val("").focus();

}