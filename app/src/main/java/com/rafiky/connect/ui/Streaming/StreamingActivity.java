package com.rafiky.connect.ui.Streaming;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.Connection;
import com.opentok.android.OpentokError;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.orhanobut.logger.Logger;
import com.rafiky.connect.R;
import com.rafiky.connect.base.BaseActivity;
import com.rafiky.connect.model.requestmodel.SignalModel;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;
import com.rafiky.connect.model.responsemodel.UserUpdateDetailsResponse;
import com.rafiky.connect.sharedpreference.SharedPreferenceData;
import com.rafiky.connect.ui.Language.LanguageActivity;
import com.rafiky.connect.ui.Language.LanguagePresenter;
import com.rafiky.connect.utils.AppUtils;
import com.rafiky.connect.utils.Constants;
import com.rafiky.connect.utils.NetworkUtil;
import com.rafiky.connect.utils.NoInternetDialog;

import org.json.JSONObject;
import org.otwebrtc.NetworkMonitorAutoDetect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class StreamingActivity extends BaseActivity implements StreamingContract.MvpView, NoInternetDialog.RetryListener {

    private Dialog progressDialog;
    private Context mContext;
    private static String TAG = "StreamingActivity";
    private StreamingPresenter streamingPresenter;
    private SharedPreferenceData sharedPreferenceData;
    private String userId = null;
    private static Timer timer = null;

    private ArrayList<Stream> oTStreamString = new ArrayList<Stream>();

    @BindView(R.id.shimmerViewContainer)
    ShimmerFrameLayout mShimmerViewContainer;

    @BindView(R.id.actionToolbar)
    Toolbar toolbar;

    @BindView(R.id.imageview)
    AppCompatImageView imageView;

    @BindView(R.id.bt_language)
    AppCompatButton languageButton;

    private EventResponseModelClass.DataBean eventData;
    private EventResponseModelClass.DataBean.LanguagesBean languageList;
    private EventResponseModelClass.DataBean.RoomsBean.SessionsBean sessionData;
    public Session session = null;
    private Subscriber subscriber = null;
    private JsonObject connectedInterpreter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = StreamingActivity.this;
        streamingPresenter = new StreamingPresenter(this, StreamingActivity.this);
        sharedPreferenceData = new SharedPreferenceData(mContext);
        progressDialog = AppUtils.showProgressDialog(mContext);
        Logger.i(TAG, "Started");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("");
        }
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString(Constants.KEY_LANGUAGE) != null && getIntent().getExtras().getString(Constants.KEY_EVENT_ROOM) != null && getIntent().getExtras().getString(Constants.KEY_EVENT_SESSION) != null) {
                TypeToken<EventResponseModelClass.DataBean> token = new TypeToken<EventResponseModelClass.DataBean>() {
                };
                TypeToken<EventResponseModelClass.DataBean.LanguagesBean> languageToken = new TypeToken<EventResponseModelClass.DataBean.LanguagesBean>() {
                };
                TypeToken<EventResponseModelClass.DataBean.RoomsBean.SessionsBean> room_token = new TypeToken<EventResponseModelClass.DataBean.RoomsBean.SessionsBean>() {
                };
                eventData = new EventResponseModelClass().getData();
                eventData = new Gson().fromJson(getIntent().getStringExtra(Constants.KEY_EVENT_ROOM).toString(), token.getType());
                languageList = new Gson().fromJson(getIntent().getStringExtra(Constants.KEY_LANGUAGE).toString(), languageToken.getType());
                sessionData = new Gson().fromJson(getIntent().getStringExtra(Constants.KEY_EVENT_SESSION).toString(), room_token.getType());
                if (eventData != null && languageList != null && sessionData != null) {
                    onCheckInternetConnection();
                } else {
                    stopShimmer();
                    onShowToast(mContext.getString(R.string.something_went_wrong_text));
                }
            }
        } else {
            stopShimmer();
            onShowToast(mContext.getString(R.string.something_went_wrong_text));
        }

        Glide.with(mContext)
                .asGif()
                .load(R.drawable.audio_visuals)
                .apply(new RequestOptions().transform(new CenterCrop()))
                .placeholder(R.drawable.audio_visuals)
                .into(imageView);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_streaming_layout;
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
            init();
        } else {
            NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
            noInternetDialog.setRetryListener(this);
            noInternetDialog.show();
        }
    }

    public void init() {
        onShowProgress();
        if (languageList.getName() != null) {
            languageButton.setText(languageList.getName());
        }
        // open talk configuration
        initializeSession(OpenTokConfig.API_KEY, sessionData.getOpentokSessionId(), sessionData.getOpentokListenerToken());
    }

    private void initializeSession(String apiKey, String sessionId, String token) {
        Log.i(TAG, "apiKey: " + apiKey);
        Log.i(TAG, "sessionId: " + sessionId);
        Log.i(TAG, "token: " + token);
        session = new Session.Builder(this, apiKey, sessionId).build();
        session.setConnectionListener(connectionListener);
        session.setSessionListener(sessionListener);
        session.setSignalListener(signalListener);
        session.setReconnectionListener(reconnectionListener);
        session.connect(token);
    }

    Session.ReconnectionListener reconnectionListener = new Session.ReconnectionListener() {
        @Override
        public void onReconnecting(Session session) {
            Log.d(TAG, "onReconnecting " + session.getSessionId());
        }

        @Override
        public void onReconnected(Session session) {
            Log.d(TAG, "onReconnected" + session.getSessionId());
        }
    };

    Session.SignalListener signalListener = new Session.SignalListener() {
        @Override
        public void onSignalReceived(Session sessionValue, String receivedSignalType, String data, Connection connection) {
            try {
                if (data != null) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject signalType = (JsonObject) jsonParser.parse(data);
                    Log.e("signalType", signalType.toString());
                    Log.e("session", sessionValue.toString());
                    if (signalType.get("role").getAsString().equalsIgnoreCase("interpreter")) {
                        Log.e("role_1_if", "interpreter");
                        if (signalType.get("method").getAsString().equalsIgnoreCase("userJoined") || signalType.get("method").getAsString().equalsIgnoreCase("userExist")) {
                            Log.e("method_1_if", signalType.get("method").getAsString());
                            Log.e("language_1_if", signalType.get("language").getAsString());
                            Log.e("language_2_if", languageList.getName());
                            if (signalType.get("language").getAsString().equalsIgnoreCase(languageList.getName())) {
                                Log.d(TAG, "join_secssion");
                                Log.e("language_2_if", signalType.get("language").getAsString());
                                connectedInterpreter = signalType; //Data stored locally to compare in Else condition
                                Log.e("streamLength", String.valueOf(oTStreamString.size()));
                                for (Stream streamItem : oTStreamString) {
                                    Log.e("streamItem", streamItem.toString());
                                    ArrayList<String> streamName = new ArrayList<>(Arrays.asList(streamItem.getName().split("_")));
                                    Log.e("streamName_1", streamName.toString());
                                    String role = streamName.get(0).toLowerCase();
                                    String userIdStream = streamName.get(1);
                                    Log.e("role_1", role);
                                    Log.e("userIdStream_1", userIdStream);
                                    if (userIdStream.equalsIgnoreCase(signalType.get("userId").getAsString())) {
                                        subscribeStream(streamItem);
                                    } else {
                                        if (subscriber != null && role.equals("moderator")) {
                                            Log.d(TAG, "no need of subscription");
                                        }
                                    }
                                }
                            } else {
                                Log.e("signalType.userid_2", signalType.get("userId").getAsString());
                                Log.e("userid_ifff", userId);
                                if (connectedInterpreter != null && connectedInterpreter.get("userId").getAsString() != null) {
                                    Log.e("connectedInterpreter.getUserid", connectedInterpreter.get("userId").getAsString());
                                    Log.e("signalType.userid", signalType.get("userId").getAsString());
                                    if (connectedInterpreter.get("userId").getAsString().equalsIgnoreCase(signalType.get("userId").getAsString())) {
                                        Log.d(TAG, "unsubscribe");
                                        if (subscriber != null)
                                            if (signalType.get("userId").getAsString().equalsIgnoreCase(connectedInterpreter.get("userId").getAsString())) {
                                                Log.d(TAG, "unsubscribe");
                                                session.unsubscribe(subscriber);
                                            }

                                    } else {
                                        Log.d(TAG, "ignore");
                                    }
                                }

                            }
                        }

                    } else if (signalType.get("role").getAsString().equalsIgnoreCase("moderator")) { // user exist api
                        if (signalType.get("method").getAsString().equalsIgnoreCase("userJoined")) {
                            sendUserExist(session);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    Session.ConnectionListener connectionListener = new Session.ConnectionListener() {
        @Override
        public void onConnectionCreated(Session session, Connection connection) {
            Log.d(TAG, "onConnectionCreated: Connection created " + session.getSessionId());
        }

        @Override
        public void onConnectionDestroyed(Session session, Connection connection) {
            Log.d(TAG, "onConnectionDestroyed: Connection Destroyed " + session.getSessionId());
        }
    };

    private Session.SessionListener sessionListener = new Session.SessionListener() {
        @Override
        public void onConnected(Session session) {
            Log.d(TAG, "onConnected: Connected to session: " + session.getSessionId());
            //Update user details to the server
            onHideProgress();
            onShowToast(getString(R.string.onconnect_msg));
            streamingPresenter.updateListnerDetails(languageList.getName(), eventData.getEventId());
        }

        @Override
        public void onDisconnected(Session session) {
            Log.d(TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
            onHideProgress();
            stopTimer(); // cancel TIMER
        }

        @Override
        public void onStreamReceived(Session session, Stream stream) {
            onHideProgress();
            try {
                Log.d(TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());
                if (!oTStreamString.contains(stream)) {
                    oTStreamString.add(stream);
                }
                Log.e("streamItem", stream.toString());
                String[] streamRole = stream.getName().split("_");
                String role = streamRole[0].toLowerCase();
                Log.e("streamItem_1_role", role);
                if (languageList.getName().equalsIgnoreCase("original")) {
                    Log.e("languageList+++", languageList.getName());
                    if (role.equals("moderator") || role.equals("speaker")) {
                        subscribeStream(stream);
                    }
                } else {
                    if (role.equals("interpreter")) {
                        Log.e(TAG, "initialsignaling_from_onStreamReceived");
                        initialSignaling(session);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStreamDropped(Session session, Stream stream) {
            onHideProgress();
            Log.d(TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());
        }

        @Override
        public void onError(Session session, OpentokError opentokError) {
            onHideProgress();
            Log.e(TAG, "Session error: " + opentokError.getMessage()+""+opentokError.getErrorCode());
            if(opentokError.getErrorCode().toString().equalsIgnoreCase("ConnectionDropped")){
                Toast.makeText(StreamingActivity.this, opentokError.getMessage().toString(), Toast.LENGTH_LONG).show();
                if (!NetworkUtil.isConnected(mContext)) {
                    NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
                    noInternetDialog.setRetryListener(StreamingActivity.this);
                    noInternetDialog.show();
                }
            }
        }
    };

    private void initialSignaling(Session session) {
        SignalModel signalModel = new SignalModel();
        signalModel.setMethod("userJoined");
        signalModel.setUserName(sharedPreferenceData.getString(Constants.KEY_USER_NAME));
        signalModel.setUserId(userId);
        signalModel.setAudio(false);
        signalModel.setVideo(false);
        signalModel.setSpeakerMode(false);
        signalModel.setRaiseHanded(false);
        signalModel.setRole("listener");
        signalModel.setSpeaker(false);
        signalModel.setFromLanguage("");
        signalModel.setLanguage("Floor");
        signalModel.setSigned(false);
        try {
            Log.e("initialSignaling", new Gson().toJson(signalModel).toString());
            session.sendSignal("", new Gson().toJson(signalModel).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUserExist(Session session) {
        SignalModel signalModel = new SignalModel();
        signalModel.setMethod("userExist");
        signalModel.setUserName(sharedPreferenceData.getString(Constants.KEY_USER_NAME));
        signalModel.setUserId(userId);
        signalModel.setAudio(false);
        signalModel.setVideo(false);
        signalModel.setSpeakerMode(false);
        signalModel.setRaiseHanded(false);
        signalModel.setRole("listener");
        signalModel.setSpeaker(false);
        signalModel.setFromLanguage("");
        signalModel.setLanguage("Floor");
        signalModel.setSigned(false);
        try {
            Log.e("sendUserExist", new Gson().toJson(signalModel).toString());
            session.sendSignal("", new Gson().toJson(signalModel).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subscribeStream(Stream streamValue) {
        Log.e(TAG, "streamValue: " + streamValue);
        if (streamValue != null) {
            subscriber = new Subscriber.Builder(StreamingActivity.this, streamValue).build();
            subscriber.setSubscribeToAudio(true);
            subscriber.setSubscribeToVideo(false);
            subscriber.setSubscriberListener(subscriberListener);
            subscriber.setStreamListener(streamListener);
            session.subscribe(subscriber);
        }
    }

    SubscriberKit.StreamListener streamListener = new SubscriberKit.StreamListener() {
        @Override
        public void onReconnected(SubscriberKit subscriberKit) {
            Log.e(TAG, "SubscriberKit_onReconnected");
        }

        @Override
        public void onDisconnected(SubscriberKit subscriberKit) {
            Log.e(TAG, "SubscriberKit_onDisconnected");
            if (!NetworkUtil.isConnected(mContext)) {
                onShowToast(mContext.getResources().getString(R.string.no_internet));
            }
        }
    };


    SubscriberKit.SubscriberListener subscriberListener = new SubscriberKit.SubscriberListener() {
        @Override
        public void onConnected(SubscriberKit subscriberKit) {
            Log.d(TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
            Log.e(TAG, "initialsignaling_from_onConnected");
            initialSignaling(session);
        }

        @Override
        public void onDisconnected(SubscriberKit subscriberKit) {
            Log.d(TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
            stopTimer(); // cancel TIMER
        }

        @Override
        public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
            Log.e(TAG, "SubscriberKit onError: " + opentokError.getMessage());
            onShowAlertDialog("Error", opentokError.getMessage().toString(), false);
            stopTimer(); // cancel TIMER
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (session != null) {
            session.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session != null) {
            session.onResume();
        }
    }


    @Override
    public void onRetry() {
        init();
    }

    @Override
    public void onBackPressed() {
        disConnect();
    }

    @OnClick(R.id.bt_language)
    public void languangeChange() {
        disConnectStream();
        super.onBackPressed();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick({R.id.backbutton, R.id.bt_disconect})
    public void disConnect() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(R.string.app_tittle_string);
        dialog.setMessage("Do you want to Disconnect?");
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.yes_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                disConnectStream();
                languangeChange();
                ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(R.string.no_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void disConnectStream() {
        if (session != null) {
            Log.e("session,", "disConnectStream");
            session.disconnect();
            if (subscriber != null) {
                session.unsubscribe(subscriber);
            }
            stopTimer();
        }
    }


    @Override
    public void onSuccsessListerDetails(UserUpdateDetailsResponse.DataBean userData) {
        if (userData != null) {
            userId = userData.getId();
            Log.d(TAG, "USER_ID" + userId);
            timeUpdate();
        }
    }

    //Update END-TIME Intervel
    private void timeUpdate() {
        timer = new Timer("TIME UPDATE");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // do your task here
                Log.e("TIMER_WORKS", ">>>>>");
                streamingPresenter.onUpdateTime(userId);
            }
        }, 0, Constants.TIME_INTERVAL);
    }

    private void stopTimer() {
        if (timer != null)
            timer.cancel();
        Log.e("TIMER_CANCELLED", "<<<<<<");
    }


}
