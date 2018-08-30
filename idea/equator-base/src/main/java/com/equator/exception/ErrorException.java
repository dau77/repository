package com.equator.exception;

import com.equator.web.Result;
import lombok.Getter;


@Getter
public class ErrorException extends RuntimeException {

//    private int code;
//
//    private String message;

//    public ErrorException(int code, String message) {
//        this.code = code;
//        this.message = message;
//    }

    private Result result;

    public ErrorException(Result result) {
        this.result = result;
    }

    public ErrorException(int resultCode, String messageCode) {
        this.result = new Result(resultCode, messageCode);
    }

    public ErrorException(String messageCode) {
        this(1000, messageCode); //1000临时，待研究
    }
}
