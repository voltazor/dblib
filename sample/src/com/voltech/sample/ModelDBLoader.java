package com.voltech.sample;

import android.util.Log;
import com.voltazor.dblib.BaseDBLoader;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Дима Довбня
 * Date: 04.05.13
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class ModelDBLoader extends BaseDBLoader<List<Model>> {

    public ModelDBLoader(DBLoaderCallback<List<Model>> callback) {
        super(callback);
    }

    @Override
    protected List<Model> load() {
        long start = System.currentTimeMillis();

        List<Model> models = App.getDbHelper().selectModels();

        Log.d("ModelDBLoader", "count: " + models.size() + " time: " + (System.currentTimeMillis() - start) + "ms");

        return models;
    }

}
