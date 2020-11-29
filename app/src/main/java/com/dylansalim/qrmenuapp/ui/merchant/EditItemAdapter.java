package com.dylansalim.qrmenuapp.ui.merchant;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.ListItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EditItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_ADD_NEW_ITEM = 2;

    private List<ListItem> listItems;

    public EditItemAdapter(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header_item, parent, false);
            return new VHHeader(v);
        } else if (viewType == TYPE_ADD_NEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button_add_new_item, parent, false);
            return new VHAddNewBtn(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_edit_list_item, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHHeader) {
            VHHeader VHheader = (VHHeader) holder;
            VHheader.mTitle.setText(getItem(position).getHeader());
        } else if (holder instanceof VHAddNewBtn) {
            VHAddNewBtn VHaddNewBtn = (VHAddNewBtn) holder;
            VHaddNewBtn.categoryId = getItem(position).getId();
        }
        if (holder instanceof VHItem) {
            ListItem currentItem = getItem(position);
            VHItem VHitem = (VHItem) holder;
            VHitem.mTitle.setText(currentItem.getName());
            VHitem.mDesc.setText(currentItem.getDesc());
            VHitem.mPricing.setText(Double.toString(currentItem.getPricing()));
        }
    }

    private ListItem getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else if (isAddNewBtn(position))
            return TYPE_ADD_NEW_ITEM;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return getItem(position).isHeader();
    }

    private boolean isAddNewBtn(int position) {
        return getItem(position).isAddNewBtn();
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView mTitle;

        public VHHeader(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.tv_header_title);
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mDesc;
        TextView mPricing;

        public VHItem(@NonNull View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.tv_edit_item_list_title);
            this.mDesc = (TextView) itemView.findViewById(R.id.tv_edit_item_list_desc);
            this.mPricing = (TextView) itemView.findViewById(R.id.tv_edit_item_list_pricing);
        }
    }

    class VHAddNewBtn extends RecyclerView.ViewHolder {
        Button mAddNewBtn;
        int categoryId;

        public VHAddNewBtn(@NonNull View itemView) {
            super(itemView);
            this.mAddNewBtn = (Button) itemView.findViewById(R.id.btn_add_new_item);
        }
    }
}
