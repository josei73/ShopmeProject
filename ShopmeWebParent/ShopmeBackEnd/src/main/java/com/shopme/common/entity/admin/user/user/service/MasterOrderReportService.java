package com.shopme.common.entity.admin.user.user.service;

import com.shopme.common.entity.admin.user.user.report.AbstractReportService;
import com.shopme.common.entity.admin.user.user.report.ReportItem;
import com.shopme.common.entity.admin.user.user.report.ReportType;
import com.shopme.common.entity.admin.user.user.repository.OrderRepository;
import com.shopme.common.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MasterOrderReportService extends AbstractReportService {
    @Autowired
    private OrderRepository orderRepo;

    protected List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType) {
        System.out.println(startTime +"SSS");
        List<Order> orders = orderRepo.findByOrderTimeBetween(startTime, endTime);
        //  printRawData(orders);
        List<ReportItem> reportData = createReportData(startTime, endTime,reportType);
        System.out.println();
        calculateSalesForReportData(orders,reportData);
        printReportData(reportData);
        return reportData;

    }

    private void calculateSalesForReportData(List<Order> orders,List<ReportItem> reportItems){

        for (Order order : orders){
            String orderDateString = dateFormatter.format(order.getOrderTime());

            ReportItem reportItem = new ReportItem(orderDateString);

            int itemIndex = reportItems.indexOf(reportItem);

            if(itemIndex >=0){
                reportItem = reportItems.get(itemIndex);
                reportItem.addGrossSales(order.getTotal());
                reportItem.addNetSales(order.getSubtotal()- order.getProductCost());
                reportItem.increaseOrderCount();

            }
        }

    }

    private List<ReportItem> createReportData(Date startTime, Date endTime,ReportType reportType) {
        List<ReportItem> reportItems = new ArrayList<>();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startTime);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endTime);

        Date currentDate = startDate.getTime();
        String dateString = dateFormatter.format(currentDate);

        reportItems.add(new ReportItem(dateString));

        do {
            if(reportType.equals(ReportType.DAY)){
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            } else if(reportType.equals(ReportType.MONTH)){
                startDate.add(Calendar.MONTH, 1);
            }

            currentDate = startDate.getTime();
            dateString = dateFormatter.format(currentDate);
            reportItems.add(new ReportItem(dateString));

        } while (startDate.before(endDate));


        return reportItems;
    }

    private void printReportData(List<ReportItem> reportData) {
        reportData.forEach(reportItem -> {
            System.out.printf("%s, %10.2f , %10.2f ,%d \n", reportItem.getIdentifier(),reportItem.getGrossSales()
                    ,reportItem.getNetSales(),reportItem.getOrdersCount());
        });
    }











}
