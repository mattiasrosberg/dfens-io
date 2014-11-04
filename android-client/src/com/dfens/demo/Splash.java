package com.dfens.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.dfens.demo.network.NetClient;
import org.androidannotations.annotations.*;

@Fullscreen
@NoTitle
@EActivity
public class Splash extends Activity {

    @Bean
    NetClient netClient;

//    @Pref
//    Prefs_ prefs;

    SharedPreferences prefs;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        prefs = getSharedPreferences("Dfens", MODE_PRIVATE);

        super.onCreate(icicle);

        final ImageView splashImage = new ImageView(this);
        splashImage.setBackgroundColor(Color.WHITE);
        splashImage.setImageResource(R.drawable.dfens_blue_modified);
        splashImage.setScaleType(ImageView.ScaleType.CENTER);
        splashImage.setPadding(30, 30, 30, 30);
        splashImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchMaxSeqNo();
            }
        }, 1000);

        setContentView(splashImage, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Background
    protected void fetchMaxSeqNo() {
        try {
            int max = netClient.getLastEventSeqNo();
            Log.d("Dfens", "Setting Prefs to: " + max);
            prefs.edit().putInt("maxSeqNo", max).commit();
            Log.d("Dfens", "Value is now " + prefs.getInt("maxSeqNo", 0));
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @UiThread
    protected void updateUI() {
        startActivity(new Intent(this, PinActivity_.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
