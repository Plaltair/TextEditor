package com.plaltair.texteditor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Pierluca Lippi on 06/03/18.
 */

public class Data {

    private Context context;

    public Data(Context context) {
        this.context = context;
    }

    public void write(String key, int n) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, n);
        editor.apply();
    }

    public void write(String key, long l) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, l);
        editor.apply();
    }

    public void write(String key, float f) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(key, f);
        editor.apply();
    }

    public void write(String key, boolean b) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, b);
        editor.apply();
    }

    public void write(String key, String s) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, s);
        editor.apply();
    }

    public void write(String key, Set<String> stringSet) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(key, stringSet);
        editor.apply();
    }

    public int read(String key, int defaultValue) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultValue);
    }

    public long read(String key, long defaultValue) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        return sharedPref.getLong(key, defaultValue);
    }

    public float read(String key, float defaultValue) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        return sharedPref.getFloat(key, defaultValue);
    }

    public boolean read(String key, boolean defaultValue) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }

    public String read(String key, String defaultValue) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public Set<String> read(String key, Set<String> defaultValue) {
        Activity activity = (Activity)context;
        SharedPreferences sharedPref = activity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        return sharedPref.getStringSet(key, defaultValue);
    }

}
