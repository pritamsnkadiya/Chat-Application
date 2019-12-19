package engineer.babu.chatapplication.ui.fragment.home.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.appevents.codeless.internal.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.database.AppDatabase;
import engineer.babu.chatapplication.data.database.AppExecutors;
import engineer.babu.chatapplication.data.model.Person;
import engineer.babu.chatapplication.data.model.UserModel;
import engineer.babu.chatapplication.data.utils.AppConstants;
import engineer.babu.chatapplication.data.utils.FetchContacts;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.FragmentHomeBinding;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static engineer.babu.chatapplication.data.utils.Method.hud;
import static engineer.babu.chatapplication.data.utils.Method.isNetworkAvailable;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public static String TAG = HomeFragment.class.getName();
    public Context context;
    public Activity activity;
    public FragmentHomeBinding binding;
    public HomeViewModel homeViewModel;
    public Person person;
    public DatabaseReference mFirebaseDatabase;
    public FirebaseDatabase mFirebaseInstance;
    private String userId;
    public String imageEncoded = "";

    public HomeFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(container.getContext());
        }

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();
        homeViewModel = new HomeViewModel(context, activity, binding);
        binding.setHomeView(homeViewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.userImage.setOnClickListener(this);
        binding.btSubmit.setOnClickListener(this);

        //  getNumber(activity.getContentResolver());

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");

        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");
                String appTitle = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        new FetchContacts(context, contacts -> {
            // Here you will get the contacts
            Log.d(TAG, contacts.toString());

        }).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                if (binding.edName.getText().toString().isEmpty()) {
                    binding.edName.setError("Please Enter Your name");
                    //Toast.makeText(context, "Please Enter Your name", Toast.LENGTH_SHORT).show();
                } else if (!Method.isValidEmail(binding.edEmail.getText().toString())) {
                    binding.edEmail.setError("Please Enter Your Email id");
                    //Toast.makeText(context, "Please Enter Your Email id", Toast.LENGTH_SHORT).show();
                } else if (!Method.isValidPhone((binding.edMobile.getText()))) {
                    binding.edMobile.setError("Please Enter Your Mobile no.");
                    //Toast.makeText(context, "Please Enter Your Mobile no.", Toast.LENGTH_SHORT).show();
                } else if (imageEncoded.isEmpty()) {
                    Toast.makeText(context, "Please Choose Image", Toast.LENGTH_SHORT).show();
                } else {
                    Method.loadingProgressDialogCall(activity);
                    createUser(binding.edName.getText().toString(), binding.edEmail.getText().toString(), binding.edMobile.getText().toString(), imageEncoded);
                }
                break;

            case R.id.user_image:
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
                break;
        }
    }

    private void createUser(String name, String email, String mobile, String imageEncoded) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        UserModel user = new UserModel(name, email, mobile, imageEncoded);
        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }
                Log.e(TAG, "User data is changed!" + user.getUserName() + ", " + user.getMobileNo());
                // clear edit text
                binding.edName.setText("");
                binding.edEmail.setText("");
                binding.edMobile.setText("");
                hud.dismiss();
                Toast.makeText(context, "User is created ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            binding.userImage.setImageBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            File extStore = new File(Environment.getExternalStorageDirectory() + File.separator + "Babu" + File.separator + "Image");
            if (!extStore.exists()) {
                extStore.mkdirs();
            }
            String docFileName = "img" + "_" + System.currentTimeMillis();
            String path = extStore.getAbsolutePath() + "/" + docFileName + ".png";
            Log.d("Image", "Save to: " + path);

            try {
                File myFile = new File(path);
                myFile.createNewFile();
                FileOutputStream out = new FileOutputStream(myFile);
                AppConstants.IMAGE_PATH = path;
                AppConstants.IMAGE_NAME = docFileName;

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getNumber(ContentResolver cr) {
        @SuppressLint("Recycle") Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        assert phones != null;
        while (phones.moveToNext()) {
            person = new Person();
            person.name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            person.number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
    }
}
