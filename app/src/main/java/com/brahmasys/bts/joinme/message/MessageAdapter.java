package com.brahmasys.bts.joinme.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brahmasys.bts.joinme.Constant;
import com.brahmasys.bts.joinme.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int[] mUsernameColors;
    String username_verify="null";
    Context context;
    private static final String CHAT_USER="chat_username";
    SharedPreferences chat_username;
    SharedPreferences.Editor edit_chat_username;

    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
        case Message.TYPE_MESSAGE:
            layout = R.layout.item_message;
            break;
        case Message.TYPE_MESSAGE_USER:
                layout = R.layout.item_message_user;
                break;

            case Message.TYPE_LOG:
            layout = R.layout.item_log;
            break;
        case Message.TYPE_ACTION:
            layout = R.layout.item_action;
            break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsername(message.getUsername());
        viewHolder.setProfileurl(message.getProfileUrl());
        viewHolder.setChatTime(message.getChat_time());

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView,mUsernameView1,chat_time;
        private TextView mMessageView,mMessageView1;
        CircularImageView profile_url,profile_url1;

        public ViewHolder(View itemView) {
            super(itemView);
            chat_username = context.getSharedPreferences(CHAT_USER, context.MODE_PRIVATE);
            edit_chat_username = chat_username.edit();

            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            profile_url = (CircularImageView) itemView.findViewById(R.id.chat_profile);
            chat_time   = (TextView) itemView.findViewById(R.id.chat_time);
            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            profile_url = (CircularImageView) itemView.findViewById(R.id.chat_profile);
            chat_time   = (TextView) itemView.findViewById(R.id.chat_time);

        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
//            Log.e("USERNAME",chat_username.getString("chat_username","null"));
//                if (chat_username.getString("chat_username","null").equals(username))
//                {   username_verify = username;
//                    mUsernameView1.setText(username);
//                    mUsernameView1.setTextColor(getUsernameColor(username));
//                }
//            else {
                    username_verify = username;
                    mUsernameView.setText(username);
                    mUsernameView.setTextColor(getUsernameColor(username));
               // }
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;

                mMessageView.setText(message);

        }

        public void setProfileurl(String profileUrl)
        {
            if (null == profile_url) return;
//            Picasso.with(context).load("http://52.37.136.238/JoinMe/" + profileUrl).placeholder(R.drawable.butterfly)
//                    .into(profile_url);

            Picasso.with(context)
                    .load(Constant.BASE_URL + profileUrl) // thumbnail url goes here
                    .placeholder(R.drawable.butterfly)
                    .resize(90, 90)
                    .noFade().centerCrop()
                    .into(profile_url, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                        }
                    });

        }
        public  void setChatTime(String chatTime)
        {
            if(null== chatTime)return;

            chat_time.setText(chatTime);
        }

    }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }

