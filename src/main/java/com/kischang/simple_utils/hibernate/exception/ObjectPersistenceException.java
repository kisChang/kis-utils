package com.kischang.simple_utils.hibernate.exception;

/**
 * @author KisChang
 * @version 1.0
 */
public class ObjectPersistenceException extends Exception {
    private int errorCode = -1;

    public ObjectPersistenceException(int errorCode) {
        this.errorCode = errorCode;
    }

    public ObjectPersistenceException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ObjectPersistenceException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ObjectPersistenceException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ObjectPersistenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
