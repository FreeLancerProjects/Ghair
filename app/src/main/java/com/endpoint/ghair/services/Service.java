package com.endpoint.ghair.services;


import com.endpoint.ghair.models.App_Data_Model;
import com.endpoint.ghair.models.Brand_Model;
import com.endpoint.ghair.models.Cities_Model;
import com.endpoint.ghair.models.Market_Model;
import com.endpoint.ghair.models.Service_Model;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.models.UserModel;

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
    Call<Service_Model> getServices(
            @Header("lang")String lang
    );
    @GET("api/brands")
    Call<Brand_Model> getBrands(
            @Query("page") int page,
            @Header("lang")String lang
    );
    @GET("api/all-markets")
    Call<Market_Model> getMarkets(
            @Query("page") int page,
            @Header("lang")String lang
    );
    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(
            @Field("phone_code") String phone_code,
            @Field("phone") String phone,
            @Field("password") String password
    );
    @GET("api/all-cities")
    Call<Cities_Model> getCity();
    @GET("api/app/info")
    Call<App_Data_Model> getterms(
            @Header("lang")String lang

    );
    @FormUrlEncoded
    @POST("api/client/register")
    Call<UserModel> sign_up(@Field("name") String name,
                            @Field("phone_code") String phone_code,
                            @Field("phone") String phone,

                            @Field("country_id") String country_id,
                            @Field("city_id") String city_id,
                            @Field("password") String password,
                            @Field("software_type")String software_type
    );
}