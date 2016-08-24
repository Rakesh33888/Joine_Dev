package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brahmasys.bts.joinme.R;

/**
 * Created by ajay on 8/10/2016.
 */
public class CustomList extends ArrayAdapter<String>{
    private String[] prgmNameList;
     private Appsetting appsetting;
    private Activity context;


    public CustomList(Activity context, String[] prgmNameList) {
        super(context,R.layout.listview_item,prgmNameList);
        this.context=context;
        this.prgmNameList=prgmNameList;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listitems=inflater.inflate(R.layout.listview_item,null,true);
        TextView itemtext= (TextView) listitems.findViewById(R.id.itemtext);
        itemtext.setText(prgmNameList[position]);
        return listitems;
    }
}
