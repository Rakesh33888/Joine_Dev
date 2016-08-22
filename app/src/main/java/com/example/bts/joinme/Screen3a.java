package com.example.bts.joinme;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class Screen3a extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imageView;
    Button continue_btn;
    CircularImageView circle_image;
    EditText firstname, lastname, conformation_code,share;
    android.support.v7.app.ActionBar actionBar;
    SegmentedGroup  rgroup;
    RadioButton male,female;
     TextView textView5;
    Spinner day,month,year;
    String gender;
    int select_image;
    String deviceuid,device_type="android",registration_type="normal",device_token="";
    SharedPreferences user_id;
    SharedPreferences.Editor edit_userid;

    private static final int SELECT_FILE1 = 1;
    String selectedPath1 = "NONE";
    HttpEntity resEntity;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager locmgr;
    Double lat  ;
    Double lon  ;
    double lat1,lon1;
    private static final String TAG = "UserRegister";
    private static final String URL = "http://52.37.136.238/JoinMe/User.svc/UserRegister";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3aa);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        deviceuid = Settings.Secure.getString(Screen3a.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        user_id =getSharedPreferences("userid",MODE_PRIVATE);
        edit_userid = user_id.edit();

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        conformation_code = (EditText) findViewById(R.id.conformation_code);
        share = (EditText) findViewById(R.id.share);

        rgroup = (SegmentedGroup) findViewById(R.id.radioGroup);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        continue_btn = (Button) findViewById(R.id.continue_btn);
        circle_image = (CircularImageView) findViewById(R.id.imageView0);
        male.setChecked(true);

        day = (Spinner) findViewById(R.id.spinner1);
        month = (Spinner) findViewById(R.id.spinner2);
        year = (Spinner) findViewById(R.id.spinner3);


        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();

        onConnected(savedInstanceState);
        onConnected(new Bundle());



        List day_list = new ArrayList<Integer>();
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
        for (int i = 1910; i <= 2010; i++)
        {
            year_list.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter1 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, year_list);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(spinnerArrayAdapter1);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | actionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView4 = new ImageView(actionBar.getThemedContext());
        imageView4.setScaleType(ImageView.ScaleType.CENTER);
        imageView4.setImageResource(R.drawable.logoaction);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL
                | Gravity.CENTER);
        layoutParams.rightMargin = 40;
        imageView4.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView4);
        circle_image.setClickable(true);
        circle_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(SELECT_FILE1);

            }
        });



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

            continue_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (select_image == 1) {
                        if (firstname.getText().toString().length() >= 2) {

                            if (male.isChecked()) {
                                gender = "male";
                            } else {
                                gender = "female";
                            }
                            String ye = (String) year.getSelectedItem();
                            String mon = (String) month.getSelectedItem();
                            String da = (String) day.getSelectedItem();
                            String birth = da + "/" + mon + "/" + ye;


                            JSONObject jsonObjSend = new JSONObject();
                            try {
                                // Add key/value pairs
                                jsonObjSend.put("confermationcode",conformation_code.getText().toString());
                                jsonObjSend.put("deviceid", deviceuid);
                                jsonObjSend.put("device_token", device_token);
                                jsonObjSend.put("device_type", device_type);
                                jsonObjSend.put("discription",share.getText().toString());
                                jsonObjSend.put("dob",birth);
                                jsonObjSend.put("fname",firstname.getText().toString());
                                jsonObjSend.put("gender",gender);
                                jsonObjSend.put("lat",lat);
                                jsonObjSend.put("lname",lastname.getText().toString());
                                jsonObjSend.put("lon",lon);
                                jsonObjSend.put("registration_type",registration_type);
                                jsonObjSend.put("userid",user_id.getString("userid","null"));
                                //  hideDialog();
                                Log.i(TAG, jsonObjSend.toString(13));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSONObject jsonObjRecv = com.example.bts.joinme.HttpClient.SendHttpPost(URL, jsonObjSend);


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




                                if (str_value.equals("Updated Successfully")) {



                                    Intent i1 = new Intent(getApplicationContext(), Screen16.class);
                                    startActivity(i1);
                                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                    finish();



                                }
                                else
                                {
                                    Toast.makeText(Screen3a.this, str_value, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }






                        } else {
                            Toast toast = Toast.makeText(Screen3a.this, "Firstname must be having at least 2 letters", Toast.LENGTH_SHORT);
                            View view = toast.getView();
                            view.setBackgroundResource(R.drawable.smallbox1);
                            TextView col = (TextView) toast.getView().findViewById(android.R.id.message);
                            col.setTextColor(Color.RED);
                            toast.show();
                        }
                    }else
                    {
                        Toast toast = Toast.makeText(Screen3a.this, "Please choose a photo", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.setBackgroundResource(R.drawable.smallbox1);
                        TextView col = (TextView) toast.getView().findViewById(android.R.id.message);
                        col.setTextColor(Color.RED);
                        toast.show();
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

    public void openGallery(int req_code){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (requestCode == SELECT_FILE1)
            {

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    circle_image.setImageBitmap(bitmap);
                    select_image =1;
                } catch (IOException e) {

                }

                selectedPath1 = getPath(selectedImageUri);
                System.out.println("selectedPath1 : " + selectedPath1);
            }

            Thread thread=new Thread(new Runnable(){
                public void run(){
                    doFileUpload();
                    runOnUiThread(new Runnable(){
                        public void run() {

                        }
                    });
                }
            });
            thread.start();

        }
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

    public boolean onKeyDown(int Keycode, KeyEvent event) {
        if (Keycode == KeyEvent.KEYCODE_BACK) {

            Intent i = new Intent(Screen3a.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            finish();


        } return super.onKeyDown(Keycode,event);
    }


}