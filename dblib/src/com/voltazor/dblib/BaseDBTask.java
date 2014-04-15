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

import android.os.AsyncTask;

public abstract class BaseDBTask extends AsyncTask<Void, Void, Object> {

    private long startTime;
    private ExecutorCallback callback;

    @Override
    protected void onPreExecute() {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected abstract Object doInBackground(Void... params);

    @Override
    protected void onPostExecute(Object result) {
        long time = System.currentTimeMillis() - startTime;
        if (callback != null) {
            callback.finished(time);
        }
    }

    public void setExecutorCallback(ExecutorCallback callback) {
        this.callback = callback;
    }

    public ExecutorCallback getExecuteCallback() {
        return callback;
    }

}
