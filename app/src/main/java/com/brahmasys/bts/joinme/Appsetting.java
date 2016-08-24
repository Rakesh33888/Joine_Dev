package com.brahmasys.bts.joinme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Gravity;
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

import com.example.bts.joinme.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private TextView editText,editText1;

    Button btnbck,btnreport,btndelt;
    ImageView imageback;
     private ListView customlistview;
    SegmentedGroup dis;
    RadioButton miles,km;
    Spinner dropdown;
    ArrayList customarraylist;
    LinearLayout logout;
    SessionManager session;
    Button yes,no;
    String deviceuid;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FragmentManager supportFragmentManager;
    public String LAYOUT_INFLATER_SERVICE;

    public Appsetting() {
        // Required empty public constructor
    }
// testing git checkin checkout
//Added Comment by Rakesh Sharma
    //Added Coment BY Ajay
    //added Comment by Rakesh 22AUGUST 2016
	//added Comment by Rakesh 22AUGUST 2016
	//added Comment by Rakesh 22AUGUST 2016
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Appsetting.
     */
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


        View v=inflater.inflate(R.layout.fragment_appsetting, container, false);



        incButton = (Button) v.findViewById(R.id.incButton);
        decButton = (Button) v.findViewById(R.id.decButton);
        editText = (TextView) v.findViewById(R.id.numberEditText);
        incButton1 = (Button) v.findViewById(R.id.incButton1);
        decButton1 = (Button) v.findViewById(R.id.decButton1);
        editText1 = (TextView) v.findViewById(R.id.numberEditText1);
        imageback = (ImageView) v.findViewById(R.id.imageback);
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
                Intent i = new Intent(getContext(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
            }
        });
        incButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (initialvalues >= min_range && initialvalues <= max_range) initialvalues++;
                if (initialvalues > max_range)
                    initialvalues = min_range;
                editText.setText("" + initialvalues);

            }
        });

        decButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (initialvalues >= min_range && initialvalues <= max_range)
                    initialvalues--;

                if (initialvalues < min_range)
                    initialvalues = max_range;

                editText.setText(initialvalues + "");
            }
        });


        incButton1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (initialvalues1 >= min_range1 && initialvalues1 <= max_range1) initialvalues1++;
                if (initialvalues1 > max_range1)
                    initialvalues1 = min_range1;
                editText1.setText("" + initialvalues1);

            }
        });

        decButton1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (initialvalues1 >= min_range1 && initialvalues1 <= max_range1)
                    initialvalues1--;

                if (initialvalues1 < min_range1)
                    initialvalues1 = max_range1;

                editText1.setText(initialvalues1 + "");
            }
        });




        session = new SessionManager(getActivity());
        dis = (SegmentedGroup) v.findViewById(R.id.distance);
        miles = (RadioButton) v.findViewById(R.id.miles);
        km = (RadioButton) v.findViewById(R.id.km);
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
                Intent i2=new Intent(getContext(),Splashscreen.class);
                startActivity(i2);
            }
        });
        return v;
    }

    private void logoutUser() {


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
                                // Launching the login activity
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                                getActivity().finish();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });



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

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
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
