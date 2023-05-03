package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.note.iosnotes.R;

public class NoteDeleteDialog extends Dialog {
    private setDeleteNote deleteNote;
    private String StrMessage;
    private String StrPositiveButton;
    private String StrTitle;

    public interface setDeleteNote {
        void onSetDeleteNote();
    }

    public NoteDeleteDialog(Context context, String title, String msg, String positiveButton, setDeleteNote deleteNote) {
        super(context);
        this.deleteNote = deleteNote;
        StrTitle = title;
        StrMessage = msg;
        StrPositiveButton = positiveButton;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_confirm_delete_tags);
        TextView TvDeleteTag = (TextView) findViewById(R.id.TvDeleteTag);
        ((TextView) findViewById(R.id.TvDialogDeleteTagTitle)).setText(StrTitle);
        ((TextView) findViewById(R.id.TvDialogDeleteTagSubTitle)).setText(StrMessage);
        TvDeleteTag.setText(StrPositiveButton);
        ((TextView) findViewById(R.id.TvCancelDeleteTag)).setOnClickListener(view -> dismiss());
        TvDeleteTag.setOnClickListener(view -> {
            dismiss();
            deleteNote.onSetDeleteNote();
        });
    }
}
