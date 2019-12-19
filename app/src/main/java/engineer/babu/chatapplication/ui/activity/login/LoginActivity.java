package engineer.babu.chatapplication.ui.activity.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.Arrays;

import engineer.babu.chatapplication.BuildConfig;
import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.ActivityLoginBinding;
import engineer.babu.chatapplication.ui.activity.firebaseLogin.FireLogin;
import engineer.babu.chatapplication.ui.activity.main.MainActivity;
import engineer.babu.chatapplication.ui.activity.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = LoginActivity.class.getName();
    public Context context;
    public Activity activity;
    public ActivityLoginBinding binding;
    public LoginViewModel loginViewModel;

    public CallbackManager callbackManager;
    public String first_name, last_name, email, image_url, id;
    private static final int RC_SIGN_IN = 200;
    public GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = LoginActivity.this;

        if (!Method.checkPermissions(this)) {
            requestPermissions();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel(context, activity, binding);
        binding.setLoginView(loginViewModel);

        callbackManager = CallbackManager.Factory.create();
        binding.loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //checkLoginStatus();
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, loginResult.toString());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, error.getMessage());
            }
        });

        binding.signInButton.setOnClickListener(this);
        binding.loginPage.setOnClickListener(this);
        binding.signupPage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;

            case R.id.loginPage:
                startActivity(new Intent(this, FireLogin.class));
                break;

            case R.id.signupPage:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Toast.makeText(LoginActivity.this, "Üser Logged Out", Toast.LENGTH_LONG).show();
            } else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(acct);
    }

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, (object, response) -> {
            try {
                first_name = object.getString("first_name");
                last_name = object.getString("last_name");
                email = object.getString("email");
                id = object.getString("id");
                image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                Log.d(TAG, first_name + "\n" + last_name + "\n" + email + "\n" + image_url + "\n" + id);
                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                        .putExtra("first_name", first_name)
                        .putExtra("last_name", last_name)
                        .putExtra("email", email)
                        .putExtra("image_url", image_url)
                        .putExtra("KEY", "FBLOGIN"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void updateUI(GoogleSignInAccount acct) {
        if (acct != null) {
            first_name = acct.getDisplayName();
            last_name = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            email = acct.getEmail();
            id = acct.getId();
            image_url = String.valueOf(acct.getPhotoUrl());

            Log.d(TAG, first_name + "\n" + last_name + "\n" + email + "\n" + image_url + "\n" + id);
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .putExtra("first_name", first_name)
                    .putExtra("last_name", last_name)
                    .putExtra("email", email)
                    .putExtra("image_url", image_url).putExtra("KEY", "GLOGIN"));

        } else {
            Log.d(TAG, "Logout");
        }
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            updateUI(acct);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    binding.activityLogin,
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, view -> {
                        // Request permission
                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                RC_SIGN_IN);
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RC_SIGN_IN);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_SIGN_IN) {
            if (grantResults.length <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Snackbar.make(
                        binding.activityLogin,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .show();
            }
        }
    }
}
