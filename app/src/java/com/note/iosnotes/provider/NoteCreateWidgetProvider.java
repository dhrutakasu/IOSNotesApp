package com.note.iosnotes.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.ui.Activity.NewCreateNotesActivity;

public class NoteCreateWidgetProvider extends AppWidgetProvider {
    private NotesDatabaseHelper helper;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        System.out.println("----------- uuid_length " + iArr.length);
        for (int id : iArr) {
            Constant.Widget_Id = id;
            System.out.println("----------- uuid " + id);
        }
    }

    @Override
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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
//
//        Constant.Widget_Id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
    }
}
