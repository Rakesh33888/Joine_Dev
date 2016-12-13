package com.brahmasys.bts.joinme;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.siyamed.shapeimageview.CircularImageView;
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
import java.util.List;
import java.util.TimeZone;

/**
 * Created by apple on 21/10/16.
 */
public class Notification_Screen extends Fragment {


    TextView did_not_go,remind_later,skip,activity_name1;
    TextView review_did_not_go,review_remind_later,review_skip,review_activity_name;
    TextView whoshowed_skip,whoshowed_remind_later,whoshowed_activityname;
    EditText activity_fedback,review_activity_fedback;
    RatingBar ratingBar;
    ListView peoples_who_showed;
    LinearLayout rating_action,rating_submission,review_submission,review_action;
    Button submit,review_submit,whoshowed_submit;
    String user_id,activity_id,type,activity_name="null",url="null",time;
    CircularImageView activity_image,review_activity_image,whoshowed_activity_image;
    private static final String TAG = "SendFeedBack";
    private static final String URL = Constant.Feedback;
    private static final String REVIEW_TAG = "ReviewActivity";
    private static final String REVIEW_URL = Constant.ReviewActivity;
    private static final String WHOSHOWED_TAG = "WhoShowedUp";
    private static final String WHOSHOWED_URL = Constant.WhoShowedUp;
    private ArrayList<Book> books;
    private ArrayAdapter<Book> adapter;
    List<String> allid;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.notification_screen,container,false);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Bundle bundle = this.getArguments();
        user_id      = bundle.getString("userid", "0");
        activity_id  = bundle.getString("activityid","0");
        type         = bundle.getString("type","null");
        time         = bundle.getString("time","null");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.GetUserActivityDetails + user_id + "/" + activity_id + "/" + 0 + "/" +0,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //  prgDialog.hide();
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject  obj = new JSONObject(response);
                            JSONObject json = null;
                            try {
                                json = new JSONObject(String.valueOf(obj));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONObject message_responce = null;
                            message_responce = json.getJSONObject("response");

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
                                 activity_name = userdetails.getString("activity_name");
                                  JSONArray cast = userdetails.getJSONArray("activity_pics");
                                   for (int i = 0; i < cast.length(); i++) {
                                    JSONObject actor = cast.getJSONObject(0);
                                    if (i==0) {
                                        url = actor.getString("url");

                                    }
                                    Log.d("Type", cast.getString(i));

                                }
//                                 Log.e("ACTIVITY NAME", activity_name);
//                                 Log.e("ACTIVITY PICK URL",url);

                                if (type.equals("rating")||type.equals("ratingreminder")) {
                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                    dialog.setContentView(R.layout.rating_subscribe);
                                    dialog.setCancelable(false);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();
                                    did_not_go = (TextView) dialog.findViewById(R.id.did_not_go);
                                    remind_later = (TextView) dialog.findViewById(R.id.remind_later);
                                    skip = (TextView) dialog.findViewById(R.id.skip);
                                    ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
                                    rating_action = (LinearLayout) dialog.findViewById(R.id.rating_action);
                                    rating_submission = (LinearLayout) dialog.findViewById(R.id.rating_submission);
                                    submit = (Button) dialog.findViewById(R.id.submit);
                                    activity_fedback = (EditText)dialog.findViewById(R.id.activity_feedback);
                                    activity_image  = (CircularImageView) dialog.findViewById(R.id.image_of_activity);
                                    activity_name1  = (TextView) dialog.findViewById(R.id.activity_name);

                                    if (time.equals("later"))
                                    {
                                        remind_later.setVisibility(View.GONE);
                                    }

                                    activity_name1.setText("How was the activty "+activity_name +"?");
                                            //Picasso.with(getActivity()).load("http://52.37.136.238/JoinMe/" + url).placeholder(R.drawable.butterfly).into(activity_image);
                                            Picasso.with(getActivity())
                                                    .load(Constant.BASE_URL + url) // thumbnail url goes here
                                                    .placeholder(R.drawable.butterfly)
                                                    .resize(200, 200)
                                                    .into(activity_image, new Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                        }

                                                        @Override
                                                        public void onError() {
                                                        }
                                                    });

                                    did_not_go.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AsyncHttpClient client = new AsyncHttpClient();
                                            client.get(Constant.JoinActivity + user_id + "/" + activity_id+ "/" +"false",
                                                    new AsyncHttpResponseHandler() {
                                                        // When the response returned by REST has Http response code '200'

                                                        public void onSuccess(String response) {
                                                            Log.e("DIDN'T",String.valueOf(response));
                                                            Intent home = new Intent(getActivity(), Screen16.class);
                                                            startActivity(home);
                                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                            getActivity().finish();

                                                        }
                                                    });
                                        }
                                    });
                                    skip.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent home = new Intent(getActivity(), Screen16.class);
                                            startActivity(home);
                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                            getActivity().finish();
                                        }
                                    });
                                    remind_later.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AsyncHttpClient client = new AsyncHttpClient();
                                            client.get(Constant.RemindMeLaterRating + user_id + "/" + activity_id,
                                                    new AsyncHttpResponseHandler() {
                                                        // When the response returned by REST has Http response code '200'

                                                        public void onSuccess(String response) {

                                                            Intent home = new Intent(getActivity(), Screen16.class);
                                                            startActivity(home);
                                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                            getActivity().finish();

                                                        }
                                                    });
                                        }
                                    });
                                    ratingBar.setOnTouchListener(new View.OnTouchListener() {
                                        public boolean onTouch(View view, MotionEvent event) {
                                            float touchPositionX = event.getX();
                                            float width = ratingBar.getWidth();
                                            float starsf = (touchPositionX / width) * 5.0f;
                                            int stars = (int) starsf + 1;
                                            ratingBar.setRating(stars);
                                            rating_action.setVisibility(View.GONE);
                                            rating_submission.setVisibility(View.VISIBLE);
                                            ratingBar.setFocusable(false);
                                            ratingBar.setClickable(false);
                                            ratingBar.setEnabled(false);
                                            ratingBar.setActivated(false);
                                            ratingBar.setIsIndicator(false);

                                            Log.e("Rating", String.valueOf(stars));
                                            AsyncHttpClient client = new AsyncHttpClient();
                                            client.get(Constant.RateActivity + user_id + "/" + activity_id + "/" + stars,
                                                    new AsyncHttpResponseHandler() {
                                                        // When the response returned by REST has Http response code '200'
                                                        public void onSuccess(String response) {

                                                            Log.e("Rating responce",String.valueOf(response));
                                                        }
                                                    });
                                            return true;
                                        }
                                    });

                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (activity_fedback.getText().toString().length() > 10) {
                                                JSONObject jsonObjSend = new JSONObject();
                                                try {
                                                    // Add key/value pairs
                                                    jsonObjSend.put("activityid", activity_id);
                                                    jsonObjSend.put("feedback_message", activity_fedback.getText().toString());
                                                    jsonObjSend.put("userid", user_id);
                                                    //  hideDialog();
                                                    Log.i(TAG, jsonObjSend.toString(3));

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                JSONObject jsonObjRecv = HttpClient.SendHttpPost(URL, jsonObjSend);
                                                JSONObject json = null;
                                                try {
                                                    json = new JSONObject(String.valueOf(jsonObjRecv));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Intent home = new Intent(getActivity(), Screen16.class);
                                                startActivity(home);
                                                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                getActivity().finish();

                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), "Atleast you have to enter 10 characters...! ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else if(type.equals("review")||type.equals("reviewrminder"))
                                {
                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                    dialog.setContentView(R.layout.review_activity);
                                    dialog.setCancelable(false);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();
                                    review_did_not_go = (TextView) dialog.findViewById(R.id.review_did_not_go);
                                    review_remind_later = (TextView) dialog.findViewById(R.id.review_remind_later);
                                    review_skip = (TextView) dialog.findViewById(R.id.review_skip);

                                    review_action = (LinearLayout) dialog.findViewById(R.id.review_action);
                                    review_submission = (LinearLayout) dialog.findViewById(R.id.review_submission);
                                    review_submit = (Button) dialog.findViewById(R.id.review_submit);
                                    review_activity_fedback = (EditText)dialog.findViewById(R.id.review_activity_feedback);
                                    review_activity_image  = (CircularImageView) dialog.findViewById(R.id.review_image_of_activity);
                                    review_activity_name  = (TextView) dialog.findViewById(R.id.review_activity_name);

                                    if (time.equals("later"))
                                    {
                                        review_remind_later.setVisibility(View.GONE);
                                    }

                                    review_activity_name.setText(activity_name+ " activity review ? ");
                                  //  Picasso.with(getActivity()).load("http://52.37.136.238/JoinMe/" + url).placeholder(R.drawable.butterfly).into(review_activity_image);
                                    Picasso.with(getActivity())
                                            .load(Constant.BASE_URL + url) // thumbnail url goes here
                                            .placeholder(R.drawable.butterfly)
                                            .resize(200,200)
                                            .into(review_activity_image, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
                                                }
                                            });
                                    review_did_not_go.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AsyncHttpClient client = new AsyncHttpClient();
                                            client.get(Constant.JoinActivity+ user_id + "/" + activity_id + "/" + "false",
                                                    new AsyncHttpResponseHandler() {
                                                        // When the response returned by REST has Http response code '200'

                                                        public void onSuccess(String response) {
                                                            Log.e("DIDN'T", String.valueOf(response));
                                                            Intent home = new Intent(getActivity(), Screen16.class);
                                                            startActivity(home);
                                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                            getActivity().finish();

                                                        }
                                                    });
                                        }
                                    });
                                    review_skip.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent home = new Intent(getActivity(), Screen16.class);
                                            startActivity(home);
                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                            getActivity().finish();
                                        }
                                    });
                                    review_remind_later.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AsyncHttpClient client = new AsyncHttpClient();
                                            client.get(Constant.RemindMeLaterReview + user_id + "/" + activity_id,
                                                    new AsyncHttpResponseHandler() {
                                                        // When the response returned by REST has Http response code '200'

                                                        public void onSuccess(String response) {

                                                            Intent home = new Intent(getActivity(), Screen16.class);
                                                            startActivity(home);
                                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                            getActivity().finish();

                                                        }
                                                    });
                                        }
                                    });

                                    review_activity_fedback.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            review_action.setVisibility(View.GONE);
                                            review_submission.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    review_submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (review_activity_fedback.getText().toString().length() > 10) {
                                                JSONObject jsonObjSend = new JSONObject();
                                                try {
                                                    // Add key/value pairs
                                                    jsonObjSend.put("activityid", activity_id);
                                                    jsonObjSend.put("review", review_activity_fedback.getText().toString());
                                                    jsonObjSend.put("userid", user_id);
                                                    //  hideDialog();
                                                    Log.i(REVIEW_TAG, jsonObjSend.toString(3));

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                JSONObject jsonObjRecv = HttpClient.SendHttpPost(REVIEW_URL, jsonObjSend);
                                                JSONObject json = null;
                                                try {
                                                    json = new JSONObject(String.valueOf(jsonObjRecv));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Intent home = new Intent(getActivity(), Screen16.class);
                                                startActivity(home);
                                                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                getActivity().finish();

                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), "Atleast you have to enter 10 characters...! ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else if(type.equals("showup")||type.equals("showupreminder"))
                                {

                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                    dialog.setContentView(R.layout.activity_compliting);
                                    dialog.setCancelable(false);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();
                                    whoshowed_submit = (Button) dialog.findViewById(R.id.submitte_congratulation);
                                    peoples_who_showed = (ListView)dialog.findViewById(R.id.peoples_who_showed);
                                    whoshowed_remind_later = (TextView) dialog.findViewById(R.id.remindme_congratulation);
                                    whoshowed_skip = (TextView) dialog.findViewById(R.id.skip_congatulation);
                                    whoshowed_activity_image  = (CircularImageView) dialog.findViewById(R.id.image_activity_congatulation);
                                    whoshowed_activityname    = (TextView) dialog.findViewById(R.id.congatulaton_text);
                                  //  setListViewAdapter();
                                    if (time.equals("later"))
                                    {
                                        whoshowed_remind_later.setVisibility(View.GONE);
                                    }

                                    whoshowed_activityname.setText(whoshowed_activityname.getText() + activity_name);
                                  //  Picasso.with(getActivity()).load("http://52.37.136.238/JoinMe/" + url).placeholder(R.drawable.butterfly).into(whoshowed_activity_image);

                                    Picasso.with(getActivity())
                                            .load(Constant.BASE_URL + url) // thumbnail url goes here
                                            .placeholder(R.drawable.butterfly)
                                            .resize(200,200)
                                            .into(whoshowed_activity_image, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
                                                }
                                            });
                                    whoshowed_skip.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent home = new Intent(getActivity(), Screen16.class);
                                            startActivity(home);
                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                            getActivity().finish();
                                        }
                                    });
                                    whoshowed_remind_later.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            AsyncHttpClient client = new AsyncHttpClient();
                                            client.get(Constant.RemindMeLaterWhoShowedUp + user_id + "/" + activity_id,
                                                    new AsyncHttpResponseHandler() {
                                                        // When the response returned by REST has Http response code '200'

                                                        public void onSuccess(String response) {

                                                            Intent home = new Intent(getActivity(), Screen16.class);
                                                            startActivity(home);
                                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                            getActivity().finish();

                                                        }
                                                    });
                                        }
                                    });





                                    AsyncHttpClient client = new AsyncHttpClient();
                                    client.get(Constant.GetActivityMambers + user_id + "/" + activity_id,
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
                                                        JSONArray cast = json.getJSONArray("users");

                                                        //  Toast.makeText(Login_Activity.this, String.valueOf(cast.length()), Toast.LENGTH_SHORT).show();
                                                                  allid = new ArrayList<String>();
                                                        List<String> allurl = new ArrayList<String>();
                                                        for (int i = 0; i < cast.length(); i++) {
                                                            JSONObject actor = cast.getJSONObject(i);
                                                            String id = actor.getString("userid");
                                                            String url = actor.getString("username");
                                                            allid.add(id);
                                                            allurl.add(url);


                                                            Book book = new Book();
                                                            book.setName(actor.getString("username"));
                                                            book.setImageUrl(actor.getString("userimage"));
                                                            book.setSelected(false);
                                                            // book.setAuthorName(formattedDate);

                                                            books.add(book);
                                                            //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();
                                                            Log.d("Type", cast.getString(i));


                                                        }
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        });
                                                        Log.e("USERNAMES", String.valueOf(allurl));

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }

                                            });
                                    setListViewAdapter();

                                    whoshowed_submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            StringBuffer responseText = new StringBuffer();
                                            //responseText.append("The following were selected...\n");
                                            JSONArray selected_list = new JSONArray();
                                            ArrayList<Book> stateList = books;

                                            for(int i=0;i<stateList.size();i++)
                                            {
                                                Book state = stateList.get(i);

                                                if(state.isSelected())
                                                {
                                                    selected_list.put(allid.get(i));
                                                    //responseText.append(allid.get(i));
                                                }
                                            }

                                            JSONObject jsonObjSend = new JSONObject();
                                            try {
                                                // Add key/value pairs
                                                jsonObjSend.put("activityid", activity_id);
                                                jsonObjSend.put("userids", selected_list);

                                                Log.i(WHOSHOWED_TAG, jsonObjSend.toString(2));

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            JSONObject jsonObjRecv = HttpClient.SendHttpPost(WHOSHOWED_URL, jsonObjSend);
                                            JSONObject json = null;
                                            try {
                                                json = new JSONObject(String.valueOf(jsonObjRecv));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Intent home = new Intent(getActivity(), Screen16.class);
                                            startActivity(home);
                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                            getActivity().finish();

                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Log.e("ACTIVITY NAME1", activity_name);
//                            Log.e("ACTIVITY PICK URL1",url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


        return v;
    }
    private void setListViewAdapter() {
        if (Connectivity_Checking.isConnectingToInternet()) {


            books = new ArrayList<Book>();
            adapter = new Custom_WhoShowedUp(getActivity(), R.layout.custom_whoshowed, books);
            peoples_who_showed.setAdapter(adapter);


        } else {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());
        }
    }


}
