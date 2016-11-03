package com.brahmasys.bts.joinme;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
import org.w3c.dom.Text;

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

public class Screen19 extends Fragment {

    GeoSearchResult result;
    Geocoder coder;
    TextView dateTextView;
    ImageView dateButton;
    FragmentManager fragmentManager;
    RelativeLayout search_layout,touchevent_layout;
    CircularImageView firstimage, secondimage, thirdimage;
    EditText edittextactivityname , enterdiscription,edit_cost,edit_limit,current_address;
    Button create;
    ImageView shareicon;
    FrameLayout address_search;
    Spinner spinnericon,currency_symbol, spinnerforhour;//, spinnerforday, spinnerformonth, spinnerforyear, spinnerforhour;
    LinearLayout not_everyone;
    CheckBox checkboxcurrent, checkBoxaddress, checkBoxforeveryone, checkBoxnotforeveryone, checkBoxformen, checkBoxforwomen;
    CrystalRangeSeekbar seekBarforage;
    private ContentResolver contentResolver;
    TextView age1,age2,text_search_address;
    String[] currency = new String[]{"€", "$"};
    String year="0",month="0",day="0",hour="0",minute;
    String availability="public";
    String gender="";
    String duration="0",icon ="null",title,address,age_start="0",age_end="100",cost="0",limit="0",description;
    double latitude=0.0,longitude=0.0,latitude1,longitude1,latitude2,longitude2;
    String complete_address,city,state,zip,country,total_address="0",checked_current_address;
    private static final String TAG = "CreateActivity";
    private static final String URL = "http://52.37.136.238/JoinMe/Activity.svc/CreateActivity";
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    private static final String LAT_LNG = "lat_lng";
    SharedPreferences user_id,activity_id,lat_lng;
    SharedPreferences.Editor edit_userid,edit_activity_id,edit_lat_lng;
    ProgressDialog  progressDialog;
    long unixTime=0;
    private Integer THRESHOLD = 2;
    private DelayAutoCompleteTextView geo_autocomplete;
    private ImageView geo_autocomplete_clear;
    HttpEntity resEntity;
    String activity_id1 ="0", str_value="0";
    private ArrayList<Book> books;
    private ArrayAdapter<Book> adapter;
    Context context;
    List<String> allurl;
    String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    private String selectedImagePath = "",selectedImagePath2 = "",selectedImagePath3 = "";
    public static final int PICK_IMAGE1 = 1;
    public static final int PICK_IMAGE2 = 2;
    public static final int PICK_IMAGE3 = 3;
    String imgPath;
    boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_screen19, container, false);
        coder = new Geocoder(getActivity());
        MultiDex.install(getActivity());
        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();
        lat_lng = getActivity().getSharedPreferences(LAT_LNG, getActivity().MODE_PRIVATE);
        edit_lat_lng = lat_lng.edit();
        latitude2 = Double.parseDouble(lat_lng.getString("lat", "0"));
        longitude2 = Double.parseDouble(lat_lng.getString("lng", "0"));
        Log.e("Location lat&lon",String.valueOf(latitude2)+"\n"+String.valueOf(longitude2));
        search_layout  = (RelativeLayout) v.findViewById(R.id.search_layout);
        current_address = (EditText)v.findViewById(R.id.current_address);
        text_search_address = (TextView)v.findViewById(R.id.search_address);
        spinnericon = (Spinner) v.findViewById(R.id.spinnericon);
        touchevent_layout = (RelativeLayout) v.findViewById(R.id.touch_outside);
