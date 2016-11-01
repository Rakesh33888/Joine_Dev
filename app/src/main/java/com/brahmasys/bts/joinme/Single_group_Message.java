package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;

import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brahmasys.bts.joinme.message.ChatApplication;
//import com.brahmasys.bts.joinme.message.LoginActivity;
import com.brahmasys.bts.joinme.message.Message;
import com.brahmasys.bts.joinme.message.MessageAdapter;
import com.devsmart.android.ui.HorizontalListView;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class Single_group_Message extends Fragment    {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    private static final String URL_RemoveMemberToGroup = "http://52.37.136.238/JoinMe/Activity.svc/RemoveMemberToGroup/";
    private static final String URL_GetActivityDetailsForChat = "http://52.37.136.238/JoinMe/Activity.svc/GetActivityDetailsForChat/";
    ProgressDialog progressDialog;
    private  CircularImageView listimage;
    FragmentManager fragmentManager;
    private static final String IMAGE_BASE_URL = "http://52.37.136.238/JoinMe/";
    CircularImageView createrimage,owner;
    HorizontalListView participants_list;
    Button yes,no;
    String act_id,userid,owner_id="0000";
    ImageView shareicon;
    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    TextView tvActivityName,tvActivityTime,tvActivityAddress,tvHostedByName,tvleave_chat;
    private String mParam1;
    private String mParam2;
    private ArrayList<Book> books;
    private ArrayList<String> other_user_id;
    private ArrayAdapter<Book> adapter;
    Context context;
    private OnFragmentInteractionListener mListener;

    /********************Chat********************/
    private static final int REQUEST_LOGIN = 0;
    private static final int TYPING_TIMER_LENGTH = 600;
    private RecyclerView mMessagesView;
    private EditText mInputMessageView;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    //private String mUsername;
    private Socket mSocket;
    private Boolean isConnected = true;
    ImageView send_btn;
    String sender_id,group_id;
    String user_porfile,UserName;
    private static final String CHAT_USER="chat_username";
    SharedPreferences chat_username;
    SharedPreferences.Editor edit_chat_username;
    /********************Chat********************/

    public Single_group_Message() {
        // Required empty public constructor
    }
   //TextView name;
   @Override
   public void onAttach(Activity activity) {
       super.onAttach(activity);
       mAdapter = new MessageAdapter(activity, mMessages);
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       /********************Chat********************/
       setHasOptionsMenu(true);

       ChatApplication app = (ChatApplication) getActivity().getApplication();
        mSocket = app.getSocket();
        mSocket.on("GetGroupMessages", onNewMessage);
        mSocket.on("typing", onTyping);
        mSocket.on("getmessages",getMessage);
        mSocket.connect();
      // mSocket.on("user joined", onUserJoined);
//       mSocket.on("user left", onUserLeft);
        // mSocket.on("stop typing", onStopTyping);


       /********************Chat********************/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        progressDialog =ProgressDialog.show(getActivity(), null, null, true);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = inflater.inflate(R.layout.fragment_single_group__message, container, false);
        owner   = (CircularImageView) v.findViewById(R.id.hosted);
        Bundle bundle = this.getArguments();
        act_id  = bundle.getString("activityid","0");
        userid = bundle.getString("userid","0");
        sender_id = userid;
        group_id = act_id;
        Log.e("Join Group", group_id + "\n" + sender_id);
        /**************Join Group ****************/
        mSocket.emit("joingroup", sender_id, group_id);
        /**************Join Group ****************/

        chat_username = getActivity().getSharedPreferences(CHAT_USER,getActivity().MODE_PRIVATE);
        edit_chat_username = chat_username.edit();
        createrimage = (CircularImageView) v.findViewById(R.id.createrimage1);
        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);
        other_user_id=new ArrayList<String>();
        createrimage.setClickable(true);
        send_btn = (ImageView) v.findViewById(R.id.send_button);
        /********************Chat********************/

        /********************Chat********************/


        createrimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Screen17 screen17 = new Screen17();
                FragmentManager fm = getFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("userid",userid);
                bundle.putString("activityid",act_id);
                screen17.setArguments(bundle);
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.flContent, screen17);
                transaction.commit();

            }
        });
        tvActivityName= (TextView) v.findViewById(R.id.textView25);
        tvActivityTime= (TextView) v.findViewById(R.id.textView26);
        tvleave_chat= (TextView) v.findViewById(R.id.leave_chat);
        listimage= (CircularImageView)v.findViewById(R.id.participants);
        tvActivityAddress = (TextView) v.findViewById(R.id.textView27);
        tvHostedByName = (TextView) v.findViewById(R.id.name);
        participants_list = (HorizontalListView) v. findViewById(R.id.participants_list);
        participants_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<other_user_id.size();i++){
                    if (position==other_user_id.indexOf(other_user_id.get(i))){

                        Screen13 screen13 = new Screen13();
                        FragmentManager fm1 = getFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putString("userid",other_user_id.get(i));
                        screen13.setArguments(bundle);
                        FragmentTransaction transaction = fm1.beginTransaction();
                        transaction.replace(R.id.flContent, screen13);
                        transaction.commit();

                    }
                }

            }
        });
        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();
        tvleave_chat.setPaintFlags(tvleave_chat.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvleave_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Connectivity_Checking.isConnectingToInternet()) {


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    yes = (Button) dialog.findViewById(R.id.yes);
                    no = (Button) dialog.findViewById(R.id.no);
                    yes.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            FnLeaveChat();
                            dialog.dismiss();
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                } else {
                    Splashscreen dia = new Splashscreen();
                    dia.Connectivity_Dialog_with_refresh(getActivity());
                    progressDialog.dismiss();
                }

            }
        });
        context=getActivity();
        setListViewAdapter();

       if (Connectivity_Checking.isConnectingToInternet()) {

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL_GetActivityDetailsForChat + act_id+"/"+userid,
                    new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http response code '200'

                        public void onSuccess(String response) {
                            // Hide Progress Dialog
                            //  prgDialog.hide();
                            try {
                                // Extract JSON Object from JSON returned by REST WS
                                JSONObject obj = new JSONObject(response);


                                JSONObject json = null;
                                try {
                                    json = new JSONObject(String.valueOf(obj));

                                    group_id  = json.getString("activityid");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                /************************* UserDetails start **************************/
                                JSONObject apiResponse = null;
                                try {
                                    apiResponse = json.getJSONObject("response");
                                    Log.e("GetDetailsForChat", String.valueOf(apiResponse));
//                                    edit_userid.putString("userid", apiResponse.getString("userid"));
//                                    edit_userid.commit();
                                    String ststus =apiResponse.getString("status");
                                     sender_id = userid;
                                    if(ststus.equals("200")){
                                        JSONArray arrGroup = json.getJSONArray("group_list");

                                        String activutyImage = json.getString("activity_pic");
                                        String txtActivityName = json.getString("activity_title");
                                        Integer txtActivityTime = json.getInt("activity_time");
                                        String txtAddress = json.getString("activity_address");

                                        Picasso.with(getContext()).load(IMAGE_BASE_URL + activutyImage).placeholder(R.drawable.butterfly)
                                                .into(createrimage);

                                        tvActivityName.setText(txtActivityName);

                                        long unixSeconds = Long.parseLong(String.valueOf(txtActivityTime));
                                        Date date2 = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' hh aa "); // the format of your date
                                        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // give a timezone reference for formating (see comment at the bottom
                                        String value = sdf.format(date2);


                                        tvActivityTime.setText(String.valueOf(value));


                                        tvActivityAddress.setText(txtAddress);

                                        for(int i=0; i<arrGroup.length(); i++) {
                                           JSONObject row = arrGroup.getJSONObject(i);
                                            if(row.getString("isowner").equals("true")){
                                                tvHostedByName.setText(row.getString("user_name"));
                                                owner_id = row.getString("userid");
                                                Picasso.with(getContext()).load(IMAGE_BASE_URL + row.getString("profile_pic")).placeholder(R.drawable.butterfly)
                                                        .into(owner);
                                                if(userid.equals(owner_id))
                                                {
                                                    UserName = row.getString("user_name");
                                                    user_porfile=row.getString("profile_pic");
                                                    edit_chat_username.putString("chat_username",UserName);
                                                    edit_chat_username.commit();
                                                }
                                            }
                                            else
                                            {
                                                Book book = new Book();
                                                book.setImageUrl(row.getString("profile_pic"));
                                                other_user_id.add(row.getString("userid"));
                                                books.add(book);
                                                if(userid.equals(row.getString("userid")))
                                                {
                                                    UserName = row.getString("user_name");
                                                    user_porfile=row.getString("profile_pic");

                                                    edit_chat_username.putString("chat_username",UserName);
                                                    edit_chat_username.commit();
                                                }
                                            }

                                        }
                                        adapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();


                        }
                    });



        } else {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());
            progressDialog.dismiss();
        }
owner.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        fragmentManager = getFragmentManager();
        Screen13 screen13 = new Screen13();
        Bundle bundle = new Bundle();
        bundle.putString("owner_id", owner_id);
        screen13.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, screen13)
                .addToBackStack(null)
                .commit();
    }
});
 participants_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     @Override
     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         fragmentManager = getFragmentManager();
         Screen13 screen13 = new Screen13();
         Bundle bundle = new Bundle();
         bundle.putString("owner_id", other_user_id.get(position));
         screen13.setArguments(bundle);
         fragmentManager.beginTransaction()
                 .replace(R.id.flContent, screen13)
                 .addToBackStack(null)
                 .commit();
     }
 });

        return v;
    }


    /********************Chat********************/
    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
          mSocket.off("GetGroupMessages", onNewMessage);
          mSocket.off("getmessages",getMessage);
