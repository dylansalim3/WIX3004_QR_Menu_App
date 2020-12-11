package com.dylansalim.qrmenuapp.ui.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dylansalim.qrmenuapp.R;

import java.util.List;


public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {
    private List<ScreenItem> mListScreen;

    public OnboardingAdapter(List<ScreenItem> mListScreen){this.mListScreen = mListScreen;}

    @NonNull
    @Override
    public OnboardingAdapter.OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingAdapter.OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_onboarding_screen, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingAdapter.OnboardingViewHolder holder, int position) {
        holder.setOnBoardingData(mListScreen.get(position));
        int size = mListScreen.size();
        if(position == size - 1){
            holder.textTitle.setVisibility(View.INVISIBLE);
        }else {
            holder.textTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mListScreen.size();
    }

    public class OnboardingViewHolder extends RecyclerView.ViewHolder{
        ImageView imgOnboarding;
        TextView textTitle;


        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOnboarding = itemView.findViewById(R.id.imgOnboarding);
            textTitle = itemView.findViewById(R.id.textTitle);
        }

        void  setOnBoardingData(ScreenItem screenItem){
            textTitle.setText(screenItem.getTitle());
            imgOnboarding.setImageResource(screenItem.getImage());
        }
    }
}