//        spinnerforday = (Spinner) v.findViewById(R.id.spinner_day);
//        spinnerformonth = (Spinner) v.findViewById(R.id.spinner_month);
//        spinnerforyear = (Spinner) v.findViewById(R.id.spinner_year);
       spinnerforhour = (Spinner) v.findViewById(R.id.spinner_hour);
        dateTextView = (TextView)v.findViewById(R.id.date_textview);
        dateButton = (ImageView)v.findViewById(R.id.date_button);

        currency_symbol = (Spinner) v.findViewById(R.id.currency_symbol);
        not_everyone = (LinearLayout) v.findViewById(R.id.not_everyone);
        address_search = (FrameLayout) v.findViewById(R.id.address_search);
        enterdiscription  = (EditText) v.findViewById(R.id.enter_discription);
        edittextactivityname = (EditText) v.findViewById(R.id.edittextactivityname);
        edit_cost = (EditText) v.findViewById(R.id.forcost);
        edit_limit = (EditText) v.findViewById(R.id.numbrlimitbtn);
        checkboxcurrent = (CheckBox) v.findViewById(R.id.checkBoxfor_current);
        checkBoxaddress = (CheckBox) v.findViewById(R.id.checkboxfor_address);
        checkBoxforeveryone = (CheckBox) v.findViewById(R.id.checkBoxfor_everyone);
        checkBoxnotforeveryone = (CheckBox) v.findViewById(R.id.checkBoxfor_noteveryone);
        checkBoxformen = (CheckBox) v.findViewById(R.id.checkBoxformen);
        checkBoxforwomen = (CheckBox) v.findViewById(R.id.checkBoxforwomen);
        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);
        seekBarforage = (CrystalRangeSeekbar) v.findViewById(R.id.rangeSeekbar);
        firstimage =(CircularImageView) v.findViewById(R.id.firstimage);
        secondimage = (CircularImageView) v.findViewById(R.id.secondimage);
        thirdimage = (CircularImageView) v.findViewById(R.id.thrdimage);
        age1 = (TextView) v.findViewById(R.id.age1);
        age2 = (TextView) v.findViewById(R.id.age2);
        create  = (Button) v.findViewById(R.id.createbutton);
        enterdiscription = (EditText) v.findViewById(R.id.enter_discription);

        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address_search_Dialog();
            }
        });

        touchevent_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
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
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);
                            JSONArray cast = obj.getJSONArray("data");
                            List<String> allid = new ArrayList<String>();
                            allurl = new ArrayList<String>();
                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String id = actor.getString("icon_id");
                                String url = actor.getString("icon_url");
                                allid.add(id);
                                allurl.add(url);
                                Book book = new Book();
                                book.setImageUrl(actor.getString("icon_url"));
                                books.add(book);
                                Log.d("Type", cast.getString(i));
                            }
                            adapter.notifyDataSetChanged();
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





        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        //  Toast.makeText(MainActivity.this, token_id.getString("token","null"), Toast.LENGTH_SHORT).show();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            //LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //timezone.setText(String.valueOf(latitude) + "\n" + String.valueOf(longitude));
            }

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

                latitude = latitude;
                longitude = longitude;

            }
            else
            {
                latitude = latitude2;
                longitude = longitude2;
            }

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            List<Address> addresses = null;
            try {

                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if(addresses.size()>0)
                    if(addresses.size()>0) {

                        complete_address = addresses.get(0).getAddressLine(0);
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                      //  zip = addresses.get(0).getPostalCode();
                        country = addresses.get(0).getCountryName();
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }

            checked_current_address = complete_address + "," + city + "," + state + "," + country;
            current_address.setText(checked_current_address);

        }else{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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



        /******************** CheckBox Functionality Start*******************/
        checkboxcurrent.setChecked(true);
        checkboxcurrent.setClickable(false);


        checkboxcurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkboxcurrent) {
                    checkBoxaddress.setChecked(false);
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
                }

            }
        });
        checkBoxforeveryone.setChecked(true);
        checkBoxforeveryone.setClickable(false);
        checkBoxforeveryone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxforeveryone) {
                    checkBoxnotforeveryone.setChecked(false);
                    not_everyone.setVisibility(View.GONE);
                    checkBoxforeveryone.setClickable(false);
                    checkBoxnotforeveryone.setClickable(true);

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

                }

            }
        });

        checkBoxforwomen.setChecked(true);
        checkBoxforwomen.setClickable(false);
        checkBoxforwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxforwomen) {
                    checkBoxformen.setChecked(false);
                    checkBoxforwomen.setClickable(false);
                    checkBoxformen.setClickable(true);
                }

            }
        });
        checkBoxformen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxformen) {
                    checkBoxforwomen.setChecked(false);
                    checkBoxforwomen.setClickable(true);
                    checkBoxformen.setClickable(false);
                }

            }
        });
        /******************** CheckBox Functionality End*******************/

        seekBarforage.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                age1.setText(String.valueOf(minValue));
                age2.setText(String.valueOf(maxValue));
            }
        });



        firstimage.setOnClickListener(new View.OnClickListener() {
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

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE1);
            }
        });
        secondimage.setOnClickListener(new View.OnClickListener() {
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

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE2);

            }
        });
        thirdimage.setOnClickListener(new View.OnClickListener() {
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


    /*************************** Spinner Functionality Start ***********************/

        List year_list = new ArrayList<Integer>();
        year_list.add(0, " ");
        for (int i = 2015; i <= 2020; i++)
        {
            if (current_year<=i) {
                year_list.add(Integer.toString(i));
            }
        }

        List hours = new ArrayList<Integer>();
        hours.add(0, "");
        for (int i =23; i >=0; i--) {
             hours.add(Integer.toString(i));

        }
        ArrayAdapter<Integer> spinnerArrayAdapter4 = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, hours);
        spinnerArrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapter4.notifyDataSetChanged();
        spinnerforhour.setAdapter(spinnerArrayAdapter4);

        ArrayAdapter<String> adapter_currency = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, currency);
        currency_symbol.setAdapter(adapter_currency);
