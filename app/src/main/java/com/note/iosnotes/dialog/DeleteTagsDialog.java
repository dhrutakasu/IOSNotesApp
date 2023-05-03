package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.note.iosnotes.R;

public class DeleteTagsDialog extends Dialog {
    private DeleteTagListeners deleteTagListeners;

    public interface DeleteTagListeners {
        void SetonDeleteTag();
    }

    public DeleteTagsDialog(Context context, DeleteTagListeners deleteTagListeners2) {
        super(context);
        this.deleteTagListeners = deleteTagListeners2;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_confirm_delete_tags);
        ((TextView) findViewById(R.id.TvCancelDeleteTag)).setOnClickListener(view -> cancel());
        ((TextView) findViewById(R.id.TvDeleteTag)).setOnClickListener(view -> deleteTagListeners.SetonDeleteTag());
    }
}
