package com.itplh.common;

import lombok.Data;

@Data
public class Result<T> {

    private String code;
    private String message;
    private boolean success;
    private T data;
    private long timestamp = System.currentTimeMillis();

    public static <T> Result ok(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("Operation success");
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> Result error(T data) {
        Result result = new Result();
        result.setCode("0");
        result.setMessage("Operation failed");
        result.setSuccess(false);
        result.setData(data);
        return result;
    }

}
