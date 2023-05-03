package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.note.iosnotes.Model.NewTagColor;
import com.note.iosnotes.R;
import com.note.iosnotes.ui.Adapter.NewTagColorAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewTagColorCodeDialog extends Dialog {
    public NewTagColorListener newTagColorListener;

    public interface NewTagColorListener {
        NewTagColor getNewTagColor(int i);

        int getNewTagColorCount();

        void onNewTagColorSelected(int i);
    }

    public NewTagColorCodeDialog(Context context, NewTagColorListener newTagColorListener2) {
        super(context);
        this.newTagColorListener = newTagColorListener2;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_new_tag_change_color);
        RecyclerView RvTagColor = (RecyclerView) findViewById(R.id.RvTagColor);
        RvTagColor.setLayoutManager(new LinearLayoutManager(RvTagColor.getContext()));
        RvTagColor.setAdapter(new NewTagColorAdapter(newTagColorListener));
    }
}
