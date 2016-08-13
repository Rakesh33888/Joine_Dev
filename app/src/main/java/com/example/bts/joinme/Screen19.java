package com.example.bts.joinme;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.io.IOException;

public class Screen19 extends android.support.v4.app.Fragment {
    private static final int PICK_IMAGE_REQUEST = 3;
    private static final int RESULT_OK = 3;

    ScrollView scrollViewscreen19;
    LinearLayout linearLayoutscreen19, linearLayoutforicon, linearLayoutfor_time,
            linearLayoutforparticipationcost, numberlimit;
    ImageView firstimage, secondimage, thirdimage;
    EditText edittextactivityname, edittextforaddress, enterdiscription;
    Button create, delete;
    TextView icontype, texttime, textday, textmonth, textyear, texthour, textforlocation,
            textViewavailblefor, textViewgender, textViewforage, textViewageshowing,
            participtioncost, participationnumberlimit;
    Spinner spinnericon, spinnerforday, spinnerformonth, spinnerforyear, spinnerforhour;
    FrameLayout frameLayoutforlocation, frameLayoutavailblefor;
    CheckBox checkboxcurrent, checkBoxaddress, checkBoxforeveryone, checkBoxnotforeveryone,
            checkBoxformen, checkBoxforwomen;
    CrystalRangeSeekbar seekBarforage;
    private ContentResolver contentResolver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_screen19, container, false);
        scrollViewscreen19 = (ScrollView) v.findViewById(R.id.ScrollViewscreen19);
        linearLayoutscreen19 = (LinearLayout) v.findViewById(R.id.linearlayoutscreen19);
        edittextactivityname = (EditText) v.findViewById(R.id.edittextactivityname);
        linearLayoutforicon = (LinearLayout) v.findViewById(R.id.linearLayout_forIcon);
        icontype = (TextView) v.findViewById(R.id.icontype);
        spinnericon = (Spinner) v.findViewById(R.id.spinnericon);
        linearLayoutfor_time = (LinearLayout) v.findViewById(R.id.linearLayoutfor_time);
        texttime = (TextView) v.findViewById(R.id.timetext);
        textday = (TextView) v.findViewById(R.id.textview_for_day);
        textmonth = (TextView) v.findViewById(R.id.textfor_month);
        textyear = (TextView) v.findViewById(R.id.textfor_year);
        texthour = (TextView) v.findViewById(R.id.textfor_hour);
        spinnerforday = (Spinner) v.findViewById(R.id.spinner_day);
        spinnerformonth = (Spinner) v.findViewById(R.id.spinner_month);
        spinnerforyear = (Spinner) v.findViewById(R.id.spinner_year);
        spinnerforhour = (Spinner) v.findViewById(R.id.spinner_hour);
        frameLayoutforlocation = (FrameLayout) v.findViewById(R.id.framelayoutfor_location);
        textforlocation = (TextView) v.findViewById(R.id.textforlocation);
        edittextforaddress = (EditText) v.findViewById(R.id.textfor_address);
        checkboxcurrent = (CheckBox) v.findViewById(R.id.checkBoxfor_current);
        checkBoxaddress = (CheckBox) v.findViewById(R.id.checkboxfor_address);
//        frameLayoutavailblefor = (FrameLayout) v.findViewById(R.id.frameLayoutfor_availble);
        textViewavailblefor = (TextView) v.findViewById(R.id.textViewfor_availblefor);
        checkBoxforeveryone = (CheckBox) v.findViewById(R.id.checkBoxfor_everyone);
        checkBoxnotforeveryone = (CheckBox) v.findViewById(R.id.checkBoxfor_noteveryone);
        textViewgender = (TextView) v.findViewById(R.id.textViewfor_gender);
        textViewforage = (TextView) v.findViewById(R.id.textviewforage);
        checkBoxformen = (CheckBox) v.findViewById(R.id.checkBoxformen);
        checkBoxforwomen = (CheckBox) v.findViewById(R.id.checkBoxforwomen);
        textViewageshowing = (TextView) v.findViewById(R.id.ageshowing_text);
        seekBarforage = (CrystalRangeSeekbar) v.findViewById(R.id.rangeSeekbar);
        firstimage =(ImageView) v.findViewById(R.id.firstimage);
        firstimage.setClickable(true);

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


        participationnumberlimit = (TextView) v.findViewById(R.id.textforparticipantlimit);
        enterdiscription = (EditText) v.findViewById(R.id.enter_discription);
        create = (Button) v.findViewById(R.id.createbutton);
        delete = (Button) v.findViewById(R.id.deletebutton);

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
}

