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

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.activities_fragments.activity_products.ProductsActivity;
import com.endpoint.ghair.adapters.ImagesAdapter;
import com.endpoint.ghair.adapters.SpinnerBrandAdapter;
import com.endpoint.ghair.adapters.SpinnerCatogryAdapter;
import com.endpoint.ghair.databinding.ActivityAddAuctionBinding;
import com.endpoint.ghair.databinding.ActivityAddProductBinding;
import com.endpoint.ghair.databinding.DialogSelectImageBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.AddAuctionModel;
import com.endpoint.ghair.models.AddProductModel;
import com.endpoint.ghair.models.Brand_Model;
import com.endpoint.ghair.models.MarketCatogryModel;
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
import retrofit2.http.Multipart;

public class AddProductActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAddProductBinding binding;
    private String lang;
private AddProductModel addProductModel;
    private String brand = "";
    private String cat = "";
    private List<Brand_Model.Data> brandList;
    private SpinnerBrandAdapter spinnerBrandAdapter;
    private List<MarketCatogryModel.Data> maDataList;
    private SpinnerCatogryAdapter spinnerCatogryAdapter;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 3, IMG_REQ2 = 2;
    private Uri url = null,uri;
    private List<Uri> urlList;
    private LinearLayoutManager manager;
    private ImagesAdapter imagesAdapter;
    private int type;
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_product);
        initView();
        getBrands();
        getCatogries();


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
        brandList = new ArrayList<>();
        maDataList = new ArrayList<>();
        urlList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());binding.setLang(lang);
        binding.setBackListener(this);
addProductModel=new AddProductModel();
binding.setAddproductmodel(addProductModel);
        spinnerBrandAdapter = new SpinnerBrandAdapter(brandList, this);
        spinnerCatogryAdapter = new SpinnerCatogryAdapter(maDataList, this);
        binding.spBrand.setAdapter(spinnerBrandAdapter);
        binding.spCat.setAdapter(spinnerCatogryAdapter);
        manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.recView.setLayoutManager(manager);
        imagesAdapter = new ImagesAdapter(urlList,this);
        binding.recView.setAdapter(imagesAdapter);
        binding.imageSelectPhoto.setOnClickListener(view -> {
            type=1;
                    CreateImageAlertDialog();
                }

        );
        binding.image.setOnClickListener(view -> {
            type=2;
            CreateImageAlertDialog();
        });

        binding.spBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    brand = "";
                } else {
                    brand = brandList.get(i).getId() + "";

                }
                addProductModel.setBrand_id(brand);
                binding.setAddproductmodel(addProductModel);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    cat = "";
                } else {
                    cat = maDataList.get(i).getId() + "";

                }
                addProductModel.setCat_id(cat);
                binding.setAddproductmodel(addProductModel);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
