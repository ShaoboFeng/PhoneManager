package com.zachary.zacharymanagerapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected final List<T> mList = new ArrayList<T>();

    /**
     * Cache the LayoutInflate to avoid asking for a new one each time.
     */
    protected final LayoutInflater mInflater;

    protected final Context mContex;

    public CommonAdapter(Context context){
        mContex = context;
        mInflater = LayoutInflater.from(context);
    }

    public CommonAdapter(Context context, LayoutInflater inflater) {
        mContex = context;
        if (inflater == null) {
            mInflater = LayoutInflater.from(mContex);
        } else {
            mInflater = inflater;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = mList.get(position);
        if (convertView == null) {
            convertView = newView(position, parent, item);
        }
        bindView(position, convertView, item);
        return convertView;
    }

    /**
     * Swap the adapter data, will clear current data and put all item in list
     * into adapter data
     * 
     * @param list if list is null,just clear all adapter data
     * @return always true
     */
    public boolean swapData(List<? extends T> list) {
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
        return true;
    }

    /**
     * Removes the first occurrence of the specified object from this List.
     * 
     * @param item the item will be removed
     * @return true if this adapter content was modified by this operation,
     *         false otherwise
     */
    public boolean deleteItem(T item) {
        boolean res = mList.remove(item);
        if (res) {
            notifyDataSetChanged();
        }
        return res;
    }

    public void deleteItem(Collection<T> items) {
        mList.removeAll(items);
        notifyDataSetChanged();
    }

    /**
     * Get the list of adapter .Return a unmodifiableList
     * 
     * @return
     */
    public List<T> getData() {
        return Collections.unmodifiableList(mList);
    }

    /**
     * Get inflater of adapter
     * @return
     */
    public LayoutInflater getInflater() {
        return mInflater;
    }

    /**
     * Get the context of adapter
     * @return
     */
    public Context getContext() {
        return mContex;
    }

    public Resources getResources() {
        return mContex.getResources();
    }

    public String getString(int resId) {
        return mContex.getString(resId);
    }

    public String getString(int resId, Object... formatArgs) {
        return mContex.getString(resId, formatArgs);
    }

    /**
     * Clear all data the adapter hold
     */
    public void clear() {
        swapData(null);
    }

    /**
     * Makes a new view to hold the data pointed to by item.
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param parent The parent to which the new view is attached to
     * @param item The specify item data at this position
     * @return the newly created view.
     */
    protected abstract View newView(int position, ViewGroup parent, T item);

    /**
     * Bind an existing view to the data pointed to by item
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param view Existing view, returned earlier by newView
     * @param item The specify item data at this position
     */
    protected abstract void bindView(int position, View view, T item);

}
