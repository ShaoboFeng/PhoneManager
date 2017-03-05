package com.zachary.zacharymanagerapp.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.zachary.zacharymanagerapp.AppInfo;
import com.zachary.zacharymanagerapp.provider.AppConstant;
import com.zachary.zacharymanagerapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaobo on 2016/10/30.
 */

public class CheckMemService extends Service{

    final static int CHECKMEM = 1;
    ActivityManager activityManager = null;
    ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
    Context context = null;

    private Handler myHandler  =new  Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case  CHECKMEM :
                    activityManager.getMemoryInfo(info);
                    long mem = info.availMem/1024/1024;
                    Toast.makeText(context, "current mem:" + mem + "M", Toast.LENGTH_SHORT).show();
                    if(mem < 600) {
                        //activityManager.forceStopPackage("com.android.mms");
                        ContentResolver resolver = getContentResolver();
                        String[] projection = new String[] {
                                AppConstant.ID,AppConstant.APPNAME,AppConstant.ALLOWSTOP
                        };
                        List<AppInfo> mList = new ArrayList<AppInfo>();
                        PackageManager pm = context.getPackageManager();
                        Cursor cursor= resolver.query(AppConstant.Content_Uri,projection,null,null,null);
                        if(cursor.moveToFirst()){
                            do{
                                mList.add( new AppInfo(pm, cursor) );
                            }while(cursor.moveToNext());
                        }
                        cursor.close();
                        List<String> cmdList= new ArrayList<String>();
                        for(AppInfo app :mList){
                            if(!app.getStatus()){
                                if(app.getPackage().indexOf("zachary")>=0){
                                        continue;
                                }
                                String cmd = "am force-stop " + app.getPackage();
                                cmd = cmd.replace("$", "\"" + "$" + "\"");
                                cmdList.add(cmd);
                                Log.d("zachary",cmd);
                                //activityManager.forceStopPackage(app.getPackage());
                                //Toast.makeText(context, "forcestop " + app.getPackage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    break;
            }
        }
    };

    private Thread myThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                SystemClock.sleep(20000);
                Message msg = new Message();
                msg.what = CHECKMEM;
                myHandler.sendMessage(msg);
                //Log.i("zachary","check mem");
            }
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flag, int startid){
        super.onStartCommand(intent,flag,startid);
        context = this;
        activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        myThread.start();
        Log.i("zachary","start serevices");
        return START_STICKY;
    }


    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
