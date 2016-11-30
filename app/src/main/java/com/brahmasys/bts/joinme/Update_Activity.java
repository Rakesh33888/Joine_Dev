package com.brahmasys.bts.joinme;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import com.squareup.picasso.Transformation;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
public class Update_Activity extends Fragment  {
    TextView dateTextView;
    ImageView dateButton;
    RelativeLayout search_layout,touch_event;
    CircularImageView firstimage, secondimage, thirdimage;
    EditText edittextactivityname, enterdiscription, edit_cost, edit_limit,current_address;
    Button update_activity, delet_activity;
    Spinner spinnericon,spinnerforhour, currency_symbol;
    LinearLayout not_everyone;
    CheckBox checkboxcurrent, checkBoxaddress, checkBoxforeveryone, checkBoxnotforeveryone, checkBoxformen, checkBoxforwomen;
    CrystalRangeSeekbar seekBarforage;
    private ContentResolver contentResolver;
    private DelayAutoCompleteTextView geo_autocomplete;
    TextView age1, age2,text_search_address;
    ProgressDialog progressDialog;
    String[] currency = new String[]{"€", "$"};
    String year = "0", month = "0", day = "0", hour = "0", minute;
    String availability;
    ImageView shareicon,msg,logo;
    ArrayList<String> data;
    long unixTime=0;
    int image;
    String gender = "",Icon_url,start_age="0",end_age="100";
    String duration = "0", icon = "0", title, address, age_start, age_end, cost = "0", limit = "0", description;
    double latitude = 0.0, longitude = 0.0, latitude1, longitude1, latitude2, longitude2;
    String complete_address, city, state, zip, country, total_address;
    private static final String TAG = "UpdateActivityDetails";
    private static final String URL = "http://52.37.136.238/JoinMe/Activity.svc/UpdateActivityDetails";
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    private static final String LAT_LNG = "lat_lng";
    private static final String SEARCH_LOCATION="search_location";
    SharedPreferences user_id, activity_id, lat_lng,search_lat_lng;
    SharedPreferences.Editor edit_userid, edit_activity_id, edit_lat_lng,edit_search_lat_lng;
    private Integer THRESHOLD = 2;
    private ImageView geo_autocomplete_clear;
    private String selectedImagePath = "", selectedImagePath2 = "", selectedImagePath3 = "";
    public static final int PICK_IMAGE1 = 1;
    public static final int PICK_IMAGE2 = 2;
    public static final int PICK_IMAGE3 = 3;
    String pick1="null",pick2="null",pick3="null";
    String owerid;
    HttpEntity resEntity;
    private ArrayList<Book> books;
    private ArrayAdapter<Book> adapter;
    private ArrayAdapter<Integer> adapter1;
    ArrayAdapter<Integer> spinnerArrayAdapter4;
    Context context;
    List<String> allurl;
    FragmentManager fragmentManager;
    boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    Bundle bundle;
    int Pic_length=0;
    List<String> allid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.update_activity, container, false);
      //  GettingActivityDetails();
        user_id = getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();
        lat_lng = getActivity().getSharedPreferences(LAT_LNG, getActivity().MODE_PRIVATE);
        edit_lat_lng = lat_lng.edit();
        search_lat_lng = getActivity().getSharedPreferences(SEARCH_LOCATION, getActivity().MODE_PRIVATE);
        edit_search_lat_lng = search_lat_lng.edit();

        latitude2 = Double.parseDouble(lat_lng.getString("lat", "0.0"));
        longitude2 = Double.parseDouble(lat_lng.getString("lng", "0.0"));
        search_layout  = (RelativeLayout) v.findViewById(R.id.search_layout);
        current_address = (EditText)v.findViewById(R.id.current_address);
        spinnericon = (Spinner) v.findViewById(R.id.spinnericon);
        dateTextView = (TextView)v.findViewById(R.id.date_textview);
        dateButton = (ImageView)v.findViewById(R.id.date_button);
        spinnerforhour = (Spinner) v.findViewById(R.id.spinner_hour);
        currency_symbol = (Spinner) v.findViewById(R.id.currency_symbol);
        not_everyone = (LinearLayout) v.findViewById(R.id.not_everyone);
        text_search_address = (TextView)v.findViewById(R.id.search_address);
        enterdiscription = (EditText) v.findViewById(R.id.enter_discription);
        edittextactivityname = (EditText) v.findViewById(R.id.edittextactivityname);
        edit_cost = (EditText) v.findViewById(R.id.forcost);
        edit_limit = (EditText) v.findViewById(R.id.numbrlimitbtn);
        checkboxcurrent = (CheckBox) v.findViewById(R.id.checkBoxfor_current);
        checkBoxaddress = (CheckBox) v.findViewById(R.id.checkboxfor_address);
        checkBoxforeveryone = (CheckBox) v.findViewById(R.id.checkBoxfor_everyone);
        checkBoxnotforeveryone = (CheckBox) v.findViewById(R.id.checkBoxfor_noteveryone);
        checkBoxformen = (CheckBox) v.findViewById(R.id.checkBoxformen);
        checkBoxforwomen = (CheckBox) v.findViewById(R.id.checkBoxforwomen);
        Toolbar refTool = ((Screen16) getActivity()).toolbar;
        shareicon = (ImageView) refTool.findViewById(R.id.shareicon);
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
        seekBarforage = (CrystalRangeSeekbar) v.findViewById(R.id.rangeSeekbar);
        firstimage = (CircularImageView) v.findViewById(R.id.firstimage);
        secondimage = (CircularImageView) v.findViewById(R.id.secondimage);
        thirdimage = (CircularImageView) v.findViewById(R.id.thrdimage);
        age1 = (TextView) v.findViewById(R.id.age1);
        age2 = (TextView) v.findViewById(R.id.age2);
        update_activity = (Button) v.findViewById(R.id.update_activity);
        delet_activity = (Button) v.findViewById(R.id.delete_activity);
        enterdiscription = (EditText) v.findViewById(R.id.enter_discription);
        touch_event    = (RelativeLayout)v.findViewById(R.id.touch_outside);
        allurl = new ArrayList<String>();

        touch_event.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });
        seekBarforage.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                age1.setText(String.valueOf(minValue));
                age2.setText(String.valueOf(maxValue));
            }
        });

        /*************************** Spinner Functionality Start ***********************/
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.times_format));
//        spinnerforhour.setAdapter(adapter1);

