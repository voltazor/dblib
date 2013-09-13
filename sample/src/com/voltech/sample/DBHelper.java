package com.voltech.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.voltazor.dblib.DBManager;
import com.voltazor.dblib.TableManager;
import com.voltazor.dblib.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 14.02.13
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = DBHelper.class.getSimpleName();

    private static String DB_NAME = "table";
    private static int DB_VERSION = 1;

    private DBManager dbManager;
    private List<Class> tables = new ArrayList<Class>();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Utils.i.log_d(TAG, "Constructor");

        dbManager = new DBManager(this);
        tables.add(Model.class);

        SQLiteDatabase db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Utils.i.log_d(TAG, "onCreate");
        try {
            for (Class table : tables) {
                db.execSQL(TableManager.i.getCreateStatement(table));
            }
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "onCreate", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Utils.i.log_d(TAG, "onUpgrade");

        try {
            for (Class table : tables) {
                db.execSQL(TableManager.i.getDropStatement(table));
            }
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "onUpgrade", e);
        }

        onCreate(db);
    }

    public int insertModel(Model model) {
        Utils.i.log_d(TAG, "insertModel");
        return dbManager.insertQuery(model);
//        String table_name = TableManager.i.getTableName(Model.class);
//        ContentValues values = TableManager.i.getContentValues(model);
//
//        return dbManager.insertQuery(table_name, values);
    }

    public int insertModels(List<Model> models) {
        Utils.i.log_d(TAG, "insertModel");
        String table_name = TableManager.i.getTableName(Model.class);
        List<ContentValues> values = new ArrayList<ContentValues>();
        Map<String, TableManager.Column> columns = TableManager.i.getColumns(Model.class);
        for (Model model : models) {
            values.add(TableManager.i.getContentValues(columns, model));
        }

        dbManager.insertQuery(table_name, values);
        return 0;
    }

    public List<Model> selectModels() {
        Utils.i.log_d(TAG, "selectModels");
        String table_name = TableManager.i.getTableName(Model.class);
        List<Model> models = new ArrayList<Model>();
        Map<String, TableManager.Column> columns = TableManager.i.getColumns(Model.class);
        Field[] fields = Model.class.getDeclaredFields();
        Cursor cursor = dbManager.selectQuery(Model.class);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Model model = new Model();
                    TableManager.i.setAllValues(cursor, fields, columns, model);
                    models.add(model);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return models;
    }

}
