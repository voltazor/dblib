package com.voltech.sample;

import com.voltazor.dblib.DatabaseTable;
import com.voltazor.dblib.TableField;
import com.voltazor.dblib.TableManager;

/**
 * Created with IntelliJ IDEA.
 * User: voltazor
 * Date: 10.05.13
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(name = "model")
public class Model {

    @TableField(name = "_id", type = TableManager.TYPE_INTEGER, isId = true)
    private int id;

    @TableField(name = "title", type = TableManager.TYPE_TEXT)
    private String title;

    @TableField(name = "time", type = TableManager.TYPE_LONG)
    private long time;

    @TableField(name = "isOpen", type = TableManager.TYPE_BOOLEAN)
    private boolean isOpen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

}
