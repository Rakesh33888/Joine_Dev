package com.brahmasys.bts.joinme;

/**
 * Created by apple on 29/04/16.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SwipeStackAdapter extends ArrayAdapter<Book> {

  public MainActivity activity;

    public SwipeStackAdapter(Activity activity, int resource, List<Book> books) {
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
            convertView = inflater.inflate(R.layout.card, parent, false);
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
        holder.time.setText(book.getTime());
        Picasso.with(getContext()).load("http://52.37.136.238/JoinMe/" + book.getIcon_image()).into(holder.icon);
        Picasso.with(getContext()).load("http://52.37.136.238/JoinMe/" + book.getImageUrl()).fit().noFade().centerCrop()
                .into(holder.image);
        //new DownloadImageTask(holder.image).execute("http://52.37.136.238/JoinMe/" + book.getImageUrl());
       /// pd.hide();
        return convertView;
    }

    private static class ViewHolder {
        private TextView name,time;
         private TextView authorName;
        private ImageView image,icon;

        public ViewHolder(View v) {
             name = (TextView) v.findViewById(R.id.textView14);
            image = (ImageView) v.findViewById(R.id.textViewCard);
            authorName = (TextView) v.findViewById(R.id.textView15);
            time = (TextView) v.findViewById(R.id.textView16);
            icon = (ImageView) v.findViewById(R.id.imageView4);

        }
    }

}