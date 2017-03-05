package com.zachary.zacharymanagerapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zachary.zacharymanagerapp.provider.AppConstant;

/**
 * Created by shaobo on 2016/10/30.
 */

public class NormalStartupInfoAdapter extends CommonAdapter<AppInfo> {
    public static final String APP = "zachary.provider";
    public static final Uri Content_Uri = Uri.parse("content://" + APP + "/app");

    private ContentResolver resolver;
    private Context mContext;
    public NormalStartupInfoAdapter(Context context) {
        super(context);
        mContext = context;
        resolver = context.getContentResolver();
    }

    @Override
    protected View newView(int position, ViewGroup parent, AppInfo item) {
        View convertView = mInflater.inflate(R.layout.applist, parent, false);
        CommonStartupViewHolder holder = new CommonStartupViewHolder();
        holder.mIcon = (ImageView) convertView.findViewById(R.id.image);
        holder.mTitle = (TextView) convertView.findViewById(R.id.textView);
        //holder.mDescription = (TextView) convertView.findViewById(R.id.textView2);
        holder.mSwitch = (ToggleButton) convertView.findViewById(R.id.toggleButton);
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    protected void bindView(final int position, View view, final AppInfo item) {
        CommonStartupViewHolder holder = (CommonStartupViewHolder) view.getTag();
        holder.mIcon.setImageDrawable(item.getIconDrawable());
        //holder.mDescription.setText(item.getStatus() ? "allow": "not allow");
        holder.mTitle.setText(item.getLabel());
        holder.mSwitch.setChecked(item.getStatus());
        holder.mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("zachary",item.getLabel()+"  "+item.getStatus());
                AppInfo appInfo = mList.get(position);
                appInfo.setStatus(!item.getStatus());
                notifyDataSetChanged();
                Uri uri = ContentUris.withAppendedId(Content_Uri,appInfo.getId());
                ContentValues values = new ContentValues();
                values.put(AppConstant.APPNAME,appInfo.getPackage());
                values.put(AppConstant.ALLOWSTOP,appInfo.getStatus() ? 1: 0);
                resolver.update(uri,values,null,null);
				
                //notify systemserver 
                Intent intent = new Intent();
                intent.setAction("zachary.updateZacharyList");
                intent.putExtra("app","add package");
                mContext.sendBroadcast(intent);
            }
        });
    }
}
