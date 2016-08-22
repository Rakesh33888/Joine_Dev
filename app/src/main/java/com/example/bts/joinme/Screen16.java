package com.example.bts.joinme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import link.fls.swipestack.SwipeStack;

public class Screen16 extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
        ,Mysearch.OnFragmentInteractionListener,
        Mygroup.OnFragmentInteractionListener,
        Appsetting.OnFragmentInteractionListener,SwipeStack.SwipeStackListener {
    private static final int PICK_IMAGE_REQUEST = 2;
    private ArrayList<Integer> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    Integer s[]={R.drawable.army1,R.drawable.army2,R.drawable.army3};
    Toolbar toolbar;
    android.support.v7.app.ActionBar actionBar;
    ImageView navimage, logo, msg,back_nav;
    TextView navtextview;
    ImageView create,like,dislike,btninfo;
    RelativeLayout reltivelayoutlogo, relativeLayoutmsg;
    FragmentManager fragmentManager;

    public static final String USERID = "userid";
    public static final String DETAILS = "user_details";
    public static final String USER_PIC = "user_pic";

    SharedPreferences user_id,user_Details,user_pic;
    SharedPreferences.Editor edit_userid,edit_user_detals,edit_user_pic;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen16);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);

        user_id =getSharedPreferences(USERID, MODE_PRIVATE);
        edit_userid = user_id.edit();
        user_Details = getSharedPreferences(DETAILS, MODE_PRIVATE);
        edit_user_detals = user_Details.edit();
        user_pic = getSharedPreferences(USER_PIC, MODE_PRIVATE);
        edit_user_pic = user_pic.edit();




        fillWithTestData();



    }

    private void fillWithTestData() {
        for (int x = 0; x < s.length; x++) {
            mData.add(s[x]);
        }

        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();





        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);



        btninfo = (ImageView)findViewById(R.id.infobutton);
        btninfo.setClickable(true);
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                Screen17 screen17 = new Screen17();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, screen17)
                        .addToBackStack(null)
                        .commit();


            }
        });

        logo = (ImageView) findViewById(R.id.logo);
        create= (ImageView)findViewById(R.id.createnewactivity);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager=getSupportFragmentManager();
                Screen19 screen19=new Screen19();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent,screen19)
                            .addToBackStack(null)
                            .commit();


            }
        });

        reltivelayoutlogo = (RelativeLayout) findViewById(R.id.custmtool);
        relativeLayoutmsg = (RelativeLayout) findViewById(R.id.msssg);
        msg = (ImageView) findViewById(R.id.msg);
        msg.setClickable(true);

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager=getSupportFragmentManager();
                Messagescreen messagescreen=new Messagescreen();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent,messagescreen)
                        .addToBackStack(null)
                        .commit();

            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navimage = (ImageView) header.findViewById(R.id.imageViewab);
        navtextview = (TextView) header.findViewById(R.id.navtextview);
        back_nav = (ImageView) header.findViewById(R.id.back_nav);
        navtextview.setText(user_Details.getString("firstname","")+"\t"+user_Details.getString("lastname","null"));

        int pic_list_size = Integer.parseInt(user_pic.getString("pic_list_size",""));
        List<String> allid = new ArrayList<String>();
        List<String> allurl = new ArrayList<String>();

        for (int j = 0; j < pic_list_size; j++)
        {
           String id= user_pic.getString("id_" + j, "");
           String url= user_pic.getString("url_" + j, "");

            allid.add(id);
            allurl.add(url);
        }


        for (int i = 0; i< allid.size(); i++) {


            if (i==allurl.size()-1)
            {
                //Toast.makeText(Screen16.this,  allurl.get(i), Toast.LENGTH_SHORT).show();

                new DownloadImageTask(navimage).execute("http://52.37.136.238/JoinMe/" + allurl.get(i));

            }
        }
        navigationView.setNavigationItemSelectedListener(this);
        navimage.setClickable(true);
        navimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.my_search) {
            fragmentClass = Mysearch.class;



        } else if (id == R.id.group_activites) {
            fragmentClass = Mygroup.class;


        } else if (id == R.id.app_setting) {
            fragmentClass = Appsetting.class;


        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                    data != null && data.getData() != null) {

                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    navimage = (ImageView) findViewById(R.id.imageViewab);
                    navimage.setImageBitmap(bitmap);
                } catch (IOException e) {

                }
            }

        }

    @Override
    public void onViewSwipedToLeft(int position) {
        Integer swipedElement = mAdapter.getItem(position);


    }

    @Override
    public void onViewSwipedToRight(int position) {
        Integer swipedElement = mAdapter.getItem(position);

    }



    @Override
    public void onStackEmpty() {

    }
public class SwipeStackAdapter extends BaseAdapter {
    ImageView textViewCard ;

    private List<Integer> mData;

    public SwipeStackAdapter(List<Integer> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Integer getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            convertView =getLayoutInflater().inflate(R.layout.card, parent, false);


        textViewCard = (ImageView) convertView.findViewById(R.id.textViewCard);
        // textViewCard.setText(mData.get(position));
        textViewCard.setImageResource(s[position]);

        return convertView;



}
}
}
