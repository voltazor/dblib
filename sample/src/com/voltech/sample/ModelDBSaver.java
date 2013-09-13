package com.voltech.sample;

import com.voltazor.dblib.BaseDBSaver;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Дима Довбня
 * Date: 04.05.13
 * Time: 12:35
 * To change this template use File | Settings | File Templates.
 */
public class ModelDBSaver extends BaseDBSaver<List<Model>> {

    public ModelDBSaver(List<Model> object, BaseDBSaver.DBSaverCallback callback) {
        super(object, callback);
    }

    @Override
    protected boolean save(List<Model> object) {
        return App.getDbHelper().insertModels(object) >= 0;
    }

}
