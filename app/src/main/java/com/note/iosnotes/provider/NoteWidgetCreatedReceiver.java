package com.note.iosnotes.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.ui.Activity.ActivityNewCreateNotes;

public class NoteWidgetCreatedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int i;
        System.out.println("------- bundle : "+intent.getIntExtra("appWidgetId",-1));
        System.out.println("------- bundle : "+intent.getIntExtra("WIDGET_NOTE_ID", 0));
        if ((i = intent.getIntExtra("appWidgetId",-1)) > 0) {
            NotesDatabaseHelper helper=new NotesDatabaseHelper(context);
            Note notes = helper.getNoteRecord(intent.getIntExtra("WIDGET_NOTE_ID", 0));
            System.out.println("------ widget : "+i+" - "+notes.toString());
            notes.setWidgetId(i);
            helper.updateNotes(notes);
            ActivityNewCreateNotes.updateNoteWidget(i, context.getPackageName(), context, intent.getStringExtra("WIDGET_NOTE_TITLE"), intent.getStringExtra("WIDGET_NOTE_CONTENT"), intent.getIntExtra("WIDGET_NOTE_ID", -1));
            Toast.makeText(context, context.getResources().getString(R.string.widget_notify_success_created_msg), Toast.LENGTH_SHORT).show();
        }
    }
/*    public void onReceive(Context context, Intent intent) {
        int i;
        Bundle extras = intent.getExtras();
        System.out.println("------- bundle : "+extras.toString());
        if (extras != null && (i = extras.getInt("appWidgetId")) > 0) {
            NotesDatabaseHelper helper=new NotesDatabaseHelper(context);
            Note notes = helper.getNoteRecord(intent.getIntExtra("WIDGET_NOTE_ID", 0));
            System.out.println("------ widgetss : "+i+" - "+notes.toString());
            notes.setWidgetId(i);
            helper.updateNotes(notes);
            ActivityNewCreateNotes.updateNoteWidget(i, context.getPackageName(), context, intent.getStringExtra("WIDGET_NOTE_TITLE"), intent.getStringExtra("WIDGET_NOTE_CONTENT"), intent.getLongExtra("WIDGET_NOTE_ID", -1));
            Toast.makeText(context, context.getResources().getString(R.string.widget_notify_success_created_msg), 0).show();
        }
    }*/
}
