package com.shopme.common.entity.admin.user.user.service;

import com.shopme.common.entity.admin.user.user.report.AbstractReportService;
import com.shopme.common.entity.admin.user.user.report.ReportItem;
import com.shopme.common.entity.admin.user.user.report.ReportType;
import com.shopme.common.entity.admin.user.user.repository.OrderDetailRepository;
import com.shopme.common.entity.order.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailReportService extends AbstractReportService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    protected List<ReportItem> getReportDataByDateRangeInternal(Date startDate, Date endDate, ReportType reportType) {
        List<OrderDetail> details = null;
        if (reportType.equals(ReportType.CATEGORY)) {
            details = orderDetailRepository.findWithCategoryAndTimeBetween(startDate, endDate);
        } else if (reportType.equals(ReportType.PRODUCT)) {
            details = orderDetailRepository.findWithProductAndTimeBetween(startDate, endDate);
        }

      //  printRawData(details);

        List<ReportItem> reportItemList = new ArrayList<>();

        details.forEach(detail -> {
            String identifier = "";
            if (reportType.equals(ReportType.CATEGORY)) {
                identifier = detail.getProduct().getCategory().getName();
            } else if (reportType.equals(ReportType.PRODUCT)) {
                identifier = detail.getProduct().getShortName();
            }
            ReportItem reportItem = new ReportItem(identifier);

            float grossSales = detail.getSubtotal() + detail.getShippingCost();
            float netSales = detail.getSubtotal() - detail.getProductCost();

            int itemIndex = reportItemList.indexOf(reportItem);

            if (itemIndex >= 0) {
                reportItem = reportItemList.get(itemIndex);
                reportItem.addGrossSales(grossSales);
                reportItem.addNetSales(netSales);
                reportItem.increaseProductsCount(detail.getQuantity());

            } else {
                reportItemList.add(new ReportItem(identifier, grossSales, netSales, detail.getQuantity()));
            }
        });

      //  printReportData(reportItemList);
        return reportItemList;
    }

    private void printReportData(List<ReportItem> reportItemList) {
        for (ReportItem reportItem :
                reportItemList) {
            System.out.printf("%-20s, %10.2f, %10.2f, %d \n",
                    reportItem.getIdentifier(), reportItem.getGrossSales(), reportItem.getNetSales(),
                    reportItem.getProductsCount());
        }
    }

    private void printRawData(List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            System.out.printf("%d, %-20s, %10.2f, %10.2f, %10.2f \n",
                    detail.getQuantity(), detail.getProduct().getShortName().substring(0,20), detail.getSubtotal(),
                    detail.getProductCost(), detail.getShippingCost());
        }
    }
}
