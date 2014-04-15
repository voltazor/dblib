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
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public enum  TableManager {
    i;

    public static final String TAG = TableManager.class.getSimpleName();

    public static final String DROP_TABLE = "drop table if exists";
    public static final String CREATE_TABLE = "create table";

    public static final String AUTOINCREMENT = "autoincrement";
    public static final String PRIMARY_KEY = "primary key";
    public static final String NOT_NULL = "not null";
    public static final String NULL = "null";

    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_LONG = "bigint";
    public static final String TYPE_TEXT = "text";

    public String getCreateStatement(Class aClass) {
        StringBuilder builder = new StringBuilder();
        Map<String, Column> columns = getColumns(aClass);

        builder .append(CREATE_TABLE).append(" ").append(getTableName(aClass)).append(" (");
        for (Column column: columns.values()) {
            if (column.isId()) {
                builder.append(statement(column, PRIMARY_KEY, AUTOINCREMENT));
            } else {
                builder.append(statement(column));
            }

            builder.append(", ");
        }
        builder.append("UNIQUE(_id) ON CONFLICT REPLACE); ");

        return builder.toString();
    }

    private String statement(Column column, String... params) {
        StringBuilder builder = new StringBuilder();

        builder.append(column.getName()).append(" ").append(column.getType());
        for (String param : params) {
            builder.append(" ").append(param);
        }

        return builder.toString();
    }

    public String getDropStatement(Class aClass) {
        return DROP_TABLE + " " + getTableName(aClass) + "; ";
    }

    public <T> ContentValues getContentValues(T model) {
        ContentValues map = new ContentValues();
        Map<String, Column> columns = getColumns(model.getClass());

        Field[] fields = model.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(TableField.class)) {
                TableField a = field.getAnnotation(TableField.class);
                Column column = columns.get(a.value());
                try {
                    field.setAccessible(true);
                    if (column.getType().equals(TYPE_INTEGER)) {
                        map.put(column.getName(), field.getInt(model));

                    } else if (column.getType().equals(TYPE_TEXT)) {
                        map.put(column.getName(), (String) field.get(model));

                    } else if (column.getType().equals(TYPE_LONG)) {
                        map.put(column.getName(), field.getLong(model));

                    } else if (column.getType().equals(TYPE_BOOLEAN)) {
                        int f = field.getBoolean(model) ? 1 : 0;
                        map.put(column.getName(), f);
                    }
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

            }
        }

        return map;
    }

    public <T> ContentValues getContentValues(Map<String, Column> columns, T model) {
        ContentValues map = new ContentValues();
        Field[] fields = model.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(TableField.class)) {
                TableField a = field.getAnnotation(TableField.class);
                Column column = columns.get(a.value());
                try {
                    field.setAccessible(true);
                    if (column.getType().equals(TYPE_INTEGER)) {
                        map.put(column.getName(), field.getInt(model));

                    } else if (column.getType().equals(TYPE_TEXT)) {
                        map.put(column.getName(), (String) field.get(model));

                    } else if (column.getType().equals(TYPE_LONG)) {
                        map.put(column.getName(), field.getLong(model));

                    } else if (column.getType().equals(TYPE_BOOLEAN)) {
                        int f = field.getBoolean(model) ? 1 : 0;
                        map.put(column.getName(), f);
                    }
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

            }
        }

        return map;
    }

    public <T> void setValues(Cursor cursor, T model) {
        Map<String, Column> columns = getColumns(model.getClass());

        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TableField.class)) {
                TableField a = field.getAnnotation(TableField.class);
                Column column = columns.get(a.value());
                try {
                    field.setAccessible(true);
                    int index = cursor.getColumnIndex(column.getName());
                    if (column.getType().equals(TYPE_INTEGER)) {
                        field.setInt(model, cursor.getInt(index));

                    } else if (column.getType().equals(TYPE_TEXT)) {
                        field.set(model, cursor.getString(index));

                    } else if (column.getType().equals(TYPE_LONG)) {
                        field.set(model, cursor.getLong(index));

                    } else if (column.getType().equals(TYPE_BOOLEAN)) {
                        boolean f = cursor.getInt(index) != 0;
                        field.set(model, f);
                    }
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

            }
        }
    }

    public <T> void setValues(Cursor cursor, Map<String, Column> columns, T model) {
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TableField.class)) {
                TableField a = field.getAnnotation(TableField.class);
                Column column = columns.get(a.value());
                try {
                    field.setAccessible(true);
                    int index = cursor.getColumnIndex(column.getName());
                    if (column.getType().equals(TYPE_INTEGER)) {
                        field.setInt(model, cursor.getInt(index));

                    } else if (column.getType().equals(TYPE_TEXT)) {
                        field.set(model, cursor.getString(index));

                    } else if (column.getType().equals(TYPE_LONG)) {
                        field.set(model, cursor.getLong(index));

                    } else if (column.getType().equals(TYPE_BOOLEAN)) {
                        boolean f = cursor.getInt(index) != 0;
                        field.set(model, f);
                    }
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

            }
        }
    }

    public <T> void setAllValues(Cursor cursor, Field[] fields, Map<String, Column> columns, T model) {
        TableField a;
        Column column;
        for (Field field : fields) {
            if (field.isAnnotationPresent(TableField.class)) {
                a = field.getAnnotation(TableField.class);
                column = columns.get(a.value());
                try {
                    field.setAccessible(true);
                    int index = cursor.getColumnIndex(column.getName());
                    if (column.getType().equals(TYPE_INTEGER)) {
                        field.setInt(model, cursor.getInt(index));

                    } else if (column.getType().equals(TYPE_TEXT)) {
                        field.set(model, cursor.getString(index));

                    } else if (column.getType().equals(TYPE_LONG)) {
                        field.set(model, cursor.getLong(index));

                    } else if (column.getType().equals(TYPE_BOOLEAN)) {
                        boolean f = cursor.getInt(index) != 0;
                        field.set(model, f);
                    }
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    public String getTableName(Class aClass) {
        if (aClass.isAnnotationPresent(DatabaseTable.class)) {
            DatabaseTable a = (DatabaseTable) aClass.getAnnotation(DatabaseTable.class);
            return a.value();
        }

        return null;
    }

    public Map<String, Column> getColumns(Class aClass) {
        Map<String, Column> columns = new HashMap<String, Column>();

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TableField.class)) {
                TableField a = field.getAnnotation(TableField.class);
                Column column = new Column(a.value(), a.type(), a.isId());
                columns.put(column.getName(), column);
            }
        }

        return columns;
    }

    public class Column {

        private String name;
        private String type;
        private boolean isId;

        public Column(String name, String type, boolean isId) {
            this.name = name;
            this.type = type;
            this.isId = isId;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isId() {
            return isId;
        }

    }

}
