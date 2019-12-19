package engineer.babu.chatapplication.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import engineer.babu.chatapplication.R;
import engineer.babu.chatapplication.data.model.MessageModel;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private static final int CURRENT_USER = 1;
    private static final int OTHER_USER = 2;
    private Context mActivity;
    public ArrayList<MessageModel> messageList;
    public FirebaseDatabase mFirebaseDatabase;

    public MessagesAdapter(Context mActivity, ArrayList<MessageModel> messageList) {

        this.mActivity = mActivity;
        this.mFirebaseDatabase = FirebaseDatabase.getInstance();
        this.messageList = messageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case CURRENT_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_current_user_chat, parent, false);
                break;

            case OTHER_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_other_user_chat, parent, false);
                break;
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (messageList.get(position).getType().equals("text")) {
            holder.tvMessage.setText(messageList.get(position).getMessage_text());
            holder.tvMessage.setVisibility(View.VISIBLE);
            holder.ivImage.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);

        } else {
            holder.tvMessage.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.VISIBLE);
            if (messageList.get(position).getMessage_text() != null) {

            } else {
                holder.progressBar.setVisibility(View.GONE);
            }
        }
        holder.tvTime.setText(messageList.get(position).getTime());


        if (getItemViewType(position) == CURRENT_USER) {
            if (messageList.get(position).getSeen().equals("true")) {
                holder.tvSeenStatus.setText("Seen");
            } else {
                holder.tvSeenStatus.setText("Unseen");
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvMessage, tvTime, tvSeenStatus;
        private ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tv_time);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvSeenStatus = itemView.findViewById(R.id.tv_seen_status);
            ivImage = itemView.findViewById(R.id.iv_image);
            progressBar = itemView.findViewById(R.id.progress_bar);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return CURRENT_USER;
        } else {
            return OTHER_USER;
        }
    }
}
