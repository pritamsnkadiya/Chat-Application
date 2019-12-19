package engineer.babu.chatapplication.ui.activity.signup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.ActivitySignUpBinding;
import engineer.babu.chatapplication.ui.activity.login.LoginActivity;
import engineer.babu.chatapplication.ui.activity.login.LoginViewModel;
import engineer.babu.chatapplication.ui.activity.main.MainActivity;

import static engineer.babu.chatapplication.data.utils.Method.hud;

public class SignUpViewModel extends Observable implements View.OnClickListener {
    public static String TAG = SignUpViewModel.class.getName();
    private Context context;
    private Activity activity;
    private ActivitySignUpBinding binding;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth auth;
    private boolean visibilityPassword = false;
    private boolean visibilityConfirmPassword = false;

    public SignUpViewModel(Context context, Activity activity, ActivitySignUpBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;

        binding.signupTollbar.toolbarTitle.setText("Sign up");

        init();
        initialpageSetup();
    }

    private void initialpageSetup() {
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        SpannableString styledString = new SpannableString(binding.tvLogin.getText().toString());   // index 103 - 112
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        styledString.setSpan(new StyleSpan(Typeface.BOLD), binding.tvLogin.getText().toString().length() - 5, binding.tvLogin.getText().toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);  // make text bold
        styledString.setSpan(clickableSpan, binding.tvLogin.getText().toString().length() - 5, binding.tvLogin.getText().toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        binding.tvLogin.setMovementMethod(LinkMovementMethod.getInstance());//--makes styled string clickable --
        styledString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.colorPrimaryDark)), binding.tvLogin.getText().toString().length() - 5, binding.tvLogin.getText().toString().length(), 0);
        binding.tvLogin.setText(styledString);

    }

    private void init() {

        //------registering listeners
        binding.signuptv.setOnClickListener(this);
        binding.ivVisibility.setOnClickListener(this);
        binding.ivVisibilityConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signuptv:
                if (binding.etUsername.getText().toString().isEmpty()) {
                    binding.etUsername.setError("Please Enter User name ");
                    //Toast.makeText(context, "Please Enter User name", Toast.LENGTH_SHORT).show();
                } else if (!Method.isValidEmail(binding.etMail.getText().toString())) {
                    binding.etMail.setError("Please Enter Email Id");
                    //Toast.makeText(context, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                } else if (binding.etPassword.getText().toString().isEmpty() || binding.etPassword.getText().length() < 6) {
                    binding.etPassword.setError("Please Enter Password min 6-8 char");
                    // Toast.makeText(context, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else if (binding.etConfirmPassword.getText().toString().isEmpty() || binding.etConfirmPassword.getText().length() < 6) {
                    binding.etConfirmPassword.setError("Please Enter Password min 6-8 char");
                    // Toast.makeText(context, "Please Enter Confirmed Password  min 6-8 char", Toast.LENGTH_SHORT).show();
                } else {
                    Method.signUpProgressDialogCall(activity);
                    signUpHit();
                }
                break;

            case R.id.iv_visibility:
                if (!visibilityPassword) {
                    binding.etPassword.setTransformationMethod(null);
                    binding.ivVisibilityConfirm.setImageResource(R.drawable.ic_visibility_black_24dp);
                    binding.etPassword.setSelection(binding.etPassword.getText().toString().length());
                    visibilityPassword = true;
                } else {
                    binding.etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.ivVisibilityConfirm.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    binding.etPassword.setSelection(binding.etPassword.getText().toString().length());
                    visibilityPassword = false;
                }
                break;

            case R.id.iv_visibility_confirm:
                if (!visibilityConfirmPassword) {
                    binding.etConfirmPassword.setTransformationMethod(null);
                    binding.ivVisibilityConfirm.setImageResource(R.drawable.ic_visibility_black_24dp);
                    binding.etConfirmPassword.setSelection(binding.etConfirmPassword.getText().toString().length());
                    visibilityConfirmPassword = true;
                } else {
                    binding.etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.ivVisibilityConfirm.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    binding.etConfirmPassword.setSelection(binding.etConfirmPassword.getText().toString().length());
                    visibilityConfirmPassword = false;
                }
                break;
        }
    }

    private void signUpHit() {
        //auth.createUserWithEmailAndPassword(binding.etUsername.getText().toString(), binding.etPassword.getText().toString())

        auth.createUserWithEmailAndPassword(binding.etMail.getText().toString(), binding.etPassword.getText().toString())
                .addOnCompleteListener(activity, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(context, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        System.out.println("Authentication" + task.getException());

                    } else {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        addUsertoDataBase(activity, firebaseUser);
                    }
                });
    }

    private void addUsertoDataBase(Activity activity, final FirebaseUser firebaseUser) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", firebaseUser.getUid());
        params.put("name", binding.etUsername.getText().toString());
        params.put("email_id", firebaseUser.getEmail());
        params.put("image", "default");
        params.put("status", "Hi there i am using TheMessenger App");
        params.put("thumb_image", "default");
        params.put("online", "true");
        FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(firebaseUser.getUid())
                .setValue(params)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                    } else {
                        Method.savePreferences(context, "USER_ID", firebaseUser.getUid());
                        Method.savePreferences(context, "FULL_NAME", binding.etUsername.getText().toString());
                        Method.savePreferences(context, "EMAIL_ID", firebaseUser.getEmail());
                        Method.savePreferences(context, "THUMB_IMAGE", "default");
                        Method.savePreferences(context, "ISLOGIN", "true");
                        hud.dismiss();
                        context.startActivity(new Intent(context, MainActivity.class).putExtra("KEY", "FSIGNUP"));
                        activity.finish();
                    }
                });
    }
}
