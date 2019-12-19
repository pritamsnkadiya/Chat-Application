package engineer.babu.chatapplication.ui.fragment.home.contact;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.database.AppDatabase;
import engineer.babu.chatapplication.data.database.AppExecutors;
import engineer.babu.chatapplication.data.model.Person;
import engineer.babu.chatapplication.databinding.FragmentContactFragmentsBinding;
import engineer.babu.chatapplication.ui.adapter.PersonListAdapter;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ContactViewModel {
    public Context context;
    public Activity activity;
    public FragmentContactFragmentsBinding binding;

    public ContactViewModel(Context context, Activity activity, FragmentContactFragmentsBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;

    }
}
