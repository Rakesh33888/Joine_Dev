package com.brahmasys.bts.joinme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.brahmasys.bts.joinme.MainActivity;
import com.brahmasys.bts.joinme.R;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by NgocTri on 4/9/2016.
 */
public class GCMPushReceiverService extends GcmListenerService {
    String type,userId,activity_id,time,title="joinme";
    public static final String CHAT_ROOM_OPEN="chat_room_open";
    public static final String USERID = "userid";
    SharedPreferences chat_room,user_id;
    SharedPreferences.Editor edit_chat_room,edit_userid;
    @Override
    public void onMessageReceived(String from, Bundle data) {

        chat_room = getSharedPreferences(CHAT_ROOM_OPEN, MODE_PRIVATE);
        edit_chat_room = chat_room.edit();

        user_id =getSharedPreferences(USERID, MODE_PRIVATE);
        edit_userid = user_id.edit();

        JSONObject json=null;
        try {
             json = new JSONObject(String.valueOf(data.getString("payload")));
            Log.e("RESULT",String.valueOf(json));
            activity_id = json.getString("activtyid");
            userId = json.getString("userid");
            type = json.getString("type");
            time = json.getString("time");
            title = json.getString("title");
            Log.e("ID's",activity_id+"\t"+userId+"\t"+type);
            Log.e("Notification Response",String.valueOf(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String message = data.getString("message");
        if (type.equals("chat"))
        {
            userId=user_id.getString("userid","null");
            if (chat_room.getString("chat_room","null").equals("open"))
            {
                if (!chat_room.getString("chat_activity","null").equals(activity_id)) {
                    sendNotification(message);
                }

            }else {
                sendNotification(message);
            }
        }
        else
        {
            sendNotification(message);
        }


    }
    private void sendNotification(String message) {
        Intent intent = new Intent(this, Screen16.class);
               intent.putExtra("type", type);
               intent.putExtra("activity_id",activity_id);
               intent.putExtra("user_id",userId);
               intent.putExtra("time",time);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;//Your request code
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
     //   PendingIntent intent = PendingIntent.getActivity(context, nid,notificationIntent, 0);
        //Setup notification
        //Sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Build notification
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.mse_mult_line);
        contentView.setImageViewResource(R.id.icon, R.mipmap.icon_web);
        contentView.setTextViewText(R.id.title, title);
        contentView.setTextViewText(R.id.multi_line, message);

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_web)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_web))
                .setContent(contentView)
                .setAutoCancel(true)
                .setSound(sound)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);

        Log.e("Message",message);
        Log.e("USERID",userId);
       // PendingIntent  pendingIntent1 =  PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m, noBuilder.build()); //0 = ID of notification
    }
}
