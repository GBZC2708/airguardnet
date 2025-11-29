package com.airguardnet.common.web;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class ApiResponse<T> {
    private T data;
    private List<String> errors;
    private Instant timestamp;

    public ApiResponse() {
        this.timestamp = Instant.now();
    }

    public ApiResponse(T data) {
        this.data = data;
        this.timestamp = Instant.now();
    }

    public ApiResponse(List<String> errors) {
        this.errors = errors;
        this.timestamp = Instant.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(Collections.singletonList(message));
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
