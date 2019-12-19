package engineer.babu.chatapplication.ui.fragment.home.contact;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.database.AppDatabase;
import engineer.babu.chatapplication.data.database.AppExecutors;
import engineer.babu.chatapplication.data.model.Person;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.FragmentContactFragmentsBinding;
import engineer.babu.chatapplication.ui.adapter.PersonListAdapter;

import static com.facebook.FacebookSdk.getApplicationContext;
import static engineer.babu.chatapplication.data.utils.Method.hud;

public class ContactFragments extends Fragment {
    public Context context;
    public Activity activity;
    public FragmentContactFragmentsBinding binding;
    public PersonListAdapter mAdapter;
    private AppDatabase mDb;

    public ContactFragments() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_fragments, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mAdapter = new PersonListAdapter(context, activity);
        binding.recyclerView.setAdapter(mAdapter);

        mDb = AppDatabase.getInstance(context);
        binding.toolbarId.toolbarTitle.setText("Contact List");
        Method.loadingProgressDialogCall(activity);
        retrieveTasks();
    }

    private void retrieveTasks() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            final List<Person> persons = mDb.personDao().loadAllPersons();
            activity.runOnUiThread(() -> mAdapter.setTasks(persons));
            hud.dismiss();
        });
    }
}