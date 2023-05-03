package com.note.iosnotes.ui.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.note.iosnotes.Model.Note;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<Note> notesList;
    private final INotesList iNotesList;
    private final String folderName;
    private boolean editMode = false;
    private boolean[] checked;

    public NotesAdapter(Context context, ArrayList<Note> notesList, String folderName, INotesList iNotesList) {
        this.context = context;
        this.notesList = notesList;
        this.folderName = folderName;
        this.iNotesList = iNotesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.title.setText(note.getNoteTitle());
        holder.folder.setText(folderName);
        String timeAgo = Constant.getTimeAgo(note.getDateModified());
        System.out.println("-------- adapter list : "+note.toString());
        if (note.isLocked()) {
            holder.lockIcon.setVisibility(View.VISIBLE);
            holder.tvShortDest.setText(timeAgo + "  " + context.getResources().getString(R.string.note_locked_string));
        } else {
            holder.lockIcon.setVisibility(View.GONE);
            holder.tvShortDest.setText(timeAgo + "  " + note.getNoteContent());
        }
        holder.itemBackground.setBackground(Constant.getDrawableBackground(position, context, holder.viewSeparateItem, getItemCount(), notesList));
        Bitmap bitmap = Constant.getBitmap(note.getImgByteArr(), note.getImgOrientCode());
        if (bitmap != null) {
            Glide.with(context).load(bitmap)
                    .apply(new RequestOptions().override(200, 200))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                    .into(holder.imgAttach);
            holder.imgAttach.setVisibility(View.VISIBLE);
        } else {
            holder.imgAttach.setVisibility(View.GONE);
        }
        if (editMode) {
            holder.imgSelected.setVisibility(View.VISIBLE);
            if (checked[position]) {
                holder.imgSelected.setImageResource(R.drawable.ic_baseline_check_circle);
            } else {
                holder.imgSelected.setImageResource(R.drawable.ic_circle_unselect);
            }
        } else {
            holder.imgSelected.setVisibility(View.GONE);
        }
        holder.imgSelected.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                GotoSelected(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                iNotesList.onNoteItemSelected(position);
            }
        });
    }

    public void updateData(ArrayList<Note> noteArrayList) {
        notesList.clear();
        for (Note model : noteArrayList) {
            notesList.add(model);
            notifyDataSetChanged();
        }
    }

    public boolean[] getListChecked() {
        return checked;
    }


    public interface INotesList {
        void onNoteItemSelected(int i);
    }

    private void GotoSelected(int position) {
        if (checked[position]) {
            checked[position] = false;
        } else {
            checked[position] = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView folder;
        private ImageView imgAttach;
        private ImageView imgSelected;
        private RelativeLayout itemBackground;
        private ImageView lockIcon;
        private TextView title;
        private TextView tvShortDest;
        private View viewSeparateItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBackground = (RelativeLayout) itemView.findViewById(R.id.rl_note_item_background);
            itemBackground = (RelativeLayout) itemView.findViewById(R.id.rl_note_item_background);
            title = (TextView) itemView.findViewById(R.id.note_title);
            tvShortDest = (TextView) itemView.findViewById(R.id.tv_note_short_desc);
            imgAttach = (ImageView) itemView.findViewById(R.id.img_attach_note);
            imgSelected = (ImageView) itemView.findViewById(R.id.img_ic_select_note_item);
            lockIcon = (ImageView) itemView.findViewById(R.id.img_note_icon_lock);
            folder = (TextView) itemView.findViewById(R.id.tv_note_folder);
            viewSeparateItem = itemView.findViewById(R.id.view_separate_notes_list);
        }
    }


    public void enableEditMode(boolean z) {
        this.editMode = z;
        resetCheckState();
        notifyDataSetChanged();
    }

    private void resetCheckState() {
        this.checked = new boolean[getItemCount()];
        for (int i = 0; i < getItemCount(); i++) {
            this.checked[i] = false;
        }
    }
}
