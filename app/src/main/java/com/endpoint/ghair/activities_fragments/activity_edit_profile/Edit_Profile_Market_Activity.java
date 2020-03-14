package com.endpoint.ghair.activities_fragments.activity_edit_profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.adapters.ChooserService_Adapter;
import com.endpoint.ghair.adapters.CityAdapter;
import com.endpoint.ghair.adapters.ServicespinnerAdapter;
import com.endpoint.ghair.databinding.ActivityEditMarketProfileBinding;
import com.endpoint.ghair.databinding.ActivityEditProfileBinding;
import com.endpoint.ghair.databinding.ActivityMarketProfileBinding;
import com.endpoint.ghair.databinding.DialogSelectImageBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Cities_Model;
import com.endpoint.ghair.models.EditMarketModel;
import com.endpoint.ghair.models.EditprofileModel;
import com.endpoint.ghair.models.PlaceGeocodeData;
import com.endpoint.ghair.models.PlaceMapDetailsData;
import com.endpoint.ghair.models.Service_Model;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mukesh.countrypicker.CountryPicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_Profile_Market_Activity extends AppCompatActivity implements Listeners.EditprofileListener, Listeners.BackListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private String lang;
    private ActivityEditMarketProfileBinding binding;
    private CountryPicker countryPicker;
    private EditMarketModel editprofileModel;
    private Preferences preferences;
    private CityAdapter adapter;
    private List<Cities_Model.Data> dataList;
    private UserModel userModel;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private double lat = 0.0, lng = 0.0;
    private String address = "";
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private List<Integer> skillid;
    private ChooserService_Adapter chooserService_adapter;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private ServicespinnerAdapter spinnerAdapter;
    private List<Service_Model.Data> servDataList,servicelists;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_market_profile);

        initView();
        getService();

        if (userModel != null) {

lat = userModel.getLatitude();
lng=userModel.getLatitude();

            updatedata(userModel);
        }
    }

    private void updatedata(UserModel userModel) {
        this.userModel = userModel;
        preferences.create_update_userdata(this, userModel);
        editprofileModel.setCity_id(this.userModel.getCity_id() + "");
        editprofileModel.setArabic_title(this.userModel.getAr_market_title());
        editprofileModel.setEnglish_title(this.userModel.getEn_market_title());
editprofileModel.setAddress(userModel.getAddress());
editprofileModel.setLatitude(lat+"");
editprofileModel.setLongitude(lng+"");
        binding.setUserModel(userModel);
        binding.setViewModel(editprofileModel);
    }

    private String city_id = "";

    private void initView() {
        dataList = new ArrayList<>();
        servDataList=new ArrayList<>();
        servicelists=new ArrayList<>();
        skillid=new ArrayList<>();
        editprofileModel = new EditMarketModel();
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setViewModel(editprofileModel);
        binding.setBackListener(this);
        binding.setEditprofilelistner(this);
        binding.setUserModel(userModel);
        adapter = new CityAdapter(dataList, this);
        binding.spinnerCity.setAdapter(adapter);
        binding.image.setOnClickListener(view -> CreateImageAlertDialog());
        spinnerAdapter=new ServicespinnerAdapter(servDataList,this);
        binding.spinnerservice.setAdapter(spinnerAdapter);
        chooserService_adapter=new ChooserService_Adapter(servicelists,this,null);
        binding.receview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        binding.receview.setAdapter(chooserService_adapter);

        getCities();

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "";
                    editprofileModel.setCity_id(city_id);
                    binding.setViewModel(editprofileModel);
                } else {
                    city_id = String.valueOf(dataList.get(i).getId_city());
                    editprofileModel.setCity_id(city_id);
                    binding.setViewModel(editprofileModel);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerservice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    if(notidisfound(i)){
                        skillid.add( servDataList.get(i).getId());
                        servicelists.add(servDataList.get(i));
                        chooserService_adapter.notifyDataSetChanged();
                        binding.receview.setVisibility(View.VISIBLE);
                    }}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        updateskill();
    }

    private void updateskill() {
        if(userModel!=null&&userModel.getServices()!=null){
        for(int i=0;i<userModel.getServices().size();i++){
            skillid.add( userModel.getServices().get(i).getPivot().getService_id());
            if(lang.equals("ar")) {
                servicelists.add(new Service_Model.Data(userModel.getServices().get(i).getPivot().getService_id(), userModel.getServices().get(i).getAr_title()));
            }
            else {
                servicelists.add(new Service_Model.Data(userModel.getServices().get(i).getPivot().getService_id(), userModel.getServices().get(i).getEn_title()));

            }
            chooserService_adapter.notifyDataSetChanged();
            binding.receview.setVisibility(View.VISIBLE);
        }
    }}

    private boolean notidisfound(int position) {
        for(int i=0;i<skillid.size();i++){
            if(servDataList.get(position).getId()==skillid.get(i)){
                return false;
            }
        }
        return true;
    }
    private void getService() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getServices(lang)
                    .enqueue(new Callback<Service_Model>() {
                        @Override
                        public void onResponse(Call<Service_Model> call, Response<Service_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if(response.body().getData()!=null){
                                    updateserviceAdapter(response.body());}
                                else {
                                    Log.e("error",response.code()+"_"+response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(Edit_Profile_Market_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();



                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Service_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(Edit_Profile_Market_Activity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Edit_Profile_Market_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }
    private void updateserviceAdapter(Service_Model body) {
        servDataList.add(new Service_Model.Data(getResources().getString(R.string.ch_service)));
        if(body.getData()!=null){
            servDataList.addAll(body.getData());
            spinnerAdapter.notifyDataSetChanged();}
    }
    private void updateCityAdapter(Cities_Model body) {
        dataList.add(new Cities_Model.Data(getResources().getString(R.string.ch_city)));
        if(body.getData()!=null){
            dataList.addAll(body.getData());
            adapter.notifyDataSetChanged();}
    }
    private void getCities() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getCity(lang)
                    .enqueue(new Callback<Cities_Model>() {
                        @Override
                        public void onResponse(Call<Cities_Model> call, Response<Cities_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if(response.body().getData()!=null){
                                    updateCityAdapter(response.body());}
                                else {
                                    Log.e("error",response.code()+"_"+response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                 //   Toast.makeText(Edit_Profile_Client_Activity.class, "Server Error", Toast.LENGTH_SHORT).show();



                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Cities_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(Edit_Profile_Market_Activity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Edit_Profile_Market_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }




    @Override
    public void Editprofile(String name) {


    }
    @Override
    public void Editprofile(String englishname, String arabicname) {
        if (editprofileModel.isDataValid(this)) {
            signUp(editprofileModel);
        }
    }
    private void signUp(EditMarketModel editprofileModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();

            Api.getService(Tags.base_url)
                    .editprofile(editprofileModel.getArabic_title(),editprofileModel.getEnglish_title(),editprofileModel.getLongitude(),editprofileModel.getLatitude(),editprofileModel.getAddress(), editprofileModel.getCity_id(),userModel.getPhone(),skillid, "Bearer" + " " + userModel.getToken())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();

                                preferences.create_update_userdata(Edit_Profile_Market_Activity.this, response.body());
                                finish();

                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                              //  Toast.makeText(Edit_Profile_Client_Activity.this, getString(R.string.fa), Toast.LENGTH_SHORT).show();


                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(Edit_Profile_Market_Activity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Edit_Profile_Market_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    @Override
    public void back() {
        finish();
    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);


        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();


        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

       // dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(Edit_Profile_Market_Activity.this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Edit_Profile_Market_Activity.this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(Edit_Profile_Market_Activity.this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(Edit_Profile_Market_Activity.this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ1);
            } else {
                Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
        else  if (requestCode == loc_req)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initGoogleApi();
            }else
            {
                Toast.makeText(Edit_Profile_Market_Activity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imgUri1 = getUriFromBitmap(bitmap);
            editImageProfile(userModel.getId() + "", imgUri1.toString());


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            imgUri1 = data.getData();
            editImageProfile(userModel.getId() + "", imgUri1.toString());


        }
        else   if (requestCode == 100&&resultCode== Activity.RESULT_OK)
        {

            startLocationUpdate();
        }

    }

    private void editImageProfile(String user_id, String image) {
        ProgressDialog dialog = Common.createProgressDialog(this, getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody id_part = Common.getRequestBodyText(String.valueOf(user_id));
        RequestBody english_part = Common.getRequestBodyText(userModel.getEn_market_title());
        RequestBody arabic_part = Common.getRequestBodyText(userModel.getAr_market_title());
        RequestBody phone_part = Common.getRequestBodyText(userModel.getPhone());
        RequestBody city_part = Common.getRequestBodyText(userModel.getCity_id()+"");
        RequestBody address_part = Common.getRequestBodyText(userModel.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(userModel.getLatitude()+"");
        RequestBody lng_part = Common.getRequestBodyText(userModel.getLongitude()+"");
        MultipartBody.Part image_part = Common.getMultiPart(this, Uri.parse(image), "logo");
        List<RequestBody> skill_part = new ArrayList<>();
        for(int i=0;i<userModel.getServices().size();i++){
            skill_part.add(Common.getRequestBodyText(userModel.getServices().get(i)+""));
       }
        Api.getService(Tags.base_url)
                .editUserImage("Bearer" + " " + userModel.getToken(), image_part,english_part,arabic_part,lng_part,lat_part,address_part,city_part,phone_part,skill_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            //listener.onSuccess(response.body());

                            Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            updatedata(response.body());

                        } else {
                            Log.e("codeimage", response.code() + "_");
                            try {
                               // Toast.makeText(Edit_Profile_Client_Activity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                Log.e("respons", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // listener.onFailed(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                    }
                });
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }
    private void CheckPermission()
    {
        if (ActivityCompat.checkSelfPermission(this,fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }
    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);

            mMap.setOnMapClickListener(latLng -> {
                lat = latLng.latitude;
                lng = latLng.longitude;
                AddMarker(lat,lng);
                getGeoData(lat,lng);

            });

        }
    }

    private void Search(String query) {


        String fields = "id,place_id,name,geometry,formatted_address";

        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, lang, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {

                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getCandidates().size() > 0) {

                                address = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                binding.edtaddres.setText(address + "");
                                editprofileModel.setAddress(address);
                                binding.setViewModel(editprofileModel);
                                AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng());
                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {

                            Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void getGeoData(final double lat, double lng) {
        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, lang, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getResults().size() > 0) {
                                address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                binding.edtaddres.setText(address + "");
                                editprofileModel.setAddress(address);
                                binding.setViewModel(editprofileModel);
                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {

                            Toast.makeText(Edit_Profile_Market_Activity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void AddMarker(double lat, double lng) {
        editprofileModel.setLatitude(lat+"");
        editprofileModel.setLongitude(lng+"");
        binding.setViewModel(editprofileModel);
        this.lat = lat;
        this.lng = lng;

        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        } else {
            marker.setPosition(new LatLng(lat, lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(Edit_Profile_Market_Activity.this,100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate()
    {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        AddMarker(lat,lng);
        getGeoData(lat,lng);

        if (googleApiClient!=null)
        {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient!=null)
        {
            if (locationCallback!=null)
            {
                LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
            }
        }
    }


}
