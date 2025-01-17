package com.shopme.common.entity.admin.user.user.report;

import java.util.Objects;

public class ReportItem {
    // IST DAS DATUM WANN BESTELLT WURDE
    private String identifier;
    private float grossSales;
    private float netSales;
    private int ordersCount;
    private int productsCount;

    public ReportItem() {
    }

    public ReportItem(String identifier, float grossSales, float netSales, int productsCount) {
        this.identifier = identifier;
        this.grossSales = grossSales;
        this.netSales = netSales;
        this.productsCount = productsCount;
    }

    public ReportItem(String identifier, float grossSales, float netSales) {
        this.identifier = identifier;
        this.grossSales = grossSales;
        this.netSales = netSales;
    }

    public ReportItem(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public float getGrossSales() {
        return grossSales;
    }

    public void setGrossSales(float grossSales) {
        this.grossSales = grossSales;
    }

    public float getNetSales() {
        return netSales;
    }

    public void setNetSales(float netSales) {
        this.netSales = netSales;
    }

    public int getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(int ordersCount) {
        this.ordersCount = ordersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportItem that = (ReportItem) o;
        return Objects.equals(identifier, that.identifier);
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    public void addGrossSales(float amount) {
        this.grossSales += amount;

    }

    public void addNetSales(float amount) {
        this.netSales += amount;
    }

    public void increaseOrderCount() {
        ++ordersCount;
    }

    public void increaseProductsCount(int count) {
        productsCount+=count;
    }
}
