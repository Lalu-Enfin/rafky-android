package com.rafiky.connect.ui.Event;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.rafiky.connect.R;
import com.rafiky.connect.base.MvpBase;
import com.rafiky.connect.data.ApiClient;
import com.rafiky.connect.data.WebApiListener;
import com.rafiky.connect.model.requestmodel.EventRequestModelClass;
import com.rafiky.connect.model.responsemodel.ErrorModel;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;
import com.rafiky.connect.sharedpreference.SharedPreferenceData;
import com.rafiky.connect.ui.Login.LoginContract;
import com.rafiky.connect.utils.AppUtils;
import com.rafiky.connect.utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPresenter implements EventContract.Presenter{

    private Context mContext ;
    private EventContract.MvpView mvpView ;
    private SharedPreferenceData sharedPreferenceData;
    private static String TAG = "EventPresenter";

    public EventPresenter(EventContract.MvpView mvpView,Context mContext ) {
        this.mContext = mContext;
        this.mvpView = mvpView;
        this.sharedPreferenceData = new SharedPreferenceData(mContext);
    }

    @Override
    public void onGetEventDetails(String username, String email, String meetingid) {
        //mvpView.onShowProgress();
        mvpView.showShimmer();
        WebApiListener apiListenr = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        EventRequestModelClass requestModelClass = new EventRequestModelClass();
        requestModelClass.setUser_name(username);
        requestModelClass.setUser_email(email);
        requestModelClass.setEvent_code(meetingid);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestModelClass));
        Call<String> call = apiListenr.getEventData(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code() == Constants.SUCCESS_CODE) {
                        if (response.body() != null) {
                            Log.e("onGetEventDetails_response : ", response.body());
                            Logger.e(String.valueOf(response));
                            if (new JsonParser().parse(response.body()).getAsJsonObject().has("error")) {
                                mvpView.onShowErrorDialog(mContext.getString(R.string.app_tittle_string), mContext.getString(R.string.error_room_not_found), true);
                                sharedPreferenceData.deleteAllPreference();
                            } else {
                                Type listType = new TypeToken<EventResponseModelClass>() {}.getType();
                                EventResponseModelClass userProfileModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (userProfileModel.getStatus().equals(Constants.SUCCESS)) {
                                    if (userProfileModel.getData() != null) {
                                        sharedPreferenceData.setString(Constants.KEY_USER_NAME, username);
                                        sharedPreferenceData.setString(Constants.KEY_USER_EMAIL, email);
                                        sharedPreferenceData.setString(Constants.KEY_MEETING_ID, meetingid);
                                        mvpView.onSucessEventData(userProfileModel.getData());
                                    }
                                }
                            }
                            mvpView.onHideProgress();
                        }

                    } else if (response.code() == Constants.ERROR_CODE) {
                        handleErrorResponse(response.errorBody(), false);
                    } else if (response.code() == Constants.ERROR_BAD_GATEWAY_CODE) {
                        handleErrorResponse(response.errorBody(), false);
                    } else if (response.code() == Constants.ERROR_UNAUTHORIZED_CODE) {
                        handleErrorResponse(response.errorBody(), true);
                    } else
                        handleErrorResponse(response.errorBody(), false);
                } catch (JsonSyntaxException e) {
                    Log.e("Exception : ", e.getMessage());
                    mvpView.onHideProgress();
                    // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mvpView.onShowErrorDialog(mContext.getString(R.string.app_tittle_string), mContext.getString(R.string.something_went_wrong_text), false);
                mvpView.onHideProgress();
            }
        });


    }

    @Override
    public boolean isSessionTimeCheck(EventResponseModelClass.DataBean.RoomsBean roomsBean, EventResponseModelClass.DataBean.RoomsBean.SessionsBean sessionsBean)  {
        Boolean returnFlag = false ;

        try {
            Date strDate = AppUtils.getDateFormat(sessionsBean.getSessionStartTime());
            Date endDate = AppUtils.getDateFormat(sessionsBean.getSessionEndTime());
            Date curentDate = AppUtils.getCurrentDate();
            Log.e("strDate", "" + strDate);
            Log.e("endDate", "" + endDate);
            Log.e("curentDate", "" + curentDate);

        if (curentDate.after(strDate) && curentDate.before(endDate)) {
            Log.e("True", "");
            returnFlag = true ;
        }else {
            Log.e("false", "");
            returnFlag = false ;
        }
        }catch (Exception e){
            Log.e("Exception", String.valueOf(e));
        }
        return returnFlag;
    }

    private void handleErrorResponse(ResponseBody errorBody, boolean isForceLogout) {
        if (errorBody != null) {
            try {
                Logger.i(TAG, "handleErrorResponse" + errorBody.toString());
                mvpView.onHideProgress();
                Type listType = new TypeToken<ErrorModel>() {
                }.getType();
                ErrorModel errorModel = new GsonBuilder().create().fromJson(errorBody.string(), listType);
                String title = Constants.ERROR_TITLE;
                mvpView.onShowAlertDialog(title, errorModel.getMessage(), isForceLogout);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
