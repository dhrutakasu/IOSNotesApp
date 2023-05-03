package com.note.iosnotes.provider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;

public class NoteRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private final NotesDatabaseHelper helper;
    byte[] imgByteArr;
    int imgOrientCode;
    private Context mContext;
    String noteContent;
    private long noteId;
    String noteTitle;
    private int textColor;

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    public NoteRemoteViewFactory(Context context, Intent intent) {
        Note note;
        this.noteId = intent.getLongExtra("WIDGET_NOTE_ID", -1);
        this.noteTitle = intent.getStringExtra("WIDGET_NOTE_TITLE");
        this.noteContent = intent.getStringExtra("WIDGET_NOTE_CONTENT");
        this.textColor = intent.getIntExtra("WIDGET_TEXT_COLOR", -1);
        this.mContext = context;
        helper = new NotesDatabaseHelper(context);
//        Realm defaultInstance = Realm.getDefaultInstance();
//        this.mRealm = defaultInstance;
        if (this.noteId >= 0 && (note = helper.getNoteRecord((int) noteId)) != null) {
            this.imgByteArr = note.getImgByteArr();
            this.imgOrientCode = note.getImgOrientCode();
        }
    }

    @Override
    public void onDestroy() {
        if (this.mContext != null) {
            this.mContext = null;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_note_main_view);
        remoteViews.removeAllViews(R.id.ll_note_widget_main);
        RemoteViews remoteViews2 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_note_title_view);
        if (this.noteTitle.equals("")) {
            this.noteTitle = this.mContext.getResources().getString(R.string.untitled);
        }
        remoteViews2.setTextViewText(R.id.tv_note_widget_title, this.noteTitle);
        if (this.textColor != -1) {
            remoteViews2.setTextColor(R.id.tv_note_widget_title, this.mContext.getResources().getColor(this.textColor));
        }
        remoteViews.addView(R.id.ll_note_widget_main, remoteViews2);
        RemoteViews remoteViews3 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_note_content_view);
        remoteViews3.setTextViewText(R.id.tv_note_widget_content, this.noteContent);
        if (this.textColor != -1) {
            remoteViews3.setTextColor(R.id.tv_note_widget_content, this.mContext.getResources().getColor(this.textColor));
        }
        remoteViews.addView(R.id.ll_note_widget_main, remoteViews3);
        Bitmap bitmap = Constant.getBitmap(this.imgByteArr, this.imgOrientCode);
        if (bitmap != null) {
            RemoteViews remoteViews4 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_note_image_view);
            remoteViews4.setImageViewBitmap(R.id.img_note_image_attach, Constant.getRoundedCornerBitmap(bitmap, 30));
            remoteViews.addView(R.id.ll_note_widget_main, remoteViews4);
        }
        remoteViews.setOnClickFillInIntent(R.id.ll_note_widget_main, new Intent());
        return remoteViews;
    }
/*

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_note_main_view);
        remoteViews.removeAllViews(R.id.ll_note_widget_main);
        RemoteViews remoteViews2 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_note_title_view);
        if (this.noteTitle.equals("")) {
            this.noteTitle = this.mContext.getResources().getString(R.string.untitled);
        }
        remoteViews2.setTextViewText(R.id.tv_note_widget_title, this.noteTitle);
        if (this.textColor != -1) {
            remoteViews2.setTextColor(R.id.tv_note_widget_title, this.mContext.getResources().getColor(this.textColor));
        }
        remoteViews.addView(R.id.ll_note_widget_main, remoteViews2);
        RemoteViews remoteViews3 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_note_content_view);
        remoteViews3.setTextViewText(R.id.tv_note_widget_content, this.noteContent);
        if (this.textColor != -1) {
            remoteViews3.setTextColor(R.id.tv_note_widget_content, this.mContext.getResources().getColor(this.textColor));
        }
        remoteViews.addView(R.id.ll_note_widget_main, remoteViews3);
        Bitmap bitmap = Constant.getBitmap(this.imgByteArr, this.imgOrientCode);
        if (bitmap != null) {
            RemoteViews remoteViews4 = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_note_image_view);
            remoteViews4.setImageViewBitmap(R.id.img_note_image_attach, Constant.getRoundedCornerBitmap(bitmap, 30));
            remoteViews.addView(R.id.ll_note_widget_main, remoteViews4);
        }
        remoteViews.setOnClickFillInIntent(R.id.ll_note_widget_main, new Intent());
        return remoteViews;
    }*/
}
