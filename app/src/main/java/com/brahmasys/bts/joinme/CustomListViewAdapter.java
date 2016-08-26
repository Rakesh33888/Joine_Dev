package com.brahmasys.bts.joinme;

/**
 * Created by apple on 29/04/16.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<Book> {

  public Mygroup activity;

    public CustomListViewAdapter(Activity activity, int resource, List<Book> books) {
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
            convertView = inflater.inflate(R.layout.groups_list, parent, false);
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
      //  holder.authorName.setText(book.getAuthorName());
       // Picasso.with(activity).load("http://52.26.55.225/devwoof/"+book.getImageUrl()).into(holder.image);
        new DownloadImageTask(holder.image).execute("http://52.37.136.238/JoinMe/" + book.getImageUrl());
        return convertView;
    }





    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }


    }

    private static class ViewHolder {
        private TextView name;
       // private TextView authorName;
        private ImageView image;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.imgaeinfotxt);
            image = (ImageView) v.findViewById(R.id.thumbnail);
           // authorName = (TextView) v.findViewById(R.id.author);
        }
    }

}