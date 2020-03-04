package com.endpoint.ghair.activities_fragments.activity_addauction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.endpoint.ghair.R;
import com.endpoint.ghair.databinding.ActivityAddAuctionBinding;
import com.endpoint.ghair.databinding.DialogSelectImageBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.AddAuctionModel;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAuctionActivity extends AppCompatActivity implements Listeners.BackListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ActivityAddAuctionBinding binding;
    private String lang;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String time;
private AddAuctionModel addAuctionModel;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private List<Uri> imageList;
    private Uri uri = null;
    private int type = 0;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_auction);
        initView();


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        imageList=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        addAuctionModel=new AddAuctionModel();
        binding.setAddauctionmodel(addAuctionModel);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.consTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(), "");

            }
        });
        binding.image1.setOnClickListener(view -> {
            type = 1;
            CreateImageAlertDialog();
        });
        binding.image2.setOnClickListener(view -> {
            type = 2;
            CreateImageAlertDialog();
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel != null) {
                    if (addAuctionModel.isDataValid(AddAuctionActivity.this)) {
                        if (imageList != null && imageList.size() > 0) {
                            auctionimage(addAuctionModel);
                        } else {
                            auction(addAuctionModel);
                        }
                    }
                } else {

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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);

        time = format.format(calendar.getTime());
        timePickerDialog.show(getFragmentManager(), "");

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
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), false);
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
        time += " " + dateFormat.format(calendar.getTime());
addAuctionModel.setTime(time);
    binding.setAddauctionmodel(addAuctionModel);
    }
    private void auction(AddAuctionModel addAuctionModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .auctionservice(addAuctionModel.getArabic_title(), addAuctionModel.getEnglish_title(), addAuctionModel.getPrice(),"Bearer" + " " + userModel.getToken(), addAuctionModel.getTime())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddAuctionActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                                Appply();
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
                                        Toast.makeText(AddAuctionActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAuctionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(this, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    private void auctionimage(AddAuctionModel addAuctionModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            RequestBody english_part = Common.getRequestBodyText(addAuctionModel.getEnglish_title());
            RequestBody arabic_part = Common.getRequestBodyText(addAuctionModel.getArabic_title());

            RequestBody price_part = Common.getRequestBodyText(addAuctionModel.getPrice());
            RequestBody time_part = Common.getRequestBodyText(addAuctionModel.getTime());


            RequestBody id_part = Common.getRequestBodyText("Bearer" + " " + userModel.getToken());

            List<MultipartBody.Part> partimageList = getMultipartBodyList(imageList, "auction_image[]");
            Api.getService(Tags.base_url)
                    .auctionwithimage(arabic_part, english_part, price_part, time_part,  "Bearer" + " " + userModel.getToken(), partimageList)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddAuctionActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();

                                Appply();
                            } else {
                                try {

                                    Log.e("errorssss", response.code() + "_" + response.errorBody().string());
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
                                        Toast.makeText(AddAuctionActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddAuctionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    private void Appply() {
        addAuctionModel = new AddAuctionModel();
        binding.setAddauctionmodel(addAuctionModel);
        binding.image1.setImageDrawable(null);
        binding.icon1.setVisibility(View.VISIBLE);
        binding.image2.setImageDrawable(null);
        binding.icon2.setVisibility(View.VISIBLE);
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
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
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
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (type == 1) {
                imgUri1 = getUriFromBitmap(bitmap);
                binding.icon1.setVisibility(View.GONE);
                Picasso.with(this).load(imgUri1).fit().into(binding.image1);
                imageList.add(imgUri1);
            } else if (type == 2) {
                uri = getUriFromBitmap(bitmap);
                binding.icon2.setVisibility(View.GONE);
                Picasso.with(this).load(uri).fit().into(binding.image2);
                imageList.add(uri);
            }

        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            if (type == 1) {

                imgUri1 = data.getData();
                binding.icon1.setVisibility(View.GONE);
                Picasso.with(this).load(imgUri1).fit().into(binding.image1);
                imageList.add(imgUri1);

            } else if (type == 2) {
                uri = data.getData();
                binding.icon2.setVisibility(View.GONE);
                Picasso.with(this).load(uri).fit().into(binding.image2);
                imageList.add(uri);
            }


        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    @Override
    public void back() {
        finish();
    }
}
