package com.example.bts.joinme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;


public class Appsetting extends Fragment{
    Button btnbck,btnreport,btndelt;
    ImageView imageback;
     private ListView customlistview;
    SegmentedGroup dis;
    RadioButton miles,km;
    Spinner dropdown;
    ArrayList customarraylist;

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


        dis = (SegmentedGroup) v.findViewById(R.id.distance);
        miles = (RadioButton) v.findViewById(R.id.miles);
        km = (RadioButton) v.findViewById(R.id.km);

          dropdown = (Spinner) v.findViewById(R.id.spinner1);
        String[] items = new String[]{"All messages", "Only from travelers", "Only from groups"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        miles.setChecked(true);


        imageback = (ImageView) v.findViewById(R.id.imageback);
        imageback.setClickable(true);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),Screen16.class);
                startActivity(i);
            }
        });
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



}
