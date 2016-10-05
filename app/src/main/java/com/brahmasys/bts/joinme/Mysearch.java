package com.brahmasys.bts.joinme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mysearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mysearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mysearch extends android.support.v4.app.Fragment {

    //static
    public static final String USERID = "userid";
    ProgressDialog progressDialog;

    public static String dateSelection = "";
    String distanceStr,date;
    private static final String TAG = "SaveUserPreference";
    public  static final String URL_SaveUserPreference ="http://52.37.136.238/JoinMe/User.svc/SaveUserPreference";
 //   ProgressDialog pd;

    LinearLayout linearLayout,layoutback;
    RelativeLayout relativeLayout1,relativeLayout2;
    ImageView back;
    ImageView shareicon;
    private ActionBar toolbar;
    TextView mysearch,textView7,Maxdistance,distance,distance_type;
    CheckBox c1,c2,c3;

    RelativeLayout relativeLayout_share_icon;
    SeekBar seekBar;
    String search_distance;
    String select_date;
   
    String Km="",Mile="",dist_type;
    SharedPreferences searchdistance,selectdate;
    SharedPreferences.Editor edit_searchdistance,edit_selectdate;
    SharedPreferences user_id,user_Details,user_pic,lat_lng;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic,edit_lat_lng;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Mysearch() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter nari1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mysearch.
     */
    // TODO: Rename and change types and number of parameters
    public static Mysearch newInstance(String param1, String param2) {
        Mysearch fragment = new Mysearch();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mysearch, container, false);
        progressDialog =ProgressDialog.show(getActivity(), null, null, true);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);
        relativeLayout1 = (RelativeLayout) v.findViewById(R.id.relativelayout_seek);
        relativeLayout2 = (RelativeLayout) v.findViewById(R.id.relativelayout_checkbox);
        textView7 = (TextView) v.findViewById(R.id.textView7);
        distance_type = (TextView) v.findViewById(R.id.distance_type);
        Maxdistance= (TextView) v.findViewById(R.id.Maxdistance);
        distance= (TextView) v.findViewById(R.id.distance);
        seekBar= (SeekBar) v.findViewById(R.id.seekBar);
        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);



        mysearch = (TextView) v.findViewById(R.id.textView6);
        layoutback= (LinearLayout) v.findViewById(R.id.backlayoutsearch);
        layoutback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!distanceStr.equals(distance.getText().toString())||!date.equals(dateSelection)) {
                    String seekBarValue = Integer.toString(seekBar.getProgress());
                    FnPostRequest(seekBarValue, dateSelection, user_id.getString("userid", ""));
                    Intent i = new Intent(getContext(), Screen16.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                    getActivity().finish();
                }
                else {
                    Intent i = new Intent(getContext(), Screen16.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                    getActivity().finish();

                }

            }
        });


        searchdistance =getActivity().getSharedPreferences(search_distance, getActivity().MODE_PRIVATE);
        edit_searchdistance = searchdistance.edit();
        selectdate=getActivity().getSharedPreferences(select_date, getActivity().MODE_PRIVATE);
        edit_selectdate=selectdate.edit();

        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(Connectivity_Checking.isConnectingToInternet()) {
            AsyncHttpClient client = new AsyncHttpClient();
            String id = user_id.getString("userid", "");
            client.get("http://52.37.136.238/JoinMe/User.svc/GetUserSettings/" + id,
                    new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http response code '200'

                        public void onSuccess(String jsonResult) {

                            try {
                                // Extract JSON Object from JSON returned by REST WS
                                JSONObject dataObj = new JSONObject(jsonResult).getJSONObject("user_setting");
                                final String distance_km = dataObj.getString("distance_km");

                                if (distance_km.equals("K") || distance_km.equals("k")) {
                                   Km =distance_km;
                                    dist_type = "km";
                                    Log.e("DISTANCE",Km);
                                } else {
                                    Mile = distance_km;
                                    dist_type="miles";
                                    Log.e("DISTANCE",distance_km);
                                }

                                AsyncHttpClient client = new AsyncHttpClient();
                                client.get("http://52.37.136.238/JoinMe/User.svc/GetUserPreference/"+ user_id.getString("userid",""),
                                        new AsyncHttpResponseHandler() {
                                            // When the response returned by REST has Http response code '200'

                                            public void onSuccess(String response) {
                                                // Hide Progress Dialog
                                                //  prgDialog.hide();
                                                try {
                                                    // Extract JSON Object from JSON returned by REST WS
                                                    JSONObject dataObj = new JSONObject(response).getJSONObject("data");
                                                    distanceStr = dataObj.getString("search_distance");
                                                    date = dataObj.getString("select_date");
                                                    // Toast.makeText(getContext(), distanceStr, Toast.LENGTH_SHORT).show();

                                                    if (Mile.equals(distance_km))
                                                    {
//                                                        double oneMile=0.62137;
//                                                        double distanceM= Integer.parseInt(distanceStr)*oneMile;
//                                                        long distM=(long)distanceM;
//
//                                                        int DISTANCE_Miles = (int)distM;
//                                                        distance.setText(String.valueOf(DISTANCE_Miles));
                                                        distance_type.setText(dist_type);
//                                                        seekBar.setProgress(DISTANCE_Miles);
//                                                        Log.e("DISTANCE", String.valueOf(DISTANCE_Miles));

                                                    }
                                                    else
                                                    {
                                                        distance.setText(distanceStr);
                                                        distance_type.setText(dist_type);

                                                    }

                                                    seekBar.setProgress(Integer.parseInt(distanceStr));
                                                    if (date.equals("0")) {
                                                        c1.setChecked(true);
                                                        c2.setChecked(false);
                                                        c3.setChecked(false);
                                                        c1.setClickable(false);
                                                        c2.setClickable(true);
                                                        c3.setClickable(true);

                                                    }
                                                    else if (date.equals("nari1")) {
                                                        c1.setChecked(false);
                                                        c2.setChecked(true);
                                                        c3.setChecked(false);
                                                        c2.setClickable(false);
                                                        c1.setClickable(true);
                                                        c3.setClickable(true);
                                                    }
                                                    else if (date.equals("3")) {
                                                        c1.setChecked(false);
                                                        c2.setChecked(false);
                                                        c3.setChecked(true);
                                                        c3.setClickable(false);
                                                        c1.setClickable(true);
                                                        c2.setClickable(true);
                                                    }
                                                    else
                                                    {
                                                        c1.setChecked(true);
                                                        c2.setChecked(false);
                                                        c3.setChecked(false);

                                                    }

                                                    // pd.hide();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                progressDialog.dismiss();

                                            }});


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    });
        }
        else
        {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());
        }

//        if(Connectivity_Checking.isConnectingToInternet()) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get("http://52.37.136.238/JoinMe/User.svc/GetUserPreference/"+ user_id.getString("userid",""),
//                new AsyncHttpResponseHandler() {
//                    // When the response returned by REST has Http response code '200'
//
//                    public void onSuccess(String response) {
//                        // Hide Progress Dialog
//                        //  prgDialog.hide();
//                        try {
//                            // Extract JSON Object from JSON returned by REST WS
//                            JSONObject dataObj = new JSONObject(response).getJSONObject("data");
//                            distanceStr = dataObj.getString("search_distance");
//                            date = dataObj.getString("select_date");
//                           // Toast.makeText(getContext(), distanceStr, Toast.LENGTH_SHORT).show();
//
//                            if (Km.equals("k"))
//                            {
//                                distance.setText(distanceStr + "km");
//                            }
//                            else
//                            {
//                                double oneMile=0.62137;
//                                double distanceKm= Integer.parseInt(distanceStr)*oneMile;
//
//                                Log.e("DISTANCE",String.valueOf(distanceKm));
//                            }
//
//                            seekBar.setProgress(Integer.parseInt(distanceStr));
//                            if (date.equals("0")) {
//                                c1.setChecked(true);
//                                c2.setChecked(false);
//                                c3.setChecked(false);
//                                c1.setClickable(false);
//                                c2.setClickable(true);
//                                c3.setClickable(true);
//
//                            }
//                            else if (date.equals("nari1")) {
//                                c1.setChecked(false);
//                                c2.setChecked(true);
//                                c3.setChecked(false);
//                                c2.setClickable(false);
//                                c1.setClickable(true);
//                                c3.setClickable(true);
//                            }
//                            else if (date.equals("3")) {
//                                c1.setChecked(false);
//                                c2.setChecked(false);
//                                c3.setChecked(true);
//                                c3.setClickable(false);
//                                c1.setClickable(true);
//                                c2.setClickable(true);
//                            }
//                            else
//                            {
//                                c1.setChecked(true);
//                                c2.setChecked(false);
//                                c3.setChecked(false);
//
//                            }
//
//                           // pd.hide();
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        progressDialog.dismiss();
//
//                    }});
//        }
//    else
//    {
//        Splashscreen dia = new Splashscreen();
//        dia.Connectivity_Dialog_with_refresh(getActivity());
//    }

        c1 = (CheckBox) v.findViewById(R.id.checkBox);
        c2 = (CheckBox) v.findViewById(R.id.checkBox2);
        c3 = (CheckBox) v.findViewById(R.id.checkBox3);




        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==c1){
                    c2.setChecked(false);
                    c3.setChecked(false);
                    dateSelection = "0";
                    c1.setClickable(false);
                    c2.setClickable(true);
                    c3.setClickable(true);
                }



            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==c2){
                    c1.setChecked(false);
                    c3.setChecked(false);
                    dateSelection = "nari1";
                    c2.setClickable(false);
                    c1.setClickable(true);
                    c3.setClickable(true);
                }


            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==c3){
                    c2.setChecked(false);
                    c1.setChecked(false);
                    dateSelection = "3";
                    c3.setClickable(false);
                    c1.setClickable(true);
                    c2.setClickable(true);
                }

            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                distance.setText(progress+"");
                distance_type.setText(dist_type);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        return v;
    }


    //Calling API in this fn to save the User Preference
    public  void FnPostRequest(String distanceStr,String select_date,String user_id){

        new Custom_Progress(getActivity());
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        JSONObject jsonObjSend = new JSONObject();


        try {
            // Add key/value pairs
            jsonObjSend.put("search_distance",distanceStr);
            jsonObjSend.put("select_date", select_date);
            jsonObjSend.put("userid", user_id);

            //  hideDialog();
            Log.i(TAG, jsonObjSend.toString(3));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObjRecv = HttpClient.SendHttpPost(URL_SaveUserPreference, jsonObjSend);

        JSONObject json = null;

        try {
            json = new JSONObject(String.valueOf(jsonObjRecv));
            String result = json.getString("message");
            new Custom_Progress(getActivity()).dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json_LL = null;
        try {
            json_LL = json.getJSONObject("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
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
                   new Custom_Progress(getActivity());
                    String seekBarValue = Integer.toString(seekBar.getProgress());
                    FnPostRequest(seekBarValue,dateSelection,user_id.getString("userid",""));
                    Intent i = new Intent(getActivity(), Screen16.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                    getActivity().finish();
                    new Custom_Progress(getActivity()).dismiss();
                    return true;
                }
                return false;
            }
        });
    }



}
