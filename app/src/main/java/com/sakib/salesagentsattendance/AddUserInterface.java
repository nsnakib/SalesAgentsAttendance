package com.sakib.salesagentsattendance;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddUserInterface {

    String URL = "http://128.199.215.102:4040/";
    @FormUrlEncoded
    @POST("api/attendance")
    Call<String> addUser(

            @Field("name") String name,
            @Field("uid") String uid,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("request_id") String request_id

    );


}