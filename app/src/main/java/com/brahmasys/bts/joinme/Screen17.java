package com.brahmasys.bts.joinme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brahmasys.bts.joinme.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;

public class Screen17 extends android.support.v4.app.Fragment  {
    FrameLayout frameLayoutbck,frameLayoutacity;
    ImageView imageView6;
    ImageView imageViewbck;
    CircularImageView createrimage;
    RatingBar myratingBar;
    TextView reporttext,updatetext;
    TextView acitvityname,distancefromnearby,owner_name1,uptopeoples,currentpeoples,costtext,timetext,timetextview,reviews;
    FragmentManager fragmentManager;
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    String lat="0",lon="0";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        final View v=inflater.inflate(R.layout.screen17,container,false);
        frameLayoutbck= (FrameLayout) v.findViewById(R.id.frameLayoutbck);
        frameLayoutacity= (FrameLayout) v.findViewById(R.id.frameLayoutactity);
        imageViewbck= (ImageView) v.findViewById(R.id.backbtnimage);
        reporttext= (TextView) v.findViewById(R.id.reportactitytext);
        updatetext= (TextView) v.findViewById(R.id.updateactivitytext);
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

        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();


        Bundle bundle = this.getArguments();
        String uid = bundle.getString("userid", "0");
        String aid  = bundle.getString("activityid","0");
       // Toast.makeText(getActivity(), uid+"\n"+aid, Toast.LENGTH_SHORT).show();

        edit_activity_id.putString("activity_id", aid);
        edit_activity_id.commit();

        edit_userid.putString("userid",uid);
        edit_userid.commit();



        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.37.136.238/JoinMe/Activity.svc/GetUserActivityDetails/"+uid+"/"+aid+"/"+0+"/"+0,
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
                                userdetails = json.getJSONObject("act_details");

                                Log.w("ACTIVITY DETAILS",String.valueOf(userdetails));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                //Getting information form the Json Response object
                                String cost = userdetails.getString("activity_cost");
                                String distance = userdetails.getString("activity_distance");
                                String duration = userdetails.getString("activity_duration");
                                String limit = userdetails.getString("participant_limit");
                                String activity_name = userdetails.getString("activity_name");
                                String owner_name = userdetails.getString("activity_owner_name");
                                String owner_pic = userdetails.getString("activity_owner_pic");
                                String rating = userdetails.getString("activity_rating");
                                String review = userdetails.getString("activity_review");
                                String time = userdetails.getString("activity_time");
                                String joined = userdetails.getString("participant_joined");
                                String activity_id = userdetails.getString("activity_id");

                                acitvityname.setText(activity_name);
                                distancefromnearby.setText(distance);
                                owner_name1.setText("Created by "+ owner_name);
                                uptopeoples.setText("Up to "+limit+" peoples:");
                                currentpeoples.setText("Currently have "+joined);
                                costtext.setText("Cost "+cost);
                                timetext.setText("Takes "+duration+"  hours");
                                new DownloadImageTask(createrimage).execute("http://52.37.136.238/JoinMe/" + owner_pic);

                                long timestampString =  Long.parseLong(time);
                                String value = new java.text.SimpleDateFormat("dd.MM.yyyy 'at' KK aa ").
                                        format(new java.util.Date(timestampString * 1000));

                                timetextview.setText(value);
                                reviews.setText(review+" reviews");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


        imageViewbck.setClickable(true);
        imageViewbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();}
        });
        reporttext.setClickable(true);
        reporttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                switch (v.getId()) {
                    case R.id.reportactitytext:
                        fragment = new Fragment();
                        replaceFragment1(fragment);
                        break;
                }
            }
        });
        updatetext.setClickable(true);
        updatetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager=getFragmentManager();
                Update_Activity update_activity =new Update_Activity();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent,update_activity)
                            .addToBackStack(null)
                            .commit();



            }

        });
        final RatingBar minimumRating = (RatingBar)v.findViewById(R.id.myRatingBar);
        minimumRating.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View view, MotionEvent event)
            {
                float touchPositionX = event.getX();
                float width = minimumRating.getWidth();
                float starsf = (touchPositionX / width) * 5.0f;
                int stars = (int)starsf + 1;
                minimumRating.setRating(stars);
                return true;
            }
        });
        ImageView createrimage= (ImageView) v.findViewById(R.id.createrimage);
        createrimage.setClickable(true);
        createrimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager=getFragmentManager();
                Screen13 screen13=new Screen13();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent,screen13)
                            .addToBackStack(null)
                            .commit();





            }
        });

        return v;
    }
    private void replaceFragment1(Fragment fragmen) {
        Fragment reportfrgmnt=new ReportFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent,reportfrgmnt);
        transaction.addToBackStack(null);
        transaction.commit();
    }

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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;

        }

        protected Bitmap doInBackground(String... urls) {

            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);

        }


    }
}
