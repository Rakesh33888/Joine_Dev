package com.example.bts.joinme;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;

public class Screen3a extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imageView;
    Button btn6;
    TextView textView3, textView4;
    EditText editText1, editText2, editText3, editText4;
    Bitmap bitmap;
    android.support.v7.app.ActionBar actionBar;

     TextView textView5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3aa);
        editText1 = (EditText) findViewById(R.id.editText5);
        editText2 = (EditText) findViewById(R.id.editText6);
        editText3 = (EditText) findViewById(R.id.editText7);
        editText4 = (EditText) findViewById(R.id.editText8);
        imageView = (ImageView) findViewById(R.id.imageView2);
        textView5 = (TextView) findViewById(R.id.editText9);



        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

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
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });
        btn6 = (Button) findViewById(R.id.button3);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getApplicationContext(), Screen16.class);
                startActivity(a);
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


                ImageView imageView = (ImageView) findViewById(R.id.imageView2);

                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {

            }
        }

    }}