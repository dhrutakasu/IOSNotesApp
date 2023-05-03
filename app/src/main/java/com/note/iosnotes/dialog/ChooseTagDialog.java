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

import com.note.iosnotes.Model.Tags;
import com.note.iosnotes.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseTagDialog extends Dialog {
    /* access modifiers changed from: private */
    public IListTag iListTag;

    public interface IListTag {
        Tags getTag(int i);

        int getTagsCount();

        void onTagSelected(int i);
    }

    /* access modifiers changed from: private */
    public int getTagIcon(int i) {
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

    public ChooseTagDialog(Context context, IListTag iListTag2) {
        super(context);
        this.iListTag = iListTag2;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_list_tag);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rc_list_tags);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        TagFolderAdapter tagFolderAdapter = new TagFolderAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tagFolderAdapter);
    }

    class TagFolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        TagFolderAdapter() {
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new TagFolderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tag_color_item, viewGroup, false));
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ((TagFolderHolder) viewHolder).bindView(ChooseTagDialog.this.iListTag.getTag(i));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lambda$new$0$ChooseTagDialog$TagFolderAdapter$TagFolderHolder(ChooseTagDialog.this.iListTag.getTag(i));
                }
            });
        }

        public int getItemCount() {
            return ChooseTagDialog.this.iListTag.getTagsCount();
        }

        class TagFolderHolder extends RecyclerView.ViewHolder {
            ImageView imgTagIcon;
            TextView tvTagName;

            public TagFolderHolder(View view) {
                super(view);
                this.imgTagIcon = (ImageView) view.findViewById(R.id.IvTagColorCode);
                this.tvTagName = (TextView) view.findViewById(R.id.TvTagColorName);

            }

            public void bindView(Tags tag) {
                this.imgTagIcon.setImageResource(ChooseTagDialog.this.getTagIcon(tag.getColorCodeId()));
                this.tvTagName.setText(tag.getTagName());
            }

        }
        public void lambda$new$0$ChooseTagDialog$TagFolderAdapter$TagFolderHolder(Tags tag) {
            ChooseTagDialog.this.dismiss();
            ChooseTagDialog.this.iListTag.onTagSelected(tag.getId());
        }
    }
}
