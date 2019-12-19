package engineer.babu.chatapplication.ui.fragment.home.list;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import engineer.babu.chatapplication.data.model.UserModel;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.FragmentListBinding;
import engineer.babu.chatapplication.ui.adapter.PersonListAdapter;
import engineer.babu.chatapplication.ui.adapter.UserListAdapter;

import static engineer.babu.chatapplication.data.utils.Method.hud;

public class ListViewModel extends Observable {

    public static String TAG = ListViewModel.class.getName();
    public Context context;
    public Activity activity;
    public FragmentListBinding binding;
    public DatabaseReference mFirebaseDatabase;
    public FirebaseDatabase mFirebaseInstance;
    public UserListAdapter userListAdapter;
    public List<UserModel> userModelList = new ArrayList<>();

    public ListViewModel(Context context, Activity activity, FragmentListBinding binding) {
        this.activity = activity;
        this.context = context;
        this.binding = binding;

        binding.rvUserList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        Method.loadingProgressDialogCall(activity);
        addUserChangeListener();

        userListAdapter = new UserListAdapter(context, activity);
        binding.rvUserList.setAdapter(userListAdapter);

        binding.toolbarId.toolbarTitle.setText("User Update");
    }

    private void addUserChangeListener() {
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userModelList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserModel university = postSnapshot.getValue(UserModel.class);
                    userModelList.add(university);
                }
                userListAdapter.setTasks(userModelList);
                hud.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
}
