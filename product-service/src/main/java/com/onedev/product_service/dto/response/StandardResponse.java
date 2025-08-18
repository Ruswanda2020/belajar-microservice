package com.onedev.product_service.dto.response;

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
public class StandardResponse {
    private String result;
    private String detail;
    private String path;
    private String date;
    private int code;
    private String version;
    private List<String> errors;
}