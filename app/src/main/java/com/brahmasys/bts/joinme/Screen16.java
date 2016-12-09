package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import link.fls.swipestack.SwipeStack;

public class Screen16 extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
        , Mysearch.OnFragmentInteractionListener,
        Mygroup.OnFragmentInteractionListener,
        Appsetting.OnFragmentInteractionListener, SwipeStack.SwipeStackListener,ComponentCallbacks2 {
    private static final int PICK_IMAGE_REQUEST = 2;
    ProgressDialog progressDialog;
    private SwipeStack mSwipeStack;
    private ArrayList<Book> books;
    private SwipeStackAdapter mAdapter;
    private ArrayAdapter<Book> adapter;
    public RelativeLayout relativeLayout_share_icon;
    Context context;
    boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    public Toolbar toolbar;
    ActionBar actionBar;
    ImageView navimage, logo, back_nav,swipestack_image;

    TextView navtextview,join,skip;
    public ImageView shareicon, msg;
    ImageButton reloadactivity;
    LinearLayout backlayoutdrawer;
    NavigationView navigationView;
    Button editbutton;
    ImageView create, like, dislike, btninfo;
    RelativeLayout reltivelayoutlogo, relativeLayoutmsg;
    FragmentManager fragmentManager;
    private static final String TAG1 = "GetUserActivity";
    private static final String URL1 = Constant.GetUserActivity;

    public static final String USERID = "userid";
    public static final String DETAILS = "user_details";
    public static final String USER_PIC = "user_pic";
    private static final String LAT_LNG = "lat_lng";
    public static final String CHAT_ROOM_OPEN="chat_room_open";
    public static final String SKIP_ACTIVITY = "skip_activity";
    SharedPreferences user_id, user_Details, user_pic, lat_lng,chat_room,skip_activity;
    SharedPreferences.Editor edit_userid, edit_user_detals, edit_user_pic, edit_lat_lng,edit_chat_room,edit_skip_activity;
    private static final int SELECT_FILE1 = 1;
    String selectedPath1 = "NONE";
    HttpEntity resEntity;
    int pic_list_size = 0;
    String lat="0.0", lon="0.0";
    List<String> activity_url;
    List<String> allurl;
    Screen19 screen19_fragment;
    Update_Profile update_profile;
    Appsetting appsettings;
    TextView name_activity, time_activity, distance_activity;
    List<String> activity_name, distance, time, activity_id, userid_other;
    ProgressDialog pd;
    Double latitude=0.0,longitude=0.0;
    TextView whoshowed_skip,whoshowed_remind_later,whoshowed_activityname;
    ListView peoples_who_showed;
    List<String> allid;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    String firstname_user="",lastname_user="",gender="male",birth_day,description="",profile_url,profile_id;
    String refresh1="home";
    String scheme="null",host="null",first="null",second="0",third="0";
    String Playstore_link;
    ArrayList<String> Skip_Activity_ID_List;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen16);

        chat_room = getSharedPreferences(CHAT_ROOM_OPEN, MODE_PRIVATE);
        edit_chat_room = chat_room.edit();

        edit_chat_room.putString("chat_room","close");
        edit_chat_room.putString("chat_activity", "0");
        edit_chat_room.commit();


