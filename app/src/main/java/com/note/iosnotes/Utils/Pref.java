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

    public void putString(String str, String str2) {
        checkNullKey(str);
        checkNullValue(str2);
        preferences.edit().putString(str, str2).apply();
    }

    public void checkNullKey(String str) {
        Objects.requireNonNull(str);
    }

    public void checkNullValue(String str) {
        Objects.requireNonNull(str);
    }
}
