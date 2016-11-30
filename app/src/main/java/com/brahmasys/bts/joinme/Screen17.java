package com.brahmasys.bts.joinme;

import android.*;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.vision.Frame;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class Screen17 extends android.support.v4.app.Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener  {
    FrameLayout frameLayoutacity ;
    ImageView imageView6;
    ImageView imageViewbck,icon,msg;
    CircularImageView createrimage;
  //  Button btnJoineActivity;

    LinearLayout backlayout_screen_17,frameLayoutbck;
    ImageView shareicon,logo;
    RatingBar myratingBar;
    String activity_name,cost,distance,duration,limit,owner_name,owner_pic,rating,review,activity_address,time,joined,icon1,description;
    Button btnJoineActivity;
    TextView  updatetext,delete_text;
    TextView acitvityname,distancefromnearby,owner_name1,uptopeoples,currentpeoples,costtext,timetext,timetextview,reviews;
    FragmentManager fragmentManager;
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    private static final String LAT_LNG = "lat_lng";
    public static final String URL_AddMemberToGroup = "http://52.37.136.238/JoinMe/Activity.svc/AddMemberToGroup/";
    SharedPreferences user_id,activity_id,lat_lng;
    SharedPreferences.Editor edit_userid,edit_activity_id,edit_lat_lng;
    String lat="0",lon="0";
    RatingBar minimumRating;
    private SliderLayout mDemoSlider;
    HashMap<String,String> url_maps;
    String uid,aid,owner_id,Where;
    ProgressDialog progressDialog;
    JSONObject obj;
    Double latitude=0.0,longitude=0.0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

              progressDialog =ProgressDialog.show(getActivity(), null, null, true);
              progressDialog.setIndeterminate(true);
              progressDialog.setCancelable(false);
              progressDialog.setContentView(R.layout.custom_progress);
              progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
              progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));






        final View v=inflater.inflate(R.layout.screen17,container,false);
        frameLayoutbck= (LinearLayout) v.findViewById(R.id.frameLayoutbck);
        frameLayoutacity= (FrameLayout) v.findViewById(R.id.frameLayoutactity);
        imageViewbck= (ImageView) v.findViewById(R.id.backbtnimage);
       // btnJoineActivity= (Button) v.findViewById(R.id.button6);
      //  reporttext= (TextView) v.findViewById(R.id.reportactitytext);
        updatetext= (TextView) v.findViewById(R.id.updateactivitytext);
        delete_text = (TextView) v.findViewById(R.id.delete_activitytext);
        acitvityname = (TextView) v.findViewById(R.id.acitvityname);
        distancefromnearby = (TextView) v.findViewById(R.id.distancefromnearby);
        owner_name1 = (TextView) v.findViewById(R.id.owner_name);
        uptopeoples = (TextView) v.findViewById(R.id.uptopeoples);
        currentpeoples = (TextView) v.findViewById(R.id.currentpeoples);
        costtext = (TextView) v.findViewById(R.id.costtext);
        timetext = (TextView) v.findViewById(R.id.timetext);
        createrimage = (CircularImageView) v.findViewById(R.id.createrimage);
        timetextview = (TextView) v.findViewById(R.id.timetextview);
        reviews     = (TextView) v.findViewById(R.id.reviews);
        minimumRating = (RatingBar)v.findViewById(R.id.myRatingBar);
        icon = (ImageView) v.findViewById(R.id.imageViewrfting);
        backlayout_screen_17= (LinearLayout) v.findViewById(R.id.backlayoutscreen17);




        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();
        lat_lng = getActivity().getSharedPreferences(LAT_LNG, getActivity().MODE_PRIVATE);
        edit_lat_lng = lat_lng.edit();
        mDemoSlider = (SliderLayout) v.findViewById(R.id.slider);

        url_maps = new HashMap<String, String>();

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        Bundle bundle = this.getArguments();
          uid = bundle.getString("userid", "0");
          aid  = bundle.getString("activityid", "0");
          Where= bundle.getString("where","0");

        edit_activity_id.putString("activity_id", aid);
        edit_activity_id.commit();

        backlayout_screen_17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Where.equals("mygroups")) {
                    FragmentManager fragmentManager = getFragmentManager();
                    Mygroup mygroup  = new Mygroup();
                    fragmentManager.beginTransaction().replace(R.id.flContent, mygroup).commit();

                }
                if (Where.equals("single_group_message"))
                {
                    FragmentManager fragmentManager = getFragmentManager();
                    Single_group_Message mygroup  = new Single_group_Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("userid",uid);
                    bundle.putString("activityid",aid);
                    bundle.putString("where","single_group_message");
                    mygroup.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.flContent, mygroup).commit();

                }
            }
        });
//        edit_userid.putString("userid", uid);
//        edit_userid.commit();



