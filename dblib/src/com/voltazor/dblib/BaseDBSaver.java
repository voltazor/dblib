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

import java.sql.SQLException;

public abstract class BaseDBSaver<T> extends BaseDBTask {

    private DBSaverCallback callback;
    private T object;

    public BaseDBSaver(T object) {
        this.object = object;
    }

    public BaseDBSaver(T object, DBSaverCallback callback) {
        this.object = object;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Void... params) {
        boolean result;
        try {

            result = save(object);

        } catch (Exception e) {
            e.printStackTrace();
            return new DBError(e.getMessage());
        } catch (Error e) {
            e.printStackTrace();
            return new DBError(e.getMessage());
        }

        return result;
    }

    protected abstract boolean save(T object) throws SQLException;

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (isCancelled()) {
            return;
        }
        if (callback != null) {
            if (result != null) {
                if (result instanceof Boolean) {
                    callback.onSuccess((Boolean)result);
                }
                if (result instanceof DBError) {
                    callback.onFailure((DBError)result);
                }
            } else {
                callback.onFailure(new DBError(new NullPointerException()));
            }
        }
    }

    public void setCallback(DBSaverCallback callback) {
        this.callback = callback;
    }

    public DBSaverCallback getCallback() {
        return callback;
    }

    public interface DBSaverCallback {

        public void onSuccess(boolean success);

        public void onFailure(DBError error);

    }

}
