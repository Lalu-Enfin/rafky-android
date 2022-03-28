package com.rafiky.connect.ui.Splashscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.rafiky.connect.ui.Event.EventActivity;
import com.rafiky.connect.ui.Login.LoginActivity;
import com.rafiky.connect.utils.AppUtils;
import com.rafiky.connect.utils.Constants;

public class SplashScreenPresenter implements SplashScreenContract.Presenter{
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private static final String TAG = "SplashScreenPresenter";
    private SplashScreenContract.MvpView mvpView ;
    private Context mContext;

    public SplashScreenPresenter(SplashScreenContract.MvpView mvpView, Context context) {
        this.mContext = context ;
        this.mvpView = mvpView ;
    }
    @Override
    public void onStartSplashTimer() {
        Logger.i(TAG,"onStartSplashTimer - Executed");
        /* New Handler to start the Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(() -> {
            Logger.i(TAG,"Splash screen timer completed");
            mvpView.onSplashTimerCompleted();
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void checkAutoLogin() {
        Logger.i(TAG,"checkAutoLogin - Executed");
        if (AppUtils.isUserLoggedIn(mContext)){
            Intent intent = new Intent(mContext, EventActivity.class);
            intent.putExtra(Constants.KEY_LOGIN_USER,"userLogined");
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else {
            Intent loginIntent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(loginIntent);
            ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        mvpView.onFinishActivity();
    }

    @Override
    public void onCheckAppUpdate() {
        mvpView.onUpdateCheckCompleted();
    }
}
