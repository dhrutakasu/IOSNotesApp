package com.note.iosnotes.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.NotesDatabaseHelper;

public class NoteCreateWidgetProvider extends AppWidgetProvider {
    private NotesDatabaseHelper helper;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
    }

    public void onDeleted(Context context, int[] iArr) {
        helper = new NotesDatabaseHelper(context);
        for (int valueof : iArr) {
            final Note note = (Note) helper.getWidgetId((int) valueof);
            if (note != null) {
                note.setCreateWidgetId(-1);
            }
            helper.updateNotes(note);
        }
    }
}
