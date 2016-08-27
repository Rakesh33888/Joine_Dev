package com.brahmasys.bts.joinme;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brahmasys.bts.joinme.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringReader;


public class Single_group_Message extends Fragment    {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String USERID = "userid";
    public static final String ACTIVITYID = "activity_id";
    private static final String URL_GetActivityDetailsForChat = "http://52.37.136.238/JoinMe/Activity.svc/GetActivityDetailsForChat/";
    private static final String IMAGE_BASE_URL = "http://52.37.136.238/JoinMe/";


    CircularImageView createrimage;

    SharedPreferences user_id,activity_id;
    SharedPreferences.Editor edit_userid,edit_activity_id;
    TextView tvActivityName,tvActivityTime,tvActivityAddress,tvHostedByName;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Single_group_Message() {
        // Required empty public constructor
    }
   TextView leave_chat,name;
    Custom_dialoge dialoge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_single_group__message, container, false);

        leave_chat = (TextView) v.findViewById(R.id.leave_chat);
        createrimage = (CircularImageView) v.findViewById(R.id.createrimage);
        tvActivityName= (TextView) v.findViewById(R.id.textView25);
        tvActivityTime= (TextView) v.findViewById(R.id.textView26);
        tvActivityAddress = (TextView) v.findViewById(R.id.textView27);
        tvHostedByName = (TextView) v.findViewById(R.id.name);

        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        activity_id = getActivity().getSharedPreferences(ACTIVITYID, getActivity().MODE_PRIVATE);
        edit_activity_id = activity_id.edit();

        leave_chat.setPaintFlags(leave_chat.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        leave_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Custom_dialoge dialogFragment = new Custom_dialoge ();
                dialogFragment.show(getActivity().getFragmentManager(),"simple dialog");


            }
        });

      //  String userid = user_id.getString("userid", "");
        String act_id = activity_id.getString("activity_id", "");
      //  String act_id ="5762432ed72fc30d6853e39b";
        //populate the date from the activity
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_GetActivityDetailsForChat + act_id,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //  prgDialog.hide();
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);


                            JSONObject json = null;
                            try {
                                json = new JSONObject(String.valueOf(obj));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            /************************* UserDetails start **************************/
                            JSONObject apiResponse = null;
                            try {
                                apiResponse = json.getJSONObject("response");
                                Log.i("GetDetailsForChat",String.valueOf(apiResponse));
                                String ststus =apiResponse.getString("status");
                                if(ststus.equals("200")){
                                    JSONArray arrGroup = json.getJSONArray("group_list");

                                    String activutyImage = json.getString("activity_pic");
                                    String txtActivityName = json.getString("activity_title");
                                    Integer txtActivityTime = json.getInt("activity_time");
                                    String txtAddress = json.getString("activity_address");

                                    new DownloadImageTask(createrimage).execute(IMAGE_BASE_URL + activutyImage);
                                    tvActivityName.setText(txtActivityName);
                                    long timestampString =  Long.parseLong(String.valueOf(txtActivityTime));
                                    String value = new java.text.SimpleDateFormat("dd.MM.yyyy 'at' KK aa ").
                                            format(new java.util.Date(timestampString * 1000));

                                    tvActivityTime.setText(String.valueOf(value));


                                    tvActivityAddress.setText(txtActivityName);

                                    for(int i=0; i<arrGroup.length(); i++) {

                                        JSONObject row = arrGroup.getJSONObject(i);
                                        if(row.getBoolean("isowner")){
                                            tvHostedByName.setText(row.getString("user_name"));
                                        }

                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Messagescreen fragment2 = new Messagescreen();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, fragment2);
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
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


}
