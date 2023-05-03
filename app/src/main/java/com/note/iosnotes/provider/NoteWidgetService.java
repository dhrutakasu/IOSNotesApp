package com.note.iosnotes.provider;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class NoteWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NoteRemoteViewFactory(getApplicationContext(), intent);
    }
}
