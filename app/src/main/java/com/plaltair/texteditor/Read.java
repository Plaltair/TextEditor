package com.plaltair.texteditor;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.nio.channels.FileChannel;

public class Read extends AppCompatActivity {

    private EditText editText;

    private String fileName;

    private StringBuffer text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int dataValue = new Data(Read.this).read("font", 5);

        editText = findViewById(R.id.editText);

        setFont(editText, dataValue);

        fileName = getIntent().getStringExtra("fileName");

        getSupportActionBar().setTitle(fileName);

        text = readFile(fileName);

        editText.setText(text);
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

    private StringBuffer readFile(String fileName) {
        try {
            FileInputStream inputStream = new FileInputStream(getFilesDir() + "/SavedTexts/" + fileName);
            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int size;

            while ((size = inputStream.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, size));
            }

            return fileContent;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        else if (id == R.id.action_save) {
            if (editText.getText().toString().length() == 0) {
                Toast.makeText(Read.this, getResources().getString(R.string.emptyText), Toast.LENGTH_SHORT).show();
            }
            else {
                File file = new File(getFilesDir() + "/SavedTexts/" + fileName);

                FileOutputStream outputStream;

                try {
                    file.createNewFile();

                    outputStream = new FileOutputStream(file.getAbsolutePath());
                    outputStream.write(editText.getText().toString().getBytes());
                    outputStream.close();

                    Toast.makeText(Read.this, getResources().getString(R.string.updateFile), Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (id == R.id.action_download) {
            if (!checkPermission()) {
                requestPermission();
            }
            else {
                File directory;
                if (fileName.contains(".")) {
                    directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
                }
                else {
                    directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName + ".txt");
                }

                File file = new File(getFilesDir() + "/SavedTexts/" + fileName);
                try {
                    copyFile(file, directory);
                    sendNotification();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (id == R.id.action_delete) {
            createDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Read.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_delete_file, null);

        TextView title = view.findViewById(R.id.title);

        ScaledFont.fixTextSize(title, 20);

        builder.setView(view)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        File file = new File(getFilesDir() + "/SavedTexts/" + fileName);
                        file.delete();
                        finish();
                        startActivity(new Intent(Read.this, TextFiles.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.create();
        builder.show();

    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    private void sendNotification() {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        PendingIntent contentIntent = PendingIntent.getActivity(Read.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Read.this, "Text Editor")
                    .setChannelId("Text Editor")
                    .setSmallIcon(R.drawable.app_icon_white)
                    .setContentTitle(Read.this.getResources().getString(R.string.fileDownloaded))
                    .setContentText(Read.this.getResources().getString(R.string.fileDownloaded2))
                    .setColor(Color.parseColor("#FFCA28"))
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setPriority(Notification.PRIORITY_HIGH);

            NotificationManager mNotificationManager = (NotificationManager) Read.this.getSystemService(Read.this.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, mBuilder.build());
        }
        else {
            try {
                NotificationChannel mChannel = new NotificationChannel("Text Editor", "Text Editor", NotificationManager.IMPORTANCE_HIGH);

                mChannel.enableVibration(true);
                mChannel.enableLights(true);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Read.this, "Text Editor")
                        .setChannelId("Text Editor")
                        .setSmallIcon(R.drawable.app_icon_white)
                        .setContentTitle(Read.this.getResources().getString(R.string.fileDownloaded))
                        .setContentText(Read.this.getResources().getString(R.string.fileDownloaded2))
                        .setColor(Color.parseColor("#FFCA28"))
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);

                NotificationManager mNotificationManager = (NotificationManager) Read.this.getSystemService(Read.this.NOTIFICATION_SERVICE);

                mNotificationManager.createNotificationChannel(mChannel);

                mNotificationManager.notify(0, mBuilder.build());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Read.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Read.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra("externalFile", false)) {
            finish();
            startActivity(new Intent(Read.this, Main.class));
        }
        else {
            super.onBackPressed();
        }
    }
}
