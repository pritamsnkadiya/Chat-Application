package engineer.babu.chatapplication.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.model.Person;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.PersonItemBinding;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.ViewHolder> {

    private static String TAG = PersonListAdapter.class.getName();
    private Context context;
    private Activity activity;
    private List<Person> modelArrayList;
    private LayoutInflater layoutInflater;
    public NavController navController = null;

    public PersonListAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        navController = Navigation.findNavController(activity, R.id.my_nav_host_fragment);
    }

    public void setTasks(List<Person> personList) {
        modelArrayList = personList;
        notifyDataSetChanged();
        Log.d(TAG, personList.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PersonItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.person_item, parent, false);
        return new PersonListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.binding.setAllListModel(modelArrayList.get(position));

      //  holder.binding.cardClick.setOnClickListener(v -> navController.navigate(R.id.messageFragment));
    }

    @Override
    public int getItemCount() {
        if (modelArrayList == null) {
            return 0;
        }
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final PersonItemBinding binding;

        public ViewHolder(final PersonItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}