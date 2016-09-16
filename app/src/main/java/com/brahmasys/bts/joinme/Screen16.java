package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
import java.util.ArrayList;
import java.util.List;

import link.fls.swipestack.SwipeStack;

public class Screen16 extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
        ,Mysearch.OnFragmentInteractionListener,
        Mygroup.OnFragmentInteractionListener,
        Appsetting.OnFragmentInteractionListener,SwipeStack.SwipeStackListener {
    private static final int PICK_IMAGE_REQUEST = 2;

    private SwipeStack mSwipeStack;
    private ArrayList<Book> books;
    private SwipeStackAdapter mAdapter;
    private ArrayAdapter<Book> adapter;
    Context context;
    boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    Toolbar toolbar;
    android.support.v7.app.ActionBar actionBar;
    ImageView navimage, logo, msg,back_nav;
    TextView navtextview;
     public ImageView shareicon;
    LinearLayout backlayoutdrawer;
    Button editbutton;
    ImageView create,like,dislike,btninfo;
    RelativeLayout reltivelayoutlogo, relativeLayoutmsg;
    FragmentManager fragmentManager;
    private static final String TAG1 = "GetUserActivity";
    private static final String URL1 = "http://52.37.136.238/JoinMe/Activity.svc/GetUserActivity";

    public static final String USERID = "userid";
    public static final String DETAILS = "user_details";
    public static final String USER_PIC = "user_pic";
    private static final String LAT_LNG = "lat_lng";
    SharedPreferences user_id,user_Details,user_pic,lat_lng;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic,edit_lat_lng;
    private static final int SELECT_FILE1 = 1;
    String selectedPath1 = "NONE";
    HttpEntity resEntity;
    int pic_list_size =0;
    String lat,lon;
    List<String> activity_url;
Screen19 screen19_fragment;
    TextView name_activity,time_activity,distance_activity;
    List<String> activity_name,distance,time,activity_id,userid_other;
    ProgressDialog pd;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen16);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);

        pd = new ProgressDialog(Screen16.this);
        pd.setMessage("Uploading...");


        user_id =getSharedPreferences(USERID, MODE_PRIVATE);
        edit_userid = user_id.edit();
        user_Details = getSharedPreferences(DETAILS, MODE_PRIVATE);
        edit_user_detals = user_Details.edit();
        user_pic = getSharedPreferences(USER_PIC, MODE_PRIVATE);
        edit_user_pic = user_pic.edit();
        lat_lng = getSharedPreferences(LAT_LNG, MODE_PRIVATE);
        edit_lat_lng = lat_lng.edit();
        lat = lat_lng.getString("lat", "0000");
        lon = lat_lng.getString("lng", "0000");
        name_activity = (TextView) findViewById(R.id.textView14);
        time_activity = (TextView) findViewById(R.id.textView15);
        distance_activity = (TextView) findViewById(R.id.textView16);

        shareicon= (ImageView) findViewById(R.id.shareicon);
        shareicon.setVisibility(View.VISIBLE);

        shareicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                finish();
            }
        });


        activity_url = new ArrayList<String>();
        activity_name= new ArrayList<String>();
        distance= new ArrayList<String>();
        time= new ArrayList<String>();
        activity_id= new ArrayList<String>();
        userid_other= new ArrayList<String>();

        context=this;
        setListViewAdapter();

     Marshmallow_Permissions.verifyStoragePermissions(Screen16.this);

    }


    @Override
    protected void onStart() {
        super.onStart();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.37.136.238/JoinMe/User.svc/GetUserDetails/" + user_id.getString("userid", ""),
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
                                String firstname_user = userdetails.getString("fname");
                                String lastname_user = userdetails.getString("lname");

                                //Save the data in sharedPreference
                                edit_user_detals.putString("firstname", firstname_user);
                                edit_user_detals.putString("lastname", lastname_user);
                                edit_user_detals.commit();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray cast = userdetails.getJSONArray("user_pic");
                            edit_user_pic.putString("pic_list_size", String.valueOf(cast.length()));
                            edit_user_pic.commit();

                            //  Toast.makeText(Login_Activity.this, String.valueOf(cast.length()), Toast.LENGTH_SHORT).show();
                            List<String> allid = new ArrayList<String>();
                            List<String> allurl = new ArrayList<String>();

                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String id = actor.getString("id");
                                String url = actor.getString("url");
                                allid.add(id);
                                allurl.add(url);
                                //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();

                                Log.d("Type", cast.getString(i));
                            }
                            for (int j = 0; j < allid.size(); j++) {
                                edit_user_pic.putString("id_" + j, allid.get(j));
                                edit_user_pic.putString("url_" + j, allurl.get(j));

                            }
                            edit_user_pic.commit();
                            edit_user_pic.commit();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillWithTestData();

    }

    private void fillWithTestData() {


        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);






        btninfo = (ImageView)findViewById(R.id.infobutton);
        btninfo.setClickable(true);


        logo = (ImageView) findViewById(R.id.logo);
        create= (ImageView)findViewById(R.id.createnewactivity);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager=getSupportFragmentManager();
                Screen19 screen19=new Screen19();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent,screen19)
                            .addToBackStack(null)
                            .commit();



            }
        });

        reltivelayoutlogo = (RelativeLayout) findViewById(R.id.custmtool);
        relativeLayoutmsg = (RelativeLayout) findViewById(R.id.msssg);
        msg = (ImageView) findViewById(R.id.msg);
        msg.setClickable(true);

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager=getSupportFragmentManager();
                Messagescreen messagescreen=new Messagescreen();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent,messagescreen)
                        .addToBackStack(null)
                        .commit();

            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navimage = (ImageView) header.findViewById(R.id.imageViewab);
        navtextview = (TextView) header.findViewById(R.id.navtextview);
        back_nav = (ImageView) header.findViewById(R.id.back_nav);
        editbutton= (Button) header.findViewById(R.id.editbutton);


        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                }else{
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE1);


            }
        });
        backlayoutdrawer= (LinearLayout) header.findViewById(R.id.backlayoutdrawer);
        navtextview.setText(user_Details.getString("firstname","")+"\t"+user_Details.getString("lastname","null"));

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
         pic_list_size = Integer.parseInt(user_pic.getString("pic_list_size","0"));
        List<String> allid = new ArrayList<String>();
        List<String> allurl = new ArrayList<String>();

        for (int j = 0; j < pic_list_size; j++)
        {
           String id= user_pic.getString("id_" + j, "");
           String url= user_pic.getString("url_" + j, "");

            allid.add(id);
            allurl.add(url);
        }


        for (int i = 0; i< allid.size(); i++) {


            if (i==allurl.size()-1)
            {
                //Toast.makeText(Screen16.this,  allurl.get(i), Toast.LENGTH_SHORT).show();
                Picasso.with(this).load("http://52.37.136.238/JoinMe/" + allurl.get(i)).into(navimage);
               // new DownloadImageTask(navimage).execute("http://52.37.136.238/JoinMe/" + allurl.get(i));

            }
        }
        navigationView.setNavigationItemSelectedListener(this);
        navimage.setClickable(true);

        navimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        backlayoutdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });


        JSONObject jsonObjSend = new JSONObject();
        try {
            // Add key/value pairs

            jsonObjSend.put("lat",lat);
            jsonObjSend.put("lon",lon);
            jsonObjSend.put("userid", user_id.getString("userid","00"));
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


            JSONArray cast = json.getJSONArray("data");

            //  Toast.makeText(Login_Activity.this, String.valueOf(cast.length()), Toast.LENGTH_SHORT).show();
            //List<String> allid = new ArrayList<String>();


            for (int i = 0; i < cast.length(); i++) {
                JSONObject actor = cast.getJSONObject(i);
                String id = actor.getString("activity_url");

                activity_name.add(actor.getString("activity_name"));
                distance.add(actor.getString("activity_distance"));
                time .add(actor.getString("activity_time"));
                activity_id.add(actor.getString("activity_id"));
                userid_other.add(actor.getString("userid"));
                activity_url.add(id);

                Book book = new Book();
                book.setName(actor.getString("activity_name"));
                book.setImageUrl(actor.getString("activity_url"));
                book.setAuthorName(actor.getString("activity_distance"));
                book.setIcon_image(actor.getString("acitivity_icon"));
                long timestampString = Long.parseLong(actor.getString("activity_time"));
                String value = new java.text.SimpleDateFormat("dd.MM.yyyy 'at' KK aa ").
                        format(new java.util.Date(timestampString * 1000));

                book.setTime(value);

                books.add(book);
                Log.d("Type", cast.getString(i));
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeStack.onViewSwipedToLeft();


            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mSwipeStack.onViewSwipedToRight();
                Log.w("position", String.valueOf(mSwipeStack.getCurrentPosition()));



                Member_add_to_Group();

            }
        });

        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSwipeStack.getCurrentPosition() >= activity_name.size()) {

                    Toast.makeText(Screen16.this, "There is no activities", Toast.LENGTH_SHORT).show();

                }
                else
                {
                   // Toast.makeText(Screen16.this, userid_other.get(mSwipeStack.getCurrentPosition()), Toast.LENGTH_SHORT).show();

                    fragmentManager = getSupportFragmentManager();
                    Other_User_Details other_user = new Other_User_Details();
                    Bundle bundle = new Bundle();
                    bundle.putString("userid", userid_other.get(mSwipeStack.getCurrentPosition()));
                    bundle.putString("activityid", activity_id.get(mSwipeStack.getCurrentPosition()));
                    other_user.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, other_user)
                            .addToBackStack(null)
                            .commit();

                }





            }
        });


    }
    private void setListViewAdapter() {
        books = new ArrayList<Book>();
        adapter = new SwipeStackAdapter(this, R.layout.card, books);
        mSwipeStack.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.my_search) {
            fragmentClass = Mysearch.class;



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
        Book swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, getString(R.string.view_swiped_right, swipedElement),
                Toast.LENGTH_SHORT).show();
        Member_add_to_Group();
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        Book swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, getString(R.string.view_swiped_left, swipedElement),
                Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onStackEmpty() {
        Toast.makeText(this, R.string.stack_empty, Toast.LENGTH_SHORT).show();
    }


