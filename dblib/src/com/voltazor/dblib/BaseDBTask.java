package com.voltazor.dblib;

import android.os.AsyncTask;

/**
 * Created by: Дима Довбня
 * Date: 03.09.13 18:22
 */
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
