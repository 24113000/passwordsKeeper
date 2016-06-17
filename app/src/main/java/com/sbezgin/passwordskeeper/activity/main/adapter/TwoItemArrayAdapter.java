package com.sbezgin.passwordskeeper.activity.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sbezgin.passwordskeeper.R;
import com.sbezgin.passwordskeeper.activity.main.MainActivity;
import com.sbezgin.passwordskeeper.service.properties.PropertiesDataHolder;
import com.sbezgin.passwordskeeper.service.properties.PropertyDTO;
import com.sbezgin.passwordskeeper.utils.Utils;

import java.util.List;
import java.util.Objects;

/**
 * Created by sbezgin on 11.06.2016.
 */
public class TwoItemArrayAdapter extends ArrayAdapter {
    private final String groupName;

    public TwoItemArrayAdapter(Context context, Object[] objects, String groupName) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        this.groupName = groupName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        text1.setTextColor(Color.GRAY);
        TextView text2 = (TextView) view.findViewById(android.R.id.text2);
        PropertyDTO dto = (PropertyDTO) getItem(position);
        text1.setText(dto.getKey());
        text2.setText(dto.getValue());
        if (Utils.SUPER_HIDDEN_GROUP.equals(groupName)) {
            text2.setTextColor(Color.RED);
        }
        return view;
    }
}
