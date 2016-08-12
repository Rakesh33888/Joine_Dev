package com.example.bts.joinme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
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
    ImageView navimage, logo, msg;
    TextView navtextview;
    Button create;
    RelativeLayout reltivelayoutlogo, relativeLayoutmsg;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen16);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        fillWithTestData();
    }

    private void fillWithTestData() {
        for (int x = 0; x < s.length; x++) {
            mData.add(s[x]);
        }

        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.flContent);
        FrameLayout frameLayoutimagebtn = (FrameLayout) findViewById(R.id.imageButton_swipe);
        FrameLayout frameLayout2 = (FrameLayout) findViewById(R.id.frameLayout2);
        ImageView activityimage = (ImageView) findViewById(R.id.imageView4);
        TextView textViewactivity = (TextView) findViewById(R.id.textView14);
        TextView textViewactivity1 = (TextView) findViewById(R.id.textView15);
        TextView textViewactivity2 = (TextView) findViewById(R.id.textView16);
        Button btninfo = (Button)findViewById(R.id.infobutton);
        btninfo.setClickable(true);
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentManager=getSupportFragmentManager();
//                Screen17 screen17=new Screen17();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.flContent,screen17)
//                            .addToBackStack(null)
//                            .commit();



            }
        });
        Button userchatadd = (Button) findViewById(R.id.button14);
        Button notintrested = (Button) findViewById(R.id.button13);
        logo = (ImageView) findViewById(R.id.logo);
        create= (Button)findViewById(R.id.createnewactivity);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager=getSupportFragmentManager();

//                    Screen19 screen19=new Screen19();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.flContent,screen19)
//                            .addToBackStack(null)
//                            .commit();


            }
        });

        reltivelayoutlogo = (RelativeLayout) findViewById(R.id.custmtool);
        relativeLayoutmsg = (RelativeLayout) findViewById(R.id.msssg);
        msg = (ImageView) findViewById(R.id.msg);
        msg.setClickable(true);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Messagescreen messagescreen=new Messagescreen();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.flContent,messagescreen)
//                        .addToBackStack(null)
//                        .commit();



            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navimage = (ImageView) header.findViewById(R.id.imageViewab);
        navtextview = (TextView) header.findViewById(R.id.navtextview);
        navtextview.setText("Ajay");
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

//
//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        View v=onCreateView(name, context, attrs);
//
//        return v;
//
//
//    }

    //
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.msg) {
//
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//   }
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
