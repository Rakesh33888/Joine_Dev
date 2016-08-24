package com.brahmasys.bts.joinme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bts.joinme.R;

/**
 * Created by ajay on 7/23/2016.
 */
public class ReportFragment extends android.support.v4.app.Fragment {
    TextView textproblm;
    EditText editTextproblem;
    Button buttoncncl,buttonsnd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.reportfragment,container,false);
        textproblm= (TextView) v.findViewById(R.id.textView17);
        editTextproblem= (EditText) v.findViewById(R.id.editText10);
        buttoncncl= (Button) v.findViewById(R.id.buttoncancel);
        buttoncncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                switch (v.getId()) {
                    case R.id.buttoncancel:
                        fragment = new Fragment();
                        replaceFragment(fragment);
                        break;
                }

            }
        });
        buttonsnd= (Button) v.findViewById(R.id.buttonsend);
        buttonsnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==buttonsnd){
                Fragment fragment = null;
                switch (v.getId()) {
                    case R.id.buttonsend:
                        fragment = new Fragment();
                        replaceFragment(fragment);
                        break;

                }
                    Toast toast = Toast.makeText(getContext(),"Thank you for your feedback!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


            }
            }
        });

        return v;
    }
    private void replaceFragment(Fragment fragment) {
        Fragment appsetting=new Appsetting();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, appsetting);
        transaction.addToBackStack(null);
        transaction.commit();
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
