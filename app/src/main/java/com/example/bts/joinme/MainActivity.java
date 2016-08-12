package com.example.bts.joinme;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button b1, b2,b4,b5;
    TextView tv1, tv2, b3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        b3 = (TextView) findViewById(R.id.btn3);
        b3.setClickable(true);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == b3) {
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
                            if (email.getText().toString().trim().length()> 0 && pass.getText().toString().trim().length() > 0){
                                Intent i = new Intent(getApplicationContext(), Screen16.class);
                                    startActivity(i);
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

//               final String email = emailEditText.getText().toString();
//                if (!isValidEmail(email)) {
//                    emailEditText.setError("Invalid Email");
//                }
//
//                final String pass = passEditText.getText().toString();
//                if (!isValidPassword(pass)) {
//                    passEditText.setError("Invalid Password");
                    //}


                }

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == b2) {
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

                            if (email.getText().toString().trim().length() > 0 && pass.getText().toString().trim().length() > 0) {
                                Intent i1 = new Intent(getApplicationContext(), Screen3a.class);
                                startActivity(i1);
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


