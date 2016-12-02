package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 29/11/16.
 */


public class Update_Profile extends Fragment {
    ImageView shareicon,msg,logo;
    String firstname_user="",lastname_user="",gender="true",birth_day,description="",profile_url,profile_id;
    private static final int SELECT_FILE1 = 1;
    boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    private String selectedImagePath = "";
    String pick1="null";
    EditText firstname,lastname,description1;
    Spinner day,month,year;
    RadioButton male,female;
    CircularImageView profile_img;
    Button cancel,update_btn;
    RelativeLayout touch_event;
    public static final String USERID = "userid";
    public static final String DETAILS = "user_details";
    public static final String PROFILE_URL = "profile_url";
    public static final String USER_PIC = "user_pic";
    SharedPreferences user_id,profile_url_home,user_Details,user_pic ;
    SharedPreferences.Editor edit_userid,edit_profile_url_home,edit_user_detals,edit_user_pic;
    List<String> allurl;
    private static final String TAG  = "UpdateUserDetail";
    private static final String URL  = "http://52.37.136.238/JoinMe/User.svc/UpdateUserDetail";
    String uid;
    HttpEntity resEntity;
    ProgressDialog progressDialog;
    int additionalPadding = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.edit_profile,container,false);
        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);
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

        user_id = getActivity().getSharedPreferences(MainActivity.USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();

        user_Details = getActivity().getSharedPreferences(MainActivity.DETAILS, getActivity().MODE_PRIVATE);
        edit_user_detals = user_Details.edit();

        profile_url_home = getActivity().getSharedPreferences(PROFILE_URL, getActivity().MODE_PRIVATE);
        edit_profile_url_home = profile_url_home.edit();

        user_pic = getActivity().getSharedPreferences(MainActivity.USER_PIC, getActivity().MODE_PRIVATE);
        edit_user_pic = user_pic.edit();

          update_btn = (Button)v.findViewById(R.id.update);
          cancel  = (Button) v.findViewById(R.id.cancel);
          profile_img = (CircularImageView)v.findViewById(R.id.profile);
         firstname= (EditText) v.findViewById(R.id.firstname);
          lastname = (EditText)v.findViewById(R.id.lastname);
          description1 = (EditText)v.findViewById(R.id.about);
          day = (Spinner) v.findViewById(R.id.spinner1);
          month = (Spinner) v.findViewById(R.id.spinner2);
          year = (Spinner) v.findViewById(R.id.spinner3);
          male = (RadioButton)v.findViewById(R.id.male);
          female = (RadioButton)v.findViewById(R.id.female);
        touch_event = (RelativeLayout)v.findViewById(R.id.scrollView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Resources resources = getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                additionalPadding = resources.getDimensionPixelSize(resourceId);
            }
        }

        Bundle bundle = this.getArguments();
        uid = bundle.getString("userid", "0");
        firstname_user =bundle.getString("firstname", "0");
        lastname_user =bundle.getString("lastname", "0");
        birth_day = bundle.getString("dob", "0");
        description  = bundle.getString("description", "0");
        gender = bundle.getString("gender", "0");
        profile_url = bundle.getString("profile_url", "0");
        profile_id = bundle.getString("profile_id", "0");
        firstname.setText(firstname_user);
        lastname.setText(lastname_user);
        description1.setText(description);
        Picasso.with(getActivity())
                        .load("http://52.37.136.238/JoinMe/" + profile_url) // thumbnail url goes here
                        .placeholder(R.drawable.butterfly)
                        .resize(200,200)
                        .noFade()
                        .into(profile_img, new Callback() {
                            @Override
                            public void onSuccess() {                   }
                            @Override
                            public void onError() {
                            }
                        });

        if (gender.equals("male"))
        {
            male.setChecked(true);
        }else
        {
            female.setChecked(true);
        }
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Image_Choose();
            }
        });
        String[] separated = birth_day.split("/");
        String day1 =separated[0]; // this will contain "Fruit"
        String month1 =separated[1];
        String year1 =separated[2];
        Log.e("DAY", birth_day + "\n" + day1);

        List day_list = new ArrayList<Integer>();
        day_list.add(0," ");
        for (int i = 1; i <= 31; i++)
        {
            day_list.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, day_list);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        day.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.month_names));
        month.setAdapter(adapter1);

        List year_list = new ArrayList<Integer>();
        year_list.add(0,"");
        for (int i = 1910; i <= 2010; i++)
        {
            year_list.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter1 = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, year_list);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(spinnerArrayAdapter1);

        day.setSelection(Integer.parseInt(day1));
        month.setSelection(Integer.parseInt(month1));
        year.setSelection(year_list.indexOf(year1));

        touch_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                progressDialog= ProgressDialog.show(getActivity(), null,null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Intent home = new Intent(getActivity(),Screen16.class);
                home.putExtra("refresh","update_profile");
                startActivity(home);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();

            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                progressDialog= ProgressDialog.show(getActivity(), null,null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                String gender1;
                if (male.isChecked())
                {
                    gender1="male";
                }
                else
                {
                    gender1 = "female";
                }
                String ye = (String) year.getSelectedItem();
                String mon = (String) month.getSelectedItem();
                String da = (String) day.getSelectedItem();
                String birth = da + "/" + mon + "/" + ye;

                JSONObject jsonObjSend = new JSONObject();
                try {
                    // Add key/value pairs
                    jsonObjSend.put("discription", description1.getText().toString());//nari1
                    jsonObjSend.put("dob", birth); //2
                    jsonObjSend.put("f_name", firstname.getText().toString());       //3
                    jsonObjSend.put("gender", gender1);               //4
                    jsonObjSend.put("l_name", lastname.getText().toString());              //5
                    jsonObjSend.put("userid", user_id.getString("userid", "null")); //16
                    //  hideDialog();
                    Log.i(TAG, jsonObjSend.toString(6));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObjRecv = com.brahmasys.bts.joinme.HttpClient.SendHttpPost(URL, jsonObjSend);


                JSONObject json = null;
                try {
                    json = new JSONObject(String.valueOf(jsonObjRecv));
                    JSONObject resp = new JSONObject(json.getString("response"));
                     final String message = resp.getString("message");
                    if (message.equals("Updated Successfully")) {

                       Thread thread = new Thread(new Runnable() {
                            public void run() {
                                doFileUpload();
                                GetUserData();
                                edit_profile_url_home.putString("profile_url", selectedImagePath);
                                edit_profile_url_home.commit();
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {




                                            progressDialog.dismiss();
                                            Intent home = new Intent(getActivity(),Screen16.class);
                                            startActivity(home);
                                            getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                                            getActivity().finish();
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                            }
                        });
                        thread.start();


                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {




        if (isKitKat && resultCode != Activity.RESULT_CANCELED) {

            String uri = new ImagePath().getPath(getContext(), data.getData());


            if (requestCode == SELECT_FILE1) {
                pick1 = "1";
                selectedImagePath = uri;
                profile_img.setImageBitmap(new DecodeImage().decodeFile(selectedImagePath));

            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }


        }
    }

    public void Image_Choose()
    {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE1);
    }

    private void doFileUpload() {

        File file1 = new File(selectedImagePath);

        String urlString = "http://52.37.136.238/JoinMe/User.svc/UpdateUserPic/" + uid+"/"+profile_id;
        try {
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
                Log.i("RESPONSE", response_str);
                if (getActivity() == null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {

//                            progressDialog.dismiss();
//
//                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return;
                }

            }
        } catch (Exception ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }

    }
    public void GetUserData()
    {
        if (Connectivity_Checking.isConnectingToInternet()) {
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
//                                    firstname_user = userdetails.getString("fname");
//                                    lastname_user = userdetails.getString("lname");
//                                    birth_day = userdetails.getString("dob");
//                                    description =userdetails.getString("about");
//                                    gender = userdetails.getString("gender");
                                    //Save the data in sharedPreference
                                    edit_user_detals.putString("firstname", userdetails.getString("fname"));
                                    edit_user_detals.putString("lastname", userdetails.getString("lname"));
                                    edit_user_detals.putString("dob", userdetails.getString("dob"));
                                    edit_user_detals.putString("about", userdetails.getString("about"));
                                    edit_user_detals.putString("gender", userdetails.getString("gender"));
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

                                //  Toast.makeText(Login_Activity.this, String.valueOf(cast.length()), Toast.LENGTH_SHORT).show();
//                                List<String> allid = new ArrayList<String>();
//                                List<String> allurl = new ArrayList<String>();
//
//                                for (int i = 0; i < cast.length(); i++) {
//                                    JSONObject actor = cast.getJSONObject(i);
//                                    String id = actor.getString("id");
//                                    String url = actor.getString("url");
//                                    allid.add(id);
//                                    allurl.add(url);
//                                    //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();
//
//                                    Log.d("Type", cast.getString(i));
//                                }
//                                for (int j = 0; j < allid.size(); j++) {
//                                    edit_user_pic.putString("id_" + j, allid.get(j));
//                                    edit_user_pic.putString("url_" + j, allurl.get(j));
//
//                                }
//                                edit_user_pic.commit();



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());

        }
    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
