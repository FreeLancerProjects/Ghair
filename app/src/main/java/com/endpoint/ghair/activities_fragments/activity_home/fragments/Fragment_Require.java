package com.endpoint.ghair.activities_fragments.activity_home.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.adapters.SpinnerBrandAdapter;
import com.endpoint.ghair.databinding.DialogSelectImageBinding;
import com.endpoint.ghair.databinding.FragmentAuctionBinding;
import com.endpoint.ghair.databinding.FragmentRequireBinding;
import com.endpoint.ghair.models.Brand_Model;
import com.endpoint.ghair.models.ReguiredModel;
import com.endpoint.ghair.models.SignUpMArketModel;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Require extends Fragment {

    private HomeActivity activity;
    private FragmentRequireBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private List<Brand_Model.Data> brandList;
    private SpinnerBrandAdapter spinnerBrandAdapter;
    private String lang;
    private ReguiredModel reguiredModel;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private List<Uri> imageList;
    private Uri uri = null;
    private int type = 0;

    public static Fragment_Require newInstance() {
        return new Fragment_Require();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_require, container, false);
        initView();
        getBrands();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        reguiredModel = new ReguiredModel();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        brandList = new ArrayList<>();
        imageList = new ArrayList<>();
        spinnerBrandAdapter = new SpinnerBrandAdapter(brandList, activity);
        binding.spBrand.setAdapter(spinnerBrandAdapter);
        binding.setRequireModel(reguiredModel);
        binding.spBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    reguiredModel.setBrand_id("");
                    binding.setRequireModel(reguiredModel);
                } else {
                    reguiredModel.setBrand_id(brandList.get(i).getId() + "");
                    binding.setRequireModel(reguiredModel);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.checknew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.checkused.setChecked(false);
                Log.e("llkkk", isChecked + "");
                if (isChecked) {
                    reguiredModel.setRequired_type("new");
                    binding.checknew.setChecked(true);
                } else {
                    reguiredModel.setRequired_type("");
                }
            }
        });
        binding.checkused.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.checknew.setChecked(false);
                if (isChecked) {
                    reguiredModel.setRequired_type("used");
                    binding.checkused.setChecked(true);

                } else {
                    reguiredModel.setRequired_type("");
                }
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
                    if (reguiredModel.isDataValid(activity)) {
                        if (imageList != null && imageList.size() > 0) {
                            requireimage(reguiredModel);
                        } else {
                            require(reguiredModel);
                        }
                    }
                } else {
                    Common.CreateNoSignAlertDialog(activity);

                }
            }
        });
    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_select_image, null, false);


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
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{camera_permission, write_permission}, IMG_REQ2);
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
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
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
                Picasso.with(activity).load(imgUri1).fit().into(binding.image1);
                imageList.add(imgUri1);
            } else if (type == 2) {
                uri = getUriFromBitmap(bitmap);
                binding.icon2.setVisibility(View.GONE);
                Picasso.with(activity).load(uri).fit().into(binding.image2);
                imageList.add(uri);
            }

        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            if (type == 1) {

                imgUri1 = data.getData();
                binding.icon1.setVisibility(View.GONE);
                Picasso.with(activity).load(imgUri1).fit().into(binding.image1);
                imageList.add(imgUri1);

            } else if (type == 2) {
                uri = data.getData();
                binding.icon2.setVisibility(View.GONE);
                Picasso.with(activity).load(uri).fit().into(binding.image2);
                imageList.add(uri);
            }


        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    private void getBrands() {
        brandList.clear();
        spinnerBrandAdapter.notifyDataSetChanged();
        try {


            Api.getService(Tags.base_url)
                    .getBrands(1, lang)
                    .enqueue(new Callback<Brand_Model>() {
                        @Override
                        public void onResponse(Call<Brand_Model> call, Response<Brand_Model> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                brandList.clear();
                                brandList.add(new Brand_Model.Data(activity.getResources().getString(R.string.ch_brand)));
                                brandList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    spinnerBrandAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    spinnerBrandAdapter.notifyDataSetChanged();


                                }
                            } else {
                                spinnerBrandAdapter.notifyDataSetChanged();


                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Brand_Model> call, Throwable t) {
                            try {


                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());


        }
    }

    private void require(ReguiredModel reguiredModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .requireservice(reguiredModel.getArabic_title(), reguiredModel.getEnglish_title(), reguiredModel.getModel(), reguiredModel.getBrand_id(), reguiredModel.getAmount(), reguiredModel.getRequired_type(), "Bearer" + " " + userModel.getToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(activity, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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
            MultipartBody.Part part = Common.getMultiPart(activity, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    private void requireimage(ReguiredModel signUpModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            RequestBody english_part = Common.getRequestBodyText(signUpModel.getEnglish_title());
            RequestBody arabic_part = Common.getRequestBodyText(signUpModel.getArabic_title());

            RequestBody model_part = Common.getRequestBodyText(signUpModel.getModel());
            RequestBody brand_part = Common.getRequestBodyText(signUpModel.getBrand_id());

            RequestBody amount_part = Common.getRequestBodyText(signUpModel.getAmount());
            RequestBody required_part = Common.getRequestBodyText(signUpModel.getRequired_type());
            RequestBody id_part = Common.getRequestBodyText("Bearer" + " " + userModel.getToken());

            List<MultipartBody.Part> partimageList = getMultipartBodyList(imageList, "product_images[]");
            Api.getService(Tags.base_url)
                    .Requiredwithimage(arabic_part, english_part, model_part, brand_part, amount_part, required_part, "Bearer" + " " + userModel.getToken(), partimageList)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(activity, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();

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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        reguiredModel = new ReguiredModel();
        binding.setRequireModel(reguiredModel);
        binding.image1.setImageDrawable(null);
        binding.icon1.setVisibility(View.VISIBLE);
        binding.image2.setImageDrawable(null);
        binding.icon2.setVisibility(View.VISIBLE);
        activity.displayFragmentMain();

    }


}
