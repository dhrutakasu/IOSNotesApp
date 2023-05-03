package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.note.iosnotes.R;

public class DeleteNoteDialog extends Dialog {
    private IDeleteNote iDeleteNote;
    private String message;
    private String positiveButton;
    private String title;

    public interface IDeleteNote {
        void onDeleteNote();
    }

    public DeleteNoteDialog(Context context, String str, String str2, String str3, IDeleteNote iDeleteNote2) {
        super(context);
        this.iDeleteNote = iDeleteNote2;
        this.title = str;
        this.message = str2;
        this.positiveButton = str3;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_confirm_delete_tags);
        TextView textView = (TextView) findViewById(R.id.TvDeleteTag);
        ((TextView) findViewById(R.id.TvDialogDeleteTagTitle)).setText(this.title);
        ((TextView) findViewById(R.id.TvDialogDeleteTagSubTitle)).setText(this.message);
        textView.setText(this.positiveButton);
        ((TextView) findViewById(R.id.TvCancelDeleteTag)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                DeleteNoteDialog.this.lambda$onCreate$0$DeleteNoteDialog(view);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                DeleteNoteDialog.this.lambda$onCreate$1$DeleteNoteDialog(view);
            }
        });
    }

    public void lambda$onCreate$0$DeleteNoteDialog(View view) {
        dismiss();
    }

    public void lambda$onCreate$1$DeleteNoteDialog(View view) {
        dismiss();
        this.iDeleteNote.onDeleteNote();
    }
}
