package com.brahmasys.bts.joinme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Terms_services extends Fragment {
    ImageView backimage;
    LinearLayout backlayout;
    ImageView shareicon,msg,logo;
    TextView terms_services;
    ProgressDialog progressDialog;
    private static final String TAG = Terms_services.class.getSimpleName();
    private WebView mWebView;
    public Terms_services() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.terms_services,container,false);
        backimage = (ImageView) v.findViewById(R.id.imageback_terms_services);
        terms_services= (TextView) v.findViewById(R.id.terms_services_text);
        backlayout= (LinearLayout) v.findViewById(R.id.backlayouthomescreen);

        Toolbar refTool = ((Screen16)getActivity()).toolbar;
        shareicon= (ImageView) refTool.findViewById(R.id.shareicon);
        shareicon.setVisibility(View.GONE);
        msg = (ImageView) refTool.findViewById(R.id.msg);
        msg.setBackgroundResource(R.drawable.custo_msg);
        logo = (ImageView) refTool.findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(getActivity(), null, null, true);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.custom_progress);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Intent i = new Intent(getContext(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
            }
        });

        mWebView = (WebView)v.findViewById(R.id.webviewterms_services);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //mWebView.loadUrl("http://www.entourageyearbooks.com/PrivacyPolicy.asp");
        mWebView.loadUrl("file:///android_asset/html/PrivacPolicy.html");
        backlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    Appsetting appsetting = new Appsetting();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, appsetting);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "Link Value: " + url);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {


            }
        });
        return  v;
    }



}