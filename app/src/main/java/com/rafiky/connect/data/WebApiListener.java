package com.rafiky.connect.data;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface WebApiListener {

    // get
    @Headers("Content-Type: application/json")
    @POST("event/event_details")
    Call<String> getEventData(@Body RequestBody requestBody);


    @Headers("Content-Type: application/json")
    @POST("event/save_user")
    Call<String> updateListerDetail(@Body RequestBody requestBody);

    @Headers("Content-Type: application/json")
    @POST("save-event-end-time")
    Call<String> updateEndTime(@Body RequestBody requestBody);


}
