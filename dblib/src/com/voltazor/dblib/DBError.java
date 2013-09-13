package com.voltazor.dblib;

import android.text.TextUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 04.05.13
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class DBError extends Exception {

    private String message;

    public DBError(String message) {
        super(message);
        this.message = message;
    }

    public DBError(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String getMessage() {
        return TextUtils.isEmpty(message) ? "DBError: " + super.getMessage() : message;
    }

    public boolean isNoData() {
        return (getCause() instanceof NoDataException);
    }

}
