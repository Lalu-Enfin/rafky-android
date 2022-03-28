package com.rafiky.connect.ui.Language;

import android.content.Context;

import com.rafiky.connect.sharedpreference.SharedPreferenceData;
import com.rafiky.connect.ui.Event.EventContract;

public class LanguagePresenter implements LanguageContract.Presenter{

    private Context mContext ;
    private LanguageContract.MvpView mvpView ;
    private SharedPreferenceData sharedPreferenceData;
    private static String TAG = "LanguagePresenter";

    public LanguagePresenter(LanguageContract.MvpView mvpView,Context mContext) {
        this.mContext = mContext;
        this.mvpView = mvpView;
    }
}
