package com.quizence.quizence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CollationSpinnerAdapter extends BaseAdapter {

    private String[] mItems = {};
    private Context mContext;

    CollationSpinnerAdapter(String[] items, Context context){
        mItems = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public String getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.spinner_design_dropdown, parent, false);
        }

        TextView numberingTextView = convertView
                .findViewById(R.id.spinner_design_dropdown_numbering);
        numberingTextView.setText(String.valueOf(position + 1));

        TextView itemTextView = convertView
                .findViewById(R.id.spinner_design_dropdown_item);
        itemTextView.setText(getItem(position));

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.spinner_design, parent, false);
        }

        TextView itemTextView = convertView
                .findViewById(R.id.spinner_design_item);
        itemTextView.setText(getItem(position));

        return convertView;
    }
}
