
$(document).ready(function () {
    $("a[id='linkRemoveDetail']").each(function (index){
        $(this).click(function (){
            removeExtraDetailSectionByID(index)
        })
    })


})



function addNextDetailsSection() {
    allDivDeatils = $("[id^='divDetail']");
    divDetailsCount = allDivDeatils.length;


    htmlExtraDetails = `
        
         <div class="form-inline" id="divDetail${divDetailsCount}">
           <input type="hidden" name="detailIDs" value="0"/>
        <label class="m-3">Name:</label>
        <input type="text" class="form-control w-25" name="detailNames" max="255"/>
        <label class="m-3">Value:</label>
        <input type="text" class="form-control w-25" name="detailValues" max="255"/>
    </div>`;

    previousDetailsDiv = allDivDeatils.last();
    previousDetailsDivID = previousDetailsDiv.attr("id");

    htmlLinkRemove = `<a class="btn fas fa-times-circle fa-2x icon-dark float-right" 
        href="javascript:removeExtraDetailByID('${previousDetailsDivID}')"
        title="Remove this Details"></a>`;

    $("#divProductDetails").append(htmlExtraDetails);

    $("input[name='detailNames']").last().focus();

    previousDetailsDiv.append(htmlLinkRemove)
}

function removeExtraDetailByID(id) {
    $("#"+ id).remove();
}

function removeExtraDetailSectionByID(index) {
    $("#divDetail"+index).remove();

}