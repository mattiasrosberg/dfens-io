package com.dfens.demo;

import android.app.Activity;
import android.os.Bundle;
import org.androidannotations.annotations.EActivity;

@EActivity
public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}