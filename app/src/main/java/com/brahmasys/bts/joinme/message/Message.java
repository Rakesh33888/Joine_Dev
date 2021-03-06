package com.brahmasys.bts.joinme.message;

public class Message {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_MESSAGE_USER=3;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;

    private int mType;
    private String mMessage,userMessage,mProfile_url;
    private String mUsername,userUsername,chat_time;

    private Message() {}

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };

    public String getProfileUrl(){return mProfile_url;};
    public String getChat_time(){return chat_time;}

    public static class Builder {
        private final int mType;
        private String mUsername,userUsername,mProfile_url,chat_time;
        private String mMessage,userMessage;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

       public Builder ProfileUrl(String profileurl)
       {
           mProfile_url = profileurl;
           return this;
       }

    public Builder ChatTime(String chatTime)
    {
        chat_time=chatTime;
        return this;
    }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.userMessage = userMessage;
            message.userUsername = userUsername;
            message.mProfile_url = mProfile_url;
            message.chat_time = chat_time;
            return message;
        }
    }
}
