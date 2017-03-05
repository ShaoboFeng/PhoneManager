package com.zachary.zacharymanagerapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.zachary.zacharymanagerapp.provider.AppConstant.ITEM;
import static com.zachary.zacharymanagerapp.provider.AppConstant.ITEM_ID;

public class ZacharyProvider extends ContentProvider {

    private DBOpenHelper dbOpenHelper;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AppConstant.APP,"app",ITEM);
        uriMatcher.addURI(AppConstant.APP,"app/#",ITEM_ID);
    }
    private static final String CONTEN_TYPE = "vnd.android.cursor.item/vnd.zachary.app";
    private static final String TABLE = "app";
    
    @Override
    public boolean onCreate() {
        this.dbOpenHelper = new DBOpenHelper(this.getContext()); 
        return false;   
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.e("zachary","query");
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();    
        return db.query("app", projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        Log.e("zachary","getType");
        switch(uriMatcher.match(uri)) {
            case ITEM:
                return CONTEN_TYPE; 
            default:
                throw new IllegalArgumentException("Error Uri" + uri);
               
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.e("zachary","insert");
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.insert(TABLE,null,values);
        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.e("zachary","update uri:" + uri);
        int count = 0;
        switch(uriMatcher.match(uri)) {
            case ITEM:
                String id = uri.getPathSegments().get(1);
                Log.e("zachary","update id:" + id);
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                count = db.update("app", values, AppConstant.ID + "=" + id, selectionArgs);
            default:
                //throw new IllegalArgumentException("Error Uri" + uri);
        }
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.e("zachary","delete");
        return 0;
    }

}
