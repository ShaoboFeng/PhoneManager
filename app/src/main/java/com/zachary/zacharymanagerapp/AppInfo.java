package com.zachary.zacharymanagerapp;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

/**
 * Created by shaobo on 2016/10/30.
 */

public class AppInfo  implements Comparable{
    protected long mUid = 0;
    protected String mPkgName = null;
    protected Drawable mAppIcon = null;
    protected String mLabel = null;
    protected boolean mStatus = false;


    public AppInfo(PackageManager pm,Cursor cursor) {
        mUid = cursor.getInt(0);
        mPkgName = cursor.getString(1);
        mStatus = cursor.getInt(2) > 0 ? true: false;

        try{
            ApplicationInfo info = pm.getApplicationInfo(mPkgName, 0);
            if((info.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
                return;
            mLabel = info.loadLabel(pm).toString();
            mAppIcon = info.loadIcon(pm);
        }catch (PackageManager.NameNotFoundException e){
            mLabel = mPkgName;
        }
        if(mLabel == null) {
            mLabel = mPkgName;
        }
    }

    public AppInfo(PackageManager pm,  String pkgName) {
        mPkgName = pkgName;
        try{
            ApplicationInfo info = pm.getApplicationInfo(mPkgName, 0);
            mUid = info.uid;
            if((info.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
                return;
            mLabel = info.loadLabel(pm).toString();
            mAppIcon = info.loadIcon(pm);
        }catch (PackageManager.NameNotFoundException e){
            mLabel = mPkgName;
        }
        if(mLabel == null) {
            mLabel = mPkgName;
        }
    }

    public AppInfo(PackageManager pm,  PackageInfo pi) {
        mPkgName = pi.packageName;
        try{
            ApplicationInfo info = pm.getApplicationInfo(mPkgName, 0);
            mUid = info.uid;
            if((info.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
                return;
            mLabel = info.loadLabel(pm).toString();
            mAppIcon = info.loadIcon(pm);
        }catch (PackageManager.NameNotFoundException e){
            mLabel = mPkgName;
        }
        if(mLabel == null) {
            mLabel = mPkgName;
        }
    }

    public Drawable getIconDrawable(){
        return mAppIcon;
    }
    public boolean getStatus() {
        return mStatus;
    }
    public void setStatus(boolean status) {
        mStatus = status;
    }

    public String getLabel(){
        return mLabel;
    }

    public String getPackage() {
        return mPkgName;
    }

    public long getId(){
        return mUid;
    }

    @Override
    public int compareTo(Object o)
    {
        AppInfo app = (AppInfo)o;
        if(app.getStatus() == this.getStatus()){
            return 0;
        }
        if(app.getStatus()==false){
            return -1;
        }
        return 1;
    }
}