//        List hours = new ArrayList<Integer>();
//        hours.add(0, "");
//        for (int i = 0; i<=23; i++) {
//            hours.add(Integer.toString(i));
//
//        }
//        spinnerArrayAdapter4 = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, hours);
//        spinnerArrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerArrayAdapter4.notifyDataSetChanged();
//        spinnerforhour.setAdapter(spinnerArrayAdapter4);



        ArrayAdapter<String> adapter_currency = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, currency);
        currency_symbol.setAdapter(adapter_currency);
        /**************************Spinner functionality Ends *****************************/

        String userProfileString=getArguments().getString("accountDetails");
        JSONObject jsonObject = null;
        try {
            jsonObject=new JSONObject(userProfileString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject userdetails = null;
        try {
            userdetails = jsonObject.getJSONObject("act_details");
            JSONArray cast = userdetails.getJSONArray("activity_pics");
            allid = new ArrayList<String>();
            Log.e("LENGTH",String.valueOf(cast.length()));
            for (int i = 0; i < cast.length(); i++) {
                JSONObject actor = cast.getJSONObject(i);
                String id = actor.getString("id");
                final String url = actor.getString("url");
               allid.add(id);
                Pic_length = cast.length();
                if (i==cast.length()-1)
                {
//                    Picasso.with(getContext()).load("http://52.37.136.238/JoinMe/" + url).resize(75, 75)
//                        .centerCrop().into(firstimage);


                    Picasso.with(context)
                            .load("http://52.37.136.238/JoinMe/" + url) // thumbnail url goes here
                            .placeholder(R.drawable.butterfly)
                            .resize(75, 75)
                            .into(firstimage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                }
                            });
                }
                if(i==cast.length()-2)
                {
//                    Picasso.with(getContext()).load("http://52.37.136.238/JoinMe/" + url).resize(75, 75)
//                            .centerCrop().into(secondimage);

                    Picasso.with(context)
                            .load("http://52.37.136.238/JoinMe/" + url) // thumbnail url goes here
                            .placeholder(R.drawable.butterfly)
                            .resize(75, 75)

                            .into(secondimage, new Callback() {
                                @Override
                                public void onSuccess() {


                                }

                                @Override
                                public void onError() {
                                }
                            });
                }
                if (i==cast.length()-3)
                {
//                    Picasso.with(getContext()).load("http://52.37.136.238/JoinMe/" + url).resize(75, 75)
//                            .centerCrop().into(thirdimage);
                    Picasso.with(context)
                            .load("http://52.37.136.238/JoinMe/" + url) // thumbnail url goes here
                            .placeholder(R.drawable.butterfly)
                            .resize(75, 75)

                            .into(thirdimage, new Callback() {
                                @Override
                                public void onSuccess() {


                                }

                                @Override
                                public void onError() {
                                }
                            });
                }
                Log.e("Activity Url", cast.getString(i));
            }
            Log.w("ACTIVITY DETAILS", String.valueOf(userdetails));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Log.e("Activity Name", userdetails.getString("activity_name"));

            edittextactivityname.setText(userdetails.getString("activity_name"));
            String cost = userdetails.getString("activity_cost");
            String number = "";
            String symbol = "";
            for (int i = 0; i < cost.length(); i++)
            {
                char a = cost.charAt(i);
                if (Character.isDigit(a))
                {
                    number = number + a;
                    continue;
                }
                else
                {
                    symbol = symbol + a;
                }
            }

                edit_cost.setText(number);

            if (symbol.equals("$"))
            {
                currency_symbol.setSelection(1);
            }
            else
            {
                currency_symbol.setSelection(0);
            }



            enterdiscription.setText(userdetails.getString("activity_Description"));
            if (!userdetails.getString("participant_limit").equals("0")) {
                edit_limit.setText(userdetails.getString("participant_limit"));
            }
            current_address.setText(userdetails.getString("activity_Adress"));
            long unixSeconds = Long.parseLong(userdetails.getString("activity_time"));
            Date date2 = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // the format of your date
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
            sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
            sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // give a timezone reference for formating (see comment at the bottom
            String formattedDate = sdf.format(date2);
            String formattedDate1 = sdf1.format(date2);
            dateTextView.setText(formattedDate);


            Calendar calander = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            String time = simpleDateFormat.format(calander.getTime());

            String current_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

            List hours =  new ArrayList<String>();;
            if (formattedDate.equals(current_date))
            {

                Log.e("Current time", time);

                hours.add(0, "");
                for (int i =0; i <=23; i++) {
                    if (i>Integer.parseInt(time))
                    {
                        hours.add(i + ":00");
                    }
                }
            }
            else {
                hours.add(0, "");
                for (int i =0; i <=23; i++) {
                    hours.add(i + ":00");

                }
            }
            ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hours);
            spinnerArrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerArrayAdapter4.notifyDataSetChanged();
            spinnerforhour.setAdapter(spinnerArrayAdapter4);

            if (formattedDate.equals(current_date))
            {
                spinnerforhour.setSelection(Integer.parseInt(formattedDate1)-Integer.parseInt(time));

            }
            else {
                spinnerforhour.setSelection(Integer.parseInt(formattedDate1)+1);
            }


            Icon_url = userdetails.getString("acitivity_icon");


            if (userdetails.getString("activity_availability").equals("public"))
            {
                checkBoxnotforeveryone.setChecked(false);
                checkBoxforeveryone.setChecked(true);
                not_everyone.setVisibility(View.GONE);
                checkBoxforeveryone.setClickable(false);
                checkBoxnotforeveryone.setClickable(true);
                checkBoxforeveryone.setEnabled(false);
            }
            else {
                checkBoxnotforeveryone.setChecked(true);
                not_everyone.setVisibility(View.VISIBLE);
                checkBoxforeveryone.setClickable(true);
                checkBoxnotforeveryone.setClickable(false);
                checkBoxnotforeveryone.setEnabled(false);
//                Log.e("AGES", start_age + "\n" + End_age);
                age1.setText(userdetails.getString("participant_age_start"));
                age2.setText(userdetails.getString("participant_age_end"));



//                seekBarforage.setMinValue(Integer.parseInt(userdetails.getString("participant_age_start")));
//                seekBarforage.setMaxValue(Integer.parseInt(userdetails.getString("participant_age_end")));





                checkBoxforeveryone.setChecked(false);
                 if (userdetails.getString("participant_gender").equals("male"))
                 {
                    checkBoxformen.setChecked(true);
//                     checkBoxforwomen.setChecked(false);
//                     checkBoxforwomen.setClickable(true);
//                     checkBoxformen.setClickable(false);

                 }
                 else if(userdetails.getString("participant_gender").equals("both"))
                 {
                     checkBoxformen.setChecked(true);
                     checkBoxforwomen.setChecked(true);
                 }
                else
                 {
                    checkBoxforwomen.setChecked(true);
//                     checkBoxformen.setChecked(false);
//                     checkBoxforwomen.setClickable(false);
//                     checkBoxformen.setClickable(true);
                 }
                 }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // user details which user wants to update//

        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address_search_Dialog();
            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = DatePickerDialog.newInstance(new DateListener(), now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                Calendar cal = Calendar.getInstance();
                cal.get(Calendar.YEAR);
                cal.get(Calendar.MONTH);
                cal.get(Calendar.DAY_OF_MONTH);
                dpd.setMinDate(cal);
                dpd.setAccentColor(Color.parseColor("#9C27B0"));
                dpd.setTitle("Date Picker");
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.37.136.238/JoinMe/Activity.svc/GetActivityIconList/" + user_id.getString("userid", "null"),
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //  prgDialog.hide();
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);

                            JSONArray cast = obj.getJSONArray("data");

                            //  Toast.makeText(Login_Activity.this, String.valueOf(cast.length()), Toast.LENGTH_SHORT).show();
                            List<String> allid = new ArrayList<String>();


                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String id = actor.getString("icon_id");
                                String url = actor.getString("icon_url");
                                allid.add(id);
                                allurl.add(url);
                                Book book = new Book();
                                book.setImageUrl(actor.getString("icon_url"));
                                books.add(book);
                                //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();

                                }
                            adapter.notifyDataSetChanged();

                            for (int i = 0; i < allurl.size(); i++) {
                               if (Icon_url.equals(allurl.get(i))) {
                                    icon = allurl.get(i);
                                    spinnericon.setSelection(allurl.indexOf(allurl.get(i)));

                                }
                               else {
                                   icon="/ActivityIcons/coffee.png";
                               }

                            }
                            Log.d("Type", String.valueOf(allurl));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        setListViewAdapter();


        spinnericon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                for (int i = 0; i < allurl.size(); i++) {
                    if (i == spinnericon.getSelectedItemPosition()) {

                        icon = allurl.get(i);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }


        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            latitude = latitude;
            longitude = longitude;
        } else {
            latitude = latitude2;
            longitude = longitude2;
        }
        /******************** CheckBox Functionality Start*******************/
        checkboxcurrent.setChecked(true);
        checkboxcurrent.setEnabled(false);
        checkboxcurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkboxcurrent) {
                    checkBoxaddress.setChecked(false);
                    //address_search.setVisibility(View.GONE);
                    current_address.setVisibility(View.VISIBLE);
                    search_layout.setVisibility(View.GONE);
                    checkboxcurrent.setClickable(false);
                    checkBoxaddress.setClickable(true);
                }

            }
        });

        checkBoxaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxaddress) {
                    checkboxcurrent.setChecked(false);
                    search_layout.setVisibility(View.VISIBLE);
                    current_address.setVisibility(View.GONE);
                    checkboxcurrent.setClickable(true);
                    checkBoxaddress.setClickable(false);
                    checkboxcurrent.setEnabled(true);
                }

            }
        });
   //     checkBoxforeveryone.setChecked(true);
        checkBoxforeveryone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxforeveryone) {
                    checkBoxnotforeveryone.setChecked(false);
                    not_everyone.setVisibility(View.GONE);
                    checkBoxforeveryone.setClickable(false);
                    checkBoxnotforeveryone.setClickable(true);
                    checkBoxnotforeveryone.setEnabled(true);
                }

            }
        });

        checkBoxnotforeveryone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxnotforeveryone) {
                    checkBoxforeveryone.setChecked(false);
                    not_everyone.setVisibility(View.VISIBLE);
                    checkBoxforeveryone.setClickable(true);
                    checkBoxnotforeveryone.setClickable(false);
                    checkBoxforeveryone.setEnabled(true);
                }

            }
        });

    //    checkBoxforwomen.setChecked(true);
        checkBoxforwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxforwomen) {
                 //   checkBoxforwomen.setChecked(true);
//                    checkBoxforwomen.setClickable(false);
//                    checkBoxformen.setClickable(true);

                }

            }
        });
        checkBoxformen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxformen) {
                //    checkBoxformen.setChecked(true);
//                    checkBoxforwomen.setClickable(true);
//                    checkBoxformen.setClickable(false);

                }

            }
        });
        /******************** CheckBox Functionality End*******************/



        firstimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE1);
            }
        });
        secondimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE2);

            }
        });
        thirdimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE3);

            }
        });


        /**************** Data & Time Validation **********************/
        Calendar c11 = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c11.getTime());

        final int current_year = Integer.parseInt(formattedDate.substring(0, 4));
        final int current_month = Integer.parseInt(formattedDate.substring(5, 7));
        final int current_day = Integer.parseInt(formattedDate.substring(8, 10));
        final int current_hour = Integer.parseInt(formattedDate.substring(11, 13));



        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT));


        update_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        // do the thing that takes a long time
                        try {


                            if(checkboxcurrent.isChecked()) {
                                UpdateActivity();
                            }
                            if (checkBoxaddress.isChecked()) {
                                if (!text_search_address.getText().toString().equals("Search Address")) {
                                    UpdateActivity();
                                }else
                                {
                                    Toast.makeText(getActivity(), "Please fill all the details...!", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                            }
                        });
                        Looper.loop();
                    }
                }).start();

            }
        });

        delet_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.setContentView(R.layout.custom_delete_msg);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button yes = (Button) dialog.findViewById(R.id.yes);
                Button no = (Button) dialog.findViewById(R.id.no);
                yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       // new Custom_Progress(getActivity());
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get("http://52.37.136.238/JoinMe/Activity.svc/DeleteActivity/" + activity_id.getString("activity_id", ""),
                                new AsyncHttpResponseHandler() {
                                    // When the response returned by REST has Http response code '200'

                                    public void onSuccess(String response) {
                                        // Hide Progress Dialog
                                        //  prgDialog.hide();
                                        try {
                                            // Extract JSON Object from JSON returned by REST WS
                                            JSONObject obj = new JSONObject(response);
                                            String del_msg = obj.getString("message");
                                            if (del_msg.equals("Deleted Successfully")) {
                                                edit_activity_id.clear().commit();
                                                fragmentManager = getFragmentManager();
                                                Mygroup mygroup = new Mygroup();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.flContent, mygroup)
                                                        .addToBackStack(null)
                                                        .commit();
                                                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                                              //  new Custom_Progress(getActivity()).dismiss();
                                                Toast.makeText(getActivity(), del_msg, Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(getActivity(), del_msg, Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                      dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });



        return v;

    }

    public void UpdateActivity() throws ParseException {
        if (Connectivity_Checking.isConnectingToInternet()) {
            Log.e("Position", String.valueOf(spinnerforhour.getSelectedItemPosition()));
            if (!edittextactivityname.getText().toString().equals("")&& !enterdiscription.getText().toString().equals("") && !dateTextView.getText().toString().equals("Select date")
                     && spinnerforhour.getSelectedItemPosition()-1>=0  ) {
              //  if (!edit_limit.getText().toString().equals("0")) {
        if (edittextactivityname.getText().toString().length() >= 2) {
            if (enterdiscription.getText().toString().length() >= 10) {
//
                progressDialog = ProgressDialog.show(getActivity(), null, null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

              //  hour = String.valueOf(spinnerforhour.getSelectedItemPosition()-1);

                String current_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

                Calendar calander = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
                String time = simpleDateFormat.format(calander.getTime());

                if (dateTextView.getText().toString().equals(current_date))
                {
                    hour = String.valueOf(spinnerforhour.getSelectedItemPosition()+Integer.parseInt(time));

                }
                else {
                    hour = String.valueOf(spinnerforhour.getSelectedItemPosition()-1);

                }

                /*************** Time Stamp Start********************/
                String dateString = dateTextView.getText().toString()+" "+hour+":00"+":00"+" "+"GMT";
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss z");
                Date date1 = dateFormat.parse(dateString );
                unixTime = (long) date1.getTime()/1000;
                Log.e("Timestamp527", String.valueOf(unixTime));

                /********************* Time Stamp End ***************/
                /********************* TimeZone Start ***************/
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                        Locale.getDefault());
                Date currentLocalTime = calendar.getTime();
                DateFormat date = new SimpleDateFormat("Z");
                String localTime = date.format(currentLocalTime);

                String sign = localTime.substring(0, 1);
                String hr = localTime.substring(1, 3);
                String min = localTime.substring(3, 5);

                int res = (Integer.parseInt(hr) * 60) + Integer.parseInt(min);
                if (sign.equals("+")) {
                    res = -res;
                } else {
                    res = +res;
                }
                /********************* TimeZone End ***************/

                if (checkBoxforeveryone.isChecked()) {
                    availability = "public";
                    gender = "";

                } else {
                    availability = "private";

                    if (checkBoxformen.isChecked() && checkBoxforwomen.isChecked())
                    {
                        gender = "both";
                    }
                    else if (checkBoxforwomen.isChecked()) {
                        gender = "female";
                    }
                    else
                    {
                        gender = "male";
                    }
                }
                if (checkboxcurrent.isChecked()) {

                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    complete_address = addresses.get(0).getAddressLine(0);
//                    city = addresses.get(0).getLocality();
//                    state = addresses.get(0).getAdminArea();
//                    zip = addresses.get(0).getPostalCode();
//                    country = addresses.get(0).getCountryName();
                    total_address = current_address.getText().toString();
                } else {

                    if (!text_search_address.getText().toString().equals("Search Address"))
                    {
                        latitude = Double.parseDouble(search_lat_lng.getString("search_lat","0.0"));
                        longitude =Double.parseDouble(search_lat_lng.getString("search_lng","0.0"));
                        total_address = geo_autocomplete.getText().toString();
                    }else
                    {
                        total_address = "address";
                    }

                }

                title = edittextactivityname.getText().toString();
                description = enterdiscription.getText().toString();
                if (!edit_cost.getText().toString().equals("") && !edit_limit.getText().toString().equals("")) {
                    cost = edit_cost.getText().toString();
                    limit = edit_limit.getText().toString();
                }
                else
                {
                    cost="0";
                    limit="0";
                }
                age_start = age1.getText().toString();
                age_end = age2.getText().toString();

                Log.w("Complete Data:", availability + "\n" + description + "\n" + duration + "\n" + 0 + "\n" + res + "\n" + unixTime + "\n" + title + "\n" + total_address + "\n" + latitude
                        + "\n" + longitude + "\n" + age_start + "\n" + age_end + "\n" + cost + "\n" + limit + "\n" + gender);


                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                JSONObject jsonObjSend = new JSONObject();
                try {
                    // Add key/value pairs
                    jsonObjSend.put("activity_availability", availability);//nari1
                    jsonObjSend.put("activity_description", description); //2
                    jsonObjSend.put("activity_duration", duration);       //3
                    jsonObjSend.put("activity_icon", icon);               //4
                    jsonObjSend.put("activity_time", unixTime);              //5
                    jsonObjSend.put("activityid", activity_id.getString("activity_id", "00000"));                 //6
                    jsonObjSend.put("activity_title", title);              //7
                    jsonObjSend.put("address", total_address);             //8
                    jsonObjSend.put("lat", latitude);                      //9
                    jsonObjSend.put("lon", longitude);                     //10
                    jsonObjSend.put("participant_age_end", age_end);       //11
                    jsonObjSend.put("participant_age_start", age_start);   //12
                    jsonObjSend.put("participant_cost", cost + currency_symbol.getSelectedItem().toString());             //13
                    jsonObjSend.put("participant_gender", gender);         //14
                    jsonObjSend.put("participant_limit", limit);           //15
                    jsonObjSend.put("userid", user_id.getString("userid", "null")); //16
                    //  hideDialog();
                    Log.i(TAG, jsonObjSend.toString(16));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                JSONObject jsonObjRecv = com.brahmasys.bts.joinme.HttpClient.SendHttpPost(URL, jsonObjSend);


                JSONObject json = null;
                try {
                    json = new JSONObject(String.valueOf(jsonObjRecv));
                    String message = json.getString("message");
                    if (message.equals("Updated Successfully")) {

                        fragmentManager = getFragmentManager();
                        if (pick1.equals("1") || pick2.equals("2") ||pick3.equals("3")) {
                            doFileUpload();
                        }
                        Mygroup mygroup = new Mygroup();
                        fragmentManager.beginTransaction()
                                .replace(R.id.flContent, mygroup)
                                .addToBackStack(null)
                                .commit();

                      Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            } else {
                Toast.makeText(getActivity(), "Description at least you have to enter 10 characters!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Activity name at least you have to enter 2 characters!", Toast.LENGTH_LONG).show();
        }
//                }
//                else
//                {
//                    Toast.makeText(getActivity(), "Participants limit should be grater than zero...!", Toast.LENGTH_LONG).show();
//                }
            }
            else
            {
                Toast.makeText(getActivity(), "Please fill all the details...!", Toast.LENGTH_LONG).show();
            }


        } else {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());
            //progressDialog.dismiss();
        }
    }

    public  void GettingActivityDetails()
    {

    }


    private void setListViewAdapter() {
        books = new ArrayList<Book>();
        adapter = new IconAdapter(getActivity(), R.layout.icon_image, books);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnericon.setAdapter(adapter);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (isKitKat && resultCode != Activity.RESULT_CANCELED) {

            String uri = new ImagePath().getPath(getContext(), data.getData());


            if (requestCode == PICK_IMAGE1) {
                pick1 = "1";
                selectedImagePath = uri;
                firstimage.setImageBitmap(new DecodeImage().decodeFile(selectedImagePath));

            }
            if (requestCode == PICK_IMAGE2) {
                pick2 = "2";
                selectedImagePath2 = uri;
                secondimage.setImageBitmap(new DecodeImage().decodeFile(selectedImagePath2));
            }
            if (requestCode == PICK_IMAGE3) {
                pick3 = "3";
                selectedImagePath3 = uri;
                thirdimage.setImageBitmap(new DecodeImage().decodeFile(selectedImagePath3));
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }


        }
    }

    public void Address_search_Dialog() {

        final Dialog emailDialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        emailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        emailDialog.setCancelable(true);
        emailDialog.setContentView(R.layout.address_search_dialog);

        Button btnAccept = (Button)emailDialog.findViewById(R.id.done);
        Button cancel  = (Button) emailDialog.findViewById(R.id.cancel);
        ImageView   logo = (ImageView) emailDialog.findViewById(R.id.imageView5);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog= ProgressDialog.show(getActivity(), null,null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Intent Home = new Intent(getActivity(),Screen16.class);
                startActivity(Home);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                getActivity().finish();
            }
        });
        geo_autocomplete_clear = (ImageView) emailDialog.findViewById(R.id.geo_autocomplete_clear);

        geo_autocomplete = (DelayAutoCompleteTextView) emailDialog.findViewById(R.id.geo_autocomplete);
        geo_autocomplete.setThreshold(THRESHOLD);
        geo_autocomplete.setAdapter(new GeoAutoCompleteAdapter(getContext())); // 'this' is Activity instance
        geo_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
                geo_autocomplete.setText(result.getAddress());
                Geocoder coder = new Geocoder(getActivity());

                try {

                    String locationName = result.getAddress();
                    List<Address> addressList = coder.getFromLocationName(locationName, 5);
                    if (addressList != null && addressList.size() > 0) {
                        latitude1 = (int) (addressList.get(0).getLatitude() );
                        longitude1 = (int) (addressList.get(0).getLongitude() );
                        Log.e("ADDRESS LIST",String.valueOf(latitude1)+"\n"+String.valueOf(longitude1));
                    }
                    edit_search_lat_lng.putString("search_lat", String.valueOf(latitude1));
                    edit_search_lat_lng.putString("search_lng",String.valueOf(longitude1));
                    edit_search_lat_lng.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                try {
//                    ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(result.getAddress(), 0);
//                    for (Address add : adresses) {
//                        //Controls to ensure it is right address such as country etc.
//                        longitude1 = add.getLongitude();
//                        latitude1 = add.getLatitude();
//                        // Toast.makeText(getActivity(), String.valueOf(longitude1) + "\n" + String.valueOf(latitude1), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                double earthRadius = 3958.75;
                double dLat = Math.toRadians(latitude - latitude1);
                double dLng = Math.toRadians(longitude - longitude1);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude)) *
                                Math.sin(dLng / 2) * Math.sin(dLng / 2);
                double c1 = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double dist = earthRadius * c1;
                if (dist > 100) {
                    Toast.makeText(getActivity(), "Address is higher than 100 km from your current location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        geo_autocomplete.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    geo_autocomplete_clear.setVisibility(View.VISIBLE);
                } else {
                    geo_autocomplete_clear.setVisibility(View.GONE);
                }
            }
        });

        geo_autocomplete_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                geo_autocomplete.setText("");
            }
        });

        // Set on click lister for accept button
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!geo_autocomplete.getText().toString().equals(""))
                {
                    emailDialog.dismiss();
                    text_search_address.setText(geo_autocomplete.getText().toString());
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        emailDialog.show();
    }


