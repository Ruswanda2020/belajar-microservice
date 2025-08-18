package com.onedev.product_service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataResponse<T> extends StandardResponse {
    private T data;

    public DataResponse(String result, String detail, String path, String date, int code, String version, List<String> errors, T data) {
        super(result, detail, path, date, code, version, errors);
        this.data = data;
    }
}