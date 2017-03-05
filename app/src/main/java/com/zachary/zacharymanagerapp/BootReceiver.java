package com.zachary.zacharymanagerapp;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Message;
import android.util.Log;

import com.zachary.zacharymanagerapp.provider.AppConstant;

/**
 * Created by shaobo on 2016/11/16.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            Log.e("zachary","revice:"+intent.getPackage()+"  "+intent.getDataString()) ;
            PackageManager pm = context.getPackageManager();
            String packageName = intent.getDataString().substring(8);
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            AppInfo appInfo = new AppInfo(pm, packageName);
            values.put(AppConstant.ID,appInfo.getId());
            values.put(AppConstant.APPNAME,appInfo.getPackage());
            values.put(AppConstant.ALLOWSTOP,appInfo.getStatus()? 1 : 0);
            resolver.insert(AppConstant.Content_Uri,values);
            Message msg = new Message();
            msg.what = MainActivity.ADDPACKAGE;
            MainActivity.myHandler.sendMessage(msg);
        }
        else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")){
            Log.e("zachary","revice:"+intent.getPackage()+"  "+intent.getDataString()) ;
        }
    }
}
