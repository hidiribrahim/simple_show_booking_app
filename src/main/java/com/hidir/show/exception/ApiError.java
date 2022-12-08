package com.hidir.show.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Setter
public class ApiError {

    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;
    private String mesage;
    Map<String, String> subErrors;

    private ApiError() {
        timestamp = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
    }

    public ApiError(HttpStatus httpStatus, String mesage) {
        this();
        this.httpStatus = httpStatus;
        this.mesage = mesage;
    }

    public ApiError(HttpStatus httpStatus, String mesage,Map<String,String> subErrors) {
        this();
        this.httpStatus = httpStatus;
        this.mesage = mesage;
        this.subErrors = subErrors;
    }
}