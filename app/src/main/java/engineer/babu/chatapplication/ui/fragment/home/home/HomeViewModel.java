package engineer.babu.chatapplication.ui.fragment.home.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.FragmentHomeBinding;

import static com.facebook.FacebookSdk.getApplicationContext;
import static engineer.babu.chatapplication.data.utils.Method.isNetworkAvailable;

public class HomeViewModel{
    public Context context;
    public Activity activity;
    public FragmentHomeBinding binding;

    public HomeViewModel(Context context, Activity activity, FragmentHomeBinding binding) {
        this.activity = activity;
        this.context = context;
        this.binding = binding;

        binding.toolbarId.toolbarTitle.setText("Register User");
    }

}
