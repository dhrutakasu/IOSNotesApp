package com.note.iosnotes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.Model.Tags;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Tags.db";
    public static final String TAGS_TABLE_NAME = "Tags";
    public static final String TAGS_ID = "tags_id";
    public static final String TAGS_NAME = "tags_name";
    public static final String TAGS_COLOR_CODE = "tags_color_code";
    public static final String TAGS_COUNTER_NOTES = "tags_counter_note";

    public static final String NOTES_TABLE_NAME = "Notes";
    public static final String NOTES_ID = "notes_id";
    public static final String NOTES_TAGS_ID = "notes_tags_id";
    public static final String NOTE_TITLE = "notes_title";
    public static final String NOTES_CONTENT = "notes_contents";
    public static final String NOTES_TIME = "tags_time";
    public static final String NOTES_IMAGE = "tags_image";
    public static final String NOTES_ISLOCK = "tags_lock";
    public static final String NOTES_ISDELETE = "tags_delete";
    public static final String NOTES_ISPIN = "tags_pin";
    public static final String NOTES_WIDGET = "tags_widget";
    public static final String NOTES_FOLDERID = "tags_folderId";
    public static final String NOTES_PIN_ORDER = "tags_pinOrder";
    public static final String NOTES_IMAGE_ORIENT_CODE = "tags_imageOrient";
    public static final String NOTES_TEXT_TITLE_BOLD = "tags_text_title_bold";
    public static final String NOTES_TEXT_TITLE_ITALIC = "tags_text_title_italic";
    public static final String NOTES_TEXT_TITLE_UNDERLINE = "tags_text_title_underline";
    public static final String NOTES_TEXT_TITLE_STRIKE = "tags_text_title_strike";
    public static final String NOTES_TEXT_CONTENT_BOLD = "tags_text_content_bold";
    public static final String NOTES_TEXT_CONTENT_ITALIC = "tags_text_content_italic";
    public static final String NOTES_TEXT_CONTENT_UNDERLINE = "tags_text_content_underline";
    public static final String NOTES_TEXT_CONTENT_STRIKE = "tags_text_content_strike";
    public static final String NOTES_TEXT_ALIGN = "tags_text_align";

    public NotesDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TAGS_TABLE = "CREATE TABLE " + TAGS_TABLE_NAME + "("
                + TAGS_ID + " INTEGER PRIMARY KEY,"
                + TAGS_NAME + " VARCHAR,"
                + TAGS_COLOR_CODE + " TEXT,"
                + TAGS_COUNTER_NOTES + " INTEGER" + ")";

        sqLiteDatabase.execSQL(CREATE_TAGS_TABLE);
        String CREATE_NOTES_TABLE = "CREATE TABLE " + NOTES_TABLE_NAME + "("
                + NOTES_ID + " INTEGER PRIMARY KEY,"
                + NOTES_TAGS_ID + " INTEGER,"
                + NOTE_TITLE + " VARCHAR,"
                + NOTES_CONTENT + " VARCHAR,"
                + NOTES_TIME + " VARCHAR,"
                + NOTES_IMAGE + " BLOB,"
                + NOTES_ISLOCK + " INTEGER,"
                + NOTES_ISDELETE + " INTEGER,"
                + NOTES_ISPIN + " INTEGER,"
                + NOTES_WIDGET + " INTEGER,"
                + NOTES_FOLDERID + " INTEGER,"
                + NOTES_PIN_ORDER + " INTEGER,"
                + NOTES_IMAGE_ORIENT_CODE + " INTEGER,"
                + NOTES_TEXT_TITLE_BOLD + " INTEGER,"
                + NOTES_TEXT_TITLE_ITALIC + " INTEGER,"
                + NOTES_TEXT_TITLE_UNDERLINE + " INTEGER,"
                + NOTES_TEXT_TITLE_STRIKE + " INTEGER,"
                + NOTES_TEXT_CONTENT_BOLD + " INTEGER,"
                + NOTES_TEXT_CONTENT_ITALIC + " INTEGER,"
                + NOTES_TEXT_CONTENT_UNDERLINE + " INTEGER,"
                + NOTES_TEXT_CONTENT_STRIKE + " INTEGER,"
                + NOTES_TEXT_ALIGN + " INTEGER" + ")";

        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //todo insert Tags
    public void insertTags(Tags tags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAGS_NAME, tags.getTagName());
        contentValues.put(TAGS_COLOR_CODE, tags.getColorCodeId());
        contentValues.put(TAGS_COUNTER_NOTES, tags.getCounterNote());
        db.insert(TAGS_TABLE_NAME, null, contentValues);
    }

    //todo get count tag record
    public long getTagsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TAGS_TABLE_NAME);
        return count;
    }

    //todo get tags data
    public ArrayList<Tags> getTags() {
        ArrayList<Tags> tagsArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String table_name = "SELECT *FROM " + TAGS_TABLE_NAME;
        Cursor cursor = db.rawQuery(table_name, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Tags tags = new Tags(cursor.getInt(cursor.getColumnIndex(TAGS_ID))
                        , cursor.getString(cursor.getColumnIndex(TAGS_NAME))
                        , cursor.getInt(cursor.getColumnIndex(TAGS_COLOR_CODE))
                        , cursor.getInt(cursor.getColumnIndex(TAGS_COUNTER_NOTES)));
                tagsArrayList.add(tags);
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }

    //todo tags update
    public boolean updateTags(Tags tags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAGS_ID, tags.getId());
        contentValues.put(TAGS_NAME, tags.getTagName());
        contentValues.put(TAGS_COLOR_CODE, tags.getColorCodeId());
        contentValues.put(TAGS_COUNTER_NOTES, tags.getCounterNote());

        db.update(TAGS_TABLE_NAME, contentValues, TAGS_ID + " = ?", new String[]{String.valueOf(tags.getId())});
        return true;
    }

    //todo get record of tags
    public Tags getTagsRecord(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TAGS_TABLE_NAME + " WHERE " + TAGS_ID + "=? ", new String[]{String.valueOf(id)});
        cursor.moveToFirst();

        @SuppressLint("Range") Tags user = new Tags(cursor.getInt(cursor.getColumnIndex(TAGS_ID))
                , cursor.getString(cursor.getColumnIndex(TAGS_NAME))
                , cursor.getInt(cursor.getColumnIndex(TAGS_COLOR_CODE))
                , cursor.getInt(cursor.getColumnIndex(TAGS_COUNTER_NOTES)));
        cursor.close();

        return user;
    }

    //todo get record of tags
    public Tags getTagCount(int count) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TAGS_TABLE_NAME + " WHERE " + TAGS_ID + "=? ", new String[]{String.valueOf(count)});
        cursor.moveToFirst();

        @SuppressLint("Range") Tags user = new Tags(cursor.getInt(cursor.getColumnIndex(TAGS_ID))
                , cursor.getString(cursor.getColumnIndex(TAGS_NAME))
                , cursor.getInt(cursor.getColumnIndex(TAGS_COLOR_CODE))
                , cursor.getInt(cursor.getColumnIndex(TAGS_COUNTER_NOTES)));
        cursor.close();
        return user;
    }

    //todo delete tags
    public void deleteTags(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TAGS_TABLE_NAME, TAGS_ID + "= ?", new String[]{String.valueOf(id)});
    }


    //todo insert Notes
    public void insertNotes(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_TAGS_ID, note.getTagId());
        contentValues.put(NOTE_TITLE, note.getNoteTitle());
        contentValues.put(NOTES_CONTENT, note.getNoteContent());
        contentValues.put(NOTES_TIME, note.getDateTimeMills().getTime());
        contentValues.put(NOTES_IMAGE, note.getImgByteFormat());
        contentValues.put(NOTES_ISLOCK, note.isLockedOrNot());
        contentValues.put(NOTES_ISDELETE, note.isDeletedOrNot());
        contentValues.put(NOTES_ISPIN, note.isPinnedOrNot());
        contentValues.put(NOTES_WIDGET, note.getCreateWidgetId());
        contentValues.put(NOTES_FOLDERID, note.getTagFolderId());
        contentValues.put(NOTES_PIN_ORDER, note.getIntPinOrder());
        contentValues.put(NOTES_IMAGE_ORIENT_CODE, note.getImgOrientionCode());
        contentValues.put(NOTES_TEXT_TITLE_BOLD, note.isTitleBold());
        contentValues.put(NOTES_TEXT_TITLE_ITALIC, note.isTitleItalic());
        contentValues.put(NOTES_TEXT_TITLE_UNDERLINE, note.isTitleUnderline());
        contentValues.put(NOTES_TEXT_TITLE_STRIKE, note.isTitleStrike());
        contentValues.put(NOTES_TEXT_CONTENT_BOLD, note.isContentBold());
        contentValues.put(NOTES_TEXT_CONTENT_ITALIC, note.isContentUnderline());
        contentValues.put(NOTES_TEXT_CONTENT_UNDERLINE, note.isContentUnderline());
        contentValues.put(NOTES_TEXT_CONTENT_STRIKE, note.isContentStrike());
        contentValues.put(NOTES_TEXT_ALIGN, note.getAlign());
        db.insert(NOTES_TABLE_NAME, null, contentValues);
    }

    //todo get count Note record
    public long getNotesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, NOTES_TABLE_NAME);
        return count;
    }
