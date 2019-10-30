package com.example.sokna.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sokna.R;

import java.util.List;

public class ProfileAdapter extends ArrayAdapter {


    public ProfileAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_profile_item, null);
        }

        String p = (String) getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.listview_tv_item);

            if (tt1 != null) {
                tt1.setText(p);
            }
        }

        return v;


    }
}