binding.btnSend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(userModel!=null){
        if(addProductModel.isDataValid(AddProductActivity.this)){
if(urlList!=null&&urlList.size()>0){
    if(uri!=null){
        Addproductimagemain(addProductModel);
    }
    else {
Addproductimage(addProductModel);
    }
}
else {
    Toast.makeText(AddProductActivity.this,getResources().getString(R.string.ch_baner),Toast.LENGTH_LONG).show();
}
        }
    }
    else {
            Common.CreateNoSignAlertDialog(AddProductActivity.this);

        }
    }
});
    }
    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(this, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    private void Addproductimagemain(AddProductModel addAuctionModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            RequestBody english_part = Common.getRequestBodyText(addAuctionModel.getEnglish_title());
            RequestBody arabic_part = Common.getRequestBodyText(addAuctionModel.getArabic_title());
            RequestBody englishdes_part = Common.getRequestBodyText(addAuctionModel.getEnglish_des());
            RequestBody arabicdes_part = Common.getRequestBodyText(addAuctionModel.getArabic_des());
            RequestBody price_part = Common.getRequestBodyText(addAuctionModel.getPrice());
            RequestBody model_part = Common.getRequestBodyText(addAuctionModel.getModel());
            RequestBody amount_part = Common.getRequestBodyText(addAuctionModel.getAmount());
            RequestBody cat_part = Common.getRequestBodyText(addAuctionModel.getCat_id());
            RequestBody brand_part = Common.getRequestBodyText(addAuctionModel.getBrand_id());


         //   RequestBody id_part = Common.getRequestBodyText("Bearer" + " " + userModel.getToken());
            MultipartBody.Part image_part = Common.getMultiPart(this, uri, "main_image");
            List<MultipartBody.Part> partimageList = getMultipartBodyList(urlList, "banner[]");
            Api.getService(Tags.base_url)
                    .Addproductwithimagemain(arabic_part, english_part, arabicdes_part,englishdes_part,model_part,price_part,cat_part,brand_part,amount_part,  "Bearer" + " " + userModel.getToken(),image_part, partimageList)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddProductActivity.this, getResources().getString(R.string.your_request_send_suc), Toast.LENGTH_LONG).show();

                               finish();
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
                                        Toast.makeText(AddProductActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }
    private void Addproductimage(AddProductModel addAuctionModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            RequestBody english_part = Common.getRequestBodyText(addAuctionModel.getEnglish_title());
            RequestBody arabic_part = Common.getRequestBodyText(addAuctionModel.getArabic_title());
            RequestBody englishdes_part = Common.getRequestBodyText(addAuctionModel.getEnglish_des());
            RequestBody arabicdes_part = Common.getRequestBodyText(addAuctionModel.getArabic_des());
            RequestBody price_part = Common.getRequestBodyText(addAuctionModel.getPrice());
            RequestBody model_part = Common.getRequestBodyText(addAuctionModel.getModel());
            RequestBody amount_part = Common.getRequestBodyText(addAuctionModel.getAmount());
            RequestBody cat_part = Common.getRequestBodyText(addAuctionModel.getCat_id());
            RequestBody brand_part = Common.getRequestBodyText(addAuctionModel.getBrand_id());


            //   RequestBody id_part = Common.getRequestBodyText("Bearer" + " " + userModel.getToken());
            List<MultipartBody.Part> partimageList = getMultipartBodyList(urlList, "banner[]");
            Api.getService(Tags.base_url)
                    .Addproductwithimage(arabic_part, english_part, arabicdes_part,englishdes_part,model_part,price_part,cat_part,brand_part,amount_part,  "Bearer" + " " + userModel.getToken(), partimageList)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddProductActivity.this, getResources().getString(R.string.your_request_send_suc), Toast.LENGTH_LONG).show();

                                finish();
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
                                        Toast.makeText(AddProductActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
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
                                brandList.add(new Brand_Model.Data(getResources().getString(R.string.ch_brand)));
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


                                Toast.makeText(AddProductActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());


        }
    }

    private void getCatogries() {
        maDataList.clear();
        spinnerCatogryAdapter.notifyDataSetChanged();
        try {


            Api.getService(Tags.base_url)
                    .get_catogries(lang)
                    .enqueue(new Callback<MarketCatogryModel>() {
                        @Override
                        public void onResponse(Call<MarketCatogryModel> call, Response<MarketCatogryModel> response) {
                            Log.e("lfkkfkkfk", response.code() + "");
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                maDataList.clear();
                                maDataList.add(new MarketCatogryModel.Data(getResources().getString(R.string.ch_cat)));
                                maDataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    spinnerCatogryAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    spinnerCatogryAdapter.notifyDataSetChanged();


                                }
                            } else {
                                spinnerCatogryAdapter.notifyDataSetChanged();


                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MarketCatogryModel> call, Throwable t) {
                            try {


                                Toast.makeText(AddProductActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());


        }
    }
    private void CreateImageAlertDialog()
    {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.dialog_select_image,null,false);



        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();



        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

       // dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    private void CheckReadPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission()
    {
        if (ContextCompat.checkSelfPermission(this,camera_permission)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this,write_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{camera_permission,write_permission},IMG_REQ2);
        }else
        {
            SelectImage(IMG_REQ2);

        }

    }
    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }else
            {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent,img_req);

        }else if (img_req ==IMG_REQ2)
        {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,img_req);
            }catch (SecurityException e)
            {
                Toast.makeText(this,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            if(type==1) {
                url = getUriFromBitmap(bitmap);

                urlList.add(url);
                imagesAdapter.notifyDataSetChanged();
            }
            else {
                uri = getUriFromBitmap(bitmap);
                Picasso.with(this).load(uri).into(binding.image);
                binding.icon.setVisibility(View.GONE);
            }


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
if(type==1) {
    url = data.getData();
    urlList.add(url);
    imagesAdapter.notifyDataSetChanged();
}
else {
    uri = data.getData();

    Picasso.with(this).load(uri).into(binding.image);
    binding.icon.setVisibility(View.GONE);
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

    public void deleteImage(int adapterPosition) {
        urlList.remove(adapterPosition);
        imagesAdapter.notifyItemRemoved(adapterPosition);

    }

    @Override
    public void back() {
        finish();
    }
}
