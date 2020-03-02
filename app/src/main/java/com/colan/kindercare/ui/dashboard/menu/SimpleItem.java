package com.colan.kindercare.ui.dashboard.menu;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.colan.kindercare.R;


public class SimpleItem extends DrawerItem<SimpleItem.ViewHolder> {

    private int selectedItemIconTint;
    private int selectedItemTextTint;

    private int selectedItemBg;

    private int normalItemIconTint;
    private int normalItemTextTint;

    private Drawable icon;
    private String title;
    private OnItemTitleSelectedListener listener;
    public SimpleItem(Drawable icon,String title) {
        this.icon = icon;
        this.title = title;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.navigation_item_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {
        holder.title.setText(title);
        holder.icon.setImageDrawable(icon);
       // holder.linearLayout.setBackgroundResource(isChecked ? R.drawable.ic_nav_selector : R.drawable.bg_navigation);
        holder.title.setTextColor(isChecked ? normalItemTextTint : normalItemTextTint);
        //holder.icon.setColorFilter(isChecked ? selectedItemIconTint : normalItemIconTint);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("selected Title",""+title);
                if (listener != null) {
                    listener.onItemSelectedTitle(title);
                }
            }
        });
    }

    public SimpleItem withSelectedTitleBg(int selectedItemBg) {
        this.selectedItemBg = selectedItemBg;
        return this;
    }

    public SimpleItem withSelectedIconTint(int selectedItemIconTint) {
        this.selectedItemIconTint = selectedItemIconTint;
        return this;
    }

    public SimpleItem withSelectedTextTint(int selectedItemTextTint) {
        this.selectedItemTextTint = selectedItemTextTint;
        return this;
    }

    public SimpleItem withIconTint(int normalItemIconTint) {
        this.normalItemIconTint = normalItemIconTint;
        return this;
    }

    public SimpleItem withTextTint(int normalItemTextTint) {
        this.normalItemTextTint = normalItemTextTint;
        return this;
    }

    static class ViewHolder extends DrawerAdapter.ViewHolder {

        private ImageView icon;
        private TextView title;
        private ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            constraintLayout=itemView.findViewById(R.id.nav_header_linear_layout);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public SimpleItem setListener(OnItemTitleSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnItemTitleSelectedListener {
        String onItemSelectedTitle(String title);

    }
}
