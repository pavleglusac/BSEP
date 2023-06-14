package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.Log;

import java.util.List;

public class LogSearchResultDto {
    private List<Log> items;
    private long totalItems;

    public LogSearchResultDto(List<Log> items, long totalItems) {
        this.items = items;
        this.totalItems = totalItems;
    }

    public List<Log> getItems() {
        return items;
    }

    public long getTotalItems() {
        return totalItems;
    }
}
