package engineer.babu.chatapplication.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.model.Person;
import engineer.babu.chatapplication.data.model.UserModel;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.LayoutUserListBinding;
import engineer.babu.chatapplication.databinding.PersonItemBinding;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private static String TAG = UserListAdapter.class.getName();
    public Context context;
    public Activity activity;
    private List<UserModel> modelArrayList;
    private LayoutInflater layoutInflater;

    public UserListAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void setTasks(List<UserModel> personList) {
        modelArrayList = personList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutUserListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_user_list, parent, false);
        return new UserListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.binding.setAllListModel(modelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if (modelArrayList == null) {
            return 0;
        }
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutUserListBinding binding;

        public ViewHolder(final LayoutUserListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}