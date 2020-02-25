package com.endpoint.ghair.services;


import com.endpoint.ghair.models.Service_Model;
import com.endpoint.ghair.models.Slider_Model;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @FormUrlEncoded
    @POST("api/slider")
    Call<Slider_Model> get_slider(
            @Field("type")String type

    );

    @GET("api/services")
    Call<Service_Model> getservices(
            @Field("page") int page,
            @Header("lang")String lang
    );
}