package com.brahmasys.bts.joinme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brahmasys.bts.joinme.R;
import com.devsmart.android.ui.HorizontalListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mygroup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mygroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mygroup extends Fragment{
    Expandable_GridView groups_list;
    Button  createalbum;
    ImageView image,imageback1;
    FragmentManager fragmentManager;
    private static final String TAG = "GetMyGroupActivity";
    private static final String URL = "http://52.37.136.238/JoinMe/Activity.svc/GetMyGroupActivity";

    private ArrayList<Book> books;
    private ArrayAdapter<Book> adapter;
    Context context;
    public static final String USERID = "userid";
    SharedPreferences user_id,user_Details,user_pic,lat_lng;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic,edit_lat_lng;

    List<String> allactivityid;
    List<String> alluserid;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Mygroup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mygroup.
     */
    // TODO: Rename and change types and number of parameters
    public static Mygroup newInstance(String param1, String param2) {
        Mygroup fragment = new Mygroup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
     View v= inflater.inflate(R.layout.fragment_mygroup, container, false);
        user_id =getActivity().getSharedPreferences(USERID, getActivity().MODE_PRIVATE);
        edit_userid = user_id.edit();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        context=getActivity();
        groups_list =  (Expandable_GridView) v.findViewById(R.id.group_grid);
        groups_list.setExpanded(true);
       setListViewAdapter();



        imageback1= (ImageView) v.findViewById(R.id.backtogroupsetting);
        imageback1.setClickable(true);
        imageback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Screen16.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
                getActivity().finish();
            }
        });
        createalbum= (Button) v.findViewById(R.id.createnewactivity);
        createalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager=getFragmentManager();

                    Screen19 screen19=new Screen19();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent,screen19)
                            .addToBackStack(null)
                            .commit();
                }
        });



        JSONObject jsonObjSend = new JSONObject();
        try {
            // Add key/value pairs
            jsonObjSend.put("lat", "0");
            jsonObjSend.put("lon", "0");
            jsonObjSend.put("userid",user_id.getString("userid","0"));
            Log.i(TAG, jsonObjSend.toString(3));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObjRecv = HttpClient.SendHttpPost(URL, jsonObjSend);

            //Log.w("RESULT",String.valueOf(jsonObjRecv));
       JSONObject json = null;
        try {
            json = new JSONObject(String.valueOf(jsonObjRecv));
        } catch (JSONException e) {
            e.printStackTrace();
        }
         alluserid = new ArrayList<String>();
         allactivityid = new ArrayList<String>();


        JSONArray userdetails = null;
        try {
            userdetails = json.getJSONArray("data");

            for (int i = 0; i < userdetails.length(); i++) {
                JSONObject actor = userdetails.getJSONObject(i);
                String activity_id = actor.getString("activity_id");
                String user_id = actor.getString("userid");
                allactivityid.add(activity_id);
                alluserid.add(user_id);
                Book book = new Book();
                book.setName(actor.getString("activity_name"));
                book.setImageUrl(actor.getString("activity_url"));


                long timestampString =  Long.parseLong(actor.getString("activity_time"));
                String value = new java.text.SimpleDateFormat("dd.MM.yyyy 'at' KK aa ").
                        format(new java.util.Date(timestampString * 1000));

                book.setAuthorName(value);

                books.add(book);

            }
            adapter.notifyDataSetChanged();

            //Log.w("details",String.valueOf(userdetails));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        groups_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


               ;
                fragmentManager=getFragmentManager();

                Screen17 screen17 = new Screen17();
                Fragment fragment = new Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("userid", alluserid.get(position));
                bundle.putString("activityid",allactivityid.get(position));
                screen17.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, screen17)
                        .addToBackStack(null)
                        .commit();

            }
        });


        return v;
    }

    private void setListViewAdapter() {
        books = new ArrayList<Book>();
        adapter = new CustomListViewAdapter(getActivity(), R.layout.groups_list, books);
        groups_list.setAdapter(adapter);

    }




    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
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
