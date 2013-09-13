package com.voltech.sample;

import android.app.Application;

import java.lang.Override;

/**
 * Created with IntelliJ IDEA.
 * User: voltazor
 * Date: 10.05.13
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 */
public class App extends Application {

    private static DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        dbHelper = new DBHelper(getApplicationContext());
    }

    public static DBHelper getDbHelper() {
        return dbHelper;
    }

}
