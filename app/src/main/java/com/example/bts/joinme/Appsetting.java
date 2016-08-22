package com.example.bts.joinme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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
    private String [] prgmNameList={"Notification","Message","New activities near by","Activity reminder","New level"};

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
                session.setLogin(false);
                // Launching the login activity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
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