//        Uri data = getIntent().getData();
//
//            scheme = data.getScheme(); // "http"
//            host = data.getHost(); // "twitter.com"
//            List<String> params = data.getPathSegments();
//            first = params.get(0); // "status"
//            second = params.get(1);
//            third = params.get(2);
//            if (first.equals("joinme")) {
//                Other_User_Details screen17 = new Other_User_Details();
//                Bundle bundle = new Bundle();
//                bundle.putString("userid", second);
//                bundle.putString("activityid", third);
//                screen17.setArguments(bundle);
//                fragmentManager.beginTransaction()
//                        .replace(R.id.flContent, screen17)
//                        .addToBackStack(null)
//                        .commit();
//
//        }

        /************* Notification Handling Start  *********************/
        String type = getIntent().getStringExtra("type");
        String notif_user_id = getIntent().getStringExtra("user_id");
        String notif_activity_id = getIntent().getStringExtra("activity_id");
        String notif_time   = getIntent().getStringExtra("time");
        FragmentManager fragmentManager = getSupportFragmentManager();
      //  FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           if (type != null)
           {
            if(type.equals("activity")||type.equals("has been updated")||type.equals("reminder"))
            {
                Other_User_Details screen17 = new Other_User_Details();
                Bundle bundle = new Bundle();
                bundle.putString("userid",notif_user_id);
                bundle.putString("activityid",notif_activity_id);
                screen17.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, screen17)
                        .addToBackStack(null)
                        .commit();

            }
            else if (type.equals("chat"))
            {
                Single_group_Message single_group_message = new Single_group_Message();
                Bundle bundle = new Bundle();
                bundle.putString("userid",notif_user_id);
                bundle.putString("activityid",notif_activity_id);
                single_group_message.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, single_group_message)
                        .addToBackStack(null)
                        .commit();
            }
            else // if (type.equals("rating")||type.equals("review"))
            {
                Notification_Screen favoritesFragment = new Notification_Screen();
                Bundle bundle = new Bundle();
                bundle.putString("userid",notif_user_id);
                bundle.putString("activityid",notif_activity_id);
                bundle.putString("type",type);
                bundle.putString("time",notif_time);
                favoritesFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, favoritesFragment)
                        .addToBackStack(null).commit();
            }
           }

        /************* Notification Handling End  *********************/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        user_id = getSharedPreferences(MainActivity.USERID, MODE_PRIVATE);
        edit_userid = user_id.edit();
        user_Details = getSharedPreferences(MainActivity.DETAILS, MODE_PRIVATE);
        edit_user_detals = user_Details.edit();
        user_pic = getSharedPreferences(MainActivity.USER_PIC, MODE_PRIVATE);
        edit_user_pic = user_pic.edit();
        lat_lng = getSharedPreferences(LAT_LNG, MODE_PRIVATE);
        edit_lat_lng = lat_lng.edit();
        skip_activity = getSharedPreferences(SKIP_ACTIVITY, MODE_PRIVATE);
        edit_skip_activity = skip_activity.edit();
        Skip_Activity_ID_List =new ArrayList<String>();

        name_activity = (TextView) findViewById(R.id.textView14);
        time_activity = (TextView) findViewById(R.id.textView15);
        distance_activity = (TextView) findViewById(R.id.textView16);
        swipestack_image = (ImageView) findViewById(R.id.swipestack_image);
        join = (TextView) findViewById(R.id.join);
        skip = (TextView) findViewById(R.id.skip);
        reloadactivity = (ImageButton) findViewById(R.id.reloadactivity);
        activity_url = new ArrayList<String>();
        activity_name = new ArrayList<String>();
        distance = new ArrayList<String>();
        time = new ArrayList<String>();
        activity_id = new ArrayList<String>();
        userid_other = new ArrayList<String>();
        context = this;
        setListViewAdapter();
        Marshmallow_Permissions.verifyStoragePermissions(Screen16.this);
     //   LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        GetUserData();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            edit_lat_lng.putString("lat",String.valueOf(latitude));
            edit_lat_lng.putString("lng", String.valueOf(longitude));
            edit_lat_lng.commit();

        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Please enable GPS to use this application..")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                   startActivity(callGPSSettingIntent);
                                }
                            });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
        lat = lat_lng.getString("lat", "0.0");
        lon = lat_lng.getString("lng", "0.0");

        userStatus(user_id.getString("userid","000"),"true");
         reloadactivity.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 progressDialog = ProgressDialog.show(Screen16.this, null, null, true);
                 progressDialog.setIndeterminate(true);
                 progressDialog.setCancelable(false);
                 progressDialog.setContentView(R.layout.custom_progress);
                 progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                 progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 new Thread(new Runnable() {
                     @Override
                     public void run() {

                         edit_skip_activity.clear();
                         edit_skip_activity.commit();
                         Skip_Activity_ID_List.clear();
                         Getting_Activities();
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 progressDialog.dismiss();

                             }
                         });

                     }
                 }).start();


             }
         });

        fillWithTestData();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        //GetUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        userStatus(user_id.getString("userid", "0000"), "true");
    }

