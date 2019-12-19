package engineer.babu.chatapplication.ui.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.ui.activity.common.CommonActivity;
import engineer.babu.chatapplication.ui.activity.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public String first_name, last_name, email, image_url, KEY;
    public GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent intent = getIntent();
        if (intent != null) {
            first_name = intent.getStringExtra("first_name");
            last_name = intent.getStringExtra("last_name");
            email = intent.getStringExtra("email");
            image_url = intent.getStringExtra("image_url");
            KEY = intent.getStringExtra("KEY");
        }
        Log.d("KEYYYYYY", KEY);
        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(navView, navController);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        CircleImageView imageView = hView.findViewById(R.id.imageView);
        TextView tv_name = hView.findViewById(R.id.tv_name);
        TextView tv_email = hView.findViewById(R.id.tv_email);
        tv_name.setText(first_name);
        tv_email.setText(email);

        Menu nav_Menu = navigationView.getMenu();

        if (KEY.equalsIgnoreCase("GLOGIN") || KEY.equalsIgnoreCase("FBLOGIN")) {
            nav_Menu.findItem(R.id.nav_message_list).setVisible(false);
        } else {
            nav_Menu.findItem(R.id.nav_message_list).setVisible(true);
        }

        Glide.with(this)
                .load(image_url)
                .into(imageView);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Do you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_contact_list) {
            startActivity(new Intent(MainActivity.this, CommonActivity.class).putExtra("KEY", "1"));
        } else if (id == R.id.nav_message_list) {
            startActivity(new Intent(MainActivity.this, CommonActivity.class).putExtra("KEY", "2"));
        } else if (id == R.id.nav_logout) {
            signOut();
            revokeAccess();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    // ...
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, task -> {
                    // ...
                });
    }
}