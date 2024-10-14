var data;
var chartOptions;


$(document).ready(function () {
    setUpButtonEventHandlers("_product", loadSalesReportByDateForProduct)
})


function loadSalesReportByDateForProduct(period) {


    if (period === "custom") {
        startDate = $("#startDate_product").val()
        endDate = $("#endDate_product").val()
        reqeustURL = contextPath + "reports/product/" + startDate + "/" + endDate;
    } else {
        reqeustURL = contextPath + "reports/product/" + period;
    }

    $.get(reqeustURL, function (response) {
        prepareChartDataForSalesReportByProduct(response)
        customizeChartForSalesReportByProduct()
        formatChartData(data, 2, 3)
        drawChartForSalesReportByProduct(period)
        setSalesAmount(period, "_product", "Total Products")
    })
}


function prepareChartDataForSalesReportByProduct(responseJson) {
    data = new google.visualization.DataTable()
    data.addColumn('string', 'Product')
    data.addColumn('number', 'Quantity')
    data.addColumn('number', 'Gross Sales')
    data.addColumn('number', 'Net Sales')


    totalGrossSales = 0.0;
    totalItems = 0;
    totalNetSales = 0.0;

    $.each(responseJson, function (index, reportItem) {
        data.addRows([[reportItem.identifier,reportItem.productsCount, reportItem.grossSales, reportItem.netSales,]])
        totalGrossSales += parseFloat(reportItem.grossSales)
        totalNetSales += parseFloat(reportItem.netSales)
        totalItems += parseFloat(reportItem.productsCount)

    })

}

function customizeChartForSalesReportByProduct() {
    chartOptions = {
        'height': 360, width: '80%',
        showRowNumber: true,
        page: 'enable',
        sortColumn: 2,
        sortAscending: false
    };


}


function drawChartForSalesReportByProduct() {
    var salesChart = new google.visualization.Table(document.getElementById("chart_sales_by_product"));
    salesChart.draw(data, chartOptions)

}
