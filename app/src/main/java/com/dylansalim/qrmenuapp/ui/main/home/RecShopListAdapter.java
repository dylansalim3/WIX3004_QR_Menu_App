package com.dylansalim.qrmenuapp.ui.main.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.Shop;

import java.util.List;

public class RecShopListAdapter extends RecyclerView.Adapter<RecShopListAdapter.MyViewHolder> {

    private List<Shop> mDatas;

    private Context mContext;

    private LayoutInflater inflater;

    private OnItemClickListener listener;


    public RecShopListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public void fillData(List<Shop> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override

    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_shop_rec_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Shop shop = mDatas.get(position);
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(shop.getId()));
        }
        holder.tvName.setText(shop.getName());
        String address = shop.getCountry() + "·" + shop.getCity() + "·" + shop.getAddress();
        holder.tvDetail.setText(address);
        holder.tvRating.setText(String.valueOf(shop.getAverage_rating()));

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetail, tvRating;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_shop_name);
            tvDetail = view.findViewById(R.id.tv_detail);
            tvRating = view.findViewById(R.id.tv_rating);
            itemView = view;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int storeId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
