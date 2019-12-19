package engineer.babu.chatapplication.ui.activity.chat;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import engineer.babu.chatapplication.data.model.MessageModel;
import engineer.babu.chatapplication.data.utils.Method;
import engineer.babu.chatapplication.databinding.ActivityChatBinding;
import engineer.babu.chatapplication.ui.adapter.MessagesAdapter;

public class ChatViewModel extends Observable implements View.OnClickListener {

    public Context context;
    public Activity activity;
    public ActivityChatBinding binding;
    private DatabaseReference mDataBaseReferense;
    private ArrayList<MessageModel> messageList;
    private MessagesAdapter messagesAdapter;
    private String lastMessage = "";
    private long unReadMessagesCount = 0;
    private boolean typingStarted;
    private String message_push_id;
    public static Map map;
    public String from;
    public String other_user_id;
    public String chat_user_name;
    public String chat_user_image;

    public ChatViewModel(Context context, Activity activity, ActivityChatBinding binding, String other_user_id, String from, String chat_user_name, String chat_user_image) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
        this.other_user_id = other_user_id;
        this.from = from;
        this.chat_user_name = chat_user_name;
        this.chat_user_image = chat_user_image;

        mDataBaseReferense = FirebaseDatabase.getInstance().getReference();


        binding.tvChatUserName.setText(chat_user_name);

        initViews();
        initialPageSetUp();
    }

    private void initViews() {
        binding.ivSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        sendMessage("text");
    }

    private void sendMessage(String type) {

        if (!TextUtils.isEmpty(binding.etMessageText.getText().toString()) || type.equals("image")) {
            Map message = new HashMap();
            message.put("message_text", binding.etMessageText.getText().toString());
            message.put("seen", "false");
            message.put("type", type);
            message.put("time", ServerValue.TIMESTAMP);
            message.put("from", FirebaseAuth.getInstance().getCurrentUser().getUid());

            lastMessage = binding.etMessageText.getText().toString();
            binding.etMessageText.setText("");
            message_push_id = mDataBaseReferense.child("messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(other_user_id).push().getKey();
            updateUserChatList(message, message_push_id, type);
        }
    }

    private void updateUserChatList(final Map message, final String message_push_id, String type) {
        final Map currentUserChatMap = new HashMap();
        currentUserChatMap.put("name", chat_user_name);
        if (chat_user_image != null) {
            currentUserChatMap.put("thumb_image", chat_user_image);
        }
        currentUserChatMap.put("count", 0);
        currentUserChatMap.put("last_message", lastMessage);
        currentUserChatMap.put("type", type);
        currentUserChatMap.put("time", ServerValue.TIMESTAMP);
        currentUserChatMap.put("from", FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDataBaseReferense.child("chat").child(other_user_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("seen").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.getValue().equals("false")) {
                                currentUserChatMap.put("seen", "false");
                            } else {
                                currentUserChatMap.put("seen", "true");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final Map otherUserChatMap = new HashMap();
        otherUserChatMap.put("name", Method.getPreferences(context, "FULL_NAME"));
        otherUserChatMap.put("thumb_image", Method.getPreferences(context, "THUMB_IMAGE"));
        otherUserChatMap.put("last_message", lastMessage);
        otherUserChatMap.put("type", type);
        otherUserChatMap.put("time", ServerValue.TIMESTAMP);
        otherUserChatMap.put("from", FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDataBaseReferense.child("chat").child(other_user_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("seen").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            otherUserChatMap.put("seen", "false");
                            otherUserChatMap.put("count", 1);
                            updateChatHistory(currentUserChatMap, otherUserChatMap, message, message_push_id);
                        } else {
                            if (dataSnapshot.getValue().equals("true")) {
                                otherUserChatMap.put("seen", "true");
                                otherUserChatMap.put("count", 0);
                                updateChatHistory(currentUserChatMap, otherUserChatMap, message, message_push_id);
                            } else {
                                otherUserChatMap.put("seen", "false");
                                mDataBaseReferense.child("chat").child(other_user_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        unReadMessagesCount = (Long) dataSnapshot.getValue();
                                        otherUserChatMap.put("count", unReadMessagesCount + 1);
                                        updateChatHistory(currentUserChatMap, otherUserChatMap, message, message_push_id);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void updateChatHistory(Map currentUserChatMap, final Map otherUserChatMap, Map message, String message_push_id) {

        Map updateMessageAndList = new HashMap();
        updateMessageAndList.put("/messages" + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + other_user_id + "/" + message_push_id, message);
        updateMessageAndList.put("/messages" + "/" + other_user_id + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + message_push_id, message);

        updateMessageAndList.put("/chat" + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + other_user_id, currentUserChatMap);
        updateMessageAndList.put("/chat" + "/" + other_user_id + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid(), otherUserChatMap);
        mDataBaseReferense.updateChildren(updateMessageAndList).addOnCompleteListener(task -> {

        });
    }

    private void initialPageSetUp() {
        messageList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(context, messageList);
        binding.recyclerViewChat.setAdapter(messagesAdapter);

        mDataBaseReferense.child("chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(other_user_id).child("seen").setValue("true");
        mDataBaseReferense.child("chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(other_user_id).child("count").setValue(0);

        mDataBaseReferense.child("messages").child(other_user_id)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("seen").equalTo("false").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    mDataBaseReferense.child("messages").child(other_user_id)
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(child.getKey()).child("seen").setValue("true");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fetchMessagesAndUpdate();

        binding.etMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString()) && editable.toString().trim().length() == 1) {
                    typingStarted = true;
                    mDataBaseReferense.child("chat").child(other_user_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("typing").setValue("true");
                } else if (editable.toString().trim().length() == 0 && typingStarted) {
                    typingStarted = false;
                    mDataBaseReferense.child("chat").child(other_user_id).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("typing").setValue("false");
                }
            }
        });

    }

    private void fetchMessagesAndUpdate() {

        mDataBaseReferense.child("messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(other_user_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String messageTexts = null;
                if (dataSnapshot.child("message_text").getValue() != null) {
                    messageTexts = dataSnapshot.child("message_text").getValue().toString();
                }
                String seen = dataSnapshot.child("seen").getValue().toString();
                String time = String.valueOf(dataSnapshot.child("time").getValue());
                String type = dataSnapshot.child("type").getValue().toString();
                String from = dataSnapshot.child("from").getValue().toString();

                Date date = new Date(Long.valueOf(time));
                Format formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
                final String messageTime = formatter.format(date);


                MessageModel message = new MessageModel(messageTexts, seen, messageTime, type, from);
                messageList.add(message);
                messagesAdapter.notifyDataSetChanged();
                binding.recyclerViewChat.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
