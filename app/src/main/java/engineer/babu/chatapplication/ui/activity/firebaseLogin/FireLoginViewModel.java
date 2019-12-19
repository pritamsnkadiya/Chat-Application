package engineer.babu.chatapplication.ui.activity.firebaseLogin;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.utils.AppConstants;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.data.utils.MyTextWatcher;
import engineer.babu.chatapplication.databinding.ActivityFireLoginBinding;
import engineer.babu.chatapplication.ui.activity.main.MainActivity;
import engineer.babu.chatapplication.ui.activity.signup.SignUpActivity;

import static engineer.babu.chatapplication.data.utils.Method.hud;

public class FireLoginViewModel implements View.OnClickListener {

    private static String TAG = FireLoginViewModel.class.getName();
    public Context context;
    public Activity activity;
    public ActivityFireLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private boolean visibility = false;

    public FireLoginViewModel(Context context, Activity activity, ActivityFireLoginBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;

        binding.loginTollbar.toolbarTitle.setText("Login");


        initialpageSetup();
        sethint();
        binding.offVisibility.setOnClickListener(this);
        binding.signup.setOnClickListener(this);
        binding.login.setOnClickListener(this);
        binding.forgotPassword.setOnClickListener(this);
    }

    private void sethint() {
        binding.etUsername.addTextChangedListener(new MyTextWatcher(binding.etUsername));
        binding.etPassword.addTextChangedListener(new MyTextWatcher(binding.etPassword));

    }

    private void initialpageSetup() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        SpannableString styledString = new SpannableString(binding.signup.getText().toString());   // index 103 - 112
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                context.startActivity(new Intent(context, SignUpActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        styledString.setSpan(new StyleSpan(Typeface.BOLD), binding.signup.getText().toString().length() - 6, binding.signup.getText().toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);  // make text bold
        styledString.setSpan(clickableSpan, binding.signup.getText().toString().length() - 6, binding.signup.getText().toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        binding.signup.setMovementMethod(LinkMovementMethod.getInstance());//--makes styled string clickable --
        styledString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.colorPrimaryDark)), binding.signup.getText().toString().length() - 6, binding.signup.getText().toString().length(), 0);
        binding.signup.setText(styledString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (!Method.isValidEmail(binding.etUsername.getText().toString())) {
                    binding.etUsername.setError("Please Enter Email ID");
                    // Toast.makeText(context, "Please Enter Email ID", Toast.LENGTH_SHORT).show();
                } else if (binding.etPassword.getText().toString().isEmpty() || binding.etPassword.getText().length() < 6) {
                    binding.etPassword.setError("Please Enter Password min 6-8 char");
                    //Toast.makeText(context, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    Method.signUpProgressDialogCall(activity);
                    loginApiCall();
                }
                break;

            case R.id.signup:
                context.startActivity(new Intent(context, SignUpActivity.class));
                break;
            case R.id.off_visibility:
                if (!visibility) {
                    binding.etPassword.setTransformationMethod(null);
                    binding.offVisibility.setImageResource(R.drawable.ic_visibility_black_24dp);
                    binding.etPassword.setSelection(binding.etPassword.getText().toString().length());
                    visibility = true;
                } else {
                    binding.etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.offVisibility.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    binding.etPassword.setSelection(binding.etPassword.getText().toString().length());
                    visibility = false;
                }

                break;

            case R.id.forgotPassword:
                // context.startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void loginApiCall() {

        auth.signInWithEmailAndPassword(binding.etUsername.getText().toString(), binding.etPassword.getText().toString())
                .addOnCompleteListener(activity, task -> {
                    if (!task.isSuccessful()) {
                        hud.dismiss();
                        Toast.makeText(context, "User not validate", Toast.LENGTH_LONG).show();
                    } else {
                        firebaseUser = auth.getCurrentUser();
                        fetchUserDataApiCall(firebaseUser.getUid());
                    }
                });
    }

    private void fetchUserDataApiCall(String uid) {

        firebaseDatabase.getReference().child(AppConstants.USERS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Method.savePreferences(context, "USER_ID", Objects.requireNonNull(dataSnapshot.child("user_id").getValue()).toString());
                    Method.savePreferences(context, "FULL_NAME", Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                    Method.savePreferences(context, "EMAIL_ID", Objects.requireNonNull(dataSnapshot.child("email_id").getValue()).toString());
                    if (dataSnapshot.child("thumb_image").getValue() != null) {
                        Method.savePreferences(context, "THUMB_IMAGE", Objects.requireNonNull(dataSnapshot.child("thumb_image").getValue()).toString());
                    }
                    Method.savePreferences(context, "ISLOGIN", "true");
                } catch (Exception e) {
                    e.getMessage();
                }
                hud.dismiss();
                context.startActivity(new Intent(context, MainActivity.class).putExtra("KEY", "FLOGIN"));
                activity.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}