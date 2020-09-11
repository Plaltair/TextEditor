package com.plaltair.texteditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.FileChannel;

public class ReadExternalFile extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_external_file);

        if (getIntent().getBooleanExtra("fromApp", false)) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int dataValue = new Data(ReadExternalFile.this).read("font", 5);

        editText = findViewById(R.id.editText);

        setFont(editText, dataValue);

        Uri uri = getIntent().getParcelableExtra("uri");

        if (uri == null) {
            editText.setText(readFile(getIntent().getData()));
        }
        else {
            editText.setText(readFile(uri));
        }
    }

    private void setFont(EditText editText, int value) {

        switch (value) {
            case 0:
                ScaledFont.fixTextSize(editText, 25);
                break;
            case 1:
                ScaledFont.fixTextSize(editText, 24);
                break;
            case 2:
                ScaledFont.fixTextSize(editText, 23);
                break;
            case 3:
                ScaledFont.fixTextSize(editText, 22);
                break;
            case 4:
                ScaledFont.fixTextSize(editText, 21);
                break;
            case 5:
                ScaledFont.fixTextSize(editText, 20);
                break;
            case 6:
                ScaledFont.fixTextSize(editText, 19);
                break;
            case 7:
                ScaledFont.fixTextSize(editText, 18);
                break;
            case 8:
                ScaledFont.fixTextSize(editText, 17);
                break;
            case 9:
                ScaledFont.fixTextSize(editText, 16);
                break;
            case 10:
                ScaledFont.fixTextSize(editText, 15);
                break;
            default:
                break;
        }
    }

    private StringBuffer readFile(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);

            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int size;

            while ((size = in.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, size));
            }

            return fileContent;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private File[] getAllFiles() {
        String path = getFilesDir() + "/SavedTexts";
        File directory = new File(path);
        File[] files = directory.listFiles();

        return files;
    }

    private void saveFile(String fileName, String text) {
        File directory = new File(getFilesDir() + "/SavedTexts");

        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(getFilesDir() + "/SavedTexts/" + fileName);

        FileOutputStream outputStream;

        try {
            file.createNewFile();

            outputStream = new FileOutputStream(file.getAbsolutePath());
            outputStream.write(text.getBytes());
            outputStream.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ReadExternalFile.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_save_file, null);

        TextView title = view.findViewById(R.id.title);
        final EditText editTextDialog = view.findViewById(R.id.editText);
        final TextView warning = view.findViewById(R.id.warning);

        editTextDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextDialog.getText().toString().contains(".")) {
                    warning.setVisibility(View.VISIBLE);
                }
                else {
                    warning.setVisibility(View.GONE);
                }
            }
        });

        ScaledFont.fixTextSize(title, 20);
        ScaledFont.fixTextSize(editTextDialog, 20);
        ScaledFont.fixTextSize(warning, 30);

        builder.setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = editTextDialog.getText().toString();
                        if (name.contains("/") || name.contains("\\") || name.contains(":") || name.contains("*") || name.contains("\"") || name.contains("<") || name.contains(">") || name.contains("|") || String.valueOf(name.charAt(0)).equals(".")) {
                            Toast.makeText(ReadExternalFile.this, getResources().getString(R.string.nameNotValid), Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (name.length() > 0) {
                                while(editTextDialog.getText().toString().charAt(editTextDialog.length() - 1) == ' ') {
                                    String newText = editTextDialog.getText().toString().substring(0, editTextDialog.getText().toString().length() - 1);
                                    editTextDialog.setText(newText);
                                }
                                while(editTextDialog.getText().toString().charAt(editTextDialog.length() - 1) == '.') {
                                    String newText = editTextDialog.getText().toString().substring(0, editTextDialog.getText().toString().length() - 1);
                                    editTextDialog.setText(newText);
                                }
                                File files[] = getAllFiles();
                                boolean nameAlreadyExists = false;
                                if (files != null) {
                                    for (int i = 0; i < files.length; i++) {
                                        if (editTextDialog.getText().toString().equals(files[i].getName())) {
                                            nameAlreadyExists = true;
                                            break;
                                        }
                                    }
                                }
                                if (!nameAlreadyExists) {
                                    saveFile(editTextDialog.getText().toString(), editText.getText().toString());
                                    editText.setText("");
                                    finish();
                                    Intent intent = new Intent(ReadExternalFile.this, Read.class);
                                    intent.putExtra("fileName", editTextDialog.getText().toString());
                                    intent.putExtra("externalFile", true);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(ReadExternalFile.this, getResources().getString(R.string.fileAlreadyExists), Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(ReadExternalFile.this, getResources().getString(R.string.nameNotValid), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.create();
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            if (editText.getText().toString().length() == 0) {
                Toast.makeText(ReadExternalFile.this, getResources().getString(R.string.emptyText), Toast.LENGTH_SHORT).show();
            }
            else {
                createDialog();
            }
        }
        else if (id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
