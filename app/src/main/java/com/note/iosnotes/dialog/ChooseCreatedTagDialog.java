package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.note.iosnotes.Model.Tags;
import com.note.iosnotes.R;

public class ChooseCreatedTagDialog extends Dialog {
    public setListTag listTag;
    public interface setListTag {
       Tags getTag(int i);
       int getTagsSize();
       void onTagSelectedPosition(int i);
    }
    public ChooseCreatedTagDialog(Context context, setListTag listTag) {
        super(context);
        this.listTag = listTag;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_list_of_tag);
        RecyclerView RvTagList = (RecyclerView) findViewById(R.id.RvTagList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        TagFoldersAdapter folderAdapter = new TagFoldersAdapter();
        RvTagList.setLayoutManager(layoutManager);
        RvTagList.setAdapter(folderAdapter);
    }

    public class TagFoldersAdapter extends RecyclerView.Adapter<TagFoldersAdapter.MyViewHolder> {

        public TagFoldersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TagFoldersAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_color_item, parent, false));
        }

        public void onBindViewHolder(TagFoldersAdapter.MyViewHolder holder, int i) {
            Tags tags= listTag.getTag(i);
            holder.IvTagColorCode.setImageResource(getTagColor(tags.getColorCodeId()));
            holder.TvTagColorName.setText(tags.getTagName());
            holder.itemView.setOnClickListener(view -> {
                dismiss();
                listTag.onTagSelectedPosition(listTag.getTag(i).getId());
            });
        }
        public int getTagColor(int i) {
            switch (i) {
                case 0:
                    return R.drawable.md_red_500;
                case 1:
                    return R.drawable.md_yellow_500;
                case 2:
                    return R.drawable.md_green_500;
                case 3:
                    return R.drawable.md_pink_500;
                case 4:
                    return R.drawable.md_purple_500;
                case 5:
                    return R.drawable.md_blue_500;
                case 6:
                    return R.drawable.md_cyan_500;
                case 7:
                    return R.drawable.md_teal_500;
                case 8:
                    return R.drawable.md_lime_500;
                case 9:
                    return R.drawable.md_amber_500;
                case 10:
                    return R.drawable.md_orange_500;
                case 11:
                    return R.drawable.md_gray;
                default:
                    return R.drawable.md_tag_outline;
            }
        }
        public int getItemCount() {
            return listTag.getTagsSize();
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView IvTagColorCode;
            TextView TvTagColorName;
            public MyViewHolder(View view) {
                super(view);
                IvTagColorCode = (ImageView) view.findViewById(R.id.IvTagColorCode);
                TvTagColorName = (TextView) view.findViewById(R.id.TvTagColorName);
            }
        }
    }
}
