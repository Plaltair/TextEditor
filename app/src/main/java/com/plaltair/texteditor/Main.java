package com.plaltair.texteditor;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import java.io.FileOutputStream;

public class Main extends AppCompatActivity {

    private final int PICK_FILE = 1234;

    private EditText editText;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);
        
        int dataValue = new Data(Main.this).read("font", 5);

        editText = findViewById(R.id.editText);

        setFont(editText, dataValue);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_icon);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_new) {
                    finish();
                    startActivity(getIntent());
                }
                else if (id == R.id.action_open) {
                    mDrawerLayout.closeDrawers();

                    if (!checkPermission()) {
                        requestPermission();
                    }
                    else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("text/*");
                        startActivityForResult(intent, PICK_FILE);
                    }
                }
                else if (id == R.id.action_my_files) {
                    mDrawerLayout.closeDrawers();

                    startActivity(new Intent(Main.this, TextFiles.class));
                }
                else if (id == R.id.action_font_size) {
                    mDrawerLayout.closeDrawers();

                    startActivity(new Intent(Main.this, FontSettings.class));
                    finish();
                }

                return false;
            }
        });
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
                Toast.makeText(Main.this, getResources().getString(R.string.emptyText), Toast.LENGTH_SHORT).show();
            }
            else {
                createDialog();
            }
        }
        else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE && resultCode == RESULT_OK) {
            Intent intent = new Intent(Main.this, ReadExternalFile.class);
            intent.putExtra("uri", data.getData());
            intent.putExtra("fromApp", true);
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
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
                            Toast.makeText(Main.this, getResources().getString(R.string.nameNotValid), Toast.LENGTH_LONG).show();
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
                                    Intent intent = new Intent(Main.this, Read.class);
                                    intent.putExtra("fileName", editTextDialog.getText().toString());
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(Main.this, getResources().getString(R.string.fileAlreadyExists), Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(Main.this, getResources().getString(R.string.nameNotValid), Toast.LENGTH_LONG).show();
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

    private File[] getAllFiles() {
        String path = getFilesDir() + "/SavedTexts";
        File directory = new File(path);
        File[] files = directory.listFiles();

        return files;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Main.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Main.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
