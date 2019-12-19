package engineer.babu.chatapplication.ui.activity.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.ActivitySignUpBinding;
import engineer.babu.chatapplication.ui.activity.login.LoginActivity;
import engineer.babu.chatapplication.ui.activity.login.LoginViewModel;

public class SignUpActivity extends AppCompatActivity {

    private static String TAG = SignUpActivity.class.getName();
    public Context context;
    public Activity activity;
    public ActivitySignUpBinding binding;
    public SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = SignUpActivity.this;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        viewModel = new SignUpViewModel(context, activity, binding);
        binding.setSignUpView(viewModel);

    }
}
