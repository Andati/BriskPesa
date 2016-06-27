package com.briskpesa.briskpesademo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.briskpesa.briskpesademo.fragments.AboutFragment;
import com.briskpesa.briskpesademo.fragments.HelpFragment;
import com.briskpesa.briskpesademo.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DBHelper mydb;
    String phone;

    FrameLayout mContentFrame;

    private boolean mUserLearnedDrawer;
    private int mCurrentSelectedPosition;

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mydb = new DBHelper(this);
        //check if there is phone number
        phone = mydb.getPhoneNumber();
        if(phone == null) {
            //get phone number
            startActivity(new Intent(this, PhoneActivity.class));
            this.finish();
            return;
        }

        mCurrentSelectedPosition = Integer.valueOf(Utils.readSharedSetting(this, STATE_SELECTED_POSITION, "" + R.id.nav_home));
        mUserLearnedDrawer = Boolean.valueOf(Utils.readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));
        mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView phoneText = (TextView) header.findViewById(R.id.phone_text);
        phoneText.setText(phone);

        if (!mUserLearnedDrawer) {
            drawer.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            Utils.saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
        }

        int pos = mCurrentSelectedPosition == R.id.nav_home ? 0 : -1;
        if (pos == -1) {
            pos = mCurrentSelectedPosition == R.id.nav_about ? 1 : 2;
        }
        navigationView.getMenu().getItem(pos).setChecked(true);
        showFragment(mCurrentSelectedPosition);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            showFragment(R.id.nav_home);
        } else if (id == R.id.nav_about) {
            showFragment(R.id.nav_about);
        } else if (id == R.id.nav_help) {
            showFragment(R.id.nav_help);
        } else if (id == R.id.nav_share) {
            share();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Bundle data = new Bundle();
        switch (position) {
            case R.id.nav_home:
                mCurrentSelectedPosition = R.id.nav_home;
                Utils.saveSharedSetting(this, STATE_SELECTED_POSITION, "" + R.id.nav_home);
                HomeFragment hFragment = new HomeFragment();
                hFragment.setArguments(data);
                ft.replace(R.id.nav_contentframe, hFragment);
                getSupportActionBar().setTitle("BriskPesa Demo");
                ft.commit();
                break;
            case R.id.nav_about:
                mCurrentSelectedPosition = R.id.nav_about;
                Utils.saveSharedSetting(this, STATE_SELECTED_POSITION, "" + R.id.nav_about);
                AboutFragment aFragment = new AboutFragment();
                aFragment.setArguments(data);
                ft.replace(R.id.nav_contentframe, aFragment);
                getSupportActionBar().setTitle("About BriskPesa");
                ft.commit();
                break;
            case R.id.nav_help:
                mCurrentSelectedPosition = R.id.nav_help;
                Utils.saveSharedSetting(this, STATE_SELECTED_POSITION, "" + R.id.nav_help);
                HelpFragment helpFragment = new HelpFragment();
                helpFragment.setArguments(data);
                ft.replace(R.id.nav_contentframe, helpFragment);
                getSupportActionBar().setTitle("Help");
                ft.commit();
                break;
            default:
                break;
        }
    }

    public void share(){
        try { Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share));
            startActivity(Intent.createChooser(i, "Choose one"));
        }
        catch(Exception e) {
            //e.toString();
        }
    }
}
