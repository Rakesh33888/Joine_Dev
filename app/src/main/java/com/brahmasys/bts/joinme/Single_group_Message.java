package com.brahmasys.bts.joinme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class Single_group_Message extends Fragment    {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    private static final String URL_RemoveMemberToGroup = "http://52.37.136.238/JoinMe/Activity.svc/RemoveMemberToGroup/";
    private static final String URL_GetActivityDetailsForChat = "http://52.37.136.238/JoinMe/Activity.svc/GetActivityDetailsForChat/";
    ProgressDialog progressDialog;
    private  CircularImageView listimage;


    private static final String IMAGE_BASE_URL = "http://52.37.136.238/JoinMe/";


    CircularImageView createrimage,owner;
    HorizontalListView participants_list;
    Button yes,no;
    String act_id,userid;
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

        progressDialog =ProgressDialog.show(getActivity(), null, null, true);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = inflater.inflate(R.layout.fragment_single_group__message, container, false);
         // v.setBackgroundResource(R.drawable.colourbubble);

        owner   = (CircularImageView) v.findViewById(R.id.hosted);
        Bundle bundle = this.getArguments();
        act_id  = bundle.getString("activityid","0");
        userid = bundle.getString("userid","0");
        createrimage = (CircularImageView) v.findViewById(R.id.createrimage1);


        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);

        other_user_id=new ArrayList<String>();

        createrimage.setClickable(true);
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

      //  String userid = user_id.getString("userid", "");
      //  String act_id = activity_id.getString("activity_id", "");
      //  String act_id ="5762432ed72fc30d6853e39b";
        //populate the date from the activity

        if (Connectivity_Checking.isConnectingToInternet()) {

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
                                    Log.e("GetDetailsForChat", String.valueOf(apiResponse));
//                                    edit_userid.putString("userid", apiResponse.getString("userid"));
//                                    edit_userid.commit();
                                    String ststus =apiResponse.getString("status");
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
                                            if(row.getBoolean("isowner")){
                                                tvHostedByName.setText(row.getString("user_name"));
                                                Picasso.with(getContext()).load(IMAGE_BASE_URL + row.getString("profile_pic")).placeholder(R.drawable.butterfly)
                                                        .into(owner);
                                            }
                                            else
                                            {
                                                Book book = new Book();
                                                book.setImageUrl(row.getString("profile_pic"));
                                                other_user_id.add(row.getString(userid));
                                                books.add(book);

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




        return v;
    }

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
