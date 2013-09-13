package com.voltazor.dblib;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 04.05.13
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
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
            return new DBError(e.getMessage());
        } catch (Error e) {
            return new DBError(e.getMessage());
        }

        return result;
    }

    protected abstract boolean save(T object);

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