//    public void openGallery(int req_code){
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select file to upload "), req_code);
//    }
//

    public  void Member_add_to_Group()
    {

        if (mSwipeStack.getCurrentPosition() >= activity_name.size()) {

            Toast.makeText(Screen16.this, "There is no activities", Toast.LENGTH_SHORT).show();

        }
        else {
         //   Toast.makeText(Screen16.this, activity_id.get(mSwipeStack.getCurrentPosition()), Toast.LENGTH_SHORT).show();


           AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://52.37.136.238/JoinMe/Activity.svc/AddMemberToGroup/" + user_id.getString("userid","000") + "/" + activity_id.get(mSwipeStack.getCurrentPosition()),
                        new AsyncHttpResponseHandler() {
                            // When the response returned by REST has Http response code '200'

                            public void onSuccess(String response) {

                                try {
                                    // Extract JSON Object from JSON returned by REST WS
                                    JSONObject obj = new JSONObject(response);

                                    String result = obj.getString("message");

                                    if (result.equals("Updated Successfully")) {

                                        fragmentManager = getSupportFragmentManager();
                                        Single_group_Message update_activity = new Single_group_Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("activityid",activity_id.get(mSwipeStack.getCurrentPosition()));
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
        if(screen19_fragment != null) {
            screen19_fragment.onActivityResult(requestCode, resultCode, data);
        }else {


            if (isKitKat && resultCode != Activity.RESULT_CANCELED) {

                String uri = new ImagePath().getPath(Screen16.this, data.getData());


                if (requestCode == SELECT_FILE1) {
                    pd.show();
                    selectedPath1 = uri;
                    navimage.setImageBitmap(new DecodeImage().decodeFile(selectedPath1));

                }
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        doFileUpload();
                        runOnUiThread(new Runnable() {
                            public void run() {

                            }
                        });
                    }
                });
                thread.start();
             }

        }
    }



    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        // For each of your Fragments add an if statement here checking for
        // the class of the Fragment and assign if it to the member variables
        // if a match is found
        if (fragment instanceof Screen19) {

            this.screen19_fragment= (Screen19) fragment;

        }
    }

    private void doFileUpload(){

        File file1 = new File(selectedPath1);

        String urlString = "http://52.37.136.238/JoinMe/User.svc/UpdateUserProfilePicture/"+user_id.getString("userid","null");
        try
        {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            FileBody bin1 = new FileBody(file1);

            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("uploadedfile1", bin1);

            reqEntity.addPart("user", new StringBody("User"));
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                Log.i("RESPONSE",response_str);
                runOnUiThread(new Runnable(){
                    public void run() {
                        try {


                            //    res.setTextColor(Color.GREEN);
                            //    res.setText("n Response from server : n " + response_str);
                            pd.hide();
                            pd.dismiss();
                           Toast.makeText(getApplicationContext(),"Updated Successfully", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch (Exception ex){
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
    }

}
