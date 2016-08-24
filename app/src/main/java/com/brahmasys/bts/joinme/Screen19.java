package com.brahmasys.bts.joinme;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.bts.joinme.R;

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
    public static final String LAT_LNG = "lat_lng";

    SharedPreferences lat_lng;
    SharedPreferences.Editor edit_lat_lng;
    ScrollView scrollViewscreen19;

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
    String duration="0",title,address,age_start,age_end,cost,limit,description;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_screen19, container, false);

        lat_lng = getActivity().getSharedPreferences(LAT_LNG, getActivity().MODE_PRIVATE);
        edit_lat_lng = lat_lng.edit();



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
        /******************** CheckBox Functionality Start*******************/
        checkboxcurrent.setChecked(true);
        checkboxcurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkboxcurrent) {
                    checkBoxaddress.setChecked(false);


                    edittextforaddress.setFocusable(false);
                    edittextforaddress.setEnabled(false);
                    edittextforaddress.setCursorVisible(false);
                    edittextforaddress.setKeyListener(null);
                    edittextforaddress.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });
        checkBoxaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == checkBoxaddress) {
                    checkboxcurrent.setChecked(false);


                    edittextforaddress.setFocusable(true);
                    edittextforaddress.setEnabled(true);
                    edittextforaddress.setCursorVisible(true);

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



        enterdiscription = (EditText) v.findViewById(R.id.enter_discription);
        create = (Button) v.findViewById(R.id.createbutton);


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
                String lat = lat_lng.getString("lat","");
                String lng = lat_lng.getString("lng","");

                if (checkBoxforeveryone.isChecked())
                {
                    availability = "public";

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

                address = edittextforaddress.getText().toString();
                title   = edittextactivityname.getText().toString();
                description = enterdiscription.getText().toString();
                cost  = edit_cost.getText().toString();
                limit = edit_limit.getText().toString();
                age_start = age1.getText().toString();
                age_end   = age2.getText().toString();

                Log.w("Complete Data:", availability + "\n" + description + "\n" + duration + "\n" + 0 + "\n" + res + "\n" + result + "\n" + title + "\n" + address + "\n" + lat
                        + "\n" + lng + "\n" + age_start + "\n" + age_end+"\n"+cost+"\n"+limit+"\n"+gender);


                //Toast.makeText(getActivity(), String.valueOf(result)+"\n"+ String.valueOf(res)+"\n"+lat+"\n"+lng, Toast.LENGTH_SHORT).show();
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

