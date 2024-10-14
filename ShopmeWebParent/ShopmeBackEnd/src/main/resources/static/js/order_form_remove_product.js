$(document).ready(function (){
    // Dieser ausdruck heißt suche in productList nach der klasse linkRemove wo geklickt wurde.
    $("#productList").on("click",".linkRemove",function (e){
        e.preventDefault();

         if(doesOrderHaveOnlyOneProduct()){
             showWarningModal("Could not remove the product. The order musst have at least one product")
         }
         else {
             removeProduct($(this));
             updateOrderAmounts();
         }
    })
})

function removeProduct(link){
    rowNumber = link.attr("rowNumber")
    $("#row"+rowNumber).remove()
    $("blankLine"+rowNumber).remove()


    $(".divCount").each(function(index,element){
        element.innerHTML = "" +(index+1)
    })
}


function doesOrderHaveOnlyOneProduct(){
    productCount = $(".hiddenProductId").length;

    return productCount == 1;
}