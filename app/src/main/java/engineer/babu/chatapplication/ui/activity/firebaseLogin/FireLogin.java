package engineer.babu.chatapplication.ui.activity.firebaseLogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.databinding.ActivityFireLoginBinding;
import engineer.babu.chatapplication.ui.activity.login.LoginActivity;

public class FireLogin extends AppCompatActivity {

    private static String TAG = FireLogin.class.getName();
    public Context context;
    public Activity activity;
    public ActivityFireLoginBinding binding;
    public FireLoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = FireLogin.this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_fire_login);
        viewModel = new FireLoginViewModel(context, activity, binding);
        binding.setFireLoginView(viewModel);
    }
}
