package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

<<<<<<< HEAD
import com.brahmasys.bts.joinme.R;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

=======
>>>>>>> origin/master
public class Splashscreen extends Activity{
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        session = new SessionManager(getApplicationContext());
<<<<<<< HEAD
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
=======
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch(InterruptedException e){
>>>>>>> origin/master
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isConnectingToInternet()) {
                            if (session.isLoggedIn()) {

                                Intent home = new Intent(Splashscreen.this, Screen16.class);
                                startActivity(home);
                                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                finish();
                            } else {


                                Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                finish();
                            }

                        } else {
                            Toast.makeText(Splashscreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            // Create Custome dialog for No Internet Connection
                        }

                    }
                });
            }
        }).start();

    }
    public static boolean isConnectingToInternet() {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(2000, TimeUnit.MILLISECONDS);
            future.cancel(true);

        } catch (Exception e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    }

