package com.brahmasys.bts.joinme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brahmasys.bts.joinme.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajay on 7/23/2016.
 */
public class ReportFragment extends android.support.v4.app.Fragment {
    TextView textproblm;
    EditText editTextproblem;
    Button buttoncncl,buttonsnd;
    public static final String ACTIVITYID = "activity_id";
    public static final String USERID = "userid";
    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    private static final String TAG1 = "Feedback";
    private static final String URL1 = "http://52.37.136.238/JoinMe/Activity.svc/Feedback";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.reportfragment,container,false);
        user_id  = getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();

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
                if (editTextproblem.getText().toString().length()>2) {





                        JSONObject jsonObjSend = new JSONObject();
                        try {
                            // Add key/value pairs
                            jsonObjSend.put("activityid", activity_id.getString("activity_id", ""));
                            jsonObjSend.put("feedback_message", editTextproblem.getText().toString());
                            jsonObjSend.put("userid", user_id.getString("userid", "0"));

                            Log.i(TAG1, jsonObjSend.toString(3));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonObjRecv = HttpClient.SendHttpPost(URL1, jsonObjSend);


                        JSONObject json = null;
                        try {
                            json = new JSONObject(String.valueOf(jsonObjRecv));
                            String result = json.getString("message");

                            if (result.equals("Inserted Successfully"))
                            {
                                Toast toast = Toast.makeText(getContext(), "Thank you for your Feedback!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                Fragment fragment = null;
                                switch (v.getId()) {
                                    case R.id.buttonsend:
                                        fragment = new Fragment();
                                        replaceFragment(fragment);
                                        break;
                                }

                            }
                            else
                            {
                                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }

                else
                {
                    Toast.makeText(getActivity(), "Please describe the problem...!", Toast.LENGTH_SHORT).show();
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
