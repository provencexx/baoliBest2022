package com.ldg.baoli.exception;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import java.util.UUID;

public class ServiceException extends Exception {
    private Throwable cause;
    private String errorCode;
    private String traceId;

    public ServiceException(String errorCode, String errorMsg) {
        this((Throwable)null, errorCode, errorMsg);
    }

    public ServiceException(Throwable cause, String errorCode, String errorMsg) {
        this(cause, errorCode, errorMsg, (String)null);
    }

    private ServiceException(Throwable cause, String errorCode, String errorMsg, String traceId) {
        super(errorMsg);
        this.cause = cause;
        this.errorCode = errorCode;
        this.traceId = traceId;
        if (this.traceId == null || "".equals(this.traceId)) {
            this.traceId = UUID.randomUUID().toString().replaceAll("-", "");
        }

    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getTraceId() {
        return this.traceId;
    }
}
