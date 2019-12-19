package engineer.babu.chatapplication.ui.activity.login;

import android.app.Activity;
import android.content.Context;

import engineer.babu.chatapplication.databinding.ActivityLoginBinding;

public class LoginViewModel {

    public static String TAG = LoginViewModel.class.getName();
    private Context context;
    private Activity activity;
    private ActivityLoginBinding binding;

    public LoginViewModel(Context context, Activity activity, ActivityLoginBinding binding) {

        this.binding = binding;
        this.activity = activity;
        this.context = context;
    }
}
