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

public abstract class BaseDBLoader<T> extends BaseDBTask {

    private DBLoaderCallback<T> callback;
    private T mResult;

    public BaseDBLoader(DBLoaderCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {

            mResult = load();

        } catch (Exception e) {
            e.printStackTrace();
            return new DBError(e);
        } catch (Error e) {
            e.printStackTrace();
            return new DBError(e);
        }

        return mResult;
    }

    protected abstract T load() throws SQLException;

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (isCancelled()) {
            return;
        }
        if (callback != null) {
            if (result != null) {
                if (result instanceof DBError) {
                    callback.onFailure((DBError)result);
                } else {
                    callback.onSuccess((T) result);
                }
            } else {
                callback.onFailure(new DBError(new NoDataException()));
            }
        }
    }

    public void setCallback(DBLoaderCallback<T> callback) {
        this.callback = callback;
    }

    public DBLoaderCallback<T> getCallback() {
        return callback;
    }

    public interface DBLoaderCallback<T> {

        public void onSuccess(T result);

        public void onFailure(DBError error);

    }

}