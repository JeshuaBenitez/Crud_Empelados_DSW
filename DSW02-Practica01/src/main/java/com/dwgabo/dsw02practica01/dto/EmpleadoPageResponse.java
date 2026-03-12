package com.dwgabo.dsw02practica01.dto;

import java.util.List;

public class EmpleadoPageResponse {

    private List<EmpleadoResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public List<EmpleadoResponse> getContent() {
        return content;
    }

    public void setContent(List<EmpleadoResponse> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}