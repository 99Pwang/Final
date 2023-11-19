package com.example.springboot.exception;

import lombok.Getter;

/**
 * custom exception
 */
@Getter
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}
