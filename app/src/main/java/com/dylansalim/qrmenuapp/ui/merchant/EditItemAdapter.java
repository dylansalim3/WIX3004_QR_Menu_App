package com.dylansalim.qrmenuapp.ui.merchant;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.dylansalim.qrmenuapp.BuildConfig;
import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.EditListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

public class EditItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_ADD_NEW_ITEM = 2;
    private OnCategoryDeleteListener onCategoryDeleteListener;
    private OnItemListener onItemListener;
    private OnAddNewBtnListener onAddNewBtnListener;
    private static String TAG = "eia";
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    private List<EditListItem> editListItems;

    public EditItemAdapter(List<EditListItem> editListItems, OnCategoryDeleteListener onCategoryDeleteListener, OnItemListener onItemListener, OnAddNewBtnListener onAddNewBtnListener) {
        this.editListItems = editListItems;
        this.onCategoryDeleteListener = onCategoryDeleteListener;
        this.onItemListener = onItemListener;
        this.onAddNewBtnListener = onAddNewBtnListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header_item, parent, false);
            return new VHHeader(v, onCategoryDeleteListener);
        } else if (viewType == TYPE_ADD_NEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button_add_new_item, parent, false);
            return new VHAddNewBtn(v, onAddNewBtnListener);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_edit_list_item, parent, false);
            return new VHItem(v, onItemListener);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHHeader) {
            Log.d(TAG, "item" + getItem(position).toString());
            VHHeader VHheader = (VHHeader) holder;
            VHheader.categoryId = getItem(position).getCategoryId();
            VHheader.mTitle.setText(getItem(position).getName());
            // if edit mode is on
            if (MerchantPresenter.editMode) {
                VHheader.mDeleteButton.setVisibility(View.VISIBLE);
            }
        } else if (holder instanceof VHAddNewBtn) {
            VHAddNewBtn VHaddNewBtn = (VHAddNewBtn) holder;
            VHaddNewBtn.categoryId = getItem(position).getCategoryId();
        }
        if (holder instanceof VHItem) {
            EditListItem currentItem = getItem(position);
            VHItem VHitem = (VHItem) holder;
            VHitem.mTitle.setText(currentItem.getName());
            VHitem.mDesc.setText(currentItem.getDesc());
            VHitem.mCurrency.setText(currentItem.getCurrency());
            String pricing = String.format("%.2f", currentItem.getPricing());
            String promoPricing = String.format("%.2f", currentItem.getPromoPricing());

            VHitem.mPricing.setText(pricing);
            VHitem.itemId = currentItem.getId();

            if (currentItem.isRecommended()) {

                VHitem.mTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_star_rate_24, 0, 0, 0);
            }

            if (currentItem.getPromoPricing() > 0) {
                VHitem.mPricing.setText(pricing, TextView.BufferType.SPANNABLE);
                Spannable spannable = (Spannable) VHitem.mPricing.getText();
                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, pricing.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                VHitem.mPromoPricing.setVisibility(View.VISIBLE);
                VHitem.mPromoPricing.setText(promoPricing);
            }

            Picasso.get().load(BuildConfig.SERVER_API_URL + "/" + getItem(position).getImgUrl())
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                    .into(VHitem.mImageView);

        }
    }

    private EditListItem getItem(int position) {
        return editListItems.get(position);
    }

    @Override
    public int getItemCount() {
        return editListItems.size();
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

    class VHHeader extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        int categoryId;
        ImageButton mDeleteButton;
        OnCategoryDeleteListener onCategoryDeleteListener;

        public VHHeader(View itemView, OnCategoryDeleteListener onCategoryDeleteListener) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.tv_header_title);
            this.mDeleteButton = (ImageButton) itemView.findViewById(R.id.ibtn_header_delete);
            this.onCategoryDeleteListener = onCategoryDeleteListener;
            mDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCategoryDeleteListener.onDeleteCategory(categoryId);
        }
    }

    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mDesc;
        TextView mPricing;
        TextView mPromoPricing;
        TextView mCurrency;
        ImageView mImageView;
        ImageButton mMoreMenuBtn;
        OnItemListener onItemListener;
        int itemId;

        public VHItem(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.tv_edit_item_list_title);
            this.mDesc = (TextView) itemView.findViewById(R.id.tv_edit_item_list_desc);
            this.mPricing = (TextView) itemView.findViewById(R.id.tv_edit_item_list_pricing);
            this.mPromoPricing = (TextView) itemView.findViewById(R.id.tv_edit_item_list_promo_pricing);
            this.mImageView = (ImageView) itemView.findViewById(R.id.iv_edit_list_item);
            this.mMoreMenuBtn = (ImageButton) itemView.findViewById(R.id.ibtn_list_item_more);
            this.mCurrency = (TextView) itemView.findViewById(R.id.tv_edit_item_list_currency);
            this.onItemListener = onItemListener;
            mMoreMenuBtn.setOnClickListener(this);

            if (MerchantPresenter.editMode) {
                mMoreMenuBtn.setVisibility(View.VISIBLE);
                // set bias to image view
                ConstraintLayout cl = (ConstraintLayout) itemView.findViewById(R.id.constraint_layout_edit_list_item);
                ConstraintSet cs = new ConstraintSet();
                cs.clone(cl);
                cs.setHorizontalBias(R.id.iv_edit_list_item, (float) 0.886);
                cs.applyTo(cl);
            } else {
                mMoreMenuBtn.setVisibility(View.INVISIBLE);
                // set bias to image view
                ConstraintLayout cl = (ConstraintLayout) itemView.findViewById(R.id.constraint_layout_edit_list_item);
                ConstraintSet cs = new ConstraintSet();
                cs.clone(cl);
                cs.setHorizontalBias(R.id.iv_edit_list_item, (float) 1);
                cs.applyTo(cl);
            }
        }

        @Override
        public void onClick(View view) {
            //inflate menu
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.merchant_cardview_edit_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.edit_menu_btn_edit:
                            onItemListener.onEditItem(itemId);
                            break;
                        case R.id.edit_menu_btn_delete:
                            onItemListener.onDeleteItem(itemId);
                            break;
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    class VHAddNewBtn extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button mAddNewBtn;
        int categoryId;
        OnAddNewBtnListener onAddNewBtnListener;

        public VHAddNewBtn(@NonNull View itemView, OnAddNewBtnListener onAddNewBtnListener) {
            super(itemView);
            this.mAddNewBtn = (Button) itemView.findViewById(R.id.btn_add_new_item);
            this.onAddNewBtnListener = onAddNewBtnListener;
            mAddNewBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onAddNewBtnListener.onAddItem(categoryId);
        }
    }

    public interface OnCategoryDeleteListener {
        void onDeleteCategory(int categoryId);
    }

    public interface OnItemListener {
        void onDeleteItem(int itemId);

        void onEditItem(int itemId);
    }

    public interface OnAddNewBtnListener {
        void onAddItem(int categoryId);
    }
}
