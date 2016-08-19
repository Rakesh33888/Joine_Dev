package com.example.bts.joinme;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button facebook, mail,b4,b5;
    TextView tv1, tv2, already_member;
     SessionManager session;
    private static final String TAG = "SignUp";
    private static final String URL = "http://52.37.136.238/JoinMe/User.svc/SignUp";
    String deviceuid,device_type="android";
    public static final String USERID = "userid";
    SharedPreferences user_id;
    SharedPreferences.Editor edit_userid;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebook = (Button) findViewById(R.id.facebook);
        mail = (Button) findViewById(R.id.mail);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        session = new SessionManager(getApplicationContext());

        user_id =getSharedPreferences(USERID,MODE_PRIVATE);
        edit_userid = user_id.edit();

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


                   b4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!email.getText().toString().trim().equals("") && !pass.getText().toString().trim().equals("")){

                                Intent i = new Intent(getApplicationContext(), Screen16.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                finish();
                                session.setLogin(true);
                                Toast toast = Toast.makeText(getApplicationContext(),"Login Succesfull", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                dialog.dismiss();

                                } else {
                                Toast toast = Toast.makeText(getApplicationContext(),"Invalid connection", Toast.LENGTH_SHORT);
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
                                    if (pass.getText().toString().trim().length()>=4) {




                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.get("http://52.37.136.238/JoinMe/User.svc/CheckUserEmailAvailability/" +email.getText().toString(),
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
                                                                    jsonObjSend.put("device_token","xyz");
                                                                    jsonObjSend.put("device_type",device_type);
                                                                    jsonObjSend.put("deviceid",deviceuid);
                                                                    jsonObjSend.put("email",email.getText().toString());
                                                                    jsonObjSend.put("password",pass.getText().toString());
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
                                                                userid  = json_LL.getString("userid");

                                                                   Toast.makeText(MainActivity.this, str_value, Toast.LENGTH_LONG).show();

                                                                  if (str_value.equals("Registered Successfully")) {


                                                                      edit_userid.putString("PetId",userid);
                                                                      edit_userid.commit();
                                                                        Intent i1 = new Intent(getApplicationContext(), Screen3a.class);
                                                                        startActivity(i1);
                                                                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                                                        finish();
                                                                       // Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                                                                        dialog.dismiss();

                                                                    }
                                                                }catch (JSONException e)
                                                                {
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



                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "Password must be at least 4 characters. ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Invalid Mail Id.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(),"Invalid connection", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }

                    });
                    dialog.show();

                }}
        });



    }

//    private boolean isValidPassword(String pass) {
//        if (pass != null && pass.length() > 4) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isValidEmail(String email) {
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//
//        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
    //}
}


