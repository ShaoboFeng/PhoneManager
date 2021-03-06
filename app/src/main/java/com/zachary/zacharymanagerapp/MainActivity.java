package com.zachary.zacharymanagerapp;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.zachary.zacharymanagerapp.provider.AppConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView applist;
    static Context context;
    static NormalStartupInfoAdapter adapter;

    static final int ADDPACKAGE = 1;
    static public Handler  myHandler=new  Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case  ADDPACKAGE :
                    Log.e("zachary","install package");
                    if(adapter==null){return ;}
                    List<AppInfo> mList = new ArrayList<AppInfo>();
                    ContentResolver resolver = context.getContentResolver();
                    String[] projection = new String[] {
                            AppConstant.ID,AppConstant.APPNAME,AppConstant.ALLOWSTOP
                    };
                    PackageManager pm = context.getPackageManager();
                    Cursor cursor= resolver.query(AppConstant.Content_Uri,projection,null,null,null);
                    if(cursor.moveToFirst()){
                        do{
                            mList.add( new AppInfo(pm, cursor) );
                        }while(cursor.moveToNext());
                    }
                    cursor.close();
                    adapter.swapData(mList);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        adapter = new NormalStartupInfoAdapter(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //startService(new Intent(this, CheckMemService.class));
    }
    private void getData(){
        List<AppInfo> mList = new ArrayList<AppInfo>();
        ContentResolver resolver = getContentResolver();
        String[] projection = new String[] {
            AppConstant.ID,AppConstant.APPNAME,AppConstant.ALLOWSTOP
        };
        PackageManager pm = this.getPackageManager();
        Cursor cursor= resolver.query(AppConstant.Content_Uri,projection,null,null,null);
        if(cursor.moveToFirst()){
            do{
                mList.add( new AppInfo(pm, cursor) );
            }while(cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(mList);
        adapter.swapData(mList);
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

        //noinspection SimplifiablIfStatement
        if (id == R.id.action_settings) {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(info);
            long mem = info.availMem/1024/1024;
            Toast.makeText(this, "current mem:" + mem + "M", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private WifiInfo getWifiInfo(){
        WifiManager wifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wifi_info) {
            // Handle the wifi info notify systemserver 
            WifiInfo wifiInfo = getWifiInfo();
            Log.w("zachary","sendBroadcast get Wifi Info:" + wifiInfo.toString());
            Intent intent = new Intent();
            intent.setAction("zachary.getWifiInfo");
            intent.putExtra("ssid",wifiInfo.getSSID());
            sendBroadcast(intent);
        } else if (id == R.id.nav_protect) {
            applist = (ListView)findViewById(R.id.applist);
            getData();
            applist.setAdapter(adapter);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
