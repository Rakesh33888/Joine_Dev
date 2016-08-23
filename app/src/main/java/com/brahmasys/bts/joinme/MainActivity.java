package com.brahmasys.bts.joinme;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    Button facebook, mail,b4,b5;
    TextView tv1, tv2, already_member;
     SessionManager session;
    private static final String TAG = "SignUp";
    private static final String URL = "http://52.37.136.238/JoinMe/User.svc/SignUp";

    private static final String TAG1 = "Login";
    private static final String URL1 = "http://52.37.136.238/JoinMe/User.svc/Login";


    String deviceuid,device_type="android";
    public static final String USERID = "userid";
    public static final String DETAILS = "user_details";
    public static final String USER_PIC = "user_pic";

    SharedPreferences user_id,user_Details,user_pic;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic;
    String userid,social_id=" ",login_type="regular";


    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager locmgr;
    Double lat  ;
    Double lon  ;
    double lat1,lon1;
    int refresh=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebook = (Button) findViewById(R.id.facebook);
        mail = (Button) findViewById(R.id.mail);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        session = new SessionManager(getApplicationContext());

        user_id =getSharedPreferences(USERID, MODE_PRIVATE);
        edit_userid = user_id.edit();
        user_Details = getSharedPreferences(DETAILS, MODE_PRIVATE);
        edit_user_detals = user_Details.edit();
        user_pic = getSharedPreferences(USER_PIC, MODE_PRIVATE);
        edit_user_pic = user_pic.edit();


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

        already_member = (TextView) findViewById(R.id.btn3);
        already_member.setClickable(true);

        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();

        onConnected(savedInstanceState);
        onConnected(new Bundle());






    }


    @Override
    protected void onResume() {
        super.onResume();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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

                                    refresh = 1;
                                }
                            });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();



        }
for (int i = 0;i<refresh;i++)
{
    finish();
    overridePendingTransition(0, 0);
    startActivity(getIntent());
    overridePendingTransition( 0, 0);
}

    }


    public void onConnectionFailed(ConnectionResult arg0) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();


    }
    @Override
    public void onConnected(Bundle arg0) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            this.lat = mLastLocation.getLatitude();
            this.lon = mLastLocation.getLongitude();
            this.lat1 = lat;
            this.lon1 = lon;




            already_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == already_member) {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.layout_xml);
                        b4 = (Button)dialog. findViewById(R.id.button4);
                        final EditText  email= (EditText)dialog.findViewById(R.id.editText);
                        final EditText  pass= (EditText)dialog. findViewById(R.id.editText2);

                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }
                        b4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!email.getText().toString().trim().equals("") && !pass.getText().toString().trim().equals("")) {


                                    JSONObject jsonObjSend = new JSONObject();
                                    try {
                                        // Add key/value pairs
                                        jsonObjSend.put("device_token", "");
                                        jsonObjSend.put("device_type", device_type);
                                        jsonObjSend.put("deviceid", deviceuid);
                                        jsonObjSend.put("login_type",login_type);
                                        jsonObjSend.put("social_id",social_id);
                                        jsonObjSend.put("lat",lat);
                                        jsonObjSend.put("lon",lon);
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



                                    try {
                                        String str_value = json_LL.getString("message");
                                        userid = json_LL.getString("userid");




                                         if (str_value.equals("User Login Successful")) {

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

                                                                 for (int i = 0; i<cast.length(); i++) {
                                                                     JSONObject actor = cast.getJSONObject(i);
                                                                     String id = actor.getString("id");
                                                                     String url = actor.getString("url");
                                                                     allid.add(id);
                                                                     allurl.add(url);
                                                                     //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();

                                                                     Log.d("Type", cast.getString(i));
                                                                 }
                                                                 for (int j = 0; j <allid.size(); j++) {
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
                                        }
                                        else
                                         {
                                             Toast.makeText(MainActivity.this, str_value, Toast.LENGTH_LONG).show();
                                         }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                else

                                {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Enter Credentials...! ", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }

                        });
                        dialog.show();




                    }

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
                                                                        jsonObjSend.put("device_token", "");
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
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


}


