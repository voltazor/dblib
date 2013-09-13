package com.voltazor.dblib;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 04.05.13
 * Time: 13:04
 * To change this template use File | Settings | File Templates.
 */
public class SelectionElement {

    private String column;
    private String value;
    private String type;

    public SelectionElement(String column, String value, String type) {
        this.column = column;
        this.value = value;
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
