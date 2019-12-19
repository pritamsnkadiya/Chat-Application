package engineer.babu.chatapplication.ui.activity.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.databinding.ActivityChatBinding;
import engineer.babu.chatapplication.ui.activity.signup.SignUpActivity;
import engineer.babu.chatapplication.ui.activity.signup.SignUpViewModel;

public class ChatActivity extends AppCompatActivity {

    private static String TAG = ChatActivity.class.getName();
    public Context context;
    public Activity activity;
    public ActivityChatBinding binding;
    public ChatViewModel chatViewModel;
    public String from;
    public String other_user_id;
    public String chat_user_name;
    public String chat_user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = ChatActivity.this;

        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getStringExtra("from");
            other_user_id = intent.getStringExtra("chat_user_id");
            chat_user_name = intent.getStringExtra("chat_user_name");
            chat_user_image = intent.getStringExtra("chat_user_image");
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        chatViewModel = new ChatViewModel(context, activity, binding, other_user_id, from, chat_user_name, chat_user_image);
        binding.setChatView(chatViewModel);

    }
}
