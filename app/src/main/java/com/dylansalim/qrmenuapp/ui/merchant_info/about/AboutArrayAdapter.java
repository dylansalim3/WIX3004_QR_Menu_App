package com.dylansalim.qrmenuapp.ui.merchant_info.about;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.AboutListItem;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AboutArrayAdapter extends ArrayAdapter<AboutListItem[]> {
    private final Activity context;
    private final AboutListItem[] values;
    private static final String TAG = "aaa";

    public AboutArrayAdapter(Activity context, AboutListItem[] values) {
        super(context, R.layout.layout_about_list_item, Collections.singletonList(values));
        this.context = context;
        this.values = values;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_about_list_item, null, true);

        TextView mTitleText = (TextView) rowView.findViewById(R.id.about_list_item_title);
        TextView mDescText = (TextView) rowView.findViewById(R.id.about_list_item_desc);
        ImageView mIcon = (ImageView) rowView.findViewById(R.id.about_list_item_icon);

        AboutListItem aboutListItem = values[position];

        StringBuilder descStringBuilder = new StringBuilder("");
        if (null != aboutListItem.getDesc() && aboutListItem.getDesc().length > 0) {

            for (String descPart : aboutListItem.getDesc()) {
                descStringBuilder.append(descPart);
                descStringBuilder.append("\n");
            }
            mDescText.setVisibility(View.VISIBLE);
            mDescText.setText(descStringBuilder.toString());
        }

        mTitleText.setText(aboutListItem.getTitle());
        mIcon.setImageDrawable(aboutListItem.getIcon());

        return rowView;
    }

    @Override
    public int getCount() {
        return values.length;
    }
}
