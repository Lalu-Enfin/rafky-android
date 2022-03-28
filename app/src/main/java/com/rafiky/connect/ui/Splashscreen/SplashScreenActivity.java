package com.rafiky.connect.ui.Splashscreen;


import android.content.Context;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.rafiky.connect.R;
import com.rafiky.connect.base.BaseActivity;
import com.rafiky.connect.sharedpreference.SharedPreferenceData;
import com.rafiky.connect.utils.NetworkUtil;
import com.rafiky.connect.utils.NoInternetDialog;

public class SplashScreenActivity extends BaseActivity implements SplashScreenContract.MvpView, NoInternetDialog.RetryListener {
    private SplashScreenPresenter splashPresenter;
    private String TAG = "SplashScreenActivity";
    private Context mContext;
    private boolean isActivityPaused;
    private SharedPreferenceData sharedPreferenceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SplashScreenActivity.this;
        splashPresenter = new SplashScreenPresenter(this, SplashScreenActivity.this);
        Logger.e(TAG, "STARTED");
        sharedPreferenceData = new SharedPreferenceData(mContext);
        onCheckInternetConnection();
    }

    public void init() {
        splashPresenter.onStartSplashTimer();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_splashscreen_layout;
    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }

    @Override
    public void onShowToast(String message) {

    }

    @Override
    public void onShowSnackBar(String message) {

    }

    @Override
    public void onShowAlertDialog(String title, String message, boolean isLoginRequired) {

    }

    @Override
    public void onCheckInternetConnection() {
        if (NetworkUtil.isConnected(mContext))
            init();
        else {
            NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
            noInternetDialog.setRetryListener(this);
            noInternetDialog.show();
        }
    }

    @Override
    public void onRetry() {
        onCheckInternetConnection();
    }

    @Override
    public void onSplashTimerCompleted() {
        Logger.i(TAG, "onSplashTimerCompleted - Executed");
        splashPresenter.onCheckAppUpdate();
    }

    @Override
    public void onFinishActivity() {
        this.finish();
    }

    @Override
    public void onUpdateCheckCompleted() {
        splashPresenter.checkAutoLogin();
    }

    @Override
    public void onShowUpdateDialog(boolean isForceUpdate, boolean isForceLogout, String message) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityPaused =  true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isActivityPaused){
            splashPresenter.onCheckAppUpdate();
            isActivityPaused = false;
        }
    }
}
