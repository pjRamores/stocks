package com.orion.stocks.core;

public class DaoException extends RuntimeException {

    private static final long serialVersionUID = -2318844067692364722L;

    private String message;

    public DaoException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
