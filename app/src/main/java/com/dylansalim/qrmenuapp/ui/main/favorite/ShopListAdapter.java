package com.dylansalim.qrmenuapp.ui.main.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.Shop;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.MyViewHolder> {

    private List<Shop> mDatas;

    private Context mContext;

    private LayoutInflater inflater;

    private OnItemClickListener listener;


    public ShopListAdapter(Context context) {
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
        View view = inflater.inflate(R.layout.item_shop_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(v, position));
        }
        Shop shop = mDatas.get(position);
        holder.tvName.setText(shop.getName());
        String address = shop.getCountry() + "·" + shop.getCity() + "·" + shop.getAddress();
        holder.tvAddress.setText(address);
        holder.tvPhone.setText(shop.getPhone_num());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvPhone;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_shop_name);
            tvAddress = view.findViewById(R.id.tv_address);
            tvPhone = view.findViewById(R.id.tv_phone);
            itemView = view;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
