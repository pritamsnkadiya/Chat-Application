package engineer.babu.chatapplication.ui.activity.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import engineer.babu.chatapplication.R;

public class CommonActivity extends AppCompatActivity {

    public static String TAG = CommonActivity.class.getName();
    public String KEY;
    public NavController navController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        Intent intent = getIntent();
        if (intent != null) {
            KEY = intent.getStringExtra("KEY");
        }

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        switch (KEY) {

            case "1":
                navController.navigate(R.id.contactFragment);
                break;

            case "2":
                navController.navigate(R.id.messageFragment);
                break;

            default:
                Toast.makeText(this, "Undefined Click !!", Toast.LENGTH_SHORT).show();
        }
    }
}