/*
    //todo IsPin count for tags table
    @SuppressLint("Range")
    public ArrayList<Note> getIsPin(int i1, int id) {
        ArrayList<Note> tagsArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_ISPIN + "=? AND " + NOTES_TAGS_ID + "=?", new String[]{String.valueOf(i1), String.valueOf(id)});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                    isLock = false;
                } else {
                    isLock = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                    isDeleted = false;
                } else {
                    isDeleted = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                    isPin = false;
                } else {
                    isPin = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                    IsTitleBold = false;
                } else {
                    IsTitleBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                    IsTitleItalic = false;
                } else {
                    IsTitleItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                    IsTitleUnderline = false;
                } else {
                    IsTitleUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                    IsTitleStrike = false;
                } else {
                    IsTitleStrike = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                    IsContentBold = false;
                } else {
                    IsContentBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                    IsContentItalic = false;
                } else {
                    IsContentItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                    IsContentUnderline = false;
                } else {
                    IsContentUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                    IsContentStrike = false;
                } else {
                    IsContentStrike = true;
                }
                @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                        , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                        , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                        , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                        , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                        , isDeleted
                        , isLock
                        , isPin
                        , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                        , IsTitleBold
                        , IsTitleItalic
                        , IsTitleUnderline
                        , IsTitleStrike
                        , IsContentBold
                        , IsContentItalic
                        , IsContentUnderline
                        , IsContentStrike
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
                tagsArrayList.add(user);
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }*/

    //todo IsPin count for tags table
    @SuppressLint("Range")
    public ArrayList<Note> getSearchIsPin(String content, int i1, int id) {
        ArrayList<Note> tagsArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_ISPIN + "=? AND " + NOTES_TAGS_ID + "=?", new String[]{String.valueOf(i1), String.valueOf(id)});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                if (content.contains(cursor.getString(cursor.getColumnIndex(NOTE_TITLE))) || content.contains(cursor.getString(cursor.getColumnIndex(NOTES_CONTENT)))) {
                    boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                        isLock = false;
                    } else {
                        isLock = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                        isDeleted = false;
                    } else {
                        isDeleted = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                        isPin = false;
                    } else {
                        isPin = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                        IsTitleBold = false;
                    } else {
                        IsTitleBold = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                        IsTitleItalic = false;
                    } else {
                        IsTitleItalic = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                        IsTitleUnderline = false;
                    } else {
                        IsTitleUnderline = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                        IsTitleStrike = false;
                    } else {
                        IsTitleStrike = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                        IsContentBold = false;
                    } else {
                        IsContentBold = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                        IsContentItalic = false;
                    } else {
                        IsContentItalic = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                        IsContentUnderline = false;
                    } else {
                        IsContentUnderline = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                        IsContentStrike = false;
                    } else {
                        IsContentStrike = true;
                    }
                    @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                            , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                            , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                            , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                            , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                            , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                            , isDeleted
                            , isLock
                            , isPin
                            , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                            , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                            , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                            , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                            , IsTitleBold
                            , IsTitleItalic
                            , IsTitleUnderline
                            , IsTitleStrike
                            , IsContentBold
                            , IsContentItalic
                            , IsContentUnderline
                            , IsContentStrike
                            , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
                    tagsArrayList.add(user);
                }
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }

    //todo IsPinOrder count for tags table
    @SuppressLint("Range")
    public int getIsPinOrder(int i) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_ID + "=? ", new String[]{String.valueOf(i)});
        cursor.moveToFirst();
        count = cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER));
        cursor.close();
        return count;
    }

    //todo get deleted false record of tags
    @SuppressLint("Range")
    public ArrayList<Note> getIsDelete(int isDelete,int isPined) {
        ArrayList<Note> tagsArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_ISDELETE + "=? AND "+NOTES_ISPIN+"=? ", new String[]{String.valueOf(isDelete),String.valueOf(isPined)});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                    isLock = false;
                } else {
                    isLock = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                    isDeleted = false;
                } else {
                    isDeleted = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                    isPin = false;
                } else {
                    isPin = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                    IsTitleBold = false;
                } else {
                    IsTitleBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                    IsTitleItalic = false;
                } else {
                    IsTitleItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                    IsTitleUnderline = false;
                } else {
                    IsTitleUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                    IsTitleStrike = false;
                } else {
                    IsTitleStrike = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                    IsContentBold = false;
                } else {
                    IsContentBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                    IsContentItalic = false;
                } else {
                    IsContentItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                    IsContentUnderline = false;
                } else {
                    IsContentUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                    IsContentStrike = false;
                } else {
                    IsContentStrike = true;
                }
                @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                        , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                        , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                        , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                        , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                        , isDeleted
                        , isLock
                        , isPin
                        , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                        , IsTitleBold
                        , IsTitleItalic
                        , IsTitleUnderline
                        , IsTitleStrike
                        , IsContentBold
                        , IsContentItalic
                        , IsContentUnderline
                        , IsContentStrike
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
                tagsArrayList.add(user);
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }

    //todo get deleted false record of tags
    @SuppressLint("Range")
    public ArrayList<Note> getSearchIsDelete(String content, int isDelete) {
        ArrayList<Note> tagsArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_ISDELETE + "=? ", new String[]{String.valueOf(isDelete)});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                if (content.contains(cursor.getString(cursor.getColumnIndex(NOTE_TITLE))) || content.contains(cursor.getString(cursor.getColumnIndex(NOTES_CONTENT)))) {
                    boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                        isLock = false;
                    } else {
                        isLock = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                        isDeleted = false;
                    } else {
                        isDeleted = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                        isPin = false;
                    } else {
                        isPin = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                        IsTitleBold = false;
                    } else {
                        IsTitleBold = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                        IsTitleItalic = false;
                    } else {
                        IsTitleItalic = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                        IsTitleUnderline = false;
                    } else {
                        IsTitleUnderline = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                        IsTitleStrike = false;
                    } else {
                        IsTitleStrike = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                        IsContentBold = false;
                    } else {
                        IsContentBold = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                        IsContentItalic = false;
                    } else {
                        IsContentItalic = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                        IsContentUnderline = false;
                    } else {
                        IsContentUnderline = true;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                        IsContentStrike = false;
                    } else {
                        IsContentStrike = true;
                    }
                    @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                            , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                            , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                            , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                            , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                            , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                            , isDeleted
                            , isLock
                            , isPin
                            , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                            , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                            , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                            , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                            , IsTitleBold
                            , IsTitleItalic
                            , IsTitleUnderline
                            , IsTitleStrike
                            , IsContentBold
                            , IsContentItalic
                            , IsContentUnderline
                            , IsContentStrike
                            , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
                    tagsArrayList.add(user);
                }
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }

    //todo get deleted or Not record of tags
    @SuppressLint("Range")
    public ArrayList<Note> getFolderIdWithIsDeleteOrNot(int folderId, int isDelete, int isPinEd) {
        ArrayList<Note> tagsArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_TAGS_ID + "=? AND " + NOTES_ISDELETE + "=? AND " + NOTES_ISPIN + "=?", new String[]{String.valueOf(folderId), String.valueOf(isDelete), String.valueOf(isPinEd)});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                    isLock = false;
                } else {
                    isLock = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                    isDeleted = false;
                } else {
                    isDeleted = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                    isPin = false;
                } else {
                    isPin = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                    IsTitleBold = false;
                } else {
                    IsTitleBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                    IsTitleItalic = false;
                } else {
                    IsTitleItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                    IsTitleUnderline = false;
                } else {
                    IsTitleUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                    IsTitleStrike = false;
                } else {
                    IsTitleStrike = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                    IsContentBold = false;
                } else {
                    IsContentBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                    IsContentItalic = false;
                } else {
                    IsContentItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                    IsContentUnderline = false;
                } else {
                    IsContentUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                    IsContentStrike = false;
                } else {
                    IsContentStrike = true;
                }
                @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                        , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                        , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                        , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                        , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                        , isDeleted
                        , isLock
                        , isPin
                        , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                        , IsTitleBold
                        , IsTitleItalic
                        , IsTitleUnderline
                        , IsTitleStrike
                        , IsContentBold
                        , IsContentItalic
                        , IsContentUnderline
                        , IsContentStrike
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
                tagsArrayList.add(user);
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }

    //todo get deleted or Not record of tags
    @SuppressLint("Range")
    public ArrayList<Note> getSearchData(String title, String content, int folderId, int isDelete, int isPinEd) {
        ArrayList<Note> tagsArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTE_TITLE + " LIKE ? AND " + NOTES_CONTENT + " LIKE ? AND " + NOTES_TAGS_ID + "=? AND " + NOTES_ISDELETE + "=? AND " + NOTES_ISPIN + "=? ", new String[]{String.valueOf("%" + title + "%"), String.valueOf("%" + content + "%"), String.valueOf(folderId), String.valueOf(isDelete), String.valueOf(isPinEd)});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                    isLock = false;
                } else {
                    isLock = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                    isDeleted = false;
                } else {
                    isDeleted = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                    isPin = false;
                } else {
                    isPin = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                    IsTitleBold = false;
                } else {
                    IsTitleBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                    IsTitleItalic = false;
                } else {
                    IsTitleItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                    IsTitleUnderline = false;
                } else {
                    IsTitleUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                    IsTitleStrike = false;
                } else {
                    IsTitleStrike = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                    IsContentBold = false;
                } else {
                    IsContentBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                    IsContentItalic = false;
                } else {
                    IsContentItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                    IsContentUnderline = false;
                } else {
                    IsContentUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                    IsContentStrike = false;
                } else {
                    IsContentStrike = true;
                }

                @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                        , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                        , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                        , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                        , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                        , isDeleted
                        , isLock
                        , isPin
                        , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                        , IsTitleBold
                        , IsTitleItalic
                        , IsTitleUnderline
                        , IsTitleStrike
                        , IsContentBold
                        , IsContentItalic
                        , IsContentUnderline
                        , IsContentStrike
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
                tagsArrayList.add(user);
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }

    //todo get record of Note
    @SuppressLint("Range")
    public Note getNoteRecord(int id) {
        System.out.println("--- isididi L " + id);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_ID + "=? ", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
        if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
            isLock = false;
        } else {
            isLock = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
            isDeleted = false;
        } else {
            isDeleted = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
            isPin = false;
        } else {
            isPin = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
            IsTitleBold = false;
        } else {
            IsTitleBold = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
            IsTitleItalic = false;
        } else {
            IsTitleItalic = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
            IsTitleUnderline = false;
        } else {
            IsTitleUnderline = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
            IsTitleStrike = false;
        } else {
            IsTitleStrike = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
            IsContentBold = false;
        } else {
            IsContentBold = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
            IsContentItalic = false;
        } else {
            IsContentItalic = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
            IsContentUnderline = false;
        } else {
            IsContentUnderline = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
            IsContentStrike = false;
        } else {
            IsContentStrike = true;
        }
        @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                , isDeleted
                , isLock
                , isPin
                , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                , IsTitleBold
                , IsTitleItalic
                , IsTitleUnderline
                , IsTitleStrike
                , IsContentBold
                , IsContentItalic
                , IsContentUnderline
                , IsContentStrike
                , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
        cursor.close();
        return user;
    }

    //todo get record of deleteTagNote
    @SuppressLint("Range")
    public Note deleteTagsNoteRecord(long id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_TAGS_ID + "=? ", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
        if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
            isLock = false;
        } else {
            isLock = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
            isDeleted = false;
        } else {
            isDeleted = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
            isPin = false;
        } else {
            isPin = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
            IsTitleBold = false;
        } else {
            IsTitleBold = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
            IsTitleItalic = false;
        } else {
            IsTitleItalic = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
            IsTitleUnderline = false;
        } else {
            IsTitleUnderline = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
            IsTitleStrike = false;
        } else {
            IsTitleStrike = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
            IsContentBold = false;
        } else {
            IsContentBold = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
            IsContentItalic = false;
        } else {
            IsContentItalic = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
            IsContentUnderline = false;
        } else {
            IsContentUnderline = true;
        }
        if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
            IsContentStrike = false;
        } else {
            IsContentStrike = true;
        }
        @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                , isDeleted
                , isLock
                , isPin
                , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                , IsTitleBold
                , IsTitleItalic
                , IsTitleUnderline
                , IsTitleStrike
                , IsContentBold
                , IsContentItalic
                , IsContentUnderline
                , IsContentStrike
                , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
        cursor.close();
        return user;
    }

    //todo get record of widgetId
    @SuppressLint("Range")
    public Note getWidgetId(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_WIDGET + "=? ", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        Note user = null;
        if (cursor.getCount() > 0) {
            boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
            if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                isLock = false;
            } else {
                isLock = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                isDeleted = false;
            } else {
                isDeleted = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                isPin = false;
            } else {
                isPin = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                IsTitleBold = false;
            } else {
                IsTitleBold = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                IsTitleItalic = false;
            } else {
                IsTitleItalic = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                IsTitleUnderline = false;
            } else {
                IsTitleUnderline = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                IsTitleStrike = false;
            } else {
                IsTitleStrike = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                IsContentBold = false;
            } else {
                IsContentBold = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                IsContentItalic = false;
            } else {
                IsContentItalic = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                IsContentUnderline = false;
            } else {
                IsContentUnderline = true;
            }
            if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                IsContentStrike = false;
            } else {
                IsContentStrike = true;
            }
            user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                    , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                    , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                    , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                    , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                    , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                    , isDeleted
                    , isLock
                    , isPin
                    , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                    , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                    , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                    , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                    , IsTitleBold
                    , IsTitleItalic
                    , IsTitleUnderline
                    , IsTitleStrike
                    , IsContentBold
                    , IsContentItalic
                    , IsContentUnderline
                    , IsContentStrike
                    , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
            cursor.close();
        }
        return user;
    }

    //todo get all tags
    @SuppressLint("Range")
    public ArrayList<Note> getAllNotes(int isDelete) {
        ArrayList<Note> tagsArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_ISDELETE + "=? ", new String[]{String.valueOf(isDelete)});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                    isLock = false;
                } else {
                    isLock = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                    isDeleted = false;
                } else {
                    isDeleted = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                    isPin = false;
                } else {
                    isPin = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                    IsTitleBold = false;
                } else {
                    IsTitleBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                    IsTitleItalic = false;
                } else {
                    IsTitleItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                    IsTitleUnderline = false;
                } else {
                    IsTitleUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                    IsTitleStrike = false;
                } else {
                    IsTitleStrike = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                    IsContentBold = false;
                } else {
                    IsContentBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                    IsContentItalic = false;
                } else {
                    IsContentItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                    IsContentUnderline = false;
                } else {
                    IsContentUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                    IsContentStrike = false;
                } else {
                    IsContentStrike = true;
                }
                @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                        , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                        , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                        , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                        , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                        , isDeleted
                        , isLock
                        , isPin
                        , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                        , IsTitleBold
                        , IsTitleItalic
                        , IsTitleUnderline
                        , IsTitleStrike
                        , IsContentBold
                        , IsContentItalic
                        , IsContentUnderline
                        , IsContentStrike
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
                tagsArrayList.add(user);
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }


    //todo note update
    public boolean updateNotes(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_TAGS_ID, note.getTagId());
        contentValues.put(NOTE_TITLE, note.getNoteTitle());
        contentValues.put(NOTES_CONTENT, note.getNoteContent());
        contentValues.put(NOTES_TIME, note.getDateTimeMills().getTime());
        contentValues.put(NOTES_IMAGE, note.getImgByteFormat());
        contentValues.put(NOTES_ISLOCK, note.isLockedOrNot());
        contentValues.put(NOTES_ISDELETE, note.isDeletedOrNot());
        contentValues.put(NOTES_ISPIN, note.isPinnedOrNot());
        contentValues.put(NOTES_WIDGET, note.getCreateWidgetId());
        contentValues.put(NOTES_FOLDERID, note.getTagFolderId());
        contentValues.put(NOTES_PIN_ORDER, note.getIntPinOrder());
        contentValues.put(NOTES_IMAGE_ORIENT_CODE, note.getImgOrientionCode());
        contentValues.put(NOTES_TEXT_TITLE_BOLD, note.isTitleBold());
        contentValues.put(NOTES_TEXT_TITLE_ITALIC, note.isTitleItalic());
        contentValues.put(NOTES_TEXT_TITLE_UNDERLINE, note.isTitleUnderline());
        contentValues.put(NOTES_TEXT_TITLE_STRIKE, note.isTitleStrike());
        contentValues.put(NOTES_TEXT_CONTENT_BOLD, note.isContentBold());
        contentValues.put(NOTES_TEXT_CONTENT_ITALIC, note.isContentItalic());
        contentValues.put(NOTES_TEXT_CONTENT_UNDERLINE, note.isContentUnderline());
        contentValues.put(NOTES_TEXT_CONTENT_STRIKE, note.isContentStrike());
        contentValues.put(NOTES_TEXT_ALIGN, note.getAlign());
        System.out.println("----- UPDATE : " + note.toString());
        db.update(NOTES_TABLE_NAME, contentValues, NOTES_ID + " = ?", new String[]{String.valueOf(note.getId())});
        return true;
    }

    //todo note update
    public boolean updateNotesTags(Note note, int id, long noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_TAGS_ID, note.getTagId());
        contentValues.put(NOTE_TITLE, note.getNoteTitle());
        contentValues.put(NOTES_CONTENT, note.getNoteContent());
        contentValues.put(NOTES_TIME, note.getDateTimeMills().getTime());
        contentValues.put(NOTES_IMAGE, note.getImgByteFormat());
        contentValues.put(NOTES_ISLOCK, note.isLockedOrNot());
        contentValues.put(NOTES_ISDELETE, note.isDeletedOrNot());
        contentValues.put(NOTES_ISPIN, note.isPinnedOrNot());
        contentValues.put(NOTES_WIDGET, note.getCreateWidgetId());
        contentValues.put(NOTES_FOLDERID, note.getTagFolderId());
        contentValues.put(NOTES_PIN_ORDER, note.getIntPinOrder());
        contentValues.put(NOTES_IMAGE_ORIENT_CODE, note.getImgOrientionCode());
        contentValues.put(NOTES_TEXT_TITLE_BOLD, note.isTitleBold());
        contentValues.put(NOTES_TEXT_TITLE_ITALIC, note.isTitleItalic());
        contentValues.put(NOTES_TEXT_TITLE_UNDERLINE, note.isTitleUnderline());
        contentValues.put(NOTES_TEXT_TITLE_STRIKE, note.isTitleStrike());
        contentValues.put(NOTES_TEXT_CONTENT_BOLD, note.isContentBold());
        contentValues.put(NOTES_TEXT_CONTENT_ITALIC, note.isContentItalic());
        contentValues.put(NOTES_TEXT_CONTENT_UNDERLINE, note.isContentUnderline());
        contentValues.put(NOTES_TEXT_CONTENT_STRIKE, note.isContentStrike());
        contentValues.put(NOTES_TEXT_ALIGN, note.getAlign());
        db.update(NOTES_TABLE_NAME, contentValues, NOTES_TAGS_ID + " = ? AND " + NOTES_ID + " = ?", new String[]{String.valueOf(id), String.valueOf(noteId)});
        return true;
    }

    //todo delete note
    public void deleteNote(int id, long noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTES_TABLE_NAME, NOTES_TAGS_ID + " = ? AND " + NOTES_ID + " = ?", new String[]{String.valueOf(id), String.valueOf(noteId)});
    }

    //todo widgetlist
    @SuppressLint("Range")
    public ArrayList<Note> getAllWidget(int i) {
        ArrayList<Note> tagsArrayList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_ISDELETE + "=? ", new String[]{String.valueOf(i)});
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                boolean isLock, isDeleted, isPin, IsTitleBold, IsTitleItalic, IsTitleUnderline, IsTitleStrike, IsContentBold, IsContentItalic, IsContentUnderline, IsContentStrike;
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISLOCK)) == 0) {
                    isLock = false;
                } else {
                    isLock = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISDELETE)) == 0) {
                    isDeleted = false;
                } else {
                    isDeleted = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_ISPIN)) == 0) {
                    isPin = false;
                } else {
                    isPin = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_BOLD)) == 0) {
                    IsTitleBold = false;
                } else {
                    IsTitleBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_ITALIC)) == 0) {
                    IsTitleItalic = false;
                } else {
                    IsTitleItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_UNDERLINE)) == 0) {
                    IsTitleUnderline = false;
                } else {
                    IsTitleUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_TITLE_STRIKE)) == 0) {
                    IsTitleStrike = false;
                } else {
                    IsTitleStrike = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_BOLD)) == 0) {
                    IsContentBold = false;
                } else {
                    IsContentBold = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_ITALIC)) == 0) {
                    IsContentItalic = false;
                } else {
                    IsContentItalic = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_UNDERLINE)) == 0) {
                    IsContentUnderline = false;
                } else {
                    IsContentUnderline = true;
                }
                if (cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_CONTENT_STRIKE)) == 0) {
                    IsContentStrike = false;
                } else {
                    IsContentStrike = true;
                }
                @SuppressLint("Range") Note user = new Note(cursor.getInt(cursor.getColumnIndex(NOTES_ID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TAGS_ID))
                        , cursor.getString(cursor.getColumnIndex(NOTE_TITLE))
                        , cursor.getString(cursor.getColumnIndex(NOTES_CONTENT))
                        , new Date(cursor.getLong(cursor.getColumnIndex(NOTES_TIME)))
                        , cursor.getBlob(cursor.getColumnIndex(NOTES_IMAGE))
                        , isDeleted
                        , isLock
                        , isPin
                        , cursor.getInt(cursor.getColumnIndex(NOTES_WIDGET))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_FOLDERID))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_PIN_ORDER))
                        , cursor.getInt(cursor.getColumnIndex(NOTES_IMAGE_ORIENT_CODE))
                        , IsTitleBold
                        , IsTitleItalic
                        , IsTitleUnderline
                        , IsTitleStrike
                        , IsContentBold
                        , IsContentItalic
                        , IsContentUnderline
                        , IsContentStrike
                        , cursor.getInt(cursor.getColumnIndex(NOTES_TEXT_ALIGN)));
                tagsArrayList.add(user);
            } while (cursor.moveToNext());
        }
        return tagsArrayList;
    }

    public boolean checkRecordExist(String columnName, String columnValue) {
        SQLiteDatabase objDatabase;
        objDatabase = getReadableDatabase();
        Cursor cursor;
        if (!columnName.isEmpty() && !columnValue.isEmpty()) {
            cursor = objDatabase.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTE_TITLE + "=? AND " + NOTES_CONTENT + "=? ", new String[]{columnName, columnValue});
        } else if (!columnName.isEmpty()) {
            cursor = objDatabase.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTE_TITLE + "=? ", new String[]{columnName});
        } else {
            cursor = objDatabase.rawQuery("SELECT * FROM " + NOTES_TABLE_NAME + " WHERE " + NOTES_CONTENT + "=? ", new String[]{columnValue});
        }
        if (cursor.moveToFirst()) {
            Log.d("Record  Already Exists", "Table is:" + NOTES_TABLE_NAME + " ColumnName:" + columnName);
            return true;
        }
        Log.d("New Record  ", "Table is:" + NOTES_TABLE_NAME + " ColumnName:" + columnName + " Column Value:" + columnValue);
        return false;
    }
}
