package com.reqres;

import android.app.Application;

import com.reqres.screens.utils.Prefs;

public class ReqresApplication extends Application {

    private static ReqresApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Prefs prefs = new Prefs(this);
        prefs.init(this);
    }

    public static ReqresApplication getInstance() {
        return instance;
    }
}
