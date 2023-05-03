package com.note.iosnotes.Application;

import android.app.Application;

import com.testfairy.TestFairy;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TestFairy.begin(this, "SDK-00WcTZTZ");
    }
}
