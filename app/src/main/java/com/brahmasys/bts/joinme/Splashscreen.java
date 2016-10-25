package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Splashscreen extends Activity{
    private SessionManager session;
    RelativeLayout refresh;
    final static private String PREF_KEY_SHORTCUT_ADDED = "PREF_KEY_SHORTCUT_ADDED";
    SharedPreferences sharedPreferences;

    @Override
    public void onStart(){
        super.onStart();
       // Checking if ShortCut was already added
        sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean shortCutWasAlreadyAdded = sharedPreferences.getBoolean(PREF_KEY_SHORTCUT_ADDED, false);
        if (!shortCutWasAlreadyAdded) createShortcutIcon();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        session = new SessionManager(getApplicationContext());
        refresh = (RelativeLayout) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refresh();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Connectivity_Checking.isConnectingToInternet()) {
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

                            Connectivity_Dialog(Splashscreen.this);

                            //refresh.setVisibility(View.VISIBLE);
                          //  Toast.makeText(Splashscreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();


                            // Create Custome dialog for No Internet Connection
                        }

                    }
                });
            }
        }).start();

    }


    public void Connectivity_Dialog(Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.connectivity_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Button yes = (Button) dialog.findViewById(R.id.ok);
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
               Splashscreen.this.refresh.setVisibility(View.VISIBLE);
            }
        });


    }

    public void Connectivity_Dialog_with_refresh(final Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.connectivity_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        Button yes = (Button) dialog.findViewById(R.id.ok);
        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });


    }
    public void Refresh()
    {
        if (Connectivity_Checking.isConnectingToInternet()) {
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

            Connectivity_Dialog(Splashscreen.this);

        }
    }

    private void createShortcutIcon(){
        Intent shortcutIntent = new Intent(getApplicationContext(), Splashscreen.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE
                , Intent.ShortcutIconResource.fromContext(getApplicationContext()
                , R.drawable.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
        // Remembering that ShortCut was already added
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_KEY_SHORTCUT_ADDED, true);
        editor.commit();

    }// end createShortcutIc

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
