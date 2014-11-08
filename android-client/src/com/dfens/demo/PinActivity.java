package com.dfens.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@NoTitle
@EActivity(R.layout.activity_pin)
public class PinActivity extends Activity {

    private static final int PIN_CODE_SIZE = 4;

    @ViewById(R.id.pin_layout)
    View pinLayout;

    @ViewById(R.id.pin_code_text)
    TextView pinCodeTextView;

    List<Integer> enteredPINCode = new ArrayList<Integer>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Click(R.id.username_ok_button)
    public void onClickOkButton(View button) {
        pinLayout.setVisibility(View.VISIBLE);
    }

    public void clickPINKeyboard(View button) {
        switch (button.getId()) {
            case R.id.button_enter:
                if (enteredPINCode.size() == 4) {
                    startActivity(new Intent(this, DeviceListActivity_.class));
                    finish();
                }
            case R.id.button_del:
                if (enteredPINCode.size() > 0) {
                    enteredPINCode.remove(enteredPINCode.size() - 1);
                }
                break;
            case R.id.button_0:
                addPIN(0);
                break;
            case R.id.button_1:
                addPIN(1);
                break;
            case R.id.button_2:
                addPIN(2);
                break;
            case R.id.button_3:
                addPIN(3);
                break;
            case R.id.button_4:
                addPIN(4);
                break;
            case R.id.button_5:
                addPIN(5);
                break;
            case R.id.button_6:
                addPIN(6);
                break;
            case R.id.button_7:
                addPIN(7);
                break;
            case R.id.button_8:
                addPIN(8);
                break;
            case R.id.button_9:
                addPIN(9);
                break;
        }

        StringBuilder buf = new StringBuilder();
        for (int a = 0; a < enteredPINCode.size(); a++) {
            buf.append("*");
        }
        pinCodeTextView.setText(buf.toString());
    }

    private void addPIN(int number) {
        if (enteredPINCode.size() < PIN_CODE_SIZE) {
            enteredPINCode.add(number);
        }
    }

}
