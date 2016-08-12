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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button facebook, mail,b4,b5;
    TextView tv1, tv2, already_member;

     SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebook = (Button) findViewById(R.id.facebook);
        mail = (Button) findViewById(R.id.mail);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        session = new SessionManager(getApplicationContext());



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
                                        Intent i1 = new Intent(getApplicationContext(), Screen3a.class);
                                        startActivity(i1);
                                        Toast toast = Toast.makeText(getApplicationContext(), "Login Succesfull", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        dialog.dismiss();
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


