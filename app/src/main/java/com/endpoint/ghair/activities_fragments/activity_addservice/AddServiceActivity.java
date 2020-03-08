package com.endpoint.ghair.activities_fragments.activity_addservice;

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
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_addauction.AddAuctionActivity;
import com.endpoint.ghair.databinding.ActivityAddAuctionBinding;
import com.endpoint.ghair.databinding.ActivityAddServiceBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.AddAuctionModel;
import com.endpoint.ghair.models.AddServiceModel;
import com.endpoint.ghair.models.PlaceGeocodeData;
import com.endpoint.ghair.models.PlaceMapDetailsData;
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
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceActivity extends AppCompatActivity implements Listeners.BackListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivityAddServiceBinding binding;
    private String lang;
    private String search_id;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private AddServiceModel addServiceModel;
    private double lat = 0.0, lng = 0.0;
    private String address = "";
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private Preferences preferences;
private UserModel userModel;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_service);
        initView();
        updateUI();
        CheckPermission();

    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        if (getIntent().getIntExtra("search", -1) != 0) {
            search_id = getIntent().getIntExtra("search", -1) + "";
        }
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
        addServiceModel = new AddServiceModel();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setAddservicemodel(addServiceModel);
        binding.tvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(), "");

            }
        });
        binding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(getFragmentManager(), "");

            }
        });
        binding.edtaddres.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.edtaddres.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    Common.CloseKeyBoard(this, binding.edtaddres);
                    Search(query);
                    return false;
                }
            }
            return false;
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userModel!=null){
                if (addServiceModel.isDataValid(AddServiceActivity.this)) {
services(addServiceModel);
                }
            }
            else {
                    Common.CreateNoSignAlertDialog(AddServiceActivity.this);

                }
            }

        });
        createDatePickerDialog();
        createTimePickerDialog();

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear + 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        // order_time_calender.set(Calendar.YEAR,year);
        //order_time_calender.set(Calendar.MONTH,monthOfYear);
        //order_time_calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        //Log.e("kkkk", calendar.getTime().getMonth() + "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        addServiceModel.setDate(format.format(calendar.getTime()));
        binding.setAddservicemodel(addServiceModel);// timePickerDialog.show(getFragmentManager(), "");

    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(this, R.color.input));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(this, R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(this, R.color.input));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(lang));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

    private void createTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), true);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ActivityCompat.getColor(this, R.color.input));
        timePickerDialog.setCancelColor(ActivityCompat.getColor(this, R.color.gray4));
        timePickerDialog.setOkColor(ActivityCompat.getColor(this, R.color.input));
        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setLocale(new Locale(lang));
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        addServiceModel.setTime(dateFormat.format(calendar.getTime()));
        binding.setAddservicemodel(addServiceModel);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
            } else {
                Toast.makeText(AddServiceActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
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
                AddMarker(lat, lng);
                getGeoData(lat, lng);

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
                                addServiceModel.setAddress(address);
                                binding.setAddservicemodel(addServiceModel);
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

                            Toast.makeText(AddServiceActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
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
                                addServiceModel.setAddress(address);
                                binding.setAddservicemodel(addServiceModel);
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

                            Toast.makeText(AddServiceActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void AddMarker(double lat, double lng) {
        addServiceModel.setLatitude(lat + "");
        addServiceModel.setLongitude(lng + "");
        binding.setAddservicemodel(addServiceModel);
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
                            status.startResolutionForResult(AddServiceActivity.this, 100);
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
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        AddMarker(lat, lng);
        getGeoData(lat, lng);

        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null) {
            if (locationCallback != null) {
                LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            startLocationUpdate();
        }
    }
    private void services(AddServiceModel addServiceModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .AddService(search_id, addServiceModel.getLatitude(), addServiceModel.getLongitude(),addServiceModel.getAddress(),addServiceModel.getDate(),addServiceModel.getTime(),addServiceModel.getDetails(),"Bearer" + " " + userModel.getToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddServiceActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                            //    Appply();
                            } else {
                                try {

                                    Log.e("errorssssss", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AddServiceActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddServiceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
}
