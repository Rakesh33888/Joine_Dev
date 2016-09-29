package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class Groups_CustomAdapter extends ArrayAdapter<Book> {

    public Mygroup activity;

    public Groups_CustomAdapter(Activity activity, int resource, List<Book> books) {
        super(activity, resource, books);
        activity = activity;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.custom_message, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);

        holder.name.setText(book.getName());
        holder.authorName.setText(book.getAuthorName());

        Picasso.with(getContext()).load("http://52.37.136.238/JoinMe/" + book.getImageUrl()).placeholder(R.drawable.butterfly)
                .resize(100, 100).noFade().fit()
                .centerCrop().into(holder.image);
        //new DownloadImageTask(holder.image).execute("http://52.37.136.238/JoinMe/" + book.getImageUrl());

        return convertView;
    }

    private static class ViewHolder {
        private TextView name;
        private TextView authorName;
        private ImageView image;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.textView1);
            image = (ImageView) v.findViewById(R.id.imageView1);
            authorName = (TextView) v.findViewById(R.id.message);
        }
    }

}


