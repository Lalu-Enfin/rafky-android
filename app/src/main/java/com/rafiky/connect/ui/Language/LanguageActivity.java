package com.rafiky.connect.ui.Language;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.rafiky.connect.R;
import com.rafiky.connect.base.BaseActivity;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;
import com.rafiky.connect.sharedpreference.SharedPreferenceData;
import com.rafiky.connect.ui.Event.EventActivity;
import com.rafiky.connect.ui.Event.EventPresenter;
import com.rafiky.connect.ui.Event.HeadphoneDialog;
import com.rafiky.connect.ui.Streaming.StreamingActivity;
import com.rafiky.connect.utils.AppUtils;
import com.rafiky.connect.utils.Constants;
import com.rafiky.connect.utils.ItemOffsetDecoration;
import com.rafiky.connect.utils.NetworkUtil;
import com.rafiky.connect.utils.NoInternetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity implements LanguageContract.MvpView, NoInternetDialog.RetryListener, LanguageAdapter.OnSelectLanguage, HeadphoneDialog.HeadPhoneListner {

    private ProgressDialog progressDialog;
    private Context mContext;
    private static String TAG = "LanguageActivity";
    private LanguagePresenter presenter;
    private SharedPreferenceData sharedPreferenceData;

    @BindView(R.id.shimmerViewContainer)
    ShimmerFrameLayout mShimmerViewContainer;

    @BindView(R.id.recyclelist)
    RecyclerView languageList;

    @BindView(R.id.actionToolbar)
    Toolbar toolbar;

    private EventResponseModelClass.DataBean eventData;

    private EventResponseModelClass.DataBean.LanguagesBean languagesBean;

    private EventResponseModelClass.DataBean.RoomsBean.SessionsBean sessionData;

    @Override
    public int getLayout() {
        return R.layout.activity_language_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = LanguageActivity.this;
        presenter = new LanguagePresenter(this, LanguageActivity.this);
        sharedPreferenceData = new SharedPreferenceData(mContext);
        Logger.i(TAG, "Started");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("Languages");
        }
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString(Constants.KEY_EVENTDATA_LANGUAGE) != null && getIntent().getExtras().getString(Constants.KEY_EVENT_SESSION) != null) {
                TypeToken<EventResponseModelClass.DataBean> token = new TypeToken<EventResponseModelClass.DataBean>() {
                };
                TypeToken<EventResponseModelClass.DataBean.RoomsBean.SessionsBean> room_token = new TypeToken<EventResponseModelClass.DataBean.RoomsBean.SessionsBean>() {
                };
                eventData = new EventResponseModelClass().getData();
                eventData = new Gson().fromJson(getIntent().getStringExtra(Constants.KEY_EVENTDATA_LANGUAGE).toString(), token.getType());
                sessionData = new Gson().fromJson(getIntent().getStringExtra(Constants.KEY_EVENT_SESSION).toString(), room_token.getType());
                List<EventResponseModelClass.DataBean.LanguagesBean> languagesBeanList = eventData.getLanguages();
                if (languagesBeanList != null && !languagesBeanList.isEmpty()) {
                    if (!languagesBeanList.contains("original")) {
                        EventResponseModelClass.DataBean.LanguagesBean orginalData = new EventResponseModelClass.DataBean.LanguagesBean();
                        orginalData.setName("Original");
                        languagesBeanList.add(0, orginalData);
                    }
                    init(languagesBeanList);
                } else {
                    languagesBeanList = new ArrayList<>();
                    EventResponseModelClass.DataBean.LanguagesBean orginalData = new EventResponseModelClass.DataBean.LanguagesBean();
                    orginalData.setName("Original");
                    languagesBeanList.add(0, orginalData);
                    init(languagesBeanList);
                }
            }
        } else {
            stopShimmer();
            onShowToast(mContext.getString(R.string.something_went_wrong_text));
        }

    }

    private void init(List<EventResponseModelClass.DataBean.LanguagesBean> roomsBeanList) {
        if (!roomsBeanList.isEmpty()) {
            if (languageList != null) {
                languageList.setLayoutManager(new LinearLayoutManager(mContext));
                languageList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                LanguageAdapter adapter = new LanguageAdapter(mContext, roomsBeanList);
                adapter.setListner(this::onSelectedLanguage);
                languageList.setAdapter(adapter);
            }
        }
        stopShimmer();

    }

    @Override
    public void showShimmer() {
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void stopShimmer() {
        if (mShimmerViewContainer != null) {
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowSnackBar(String message) {

    }

    @Override
    public void onShowAlertDialog(String title, String message, boolean isLoginRequired) {
        AppUtils.showDialog(mContext, title, message, isLoginRequired);
    }

    @Override
    public void onCheckInternetConnection() {
        if (NetworkUtil.isConnected(mContext)) {

        } else {
            NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
            noInternetDialog.setRetryListener(this);
            noInternetDialog.show();
        }
    }

    @Override
    public void onRetry() {

    }

    @OnClick(R.id.backbutton)
    @Override
    public void onBackPressed() {
        /*super.onBackPressed();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/

        Intent intent = new Intent(mContext, EventActivity.class);
        intent.putExtra(Constants.KEY_LOGIN_USER, "userLogined");
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onSelectedLanguage(EventResponseModelClass.DataBean.LanguagesBean lanfuageItem) {
        languagesBean = null;
        languagesBean = lanfuageItem;
        if(sharedPreferenceData.getString(Constants.KEY_TEMP_SESSION_ID).equals("") || !sharedPreferenceData.getString(Constants.KEY_TEMP_SESSION_ID).equalsIgnoreCase(sessionData.getOpentokSessionId())){
            new HeadphoneDialog(mContext, this::dialogOkClicked).show();
        }else {
            gotoStreaming();
        }

    }

    @Override
    public void dialogOkClicked() {
        gotoStreaming();
    }

    public void gotoStreaming(){
        if (NetworkUtil.isConnected(mContext)) {
            Intent streamIntent = new Intent(LanguageActivity.this, StreamingActivity.class);
            streamIntent.putExtra(Constants.KEY_LANGUAGE, new Gson().toJson(languagesBean));
            streamIntent.putExtra(Constants.KEY_EVENT_ROOM, new Gson().toJson(eventData));
            streamIntent.putExtra(Constants.KEY_EVENT_SESSION, new Gson().toJson(sessionData));
            startActivity(streamIntent);
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
            noInternetDialog.setRetryListener(this);
            noInternetDialog.show();
        }
    }

}
