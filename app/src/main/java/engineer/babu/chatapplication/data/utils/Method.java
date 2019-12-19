package engineer.babu.chatapplication.data.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Patterns;

import androidx.core.app.ActivityCompat;

import com.kaopiz.kprogresshud.KProgressHUD;

import engineer.babu.chatapplication.di.init.ApplicationAppContext;

public class Method {

    private static final String TAG = Method.class.getSimpleName();
    public static KProgressHUD hud;

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ApplicationAppContext.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean checkPermissions(Context context) {
        int permissionState = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean validateFirstName(String firstName) {
        return firstName.matches("[A-Z][a-zA-Z]*");
    }

    public final static boolean isValidPhone(CharSequence target) {
        if (target == null) {
            return false;
        } else {

            return Patterns.PHONE.matcher(target)
                    .matches() && (target.length() >= 10 && target.length() <= 20);
        }
    }

    public static String getPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void signUpProgressDialogCall(Activity activity) {
        hud = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Signing up....")
                .setDimAmount(0.5f)
                .setCancellable(true);
        hud.show();
    }

    public static void loadingProgressDialogCall(Activity activity) {
        hud = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Data Loading....")
                .setDimAmount(0.5f)
                .setCancellable(true);
        hud.show();
    }
}
