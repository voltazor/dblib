package com.voltazor.dblib;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 03.05.13
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 */
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
            return new DBError(e);
        } catch (Error e) {
            return new DBError(e);
        }

        return mResult;
    }

    protected abstract T load();

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