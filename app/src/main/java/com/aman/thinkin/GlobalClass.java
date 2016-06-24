package com.aman.thinkin;

import android.app.Application;

import com.batch.android.Batch;
import com.batch.android.Config;

/**
 * Created by aman on 19/6/16.
 */
public class GlobalClass extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        Batch.Push.setGCMSenderId("AIzaSyBU9cmbgL8T0k9-SO67oinsBaNUqGD9bh8");

        // TODO : switch to live Batch Api Key before shipping
        Batch.setConfig(new Config("DEV576693299B024E61359FBE26841")); // devloppement
        // Batch.setConfig(new Config("57669329978405E24DB1A6D19F07FF")); // live
    }
}
