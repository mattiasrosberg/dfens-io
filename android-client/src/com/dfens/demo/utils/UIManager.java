package com.dfens.demo.utils;

import android.app.*;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dfens.demo.R;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

import java.util.List;

/**
 * Manages the UI holding common UI components.
 */

@EBean
public class UIManager {

    private static final String TAG = UIManager.class.getSimpleName();

    public static final int EMPTY_STRING = -1;

    public static final int PROGRESS_DIALOG = 0;

    public static final int OK_DIALOG = 1;

    public static final int OK_CANCEL_DIALOG = 2;

    private TextView searchIcon;

    private LinearLayout llTitleBar;

    private TextView tvTitle;

    private Activity activity;

    private Context context;

    private String dialogTitle = null;

    private String dialogMessage = null;

    private String dialogPositiveButtonText = null;

    private String dialogNegativeButtonText = null;

    private boolean dialogCancelable = false;

    private OnClickListener dialogOkListener = null;

    private OnClickListener dialogCancelListener = null;

    private OnCancelListener preloaderCancelListener = null;

    private Dialog progressDialog;


    @SystemService
    ActivityManager activityManager;

    public UIManager(Context context) {
        this.context = context;
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void prepareOkDialog(int title, int message, OnClickListener listener) {
        dialogTitle = title < 0 ? null : getString(title);
        dialogMessage = message < 0 ? null : getString(message);
        dialogOkListener = listener;
    }

    public void prepareOkDialog(String title, String message, OnClickListener listener) {
        dialogTitle = title;
        dialogMessage = message;
        dialogOkListener = listener;
    }

    public void prepareOkDialog(int title, String message, OnClickListener listener) {
        dialogTitle = title < 0 ? null : getString(title);
        dialogMessage = message;
        dialogOkListener = listener;
    }

    public void prepareOkDialog(String title, int message, OnClickListener listener) {
        dialogTitle = title;
        dialogMessage = message < 0 ? null : getString(message);
        dialogOkListener = listener;
    }

    public void prepareOkCancelDialog(int message,
                                      OnClickListener ok, OnClickListener cancel) {
        dialogTitle = null;
        dialogMessage = message < 0 ? null : getString(message);
        dialogPositiveButtonText = null;
        dialogOkListener = ok;
        dialogCancelListener = cancel;
    }

    public void prepareOkCancelDialog(int title, int message, OnClickListener ok,
                                      OnClickListener cancel) {
        dialogTitle = title < 0 ? null : getString(title);
        dialogMessage = message < 0 ? null : getString(message);
        dialogPositiveButtonText = null;
        dialogOkListener = ok;
        dialogCancelListener = cancel;
    }

    public void prepareOkCancelDialog(int title, String message, OnClickListener ok,
                                      OnClickListener cancel) {
        dialogTitle = title < 0 ? null : getString(title);
        dialogMessage = message;
        dialogPositiveButtonText = null;
        dialogOkListener = ok;
        dialogCancelListener = cancel;
    }

    public void prepareOkCancelDialog(int title, int message, int positiveButtonText,
                                      OnClickListener ok, OnClickListener cancel) {
        dialogTitle = title < 0 ? null : getString(title);
        dialogMessage = message < 0 ? null : getString(message);
        dialogPositiveButtonText = positiveButtonText < 0 ? null : getString(positiveButtonText);
        dialogOkListener = ok;
        dialogCancelListener = cancel;
    }

    public void preparePreloader(int title, int message, boolean cancelable,
                                 OnCancelListener listener) {
        dialogTitle = title < 0 ? null : getString(title);
        dialogMessage = message < 0 ? null : getString(message);
        dialogCancelable = cancelable;
        preloaderCancelListener = listener;
    }

    public void preparePreloader(String title, int message, boolean cancelable,
                                 OnCancelListener listener) {
        dialogTitle = title;
        dialogMessage = message < 0 ? null : getString(message);
        dialogCancelable = cancelable;
        preloaderCancelListener = listener;
    }

    public void preparePreloader(int title, String message, boolean cancelable,
                                 OnCancelListener listener) {
        dialogTitle = getString(title);
        dialogMessage = message;
        dialogCancelable = cancelable;
        preloaderCancelListener = listener;
    }

    public void showDialogIfPrimaryTask(int dialogId) {
        List<RunningTaskInfo> tasks = activityManager.getRunningTasks(10);

        // SimpleName of top task
        String simpleName = tasks.get(0).topActivity.getShortClassName().substring(
                tasks.get(0).topActivity.getShortClassName().lastIndexOf('.') + 1,
                tasks.get(0).topActivity.getShortClassName().length());

        // Parent Activity

        String parentActivitySimpleName = null;
        if (activity.getParent() != null) {
            parentActivitySimpleName = activity.getParent().getClass().getSimpleName();
        }

        if (tasks.get(0).topActivity.getPackageName().equals(activity.getPackageName())
                && simpleName.equals(activity.getClass().getSimpleName())) {
            activity.showDialog(dialogId);
        } else if (parentActivitySimpleName != null
                && tasks.get(0).topActivity.getPackageName().equals(activity.getPackageName())
                && simpleName.equals(parentActivitySimpleName)) {
            activity.showDialog(dialogId);
        }
    }

    private void showIfActivityAlive(Dialog d) {
        if (activity != null && !activity.isFinishing()) {
            d.show();
        } else {
            Log.d(TAG, "===============>NOT SHOWING A DIALOG BECAUSE ACTIVITY IS NOT ALIVE...");
        }
    }

//    public Dialog showOkDialog(String title, String message) {
//        dialogTitle = title;
//        dialogMessage = message;
//        dialogOkListener = new DismissButtonListener();
//        Dialog d = getOkDialog();
//        showIfActivityAlive(d);
//        return d;
//    }

    public Dialog showOkDialog(String title, String message, OnClickListener dialogOkListener) {
        dialogTitle = title;
        dialogMessage = message;
        this.dialogOkListener = dialogOkListener;
        Dialog d = getOkDialog();
        showIfActivityAlive(d);
        return d;
    }

//    public Dialog showOkDialogAndFinish(String title, String message, Activity activity) {
//        dialogTitle = title;
//        dialogMessage = message;
//        dialogOkListener = new DismissAndFinishListener(activity);
//        Dialog d = getOkDialog();
//        showIfActivityAlive(d);
//        return d;
//    }

    public void showProgressDialog(String title, String message, Activity activity,
                                   boolean cancelable, OnCancelListener cancelListener) {
        dialogTitle = title;
        dialogMessage = message;
        dialogOkListener = null;
        dialogCancelable = cancelable;
        preloaderCancelListener = cancelListener;
        getPreloader().show();
    }

    public void showProgressDialog(final boolean cancelable, final OnCancelListener cancelListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                dialogOkListener = null;
                dialogCancelable = cancelable;
                preloaderCancelListener = cancelListener;
                createProgressDialog();
            }
        });
    }

    public void showGenericError(final OnClickListener dialogOkListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                showOkDialog("Message",
                        "An error occured", dialogOkListener);
            }
        });
    }

    public void dismissProgressDialog() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        //Just ignore exceptions, because they are caused by the activity being destroyed in the interim.
                        //It would of course be even better if we handled this, keeping track of the activity.
                    }
                    progressDialog = null;
                }
            }
        });

    }

    private Dialog getOkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(dialogTitle)) {
            builder.setTitle(dialogTitle);
        }
        if (!TextUtils.isEmpty(dialogMessage)) {
            builder.setMessage(dialogMessage);
        }
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", dialogOkListener);
        return builder.create();
    }

    private Dialog getOkCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(dialogTitle)) {
            builder.setTitle(dialogTitle);
        }
        if (!TextUtils.isEmpty(dialogMessage)) {
            builder.setMessage(dialogMessage);
        }
        builder.setCancelable(false);
        builder.setPositiveButton(dialogPositiveButtonText != null ? dialogPositiveButtonText
                : "Ok", dialogOkListener);
        builder.setNegativeButton(dialogNegativeButtonText != null ? dialogNegativeButtonText
                : "Cancel", dialogCancelListener);
        return builder.create();
    }

    private void createProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new Dialog(activity, R.style.Dfens_ProgressDialog) {

            };
            progressDialog.setCancelable(dialogCancelable);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(preloaderCancelListener);
            progressDialog.setContentView(new ProgressBar(activity),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
            );

            progressDialog.show();
        }
    }

    private Dialog getPreloader() {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(true);
        if (!TextUtils.isEmpty(dialogTitle)) {
            dialog.setTitle(dialogTitle);
        }
        if (!TextUtils.isEmpty(dialogMessage)) {
            dialog.setMessage(dialogMessage);
        }
        dialog.setCancelable(dialogCancelable);
        dialog.setOnCancelListener(preloaderCancelListener);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void prepareMapInfoDialog(OnClickListener ok) {
        dialogOkListener = ok;
    }

    public Dialog getDialog(int id) {
        Dialog d = null;
        switch (id) {
            case UIManager.PROGRESS_DIALOG:
                d = getPreloader();
                break;
            case UIManager.OK_DIALOG:
                d = getOkDialog();
                break;
            case UIManager.OK_CANCEL_DIALOG:
                d = getOkCancelDialog();
                break;
            default:
                break;
        }
        resetDialogValues();
        return d;
    }

    private void resetDialogValues() {
        dialogTitle = null;
        dialogMessage = null;
        dialogCancelable = false;
        dialogOkListener = null;
        dialogCancelListener = null;
        preloaderCancelListener = null;
    }

    private String getString(int resId) {
        return context.getString(resId);
    }

    private View findViewById(int id) {
        return activity.findViewById(id);
    }


    /**
     * Utility method for enabling sub pixel hinting on text views.
     * <p/>
     * Due to issues with Android 4.0.4 on Xperia P (lt22i) we've disabled this for
     * ICS 4.0.3. This will result in worse text, but it fixes issues with characters disappearing.
     *
     * @param paint The paint instance belonging to a TextView.
     */
    public static void enableSubpixelText(Paint paint) {
        //Check for Android 4.0.3 (or 4.0.4) running on Sony devices, since these seems to be the problematic ones.
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 || !Build.MANUFACTURER.equals("Sony")) {
            paint.setSubpixelText(true);
        }
    }

    /**
     * Utility method for enabling sub pixel hinting on text views.
     * <p/>
     * Due to issues with Android 4.0.4 on Xperia P (lt22i) we've disabled this for
     * ICS 4.0.3. This will result in worse text, but it fixes issues with characters disappearing.
     *
     * @param textView A textview.
     */
    public static void enableSubpixelText(TextView textView) {
        //Check for Android 4.0.3 (or 4.0.4) running on Sony devices, since these seems to be the problematic ones.
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 || !Build.MANUFACTURER.equals("Sony")) {
            //Note that we're setting the flags on the View instead of accessing the Paint directly.
            //The reason is that the latter will also make the view invalidate it's drawing and layout cache.
            textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
