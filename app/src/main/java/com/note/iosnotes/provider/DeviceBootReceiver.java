package com.note.iosnotes.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.ui.Activity.ActivityNewCreateNotes;

import java.util.Iterator;

public class DeviceBootReceiver extends BroadcastReceiver {
    private NotesDatabaseHelper helper;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i("CHECKER", "Boot completed");
            restartAllWidgets(context);
        }
    }

    public void restartAllWidgets(Context context) {
        helper = new NotesDatabaseHelper(context);
        Iterator it =  helper.getAllWidget(0).iterator();
        while (it.hasNext()) {
            Note note = (Note) it.next();
            ActivityNewCreateNotes.updateNoteWidget(note.getWidgetId(), context.getPackageName(), context, note.getNoteTitle(), note.getNoteContent(), note.getId());
        }
    }
}
