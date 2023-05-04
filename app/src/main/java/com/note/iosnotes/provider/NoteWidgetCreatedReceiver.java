package com.note.iosnotes.provider;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.ui.Activity.NewCreateNotesActivity;

public class NoteWidgetCreatedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int i;
        if ((i = intent.getIntExtra("appWidgetId",-1)) > 0) {
            NotesDatabaseHelper helper=new NotesDatabaseHelper(context);
            Note notes = helper.getNoteRecord(intent.getIntExtra(Constant.TAG_WIDGET_NOTE_ID, 0));
            notes.setCreateWidgetId(i);
            helper.updateNotes(notes);
            NewCreateNotesActivity.NoteUpdateWidget(i, context.getPackageName(), context, intent.getStringExtra(Constant.TAG_WIDGET_NOTE_TITLE), intent.getStringExtra(Constant.TAG_WIDGET_NOTE_CONTENT), intent.getIntExtra(Constant.TAG_WIDGET_NOTE_ID, -1));
            Toast.makeText(context, context.getResources().getString(R.string.widget_notify_success_created_msg), Toast.LENGTH_SHORT).show();
        }
    }
}
