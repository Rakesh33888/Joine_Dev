package com.brahmasys.bts.joinme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mygroup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mygroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mygroup extends Fragment{
    Expandable_GridView groups_list;
    Button  createalbum;
    ImageView image,imageback1;
    FragmentManager fragmentManager;
    LinearLayout backlayoutgroup;
    public static final String CHAT_ROOM_OPEN="chat_room_open";
    private static final String TAG = "GetMyGroupActivity";
    private static final String URL = Constant.GetMyGroupActivity;
    ImageView shareicon,msg,logo;
    private ArrayList<Book> books;
    private ArrayAdapter<Book> adapter;
    Context context;
    public static final String USERID = "userid";
    SharedPreferences user_id,user_Details,user_pic,lat_lng,chat_room;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic,edit_lat_lng,edit_chat_room;

    List<String> allactivityid;
    List<String> alluserid;
    ProgressDialog progressDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Mygroup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter nari1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mygroup.
     */
    // TODO: Rename and change types and number of parameters
    public static Mygroup newInstance(String param1, String param2) {
        Mygroup fragment = new Mygroup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        chat_room = getActivity().getSharedPreferences(CHAT_ROOM_OPEN, getActivity().MODE_PRIVATE);
        edit_chat_room = chat_room.edit();
        edit_chat_room.putString("chat_room","close");
        edit_chat_room.putString("chat_activity","0");
        edit_chat_room.commit();
        View v= inflater.inflate(R.layout.fragment_mygroup, container, false);
//        progressDialog =ProgressDialog.show(getActivity(), null, null, true);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(false);
//        progressDialog.setContentView(R.layout.custom_progress);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressDialog = ProgressDialog.show(getActivity(), null,null, true);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);
        msg = (ImageView) refTool.findViewById(R.id.msg);
        msg.setBackgroundResource(R.drawable.custo_msg);
        logo = (ImageView) refTool.findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog =ProgressDialog.show(getActivity(), null, null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Intent i = new Intent(getContext(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
            }
        });
        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        context=getActivity();
        groups_list =  (Expandable_GridView) v.findViewById(R.id.group_grid);
        groups_list.setExpanded(true);
        setListViewAdapter();




        backlayoutgroup= (LinearLayout) v.findViewById(R.id.backlayoutgroup);
        backlayoutgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog =ProgressDialog.show(getActivity(), null, null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Intent i = new Intent(getContext(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();

            }
        });




        imageback1= (ImageView) v.findViewById(R.id.backtogroupsetting);
        imageback1.setClickable(true);
        imageback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        createalbum= (Button) v.findViewById(R.id.createnewactivity);
//


        Display display = getActivity().getWindowManager().getDefaultDisplay();
       int stageWidth = display.getWidth();
       int  stageHeight = display.getHeight();
        Log.e("WIDTH",String.valueOf(stageWidth));
        ViewGroup.LayoutParams params = null;

        params =  createalbum .getLayoutParams();
        params.height =(stageWidth/2)-21;
        params.width = (stageWidth/2)-21;
        createalbum .setLayoutParams(params);

        createalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();

                Screen19 screen19 = new Screen19();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, screen19)
                        .addToBackStack(null)
                        .commit();

            }
        });



        new Thread(new Runnable() {
            @Override
            public void run()
            {
                if(Connectivity_Checking.isConnectingToInternet()) {
                Getting_Groups();
            }
            else
            {
                progressDialog.dismiss();
//                fragmentManager=getFragmentManager();
//                Mygroup screen17 = new Mygroup();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.flContent, screen17)
//                        .addToBackStack(null)
//                        .commit();
            }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();

                    }
                });

            }
        }).start();


        groups_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentManager=getFragmentManager();
                Screen17 screen17 = new Screen17();
                Bundle bundle = new Bundle();
                bundle.putString("userid", alluserid.get(position));
                bundle.putString("activityid",allactivityid.get(position));
                bundle.putString("where","mygroups");
                screen17.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, screen17)
                        .addToBackStack(null)
                        .commit();

            }
        });


        return v;
    }


    public  void Getting_Groups()
    {


            JSONObject jsonObjSend = new JSONObject();
            try {
                // Add key/value pairs
                jsonObjSend.put("lat", "0");
                jsonObjSend.put("lon", "0");
                jsonObjSend.put("userid",user_id.getString("userid","0"));
                Log.i(TAG, jsonObjSend.toString(3));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jsonObjRecv = HttpClient.SendHttpPost(URL, jsonObjSend);

            //Log.w("RESULT",String.valueOf(jsonObjRecv));
            JSONObject json = null;
            try {
                json = new JSONObject(String.valueOf(jsonObjRecv));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            alluserid = new ArrayList<String>();
            allactivityid = new ArrayList<String>();


            JSONArray userdetails = null;


            try {
                userdetails = json.getJSONArray("data");
                if (userdetails.length()>0) {

                for (int i = 0; i < userdetails.length(); i++) {
                    JSONObject actor = userdetails.getJSONObject(i);
                    String activity_id = actor.getString("activity_id");
                    String user_id = actor.getString("userid");

                    allactivityid.add(activity_id);
                    alluserid.add(user_id);
                    Book book = new Book();
                    book.setName(actor.getString("activity_name"));
                    book.setImageUrl(actor.getString("activity_url"));

                    long unixSeconds = Long.parseLong(actor.getString("activity_time"));
                    Date date2 = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' hh:mm aa "); // the format of your date
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // give a timezone reference for formating (see comment at the bottom
                    String formattedDate = sdf.format(date2);
                    Log.e("Timestamp527", String.valueOf(formattedDate));

//                    long timestampString =  Long.parseLong(actor.getString("activity_time"));
//                    String value = new java.text.SimpleDateFormat("dd.MM.yyyy 'at' hh aa ").
//                            format(new java.util.Date(timestampString * 1000L));

                    book.setAuthorName(formattedDate);

                    books.add(book);

                }
                // adapter.notifyDataSetChanged();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                //Log.w("details",String.valueOf(userdetails));
            }
            }catch (JSONException e) {
                e.printStackTrace();
            }


    }

    private void setListViewAdapter() {
        if (Connectivity_Checking.isConnectingToInternet()) {


            books = new ArrayList<Book>();
            adapter = new CustomListViewAdapter(getActivity(), R.layout.groups_list, books);
            groups_list.setAdapter(adapter);


        } else {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());
        }
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    progressDialog =ProgressDialog.show(getActivity(), null, null, true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.custom_progress);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Intent i = new Intent(getActivity(), Screen16.class);
                            startActivity(i);
                            getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                            getActivity().finish();

                    return true;
                }
                return false;
            }
        });
    }


}
