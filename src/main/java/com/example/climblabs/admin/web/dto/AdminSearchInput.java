package com.example.climblabs.admin.web.dto;

import com.example.climblabs.global.utils.common.SearchType;
import lombok.Data;

@Data
public class AdminSearchInput {
    private SearchType searchType;
    private String searchValue;
}
