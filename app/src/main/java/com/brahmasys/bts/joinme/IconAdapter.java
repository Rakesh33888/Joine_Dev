package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by apple on 01/09/16.
 */

public class IconAdapter extends ArrayAdapter<Book> {

    public Screen19 activity;



    private Context mContext;
    private int layoutId;
    private List<Book> dataList;
    public IconAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutId = resource;
        this.dataList = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.icon_image, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);

        Picasso.with(getContext()).load(Constant.BASE_URL + book.getImageUrl()).into(holder.image);

        //new DownloadImageTask(holder.image).execute("http://52.37.136.238/JoinMe/" + book.getImageUrl());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.icon_image, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);

        Picasso.with(getContext()).load(Constant.BASE_URL + book.getImageUrl()).into(holder.image);

        //new DownloadImageTask(holder.image).execute("http://52.37.136.238/JoinMe/" + book.getImageUrl());

        return convertView;
    }

    private static class ViewHolder {


        private ImageView image;
        TextView position;
        public ViewHolder(View v) {


           image = (ImageView) v.findViewById(R.id.icon_image);

        }
    }

}


