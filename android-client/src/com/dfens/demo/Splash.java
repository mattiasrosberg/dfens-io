package com.dfens.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.UiThread;

@Fullscreen
@NoTitle
@EActivity
public class Splash extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        super.onCreate(icicle);

        final ImageView splashImage = new ImageView(this);
        splashImage.setBackgroundColor(Color.WHITE);
        splashImage.setImageResource(R.drawable.dfens_blue_modified);
        splashImage.setScaleType(ImageView.ScaleType.CENTER);
        splashImage.setPadding(30,30,30,30);
        splashImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        }, 3000);

        setContentView(splashImage, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
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