//        reporttext.setClickable(true);
//        reporttext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Connectivity_Checking.isConnectingToInternet()) {
//
//                    Fragment reportfrgmnt = new ReportFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("screen", "screen17");
//                    bundle.putString("userid", uid);
//                    bundle.putString("activityid",aid);
//                    bundle.putString("owner_id","null");
//                    bundle.putString("where",Where);
//                    reportfrgmnt.setArguments(bundle);
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.flContent, reportfrgmnt);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//
//                } else {
//                    Splashscreen dia = new Splashscreen();
//                    dia.Connectivity_Dialog_with_refresh(getActivity());
//                    progressDialog.dismiss();
//                }
//
//            }
//        });

//    btnJoineActivity.setClickable(true);
//       btnJoineActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//           public void onClick(View v) {
//               FnJoinActivvity();
//               getActivity().finish();}
//        });



        updatetext.setClickable(true);
        updatetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity_Checking.isConnectingToInternet()) {

                    fragmentManager = getFragmentManager();
                    Update_Activity update_activity = new Update_Activity();
                    Bundle args = new Bundle();
                    args.putString("accountDetails", String.valueOf(obj));
                    args.putString("userid", uid);
                    args.putString("activityid", aid);
                    args.putString("where", Where);
                    args.putString("screen", "screen17");
                    update_activity.setArguments(args);
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, update_activity)
                            .addToBackStack(null)
                            .commit();


                } else {
                    Splashscreen dia = new Splashscreen();
                    dia.Connectivity_Dialog_with_refresh(getActivity());
                    progressDialog.dismiss();
                }


            }

        });

        delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.custom_delete_msg);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button yes = (Button) dialog.findViewById(R.id.yes);
                Button no = (Button) dialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // new Custom_Progress(getActivity());
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("http://52.37.136.238/JoinMe/Activity.svc/DeleteActivity/" + aid,
                                new AsyncHttpResponseHandler() {
                                    // When the response returned by REST has Http response code '200'

                                    public void onSuccess(String response) {
                                        // Hide Progress Dialog
                                        //  prgDialog.hide();
                                        try {
                                            // Extract JSON Object from JSON returned by REST WS
                                            JSONObject obj = new JSONObject(response);
                                            String del_msg = obj.getString("message");
                                            if (del_msg.equals("Deleted Successfully")) {
                                                edit_activity_id.clear().commit();
                                                fragmentManager = getFragmentManager();
                                                Mygroup mygroup = new Mygroup();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.flContent, mygroup)
                                                        .addToBackStack(null)
                                                        .commit();
                                                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                                                //  new Custom_Progress(getActivity()).dismiss();
                                                Toast.makeText(getActivity(), del_msg, Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(getActivity(), del_msg, Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
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


//        minimumRating.setOnTouchListener(new View.OnTouchListener()
//        {
//            public boolean onTouch(View view, MotionEvent event)
//            {
//                float touchPositionX = event.getX();
//                float width = minimumRating.getWidth();
//                float starsf = (touchPositionX / width) * 5.0f;
//                int stars = (int)starsf + 1;
//                minimumRating.setRating(stars);
//                return true;
//            }
//        });
        final ImageView createrimage= (ImageView) v.findViewById(R.id.createrimage);
        createrimage.setClickable(true);
        createrimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                Screen13 screen13 = new Screen13();
                Bundle bundle = new Bundle();
                bundle.putString("owner_id", owner_id);
                bundle.putString("userid",uid);
                bundle.putString("activityid",aid);
                bundle.putString("where",Where);
                bundle.putString("screen","screen17");
                screen13.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, screen13)
                        .addToBackStack(null)
                        .commit();


            }
        });
        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.VISIBLE);
        msg = (ImageView) refTool.findViewById(R.id.msg);
        msg.setBackgroundResource(R.drawable.custo_msg);
        logo = (ImageView) refTool.findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(getActivity(), null, null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Intent i = new Intent(getContext(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
            }
        });
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
           // Toast.makeText(getActivity(), (String.valueOf(latitude) + "\n" + String.valueOf(longitude)), Toast.LENGTH_SHORT).show();
        }
        if (Connectivity_Checking.isConnectingToInternet()) {

            AsyncHttpClient client = new AsyncHttpClient();
            client.get("http://52.37.136.238/JoinMe/Activity.svc/GetUserActivityDetails/" + uid + "/" + aid + "/" + longitude + "/" + latitude,
                    new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http response code '200'

                        public void onSuccess(String response) {
                            // Hide Progress Dialog
                            //  prgDialog.hide();
                            try {
                                // Extract JSON Object from JSON returned by REST WS
                                obj = new JSONObject(response);


                                JSONObject json = null;
                                try {
                                    json = new JSONObject(String.valueOf(obj));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONObject message_responce = null;
                                message_responce = json.getJSONObject("response");
                                //edit_userid.putString("userid", message_responce.getString("userid"));
                                //edit_userid.commit();


                                /************************* UserDetails start **************************/
                                JSONObject userdetails = null;
                                try {
                                    userdetails = json.getJSONObject("act_details");

                                    Log.w("ACTIVITY DETAILS", String.valueOf(userdetails));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    //Getting information form the Json Response object
                                    cost = userdetails.getString("activity_cost");
                                    distance = userdetails.getString("activity_distance");
                                    duration = userdetails.getString("activity_duration");
                                    limit = userdetails.getString("participant_limit");
                                    activity_name = userdetails.getString("activity_name");
                                    owner_name = userdetails.getString("activity_owner_name");
                                    owner_pic = userdetails.getString("activity_owner_pic");
                                    rating = userdetails.getString("activity_rating");
                                    review = userdetails.getString("activity_review");
                                    activity_address = userdetails.getString("activity_Adress");
                                    time = userdetails.getString("activity_time");
                                    joined = userdetails.getString("participant_joined");
                                    icon1 = userdetails.getString("acitivity_icon");
                                    owner_id = userdetails.getString("activity_owner_id");
                                    description = userdetails.getString("activity_Description");
                                    String activity_id = userdetails.getString("activity_id");


                                    acitvityname.setText(activity_name);
                                    distancefromnearby.setText(distance + " at " + activity_address);
                                    owner_name1.setText("Created by " + owner_name);
                                    if (!limit.equals("0"))
                                    {
                                        uptopeoples.setText("Up to " + limit + " participants:");

                                    }
                                    else
                                    {
                                        uptopeoples.setText("No participants limitation:");
                                    }
                                currentpeoples.setText("Currently have " + joined);
                                costtext.setText("Cost " + cost);
                                timetext.setText("Description: " + description);

                                //  new DownloadImageTask(createrimage).execute("http://52.37.136.238/JoinMe/" + owner_pic);
                                Picasso.with(getActivity()).load("http://52.37.136.238/JoinMe/" + icon1).placeholder(R.drawable.butterfly).into(icon);
                                //Picasso.with(getActivity()).load("http://52.37.136.238/JoinMe/" + owner_pic).placeholder(R.drawable.butterfly).into(createrimage);
                                Picasso.with(getActivity())
                                        .load("http://52.37.136.238/JoinMe/" + owner_pic) // thumbnail url goes here
                                        .placeholder(R.drawable.butterfly)
                                        .resize(200, 200)
                                        .centerCrop()
                                        .skipMemoryCache()
                                        .into(createrimage, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                            }
                                        });


                                long unixSeconds = Long.parseLong(time);
                                Date date2 = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' hh aa "); // the format of your date
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // give a timezone reference for formating (see comment at the bottom
                                String value = sdf.format(date2);


                                timetextview.setText(value);
                                reviews.setText(review + " reviews");
                                minimumRating.setRating(Float.parseFloat(rating));


                                JSONArray cast = userdetails.getJSONArray("activity_pics");


                                //  Toast.makeText(Login_Activity.this, String.valueOf(cast.length()), Toast.LENGTH_SHORT).show();
                                List<String> allid = new ArrayList<String>();
                                List<String> allurl = new ArrayList<String>();

                                for (int i = 0; i < cast.length(); i++) {
                                    JSONObject actor = cast.getJSONObject(i);
                                    String id = actor.getString("id");
                                    String url = actor.getString("url");
                                    allid.add(id);
                                    url_maps.put("image" + i, "http://52.37.136.238/JoinMe/" + url);
                                    //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();
                                    Log.d("Type", cast.getString(i));
                                }

                                for (String name : url_maps.keySet()) {
                                    TextSliderView textSliderView = new TextSliderView(getActivity());
                                    // initialize a SliderLayout
                                    textSliderView.image(url_maps.get(name))
                                            .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                                                          @Override
                                                                          public void onSliderClick(BaseSliderView baseSliderView) {
                                                                              // some method
                                                                          }
                                                                      }
                                            );
                                    mDemoSlider.addSlider(textSliderView);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        catch(
                        JSONException e
                        )

                        {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
        });


        } else {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());
            progressDialog.dismiss();
        }

        return v;
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


