package com.note.iosnotes.ui.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.note.iosnotes.Model.NewTagColor;
import com.note.iosnotes.R;
import com.note.iosnotes.dialog.NewTagColorCodeDialog;

import androidx.recyclerview.widget.RecyclerView;

public class NewTagColorAdapter extends RecyclerView.Adapter<NewTagColorAdapter.MyViewHolder> {
    private final NewTagColorCodeDialog.NewTagColorListener newTagColorListener;

    public NewTagColorAdapter(NewTagColorCodeDialog.NewTagColorListener newTagColorListener) {
        this.newTagColorListener = newTagColorListener;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tag_color_item, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int i) {
        NewTagColor newTagColor = newTagColorListener.getNewTagColor(i);
        holder.IvTagColorCode.setImageResource(newTagColor.getTagColorIcon());
        holder.TvTagColorName.setText(newTagColor.getTagColorName());
        holder.itemView.setOnClickListener(view -> newTagColorListener.onNewTagColorSelected(i));
    }

    public int getItemCount() {
        return newTagColorListener.getNewTagColorCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView IvTagColorCode;
        TextView TvTagColorName;

        public MyViewHolder(View view) {
            super(view);
            IvTagColorCode = (ImageView) view.findViewById(R.id.IvTagColorCode);
            TvTagColorName = (TextView) view.findViewById(R.id.TvTagColorName);
        }
    }
}
