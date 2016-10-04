package com.brahmasys.bts.joinme;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by apple on 20/09/16.
 */
public class Custom_Progress extends ProgressDialog {

    public Custom_Progress(Context context) {
        super(context);
        ProgressDialog  progressDialog =ProgressDialog.show(context, null, null, true);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#82ADBD")));
         progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }




}
