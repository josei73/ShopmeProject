var data;
var chartOptions;
var totalGrossSales;
var totalNetSales;
var totalItems;





$(document).ready(function () {
   setUpButtonEventHandlers("_date",loadSalesReportByDate)
})



function prepareChartDataForSalesReportByDate(responseJson) {
    data = new google.visualization.DataTable()
    data.addColumn('string', 'Date')
    data.addColumn('number', 'Gross Sales')
    data.addColumn('number', 'Net Sales')
    data.addColumn('number', 'Orders')

    totalGrossSales = 0.0;
    totalItems = 0;
    totalNetSales = 0.0;

    $.each(responseJson, function (index, reportItem) {
        data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.ordersCount]])
        totalGrossSales += parseFloat(reportItem.grossSales)
        totalNetSales += parseFloat(reportItem.netSales)
        totalItems += parseFloat(reportItem.ordersCount)

    })

}

function customizeChartForSalesReportByDate(period) {
    chartOptions = {
        title: getChartTitle(period),
        'height': 360,
        legend: {position: 'top'},

        series: {
            0: {targetAxisIndex: 0},
            1: {targetAxisIndex: 0},
            2: {targetAxisIndex: 1},

        },

        vAxes: {
            0: {title: 'Sales Amount', format: 'currency'},
            1: {title: 'Number of Orders'}
        }
    };



}

function drawChartForSalesReportByDate() {
    var salesChart = new google.visualization.ColumnChart(document.getElementById("chart_sales_by_date"));
    salesChart.draw(data, chartOptions)

}


function loadSalesReportByDate(period) {

    if (period === "custom") {
        startDate = $("#startDate_date").val()
        console.log(startDate)
        endDate = $("#endDate_date").val()
        console.log(endDate)
        reqeustURL = contextPath + "reports/sales_by_date/" + startDate + "/" + endDate;
    } else {
        reqeustURL = contextPath + "reports/sales_by_date/" + period;
    }

    $.get(reqeustURL, function (response) {
        prepareChartDataForSalesReportByDate(response)
        customizeChartForSalesReportByDate(period)
        formatChartData(data,1,2)
        drawChartForSalesReportByDate(period)
        setSalesAmount(period,"_date","Total Orders")
    })
}

