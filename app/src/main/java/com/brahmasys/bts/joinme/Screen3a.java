package com.brahmasys.bts.joinme;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class Screen3a extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imageView;
    Button continue_btn;
    RelativeLayout touch;
    CircularImageView circle_image;
    EditText firstname, lastname, conformation_code,share;
    android.support.v7.app.ActionBar actionBar;
    SegmentedGroup  rgroup;
    RadioButton male,female;
     TextView resend;
    Spinner day,month,year;
    String gender;
    int select_image;
    Context context;
    String deviceuid,device_type="android",registration_type="normal",device_token="";
    boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    ProgressDialog progressDialog;
    SessionManager session;
    private static final int SELECT_FILE1 = 1;
    String selectedPath1 = "NONE";
    HttpEntity resEntity;

    Double latitude=0.0,longitude=0.0;
    public  static final String TOKEN_ID = "token";
    public static final String USERID = "userid";
    public static final String DETAILS = "user_details";
    public static final String USER_PIC = "user_pic";

    SharedPreferences user_id,user_Details,user_pic,token_id;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic,edit_token_id;

    private static final String TAG = "UserRegister";
    private static final String URL = "http://52.37.136.238/JoinMe/User.svc/UserRegister";
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3aa);

        Marshmallow_Permissions.verifyStoragePermissions(Screen3a.this);

       // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        deviceuid = Settings.Secure.getString(Screen3a.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        session = new SessionManager(getApplicationContext());
        user_id =getSharedPreferences(USERID, MODE_PRIVATE);
        edit_userid = user_id.edit();
        user_Details = getSharedPreferences(DETAILS, MODE_PRIVATE);
        edit_user_detals = user_Details.edit();
        user_pic = getSharedPreferences(USER_PIC, MODE_PRIVATE);
        edit_user_pic = user_pic.edit();
        token_id = getSharedPreferences(TOKEN_ID, MODE_PRIVATE);
        edit_token_id = token_id.edit();

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        conformation_code = (EditText) findViewById(R.id.conformation_code);
        share = (EditText) findViewById(R.id.share);
        resend = (TextView) findViewById(R.id.resend_code);

        rgroup = (SegmentedGroup) findViewById(R.id.radioGroup);
        rgroup.setTintColor(Color.parseColor("#005F99"));
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        continue_btn = (Button) findViewById(R.id.continue_btn);
        circle_image = (CircularImageView) findViewById(R.id.imageView0);
        male.setChecked(true);

        day = (Spinner) findViewById(R.id.spinner1);
        month = (Spinner) findViewById(R.id.spinner2);
        year = (Spinner) findViewById(R.id.spinner3);
        touch = (RelativeLayout) findViewById(R.id.scrollView);
        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        Bundle extras = getIntent().getExtras();
        final String mailId = extras.getString("mailId");
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend.setClickable(false);
               AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://52.37.136.238/JoinMe/User.svc/ResendVerificationCode/" + mailId,
                        new AsyncHttpResponseHandler() {
                            // When the response returned by REST has Http response code '200'

                            public void onSuccess(String response) {
                                try {
                                    // Extract JSON Object from JSON returned by REST WS
                                    JSONObject obj = new JSONObject(response);
                                    JSONObject json = null;
                                    try {
                                        json = new JSONObject(String.valueOf(obj));
                                        Toast.makeText(Screen3a.this, json.getString("message")+" " + "Please check your mail", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        });





        List day_list = new ArrayList<Integer>();
        day_list.add(0,"dd");
        for (int i = 1; i <= 31; i++)
        {
            day_list.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, day_list);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
       day.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.month_names));
        month.setAdapter(adapter1);

        List year_list = new ArrayList<Integer>();
        year_list.add(0,"yyyy");
        for (int i = 1910; i <= 2010; i++)
        {
            year_list.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter1 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, year_list);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(spinnerArrayAdapter1);


//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
//                | actionBar.DISPLAY_SHOW_CUSTOM);
//        ImageView imageView4 = new ImageView(actionBar.getThemedContext());
//        imageView4.setScaleType(ImageView.ScaleType.CENTER);
//        imageView4.setImageResource(R.drawable.logoaction);
//        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL
//                | Gravity.CENTER);
//        layoutParams.rightMargin = 40;
//        imageView4.setLayoutParams(layoutParams);
//        actionBar.setCustomView(imageView4);
        circle_image.setClickable(true);
        circle_image.setOnClickListener(new View.OnClickListener() {
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


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //timezone.setText(String.valueOf(latitude) + "\n" + String.valueOf(longitude));
        }



        continue_btn.setOnClickListener(new View.OnClickListener() {
            private View currentFocus;

            public View getCurrentFocus() {
                return currentFocus;
            }

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (Connectivity_Checking.isConnectingToInternet()) {

                    if (select_image == 1) {
                        if (!String.valueOf(day.getSelectedItem()).equals("")&& !String.valueOf(month.getSelectedItem()).equals("")&& !String.valueOf(year.getSelectedItem()).equals(""))
                        {
                            if (firstname.getText().toString().length() >= 2) {
                                if (share.getText().toString().length() >= 10) {

                                    progressDialog = ProgressDialog.show(Screen3a.this, null, null, true);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setContentView(R.layout.custom_progress);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    if (male.isChecked()) {
                                        gender = "male";
                                    } else {
                                        gender = "female";
                                    }
                                    String ye = (String) year.getSelectedItem();
                                    String mon = (String) month.getSelectedItem();
                                    String da = (String) day.getSelectedItem();
                                    String birth = da + "/" + mon + "/" + ye;

                                    if (android.os.Build.VERSION.SDK_INT > 9) {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                    }

                                    JSONObject jsonObjSend = new JSONObject();
                                    try {
                                        // Add key/value pairs
                                        jsonObjSend.put("confermationcode", conformation_code.getText().toString());
                                        jsonObjSend.put("deviceid", deviceuid);
                                        jsonObjSend.put("device_token", token_id.getString("token", "0000"));
                                        jsonObjSend.put("device_type", device_type);
                                        jsonObjSend.put("discription", share.getText().toString());
                                        jsonObjSend.put("dob", birth);
                                        jsonObjSend.put("fname", firstname.getText().toString());
                                        jsonObjSend.put("gender", gender);
                                        jsonObjSend.put("lat", latitude);
                                        jsonObjSend.put("lname", lastname.getText().toString());
                                        jsonObjSend.put("lon", longitude);
                                        jsonObjSend.put("registration_type", registration_type);
                                        jsonObjSend.put("userid", user_id.getString("userid", "null"));
                                        //  hideDialog();
                                        Log.i(TAG, jsonObjSend.toString(13));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    JSONObject jsonObjRecv = com.brahmasys.bts.joinme.HttpClient.SendHttpPost(URL, jsonObjSend);


                                    JSONObject json = null;
                                    try {
                                        json = new JSONObject(String.valueOf(jsonObjRecv));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JSONObject json_LL = null;
                                    try {
                                        json_LL = json.getJSONObject("response");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    try {
                                        String str_value = json_LL.getString("message");

                                        userid = json_LL.getString("userid");


                                        if (str_value.equals("Updated Successfully")) {

                                            edit_userid.putString("userid", userid);
                                            edit_userid.commit();

                                            AsyncHttpClient client = new AsyncHttpClient();
                                            client.get("http://52.37.136.238/JoinMe/User.svc/GetUserDetails/" + userid,
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

                                                                    Log.w("GetDetails", String.valueOf(json));
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
                                                            session.setLogin(true);
                                                            Intent i1 = new Intent(getApplicationContext(), Screen16.class);
                                                            startActivity(i1);
                                                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                            finish();

                                                            progressDialog.dismiss();
                                                        }
                                                    });


                                        } else {
                                            Toast.makeText(Screen3a.this, str_value, Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast toast = Toast.makeText(Screen3a.this, "Description must be having at least 10 characters...!", Toast.LENGTH_SHORT);
                                    View view = toast.getView();

                                    view.setBackgroundResource(R.drawable.smallbox1);
                                    TextView col = (TextView) toast.getView().findViewById(android.R.id.message);
                                    col.setTextColor(Color.RED);
                                    toast.show();
                                }
                            } else {
                                Toast toast = Toast.makeText(Screen3a.this, "Firstname must be having at least 2 letters...!", Toast.LENGTH_SHORT);
                                View view = toast.getView();

                                view.setBackgroundResource(R.drawable.smallbox1);
                                TextView col = (TextView) toast.getView().findViewById(android.R.id.message);
                                col.setTextColor(Color.RED);
                                toast.show();
                                //progressDialog.dismiss();
                            }
                        }else
                        {
                            Toast toast = Toast.makeText(Screen3a.this, "Please Choose birthday...!", Toast.LENGTH_SHORT);
                            View view = toast.getView();

                            view.setBackgroundResource(R.drawable.smallbox1);
                            TextView col = (TextView) toast.getView().findViewById(android.R.id.message);
                            col.setTextColor(Color.RED);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(Screen3a.this, "Please choose a photo...!", Toast.LENGTH_SHORT);
                        View view = toast.getView();

                        view.setBackgroundResource(R.drawable.smallbox1);
                        TextView col = (TextView) toast.getView().findViewById(android.R.id.message);
                        col.setTextColor(Color.RED);
                        toast.show();

                    }


                } else {
                    Splashscreen dia = new Splashscreen();
                    dia.Connectivity_Dialog_with_refresh(Screen3a.this);
                  //  progressDialog.dismiss();
                }

            }
        });
    }


//    public void openGallery(int req_code){
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);
//    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (isKitKat && resultCode != Activity.RESULT_CANCELED) {

            String uri = new ImagePath().getPath(Screen3a.this, data.getData());


            if (requestCode == SELECT_FILE1) {

                selectedPath1 = uri;
                circle_image.setImageBitmap(new DecodeImage().decodeFile(selectedPath1));
                select_image =1;
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






//        if (resultCode == RESULT_OK) {
//            Uri selectedImageUri = data.getData();
//            if (requestCode == SELECT_FILE1)
//            {
//
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
//                    circle_image.setImageBitmap(bitmap);
//                    select_image =nari1;
//                } catch (IOException e) {
//
//                }
//
//                selectedPath1 = getPath(selectedImageUri);
//                System.out.println("selectedPath1 : " + selectedPath1);
//            }
//
//            Thread thread=new Thread(new Runnable(){
//                public void run(){
//                    doFileUpload();
//                    runOnUiThread(new Runnable(){
//                        public void run() {
//
//                        }
//                    });
//                }
//            });
//            thread.start();

//        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private void doFileUpload(){

        File file1 = new File(selectedPath1);

        String urlString = "http://52.37.136.238/JoinMe/User.svc/UpdateUserProfilePicture/"+user_id.getString("userid","null");
        try
        {
            HttpClient client = new DefaultHttpClient();
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
                            //Toast.makeText(getApplicationContext(),"Upload Complete. Check the server uploads directory.", Toast.LENGTH_LONG).show();
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
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public boolean onKeyDown(int Keycode, KeyEvent event) {
        if (Keycode == KeyEvent.KEYCODE_BACK) {

            Intent i = new Intent(Screen3a.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            finish();


        } return super.onKeyDown(Keycode,event);
    }


}