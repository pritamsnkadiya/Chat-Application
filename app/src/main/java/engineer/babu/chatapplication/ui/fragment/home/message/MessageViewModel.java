package engineer.babu.chatapplication.ui.fragment.home.message;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observable;

import engineer.babu.chatapplication.data.model.UserModel;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.FragmentMessageBinding;
import engineer.babu.chatapplication.ui.adapter.AllUserAdapter;

public class MessageViewModel extends Observable {
    private static String TAG =MessageViewModel.class.getName();
    public Context context;
    public Activity activity;
    public FragmentMessageBinding binding;
    private AllUserAdapter allUserAdapter;
    private ArrayList<UserModel> userList;
    private FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;

    public MessageViewModel(Context context, Activity activity, FragmentMessageBinding binding) {

        this.context = context;
        this.activity = activity;
        this.binding = binding;

        binding.conversationId.toolbarTitle.setText("All Users");
        binding.rvMessageList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        initialPageSetUp();
    }

    private void initialPageSetUp() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        binding.rvMessageList.setLayoutManager(new LinearLayoutManager(context));
        userList = new ArrayList<>();
        allUserAdapter = new AllUserAdapter(activity, userList);
        binding.rvMessageList.setAdapter(allUserAdapter);

        //--------fetch all users data----
        hitAllUserApi();
    }

    private void hitAllUserApi() {

        if (!activity.isFinishing()) {
            mFirebaseDatabase.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (!child.getRef().getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            UserModel userModel = child.getValue(UserModel.class);
                            if (userModel.user_id != null) {
                                userList.add(userModel);
                                Log.d(TAG,userList.toString());
                            }
                        }
                    }
                    allUserAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
