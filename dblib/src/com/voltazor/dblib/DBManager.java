/*
 * Copyright (c) 2014 Dmitriy Dovbnya
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.voltazor.dblib;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Log.d(TAG, "insertQuery");
        if (values != null) {
            Log.d(TAG, "table: " + table_name + ", " + values.toString());
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = -1;

        try {
            result = (int) db.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException e) {
            Log.e(TAG, "insertQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public <T> int insertQuery(T model) {
        Log.d(TAG, "insertQuery");
        String table_name = TableManager.i.getTableName(model.getClass());
        ContentValues values = TableManager.i.getContentValues(model);
        if (values != null) {
            Log.d(TAG, "table: " + table_name + ", " + values.toString());
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = -1;

        try {
            result = (int) db.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException e) {
            Log.e(TAG, "insertQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public List<Integer> insertQuery(String table_name, List<ContentValues> values) {
        Log.d(TAG, "insertQuery");
        if (values == null) {
            return null;
        }
        Log.d(TAG, "table: " + table_name + ", " + values.toString());
        SQLiteDatabase db = mHelper.getWritableDatabase();

        List<Integer> result = new ArrayList<Integer>();
        try {
            for (ContentValues value : values) {
                int res = (int) db.insertWithOnConflict(table_name, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                result.add(res);
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "insertQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public <T> int updateQuery(T model) {
        Log.d(TAG, "updateQuery");
        String table_name = TableManager.i.getTableName(model.getClass());
        ContentValues values = TableManager.i.getContentValues(model);
        if (values != null) {
            Log.d(TAG, "table: " + table_name + ", " + values.toString());
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = -1;

        try {
            result = (int) db.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException e) {
            Log.e(TAG, "updateQuery", e);
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
        Log.d(TAG, "selectQuery");
        Log.d(TAG, "table: " + table_name);
        if (selection != null && arguments != null) {
            Log.d(TAG, selection);
            Log.d(TAG, Arrays.toString(arguments));
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(table_name, null, selection, arguments, null, null, null);
        } catch (SQLiteException e) {
            Log.e(TAG, "selectQuery", e);
        }

        return cursor;
    }

    public boolean deleteQuery(String table_name, long id) {
        Log.d(TAG, "deleteQuery");
        Log.d(TAG, "table: " + table_name + ", id: " + id);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        boolean result = false;

        try {
            String where = "_id = " + id;
            result = db.delete(table_name, where, null) > 0;
        } catch (SQLiteException e) {
            Log.e(TAG, "deleteQuery", e);
        } finally {
            db.close();
        }

        return result;
    }

    public int deleteQuery(String table_name, String where) {
        Log.d(TAG, "deleteQuery");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int result = -1;

        try {
            result = db.delete(table_name, where, null);
        } catch (SQLiteException e) {
            Log.e(TAG, "deleteQuery", e);
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
