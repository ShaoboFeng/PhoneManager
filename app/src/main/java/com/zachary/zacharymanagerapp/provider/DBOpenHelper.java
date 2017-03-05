package com.zachary.zacharymanagerapp.provider;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zachary.zacharymanagerapp.AppInfo;

import java.util.List;


public class DBOpenHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "app.db";
    
    private static final int DATABASE_VERSION = 1;
    private Context cxt;

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        cxt = context;
    } 

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE app (" + AppConstant.ID + " integer primary key autoincrement, " + AppConstant.APPNAME + " varchar(200), " + AppConstant.ALLOWSTOP  + " integer)");

        PackageManager pm = cxt.getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        Log.i("zachary","package count:"+packs.size());
        for(PackageInfo pi :packs) {
            AppInfo appInfo = new AppInfo(pm,pi);
            if(appInfo.getLabel()==null){
                continue;
            }
            ContentValues values = new ContentValues();
            values.put(AppConstant.ID,appInfo.getId());
            values.put(AppConstant.APPNAME,appInfo.getPackage());
            values.put(AppConstant.ALLOWSTOP,appInfo.getStatus()? 1 : 0);
            db.insert("app",null,values);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS app");
        onCreate(db);    
    }
    
    
}
