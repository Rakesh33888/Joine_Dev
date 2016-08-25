package com.brahmasys.bts.joinme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
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

    private ArrayList<Book> books;
    private ArrayAdapter<Book> adapter;
    Context context;
    public static final String USERID = "userid";
    SharedPreferences user_id,user_Details,user_pic,lat_lng;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic,edit_lat_lng;

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

        context=getActivity();
        groups_list =  (Expandable_GridView) v.findViewById(R.id.group_grid);
        groups_list.setExpanded(true);
       setListViewAdapter();


       /* image= (ImageView) v.findViewById(R.id.groupinfoscreen);
        image.setClickable(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentManager=getFragmentManager();
//
//                    Screen17 screen17=new Screen17();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.flContent,screen17)
//                            .addToBackStack(null)
//                            .commit();


            }
        });*/
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


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.37.136.238/JoinMe/Activity.svc/GetUserGroups/" + user_id.getString("userid","0"),
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'

                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //  prgDialog.hide();
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);

                            //Log.w("details", String.valueOf(obj));
                            JSONObject json = null;
                            try {
                                json = new JSONObject(String.valueOf(obj));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            List<String> allid = new ArrayList<String>();
                            List<String> allurl = new ArrayList<String>();


                            JSONArray userdetails = null;
                            try {
                                userdetails = json.getJSONArray("groups");

                                for (int i = 0; i < userdetails.length(); i++) {
                                    JSONObject actor = userdetails.getJSONObject(i);
                                    String id = actor.getString("activity_title");
                                    String url = actor.getString("activity_pic");
                                    allid.add(id);
                                    allurl.add(url);
                                    Book book = new Book();
                                    book.setName(actor.getString("activity_title"));
                                    book.setImageUrl(actor.getString("activity_pic"));
                                    books.add(book);
                                    //   Toast.makeText(Login_Activity.this, pet_id, Toast.LENGTH_SHORT).show();
                                    Log.d("Type", actor.getString("activity_pic"));
                                   // Log.d("Type", userdetails.getString(i));
                                }
                                adapter.notifyDataSetChanged();

                                //Log.w("details",String.valueOf(userdetails));
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
