package com.example.bts.joinme;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Screen13 extends android.support.v4.app.Fragment {

    TextView report_text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_screen13,container,false);
        report_text = (TextView) v.findViewById(R.id.report_text);

        report_text.setPaintFlags(report_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        return  v;
    }
}
