package com.planet.wondering.chemi.util.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.BottomSheetMenu;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 2. 26..
 */

public class BottomSheetMenuAdapter extends RecyclerView.Adapter<BottomSheetMenuAdapter.MenuItemHolder> {

    private static final String TAG = BottomSheetMenuAdapter.class.getSimpleName();

    private ArrayList<BottomSheetMenu> mBottomSheetMenus;
    private OnItemClickListener mItemClickListener;

    public BottomSheetMenuAdapter(ArrayList<BottomSheetMenu> bottomSheetMenus) {
        mBottomSheetMenus = bottomSheetMenus;
    }

    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_bottom_sheet_menu, parent, false);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, int position) {
        holder.bindMenuItem(mBottomSheetMenus.get(position));
    }

    @Override
    public int getItemCount() {
        return mBottomSheetMenus.size();
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItemHolder itemHolder, int position);
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private BottomSheetMenu mBottomSheetMenu;

        private ImageView mMenuItemIconImageView;
        private TextView mMenuItemTitleTextView;

        public MenuItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mMenuItemIconImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_bottom_sheet_menu_icon_image_view);
            mMenuItemTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_bottom_sheet_menu_title_text_view);
        }

        public void bindMenuItem(BottomSheetMenu bottomSheetMenu) {
            mBottomSheetMenu = bottomSheetMenu;
            if (mBottomSheetMenu.getImageId() != 0) {
                mMenuItemIconImageView.setImageResource(mBottomSheetMenu.getImageId());
            }
            mMenuItemTitleTextView.setText(mBottomSheetMenu.getTitleId());
        }

        @Override
        public void onClick(View v) {
//            Log.i(TAG, "onClick getAdapterPosition() : " + getAdapterPosition());
            OnItemClickListener itemClickListener = getItemClickListener();
            if (itemClickListener != null) {
                itemClickListener.onItemClick(this, getAdapterPosition());
            }
        }
    }

}
