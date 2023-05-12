package com.note.iosnotes.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.ui.Activity.NewCreateNotesActivity;

import java.util.Iterator;

public class BootReceiver extends BroadcastReceiver {
    private NotesDatabaseHelper helper;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            System.out.println("CHECKER Boot completed");
            AllRestartWidgets(context);
        }
    }

    public void AllRestartWidgets(Context context) {
        helper = new NotesDatabaseHelper(context);
        Iterator<Note> it =  helper.getAllWidget(0).iterator();
        while (it.hasNext()) {
            Note note = (Note) it.next();
            NewCreateNotesActivity.NoteUpdateWidget(note.getCreateWidgetId(), context.getPackageName(), context, note.getNoteTitle(), note.getNoteContent(), note.getId());
        }
    }
}
