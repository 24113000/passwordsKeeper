package com.sbezgin.passwordskeeper.activity.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sbezgin.passwordskeeper.activity.main.MainActivity;

import java.util.List;

/**
 * Created by sbezgin on 12.06.2016.
 */
public class SimpleListAdapter extends ArrayAdapter {

    private final String groupName;

    public SimpleListAdapter(Context context, Object[] objects, String groupName) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.groupName = groupName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        if (MainActivity.SUPER_HIDDEN_GROUP.equals(groupName)) {
            text1.setTextColor(Color.RED);
        }
        return view;
    }
}
