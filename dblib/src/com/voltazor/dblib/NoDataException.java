package com.voltazor.dblib;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 04.05.13
 * Time: 15:56
 * To change this template use File | Settings | File Templates.
 */
public class NoDataException extends Exception {

    private static final String NO_DATA = "No data";

    @Override
    public String getMessage() {
        return NO_DATA;
    }

}
