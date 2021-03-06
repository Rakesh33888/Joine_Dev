package com.brahmasys.bts.joinme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajay on 7/23/2016.
 */
public class ReportFragment extends android.support.v4.app.Fragment {
    TextView textproblm;
    EditText editTextproblem;
    ImageView shareicon,msg,logo;
    Button buttoncncl,buttonsnd;
    FrameLayout reportlayout;
    public static final String ACTIVITYID = "activity_id";
    public static final String USERID = "userid";
    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    private static final String TAG1 = "Feedback";
    private static final String URL1 = Constant.Feedback;
    String screen="null",userid,activityid,ownerid,where;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.reportfragment, container, false);

         bundle = this.getArguments();
        screen = bundle.getString("screen", "0");
        userid=bundle.getString("userid", "null");
        ownerid = bundle.getString("owner_id","null");
        activityid =bundle.getString("activityid", "null");
        where = bundle.getString("where", "0");
        user_id  = getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();

        textproblm= (TextView) v.findViewById(R.id.textView17);
        editTextproblem= (EditText) v.findViewById(R.id.editText10);

        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);
        msg = (ImageView) refTool.findViewById(R.id.msg);
        msg.setBackgroundResource(R.drawable.custo_msg);
        logo = (ImageView) refTool.findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
            }
        });

        buttoncncl= (Button) v.findViewById(R.id.buttoncancel);
        buttoncncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Screen",screen);
                hideKeyboard(v);
                if (screen.equals("setting")) {
                    Fragment appsetting = new Appsetting();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContent, appsetting);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (screen.equals("screen17")) {
                    Fragment screen17 = new Screen17();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("userid", userid);
                    bundle2.putString("activityid",activityid);
                    bundle2.putString("where",where);
                    screen17.setArguments(bundle2);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContent, screen17);
                    transaction.addToBackStack(null);
                    transaction.commit();

                } else if (screen.equals("screen13")) {
                    if (where.equals("mygroups")){
                        Fragment screen13 = new Screen13();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("owner_id",ownerid);
                        bundle1.putString("screen","screen17");
                        bundle1.putString("userid",userid);
                        bundle1.putString("activityid",activityid);
                        bundle1.putString("where",where);
                        screen13.setArguments(bundle1);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.flContent, screen13);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    if (where.equals("other_user_details"))
                    {
                        Fragment screen13 = new Screen13();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("owner_id",ownerid);
                        bundle1.putString("screen","other_user_details");
                        bundle1.putString("userid",userid);
                        bundle1.putString("activityid",activityid);
                        bundle1.putString("where",where);
                        screen13.setArguments(bundle1);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.flContent, screen13);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                   if (where.equals("single_group_message"))
                    {
                        Fragment screen13 = new Screen13();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("owner_id",ownerid);
                        bundle1.putString("screen","single_group_message");
                        bundle1.putString("userid",userid);
                        bundle1.putString("activityid",activityid);
                        bundle1.putString("where",where);
                        screen13.setArguments(bundle1);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.flContent, screen13);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }


                } else if (screen.equals("other_user_details")) {

                    Fragment appsetting = new Other_User_Details();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("screen","other_user_details");
                    bundle3.putString("userid",userid);
                    bundle3.putString("activityid",activityid);
                    bundle3.putString("where",where);
                    appsetting.setArguments(bundle3);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContent, appsetting);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
                else if (where.equals("single_group_message"))
                {
                    Fragment screen13 = new Screen13();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("owner_id",ownerid);
                    bundle1.putString("screen","single_group_message");
                    bundle1.putString("userid",userid);
                    bundle1.putString("activityid",activityid);
                    bundle1.putString("where",where);
                    screen13.setArguments(bundle1);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContent, screen13);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    Intent Home = new Intent(getActivity(), Screen16.class);
                    startActivity(Home);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    getActivity().finish();

                }
            }
        });

        reportlayout= (FrameLayout) v.findViewById(R.id.framelayoutreport);
        reportlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }

            private void hideKeyboard(View view) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        buttonsnd= (Button) v.findViewById(R.id.buttonsend);
        buttonsnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (Connectivity_Checking.isConnectingToInternet()) {

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

                                    Intent Home = new Intent(getActivity(), Screen16.class);
                                    startActivity(Home);
                                    getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                    getActivity().finish();

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


                } else {
                    Splashscreen dia = new Splashscreen();
                    dia.Connectivity_Dialog_with_refresh(getActivity());

                }

            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                hideKeyboard(v);
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (screen.equals("setting")) {
                        Fragment appsetting = new Appsetting();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.flContent, appsetting);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else if (screen.equals("screen17")) {
                        Fragment screen17 = new Screen17();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("userid", userid);
                        bundle2.putString("activityid",activityid);
                        bundle2.putString("where",where);
                        screen17.setArguments(bundle2);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.flContent, screen17);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    } else if (screen.equals("screen13")) {
                        if (where.equals("mygroups")){
                            Fragment screen13 = new Screen13();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("owner_id",ownerid);
                            bundle1.putString("screen","screen17");
                            bundle1.putString("userid",userid);
                            bundle1.putString("activityid",activityid);
                            bundle1.putString("where",where);
                            screen13.setArguments(bundle1);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.flContent, screen13);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        if (where.equals("other_user_details"))
                        {
                            Fragment screen13 = new Screen13();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("owner_id",ownerid);
                            bundle1.putString("screen","other_user_details");
                            bundle1.putString("userid",userid);
                            bundle1.putString("activityid",activityid);
                            bundle1.putString("where",where);
                            screen13.setArguments(bundle1);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.flContent, screen13);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                        if (where.equals("single_group_message"))
                        {
                            Fragment screen13 = new Screen13();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("owner_id",ownerid);
                            bundle1.putString("screen","single_group_message");
                            bundle1.putString("userid",userid);
                            bundle1.putString("activityid",activityid);
                            bundle1.putString("where",where);
                            screen13.setArguments(bundle1);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.flContent, screen13);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }

                    } else if (screen.equals("other_user_details")) {

                        Fragment appsetting = new Other_User_Details();
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("screen","other_user_details");
                        bundle3.putString("userid",userid);
                        bundle3.putString("activityid",activityid);
                        bundle3.putString("where",where);
                        appsetting.setArguments(bundle3);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.flContent, appsetting);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    } else {
                        Intent Home = new Intent(getActivity(), Screen16.class);
                        startActivity(Home);
                        getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        getActivity().finish();

                    }
                    return true;
                }
                return false;
            }
        });
    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
