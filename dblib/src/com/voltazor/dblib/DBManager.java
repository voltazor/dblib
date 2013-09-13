package com.voltazor.dblib;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 10.05.13
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();

    private static final String LIKE = "like";
    private static final String AND = "and";
    private static final String OR = "or";

    private SQLiteOpenHelper mHelper;

    public DBManager(SQLiteOpenHelper helper) {
        mHelper = helper;
    }

    public int insertQuery(String table_name, ContentValues values) {
        Utils.i.log_d(TAG, "insertQuery");
        if (values != null) {
            Utils.i.log_d(TAG, "table: " + table_name + ", " + values.toString());
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = -1;

        try {
            result = (int) db.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "insertQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public <T> int insertQuery(T model) {
        Utils.i.log_d(TAG, "insertQuery");
        String table_name = TableManager.i.getTableName(model.getClass());
        ContentValues values = TableManager.i.getContentValues(model);
        if (values != null) {
            Utils.i.log_d(TAG, "table: " + table_name + ", " + values.toString());
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = -1;

        try {
            result = (int) db.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "insertQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public List<Integer> insertQuery(String table_name, List<ContentValues> values) {
        Utils.i.log_d(TAG, "insertQuery");
        if (values == null) {
            return null;
        }
        Utils.i.log_d(TAG, "table: " + table_name + ", " + values.toString());
        SQLiteDatabase db = mHelper.getWritableDatabase();

        List<Integer> result = new ArrayList<Integer>();
        try {
            for (ContentValues value : values) {
                int res = (int) db.insertWithOnConflict(table_name, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                result.add(res);
            }
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "insertQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public <T> int updateQuery(T model) {
        Utils.i.log_d(TAG, "updateQuery");
        String table_name = TableManager.i.getTableName(model.getClass());
        ContentValues values = TableManager.i.getContentValues(model);
        if (values != null) {
            Utils.i.log_d(TAG, "table: " + table_name + ", " + values.toString());
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = -1;

        try {
            result = (int) db.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "updateQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public <T> Cursor selectQuery(T model) {
        String table_name = TableManager.i.getTableName(model.getClass());
        return selectQuery(table_name);
    }

    public Cursor selectQuery(Class aClass) {
        String table_name = TableManager.i.getTableName(aClass);
        return selectQuery(table_name);
    }

    public Cursor selectQuery(String table_name) {
        return selectQuery(table_name, null, null);
    }

    public Cursor selectQuery(String table_name, List<SelectionElement> selectionElements) {
        return selectQuery(table_name, expandSelection(selectionElements), expandSelectionArguments(selectionElements));
    }

    public Cursor selectQuery(String table_name, SelectionElement... selectionElements) {
        return selectQuery(table_name, expandSelection(selectionElements), expandSelectionArguments(selectionElements));
    }

    private Cursor selectQuery(String table_name, String selection, String[] arguments) {
        Utils.i.log_d(TAG, "selectQuery");
        Utils.i.log_d(TAG, "table: " + table_name);
        if (selection != null && arguments != null) {
            Utils.i.log_d(TAG, selection);
            Utils.i.log_d(TAG, Arrays.toString(arguments));
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(table_name, null, selection, arguments, null, null, null);
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "selectQuery", e);
        }

        return cursor;
    }

    public boolean deleteQuery(String table_name, long id) {
        Utils.i.log_d(TAG, "deleteQuery");
        Utils.i.log_d(TAG, "table: " + table_name + ", id: " + id);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        boolean result = false;

        try {
            String where = "_id = " + id;
            result = db.delete(table_name, where, null) > 0;
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "deleteQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public int deleteQuery(String table_name, String where) {
        Utils.i.log_d(TAG, "deleteQuery");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = -1;

        try {
            result = db.delete(table_name, where, null);
        } catch (SQLiteException e) {
            Utils.i.log_e(TAG, "deleteQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    private static String expandSelection(List<SelectionElement> selectionElements) {
        StringBuilder builder = new StringBuilder();

        if (selectionElements == null) {
            return null;
        }

        for (int i = 0; i < selectionElements.size(); i++) {
            SelectionElement se = selectionElements.get(i);

            builder.append(se.getColumn());
            if (se.getType().equalsIgnoreCase(TableManager.TYPE_INTEGER)) {
                builder.append(" = ?");
            } else {
                builder.append(" ").append(LIKE).append(" ?");
            }

            if (i < (selectionElements.size() - 1)) {
                builder.append(" ").append(AND).append(" ");
            } else {
                builder.append(";");
            }
        }

        return builder.toString();
    }

    private static String[] expandSelectionArguments(List<SelectionElement> selectionElements) {
        if (selectionElements == null) {
            return null;
        }

        String[] arguments = new String[selectionElements.size()];

        for (int i = 0; i < selectionElements.size(); i++) {
            SelectionElement se = selectionElements.get(i);
            if (se.getType().equalsIgnoreCase(TableManager.TYPE_INTEGER)) {
                arguments[i] = selectionElements.get(i).getValue();
            } else {
                arguments[i] = "%" + selectionElements.get(i).getValue() + "%";
            }
        }

        return arguments;
    }

    private static String expandSelection(SelectionElement... selectionElements) {
        StringBuilder builder = new StringBuilder();

        if (selectionElements == null) {
            return null;
        }

        for (int i = 0; i < selectionElements.length; i++) {
            SelectionElement se = selectionElements[i];

            if (se == null) {
                continue;
            }

            builder.append(se.getColumn());
            if (se.getType().equalsIgnoreCase(TableManager.TYPE_INTEGER)) {
                builder.append(" = ?");
            } else {
                builder.append(" ").append(LIKE).append(" ?");
            }

            if (i < (selectionElements.length - 1)) {
                builder.append(" ").append(AND).append(" ");
            } else {
                builder.append(";");
            }
        }

        if (TextUtils.isEmpty(builder.toString())) {
            return null;
        }

        return builder.toString();
    }

    private static String[] expandSelectionArguments(SelectionElement... selectionElements) {
        if (selectionElements == null) {
            return null;
        }

        int count = 0;
        String[] arguments = new String[selectionElements.length];
        for (int i = 0; i < selectionElements.length; i++) {
            if (selectionElements[i] == null) {
                continue;
            }

            count++;
            if (selectionElements[i].getType().equalsIgnoreCase(TableManager.TYPE_INTEGER)) {
                arguments[i] = selectionElements[i].getValue();
            } else {
                arguments[i] = "%" + selectionElements[i].getValue() + "%";
            }
        }

        if (count == 0) {
            return null;
        }

        return arguments;
    }

}
