package com.rafiky.connect.ui.Event;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.rafiky.connect.R;
import com.rafiky.connect.base.BaseActivity;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;
import com.rafiky.connect.sharedpreference.SharedPreferenceData;
import com.rafiky.connect.ui.Language.LanguageActivity;
import com.rafiky.connect.ui.Login.LoginActivity;
import com.rafiky.connect.utils.AppUtils;
import com.rafiky.connect.utils.Constants;
import com.rafiky.connect.utils.NetworkUtil;
import com.rafiky.connect.utils.NoInternetDialog;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EventActivity extends BaseActivity implements EventContract.MvpView, NoInternetDialog.RetryListener, SwipeRefreshLayout.OnRefreshListener {

    private Dialog progressDialog;
    private Context mContext;
    private static String TAG = "EventActivity";
    private EventPresenter eventPresenter;


    @BindView(R.id.actionToolbar)
    Toolbar toolbar;

    @BindView(R.id.expandblelist)
    ExpandableListView expandableListView;

    @BindView(R.id.shapableiv)
    AppCompatImageView imageviewLogo;

    @BindView(R.id.shimmerViewContainer)
    ShimmerFrameLayout mShimmerViewContainer;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    private EventExpandableListView adpter;

    private EventResponseModelClass.DataBean eventData;

    private SharedPreferenceData sharedPreferenceData;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = EventActivity.this;
        swipeRefreshLayout.setOnRefreshListener(this);
        eventPresenter = new EventPresenter(this, EventActivity.this);
        sharedPreferenceData = new SharedPreferenceData(mContext);
        progressDialog = AppUtils.showProgressDialog(mContext);
        Logger.i(TAG, "Started");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("Events");
        }
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString(Constants.KEY_EVENTDATA) != null) {
                TypeToken<EventResponseModelClass.DataBean> token = new TypeToken<EventResponseModelClass.DataBean>() {};
                eventData = new EventResponseModelClass().getData();
                eventData = new Gson().fromJson(getIntent().getStringExtra(Constants.KEY_EVENTDATA).toString(), token.getType());
                List<EventResponseModelClass.DataBean.RoomsBean> roomsBeanList = eventData.getRooms();
                if (roomsBeanList != null && !roomsBeanList.isEmpty()) {
                    init(roomsBeanList);
                }
            } else if (getIntent().getExtras().getString(Constants.KEY_LOGIN_USER) != null) {
                //logined user
                if (AppUtils.isUserLoggedIn(mContext)) {
                    String userName = sharedPreferenceData.getString(Constants.KEY_USER_NAME);
                    String userEmail = sharedPreferenceData.getString(Constants.KEY_USER_EMAIL);
                    String meetingId = sharedPreferenceData.getString(Constants.KEY_MEETING_ID);
                    eventPresenter.onGetEventDetails(userName, userEmail, meetingId);
                } else {
                    stopShimmer();
                    onShowToast(mContext.getString(R.string.something_went_wrong_text));
                    logout();
                }
            }
        } else {
            stopShimmer();
            onShowToast(mContext.getString(R.string.something_went_wrong_text));
            logout();
        }
    }

    @Override
    public void onShowErrorDialog(String title, String message, boolean isFinishActivity) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_alert_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        // set the custom dialog components - text and button
        TextView heading = dialog.findViewById(R.id.titleTextView);
        if (title.equals(""))
            heading.setVisibility(View.GONE);
        TextView error = dialog.findViewById(R.id.errorTextView);
        heading.setText(title);
        error.setText(message);
        Button cancelButton = dialog.findViewById(R.id.okButton);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(v -> {
            if (isFinishActivity) {
                sharedPreferenceData.deleteAllPreference();
                this.finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                dialog.dismiss();
            } else {
                this.finish();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onSucessEventData(EventResponseModelClass.DataBean data) {
        Logger.e("onSucessEventData", data);
        if (data != null) {
            TypeToken<EventResponseModelClass.DataBean> token = new TypeToken<EventResponseModelClass.DataBean>() {};
            eventData = new EventResponseModelClass().getData();
            eventData = new Gson().fromJson(new Gson().toJson(data), token.getType());
            List<EventResponseModelClass.DataBean.RoomsBean> roomsBeanList = eventData.getRooms();
            if (roomsBeanList != null && !roomsBeanList.isEmpty()) {
                init(roomsBeanList);
            }
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_event_layout;
    }

    @Override
    public void showShimmer() {
        if (mShimmerViewContainer != null)
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
        /*Snackbar.make(parent, message, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.red))
                .setAction(R.string.snackbar_ok_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();*/

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

    @OnClick({R.id.logout_button})
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (!EventActivity.this.isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.logout_title)
                    .setMessage(R.string.logut_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                        }
                    })
                    .setNegativeButton(R.string.no_text, null)
                    .show();
        }
    }

    public void logout() {
        sharedPreferenceData.deleteAllPreference();
        Intent intent = new Intent(EventActivity.this, LoginActivity.class);
        startActivity(intent);
        EventActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void init(List<EventResponseModelClass.DataBean.RoomsBean> roomsBeanList) {
        adpter = new EventExpandableListView(mContext, roomsBeanList);
        if (expandableListView != null)
            expandableListView.setAdapter(adpter);
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });

        //load icon image //
        if (eventData.getEventLogoImage() != null && !eventData.getEventLogoImage().isEmpty() && !eventData.getEventLogoImage().equals("")) {
            Glide.with(mContext).asBitmap()
                    .load(eventData.getEventLogoImage())
                    .placeholder(R.drawable.logo_img)
                    .into(imageviewLogo);
        } else {
            Glide.with(mContext).asBitmap()
                    .load(R.drawable.logo_img)
                    .placeholder(R.drawable.logo_img)
                    .into(imageviewLogo);
        }

        if(roomsBeanList.size() > 3){
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            expandableListView.setLayoutParams(param);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            lp.setMargins(0, 0, 0, 15);
            imageviewLogo.setLayoutParams(lp);

        }else {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.0f
            );
            expandableListView.setLayoutParams(param);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            lp.setMargins(0, 200, 0, 15);
            imageviewLogo.setLayoutParams(lp);
        }

        stopShimmer();

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Logger.i("ChildClicked");
                Log.e("clecked", roomsBeanList.get(groupPosition).getRoomName() + "session" + roomsBeanList.get(groupPosition).getSessions().get(childPosition).getSessionName());
                if (eventPresenter.isSessionTimeCheck(roomsBeanList.get(groupPosition), roomsBeanList.get(groupPosition).getSessions().get(childPosition))) {
                    if (NetworkUtil.isConnected(mContext)) {
                        sharedPreferenceData.setString(Constants.KEY_TEMP_SESSION_ID,"");
                        Intent languageIntent = new Intent(EventActivity.this, LanguageActivity.class);
                        languageIntent.putExtra(Constants.KEY_EVENTDATA_LANGUAGE, new Gson().toJson(eventData));
                        languageIntent.putExtra(Constants.KEY_EVENT_SESSION, new Gson().toJson(roomsBeanList.get(groupPosition).getSessions().get(childPosition)));
                        startActivity(languageIntent);
                        (EventActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }else {
                        NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
                        noInternetDialog.setRetryListener(EventActivity.this);
                        noInternetDialog.show();
                    }
                } else {
                   onShowAlertDialog(getString(R.string.app_tittle_string), getString(R.string.meeting_unavilable_error), false);
                }
                return false;
            }
        });

    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
        if (NetworkUtil.isConnected(mContext)) {
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            //showShimmer();
            //logined user
            if (AppUtils.isUserLoggedIn(mContext)) {
                String userName = sharedPreferenceData.getString(Constants.KEY_USER_NAME);
                String userEmail = sharedPreferenceData.getString(Constants.KEY_USER_EMAIL);
                String meetingId = sharedPreferenceData.getString(Constants.KEY_MEETING_ID);
                eventPresenter.onGetEventDetails(userName, userEmail, meetingId);
            } else {
                stopShimmer();
                onShowToast(mContext.getString(R.string.something_went_wrong_text));
                logout();
            }
        } else {
            NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
            noInternetDialog.setRetryListener(this);
            noInternetDialog.show();
        }
    }
}
