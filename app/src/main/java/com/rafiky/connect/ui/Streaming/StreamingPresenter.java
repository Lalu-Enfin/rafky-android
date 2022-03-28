package com.rafiky.connect.ui.Streaming;

import android.content.Context;
import android.util.Log;

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
import com.rafiky.connect.model.requestmodel.TimerUpdateModel;
import com.rafiky.connect.model.requestmodel.UpdateListnerDetailsModel;
import com.rafiky.connect.model.responsemodel.ErrorModel;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;
import com.rafiky.connect.model.responsemodel.UserUpdateDetailsResponse;
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

public class StreamingPresenter implements StreamingContract.Presenter{

    private StreamingContract.MvpView mvpView ;
    private Context mContext;
    private static String TAG = "StreamingPresenter";
    private SharedPreferenceData sharedPreferenceData;

    public StreamingPresenter(StreamingContract.MvpView mvpView, Context mContext) {
        this.mvpView = mvpView;
        this.mContext = mContext;
        this.sharedPreferenceData = new SharedPreferenceData(mContext);
    }


    @Override
    public void updateListnerDetails(String language, String eventId) {
        Log.d(TAG,"API-updateListnerDetails");
        String userName = sharedPreferenceData.getString(Constants.KEY_USER_NAME);
        String userEmail = sharedPreferenceData.getString(Constants.KEY_USER_EMAIL);
        String meetingId = sharedPreferenceData.getString(Constants.KEY_MEETING_ID);

        UpdateListnerDetailsModel requestModelClass = new UpdateListnerDetailsModel();

        requestModelClass.setName(userName);
        requestModelClass.setEmail(userEmail);
        requestModelClass.setEvent_id(eventId);
        requestModelClass.setLanguage(language);
        requestModelClass.setRole(Constants.SESSION_LISTENER);

        WebApiListener apiListenr = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestModelClass));
        Call<String> call = apiListenr.updateListerDetail(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code() == Constants.SUCCESS_CODE) {
                        if (response.body() != null) {
                            Log.d("updateListerDetail : ", response.body());
                            Logger.e(String.valueOf(response));
                            if (new JsonParser().parse(response.body()).getAsJsonObject().has("error")) {
                                Log.e(TAG,"updateListerDetail : -API_Error");
                            } else {
                                Type listType = new TypeToken<UserUpdateDetailsResponse>() {}.getType();
                                UserUpdateDetailsResponse userUpdateDetailsResponse = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (userUpdateDetailsResponse.getStatus().equals(Constants.SUCCESS)) {
                                    if (userUpdateDetailsResponse.getData() != null) {
                                        Logger.d(String.valueOf(userUpdateDetailsResponse.getData()));
                                        mvpView.onSuccsessListerDetails(userUpdateDetailsResponse.getData());
                                    }
                                }
                            }
                        }

                    } else if (response.code() == Constants.ERROR_CODE) {
                        //handleErrorResponse(response.errorBody(), false);
                    } else if (response.code() == Constants.ERROR_BAD_GATEWAY_CODE) {
                       // handleErrorResponse(response.errorBody(), false);
                    } else if (response.code() == Constants.ERROR_UNAUTHORIZED_CODE) {
                        //handleErrorResponse(response.errorBody(), true);
                    } else {
                        //handleErrorResponse(response.errorBody(), false);
                    }
                } catch (JsonSyntaxException e) {
                    Log.e("Exception : ", e.getMessage());
                    mvpView.onHideProgress();
                    // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exception : ", t.getMessage());
            }
        });
    }


    @Override
    public void onUpdateTime(String userId) {
        Log.d(TAG,"API-onUpdateTime");
        TimerUpdateModel requestModelClass = new TimerUpdateModel();
        requestModelClass.setUser_id(userId);
        WebApiListener apiListenr = ApiClient.getRetrofitInstance().create(WebApiListener.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestModelClass));
        Call<String> call = apiListenr.updateEndTime(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.code() == Constants.SUCCESS_CODE) {
                        if (response.body() != null) {
                            Log.d(TAG,"API-onUpdateTime_Response");
                            Log.d("updateListerDetail : ", response.body());
                            Logger.e(String.valueOf(response));
                           /* if (new JsonParser().parse(response.body()).getAsJsonObject().has("error")) {
                                Log.e(TAG,"updateListerDetail : -API_Error");
                            } else {
                                Type listType = new TypeToken<UserUpdateDetailsResponse>() {}.getType();
                                UserUpdateDetailsResponse userUpdateDetailsResponse = new GsonBuilder().create().fromJson(response.body(), listType);
                                if (userUpdateDetailsResponse.getStatus().equals(Constants.SUCCESS)) {
                                    if (userUpdateDetailsResponse.getData() != null) {
                                        Logger.d(String.valueOf(userUpdateDetailsResponse.getData()));
                                        mvpView.onSuccsessListerDetails(userUpdateDetailsResponse.getData());
                                    }
                                }
                            }*/
                        }

                    } else if (response.code() == Constants.ERROR_CODE) {
                        //handleErrorResponse(response.errorBody(), false);
                    } else if (response.code() == Constants.ERROR_BAD_GATEWAY_CODE) {
                        // handleErrorResponse(response.errorBody(), false);
                    } else if (response.code() == Constants.ERROR_UNAUTHORIZED_CODE) {
                        //handleErrorResponse(response.errorBody(), true);
                    } else {
                        //handleErrorResponse(response.errorBody(), false);
                    }
                } catch (JsonSyntaxException e) {
                    Log.e("Exception : ", e.getMessage());
                    mvpView.onHideProgress();
                    // HyperLog.i(TAG,"onUserLogin Error - "+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Exception : ", t.getMessage());
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
