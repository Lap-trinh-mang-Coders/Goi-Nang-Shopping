package com.example.goinangshopping.exceptions;

public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found"),
    USERNAME_EXISTED(1002, "Username already exists"),
    EMAIL_EXISTED(1003, "Email already exists"),
    PASSWORD_INVALID(1004, "Invalid password"), EMAIL_INVALID(1005, "Invalid email" ),
    PASSWORD_CONFIRM_NOT_MATCH(1006, "Password confirm not match" ),;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}
