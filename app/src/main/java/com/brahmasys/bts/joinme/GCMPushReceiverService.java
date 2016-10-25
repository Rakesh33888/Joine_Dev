package com.brahmasys.bts.joinme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.brahmasys.bts.joinme.MainActivity;
import com.brahmasys.bts.joinme.R;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NgocTri on 4/9/2016.
 */
public class GCMPushReceiverService extends GcmListenerService {
    String type,userId,activity_id,time;
    @Override
    public void onMessageReceived(String from, Bundle data) {

        JSONObject json=null;
        try {
             json = new JSONObject(String.valueOf(data.getString("payload")));
            activity_id = json.getString("activtyid");
            userId = json.getString("userid");
            type = json.getString("type");
            time = json.getString("time");
            Log.e("ID's",activity_id+"\t"+userId+"\t"+type);
            Log.e("Notification Responce",String.valueOf(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String message = data.getString("message");

        sendNotification(message);
    }
    private void sendNotification(String message) {
        Intent intent = new Intent(this, Screen16.class);
               intent.putExtra("type", type);
               intent.putExtra("activity_id",activity_id);
               intent.putExtra("user_id",userId);
               intent.putExtra("time",time);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;//Your request code
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        //Setup notification
        //Sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Build notification

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_web)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_web))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(sound)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);
        Log.e("Message",message);
       // PendingIntent  pendingIntent1 =  PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }
}
