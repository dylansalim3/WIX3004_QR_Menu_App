package com.dylansalim.qrmenuapp.ui.merchant_info.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.RatingListItem;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewArrayAdapter extends RecyclerView.Adapter<ReviewArrayAdapter.ReviewViewHolder> {
    private List<RatingListItem> ratingListItems;

    public ReviewArrayAdapter() {
    }

    public ReviewArrayAdapter(List<RatingListItem> ratingListItems) {
        this.ratingListItems = ratingListItems;
    }

    public void fillData(List<RatingListItem> ratingListItems) {
        this.ratingListItems = ratingListItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rating_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        RatingListItem ratingListItem = ratingListItems.get(position);
        if (null != ratingListItem.getDate()) {
            holder.mDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(ratingListItem.getDate()));
        }
        holder.mReviewerName.setText(ratingListItem.getReviewerName());
        holder.mDesc.setText(ratingListItem.getDesc());
        holder.mRating.setText(String.valueOf(ratingListItem.getRating()));
    }

    @Override
    public int getItemCount() {
        if (null != ratingListItems) {
            return ratingListItems.size();
        }
        return 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewerName, mDate, mDesc, mRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mReviewerName = itemView.findViewById(R.id.tv_rating_item_list_title);
            mDate = itemView.findViewById(R.id.tv_rating_item_list_date);
            mDesc = itemView.findViewById(R.id.tv_rating_item_list_desc);
            mRating = itemView.findViewById(R.id.tv_rating_item_list_rating);


        }
    }
}
