package com.dfens.demo;

import android.app.Activity;
import android.os.Bundle;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;

/**
 * Created by mattias on 2014-11-02.
 */

@NoTitle
@EActivity(R.layout.activity_device_list)
public class DeviceListActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