private void GetUserData()
{
    if (Connectivity_Checking.isConnectingToInternet()) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.GetUserDetails + user_id.getString("userid", ""),
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
                                  firstname_user = userdetails.getString("fname");
                                  lastname_user = userdetails.getString("lname");
                                  birth_day = userdetails.getString("dob");
                                  description =userdetails.getString("about");
                                  gender = userdetails.getString("gender");
                                //Save the data in sharedPreference
                                edit_user_detals.putString("firstname", firstname_user);
                                edit_user_detals.putString("lastname", lastname_user);
                                edit_user_detals.putString("dob", birth_day);
                                edit_user_detals.putString("about", description);
                                edit_user_detals.putString("gender", gender);
                                edit_user_detals.commit();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray cast = userdetails.getJSONArray("user_pic");
                            JSONObject actor = cast.getJSONObject(0);
                            String id = actor.getString("id");
                            String url = actor.getString("url");
                            edit_user_pic.putString("pic_list_size", String.valueOf(cast.length()));
                            edit_user_pic.putString("id" , id);
                            edit_user_pic.putString("url"  , url);
                            edit_user_pic.commit();

//                            edit_user_pic.putString("pic_list_size", String.valueOf(cast.length()));
//                            edit_user_pic.commit();
//                            JSONObject actor1 = cast.getJSONObject(0);
//
//                           String Image_Url = actor1.getString("url");
//                            //  Toast.makeText(Login_Activity.this, String.valueOf(cast.length()), Toast.LENGTH_SHORT).show();
//                            List<String> allid = new ArrayList<String>();
//                            List<String> allurl = new ArrayList<String>();
//
//                            for (int i = 0; i < cast.length(); i++) {
//                                JSONObject actor = cast.getJSONObject(i);
//                                String id = actor.getString("id");
//                                String url = actor.getString("url");
//                                allid.add(id);
//                                allurl.add(url);
//                                //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();
//
//                                Log.d("Type", cast.getString(i));
//                            }
//                            for (int j = 0; j < allid.size(); j++) {
//                                edit_user_pic.putString("id_" + j, allid.get(j));
//                                edit_user_pic.putString("url_" + j, allurl.get(j));
//
//                            }
//                            edit_user_pic.commit();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    } else {
        Splashscreen dia = new Splashscreen();
        dia.Connectivity_Dialog_with_refresh(Screen16.this);

    }
}

    public void fillWithTestData() {
        GetUserData();
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        btninfo = (ImageView) findViewById(R.id.infobutton);
        btninfo.setClickable(true);
        logo = (ImageView) findViewById(R.id.logo);
        create = (ImageView) findViewById(R.id.createnewactivity);
        relativeLayout_share_icon = (RelativeLayout) toolbar.findViewById(R.id.Relativelayout_share_icon);
        shareicon = (ImageView) toolbar.findViewById(R.id.shareicon);
        reltivelayoutlogo = (RelativeLayout) findViewById(R.id.custmtool);
        relativeLayoutmsg = (RelativeLayout) findViewById(R.id.msssg);
        msg = (ImageView) findViewById(R.id.msg);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navimage = (ImageView) header.findViewById(R.id.imageViewab);
        navtextview = (TextView) header.findViewById(R.id.navtextview);
        back_nav = (ImageView) header.findViewById(R.id.back_nav);
        editbutton = (Button) header.findViewById(R.id.editbutton);
        backlayoutdrawer = (LinearLayout) header.findViewById(R.id.backlayoutdrawer);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//             GetUserData();

                navtextview.setText(user_Details.getString("firstname", "") + "\t" + user_Details.getString("lastname", ""));
                pic_list_size = Integer.parseInt(user_pic.getString("pic_list_size", "0"));

                profile_id = user_pic.getString("id", "0");
                profile_url = user_pic.getString("url", "0");
                Picasso.with(Screen16.this)
                        .load(Constant.BASE_URL + profile_url) // thumbnail url goes here
                        .placeholder(R.drawable.butterfly)
                        .resize(200,200)
                        .noFade()
                        .into(navimage, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {
                            }
                        });
            }

        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        shareicon.setVisibility(View.VISIBLE);
        shareicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Connectivity_Checking.isConnectingToInternet()) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(Constant.GetPlayStoreLink,
                            new AsyncHttpResponseHandler() {
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
                                        JSONObject Link = null;
                                        try {
                                            Link = json.getJSONObject("data");
                                            Playstore_link = Link.getString("playStoreLink");
                                            Intent sendIntent = new Intent();
                                            sendIntent.setAction(Intent.ACTION_SEND);
                                            sendIntent.putExtra(Intent.EXTRA_TEXT, Playstore_link);
                                            sendIntent.setType("text/plain");
                                            startActivity(sendIntent);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                }

            }
        });


        Display display = getWindowManager().getDefaultDisplay();
        int stageWidth = display.getWidth();
        int  stageHeight = display.getHeight();
        Log.e("WIDTH", String.valueOf(stageWidth));
        ViewGroup.LayoutParams params = null;
        params =  like .getLayoutParams();
        params.height =(stageWidth/4);
        params.width = (stageWidth/4);
        like .setLayoutParams(params);

        ViewGroup.LayoutParams params1 = null;
        params1 =  dislike .getLayoutParams();
        params1.height =(stageWidth/4);
        params1.width = (stageWidth/4);
        dislike .setLayoutParams(params1);

        ViewGroup.LayoutParams params2 = null;
        params2 =  btninfo .getLayoutParams();
        params2.height =(stageWidth/5);
        params2.width = (stageWidth/5);
        btninfo .setLayoutParams(params2);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                Screen19 screen19 = new Screen19();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, screen19)
                        .addToBackStack(null)
                        .commit();
            }
        });

        msg.setClickable(true);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg.setBackgroundResource(R.drawable.colourbubble);
                fragmentManager = getSupportFragmentManager();
                Messagescreen messagescreen = new Messagescreen();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, messagescreen)
                        .addToBackStack(null)
                        .commit();

            }
        });






