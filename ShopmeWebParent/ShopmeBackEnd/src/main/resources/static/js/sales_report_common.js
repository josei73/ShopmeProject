var MILLISECONDS_A_DAYS = 24 * 60 * 60 * 1000


function setUpButtonEventHandlers(reportType, callbackFunction) {

    startDateField = document.getElementById("startDate" + reportType)
    endDateField = document.getElementById("endDate" + reportType);
    $(".button-sales-by"+reportType).on("click", function () {

        $(".button-sales-by" + reportType).each(function (e) {
            $(this).removeClass('btn-primary').addClass('btn-secondary')
        });
        $(this).removeClass('btn-secondary').addClass('btn-primary')
        period = $(this).attr("period");
        if (period) {
            callbackFunction(period)
            $("#divCustomDateRange" + reportType).addClass(' d-none')
        } else {
            $("#divCustomDateRange" + reportType).removeClass(' d-none')
        }

    })

    initCustomDateRange(reportType)

    $("#buttonViewReportByDateRange" + reportType).on("click", function (e) {
        validateDateRange(reportType,callbackFunction)
    })
}

function validateDateRange(reportType,callbackFunction) {
    startDateField = document.getElementById('startDate' + reportType)
    startDateField.setCustomValidity("")
    days = calculateDays(reportType)
    if (days >= 7 && days <= 30) {
        callbackFunction("custom")
    } else {
        startDateField.setCustomValidity("Dates must be in tha range of 7..30 days")
        startDateField.reportValidity()
    }
}


function calculateDays(reportType) {
    startDateField = document.getElementById('startDate' + reportType)
    endDateField = document.getElementById('endDate' + reportType)

    startDate = startDateField.valueAsDate;
    endDate = endDateField.valueAsDate;

    differenceInMilliSecond = endDate - startDate;

    return differenceInMilliSecond / MILLISECONDS_A_DAYS;
}

function initCustomDateRange(reportType) {
    startDateField = document.getElementById('startDate' + reportType)
    endDateField = document.getElementById('endDate' + reportType)

    toDate = new Date()
    endDate = new Date()

    endDateField.valueAsDate = toDate;

    fromDate = new Date()
    fromDate.setDate(toDate.getDate() - 30);
    startDateField.valueAsDate = fromDate;
}

function getChartTitle(period) {
    if (period === "last_7_days") return "Sales in Last 7 Days";
    if (period === "last_28_days") return "Sales in Last 28 Days";
    if (period === "last_6_months") return "Sales in Last 6 Months";
    if (period === "last_year") return "Sales in Last Year";
    if (period === "custom") return "Custom Date Range";


    return "";
}


function getDenominator(period, reportType) {
    if (period === "last_28_days") return 28;
    if (period === "last_6_months") return 6;
    if (period === "last_year") return 12;
    if (period === "custom") return calculateDays(reportType);


    return 7
}

function formatCurrency(amount) {
    formattedAmount = $.number(amount, decimalDigits, decimalPontType, thousandPointType)
    return prefixCurrencySymbol + formattedAmount + suffixCurrencySymbol
}

function setSalesAmount(period, reportType, labelTotalItems) {
    denominator = getDenominator(period, reportType)
    $("#textTotalGrossSales" + reportType).text(formatCurrency(totalGrossSales))
    $("#textTotalNetSales" + reportType).text(formatCurrency(totalNetSales))
    $("#textAvgGrossSales" + reportType).text(formatCurrency(totalGrossSales / denominator))
    $("#labelTotalItems" + reportType).text(labelTotalItems)
    $("#textAvgNetSales" + reportType).text(formatCurrency(totalNetSales / denominator))

    $("#textTotalItems" + reportType).text(totalItems)
}


function formatChartData(data, columnIndex1, columnIndex2) {
    var formatter = new google.visualization.NumberFormat({
        prefix: prefixCurrencySymbol,
        suffix: suffixCurrencySymbol,
        decimalSymbol: decimalPontType,
        groupingSymbol: thousandPointType,
        fractionDigits: decimalDigits

    });


    formatter.format(data, columnIndex1) // Gross Sales index
    formatter.format(data, columnIndex2) // Net Sales index

}
