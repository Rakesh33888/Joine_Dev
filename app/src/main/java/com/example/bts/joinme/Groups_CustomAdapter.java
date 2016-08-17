package com.example.bts.joinme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



class Groups_CustomAdapter extends BaseAdapter {
   String [] result;
    String [] message;
    FragmentManager fragmentManager;
   Context context;
    Fragment fragment = null;
    Class fragmentClass = null;
   int [] imageId;

   private static LayoutInflater inflater=null;
   public Groups_CustomAdapter(FragmentActivity mainActivity, String[] prgmNameList, int[] prgmImages,String[] messageList ) {
       // TODO Auto-generated constructor stub
       result=prgmNameList;
       context=mainActivity;
       message = messageList;
       imageId=prgmImages;
       inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

   }

   public int getCount() {
       // TODO Auto-generated method stub
       return result.length;
   }


   public Object getItem(int position) {
       // TODO Auto-generated method stub
       return position;
   }


   public long getItemId(int position) {
       // TODO Auto-generated method stub
       return position;
   }



   public class Holder
   {
       TextView tv,message ;
       ImageView img;
   }
   @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
       // TODO Auto-generated method stub
       Holder holder=new Holder();
       View rowView;
       rowView = inflater.inflate(R.layout.custom_message, null);
       holder.tv=(TextView) rowView.findViewById(R.id.textView1);
       holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
       holder.message =(TextView) rowView.findViewById(R.id.message);



       holder.tv.setText(result[position]);
       holder.img.setImageResource(imageId[position]);
       holder.message.setText(message[position]);

       fragmentClass = Single_group_Message.class;
       rowView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
              // Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
               //Toast.makeText(context, "Under Process" , Toast.LENGTH_LONG).show();
                //Intent i = new Intent(context,Single_group_Message.class);
                //context.startActivity(i);

               Single_group_Message fragment2 = new Single_group_Message();
               FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.flContent, fragment2);
               fragmentTransaction.commit();

           }
       });
       return rowView;
   }

}