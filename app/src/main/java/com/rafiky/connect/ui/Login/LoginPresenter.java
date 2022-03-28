package com.rafiky.connect.ui.Login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.rafiky.connect.R;
import com.rafiky.connect.data.ApiClient;
import com.rafiky.connect.data.WebApiListener;
import com.rafiky.connect.model.requestmodel.EventRequestModelClass;
import com.rafiky.connect.model.responsemodel.ErrorModel;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;
import com.rafiky.connect.sharedpreference.SharedPreferenceData;
import com.rafiky.connect.utils.AppUtils;
import com.rafiky.connect.utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginPresenter implements LoginContract.Presnter {

    private static final String TAG = "LoginPresenter";
    private LoginContract.MvpView mvpView;
    private Context mContext;
    private SharedPreferenceData sharedPreferenceData;

    public LoginPresenter(LoginContract.MvpView mvpView, Context mContext) {
        this.mvpView = mvpView;
        this.mContext = mContext;
        this.sharedPreferenceData = new SharedPreferenceData(mContext);
    }

    @Override
    public void checkLogin() {
        if(AppUtils.isUserLoggedIn(mContext)){
            String userName = sharedPreferenceData.getString(Constants.KEY_USER_NAME);
            String userEmail = sharedPreferenceData.getString(Constants.KEY_USER_EMAIL);
            String meetingId = sharedPreferenceData.getString(Constants.KEY_MEETING_ID);
            mvpView.updateUserDetails(userName,userEmail,meetingId);
        }

    }



    @Override
    public boolean isValid(String userName, String eMail, String meetingId) {
        boolean isValid = true;

        if (userName.equals(" ") || TextUtils.isEmpty(userName.trim()) || eMail.equals(" ") || TextUtils.isEmpty(eMail.trim()) || meetingId.equals(" ") || TextUtils.isEmpty(meetingId.trim())) {
            isValid = false;
            mvpView.onShowSnackBar(mContext.getString(R.string.fill_field_error_msg));
            return isValid;
        }
        if (userName.equals(" ") || TextUtils.isEmpty(userName)) {
            isValid = false;
            mvpView.onShowSnackBar(mContext.getString(R.string.username_empty_error));
        }

        if (eMail.equals(" ") || TextUtils.isEmpty(eMail)) {
            isValid = false;
            mvpView.onShowSnackBar(mContext.getString(R.string.email_empty_erorr));
        } else if (!AppUtils.isValidEmail(eMail)) {
            isValid = false;
            mvpView.onShowSnackBar(mContext.getString(R.string.valid_email_error_msg));
        } else {
            isValid = true;
        }

        if (meetingId.equals(" ") || TextUtils.isEmpty(meetingId)) {
            isValid = false;
            mvpView.onShowSnackBar(mContext.getString(R.string.meetingid_empty_error));
        }

        return isValid;
    }

    @Override
    public void onGetEventDetails(String userName, String userEmail, String meetingId) {
        mvpView.onShowProgress();
        WebApiListener apiListenr = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        EventRequestModelClass requestModelClass = new EventRequestModelClass();
        requestModelClass.setUser_name(userName);
        requestModelClass.setUser_email(userEmail);
        requestModelClass.setEvent_code(meetingId);
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
                                mvpView.onShowErrorDialog(mContext.getString(R.string.app_tittle_string), mContext.getString(R.string.error_room_not_found), false);
                                sharedPreferenceData.deleteAllPreference();
                            } else {
                                Type listType = new TypeToken<EventResponseModelClass>() {}.getType();
                                EventResponseModelClass userProfileModel = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (userProfileModel.getStatus().equals(Constants.SUCCESS)) {
                                    if (userProfileModel.getData() != null) {
                                        mvpView.onSucessEventData(userProfileModel.getData());
                                        sharedPreferenceData.setString(Constants.KEY_USER_NAME, userName);
                                        sharedPreferenceData.setString(Constants.KEY_USER_EMAIL, userEmail);
                                        sharedPreferenceData.setString(Constants.KEY_MEETING_ID, meetingId);
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
                        handleErrorResponse(response.errorBody(), false);
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
