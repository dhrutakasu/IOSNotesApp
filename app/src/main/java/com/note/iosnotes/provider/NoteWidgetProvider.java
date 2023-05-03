package com.note.iosnotes.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.ui.Activity.ActivityNewCreateNotes;

public class NoteWidgetProvider extends AppWidgetProvider {
    private NotesDatabaseHelper helper;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
//        Bundle extras = intent.getExtras();
//        System.out.println("------- bundle :extra "+extras.toString());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                int i;
//                System.out.println("------- bundle : "+intent.getIntExtra("appWidgetId",-1));
//                System.out.println("------- bundle iii : "+intent.getIntExtra("WIDGET_NOTE_ID",-1));
//                if ((i = intent.getIntExtra("appWidgetId",-1)) > 0) {
//                    NotesDatabaseHelper helper=new NotesDatabaseHelper(context);
//                    Note notes = helper.getNoteRecord(intent.getIntExtra("WIDGET_NOTE_ID", -1));
//                    System.out.println("------ widget : "+i+" - "+notes.toString());
//                    notes.setWidgetId(i);
//                    helper.updateNotes(notes);
//                    ActivityNewCreateNotes.updateNoteWidget(i, context.getPackageName(), context, intent.getStringExtra("WIDGET_NOTE_TITLE"), intent.getStringExtra("WIDGET_NOTE_CONTENT"), intent.getIntExtra("WIDGET_NOTE_ID", -1));
//                    Toast.makeText(context, context.getResources().getString(R.string.widget_notify_success_created_msg), Toast.LENGTH_SHORT).show();
//                }
            }
        },200);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
    }

    public void onDeleted(Context context, int[] iArr) {
//        Realm defaultInstance = Realm.getDefaultInstance();
        helper = new NotesDatabaseHelper(context);
        for (int valueOf : iArr) {
            System.out.println("------ widget : 2 "+valueOf);
            final Note note = (Note) helper.getWidgetId((int) valueOf);
            if (note != null) {
                note.setWidgetId(-1);
            }
            helper.updateNotes(note);
        }
    }
}
