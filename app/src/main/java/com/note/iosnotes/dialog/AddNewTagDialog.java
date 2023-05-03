package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.note.iosnotes.R;


public class AddNewTagDialog extends Dialog {
    private CreateNewTagListeners createNewTagListeners;
    private String StrPositiveButton;
    private String StrSubtitle;
    private String StrTitle;

    public interface CreateNewTagListeners {
        void onSaveTag(String str);
    }

    public AddNewTagDialog(Context context, CreateNewTagListeners createNewTagListeners) {
        super(context);
        this.createNewTagListeners = createNewTagListeners;
    }

    public AddNewTagDialog(Context context, String title, String SubTitle, String positiveButton, CreateNewTagListeners createNewTagListeners) {
        super(context);
        this.createNewTagListeners = createNewTagListeners;
        this.StrTitle = title;
        this.StrSubtitle = SubTitle;
        this.StrPositiveButton = positiveButton;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_add_new_tags);
        ((TextView) findViewById(R.id.TvNewTagTitle)).setText(StrTitle);
        ((TextView) findViewById(R.id.TvNewTagSubTitle)).setText(StrSubtitle);
        TextView TvSaveNewTag = (TextView) findViewById(R.id.TvSaveNewTag);
        TvSaveNewTag.setText(StrPositiveButton);
        EditText EdtTagName = (EditText) findViewById(R.id.EdtTagName);
        TvSaveNewTag.setOnClickListener(view -> {
            dismiss();
            createNewTagListeners.onSaveTag(EdtTagName.getText().toString());
        });
        ((TextView) findViewById(R.id.TvCancelNewTag)).setOnClickListener(view -> cancel());
    }
}
