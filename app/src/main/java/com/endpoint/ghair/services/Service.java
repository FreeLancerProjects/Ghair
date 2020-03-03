package com.endpoint.ghair.services;


import com.endpoint.ghair.models.App_Data_Model;
import com.endpoint.ghair.models.Auction_Model;
import com.endpoint.ghair.models.Brand_Model;
import com.endpoint.ghair.models.Cities_Model;
import com.endpoint.ghair.models.MarketCatogryModel;
import com.endpoint.ghair.models.Market_Model;
import com.endpoint.ghair.models.PlaceGeocodeData;
import com.endpoint.ghair.models.PlaceMapDetailsData;
import com.endpoint.ghair.models.Product_Model;
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
    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @FormUrlEncoded
    @POST("api/slider")
    Call<Slider_Model> get_slider(
            @Field("type") String type

    );

    @GET("api/services")
    Call<Service_Model> getServices(
            @Header("lang") String lang
    );

    @GET("api/brands")
    Call<Brand_Model> getBrands(
            @Query("page") int page,
            @Header("lang") String lang
    );

    @GET("api/all-markets")
    Call<Market_Model> getMarkets(
            @Query("page") int page,
            @Header("lang") String lang
    );

    @GET("api/allAuctions")
    Call<Auction_Model> getAuctions(
            @Query("page") int page,
            @Header("lang") String lang
    );

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(
            @Field("phone_code") String phone_code,
            @Field("phone") String phone,
            @Field("password") String password
    );

    @GET("api/all-cities")
    Call<Cities_Model> getCity(
            @Header("lang") String lang

    );

    @GET("api/app/info")
    Call<App_Data_Model> getterms(
            @Header("lang") String lang

    );

    @FormUrlEncoded
    @POST("api/client/register")
    Call<UserModel> sign_up(@Field("name") String name,
                            @Field("phone_code") String phone_code,
                            @Field("phone") String phone,

                            @Field("country_id") String country_id,
                            @Field("city_id") String city_id,
                            @Field("password") String password,
                            @Field("software_type") String software_type
    );

    @FormUrlEncoded
    @POST("api/market/register")
    Call<UserModel> sign_upMArket(
            @Field("ar_market_title") String ar_market_title,
            @Field("en_market_title") String en_market_title,
            @Field("national_number") String national_number,

            @Field("phone_code") String phone_code,
            @Field("phone") String phone,

            @Field("country_id") String country_id,
            @Field("city_id") String city_id,
            @Field("password") String password,
            @Field("software_type") String software_type,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("services") List<Integer> services
    );

    @Multipart
    @POST("api/market/register")
    Call<UserModel> sign_upMArketwithimage(
            @Part("ar_market_title") RequestBody ar_market_title,
            @Part("en_market_title") RequestBody en_market_title,
            @Part("national_number") RequestBody national_number,

            @Part("phone_code") RequestBody phone_code,
            @Part("phone") RequestBody phone,

            @Part("country_id") RequestBody country_id,
            @Part("city_id") RequestBody city_id,
            @Part("password") RequestBody password,
            @Part("software_type") RequestBody software_type,
            @Part("address") RequestBody address,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("services") List<RequestBody> services,
            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("api/contactUs")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("phone") String phone,
                                   @Field("message") String message
    );

    @FormUrlEncoded
    @POST("api/client/profile/update")
    Call<UserModel> editprofile(@Field("name") String name,
                                @Field("phone") String phone,
                                @Field("city_id") String city_id,
                                @Header("Authorization") String Authorization);

    @Multipart
    @POST("api/user_image")
    Call<UserModel> editUserImage(@Part("Authorization") RequestBody Authorization,
                                  @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("api/addNewRequiredRequest")
    Call<ResponseBody> requireservice(@Field("ar_title") String ar_title,
                                      @Field("en_title") String en_title,
                                      @Field("model_title") String model_title,

                                      @Field("brand_id") String brand_id,
                                      @Field("amount") String amount,
                                      @Field("required_type") String required_type,
                                      @Header("Authorization") String Authorization
    );

    @Multipart
    @POST("api/addNewRequiredRequest")
    Call<ResponseBody> Requiredwithimage(
            @Part("ar_title") RequestBody ar_title,
            @Part("en_title") RequestBody en_title,
            @Part("model_title") RequestBody model_title,

            @Part("brand_id") RequestBody brand_id,
            @Part("amount") RequestBody amount,

            @Part("required_type") RequestBody required_type,
            @Header("Authorization") String Authorization,

            @Part List<MultipartBody.Part> image);

    @FormUrlEncoded
    @POST("api/addNewAuctionRequest")
    Call<ResponseBody> auctionservice(@Field("ar_title") String ar_title,
                                      @Field("en_title") String en_title,
                                      @Field("start_price") String start_price,
                                      @Header("Authorization") String Authorization,
                                      @Field("end_date") String end_date
    );

    @Multipart
    @POST("api/addNewAuctionRequest")
    Call<ResponseBody> auctionwithimage(
            @Part("ar_title") RequestBody ar_title,
            @Part("en_title") RequestBody en_title,
            @Part("start_price") RequestBody start_price,

            @Part("end_date") RequestBody end_date,

            @Header("Authorization") String Authorization,

            @Part List<MultipartBody.Part> image);
    @FormUrlEncoded
    @POST("api/singleAuction")
    Call<Auction_Model> get_singleauction(
            @Field("auction_id") String auction_id,
            @Header("lang") String lang,
            @Field("page") int page


    );
    @FormUrlEncoded
    @POST("api/market-details")
    Call<UserModel> get_singlemarket(
            @Field("market_id") String market_id,
            @Header("lang") String lang


    );
    @FormUrlEncoded
    @POST("api/AllProductsFilterByBrandCatMarket")
    Call<Product_Model> get_productss(
            @Field("brand_id") String brand_id,

            @Field("cat_id") String cat_id,

            @Field("market_id") String market_id,
            @Header("lang") String lang


    );
    @FormUrlEncoded
    @POST("api/catsOfMarket")
    Call<MarketCatogryModel> get_Catogries(


            @Field("market_id") String market_id,
            @Header("lang") String lang


    );
}