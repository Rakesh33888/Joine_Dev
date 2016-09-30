package com.brahmasys.bts.joinme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;


public class Appsetting extends Fragment{

    int max_range = 23;
    int min_range = 0;
    int initialvalues = 0;
    int max_range1 = 60;
    int min_range1= 0;
    int initialvalues1 = 0;
    private Button incButton,incButton1;
    private Button decButton,decButton1;
    private TextView hours,mins;
   // ProgressDialog pd;
    Button btnbck,btnreport,btndelt;
    ImageView imageback;
     private ListView customlistview;
    SegmentedGroup dis;
    RadioButton miles,km;
    Spinner dropdown;
    ArrayList customarraylist;
    FragmentManager fragmentManager;
    LinearLayout logout,linearLayoutterm,backlayoutappsetting;
    ImageView shareicon;
    SessionManager session;
    Button yes,no;
    String deviceuid;
    SwitchButton switchNear;
     ProgressDialog progressDialog;
    public static final String USERID = "userid";
    public static final String DETAILS = "user_details";
    public static final String USER_PIC = "user_pic";

    SharedPreferences user_id,user_Details,user_pic;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic;

    private String [] prgmNameList={"Notification","Message","New activities near by","Activity reminder","New level"};
//Add a Comment On 22 AUGUSR 2016
//Add a Comment On 22 AUGUSR 2016 2
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  static  final  String URL_GetUserSettings ="http://52.37.136.238/JoinMe/User.svc/GetUserSettings/";
    public  static  final  String URL_UpdateSettings ="http://52.37.136.238/JoinMe/User.svc/UpdateSettings";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FragmentManager supportFragmentManager;
    public String LAYOUT_INFLATER_SERVICE;

    public Appsetting() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Appsetting newInstance(String param1, String param2) {
        Appsetting fragment = new Appsetting();
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


        View v = inflater.inflate(R.layout.fragment_appsetting, container, false);

//        pd = new ProgressDialog(getActivity());
//        pd.setMessage("Updating...");
//        pd.show();
        progressDialog =ProgressDialog.show(getActivity(), null, null, true);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);


        incButton = (Button) v.findViewById(R.id.incButton);
        decButton = (Button) v.findViewById(R.id.decButton);
        hours = (TextView) v.findViewById(R.id.numberEditText);
        incButton1 = (Button) v.findViewById(R.id.incButton1);
        decButton1 = (Button) v.findViewById(R.id.decButton1);
        mins = (TextView) v.findViewById(R.id.numberEditText1);
        switchNear = (SwitchButton)v.findViewById(R.id.near);

        km = (RadioButton) v.findViewById(R.id.km);
        backlayoutappsetting= (LinearLayout) v.findViewById(R.id.backlayoutappsetting);
        backlayoutappsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  pd.show();
                FnSaveUserSettings();
                Intent i = new Intent(getActivity(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
                //   pd.hide();

            }
        });

        imageback = (ImageView) v.findViewById(R.id.imageback_App_Sett);
        imageback.setClickable(true);


        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        user_Details = getActivity().getSharedPreferences(DETAILS, getActivity().MODE_PRIVATE);
        edit_user_detals = user_Details.edit();
        user_pic = getActivity().getSharedPreferences(USER_PIC, getActivity().MODE_PRIVATE);
        edit_user_pic = user_pic.edit();


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }




        deviceuid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        incButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (initialvalues >= min_range && initialvalues <= max_range) initialvalues++;
                if (initialvalues > max_range)
                    initialvalues = min_range;
                hours.setText("" + initialvalues);

            }
        });

        decButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (initialvalues >= min_range && initialvalues <= max_range)
                    initialvalues--;

                if (initialvalues < min_range)
                    initialvalues = max_range;

                hours.setText(initialvalues + "");
            }
        });


        incButton1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (initialvalues1 >= min_range1 && initialvalues1 <= max_range1) initialvalues1++;
                if (initialvalues1 > max_range1)
                    initialvalues1 = min_range1;
                mins.setText("" + initialvalues1);

            }
        });

        decButton1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (initialvalues1 >= min_range1 && initialvalues1 <= max_range1)
                    initialvalues1--;

                if (initialvalues1 < min_range1)
                    initialvalues1 = max_range1;

                mins.setText(initialvalues1 + "");
            }
        });




        session = new SessionManager(getActivity());
        dis = (SegmentedGroup) v.findViewById(R.id.distance);
        dis.setTintColor(Color.parseColor("#005F99"));

        miles = (RadioButton) v.findViewById(R.id.miles);
        km = (RadioButton) v.findViewById(R.id.km);
        linearLayoutterm= (LinearLayout) v.findViewById(R.id.linearlayoutterm);
        linearLayoutterm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Terms_services terms_services = new Terms_services();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent,terms_services);
                fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out);
                fragmentTransaction.commit();
