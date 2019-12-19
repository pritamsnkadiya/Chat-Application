package engineer.babu.chatapplication.ui.fragment.home.message;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.databinding.FragmentMessageBinding;

public class MessageFragment extends Fragment {
    public static String TAG = MessageFragment.class.getName();
    public Context context;
    public Activity activity;
    public FragmentMessageBinding binding;
    public MessageViewModel messageViewModel;

    public MessageFragment() {
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

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_message, container, false);
        View view = binding.getRoot();
        messageViewModel = new MessageViewModel(context, activity, binding);
        binding.setMessageView(messageViewModel);

        return view;

    }
}
