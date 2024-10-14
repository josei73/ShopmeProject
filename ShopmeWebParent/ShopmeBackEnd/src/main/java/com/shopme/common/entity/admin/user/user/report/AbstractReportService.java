package com.shopme.common.entity.admin.user.user.report;

import com.shopme.common.entity.order.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService {
   protected DateFormat dateFormatter;

    public  List<ReportItem> getReportDataLast7Days( ReportType reportType) {
        return getReportDataLastXDays(7,reportType);
    }

    public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
        return getReportDataLastXMonths(6,reportType);
    }


    public List<ReportItem> getReportDataLast28Days(ReportType reportType) {
        return getReportDataLastXDays(28,reportType);
    }

    public List<ReportItem> getReportDataLastYear(ReportType reportType) {
        return getReportDataLastXMonths(12,reportType);
    }


    protected List<ReportItem> getReportDataLastXDays(int days,ReportType reportType) {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
        Date startTime = cal.getTime();

        System.out.println("START TIME: " + startTime);
        System.out.println("END TIME: " + endTime);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDataByDateRangeInternal(startTime, endTime,reportType);
    }

    protected List<ReportItem> getReportDataLastXMonths(int days,ReportType reportType) {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -(days - 1));
        Date startTime = cal.getTime();

        System.out.println("START TIME: " + startTime);
        System.out.println("END TIME: " + endTime);

        dateFormatter = new SimpleDateFormat("yyyy-MM");
        return getReportDataByDateRangeInternal(startTime, endTime,reportType);
    }


    protected abstract List<ReportItem> getReportDataByDateRangeInternal(Date startDate,Date endDate,ReportType reportType);


    public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime,ReportType reportType) {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDataByDateRangeInternal(startTime,endTime,reportType);

    }



}