//                Fragment fragment = null;
//                switch (v.getId()) {
//                    case R.id.linearlayoutterm:
//                        fragment = new Terms_services();
//                        replaceFragment1(fragment);
//                        break;
//                }


            }


        });
        logout = (LinearLayout) v.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.logout_custom);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                yes = (Button) dialog.findViewById(R.id.yes);
                no = (Button) dialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        logoutUser();
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

          dropdown = (Spinner) v.findViewById(R.id.spinner1);
        String[] items = new String[]{"All messages", "Only from travelers", "Only from groups"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        miles.setChecked(true);




        btnreport= (Button) v.findViewById(R.id.button11);
        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                switch (v.getId()) {
                    case R.id.button11:
                        fragment = new ReportFragment();
                        replaceFragment(fragment);
                        break;
                }
            }
        });
        btndelt= (Button) v.findViewById(R.id.button12);

        btndelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.custom_delete_account);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                yes = (Button) dialog.findViewById(R.id.yes);
                no = (Button) dialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Delete_Account();
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
        FnPopulateUserSettings(v);
        return v;
    }

//    private void replaceFragment1(Fragment f) {
//        Fragment terms_services=new Terms_services();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.flContent,terms_services);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//    }

    public  void Delete_Account()
    {
        new Custom_Progress(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.37.136.238/JoinMe/User.svc/DeleteUser/" +user_id.getString("userid",""),
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //  prgDialog.hide();
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);
                            String result=obj.getString("message");
                            logoutUser();
                            Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);
                            onFinish();
                            new Custom_Progress(getActivity()).dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }});
    }

    private void logoutUser() {

         new Custom_Progress(getActivity());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.37.136.238/JoinMe/User.svc/LogOut/" + user_id.getString("userid", "") + "/" + deviceuid,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //  prgDialog.hide();
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);
                            String out = obj.getString("message");
                            if (out.equals("Logged Out Successfully")) {

                                session.setLogin(false);
                                edit_user_detals.clear();
                                edit_user_detals.commit();

                                edit_user_pic.clear();
                                edit_user_pic.commit();

                                edit_userid.clear();
                                edit_userid.commit();
                                // Launching the login activity
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                                getActivity().finish();
                                new Custom_Progress(getActivity()).dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });



    }


    protected void FnPopulateUserSettings(final View v ){
        if(Connectivity_Checking.isConnectingToInternet()) {
            AsyncHttpClient client = new AsyncHttpClient();
            String id = user_id.getString("userid", "");
            client.get(URL_GetUserSettings + id,
                    new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http response code '200'

                        public void onSuccess(String jsonResult) {

                            try {
                                // Extract JSON Object from JSON returned by REST WS
                                JSONObject dataObj = new JSONObject(jsonResult).getJSONObject("user_setting");
                                String activity_nearby = dataObj.getString("activity_nearby");
                                String activity_reminder_hour = dataObj.getString("activity_reminder_hour");
                                String activity_reminder_min = dataObj.getString("activity_reminder_min");
                                String distance_km = dataObj.getString("distance_km");
                                String new_level = dataObj.getString("new_level");
                                String notification_by = dataObj.getString("notification_by");
                                String userid = dataObj.getString("userid");


                                if (distance_km.equals("K") || distance_km.equals("k")) {
                                    km.setChecked(true);
                                } else {
                                    miles.setChecked(true);
                                }


                                SwitchButton switchButton = (SwitchButton) v.findViewById(R.id.near);

                                if (true == Boolean.valueOf(activity_nearby)) {
                                    switchButton.setChecked(true);
                                } else {
                                    switchButton.setChecked(false);
                                }
                                TextView hour = (TextView) v.findViewById(R.id.numberEditText);

                                hour.setText(activity_reminder_hour);
                                TextView minutes = (TextView) v.findViewById(R.id.numberEditText1);

                                minutes.setText(activity_reminder_min);
                                progressDialog.dismiss();
                                //pd.hide();

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
    }

    //Calling API in this fn to save the User Settings
    //@Added By Ajay
    private void FnSaveUserSettings(){
        new Custom_Progress(getActivity());
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }


        //Here we are getting values from the UI elements

        Boolean activity_nearby = switchNear.isChecked();

        String activity_reminder_hours = hours.getText().toString();
        String activity_reminder_mins = mins.getText().toString();

        String distance = "";
        if(miles.isChecked()){
            distance ="M";
        }
        if(km.isChecked()){
            distance="K";
        }

            JSONObject jsonObjSend = new JSONObject();
            try {
                String userid = user_id.getString("userid", "");

                // Add key/value pairs
                jsonObjSend.put("activity_nearby",String.valueOf(activity_nearby));
                jsonObjSend.put("activity_reminder_hours", activity_reminder_hours);
                jsonObjSend.put("activity_reminder_mins", activity_reminder_mins);
                jsonObjSend.put("distance_km", distance);
                jsonObjSend.put("new_level", true);
                jsonObjSend.put("notification_by", "");
                jsonObjSend.put("userid",userid);
                Log.i("UserSettings_Request", jsonObjSend.toString(3));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonObjRecv = HttpClient.SendHttpPost(URL_UpdateSettings, jsonObjSend);

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

        //pd.hide();

    }



    private void replaceFragment(Fragment frg) {
        Fragment reportfragment=new ReportFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent,reportfragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    public void getSystemService(String layoutInflaterService) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onResume() {
        super.onResume();
        //pd.show();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    FnSaveUserSettings();
                    Intent i = new Intent(getActivity(), Screen16.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                    getActivity().finish();

                //    pd.hide();
                    return true;
                }
                return false;
            }
        });
    }


}
