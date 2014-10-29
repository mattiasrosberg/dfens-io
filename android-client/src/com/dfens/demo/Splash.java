
package com.handelsbanken.mobile.android;

import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.handelsbanken.android.resources.domain.EntryPointDTO;
import com.handelsbanken.android.resources.domain.error.HBError;
import com.handelsbanken.android.resources.network.RestException;

@Fullscreen
@NoTitle
@EActivity(R.layout.splash)
public class Splash extends Activity {

    private static final String TAG = Splash.class.getSimpleName();

    @ViewById(R.id.splash_handelsbanken_text_Image)
    ImageView textImage;

    private static final float XHDPI_WIDTH = 800.0f;

    private DisplayMetrics displayMetrics;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        fetchEntries();
    }

    @Background
    protected void fetchEntries() {

        try {
            restClient.clearSession();
            EntryPointDTO entryPointDTO = restClient.getEntryPoint(this);
            application.setEntryPointDTO(entryPointDTO);
            updateUIAfterFetchEntries();
        } catch (RestException e) {
            handleError(e.getError());
        }
    }

    private DialogInterface.OnClickListener navigateToLoginOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            router.gotoStartAppActivity(Splash.this);
            finish();
        }
    };

    @UiThread
    protected void handleError(HBError error) {
        try {
            uiManager.showOkDialog(getString(R.string.common_message_title), error.getMessage(),
                    navigateToLoginOnClickListener);
        } catch (Exception e) {
            uiManager.showOkDialog(getString(R.string.common_message_title),
                    getString(R.string.common_error_message_with_network_check), navigateToLoginOnClickListener);
        }
    }

    @UiThread
    protected void updateUIAfterFetchEntries() {
        router.gotoLoginActivity(Splash.this, 0);
        finish();
    }

    @AfterViews
    protected void setupViews() {
        float scale = (XHDPI_WIDTH / displayMetrics.widthPixels) * displayMetrics.scaledDensity;

        float imageWidth = getResources().getDrawable(R.drawable.splash_handelsbanken)
                .getIntrinsicWidth() / displayMetrics.scaledDensity;
        float imageHeight = getResources().getDrawable(R.drawable.splash_handelsbanken)
                .getIntrinsicHeight() / displayMetrics.scaledDensity;

        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, imageWidth / scale,
                displayMetrics);
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, imageHeight / scale,
                displayMetrics);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) width, (int) height);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        textImage.setLayoutParams(params);

        // startSplashIfnotAlreadyStarted();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            Splash.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