//    private void FnJoinActivvity() {
//        String userid = user_id.getString("userid", "");
//        String act_id = activity_id.getString("activity_id", "");
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(URL_AddMemberToGroup + userid + "/" + act_id,
//                new AsyncHttpResponseHandler() {
//                    // When the response returned by REST has Http response code '200'
//
//                    public void onSuccess(String response) {
//
//                        fragmentManager = getFragmentManager();
//                        Single_group_Message update_activity = new Single_group_Message();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.flContent, update_activity)
//                                .addToBackStack(null)
//                                .commit();
//
//                    }
//                });
//
//
//
//    }

    private void replaceFragment(Fragment fr) {
        Fragment mygroup=new Mygroup();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent,mygroup);
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
                    if (Where.equals("mygroups")) {
                        FragmentManager fragmentManager = getFragmentManager();
                        Mygroup mygroup  = new Mygroup();
                        fragmentManager.beginTransaction().replace(R.id.flContent,mygroup).commit();
                        getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                    }
                    if (Where.equals("single_group_message"))
                    {
                        FragmentManager fragmentManager = getFragmentManager();
                        Single_group_Message mygroup  = new Single_group_Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("userid",uid);
                        bundle.putString("activityid",aid);
                        bundle.putString("where",Where);
                        mygroup.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.flContent, mygroup).commit();

                    }
                    return true;
                }
                return false;
            }
        });
    }


}
