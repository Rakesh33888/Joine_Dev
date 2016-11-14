package com.brahmasys.bts.joinme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Screen13 extends android.support.v4.app.Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    String firstname="join",lastname="me",about="null",owner_id,where, screen;
    TextView report_text,name,age,description,owner_name;
    ImageView shareicon;
  //  ProgressDialog pd;
    private SliderLayout mDemoSlider;
    HashMap<String,String> url_maps;
    FragmentManager fragmentManager;
    ProgressDialog progressDialog;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_screen13,container,false);
        report_text = (TextView) v.findViewById(R.id.report_text);
        age = (TextView) v.findViewById(R.id.textView22);
        owner_name = (TextView) v.findViewById(R.id.owner_name);
        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();

//        pd = new ProgressDialog(getActivity());
//        pd.setMessage("loading...");
//        pd.show();

        progressDialog =ProgressDialog.show(getActivity(), null, null, true);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDemoSlider = (SliderLayout) v.findViewById(R.id.slider);

        url_maps = new HashMap<String, String>();

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        bundle = this.getArguments();
        owner_id = bundle.getString("owner_id", "0");
        where    = bundle.getString("where", "0");
        screen   =bundle.getString("screen","0");
        report_text.setPaintFlags(report_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        name  = (TextView) v.findViewById(R.id.textView19);
        description = (TextView) v.findViewById(R.id.textView18);


        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);


        report_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screen.equals("screen17")) {
                    Fragment reportfrgmnt = new ReportFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("screen", "screen13");
                    bundle1.putString("userid", bundle.getString("userid", "0"));
                    bundle1.putString("activityid", bundle.getString("activityid", "0"));
                    bundle1.putString("owner_id", owner_id);
                    bundle1.putString("where", "mygroups");
                    reportfrgmnt.setArguments(bundle1);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContent, reportfrgmnt);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                if (screen.equals("other_user_details"))
                {
                    Fragment reportfrgmnt = new ReportFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("screen", screen);
                    bundle1.putString("userid", bundle.getString("userid", "0"));
                    bundle1.putString("activityid", bundle.getString("activityid", "0"));
                    bundle1.putString("owner_id", owner_id);
                    bundle1.putString("where", bundle.getString("where","0"));
                    reportfrgmnt.setArguments(bundle1);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContent, reportfrgmnt);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                if (screen.equals("single_group_message"))
                {
                    Fragment reportfrgmnt = new ReportFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("screen", screen);
                    bundle1.putString("userid", bundle.getString("userid", "0"));
                    bundle1.putString("activityid", bundle.getString("activityid", "0"));
                    bundle1.putString("owner_id", owner_id);
                    bundle1.putString("where", bundle.getString("where","0"));
                    reportfrgmnt.setArguments(bundle1);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContent, reportfrgmnt);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        if (Connectivity_Checking.isConnectingToInternet()) {

            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://52.37.136.238/JoinMe/User.svc/GetUserDetails/" + owner_id,
                    new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http response code '200'

                        public void onSuccess(String response) {

                            try {
                                // Extract JSON Object from JSON returned by REST WS
                                JSONObject obj = new JSONObject(response);
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(String.valueOf(obj));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                /************************* UserDetails start **************************/
                                JSONObject userdetails = null;
                                try {
                                    userdetails = json.getJSONObject("details");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    //Getting information form the Json Response object
                                    firstname = userdetails.getString("fname");
                                    lastname = userdetails.getString("lname");
                                    about = userdetails.getString("about");
                                    age.setText(userdetails.getString("age"));
                                    name.setText(firstname + " " + lastname);
                                    description.setText(about);
                                    // owner_name.setText("Hello"+" "+userdetails.getString("fname")+"!");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray cast = userdetails.getJSONArray("user_pic");


                                for (int i = 0; i < cast.length(); i++) {
                                    JSONObject actor = cast.getJSONObject(i);

                                    String url = actor.getString("url");

                                    url_maps.put("image" + i, "http://52.37.136.238/JoinMe/" + url);
                                    //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();

                                    Log.d("Type", cast.getString(i));
                                }
                                for (String name : url_maps.keySet()) {
                                    TextSliderView textSliderView = new TextSliderView(getActivity());
                                    // initialize a SliderLayout
                                    textSliderView.image(url_maps.get(name))
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                                                          @Override
                                                                          public void onSliderClick(BaseSliderView baseSliderView) {
                                                                              // some method
                                                                          }
                                                                      }
                                            );
                                    mDemoSlider.addSlider(textSliderView);

                                }
                                progressDialog.dismiss();
                                //pd.hide();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


        } else {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());
            progressDialog.dismiss();
        }


        return  v;
    }



    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        // Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }





    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }


    public void onPageScrollStateChanged(int state) {
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
//                    Intent i = new Intent(getActivity(), Screen16.class);
//                    startActivity(i);
//                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
//                    getActivity().finish();
                    if (screen.equals("screen17")) {
                        FragmentManager fragmentManager = getFragmentManager();
                        Screen17 mygroup = new Screen17();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("userid",bundle.getString("userid","0"));
                        bundle1.putString("activityid",bundle.getString("activityid","0"));
                        bundle1.putString("where",where);
                        mygroup.setArguments(bundle1);
                        fragmentManager.beginTransaction().replace(R.id.flContent, mygroup).commit();
                    }
                    if (screen.equals("other_user_details"))
                    {
                        FragmentManager fragmentManager = getFragmentManager();
                        Other_User_Details mygroup = new Other_User_Details();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("userid",bundle.getString("userid","0"));
                        bundle1.putString("activityid",bundle.getString("activityid","0"));
                        bundle1.putString("where", bundle.getString("where","0"));
                      //  bundle1.putString("where",where);
                        mygroup.setArguments(bundle1);
                        fragmentManager.beginTransaction().replace(R.id.flContent, mygroup).commit();

                    }
                    if (screen.equals("single_group_message"))
                    {
                        Fragment reportfrgmnt = new Single_group_Message();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("screen", screen);
                        bundle1.putString("userid", bundle.getString("userid", "0"));
                        bundle1.putString("activityid", bundle.getString("activityid", "0"));
                        bundle1.putString("owner_id", owner_id);
                        bundle1.putString("where", bundle.getString("where","0"));
                        reportfrgmnt.setArguments(bundle1);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.flContent, reportfrgmnt);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    return true;
                }
                return false;
            }
        });
    }

}
