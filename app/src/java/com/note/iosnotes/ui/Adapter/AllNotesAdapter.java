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
import com.bumptech.glide.request.RequestOptions;
import com.note.iosnotes.Model.Note;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllNotesAdapter extends RecyclerView.Adapter<AllNotesAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<Note> notesList;
    private final setNotesList setNotesList;
    private final String folderName;
    private boolean editMode = false;
    private boolean[] checked;

    public AllNotesAdapter(Context context, ArrayList<Note> notesList, String folderName, setNotesList setNotesList) {
        this.context = context;
        this.notesList = notesList;
        this.folderName = folderName;
        this.setNotesList = setNotesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.TvNoteTitle.setText(note.getNoteTitle());
        String timeAgo = Constant.getTime(note.getDateTimeMills());
        if (note.isLockedOrNot()) {
            holder.IvNoteLock.setVisibility(View.VISIBLE);
        } else {
            holder.IvNoteLock.setVisibility(View.GONE);
        }
        holder.TvNoteShortDesc.setText(timeAgo);
        if (note.getNoteContent().equalsIgnoreCase("")){
            holder.TvNoteContent.setVisibility(View.GONE);
        }else {
            holder.TvNoteContent.setText(note.getNoteContent());
        }
        holder.TvNoteContent.setTextColor(context.getResources().getColor(R.color.ios_sys_second_label));
//        holder.RlNoteBackground.setBackground(Constant.getDrawableOfBackground(position, context, holder.ViewSeparateNotes, getItemCount(), notesList));
        Bitmap bitmap = Constant.getBitmapOfRotate(note.getImgByteFormat(), note.getImgOrientionCode());
        if (bitmap != null) {
            Glide.with(context).load(bitmap)
                    .apply(new RequestOptions().override(200, 200))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                    .into(holder.IvAttachNote);
            holder.IvAttachNote.setVisibility(View.VISIBLE);
        } else {
            holder.IvAttachNote.setVisibility(View.GONE);
        }
        if (editMode) {
            holder.IvSelectNote.setVisibility(View.VISIBLE);
            if (checked[position]) {
                holder.IvSelectNote.setImageResource(R.drawable.ic_baseline_check_circle);
            } else {
                holder.IvSelectNote.setImageResource(R.drawable.ic_circle_unselect);
            }
        } else {
            holder.IvSelectNote.setVisibility(View.GONE);
        }
        holder.IvSelectNote.setOnClickListener(view -> GotoSelected(position));
        holder.itemView.setOnClickListener(view -> setNotesList.onNoteSelected(position));
    }

    public void updateData(ArrayList<Note> noteArrayList) {
        notesList.clear();
        for (Note note : noteArrayList) {
            notesList.add(note);
            notifyDataSetChanged();
        }
    }

    public boolean[] getListChecked() {
        return checked;
    }

    public interface setNotesList {
        void onNoteSelected(int i);
    }

    private void GotoSelected(int position) {
        checked[position] = !checked[position];
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView IvAttachNote;
        private final ImageView IvSelectNote;
        private RelativeLayout RlNoteBackground;
        private final ImageView IvNoteLock;
        private final TextView TvNoteTitle;
        private final TextView TvNoteShortDesc;
        private final TextView TvNoteContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            RlNoteBackground = itemView.findViewById(R.id.RlNoteBackground);
            TvNoteTitle = itemView.findViewById(R.id.TvNoteTitle);
            TvNoteShortDesc = itemView.findViewById(R.id.TvNoteShortDesc);
            TvNoteContent = itemView.findViewById(R.id.TvNoteContent);
            IvAttachNote = itemView.findViewById(R.id.IvAttachNote);
            IvSelectNote = itemView.findViewById(R.id.IvSelectNote);
            IvNoteLock = itemView.findViewById(R.id.IvNoteLock);
        }
    }

    public void enableEditNote(boolean z) {
        editMode = z;
        CheckState();
        notifyDataSetChanged();
    }

    private void CheckState() {
        checked = new boolean[getItemCount()];
        for (int i = 0; i < getItemCount(); i++) {
            checked[i] = false;
        }
    }
}
