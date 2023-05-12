package com.note.iosnotes.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Objects;

public class Pref {
    private SharedPreferences preferences;

    public Pref(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getInt(String str) {
        return preferences.getInt(str, 0);
    }

    public String getString(String str) {
        return preferences.getString(str, "");
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }
}
