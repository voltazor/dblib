package com.voltech.sample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.voltazor.dblib.BaseDBLoader;
import com.voltazor.dblib.BaseDBSaver;
import com.voltazor.dblib.DBError;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {

    private static final String SHARED_PREF_NAME = "shared_pref";
    private static final String SP_NEXT_ID = "next_id";

    private TextView textView;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView = (TextView) findViewById(R.id.text);

        findViewById(R.id.write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(write() ? "Writing success" : "Writing failed");
            }
        });

        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(read());
            }
        });

        findViewById(R.id.write_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeAsync();
            }
        });

        findViewById(R.id.read_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAsync();
            }
        });

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

    }

    private boolean write() {
        return App.getDbHelper().insertModel(createModel()) >= 0;
    }

    private void writeAsync() {
        List<Model> list = new ArrayList<Model>();
        for (int i = 0; i < 10; i++) {
            list.add(createModel());
        }

        new ModelDBSaver(list, new BaseDBSaver.DBSaverCallback() {
            @Override
            public void onSuccess(boolean success) {
                Toast.makeText(getApplicationContext(), "Success: " + success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(DBError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private String read() {
        return executeText(App.getDbHelper().selectModels());
    }

    private void readAsync() {
        new ModelDBLoader(new BaseDBLoader.DBLoaderCallback<List<Model>>() {
            @Override
            public void onSuccess(List<Model> result) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                textView.setText(executeText(result));
            }

            @Override
            public void onFailure(DBError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private Model createModel() {
        Model model = new Model();

        model.setId(getNextId());
        model.setTitle("Model " + model.getId());
        model.setTime(System.currentTimeMillis());
        model.setOpen(model.getId() % 2 == 0);

        return model;
    }

    private String executeText(List<Model> models) {
        StringBuilder builder = new StringBuilder();
        for (Model model : models) {
            builder.append(model.getId()).append("\n")
                    .append(model.getTitle()).append("\n")
                    .append(model.getTime()).append("\n")
                    .append(model.isOpen()).append("\n")
                    .append("---");
        }

        return builder.toString();
    }

    private int getNextId() {
        int id = sharedPreferences.getInt(SP_NEXT_ID, 1);

        sharedPreferences.edit().putInt(SP_NEXT_ID, id + 1).commit();

        return id;
    }



}
