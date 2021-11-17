package com.example.climblabs.admin.web.dto;

import com.example.climblabs.global.utils.pagination.Criteria;
import com.example.climblabs.global.utils.pagination.PaginationInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonRequestDto extends Criteria {

    private PaginationInfo paginationInfo;
}