//        mSocket.off("user joined", onUserJoined);
//        mSocket.off("user left", onUserLeft);
          mSocket.off("typing", onTyping);
       //   mSocket.off("stop typing", onStopTyping);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessagesView = (RecyclerView) view.findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);

        mInputMessageView = (EditText) view.findViewById(R.id.message_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
//        mInputMessageView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//             //   if (null == mUsername) return;
//                if (!mSocket.connected()) return;
//
//                if (!mTyping) {
//                    mTyping = true;
//                    mSocket.emit("ontyping","leo");
//                }
//
//                mTypingHandler.removeCallbacks(onTypingTimeout);
//                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });


//        mInputMessageView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//             //   if (null == mUsername) return;
//                if (!mSocket.connected()) return;
//
//                if (!mTyping) {
//                    mTyping = true;
//                    mSocket.emit("ontyping","narendra");
//                }
//
//
////                mTypingHandler.removeCallbacks(onTypingTimeout);
////                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                removeTyping("narendra");
                attemptSend();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            getActivity().finish();
            return;
        }

//        mUsername = data.getStringExtra("username");
//        int numUsers = data.getIntExtra("numUsers", 1);

    //     addLog(getResources().getString(R.string.message_welcome));
//        addParticipantsLog(numUsers);
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

//    private void addParticipantsLog(int numUsers) {
//        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
//    }

    private void addMessage(String username, String message , String profileurl,String chat_time) {

        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).ProfileUrl(profileurl).ChatTime(chat_time).build());

        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addMessage1(String username, String message, String profileurl,String chat_time) {

        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE_USER)
                .username(username).message(message).ProfileUrl(profileurl).ChatTime(chat_time).build());

        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }
    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void attemptSend() {
       // if (null == mUsername) return;
      //  if (!mSocket.connected()) return;

        mTyping = false;
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }
        else {
            mInputMessageView.setText("");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("hh:mm: aa");
            String Chat_Time = df.format(c.getTime());
            addMessage1(UserName, message, user_porfile,Chat_Time);
            mSocket.emit("sendchat", sender_id, UserName, user_porfile, group_id, message);
        }
    }


    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (!args.equals("")) {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray data1 = (JSONArray) args[0];
                            Log.e("RESULT", String.valueOf(data1));
                            String username = null;
                            String message = null;
                            String profile_url = null;
                            String s_senderid=null;
                            String chat_time=null;
                            for (int i = 0; i < data1.length(); i++) {

                                JSONObject jsonobject = null;
                                try {
                                    jsonobject = (JSONObject) data1.get(i);

                                    s_senderid=jsonobject.optString("senderid");
                                    username = jsonobject.getString("s_username");
                                    message = jsonobject.getString("message");
                                    profile_url = jsonobject.getString("u_userpic");

                                    String dtStart = jsonobject.getString("timestamp");
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                    format.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    try {
                                        Date date = format.parse(dtStart);
                                        System.out.println(date);
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                                        chat_time = dateFormat.format(date);
                                    } catch (ParseException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        if (sender_id!=null && sender_id.equals(s_senderid))
                        {

                            addMessage1(username, message,profile_url,chat_time);
                        }
                        else {

                                addMessage(username, message,profile_url,chat_time);
                                 }
                            }

                        }
                    });
                    return;
                }
            }
        }
    };

    private Emitter.Listener getMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (!args.equals("")) {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          JSONObject data = (JSONObject) args[0];
                         //   JSONArray data1 = (JSONArray) args[0];
                            Log.e("RESULT", String.valueOf(data));
                            String username = null;
                            String message = null;
                            String profile_url = null;
                            String s_senderid=null;
                            String chat_time=null;
                            try {
                                s_senderid=data.optString("senderid");
                                username = data.getString("s_username");
                                message = data.getString("message");
                                profile_url = data.getString("u_userpic");

                                String dtStart = data.getString("timestamp");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                                try {
                                    Date date = format.parse(dtStart);
                                    System.out.println(date);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                                    chat_time = dateFormat.format(date);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                                if (sender_id!=null && sender_id.equals(s_senderid))
                                {

                                    addMessage1(username, message,profile_url,chat_time);
                                }
                                else {

                                    addMessage(username, message,profile_url,chat_time);
                              }
                          // }

                        }
                    });
                    return;
                }
            }
        }
    };
