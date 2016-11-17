package com.brahmasys.bts.joinme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 07/11/16.
 */

public class Custom_WhoShowedUp extends ArrayAdapter<Book>  {
    SparseBooleanArray mCheckStates;
    public Notification_Screen activity;
    ProgressDialog pd;
    boolean[] itemChecked;
    List<Book> items_check;
    public Custom_WhoShowedUp(Activity activity, int resource, List<Book> books) {
        super(activity, resource, books);
        activity = activity;
        items_check = books;
        itemChecked = new boolean[books.size()];

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.custom_whoshowed, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);

            holder.selection.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Book _state = (Book) cb.getTag();

//                Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
//                        Toast.LENGTH_LONG).show();

                    _state.setSelected(cb.isChecked());
                }
            });
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);

           holder.name.setText(book.getName());
        //  holder.authorName.setText(book.getAuthorName());
        Picasso.with(getContext()).load("http://52.37.136.238/JoinMe/" + book.getImageUrl()).placeholder(R.drawable.butterfly)
                .into(holder.image);
        //new DownloadImageTask(holder.image).execute("http://52.37.136.238/JoinMe/" + book.getImageUrl());

        Book state = items_check.get(position);
       // holder.selection.setText(state.getName());
        holder.selection.setChecked(state.isSelected());
        holder.selection.setTag(state);


        return convertView;
    }



    private static class ViewHolder {
         private TextView name;
        //   private TextView authorName;
        private CircularImageView image;
        CheckBox selection;
        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.user_name);
            image = (CircularImageView) v.findViewById(R.id.user_profile);
            selection = (CheckBox)v.findViewById(R.id.menu_source_check_box);
            //   authorName = (TextView) v.findViewById(R.id.timeinotext);
        }
    }

}

