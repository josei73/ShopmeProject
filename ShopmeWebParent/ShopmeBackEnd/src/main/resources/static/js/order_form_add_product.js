
$(document).ready(function () {
    productDetailCount = $(".hiddenProductId").length;
    $("#products").on("click", "#linkAddProduct", function (e) {
        e.preventDefault();

        link = $(this)
        url = link.attr("href") // Add Button aus order_form_product

        $("#addProductModal").on("shown.bs.modal", function () {
            $(this).find("iframe").attr("src", url)
        })

        $("#addProductModal").modal()
    })
})

function addProduct(productId, productName) {
    getShippingCost(productId)
}

function genrateProductCode(productId, mainImagePath, productName, productCost, shippingCost, productPrice) {
    nextCount = productDetailCount +1;
    productDetailCount++;
    quantityId = "quantity" + nextCount;
    priceId = "price" + nextCount
    subtotalId = "subtotal" + nextCount
    rowId = "row" + nextCount;

    blankLineId = "blankLine" + nextCount;


    htmlCode = ` <div class="border rounded p-1" id="${rowId}">
  <input type="hidden" name="detailId" id="detailId" value="0"/>
                <input type="hidden" name="productId" value="${productId}" class="hiddenProductId"/>
                <div class="row">
                    <div class="col-1">
                        <div class="divCount">${nextCount}</div>
                          <div><a class="fas fa-trash icon-dark linkRemove" rowNumber="${nextCount}" href="" ></a> </div>
                    </div>
                    <div class="col-3">
                        <img src="${mainImagePath}" class="img-fluid"/>
                    </div>
                </div>

                <div class="row m-2">
                    <b>${productName}</b>
                </div>

                <div class="row m-2">
                    <table>
                        <tr>
                            <td>Product Cost:</td>
                            <td>
                                <input type="text" class="form-control m-1 cost-input"
                                       rowNumber="${nextCount}"
                                       name="productDetailCost"
                                       value="${productCost}" style="max-width: 140px" required/>
                            </td>
                        </tr>

                        <tr>
                            <td>Quantity:</td>
                            <td>
                                <input type="number" step="1" min="1" max="5" class="form-control m-1 quantity-input"
                                       id="${quantityId}"
                                       rowNumber="${nextCount}"
                                       name="quantity"
                                       value="1" style="max-width: 140px" required/>
                            </td>
                        </tr>

                        <tr>
                            <td>Unit Price:</td>
                            <td>
                                <input type="text" class="form-control m-1 price-input"
                                       id="${priceId}"
                                       rowNumber="${nextCount}"
                                       name="productPrice"
                                       value="${productPrice}" style="max-width: 140px" required/>
                            </td>
                        </tr>

                        <tr>
                            <td>Subtotal </td>
                            <td>
                                <input type="text" class="form-control m-1 subtotal-output"
                                       id="${subtotalId}"
                                       name="productSubtotal"
                                       value="${productPrice}" readonly style="max-width: 140px" required/>
                            </td>
                        </tr>

                        <tr>
                            <td>Shipping Cost:</td>
                            <td>
                                <input type="text" class="form-control m-1 ship-input"
                                name="productShippingCost"
                                       value="${shippingCost}" style="max-width: 140px" required/>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="${blankLineId}" class="row">&nbsp;</div>
    
    `;

    return htmlCode;
}

function getProductInfo(productId, shippingCost) {
    shippingCost = shippingCost.replace(".", ",")
    url = contextPath + "products/get/" + productId;
    $.get(url, function (productJson) {
        mainImagePath = contextPath.substring(0, contextPath.length - 1) + productJson.imagePath
        productName = productJson.name
        productCost = $.number(productJson.cost, 2)
        productPrice = $.number(productJson.price, 2)


        htmlCode = genrateProductCode(productId, mainImagePath, productName, productCost, shippingCost, productPrice)

        $("#productList").append(htmlCode);
        updateOrderAmounts();

    }).fail(function (err) {
        showWarningModal(err.responseJSON.message);
    })
}

function getShippingCost(productId) {
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val()
    state = $("#state").val();
    if (state.length == 0) {
        state = $("#city").val()
    }

    url = contextPath + "get_shipping_cost";

    jsonDate = {countryId: countryId, productId: productId, state: state};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        },
        data: jsonDate,
    }).done(function (shippingCost) {
        getProductInfo(productId, shippingCost)
    }).fail(function (err) {
        showWarningModal(err.responseJSON.message);
        shippingCost = 0.0;
        getProductInfo(productId, shippingCost)
    }).always(function () {
        $("#addProductModal").modal("hide");
    })
}

function isProductAlreadyAdded(productId) {
    productExist = false;
    $(".hiddenProductId").each(function (e) {
        aProductId = $(this).val()
        if (productId == aProductId) {
            productExist = true;
            return true;
        }
    })
    return productExist;

}