//    private Emitter.Listener onUserJoined = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    int numUsers;
//                    try {
//                        username = data.getString("username");
//                        numUsers = data.getInt("numUsers");
//                    } catch (JSONException e) {
//                        return;
//                    }
//
//                    addLog(getResources().getString(R.string.message_user_joined, username));
//                    addParticipantsLog(numUsers);
//                }
//            });
//        }
//    };
//
//    private Emitter.Listener onUserLeft = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    int numUsers;
//                    try {
//                        username = data.getString("username");
//                        numUsers = data.getInt("numUsers");
//                    } catch (JSONException e) {
//                        return;
//                    }
//
//                    addLog(getResources().getString(R.string.message_user_left, username));
//                    addParticipantsLog(numUsers);
//                    removeTyping(username);
//                }
//            });
//        }
//    };
//
    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("TYPING",String.valueOf(args));
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    Log.e("TYPING",String.valueOf(data));
//                    String username;
//                    try {
//                        username = data.getString("username");
//                    } catch (JSONException e) {
//                        return;
//                    }
//                    if (!UserName.equals("leo"))
//                    {
                    addTyping("leo");
            //    }

                }
            });
        }
    };

//    private Emitter.Listener onStopTyping = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    try {
//                        username = data.getString("username");
//                    } catch (JSONException e) {
//                        return;
//                    }
//                    removeTyping(username);
//                }
//            });
//        }
//    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
           // mSocket.emit("stop typing");
        }
    };
    /********************Chat********************/

    private void setListViewAdapter() {
        books = new ArrayList<Book>();
        adapter = new custom_listview_participants(getActivity(), R.layout.participants, books);
        participants_list.setAdapter(adapter);
       // progressDialog.dismiss();

    }

    private void FnLeaveChat() {
        userid = user_id.getString("userid", "");
      //  String act_id = activity_id.getString("activity_id", "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_RemoveMemberToGroup + userid + "/" + act_id,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {
                        Messagescreen fragment2 = new Messagescreen();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.flContent, fragment2);
                        fragmentTransaction.commit();

                    }



                });


    }




        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Messagescreen fragment2 = new Messagescreen();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, fragment2);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
    }


}
