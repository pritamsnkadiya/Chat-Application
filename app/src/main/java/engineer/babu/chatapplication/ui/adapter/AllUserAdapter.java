package engineer.babu.chatapplication.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.model.UserModel;
import engineer.babu.chatapplication.databinding.RowAllUsersBinding;
import engineer.babu.chatapplication.ui.activity.chat.ChatActivity;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.MyViewHolder> {

    private Activity allUsersActivity;
    public ArrayList<UserModel> userList;
    private LayoutInflater layoutInflater;

    public AllUserAdapter(Activity allUsersActivity, ArrayList<UserModel> userList) {
        this.allUsersActivity = allUsersActivity;
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        RowAllUsersBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_all_users, parent, false);
        return new AllUserAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.binding.setAllListModel(userList.get(position));

        holder.binding.rootLayout.setOnClickListener(view -> {
            Intent chatActivityIntent = new Intent(allUsersActivity, ChatActivity.class);
            chatActivityIntent.putExtra("from", "FriendsFragment");
            chatActivityIntent.putExtra("chat_user_name", userList.get(position).name);
            chatActivityIntent.putExtra("chat_user_image", userList.get(position).image);
            chatActivityIntent.putExtra("chat_user_id", userList.get(position).user_id);
            allUsersActivity.startActivity(chatActivityIntent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowAllUsersBinding binding;

        public MyViewHolder(final RowAllUsersBinding binding) {

            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
