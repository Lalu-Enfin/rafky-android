package com.rafiky.connect.ui.Splashscreen;

import com.rafiky.connect.base.MvpBase;

public interface SplashScreenContract {

    interface  MvpView extends MvpBase {

        void onSplashTimerCompleted();
        void onFinishActivity();
        void onUpdateCheckCompleted();
        void onShowUpdateDialog(boolean isForceUpdate,  boolean isForceLogout, String message);

    }
    interface Presenter{
        void onStartSplashTimer();
        void checkAutoLogin();
        void onCheckAppUpdate();

    }

}
