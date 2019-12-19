package engineer.babu.chatapplication.ui.fragment.home.list;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.model.UserModel;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.FragmentListBinding;
import engineer.babu.chatapplication.ui.fragment.home.home.HomeFragment;

public class ListFragment extends Fragment {

    public static String TAG = ListFragment.class.getName();
    public Context context;
    public Activity activity;
    public FragmentListBinding binding;
    public ListViewModel listViewModel;

    public ListFragment() {
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

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_list, container, false);
        View view = binding.getRoot();
        listViewModel = new ListViewModel(context, activity, binding);
        binding.setListView(listViewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
