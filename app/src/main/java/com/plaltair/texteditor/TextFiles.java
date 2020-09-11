package com.plaltair.texteditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextFiles extends AppCompatActivity {

    private List<TextFilesRowItem> rowItems = new ArrayList<TextFilesRowItem>();

    private TextFilesCustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_files);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.listView);
        TextView emptyListView = findViewById(R.id.emptyListView);

        listView.setEmptyView(emptyListView);

        ScaledFont.fixTextSize(emptyListView, 10);

        File files[] = getAllFiles();

        if (files != null) {
            Collections.reverse(Arrays.asList(files));

            for (int i = 0; i < files.length; i++) {
                TextFilesRowItem item = new TextFilesRowItem(files[i].getName());
                rowItems.add(item);
            }
        }

        customAdapter = new TextFilesCustomAdapter(TextFiles.this, rowItems);
        listView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (customAdapter.isSelectMode()) {
            getMenuInflater().inflate(R.menu.menu_text_files, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        else if (id == R.id.action_delete) {
            customAdapter.deleteSlectedFiles();
        }

        return super.onOptionsItemSelected(item);
    }

    private File[] getAllFiles() {
        String path = getFilesDir() + "/SavedTexts";
        File directory = new File(path);
        File[] files = directory.listFiles();

        return files;
    }
}
