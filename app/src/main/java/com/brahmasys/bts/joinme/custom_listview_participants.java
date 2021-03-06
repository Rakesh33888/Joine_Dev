package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by apple on 29/08/16.
 */

public class custom_listview_participants extends ArrayAdapter<Book> {

    public Mygroup activity;
    ProgressDialog pd;
    public custom_listview_participants(Activity activity, int resource, List<Book> books) {
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
            convertView = inflater.inflate(R.layout.participants, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);

      //  holder.name.setText(book.getName());
      //  holder.authorName.setText(book.getAuthorName());

//        Picasso.with(getContext()).load("http://52.37.136.238/JoinMe/" + book.getImageUrl()).placeholder(R.drawable.butterfly)
//                .into(holder.image);

        Picasso.with(getContext())
                .load(Constant.BASE_URL + book.getImageUrl()) // thumbnail url goes here
                .placeholder(R.drawable.butterfly)
                .resize(50,50)
                .centerCrop()
                .skipMemoryCache()
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                    }
                });

        return convertView;
    }

    private static class ViewHolder {
       // private TextView name;
     //   private TextView authorName;
        private ImageView image;

        public ViewHolder(View v) {
         //   name = (TextView) v.findViewById(R.id.imgaeinfotxt);
            image = (ImageView) v.findViewById(R.id.participants);
         //   authorName = (TextView) v.findViewById(R.id.timeinotext);
        }
    }

}