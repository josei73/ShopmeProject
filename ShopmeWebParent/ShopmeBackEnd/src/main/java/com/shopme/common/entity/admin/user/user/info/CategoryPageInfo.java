package com.shopme.common.entity.admin.user.user.info;

public class CategoryPageInfo {
    private int totalPages;
    private Long totalElements;

    public CategoryPageInfo(int totalPages, Long totalElement) {
        this.totalPages = totalPages;
        this.totalElements = totalElement;
    }

    public CategoryPageInfo() {
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