private void doFileUpload(){

    String [] paths = {selectedImagePath,selectedImagePath2,selectedImagePath3};
    for (int i=0;i<Pic_length;i++) {
        String urlString = "http://52.37.136.238/JoinMe/Activity.svc/UpdateActivityPic/" + activity_id.getString("activity_id","")+"/"+allid.get(i);
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("uploadedfile1", new FileBody(new File(paths[i])));
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                Log.i("RESPONSE", response_str);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
    }

}

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        bundle = this.getArguments();
        final String screen   =bundle.getString("screen","0");
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (screen.equals("screen17")) {
                        FragmentManager fragmentManager = getFragmentManager();
                        Screen17 mygroup = new Screen17();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("userid",bundle.getString("userid","0"));
                        bundle1.putString("activityid",bundle.getString("activityid","0"));
                        bundle1.putString("where",bundle.getString("where","0"));
                        mygroup.setArguments(bundle1);
                        fragmentManager.beginTransaction().replace(R.id.flContent, mygroup).commit();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public class DateListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
            Calendar calander = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            String time = simpleDateFormat.format(calander.getTime());

            String current_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

            List hours =  new ArrayList<String>();;
            if (date.equals(current_date))
            {

                Log.e("Current time", time);

                hours.add(0, "");
                for (int i =0; i <=23; i++) {
                    if (i>Integer.parseInt(time))
                    {
                        hours.add(i + ":00");
                    }
                }
            }
            else {
                hours.add(0, "");
                for (int i =0; i <=23; i++) {
                    hours.add(i + ":00");

                }
            }
            ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hours);
            spinnerArrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerArrayAdapter4.notifyDataSetChanged();
            spinnerforhour.setAdapter(spinnerArrayAdapter4);
            dateTextView.setText(date);

        }
    }
}

