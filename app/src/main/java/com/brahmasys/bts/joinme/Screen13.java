package com.brahmasys.bts.joinme;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brahmasys.bts.joinme.R;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Screen13 extends android.support.v4.app.Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    String firstname="join",lastname="me",about="null",owner_id;
    TextView report_text,name,age,description,owner_name;
    ProgressDialog pd;
    private SliderLayout mDemoSlider;
    HashMap<String,String> url_maps;
    FragmentManager fragmentManager;
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

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading...");
        pd.show();

        mDemoSlider = (SliderLayout) v.findViewById(R.id.slider);

        url_maps = new HashMap<String, String>();

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        Bundle bundle = this.getArguments();
        owner_id = bundle.getString("owner_id", "0");

        report_text.setPaintFlags(report_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        name  = (TextView) v.findViewById(R.id.textView19);
        description = (TextView) v.findViewById(R.id.textView18);

        report_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager=getFragmentManager();
                ReportFragment report =new ReportFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent,report)
                        .addToBackStack(null)
                        .commit();
            }
        });
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.37.136.238/JoinMe/User.svc/GetUserDetails/" + owner_id,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //  prgDialog.hide();
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
                                owner_name.setText("Hello"+" "+userdetails.getString("fname")+"!");

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

                            pd.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

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
}
