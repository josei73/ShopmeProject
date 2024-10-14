function clearFilter() {
    window.location = moduleURL;
}


function showDeleteConfirmModal(link, entityName){
    /*
          JQuery selectors:
              Element mit der ID finden : $("#id")
              Element mit CSS finden : $(".class-name")
          Attribute setzen
          element.attr("name","value")

          Wenn Events von input Felder handlen möchte
          input.on("event-name",function(e){})

           */
    entityId = link.attr("entityId")
    $("#yesButton").attr("href", link.attr("href")) // Wir setzen den für unseren Controller
    $("#confirmText").text("Are your sure you want to delete this "+ entityName+ " ID "+entityId+ "?")
    $("#confirmModal").modal()
}
