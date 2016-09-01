package com.brahmasys.bts.joinme;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity  {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Button facebook, mail,b4,b5;
    Button already_member,show;
    SessionManager session;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    private static final String TAG = "SignUp";
    private static final String URL = "http://52.37.136.238/JoinMe/User.svc/SignUp";

    private static final String TAG1 = "Login";
    private static final String URL1 = "http://52.37.136.238/JoinMe/User.svc/Login";
   public  static final String TOKEN_ID = "token";

    String deviceuid,device_type="android";
    public static final String USERID = "userid";
    public static final String DETAILS = "user_details";
    public static final String USER_PIC = "user_pic";
    private static final String LAT_LNG = "lat_lng";
    public  static final String PROFILE_PIC = "profile_pic";

    SharedPreferences user_id,user_Details,user_pic,lat_lng,token_id,profile_pic;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic,edit_lat_lng,edit_token_id,edit_profile_pic;
    String userid,social_id=" ",login_type="regular",device_token;
    Double latitude=0.0,longitude=0.0;


    int refresh=0;
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebook = (Button) findViewById(R.id.facebook);
        mail = (Button) findViewById(R.id.mail);
        session = new SessionManager(getApplicationContext());
        user_id =getSharedPreferences(USERID, MODE_PRIVATE);
        edit_userid = user_id.edit();
        user_Details = getSharedPreferences(DETAILS, MODE_PRIVATE);
        edit_user_detals = user_Details.edit();
        user_pic = getSharedPreferences(USER_PIC, MODE_PRIVATE);
        edit_user_pic = user_pic.edit();
        lat_lng = getSharedPreferences(LAT_LNG, MODE_PRIVATE);
        edit_lat_lng = lat_lng.edit();
        token_id = getSharedPreferences(TOKEN_ID, MODE_PRIVATE);
        edit_token_id = token_id.edit();
        profile_pic = getSharedPreferences(PROFILE_PIC, MODE_PRIVATE);
        edit_profile_pic = profile_pic.edit();

        already_member = (Button) findViewById(R.id.show);



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        deviceuid = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(MainActivity.this,Screen16.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            finish();
        }

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), MainActivity.this)) {
            fetchLocationData();
        }
        else
        {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), MainActivity.this);
        }


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration success
                    String token = intent.getStringExtra("token");

                    edit_token_id.putString("token",token);
                    edit_token_id.commit();
                    //Toast.makeText(getApplicationContext(), "GCM Token:" + token, Toast.LENGTH_LONG).show();

                   // token1.setText(token);
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //Tobe define
                }
            }
        };

        //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }


    }

    public void requestPermission(String strPermission, int perCode, Context _c, Activity _a){

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
            Toast.makeText(getApplicationContext(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
        }
    }

    public static boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocationData();

                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;

        }
    }
    private void fetchLocationData() {

    }

    @Override
    protected void onResume() {
        super.onResume();



        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

      //  Toast.makeText(MainActivity.this, token_id.getString("token","null"), Toast.LENGTH_SHORT).show();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

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
            edit_lat_lng.putString("lat",String.valueOf(latitude));
            edit_lat_lng.putString("lng", String.valueOf(longitude));
            edit_lat_lng.commit();

        already_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                final Dialog dialog = new Dialog(MainActivity.this);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.layout_xml);
                dialog.show();
                b4 = (Button) dialog.findViewById(R.id.button4);
                final EditText email = (EditText) dialog.findViewById(R.id.editText);
                final EditText pass = (EditText) dialog.findViewById(R.id.editText2);


                b4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!email.getText().toString().trim().equals("") && !pass.getText().toString().trim().equals("")) {

                            if (android.os.Build.VERSION.SDK_INT > 9) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                            }
                            JSONObject jsonObjSend = new JSONObject();
                            try {
                                // Add key/value pairs
                                jsonObjSend.put("device_token",token_id.getString("token","null"));
                                jsonObjSend.put("device_type", device_type);
                                jsonObjSend.put("deviceid", deviceuid);
                                jsonObjSend.put("login_type", login_type);
                                jsonObjSend.put("social_id", social_id);
                                jsonObjSend.put("lat", latitude);
                                jsonObjSend.put("lon", longitude);
                                jsonObjSend.put("email", email.getText().toString());
                                jsonObjSend.put("password", pass.getText().toString());
                                //  hideDialog();
                                Log.i(TAG1, jsonObjSend.toString(9));

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

                            JSONObject json_LL = null;
                            try {
                                json_LL = json.getJSONObject("response");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            JSONObject response = null;
                            try {
                                response = json.getJSONObject("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {

                                String verify = response.getString("isverified");
                                String str_value = json_LL.getString("message");
                                String profile_pic = response.getString("profile_pic");

                                edit_profile_pic.putString("profile_pic",profile_pic);
                                edit_profile_pic.commit();
                                userid = json_LL.getString("userid");


                                if (str_value.equals("User Login Successful")) {
                                    if (verify.equals("true")) {

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


                                        Intent i = new Intent(getApplicationContext(), Screen16.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                        finish();
                                        session.setLogin(true);
                                        Toast toast = Toast.makeText(getApplicationContext(), "Login Succesfull", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        dialog.dismiss();
                                    } else {
                                        Intent i = new Intent(getApplicationContext(), Screen3a.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                        Toast.makeText(MainActivity.this, "Please verify your account...!", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, str_value, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else

                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Enter Credentials...! ", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                });
                // dialog.show();


            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mail) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.layout1_xml);
                    b5 = (Button) dialog.findViewById(R.id.button5);
                    final EditText email = (EditText) dialog.findViewById(R.id.editText3);
                    final EditText pass = (EditText) dialog.findViewById(R.id.editText4);


                    b5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                            CharSequence inputStr = email.getText().toString();
                            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(inputStr);

                            if (!email.getText().toString().trim().equals("") && !pass.getText().toString().trim().equals("")) {
                                if (matcher.matches()) {
                                    if (pass.getText().toString().trim().length() >= 4) {


                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.get("http://52.37.136.238/JoinMe/User.svc/CheckUserEmailAvailability/" + email.getText().toString(),
                                                new AsyncHttpResponseHandler() {
                                                    // When the response returned by REST has Http response code '200'

                                                    public void onSuccess(String response) {
                                                        // Hide Progress Dialog
                                                        //  prgDialog.hide();
                                                        try {
                                                            // Extract JSON Object from JSON returned by REST WS
                                                            JSONObject obj = new JSONObject(response);
                                                            String query = obj.getString("message");
                                                            // Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
                                                            if (query.equals("Available")) {


                                                                JSONObject jsonObjSend = new JSONObject();
                                                                try {
                                                                    // Add key/value pairs
                                                                    jsonObjSend.put("device_token", token_id.getString("token","null"));
                                                                    jsonObjSend.put("device_type", device_type);
                                                                    jsonObjSend.put("deviceid", deviceuid);
                                                                    jsonObjSend.put("email", email.getText().toString());
                                                                    jsonObjSend.put("password", pass.getText().toString());
                                                                    //  hideDialog();
                                                                    Log.i(TAG, jsonObjSend.toString(5));

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

                                                                JSONObject json_LL = null;
                                                                try {
                                                                    json_LL = json.getJSONObject("response");
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }


                                                                try {
                                                                    String str_value = json_LL.getString("message");
                                                                    userid = json_LL.getString("userid");

                                                                    Toast.makeText(MainActivity.this, str_value, Toast.LENGTH_LONG).show();

                                                                    if (str_value.equals("Registered Successfully")) {


                                                                        edit_userid.putString("userid", userid);
                                                                        edit_userid.commit();
                                                                        Intent i1 = new Intent(getApplicationContext(), Screen3a.class);
                                                                        startActivity(i1);
                                                                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                                        finish();
                                                                        // Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                                                                        dialog.dismiss();

                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }


                                                            } else {

                                                                Toast.makeText(getApplicationContext(), "Username already exist!", Toast.LENGTH_LONG).show();
                                                            }

                                                        } catch (JSONException e) {
                                                            // TODO Auto-generated catch block
                                                            Toast.makeText(getApplicationContext(), "Error Occured while parsing [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    // When the response returned by REST has Http response code other than '200' such as '404', '500' or '403' etc

                                                    public void onFailure(int statusCode, Throwable error, String content) {
                                                        // Hide Progress Dialog
                                                        //prgDialog.hide();
                                                        // When Http response code is '404'
                                                        if (statusCode == 404) {
                                                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                                                        }
                                                        // When Http response code is '500'
                                                        else if (statusCode == 500) {
                                                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                                                        }
                                                        // When Http response code other than 404, 500
                                                        else {
                                                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might " +
                                                                            "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });




                                    } else {
                                        Toast.makeText(MainActivity.this, "Password must be at least 4 characters. ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Mail Id.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Invalid connection", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }

                    });
                    dialog.show();

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    @Override
    public void onRestart()
    {
        super.onRestart();
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }



}