//         allid = new ArrayList<String>();
//          allurl = new ArrayList<String>();
//         for (int j = 0; j < pic_list_size; j++) {
//            String id = user_pic.getString("id_" + j, "");
//            String url = user_pic.getString("url_" + j, "");
//
//            allid.add(id);
//            allurl.add(url);
//        }
//        for (int i = 0; i < allid.size(); i++) {
//            if (i == allurl.size() - 1) {
//                //Toast.makeText(Screen16.this,  allurl.get(i), Toast.LENGTH_SHORT).show();
//                //  Picasso.with(this).load("http://52.37.136.238/JoinMe/" + allurl.get(i)).into(navimage);
////                Picasso.with(this)
////                        .load("http://52.37.136.238/JoinMe/" + allurl.get(i))
////                        .placeholder(R.drawable.default_profile)
////                        .into(navimage);
//
//                profile_url=allurl.get(i);
//                profile_id =allid.get(i);
//                Picasso.with(this)
//                        .load("http://52.37.136.238/JoinMe/" + allurl.get(i)) // thumbnail url goes here
//                        .placeholder(R.drawable.butterfly)
//                        .resize(200,200)
//                        .noFade()
//                        .into(navimage, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//
//                            }
//
//                            @Override
//                            public void onError() {
//                            }
//                        });
//
//                // new DownloadImageTask(navimage).execute("http://52.37.136.238/JoinMe/" + allurl.get(i));
//
//            }
//        }

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                drawerLayout.closeDrawers();
                fragmentManager = getSupportFragmentManager();
                Update_Profile other_user = new Update_Profile();
                Bundle bundle = new Bundle();
                bundle.putString("userid", user_id.getString("userid",""));
                bundle.putString("firstname",user_Details.getString("firstname", ""));
                bundle.putString("lastname",user_Details.getString("lastname", ""));
                bundle.putString("dob",user_Details.getString("dob", ""));
                bundle.putString("description",user_Details.getString("about", ""));
                bundle.putString("gender", gender);
                bundle.putString("profile_url", profile_url);
                bundle.putString("profile_id", profile_id);
                other_user.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, other_user)
                        .addToBackStack(null)
                        .commit();

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(false);
        backlayoutdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        Getting_Activities();
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeStack.swipeTopViewToLeft();
                  //  Skip_Activity(activity_id.get(mSwipeStack.getCurrentPosition()), mSwipeStack.getCurrentPosition());
                    mSwipeStack.setRotation(View.DRAWING_CACHE_QUALITY_AUTO);
                    //Skip_Activity(activity_id.get(mSwipeStack.getCurrentPosition()),mSwipeStack.getCurrentPosition());
                    if (mSwipeStack.getCurrentPosition() >= activity_name.size())
                    {
                        Toast.makeText(Screen16.this, "There is no activity...!", Toast.LENGTH_SHORT).show();
                        reloadactivity.setVisibility(View.VISIBLE);
                    }
                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeStack.swipeTopViewToRight();
                    mSwipeStack.setRotation(View.DRAWING_CACHE_QUALITY_AUTO);
                    Member_add_to_Group();
                }
            });

            btninfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity_Info();
                }
            });
            swipestack_image.setClickable(true);
            swipestack_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity_Info();
                }
            });

    }
    public void Activity_Info()
    {
        if (mSwipeStack.getCurrentPosition() >= activity_name.size())
        {
            Toast.makeText(Screen16.this, "There is no activity...!", Toast.LENGTH_SHORT).show();
            reloadactivity.setVisibility(View.VISIBLE);
        }
        else {
            fragmentManager = getSupportFragmentManager();
            Other_User_Details other_user = new Other_User_Details();
            Bundle bundle = new Bundle();
            bundle.putString("userid", userid_other.get(mSwipeStack.getCurrentPosition()));
            bundle.putString("activityid", activity_id.get(mSwipeStack.getCurrentPosition()));
            bundle.putString("screen","other_user_details");
            bundle.putString("where","null");
            other_user.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, other_user)
                    .addToBackStack(null)
                    .commit();
        }
    }
    public void Getting_Activities()
    {
        if (Connectivity_Checking.isConnectingToInternet()) {

            JSONObject jsonObjSend = new JSONObject();
            try {
                // Add key/value pairs
                jsonObjSend.put("lat", lat);
                jsonObjSend.put("lon", lon);
                jsonObjSend.put("userid", user_id.getString("userid", "00"));
                //  hideDialog();
                Log.i(TAG1, jsonObjSend.toString(3));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonObjRecv = HttpClient.SendHttpPost(URL1, jsonObjSend);
            JSONObject json = null;
            try {
                json = new JSONObject(String.valueOf(jsonObjRecv));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

               if (!json.equals("")) {

                    JSONArray cast = json.getJSONArray("data");
                    if(!cast.equals("")) {
                        if (cast.length() != 0) {


                            Map<String, ?> allEntries = skip_activity.getAll();
                            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                                Log.e("map values", entry.getKey() + ": " + entry.getValue().toString());

                                Skip_Activity_ID_List.add(entry.getValue().toString());
                            }

                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);



                                if (Skip_Activity_ID_List.contains(actor.getString("activity_id"))) {
                                    continue;
                                } else {
                                    reloadactivity.setVisibility(View.VISIBLE);
                                }

//                               if (skip_activity.getString("id_" + i, "0").equals(actor.getString("activity_id")))
//                               {
//
//                               }
//                               else
//                                   {
//                                            book.setName(actor.getString("activity_name"));
//                                            book.setImageUrl(actor.getString("activity_url"));
//                                            book.setAuthorName(actor.getString("activity_distance"));
//                                            book.setIcon_image(actor.getString("acitivity_icon"));
//                                            long unixSeconds = Long.parseLong(actor.getString("activity_time"));
//                                            Date date2 = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
//                                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' hh:mm aa "); // the format of your date
//                                            sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // give a timezone reference for formating (see comment at the bottom
//                                            String value = sdf.format(date2);
//                                            book.setTime(value);
//                                            books.add(book);
//                                   }

                                String id = actor.getString("activity_url");
                                activity_name.add(actor.getString("activity_name"));
                                distance.add(actor.getString("activity_distance"));
                                time.add(actor.getString("activity_time"));
                                activity_id.add(actor.getString("activity_id"));
                                userid_other.add(actor.getString("userid"));
                                activity_url.add(id);
                                    Book book = new Book();
                                    book.setName(actor.getString("activity_name"));
                                    book.setImageUrl(actor.getString("activity_url"));
                                    book.setAuthorName(actor.getString("activity_distance"));
                                    book.setIcon_image(actor.getString("acitivity_icon"));
                                    long unixSeconds = Long.parseLong(actor.getString("activity_time"));
                                    Date date2 = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' hh:mm aa "); // the format of your date
                                    sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // give a timezone reference for formating (see comment at the bottom
                                    String value = sdf.format(date2);
                                    book.setTime(value);
                                    books.add(book);


                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    adapter.notifyDataSetChanged();
                                    reloadactivity.setVisibility(View.GONE);
                                }
                            });




                        } else {
                            reloadactivity.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        reloadactivity.setVisibility(View.VISIBLE);
                    }
               }
               else {
                   reloadactivity.setVisibility(View.VISIBLE);
               }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        }

