package com.example.bts.joinme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mysearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mysearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mysearch extends android.support.v4.app.Fragment {
    LinearLayout linearLayout;
    RelativeLayout relativeLayout1,relativeLayout2;
    ImageView back;
    TextView mysearch,textView7,Maxdistance,distance;
    CheckBox c1,c2,c3;
    SeekBar seekBar;

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
     * @param param1 Parameter 1.
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
        linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);
        relativeLayout1 = (RelativeLayout) v.findViewById(R.id.relativelayout_seek);
        relativeLayout2 = (RelativeLayout) v.findViewById(R.id.relativelayout_checkbox);
        textView7 = (TextView) v.findViewById(R.id.textView7);
        mysearch = (TextView) v.findViewById(R.id.textView6);
        back = (ImageView) v.findViewById(R.id.button7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Screen16.class);
                startActivity(i);
            }
        });
        c1 = (CheckBox) v.findViewById(R.id.checkBox);
        c2 = (CheckBox) v.findViewById(R.id.checkBox2);
        c3 = (CheckBox) v.findViewById(R.id.checkBox3);


        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==c1){
                    c2.setChecked(false);
                    c3.setChecked(false);
                }

            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==c2){
                    c1.setChecked(false);
                    c3.setChecked(false);
                }

            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==c3){
                    c2.setChecked(false);
                    c1.setChecked(false);
                }

            }
        });
        Maxdistance= (TextView) v.findViewById(R.id.Maxdistance);
        distance= (TextView) v.findViewById(R.id.distance);
        seekBar= (SeekBar) v.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance.setText(progress+"km");


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
