package com.example.bts.joinme;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
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

import com.github.siyamed.shapeimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class Screen3a extends AppCompatActivity  {
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
    String deviceuid,device_type="android",registration_type;
    SharedPreferences user_id;
    SharedPreferences.Editor edit_userid;
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

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


                        Intent a = new Intent(getApplicationContext(), Screen16.class);
                        startActivity(a);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        finish();
                        Log.w("myApp", firstname.getText().toString() + "\n" + lastname.getText().toString() + "\n" + conformation_code.getText().toString() + "\n" + gender + "\n" + birth);
                        Toast.makeText(Screen3a.this, firstname.getText().toString() + "\n" + lastname.getText().toString() + "\n" + conformation_code.getText().toString() + "\n" + gender + "\n" + birth, Toast.LENGTH_SHORT).show();
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


              //  CircularImageView imageView = (CircularImageView) findViewById(R.id.imageView2);

                circle_image.setImageBitmap(bitmap);
                select_image =1;
            } catch (IOException e) {

            }
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