//    public void Image_Choose()
//    {
//        Intent intent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
//        } else {
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
//        }
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE1);
//    }
    private void setListViewAdapter() {
        books = new ArrayList<Book>();
        adapter = new SwipeStackAdapter(this, R.layout.card, books);
        mSwipeStack.setAdapter(adapter);
        mSwipeStack.setListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        userStatus(user_id.getString("userid", "0000"), "false");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.my_search) {
            fragmentClass = Mysearch.class;
            navigationView.getMenu().getItem(0).setChecked(true);

        } else if (id == R.id.group_activites) {
            fragmentClass = Mygroup.class;


        } else if (id == R.id.app_setting) {
            fragmentClass = Appsetting.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
   @Override
    public void onViewSwipedToRight(int position) {
        Member_add_to_Group();
    }
    @Override
    public void onViewSwipedToLeft(int position) {
        Skip_Activity(activity_id.get(mSwipeStack.getCurrentPosition()),mSwipeStack.getCurrentPosition());
    }
    @Override
    public void onStackEmpty() {
        reloadactivity.setVisibility(View.VISIBLE);
        reloadactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mSwipeStack.resetStack();
                edit_skip_activity.clear();
                edit_skip_activity.commit();
                Skip_Activity_ID_List.clear();
                Getting_Activities();
                reloadactivity.setVisibility(View.GONE);

            }
        });


        //Toast.makeText(this, "There is no activity", Toast.LENGTH_SHORT).show();


    }

    public void Member_add_to_Group() {

        if (mSwipeStack.getCurrentPosition() >= activity_name.size()) {
            Toast.makeText(Screen16.this, "There is no activity...!", Toast.LENGTH_SHORT).show();
        }
        else {

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(Constant.AddMemberToGroup + user_id.getString("userid", "000") + "/" + activity_id.get(mSwipeStack.getCurrentPosition()),
                    new AsyncHttpResponseHandler() {
                        // When the response returned by REST has Http response code '200'

                        public void onSuccess(String response) {

                            try {
                                // Extract JSON Object from JSON returned by REST WS
                                JSONObject obj = new JSONObject(response);

                                String result = obj.getString("message");
                                if (result.equals("Updated Successfully")) {
                                    fragmentManager = getSupportFragmentManager();
                                    Other_User_Details update_activity = new Other_User_Details();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userid", userid_other.get(mSwipeStack.getCurrentPosition()-1));
                                    bundle.putString("activityid", activity_id.get(mSwipeStack.getCurrentPosition()-1));
                                    update_activity.setArguments(bundle);
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.flContent, update_activity)
                                            .addToBackStack(null)
                                            .commit();

                                } else {

                                    Toast.makeText(Screen16.this, "String was not recognized as a valid DateTime.", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (screen19_fragment != null) {
            screen19_fragment.onActivityResult(requestCode, resultCode, data);

        }else if(update_profile!=null)
        {
            update_profile.onActivityResult(requestCode, resultCode, data);
        }
        else {


            if (isKitKat && resultCode != Activity.RESULT_CANCELED) {

                String uri = new ImagePath().getPath(Screen16.this, data.getData());


                if (requestCode == SELECT_FILE1) {
                   // pd.show();

                 selectedPath1 = uri;

                }

            }

        }
    }


    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        // For each of your Fragments add an if statement here checking for
        // the class of the Fragment and assign if it to the member variables
        // if a match is found
        if (fragment instanceof Screen19) {

            this.screen19_fragment = (Screen19) fragment;

        }
    }

//    private void doFileUpload() {
//
//        File file1 = new File(selectedPath1);
//
//        String urlString = "http://52.37.136.238/JoinMe/User.svc/UpdateUserPic/" + user_id.getString("userid", "null")+"/"+allid.get(0);
//        try {
//            org.apache.http.client.HttpClient client = new DefaultHttpClient();
//            HttpPost post = new HttpPost(urlString);
//            FileBody bin1 = new FileBody(file1);
//
//            MultipartEntity reqEntity = new MultipartEntity();
//            reqEntity.addPart("uploadedfile1", bin1);
//
//            reqEntity.addPart("user", new StringBody("User"));
//            post.setEntity(reqEntity);
//            HttpResponse response = client.execute(post);
//            resEntity = response.getEntity();
//            final String response_str = EntityUtils.toString(resEntity);
//            if (resEntity != null) {
//                Log.i("RESPONSE", response_str);
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        try {
//
//                            progressDialog.dismiss();
//
//                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        } catch (Exception ex) {
//            Log.e("Debug", "error: " + ex.getMessage(), ex);
//        }
//
//    }

    public void userStatus(String userid,String status)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant.UserOnline + userid + "/" + status,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {

                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("message");

                            Log.e("STATUS RESULT", result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void Skip_Activity(String activity_id,int position)
    {

        edit_skip_activity.putString(activity_id,activity_id);
        edit_skip_activity.commit();
    }
    @Override
    public void onConfigurationChanged(final Configuration newConfig) {   }
    @Override
    public void onLowMemory() {    }
    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // app is in background
            countDownTimer = new MyCountDownTimer(1000000, 1000);
            if (!timerHasStarted) {
                countDownTimer.start();
                timerHasStarted = true;
            } else {
                countDownTimer.cancel();
                timerHasStarted = false;
            }

        }else {
            Log.e("Run","not Background running");
        }
    }
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            userStatus(user_id.getString("userid", "0000"), "false");
            Log.e("Run","finish Background running");
        }
        @Override
        public void onTick(long millisUntilFinished) {        }
    }
    @Override
    public void onStop() {
        super.onStop();

    }


}
