package com.endpoint.ghair.activities_fragments.chat_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.endpoint.ghair.R;
import com.endpoint.ghair.adapters.Chat_Adapter;
import com.endpoint.ghair.databinding.ActivityChatBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.ChatUserModel;
import com.endpoint.ghair.models.MessageDataModel;
import com.endpoint.ghair.models.MessageModel;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatBinding binding;
    private String lang;
    private Chat_Adapter chat_adapter;
    private List<MessageModel> messagedatalist;
    private LinearLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private ChatUserModel chatUserModel;
    private int current_page = 1;
    private boolean isLoading = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        getDataFromIntent();
        initView();
        getChatMessages();

    }

    private void initView() {
        EventBus.getDefault().register(this);

        messagedatalist = new ArrayList<>();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        manager = new LinearLayoutManager(this);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.input), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(manager);
        chat_adapter = new Chat_Adapter(messagedatalist, userModel.getId(),chatUserModel.getImage(), this);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        // binding.llMsgContainer.setVisibility(View.GONE);
        binding.recView.setAdapter(chat_adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    int lastItemPos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items = chat_adapter.getItemCount();

                    if (lastItemPos == (total_items - 2) && !isLoading) {
                        isLoading = true;
                        messagedatalist.add(0, null);
                        chat_adapter.notifyItemInserted(0);
                        int next_page = current_page + 1;
                        loadMore(next_page);


                    }
                }
            }
        });
        binding.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });


    }


    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent != null) {

            chatUserModel = (ChatUserModel) intent.getSerializableExtra("chat_user_data");
binding.setName(chatUserModel.getName());

        }
    }

    private void checkdata() {
        String message = binding.edtMsgContent.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            Common.CloseKeyBoard(this, binding.edtMsgContent);
            binding.edtMsgContent.setText("");
            sendMessage(message);

        } else {
            binding.edtMsgContent.setError(getResources().getString(R.string.field_req));
        }
    }

    private void getChatMessages() {
        try {

Log.e("chat",chatUserModel.getRoom_id()+"");
            Api.getService(Tags.base_url)
                    .getRoomMessages(userModel.getId(), chatUserModel.getRoom_id(), 1,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MessageDataModel>() {
                        @Override
                        public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null) {
                                Log.e("kkkkk",response.body().getMessages().getData().size()+"");
                                chatUserModel = new ChatUserModel(response.body().getRoom().getOther_user_name(), response.body().getRoom().getOther_user_avatar(), response.body().getRoom().getSecond_user_id(), response.body().getRoom().getId(),response.body().getRoom().getOther_user_phone_code(),response.body().getRoom().getOther_user_phone());
                                preferences.create_update_ChatUserData(ChatActivity.this,chatUserModel);

                                messagedatalist.clear();
                                messagedatalist.addAll(response.body().getMessages().getData());
                                chat_adapter.notifyDataSetChanged();
                                scrollToLastPosition();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                 //   Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDataModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void loadMore(int next_page) {
        try {

            Api.getService(Tags.base_url)
                    .getRoomMessages(userModel.getId(), chatUserModel.getRoom_id(), next_page,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MessageDataModel>() {
                        @Override
                        public void onResponse(Call<MessageDataModel> call, Response<MessageDataModel> response) {
                            isLoading = false;
                            messagedatalist.remove(0);
                            chat_adapter.notifyItemRemoved(0);
                            if (response.isSuccessful() && response.body() != null) {

                                current_page = response.body().getMessages().getCurrent_page();
                                messagedatalist.addAll(0, response.body().getMessages().getData());
                                chat_adapter.notifyItemRangeInserted(0, response.body().getMessages().getData().size());


                            } else {

                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                   // Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDataModel> call, Throwable t) {
                            try {
                                isLoading = false;

                                if (messagedatalist.get(0) == null) {
                                    messagedatalist.remove(0);
                                    chat_adapter.notifyItemRemoved(0);
                                }
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }



    private void sendMessage(String message)
    {
        try {

            long date = Calendar.getInstance().getTimeInMillis()/1000;

            Api.getService(Tags.base_url)
                    .sendChatMessage(chatUserModel.getRoom_id(), userModel.getId(), chatUserModel.getId(), 1, message, date,"Bearer" + " " + userModel.getToken())
                    .enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Log.e("ddd",response.body().getMessage_type()+"__");
                                messagedatalist.add(response.body());
                                chat_adapter.notifyDataSetChanged();
                                scrollToLastPosition();
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                   // Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();//


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ChatActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }
    private void scrollToLastPosition()
    {

        new Handler()
                .postDelayed(() -> binding.recView.scrollToPosition(messagedatalist.size()-1),10);
    }


    @Override
    public void back() {
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenToNewMessage(MessageModel messageModel)
    {
        messagedatalist.add(messageModel);
        scrollToLastPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
        preferences.clearChatUserData(this);
    }
}
