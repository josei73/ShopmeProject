var trackRecordCount
$(document).ready(function () {
    trackRecordCount = $(".hiddenTrackId").length;
    $("#trackList").on("click", ".linkRemoveTrack", function (e) {
        e.preventDefault();
        deleteTrack($(this))
        updateTrackCountNumbers();
    });

    $("#track").on("click", "#linkAddTracK", function (e) {
        e.preventDefault();
        addNewTrackRecord();
    });

    $("#trackList").on("change", ".dropDownStatus", function (e) {
        dropDownList = $(this);
        rowNumber = dropDownList.attr("rowNumber")
        selectedOption = $("option:selected", dropDownList);

        defaultNote = selectedOption.attr("defaultDescription")
        $("#trackNote" + rowNumber).text(defaultNote)
    })
})

function deleteTrack(link) {
    rowNumber = link.attr('rowNumber')
    $("#rowTrack"+rowNumber).remove()
    $("#emptyLine"+rowNumber).remove()
}

function updateTrackCountNumbers(){
    $(".divCountTrack").each(function (index,element){
        element.innerHTML=""+ (index+1)
    })
}

function addNewTrackRecord() {
    htmlCode = generateTrackCode()
    $("#trackList").append(htmlCode)
}

function generateTrackCode() {
    nextCount = trackRecordCount + 1;
    ++trackRecordCount;
    trackNoteId = "trackNote" + nextCount
    rowId = "row" + nextCount;
    emptyLineId = "emptyLine" + nextCount;

    currentDateTime = formatCurrentDateTime();


    htmlCode = ` 
                  <div class="border row rounded p-1" id= "${rowId}">
                <input type="hidden" name="trackId" value="0" class="hiddenTrackId"/>
                <div class="col-2">
                    <div class="divCountTrack">${nextCount}</div>
           
                        <div class="mt-1"><a class="fas fa-trash icon-dark linkRemoveTrack" rowNumber="${nextCount}"
                                href=""></a>
                        </div>
           
                </div>

                <div class="col-10">
                    <div class="form-group row">
                        <label class="col-form-label">Time: </label>
                        <div class="col">

                            <input type="datetime-local" class="form-control" value="${currentDateTime}"
                                    name="trackDate"
                                   required
                                   style="max-width: 300px"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-form-label">Status: </label>
                        <div class="col">
                   
                            <select name="trackStatus" class="form-control dropDownStatus"
                                    required style="max-width: 150px"
                  
                                    rowNumber="${nextCount}">

                                `;
             htmlCode += $("#trackStatusOptions").clone().html();
             htmlCode += `
                            </select>
                        </div>
                    </div>


                    <div class="form-group row">
                        <label class="col-form-label">Notes: </label>
                        <div class="col">
                            <textarea rows="2" cols="10" style="max-width: 300px" class="form-control"
                                      id="${trackNoteId}"
                                      name="trackNotes"
                                      required>
                            </textarea>
                        </div>
                    </div>
                </div>


            </div>
            <div id="+${emptyLineId}" class="row">&nbsp;</div>  
    `;

    return htmlCode;
}
function formatCurrentDateTime(){
    date = new Date()
    year = date.getFullYear();
    month = date.getMonth()+1
    day = date.getDate();
    hour = date.getHours()
    minute = date.getMinutes();
    second = date.getSeconds();

    if(month < 10 ) month = "0" + month;
    if(day < 10 ) day = "0"+day;
    if(hour < 10 ) hour = "0"+hour;
    if(minute < 10 ) minute = "0"+minute;
    if(second < 10 ) second = "0"+second;


    return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second;
}

