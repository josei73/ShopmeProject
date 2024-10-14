var data;
var chartOptions;



$(document).ready(function () {
    setUpButtonEventHandlers("_category", loadSalesReportByDateForCategory)
})


function loadSalesReportByDateForCategory(period) {



    if (period === "custom") {
        startDate = $("#startDate_category").val()

        endDate = $("#endDate_category").val()
        reqeustURL = contextPath + "reports/category/" + startDate + "/" + endDate;
    } else {
        reqeustURL = contextPath + "reports/category/" + period;
    }

    $.get(reqeustURL, function (response) {
        prepareChartDataForSalesReportByCategory(response)
        customizeChartForSalesReportByCategory()
        formatChartData(data, 1, 2)
        drawChartForSalesReportByCategory(period)
        setSalesAmount(period, "_category", "Total Products")
    })
}


function prepareChartDataForSalesReportByCategory(responseJson) {
    data = new google.visualization.DataTable()
    data.addColumn('string', 'Category')
    data.addColumn('number', 'Gross Sales')
    data.addColumn('number', 'Net Sales')



    totalGrossSales = 0.0;
    totalItems = 0;
    totalNetSales = 0.0;

    $.each(responseJson, function (index, reportItem) {
        data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales]])
        totalGrossSales += parseFloat(reportItem.grossSales)
        totalNetSales += parseFloat(reportItem.netSales)
        totalItems += parseFloat(reportItem.productsCount)

    })

}

function customizeChartForSalesReportByCategory() {
    chartOptions = {
        'height': 360, legend: {position: 'right'}
    };


}


function drawChartForSalesReportByCategory() {
    var salesChart = new google.visualization.PieChart(document.getElementById("chart_sales_by_category"));
    salesChart.draw(data, chartOptions)

}
