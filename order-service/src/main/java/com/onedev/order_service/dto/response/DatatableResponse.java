package com.onedev.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DatatableResponse<T> {
    private List<T> data;
    private long recordsTotal;
    private long recordsFiltered;
    private int page;
    private int limit;
}