package com.dylansalim.qrmenuapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.SpinnerDto;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SpinnerDto> {


    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<SpinnerDto> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.algorithm_spinner, parent, false);
//        }
//
//        TextView textViewName = convertView.findViewById(R.id.text_view);
//        AlgorithmItem currentItem = getItem(position);
//
//        // It is used the name to the TextView when the
//        // current item is not null.
//        if (currentItem != null) {
//            textViewName.setText(currentItem.getAlgorithmName());
//        }
        return convertView;
    }
}
