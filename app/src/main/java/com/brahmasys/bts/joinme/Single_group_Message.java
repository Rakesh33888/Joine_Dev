package com.brahmasys.bts.joinme;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Single_group_Message extends Fragment    {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    private static final String URL_RemoveMemberToGroup = "http://52.37.136.238/JoinMe/Activity.svc/RemoveMemberToGroup/";
    private static final String URL_GetActivityDetailsForChat = "http://52.37.136.238/JoinMe/Activity.svc/GetActivityDetailsForChat/";




    private static final String IMAGE_BASE_URL = "http://52.37.136.238/JoinMe/";


    CircularImageView createrimage,owner;
    Button yes,no;
    String act_id;
    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    TextView tvActivityName,tvActivityTime,tvActivityAddress,tvHostedByName,tvleave_chat;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Single_group_Message() {
        // Required empty public constructor
    }
   //TextView name;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_single_group__message, container, false);

        owner   = (CircularImageView) v.findViewById(R.id.hosted);
        createrimage = (CircularImageView) v.findViewById(R.id.createrimage);
        tvActivityName= (TextView) v.findViewById(R.id.textView25);
        tvActivityTime= (TextView) v.findViewById(R.id.textView26);
        tvleave_chat= (TextView) v.findViewById(R.id.leave_chat);
        tvActivityAddress = (TextView) v.findViewById(R.id.textView27);
        tvHostedByName = (TextView) v.findViewById(R.id.name);

        Bundle bundle = this.getArguments();

        act_id  = bundle.getString("activityid","0");


        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();
        tvleave_chat.setPaintFlags(tvleave_chat.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvleave_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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





            }
        });

      //  String userid = user_id.getString("userid", "");
      //  String act_id = activity_id.getString("activity_id", "");
      //  String act_id ="5762432ed72fc30d6853e39b";
        //populate the date from the activity
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_GetActivityDetailsForChat + act_id,
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            /************************* UserDetails start **************************/
                            JSONObject apiResponse = null;
                            try {
                                apiResponse = json.getJSONObject("response");
                                Log.i("GetDetailsForChat",String.valueOf(apiResponse));
                                String ststus =apiResponse.getString("status");
                                if(ststus.equals("200")){
                                    JSONArray arrGroup = json.getJSONArray("group_list");

                                    String activutyImage = json.getString("activity_pic");
                                    String txtActivityName = json.getString("activity_title");
                                    Integer txtActivityTime = json.getInt("activity_time");
                                    String txtAddress = json.getString("activity_address");

                                    Picasso.with(getContext()).load(IMAGE_BASE_URL + activutyImage).into(createrimage);

                                    tvActivityName.setText(txtActivityName);
                                    long timestampString =  Long.parseLong(String.valueOf(txtActivityTime));
                                    String value = new java.text.SimpleDateFormat("dd.MM.yyyy 'at' KK aa ").
                                            format(new java.util.Date(timestampString * 1000));

                                    tvActivityTime.setText(String.valueOf(value));


                                    tvActivityAddress.setText(txtActivityName);

                                    for(int i=0; i<arrGroup.length(); i++) {


                                        JSONObject row = arrGroup.getJSONObject(i);
                                        if(row.getBoolean("isowner")){
                                            tvHostedByName.setText(row.getString("user_name"));
                                            Picasso.with(getContext()).load(IMAGE_BASE_URL + row.getString("profile_pic")).into(owner);
                                        }
                                        else
                                        {

                                        }

                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


        return v;
    }
    private void FnLeaveChat() {
        String userid = user_id.getString("userid", "");
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
