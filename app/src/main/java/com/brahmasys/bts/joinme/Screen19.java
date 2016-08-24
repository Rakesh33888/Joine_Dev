package com.brahmasys.bts.joinme;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Screen19 extends android.support.v4.app.Fragment {
    private static final int PICK_IMAGE_REQUEST = 3;
    private static final int RESULT_OK = 3;

    ImageView firstimage, secondimage, thirdimage;
    EditText edittextactivityname, edittextforaddress, enterdiscription,edit_cost,edit_limit;
    Button create;
    Spinner spinnericon, spinnerforday, spinnerformonth, spinnerforyear, spinnerforhour,currency_symbol;
    LinearLayout not_everyone;
    CheckBox checkboxcurrent, checkBoxaddress, checkBoxforeveryone, checkBoxnotforeveryone, checkBoxformen, checkBoxforwomen;
    CrystalRangeSeekbar seekBarforage;
    private ContentResolver contentResolver;
    TextView age1,age2;
    String[] currency = new String[]{"$", "€"};

    String year,month,day,hour,minute;
    String availability;
    String gender="";
    String duration="0",icon = "0",title,address,age_start,age_end,cost,limit,description;
    Double latitude=0.0,longitude=0.0,latitude1,longitude1;
    String complete_address,city,state,zip,country,total_address;

    private static final String TAG = "CreateActivity";
    private static final String URL = "http://52.37.136.238/JoinMe/Activity.svc/CreateActivity";
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";

    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_screen19, container, false);

        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();

          pd = new ProgressDialog(getActivity());
          pd.setMessage("loading");

        spinnericon = (Spinner) v.findViewById(R.id.spinnericon);
        spinnerforday = (Spinner) v.findViewById(R.id.spinner_day);
        spinnerformonth = (Spinner) v.findViewById(R.id.spinner_month);
        spinnerforyear = (Spinner) v.findViewById(R.id.spinner_year);
        spinnerforhour = (Spinner) v.findViewById(R.id.spinner_hour);

        currency_symbol = (Spinner) v.findViewById(R.id.currency_symbol);
        not_everyone = (LinearLayout) v.findViewById(R.id.not_everyone);

        edittextforaddress = (EditText) v.findViewById(R.id.textfor_address);
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

        seekBarforage = (CrystalRangeSeekbar) v.findViewById(R.id.rangeSeekbar);
        firstimage =(ImageView) v.findViewById(R.id.firstimage);
        firstimage.setClickable(true);
        age1 = (TextView) v.findViewById(R.id.age1);
        age2 = (TextView) v.findViewById(R.id.age2);

        create  = (Button) v.findViewById(R.id.createbutton);
        enterdiscription = (EditText) v.findViewById(R.id.enter_discription);


        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //timezone.setText(String.valueOf(latitude) + "\n" + String.valueOf(longitude));
        }


        /******************** CheckBox Functionality Start*******************/
        checkboxcurrent.setChecked(true);

        checkboxcurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkboxcurrent) {
                    checkBoxaddress.setChecked(false);
                    edittextforaddress.setVisibility(View.GONE);

                }

            }
        });




        checkBoxaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxaddress) {
                    checkboxcurrent.setChecked(false);
                    edittextforaddress.setVisibility(View.VISIBLE);
                }

            }
        });
        checkBoxforeveryone.setChecked(true);
        checkBoxforeveryone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxforeveryone) {
                    checkBoxnotforeveryone.setChecked(false);
                    not_everyone.setVisibility(View.GONE);


                }

            }
        });

        checkBoxnotforeveryone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxnotforeveryone) {
                    checkBoxforeveryone.setChecked(false);
                    not_everyone.setVisibility(View.VISIBLE);


                }

            }
        });

        checkBoxforwomen.setChecked(true);
        checkBoxforwomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxforwomen) {
                    checkBoxformen.setChecked(false);

                }

            }
        });
        checkBoxformen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxformen) {
                    checkBoxforwomen.setChecked(false);

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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        secondimage = (ImageView) v.findViewById(R.id.secondimage);
        secondimage.setClickable(true);
        secondimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        thirdimage = (ImageView) v.findViewById(R.id.thrdimage);
        thirdimage.setClickable(true);
        thirdimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });






    /*************************** Spinner Functionality Start ***********************/
        List day_list = new ArrayList<Integer>();
        for (int i = 1; i <= 31; i++)
        {
            day_list.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, day_list);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinnerforday.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.month_names));
        spinnerformonth.setAdapter(adapter1);

        List year_list = new ArrayList<Integer>();
        for (int i = 2015; i <= 2020; i++)
        {
            year_list.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter1 = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, year_list);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerforyear.setAdapter(spinnerArrayAdapter1);

        List hours = new ArrayList<Integer>();
        for (int i = 0; i <= 23; i++)
        {
            hours.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter4 = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, hours);
        spinnerArrayAdapter4.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinnerforhour.setAdapter(spinnerArrayAdapter4);

        ArrayAdapter<String> adapter_currency = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, currency);
        currency_symbol.setAdapter(adapter_currency);
    /**************************Spinner functionality Ends *****************************/
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT));







        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                int int_month=0;
                year = (String) spinnerforyear.getSelectedItem();
                month= (String) spinnerformonth.getSelectedItem();
                day  = (String) spinnerforday.getSelectedItem();
                hour = (String) spinnerforhour.getSelectedItem();

                if (month.equals("Jan"))
                {
                    int_month=1;
                }
                else  if (month.equals("Feb"))
                {
                    int_month=2;
                }
                else  if (month.equals("Mar"))
                {
                    int_month=3;
                }
                else  if (month.equals("Apr"))
                {
                    int_month=4;
                }
                else  if (month.equals("May"))
                {
                    int_month=5;
                }
                else  if (month.equals("Jun"))
                {
                    int_month=6;
                }
                else  if (month.equals("Jul"))
                {
                    int_month=7;
                }
                else  if (month.equals("Aug"))
                {
                    int_month=8;
                }else  if (month.equals("Sept"))
                {
                    int_month=9;
                }
                else  if (month.equals("Oct"))
                {
                    int_month=10;
                }
                else  if (month.equals("Nov"))
                {
                    int_month=11;
                }
                else  if (month.equals("Dec"))
                {
                    int_month=12;
                }


                /*************** Time Stamp Start********************/
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, Integer.parseInt(year));
                c.set(Calendar.MONTH, int_month);
                c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                c.set(Calendar.HOUR, Integer.parseInt(hour));
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                long result = (c.getTimeInMillis() / 1000L);
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

                int res = (Integer.parseInt(hr)*60)+Integer.parseInt(min);
                if (sign.equals("+"))
                {
                    res = -res;
                }
                else
                {
                    res = +res;
                }
                /********************* TimeZone End ***************/

                if (checkBoxforeveryone.isChecked())
                {
                    availability = "public";
                    gender = "";

                }
                else
                {
                    availability = "private";
                    if (checkBoxformen.isChecked())
                    {
                        gender = "male";
                    }
                    else
                    {
                        gender = "female";
                    }
                }
                if (checkboxcurrent.isChecked())
                {

                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                    List<Address> addresses  = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude,longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    complete_address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    zip = addresses.get(0).getPostalCode();
                    country = addresses.get(0).getCountryName();
                    total_address = complete_address+","+city+","+state+","+zip+","+country;
                }
                else {

                        total_address = edittextforaddress.getText().toString();
                    /************** Calculating Distance *************************
                     Geocoder coder = new Geocoder(getActivity());
                     try {
                     ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(edittextforaddress.getText().toString(), 2);
                     for (Address add : adresses) {
                     //Controls to ensure it is right address such as country etc.
                     longitude1 = add.getLongitude();
                     latitude1 = add.getLatitude();
                     Toast.makeText(getActivity(), String.valueOf(longitude) + "\n" + String.valueOf(latitude), Toast.LENGTH_SHORT).show();
                     }
                     } catch (IOException e) {
                     e.printStackTrace();
                     }




                     double earthRadius = 3958.75;
                     double dLat = Math.toRadians(latitude - latitude1);
                     double dLng = Math.toRadians(longitude - longitude1);
                     double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                     Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude)) *
                     Math.sin(dLng / 2) * Math.sin(dLng / 2);
                     double c1 = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                     double dist = earthRadius * c1;
                     Toast.makeText(getActivity(), String.valueOf(dist), Toast.LENGTH_SHORT).show();

                     *************************************/

                }


                address = edittextforaddress.getText().toString();
                title   = edittextactivityname.getText().toString();
                description = enterdiscription.getText().toString();
                cost  = edit_cost.getText().toString();
                limit = edit_limit.getText().toString();
                age_start = age1.getText().toString();
                age_end   = age2.getText().toString();

                Log.w("Complete Data:", availability + "\n" + description + "\n" + duration + "\n" + 0 + "\n" + res + "\n" + result + "\n" + title + "\n" + total_address + "\n" + latitude
                        + "\n" + longitude + "\n" + age_start + "\n" + age_end+"\n"+cost+"\n"+limit+"\n"+gender);


                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                JSONObject jsonObjSend = new JSONObject();
                try {
                    // Add key/value pairs
                    jsonObjSend.put("activity_availability",availability);
                    jsonObjSend.put("activity_description", description);
                    jsonObjSend.put("activity_duration", duration);
                    jsonObjSend.put("activity_icon", icon);
                    jsonObjSend.put("activity_time",result);
                    jsonObjSend.put("activity_timezoneoffset",res);
                    jsonObjSend.put("activity_title",title);
                    jsonObjSend.put("gender",gender);
                    jsonObjSend.put("address",total_address);
                    jsonObjSend.put("lat",latitude);
                    jsonObjSend.put("lon",longitude);
                    jsonObjSend.put("participant_age_end",age_end);
                    jsonObjSend.put("participant_age_start",age_start);
                    jsonObjSend.put("participant_cost",cost);
                    jsonObjSend.put("participant_gender",gender);
                    jsonObjSend.put("participant_limit",limit);
                    jsonObjSend.put("userid",user_id.getString("userid","null"));
                    //  hideDialog();
                    Log.i(TAG, jsonObjSend.toString(13));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObjRecv = com.brahmasys.bts.joinme.HttpClient.SendHttpPost(URL, jsonObjSend);


                JSONObject json = null;
                try {
                    json = new JSONObject(String.valueOf(jsonObjRecv));
                    String activity_id1 = json.getString("activityid");
                    edit_activity_id.putString("activity_id",activity_id1);
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
                    String str_value = json_LL.getString("message");


                    Toast.makeText(getActivity(), str_value, Toast.LENGTH_LONG).show();

                    if (str_value.equals("Added Successfully")) {

                        pd.hide();
                        Toast.makeText(getActivity(), str_value, Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                firstimage = (ImageView) getActivity().findViewById(R.id.firstimage);
                firstimage.setImageBitmap(bitmap);
//
            } catch (IOException e) {

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
}

