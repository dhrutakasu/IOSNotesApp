package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.note.iosnotes.R;

public class NoteWidgetBackgroundDialog extends Dialog {
    private final setWidgetBackground setWidgetBackground;

    public interface setWidgetBackground {
        void onNoteBackgroundSelected(int i);
    }

    public NoteWidgetBackgroundDialog(Context context, setWidgetBackground setWidgetBackground2) {
        super(context);
        this.setWidgetBackground = setWidgetBackground2;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_note_widget_background);
        findViewById(R.id.LlBgStyle1).setOnClickListener(view -> {
            dismiss();
            setWidgetBackground.onNoteBackgroundSelected(0);
        });
        findViewById(R.id.LlBgStyle2).setOnClickListener(view -> {
            dismiss();
            setWidgetBackground.onNoteBackgroundSelected(1);
        });
        findViewById(R.id.LlBgStyle3).setOnClickListener(view -> {
            dismiss();
            setWidgetBackground.onNoteBackgroundSelected(2);
        });
        findViewById(R.id.LlBgStyle4).setOnClickListener(view -> {
            dismiss();
            setWidgetBackground.onNoteBackgroundSelected(3);
        });
        findViewById(R.id.LlBgSettingCancel).setOnClickListener(view -> dismiss());
    }
}