//    /**************************Spinner functionality Ends *****************************/
//        TimeZone tz = TimeZone.getDefault();
//        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT));
//
//

        create.setOnClickListener(new View.OnClickListener() {
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
                            CreateActivity();
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

        new Marshmallow_Permissions().verifyStoragePermissions(getActivity());

        return v;
    }


    public void CreateActivity() throws ParseException {
        if (Connectivity_Checking.isConnectingToInternet()) {

         if (!edittextactivityname.getText().toString().equals("")&& !enterdiscription.getText().toString().equals("") && !dateTextView.getText().toString().equals("Select date")
                 && !edit_cost.getText().toString().equals("") && !edit_limit.getText().toString().equals("") ) {
             if (edittextactivityname.getText().toString().length() >= 2) {
                 if (enterdiscription.getText().toString().length() >= 10) {


                     int int_month = 0;
//                year = spinnerforyear.getSelectedItem().toString();
//                month = spinnerformonth.getSelectedItem().toString();
//                day = spinnerforday.getSelectedItem().toString();
                     hour = spinnerforhour.getSelectedItem().toString();


                     /*************** Time Stamp Start********************/

                     String dateString = dateTextView.getText().toString() + " " + hour + ":00" + ":00" + " " + "GMT";
                     DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss z");

                     Date date1 = dateFormat.parse(dateString);
                     unixTime = (long) date1.getTime() / 1000;
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
                         if (checkBoxformen.isChecked()) {
                             gender = "male";
                         } else {
                             gender = "female";
                         }
                     }
                     if (checkboxcurrent.isChecked()) {

                         total_address = checked_current_address;
                     } else {

                         total_address = geo_autocomplete.getText().toString();

                     }

                     title = edittextactivityname.getText().toString();
                     description = enterdiscription.getText().toString();
                     cost = edit_cost.getText().toString();
                     limit = edit_limit.getText().toString();
                     age_start = age1.getText().toString();
                     age_end = age2.getText().toString();

                     Log.e("Complete Data:", availability + "\n" + description + "\n" + duration + "\n" + 0 + "\n" + res + "\n" + unixTime + "\n" + title + "\n" + total_address + "\n" + latitude
                             + "\n" + longitude + "\n" + age_start + "\n" + age_end + "\n" + cost + "\n" + limit + "\n" + gender);


                     if (android.os.Build.VERSION.SDK_INT > 9) {
                         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                         StrictMode.setThreadPolicy(policy);
                     }

                     JSONObject jsonObjSend = new JSONObject();
                     try {
                         jsonObjSend.put("activity_availability", availability);//nari1
                         jsonObjSend.put("activity_description", description); //2
                         jsonObjSend.put("activity_duration", duration);       //3
                         jsonObjSend.put("activity_icon", icon);               //4
                         jsonObjSend.put("activity_time", unixTime);              //5
                         jsonObjSend.put("activity_timezoneoffset", res);       //6
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

                         Log.i(TAG, jsonObjSend.toString(16));


                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     JSONObject jsonObjRecv = com.brahmasys.bts.joinme.HttpClient.SendHttpPost(URL, jsonObjSend);


                     JSONObject json = null;
                     try {
                         json = new JSONObject(String.valueOf(jsonObjRecv));
                         activity_id1 = json.getString("activityid");
                         edit_activity_id.putString("activity_id", activity_id1);
                         edit_activity_id.commit();

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
                         str_value = json_LL.getString("message");

                         if (str_value.equals("Added Successfully")) {

                             fragmentManager = getFragmentManager();
                             doFileUpload();
                             Mygroup mygroup = new Mygroup();
                             fragmentManager.beginTransaction()
                                     .replace(R.id.flContent, mygroup)
                                     .addToBackStack(null)
                                     .commit();

                             Toast.makeText(getActivity(), "Activity has been created.", Toast.LENGTH_LONG).show();

                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }


                 } else {
                     Toast.makeText(getActivity(), "Description at least you have to enter 10 characters!", Toast.LENGTH_LONG).show();
                 }
             } else {
                 Toast.makeText(getActivity(), "Activity name at least you have to enter 2 characters!", Toast.LENGTH_LONG).show();
             }
         }
         else
         {
             Toast.makeText(getActivity(), "Please fill all the details...!", Toast.LENGTH_LONG).show();
         }


        } else {
            Splashscreen dia = new Splashscreen();
            dia.Connectivity_Dialog_with_refresh(getActivity());
            progressDialog.dismiss();
        }

    }

    private void setListViewAdapter() {
        books = new ArrayList<Book>();
        adapter = new IconAdapter(getActivity(), R.layout.icon_image, books);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnericon.setAdapter(adapter);

    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        imgPath = file.getAbsolutePath();
        return imgUri;
    }


    public String getImagePath() {
        return imgPath;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {




        if (isKitKat && resultCode != Activity.RESULT_CANCELED) {

            String uri = new ImagePath().getPath(getContext(), data.getData());


            if (requestCode == PICK_IMAGE1) {

                selectedImagePath = uri;
                firstimage.setImageBitmap(new DecodeImage().decodeFile(selectedImagePath));

            }
            if (requestCode == PICK_IMAGE2) {
                selectedImagePath2 = uri;
                secondimage.setImageBitmap(new DecodeImage().decodeFile(selectedImagePath2));
            }
            if (requestCode == PICK_IMAGE3) {
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

        // Get dialog widgets references.
       // final EditText editFriendsEmail = (EditText)emailDialog.findViewById(R.id.editEmailAddFriendEmail);
        Button btnAccept = (Button)emailDialog.findViewById(R.id.done);
        Button cancel  = (Button) emailDialog.findViewById(R.id.cancel);

        geo_autocomplete_clear = (ImageView) emailDialog.findViewById(R.id.geo_autocomplete_clear);
        geo_autocomplete = (DelayAutoCompleteTextView) emailDialog.findViewById(R.id.geo_autocomplete);
        geo_autocomplete.setThreshold(THRESHOLD);
        geo_autocomplete.setAdapter(new GeoAutoCompleteAdapter(getContext())); // 'this' is Activity instance

        geo_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                result  = (GeoSearchResult) adapterView.getItemAtPosition(position);
                geo_autocomplete.setText(result.getAddress());
             Log.e("String Addree", String.valueOf(result.getAddress()));

                try {

                    String locationName = result.getAddress();
                    List<Address> addressList = coder.getFromLocationName(locationName, 5);
                    if (addressList != null && addressList.size() > 0) {
                        latitude1 = (int) (addressList.get(0).getLatitude() );
                        longitude1 = (int) (addressList.get(0).getLongitude() );
                        Log.e("ADDRESS LIST",String.valueOf(latitude1)+"\n"+String.valueOf(longitude1));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

//               try {
//                    ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(result.getAddress(), 0);
//                    for (Address add : adresses) {
//                        //Controls to ensure it is right address such as country etc.
//                        longitude1 = add.getLongitude();
//                        latitude1 = add.getLatitude();
//
//                        // Toast.makeText(getActivity(), String.valueOf(longitude1) + "\n" + String.valueOf(latitude1), Toast.LENGTH_SHORT).show();
//                    }
//                   // Log.e("ADDRESS LIST",String.valueOf(longitude1)+"\n"+String.valueOf(latitude1));
//
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
        for (int i=0;i<3;i++) {
            String urlString = "http://52.37.136.238/JoinMe/Activity.svc/UploadActivityPic/" + activity_id1;
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

    public ContentResolver getContentResolver() {
        return contentResolver;
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

    public class DateListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
            dateTextView.setText(date);
        }
    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onRestart()
    {
        super.getActivity().onBackPressed();
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("screen19");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}

