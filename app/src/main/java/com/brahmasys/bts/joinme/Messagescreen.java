package com.brahmasys.bts.joinme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Messagescreen extends Fragment{
    ListView groups_listView;
    ImageView back;
    LinearLayout backlayoutmessge;
    FragmentManager fragmentManager;
    public static final String USERID = "userid";
    SharedPreferences user_id,user_Details,user_pic,lat_lng;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic,edit_lat_lng;

    ProgressDialog progressDialog;
    private ArrayList<Book> books;
    private ArrayAdapter<Book> adapter;
    Context context;
    List<String> allid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_messagescreen,container,false);

        progressDialog =ProgressDialog.show(getActivity(), null, null, true);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        groups_listView = (ListView) v.findViewById(R.id.listView);
        back = (ImageView) v.findViewById(R.id.back);
        backlayoutmessge= (LinearLayout) v.findViewById(R.id.backlayoutmessge);
        backlayoutmessge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
            }
        });
        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
         back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        context=getActivity();
         setListViewAdapter();




        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.37.136.238/JoinMe/Activity.svc/GetUserGroups/" + user_id.getString("userid","000"),
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



                            JSONArray cast = json.getJSONArray("groups");


                            //  Toast.makeText(Login_Activity.this, String.valueOf(cast.length()), Toast.LENGTH_SHORT).show();
                              allid = new ArrayList<String>();
                           // List<String> allurl = new ArrayList<String>();

                            for (int i = 0; i < cast.length(); i++) {
                                JSONObject actor = cast.getJSONObject(i);
                                String ac_id = actor.getString("activityid");
                              //  String url = actor.getString("url");
                                  allid.add(ac_id);
                               // allurl.add(url);

                                Book book = new Book();
                                book.setName(actor.getString("activity_title"));
                                book.setImageUrl(actor.getString("activity_pic"));
                                book.setAuthorName(actor.getString("activity_massege"));
                                books.add(book);

                                Log.d("Type", cast.getString(i));
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        groups_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                fragmentManager = getFragmentManager();

                Single_group_Message single_group_message = new Single_group_Message();
                Bundle bundle = new Bundle();
               // bundle.putString("userid", alluserid.get(position));
                bundle.putString("activityid", allid.get(position));
                single_group_message.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, single_group_message)
                        .addToBackStack(null)
                        .commit();

            }
        });

            return v;
    }
    private void setListViewAdapter() {
        books = new ArrayList<Book>();
        adapter = new Groups_CustomAdapter(getActivity(), R.layout.custom_message, books);
        groups_listView.setAdapter(adapter);

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
                    Intent i = new Intent(getActivity(), Screen16.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }
}

