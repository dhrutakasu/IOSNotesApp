package com.note.iosnotes.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.iosnotes.Model.Tags;
import com.note.iosnotes.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewTagAdapter extends RecyclerView.Adapter<NewTagAdapter.MyViewHolder> {
    private static boolean isEditItem;
    private final Context context;
    private final ArrayList<Tags> tagArray;
    private final ITagAction iTagAction;

    public NewTagAdapter(Context context, ArrayList<Tags> tagsArrayList, ITagAction iTagAction) {
        this.context = context;
        this.tagArray = tagsArrayList;
        this.iTagAction = iTagAction;
    }

    public void enableEditMode(boolean z) {
        isEditItem = z;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_item, parent, false));
    }

    public static int getTagColorCode(int position) {
        switch (position) {
            case 0:
                return R.drawable.color_1;
            case 1:
                return R.drawable.color_2;
            case 2:
                return R.drawable.color_3;
            case 3:
                return R.drawable.color_4;
            case 4:
                return R.drawable.color_5;
            case 5:
                return R.drawable.color_6;
            case 6:
                return R.drawable.color_8;
            case 7:
                return R.drawable.color_9;
            case 8:
                return R.drawable.color_10;
            case 9:
                return R.drawable.color_11;
            case 10:
                return R.drawable.color_12;
            case 11:
                return R.drawable.color_13;
            case 12:
                return R.drawable.color_14;
            case 13:
                return R.drawable.color_15;
            default:
                return R.drawable.color_7;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tags tags = tagArray.get(position);
        holder.TvTagName.setText(tags.getTagName());
        holder.IvTagColorIcon.setImageResource(NewTagAdapter.getTagColorCode(tags.getColorCodeId()));
        holder.TvTagNumberNotes.setText(tags.getCounterNote() + "");
        if (NewTagAdapter.isEditItem) {
            holder.RlTagLastGrp.setVisibility(View.INVISIBLE);
            if (position <= 1) {
                holder.ViewDisableLayout.setVisibility(View.VISIBLE);
                holder.LlTagMore.setVisibility(View.INVISIBLE);
            } else {
                holder.ViewDisableLayout.setVisibility(View.INVISIBLE);
                holder.LlTagMore.setVisibility(View.VISIBLE);
            }
        } else {
            holder.ViewDisableLayout.setVisibility(View.GONE);
            holder.LlTagMore.setVisibility(View.GONE);
            holder.RlTagLastGrp.setVisibility(View.VISIBLE);
        }
        holder.IvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position >= 1) {
                    Tags getTag = tagArray.get(position);
                    iTagAction.onActionEditTag(getTag.getId(), position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tags getTag = tagArray.get(position);
                iTagAction.onTagFolderSelected(getTag, position);
            }
        });
    }

    public interface ITagAction {
        void onActionEditTag(int id, int pos);

        void onTagFolderSelected(Tags tags, int position);
    }

    @Override
    public int getItemCount() {
        return tagArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout LlTagMore;
        private RelativeLayout RlTagLastGrp;
        private ImageView IvMore;
        private ImageView IvTagColorIcon;
        private TextView TvTagNumberNotes;
        private TextView TvTagName;
        private View ViewDisableLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TvTagName = (TextView) itemView.findViewById(R.id.TvTagName);
            IvTagColorIcon = (ImageView) itemView.findViewById(R.id.IvTagColorIcon);
            TvTagNumberNotes = (TextView) itemView.findViewById(R.id.TvTagNumberNotes);
            LlTagMore = (LinearLayout) itemView.findViewById(R.id.LlTagMore);
            RlTagLastGrp = (RelativeLayout) itemView.findViewById(R.id.RlTagLastGrp);
            ViewDisableLayout = itemView.findViewById(R.id.ViewDisableLayout);
            IvMore = (ImageView) itemView.findViewById(R.id.IvMore);
        }
    }
}
