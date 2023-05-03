package com.note.iosnotes.ui.Activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.divyanshu.draw.activity.DrawingActivity;
import com.ligl.android.widget.iosdialog.IOSSheetDialog;
import com.note.iosnotes.BuildConfig;
import com.note.iosnotes.Model.Note;
import com.note.iosnotes.Model.Tags;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.BottomTextMenu;
import com.note.iosnotes.Utils.BottomView;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.Utils.TinyDB;
import com.note.iosnotes.dialog.AddNewTagDialog;
import com.note.iosnotes.dialog.ChooseCreatedTagDialog;
import com.note.iosnotes.dialog.NoteDeleteDialog;
import com.note.iosnotes.dialog.SettingPasswordDialog;
import com.note.iosnotes.provider.NoteWidgetCreatedReceiver;
import com.note.iosnotes.provider.NoteWidgetProvider;
import com.note.iosnotes.provider.NoteWidgetService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

public class ActivityNewCreateNotes extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    public static final int REQ_TAKE_PHOTO = 301;
    public static final int REQ_PICK_IMAGE = 300;
    private static final int DRAWING_CODE = 305;
    private static final int QR_CODE = 304;
    private Date dateModified;
    private int folderId;
    private int noteAction;
    private String folderName;
    private long id;
    int originFolderId;
    private byte[] imgByteArr;
    private int imgOrientCode;
    private boolean isDeleted;
    private boolean isLocked;
    private boolean isPinned;
    private String noteContent;
    private String noteTitle;
    private int pinOrder;
    private static int widgetId;

    private boolean titleBold;
    private boolean titleItalic;
    private boolean titleUnderline;
    private boolean titleStrike;
    private boolean contentBold;
    private boolean contentItalic;
    private boolean contentUnderline;
    private boolean contentStrike;
    private int align;
    private TextView TvFolderNameNote;
    private RelativeLayout RlAttachViewer;
    private ImageView IvBackCreateNotes;
    private EditText EdtCreateNoteTitle;
    private EditText EdtCreateNote;
    private ImageView IvAttach;
    private ImageView IvDeleteNote;
    private ImageView IvCamera;
    private ImageView IvDrawing;
    private ImageView IvEdit;
    private ImageView IvMoreCreateNotes;
    private TextView TvDoneEditNote;
    private ScrollView SvNote;
    private ImageButton IvBtnRemoveAttach;
    private String password;
    private Note mNoteView;
    private NotesDatabaseHelper noteHelper;
    private boolean isNoteEdited = false;
    private boolean isNoteMoved = false;
    public int returnResult;
    private String mPhotoTakenPath = null;
    private CAMERA_ACTION cameraAction;
    private NoteDeleteDialog deleteNoteDialog;
    private boolean isInserted = false;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_create_notes);
        initViews();
        getIntents();
        getNoteData();
        initListeners();
        initActions();
    }

    enum CAMERA_ACTION {
        SCAN_PHOTO,
        TAKE_PHOTO,
        CHOOSE_PHOTO,
        UNDEFINED
    }

    private void initViews() {
        context = this;
        noteHelper = new NotesDatabaseHelper(context);
        TvFolderNameNote = (TextView) findViewById(R.id.TvFolderNameNote);
        RlAttachViewer = (RelativeLayout) findViewById(R.id.RlAttachViewer);
        EdtCreateNoteTitle = (EditText) findViewById(R.id.EdtCreateNoteTitle);
        EdtCreateNote = (EditText) findViewById(R.id.EdtCreateNote);
        IvAttach = (ImageView) findViewById(R.id.IvAttach);
        IvDeleteNote = (ImageView) findViewById(R.id.IvDeleteNote);
        IvCamera = (ImageView) findViewById(R.id.IvCamera);
        IvDrawing = (ImageView) findViewById(R.id.IvDrawing);
        IvEdit = (ImageView) findViewById(R.id.IvEdit);
        IvBackCreateNotes = (ImageView) findViewById(R.id.IvBackCreateNotes);
        IvMoreCreateNotes = (ImageView) findViewById(R.id.IvMoreCreateNotes);
        TvDoneEditNote = (TextView) findViewById(R.id.TvDoneEditNote);
        SvNote = (ScrollView) findViewById(R.id.SvNote);
        IvBtnRemoveAttach = (ImageButton) findViewById(R.id.IvBtnRemoveAttach);
    }

    private void getIntents() {
        Intent intent = getIntent();
//        folderName = intent.getStringExtra(Constant.TAGS_NAME);
//        folderId = intent.getIntExtra(Constant.TAGS_ID, 0);
//        noteAction = intent.getIntExtra(Constant.NOTE_ACTION, 0);
        Note note = null;
        int intExtra = intent.getIntExtra(Constant.FOLDER_ID, 0);
        folderId = intExtra;
        originFolderId = intExtra;
        folderName = intent.getStringExtra(Constant.TAGS_NAME);
        noteAction = intent.getIntExtra(Constant.NOTE_ACTION, 0);
        id = getIntent().getLongExtra(Constant.TAGS_ID, 0);
        int intExtra2 = intent.getIntExtra(Constant.WIDGET_ID, -1);
        if (intExtra2 != -1 && (noteHelper.getWidgetId(intExtra2)) != null) {
            int folderId2 = note != null ? note.getFolderId() : 0;
            folderId = folderId2;
            originFolderId = folderId2;
            id = note != null ? note.getId() : 0;
            noteAction = 1;
            Tags tag = noteHelper.getTagsRecord(folderId);
            if (tag != null) {
                folderName = tag.getTagName();
            }
        }

        System.out.println("-------F_ID - id iiii : " + folderId + " - " + id);
    }

    private void getNoteData() {
        System.out.println("------- save iiii : " + noteAction);
        if (noteAction == 1) {
            mNoteView = (Note) noteHelper.getNoteRecord((int) id);
            password = new TinyDB(this).getString(Constant.PASSWORD);
            Note note = mNoteView;
            if (note != null) {
//                id = note.getTagId();
                noteTitle = note.getNoteTitle();
                noteContent = mNoteView.getNoteContent();
                dateModified = mNoteView.getDateModified();
                imgOrientCode = mNoteView.getImgOrientCode();
                imgByteArr = mNoteView.getImgByteArr();
                isPinned = mNoteView.isPinned();
                pinOrder = mNoteView.getPinOrder();
                isLocked = mNoteView.isLocked();
                isDeleted = mNoteView.isDeleted();
                widgetId = mNoteView.getWidgetId();
                titleBold = mNoteView.isTitleBold();
                titleItalic = mNoteView.isTitleItalic();
                titleUnderline = mNoteView.isTitleUnderline();
                titleStrike = mNoteView.isTitleStrike();
                contentBold = mNoteView.isContentBold();
                contentItalic = mNoteView.isContentItalic();
                contentUnderline = mNoteView.isContentUnderline();
                contentStrike = mNoteView.isContentStrike();
                align = mNoteView.getAlign();
                return;
            }
            return;
        }
        noteTitle = "";
        noteContent = "";
        password = new TinyDB(this).getString(Constant.PASSWORD);
        dateModified = null;
        imgOrientCode = 0;
        imgByteArr = null;
        isPinned = false;
        pinOrder = 0;
        isLocked = false;
        isDeleted = false;
        widgetId = -1;
        titleBold = true;
        titleItalic = false;
        titleUnderline = false;
        titleStrike = false;
        contentBold = false;
        contentItalic = false;
        contentUnderline = false;
        contentStrike = false;
        align = 1;
    }

    private void collectNoteData() {
        if (this.noteAction == 0) {
            this.noteTitle = this.EdtCreateNoteTitle.getText().toString();
            this.noteContent = this.EdtCreateNote.getText().toString();
            this.dateModified = Calendar.getInstance().getTime();
            return;
        }
        String obj = this.EdtCreateNoteTitle.getText().toString();
        String obj2 = this.EdtCreateNote.getText().toString();
        if (!this.EdtCreateNoteTitle.equals(obj) || obj2.length() != this.EdtCreateNote.length()) {
            this.isNoteEdited = true;
        }
        this.noteTitle = obj;
        this.noteContent = obj2;
    }

    private void initListeners() {
        IvMoreCreateNotes.setOnClickListener(this);
        IvBackCreateNotes.setOnClickListener(this);
        IvCamera.setOnClickListener(this);
        IvDrawing.setOnClickListener(this);
        IvBtnRemoveAttach.setOnClickListener(this);
        IvDeleteNote.setOnClickListener(this);
        IvEdit.setOnClickListener(this);
    }

    private void initActions() {
        TvFolderNameNote.setText(folderName);
        EdtCreateNote.setText(noteContent);
        EdtCreateNoteTitle.setText(noteTitle);
        EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_bold));
        if (Constant.getBitmap(imgByteArr, imgOrientCode) == null) {
            RlAttachViewer.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(Constant.getBitmap(imgByteArr, imgOrientCode))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                    .into(IvAttach);
            RlAttachViewer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        saveNote();
        int i = this.returnResult;
        if (i == 10 || i == 11 || i == 12 || i == 13) {
            Intent intent = new Intent();
            intent.putExtra(Constant.NOTE_RETURN_RESULT, this.returnResult);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }


    private void saveNote() {
        collectNoteData();
        int i = this.noteAction;
        System.out.println("------- save : " + noteAction);
        if (i == 0) {

            int notesCount = (int) noteHelper.getNotesCount();
            if (notesCount >= 0) {
                notesCount++;
            }
//            this.mRealm.executeTransaction(new Realm.Transaction() {
//                public void execute(Realm realm) {
//                    int i;
//                    Number max = realm.where(Note.class).max(FacebookAdapter.KEY_ID);
//                    if (max == null) {
//                        i = 0;
//                    } else {
//                        i = max.intValue() + 1;
//                    }
            count = notesCount;
//            id = j;
            System.out.println("---- F_ID iiii : " + folderId + " - " + id);
            Note note = new Note();
            note.setId(notesCount);
            note.setTagId((int) id);
            note.setNoteTitle(noteTitle);
            note.setNoteContent(noteContent);
            note.setDateModified(dateModified);
            note.setPinned(isPinned);
            note.setWidgetId(widgetId);
            note.setImgOrientCode(imgOrientCode);
            note.setImgByteArr(imgByteArr);
            note.setDeleted(false);
            note.setPinOrder(pinOrder);
            note.setLocked(isLocked);
            note.setFolderId(folderId);
            note.setTitleBold(titleBold);
            note.setTitleItalic(titleItalic);
            note.setTitleUnderline(titleUnderline);
            note.setTitleStrike(titleStrike);
            note.setContentBold(contentBold);
            note.setContentItalic(contentItalic);
            note.setContentUnderline(contentUnderline);
            note.setContentStrike(contentStrike);
            note.setAlign(align);
            if (!isInserted) {
                noteHelper.insertNotes(note);
            }

            System.out.println("------- folder  iiii : " + id);
//            Tags tag = noteHelper.getTagCount(0);
            Tags tag2 = null;
            if (id != 0) {
                tag2 = noteHelper.getTagCount((int) id);
            }
            if (tag2 != null) {
                tag2.setCounterNote(tag2.getCounterNote() + 1);
            }
//            if (tag != null) {
//                tag.setCounterNote(tag.getCounterNote() + 1);
//            }
            if (!isNoteMoved) {
                returnResult = 10;
            } else if (originFolderId != 0) {
                returnResult = 13;
            } else {
                returnResult = 10;
            }
//                }
//            });
        } else if (i == 1) {
            System.out.println("---- iiii : " + id + " ff " + folderId);
//            this.mRealm.executeTransaction(new Realm.Transaction() {
//                public void execute(Realm realm) {

            mNoteView.setId(id);
            mNoteView.setTagId((int) folderId);
            mNoteView.setNoteTitle(noteTitle);
            mNoteView.setNoteContent(noteContent);
            mNoteView.setPinned(isPinned);
            mNoteView.setPinOrder(pinOrder);
            mNoteView.setImgByteArr(imgByteArr);
            mNoteView.setImgOrientCode(imgOrientCode);
            mNoteView.setDeleted(isDeleted);
            mNoteView.setLocked(isLocked);
            mNoteView.setWidgetId(widgetId);
            mNoteView.setTitleBold(titleBold);
            mNoteView.setTitleItalic(titleItalic);
            mNoteView.setTitleUnderline(titleUnderline);
            mNoteView.setTitleStrike(titleStrike);
            mNoteView.setContentBold(contentBold);
            mNoteView.setContentItalic(contentItalic);
            mNoteView.setContentUnderline(contentUnderline);
            mNoteView.setContentStrike(contentStrike);
            mNoteView.setAlign(align);
            if (isNoteMoved) {
                mNoteView.setFolderId(folderId);
                if (originFolderId != 0) {
                    int unused = returnResult = 13;
                }
            } else {
                returnResult = 12;
            }
            if (isNoteEdited) {
                dateModified = Calendar.getInstance().getTime();
                mNoteView.setDateModified(dateModified);
            }
//                }
//            });
            noteHelper.updateNotes(mNoteView);
        }
        Log.i("WIDGET", widgetId + "");
        int i2 = widgetId;
        if (i2 > 0) {
            updateNoteWidget(i2, getPackageName(), this, this.noteTitle, this.noteContent, this.id);
        }
    }

    public void requestAddWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppWidgetManager appWidgetManager = (AppWidgetManager) getSystemService(AppWidgetManager.class);
            ComponentName componentName = new ComponentName(this, NoteWidgetProvider.class);
            if (appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String title = EdtCreateNoteTitle.getText().toString();
                        String content = EdtCreateNote.getText().toString();
                        if (!noteHelper.checkRecordExist(title, content)) {
                            isInserted = true;
                            saveNote();
                        }
                        System.out.println("------ widget : 0 ");
                        Intent intent = new Intent(context, NoteWidgetCreatedReceiver.class);
                        int ids = (int) count;
                        intent.putExtra("WIDGET_NOTE_TITLE", title);
                        intent.putExtra("WIDGET_NOTE_CONTENT", content);
                        intent.putExtra("WIDGET_NOTE_ID", ids);
                        System.out.println("------ widget : id " + id);
                        PendingIntent pendingIntent;
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//                            pendingIntent = PendingIntent.getBroadcast(
//                                    context,
//                                    0, intent,
//                                    PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
//                        } else {
                        pendingIntent = PendingIntent.getBroadcast(
                                context,
                                0, intent,
//                                    PendingIntent.FLAG_MUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
                                PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//                        }
                        appWidgetManager.requestPinAppWidget(componentName, (Bundle) null, pendingIntent);
                    }
                }, 100);
            }
        }
    }

    public static void updateNoteWidget(int i, String str, Context context, String str2, String str3, long j) {
        System.out.println("----- bundle : iddd " + i);
        int i2;
        RemoteViews remoteViews = new RemoteViews(str, R.layout.note_widget_layout);
        widgetId = i;
        int i3 = new TinyDB(context).getInt(Constant.WIDGET_BG_ID);
        int i4 = R.color.black;
        if (i3 != 1) {
            if (i3 == 2) {
                i2 = R.drawable.bg_widget_03;
            } else if (i3 != 3) {
                i2 = R.drawable.bg_widget_01;
            } else {
                i2 = R.drawable.bg_widget_04;
            }
            remoteViews.setInt(R.id.note_widget_root_layout, "setBackgroundResource", i2);
            Intent intent = new Intent(context, NoteWidgetService.class);
            intent.putExtra("WIDGET_NOTE_TITLE", str2);
            intent.putExtra("WIDGET_NOTE_CONTENT", str3);
            intent.putExtra("WIDGET_NOTE_ID", j);
            intent.putExtra("WIDGET_TEXT_COLOR", i4);
            intent.setData(Uri.fromParts("content", String.valueOf(new Random().nextInt()), (String) null));
            remoteViews.setRemoteAdapter(R.id.note_widget_container, intent);
            Intent intent2 = new Intent(context, ActivityNewCreateNotes.class);
            intent2.putExtra(Constant.WIDGET_ID, i);
            PendingIntent activity = PendingIntent.getActivity(context, i, intent2, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.note_widget_root_layout, activity);
            remoteViews.setPendingIntentTemplate(R.id.note_widget_container, activity);
            AppWidgetManager.getInstance(context).updateAppWidget(i, remoteViews);
        }
        i2 = R.drawable.bg_widget_02;
        i4 = R.color.white;
        remoteViews.setInt(R.id.note_widget_root_layout, "setBackgroundResource", i2);
        Intent intent3 = new Intent(context, NoteWidgetService.class);
        intent3.putExtra("WIDGET_NOTE_TITLE", str2);
        intent3.putExtra("WIDGET_NOTE_CONTENT", str3);
        intent3.putExtra("WIDGET_NOTE_ID", j);
        intent3.putExtra("WIDGET_TEXT_COLOR", i4);
        intent3.setData(Uri.fromParts("content", String.valueOf(new Random().nextInt()), (String) null));
        remoteViews.setRemoteAdapter(R.id.note_widget_container, intent3);
        Intent intent22 = new Intent(context, ActivityNewCreateNotes.class);
        intent22.putExtra(Constant.WIDGET_ID, i);
        PendingIntent activity2 = PendingIntent.getActivity(context, i, intent22, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.note_widget_root_layout, activity2);
        remoteViews.setPendingIntentTemplate(R.id.note_widget_container, activity2);
        AppWidgetManager.getInstance(context).updateAppWidget(i, remoteViews);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvBackCreateNotes:
                GotoBack();
                break;
            case R.id.IvMoreCreateNotes:
                GotoMore();
                break;
            case R.id.IvCamera:
                GotoCamera();
                break;
            case R.id.IvDrawing:
                GotoDrawing();
                break;
            case R.id.IvBtnRemoveAttach:
                GotoRemoveImage();
                break;
            case R.id.IvDeleteNote:
                GotoDeleteNote();
                break;
            case R.id.IvEdit:
                GotoEdit();
                break;
        }
    }

    private void GotoBack() {
        onBackPressed();
    }

    private void GotoMore() {
        Constant.hideKeyboard(ActivityNewCreateNotes.this);
        String obj = EdtCreateNoteTitle.getText().toString();
        if (obj == null || obj.equals("")) {
            obj = getResources().getString(R.string.untitled);
        }
        String str = obj;
        if (dateModified == null) {
            dateModified = Calendar.getInstance().getTime();
        }
        new BottomView(getSupportFragmentManager(), str, Constant.getTimeAgo(dateModified), isPinned, isLocked, imgByteArr, imgOrientCode, titleBold, titleItalic, titleUnderline, titleStrike, contentBold, contentItalic, contentUnderline, contentStrike, align, new BottomView.IBottomMenu() {
            public void onDeleteNote() {
                showConfirmDialogDeleteNote();
            }

            public void onLockNote() {
                System.out.println("------- lock ppp: " + password);
                if (password == null || !password.equals("")) {
                    isLocked = !isLocked;
                    System.out.println("------- lock : " + isLocked + "->>>>.. " + mNoteView.toString());
                    if (isLocked) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.note_locked), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.note_unlocked), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SettingPasswordDialog settingPasswordDialog = new SettingPasswordDialog(context, getResources().getString(R.string.set_password), getResources().getString(R.string.set_pass_msg), true, false, password, new SettingPasswordDialog.SetPasswordListener() {
                        public void SetOnPasswordEnter(String str) {
                        }

                        public void SetOnNewPasswordSet(String str) {
                            password = str;
                            new TinyDB(context).putString(Constant.PASSWORD, password);
                            isLocked = true;

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.note_locked), Toast.LENGTH_SHORT).show();

                        }
                    });
                    settingPasswordDialog.show();

                    WindowManager.LayoutParams lp = settingPasswordDialog.getWindow().getAttributes();
                    Window window = settingPasswordDialog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                    lp.gravity = Gravity.CENTER;
                    settingPasswordDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                }
                saveNote();
                if (noteAction == 1) {
                    isNoteEdited = true;
                }
            }

            public void onPinNote() {
                isPinned = !isPinned;
                if (isPinned) {
                    int TagCount = (int) noteHelper.getIsPinOrder((int) id);
//                    Number max = mRealm.where(Note.class).max("pinOrder");
                    if (TagCount == 0) {
                        pinOrder = 1;
                    } else {
                        pinOrder = TagCount + 1;
                    }
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.pin_to_top), Toast.LENGTH_SHORT).show();
                } else {
                    pinOrder = 0;
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.un_pin_note), Toast.LENGTH_SHORT).show();
                }
                if (noteAction == 1) {
                    isNoteEdited = true;
                    returnResult = 11;
                }
                Tags tags = noteHelper.getTagsRecord(folderId);
                noteHelper.updateTags(new Tags(tags.getId(), tags.getTagName(), tags.getColorCodeId(), (tags.getCounterNote() + 1)));
            }

            public void onScanNote() {
                startActivityForResult(new Intent(context, ActivityScanCode.class), QR_CODE);
            }

            public void onMoveNote() {
                ArrayList<Tags> allTags = new ArrayList();
                int getTags = noteHelper.getTags().size();
                System.out.println("------ FFF : " + folderId + " - - : " + id);
                for (int p = 0; p < getTags; p++) {
                    if (noteHelper.getTags().get(p).getId() != folderId) {
                        allTags.add(noteHelper.getTags().get(p));
                    }
                }
                if (allTags == null || allTags.size() == 0) {
                    showDialogCreateNewTag();
                } else {
                    showListTagToMove(allTags);
                }
            }

            public void onShareNote() {
                Intent intent = new Intent("android.intent.action.SEND");
                String obj = EdtCreateNote.getText().toString();
                String obj2 = EdtCreateNoteTitle.getText().toString();
                if (obj2.equals("")) {
                    obj2 = getResources().getString(R.string.untitled);
                }
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", obj2 + "\n" + obj);
                startActivity(Intent.createChooser(intent, "Share via"));
            }

            @Override
            public void onTitleNote(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike) {
                titleBold = IsBold;
                titleItalic = IsItalic;
                titleUnderline = IsUnderline;
                titleStrike = IsStrike;
                if (IsBold) {
                    EdtCreateNoteTitle.setTypeface(null, Typeface.BOLD);
                }
                if (IsItalic) {
                    EdtCreateNoteTitle.setTypeface(null, Typeface.ITALIC);
                } else {
                    EdtCreateNoteTitle.setTypeface(null, Typeface.BOLD);
                }
                if (IsBold && IsItalic) {
                    EdtCreateNoteTitle.setTypeface(null, Typeface.BOLD_ITALIC);
                }
                if (IsUnderline) {
                    EdtCreateNoteTitle.setPaintFlags(EdtCreateNoteTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else if (IsStrike) {
                    EdtCreateNoteTitle.setPaintFlags(EdtCreateNoteTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else if (IsUnderline && IsStrike) {
                    EdtCreateNoteTitle.setPaintFlags(EdtCreateNoteTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG & Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    EdtCreateNoteTitle.setPaintFlags(0);
                }
            }

            @Override
            public void onContentNote(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike) {
                contentBold = IsBold;
                contentItalic = IsItalic;
                contentUnderline = IsUnderline;
                contentStrike = IsStrike;
            }

            @Override
            public void onAlignNote(int pos) {
                align = pos;
            }

            public void onCreateWidget() {
                if (Build.VERSION.SDK_INT < 26) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.widget_notify_feature_not_supported), Toast.LENGTH_SHORT).show();
                } else if (widgetId > 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.widget_notify_note_already_added_msg), Toast.LENGTH_SHORT).show();
                } else {
                    requestAddWidget();
                }
            }
        }).show(getSupportFragmentManager(), "BottomMenu");
    }

    public void showListTagToMove(final ArrayList<Tags> realmResults) {
        ChooseCreatedTagDialog chooseTagDialog = new ChooseCreatedTagDialog(this, new ChooseCreatedTagDialog.setListTag() {
            public Tags getTag(int i) {
                return (Tags) realmResults.get(i);
            }

            public int getTagsSize() {
                return realmResults.size();
            }

            public void onTagSelectedPosition(int i) {
                System.out.println("---- List folderId : " + realmResults);
                final Tags tag = (Tags) noteHelper.getTagsRecord(i);
//                mRealm.executeTransaction(new Realm.Transaction() {
//                    public void execute(Realm realm) {
                tag.setCounterNote(tag.getCounterNote() + realmResults.size());
                System.out.println("----- ii realmList: " + realmResults.size());
//                for (Note note : realmList) {
                Note note = noteHelper.getNoteRecord((int) id);
                System.out.println("----- ii realmList: " + note.toString());
                note.setTagId(tag.getId());
                noteHelper.updateNotesTags(note, folderId, note.getId());
//                }
                if (folderId != 0) {
                    Tags tags = noteHelper.getTagsRecord(folderId);
                    tags.setCounterNote(Math.max(tag.getCounterNote() - realmResults.size(), 0));
                }
//                final Tags tag = (Tags) realmResults.get(i);
//                tag.setCounterNote(tag.getCounterNote() + 1);
//                Tags tags = (Tags) noteHelper.getTagsRecord(folderId);
//                if (!(tags == null || tags.getId() == 0)) {
//                    tags.setCounterNote(Math.max(tags.getCounterNote() - 1, 0));
//                }
                folderId = tag.getId();
                folderName = tag.getTagName();
                TvFolderNameNote.setText(folderName);
                isNoteMoved = true;
                Toast.makeText(context, getResources().getString(R.string.moved_to_new_tag) + " " + folderName, Toast.LENGTH_SHORT).show();
            }
        });
        chooseTagDialog.show();

        WindowManager.LayoutParams lp = chooseTagDialog.getWindow().getAttributes();
        Window window = chooseTagDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
        chooseTagDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void showDialogCreateNewTag() {
        AddNewTagDialog addNewTagDialog = new AddNewTagDialog(this, getResources().getString(R.string.new_tag), getResources().getString(R.string.move_to_this_tag), getResources().getString(R.string.create_button), new AddNewTagDialog.CreateNewTagListeners() {
            public void onSaveTag(final String str) {
                int i;
                int TagCount = (int) noteHelper.getTagsCount();
                if (TagCount >= 0) {
                    TagCount++;
                }

                Tags tag = new Tags(TagCount, str, -1, 1);
                noteHelper.insertTags(tag);
                folderId = TagCount;
                folderName = str;
                TvFolderNameNote.setText(folderName);
                isNoteMoved = true;
                Toast.makeText(context, getResources().getString(R.string.moved_to_new_tag) + " " + str, Toast.LENGTH_SHORT).show();
            }
        });
        addNewTagDialog.show();

        WindowManager.LayoutParams lp = addNewTagDialog.getWindow().getAttributes();
        Window window = addNewTagDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
        addNewTagDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void showConfirmDialogDeleteNote() {
        String str;
        String str2;
        String str3;
        if (this.noteAction == 1) {
            str3 = getResources().getString(R.string.note_keep_30_day);
            str2 = getResources().getString(R.string.delete_note);
            str = getResources().getString(R.string.delete_button);
        } else {
            str3 = getResources().getString(R.string.confirm_discard_note);
            str2 = getResources().getString(R.string.discard_note);
            str = getResources().getString(R.string.discard_button);
        }
        NoteDeleteDialog deleteNoteDialog = new NoteDeleteDialog(this, str2, str3, str, new NoteDeleteDialog.setDeleteNote() {
            public void onSetDeleteNote() {
                if (noteAction == 0) {
                    finish();
                    return;
                }
                isDeleted = true;
                isPinned = false;
                pinOrder = 0;
                returnResult = 12;
//                mRealm.executeTransaction(new Realm.Transaction() {
//                    public void execute(Realm realm) {
                Tags tag = noteHelper.getTagsRecord(1);
//                        Tags tag2 = noteHelper.getTagsRecord(0);
                System.out.println("----- id : iiii : " + folderId);
                Tags tag3 = folderId != 0 ? noteHelper.getTagsRecord((int) folderId) : null;
                if (tag3 != null) {
                    tag3.setCounterNote(Math.max(tag3.getCounterNote() - 1, 0));
                }
//                        if (tag2 != null) {
//                            tag2.setCountNote(Math.max(tag2.getCountNote() - 1, 0));
//                        }
                if (tag != null) {
                    tag.setCounterNote(tag.getCounterNote() + 1);
                }
//                    }
//                });
                onBackPressed();
            }
        });
        deleteNoteDialog.show();
        WindowManager.LayoutParams lp = deleteNoteDialog.getWindow().getAttributes();
        Window window = deleteNoteDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
        deleteNoteDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void GotoCamera() {
        Constant.hideKeyboard(ActivityNewCreateNotes.this);
        showDialogAttachImage();
    }

    private void GotoDrawing() {
        Constant.hideKeyboard(ActivityNewCreateNotes.this);
        startActivityForResult(new Intent(this, DrawingActivity.class), DRAWING_CODE);
    }

    private void showDialogAttachImage() {
        new IOSSheetDialog.Builder(this).setTitle((CharSequence) getResources().getString(R.string.choose_action)).setCancelText((CharSequence) getResources().getString(R.string.cancel)).setData(new IOSSheetDialog.SheetItem[]{new IOSSheetDialog.SheetItem(getResources().getString(R.string.take_photo), IOSSheetDialog.SheetItem.BLUE), new IOSSheetDialog.SheetItem(getResources().getString(R.string.choose_photo), IOSSheetDialog.SheetItem.BLUE)}, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (i == 0) {
                    cameraAction = CAMERA_ACTION.TAKE_PHOTO;
                    actionTakePhoto();
                } else if (i == 1) {
                    cameraAction = CAMERA_ACTION.CHOOSE_PHOTO;
                    actionPickPhoto();
                }
            }
        }).create().show();
    }

    private void GotoEdit() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(EdtCreateNoteTitle.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public void actionTakePhoto() {
        boolean checkPer = Constant.checkPermission(this, "android.permission.CAMERA");
        boolean checkPer2 = Constant.checkPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (checkPer && checkPer2) {
            takePhoto();
        } else if (!checkPer && checkPer2) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 101);
        } else if (!checkPer || checkPer2) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, 103);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 102);
        }
    }

    public void actionPickPhoto() {
        if (Constant.checkPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            selectImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
        }
    }

    private void takePhoto() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getString(R.string.app_name) + "/";
        File newdir = new File(dir);
        newdir.mkdirs();

        String str = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_";
        String file = dir + str + ".jpg";
        File newfile = new File(file);
        try {
//            newfile.createNewFile();
            newfile = createImageFilePath();
        } catch (IOException e) {
            System.out.println("----- ffff : " + e.getMessage());
        }

        Uri outputFileUri = Uri.fromFile(new File(newfile.getAbsolutePath()));
        System.out.println("----- outputFileUri : " + outputFileUri.getPath());
        System.out.println("----- getUriForFile : " + FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newfile));
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newfile));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri);
        startActivityForResult(intent, REQ_TAKE_PHOTO);
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            File file = null;
//            try {
//                file = createImageFilePath();
//            } catch (IOException e) {
//                System.out.println("---- catch : " + e.getMessage());
//                e.printStackTrace();
//            }
//            if (file != null) {
//                intent.putExtra("output", FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file));
//                startActivityForResult(intent, REQ_TAKE_PHOTO);
//            }
//        }
    }

    private File createImageFilePath() throws IOException {
        String str = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_";
        String str2 = Environment.getExternalStorageDirectory() + "/WordOfDay";
        File file = new File(str2);
        if (!file.exists()) {
            new File(str2).mkdirs();
        }
        File createTempFile = new File(file, str + ".jpg");
//        File createTempFile = File.createTempFile(str, ".jpg", file);
        this.mPhotoTakenPath = createTempFile.getAbsolutePath();
        System.out.println("----- path : " + file + " - " + createTempFile.getAbsoluteFile());
        return createTempFile;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_PICK_IMAGE);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 100) {
            if (iArr.length > 0 && iArr[0] == 0) {
                selectImage();
            }
        } else if (i == 103 && this.cameraAction == CAMERA_ACTION.TAKE_PHOTO) {
            if (iArr.length >= 2 && iArr[0] == 0 && iArr[1] == 0) {
                takePhoto();
            }
        } else if (i == 102 && this.cameraAction == CAMERA_ACTION.TAKE_PHOTO) {
            if (iArr.length > 0 && iArr[0] == 0) {
                takePhoto();
            }
        } else if (i == 101 && this.cameraAction == CAMERA_ACTION.TAKE_PHOTO && iArr.length > 0 && iArr[0] == 0) {
            takePhoto();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        System.out.println("------ reer : " + requestCode + " - " + resultCode + " - " + intent);
        switch (requestCode) {
            case REQ_PICK_IMAGE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (intent != null) {
                            Uri selectedImage = intent.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            if (selectedImage != null) {
                                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    String picturePath = cursor.getString(columnIndex);
                                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                    System.out.println("------ width : " + bitmap.getWidth() + " height - " + bitmap.getHeight());

                                    //todo check
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            handleImagePicked(bitmap);
                                        }
                                    }, 1000);
                                }
                            }
                        }
                        break;
                }
                break;
            case REQ_TAKE_PHOTO:
                switch (resultCode) {
                    case RESULT_OK:
                        if (intent != null) {
                            Bitmap selectedImage = (Bitmap) intent.getExtras().get("data");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    handleImagePicked(selectedImage);
                                }
                            }, 1000);
                        }
                        break;
                }
                break;
            case DRAWING_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (intent != null) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    handleImageDrawingBitmap(intent.getByteArrayExtra("bitmap"));
                                }
                            }, 1000);
                        }
                        break;
                }
                break;
            case QR_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (intent != null) {
                            String stringExtra = intent.getStringExtra(Constant.SCAN_RESULT);
                            String obj = this.EdtCreateNote.getText().toString();
                            if (!obj.equals("")) {
                                stringExtra = obj + "\n" + stringExtra;
                            }
                            this.EdtCreateNote.setText(stringExtra);
                        }
                        break;
                }
                break;
        }

    }

    private void handleImagePicked(Bitmap selectedImage) {
        System.out.println("------ width : " + selectedImage.getWidth() + " height - " + selectedImage.getHeight());
        Bitmap bitmapresize = Constant.resize(selectedImage, Constant.IMG_REL_MAX_WIDTH, Constant.IMG_REL_MAX_HEIGHT);
        Glide.with(context).load(bitmapresize).apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(new RoundedCorners(24))).into(this.IvAttach);
        this.RlAttachViewer.setVisibility(View.VISIBLE);
    }

    private void handleImagePicked(Uri uri, String mPhotoTakenPath) {
        Bitmap bitmap = null;
        if (uri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(mPhotoTakenPath, bmOptions);
        }
        Bitmap bitmapresize = Constant.resize(bitmap, Constant.IMG_REL_MAX_WIDTH, Constant.IMG_REL_MAX_HEIGHT);
        Glide.with(context).load(bitmapresize).apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(new RoundedCorners(24))).into(this.IvAttach);
        this.RlAttachViewer.setVisibility(View.VISIBLE);
    }

    private void handleImageDrawingBitmap(byte[] bArr) {
        if (bArr != null && bArr.length != 0) {
            Bitmap resize = Constant.resize(BitmapFactory.decodeByteArray(bArr, 0, bArr.length), Constant.IMG_REL_MAX_WIDTH, Constant.IMG_REL_MAX_HEIGHT);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resize.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            this.imgByteArr = byteArray;
            this.imgOrientCode = 1;
            if (this.noteAction == 1) {
                this.isNoteEdited = true;
            }
            Glide.with(context).load(Constant.getBitmap(byteArray, 1)).apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(new RoundedCorners(24))).into(this.IvAttach);
            this.RlAttachViewer.setVisibility(View.VISIBLE);
        }
    }

    private void GotoRemoveImage() {
        Constant.hideKeyboard(ActivityNewCreateNotes.this);
        showDialogDeleteImage();
    }

    private void showDialogDeleteImage() {
        NoteDeleteDialog deleteNoteDialog = new NoteDeleteDialog(this, getResources().getString(R.string.delete_photo), getResources().getString(R.string.confirm_delete_image), getResources().getString(R.string.delete_button), new NoteDeleteDialog.setDeleteNote() {
            public void onSetDeleteNote() {
                imgByteArr = null;
                imgOrientCode = 0;
                IvAttach.setImageDrawable((Drawable) null);
                RlAttachViewer.setVisibility(View.GONE);
                if (noteAction == 1) {
                    isNoteEdited = true;
                }
            }
        });
        this.deleteNoteDialog = deleteNoteDialog;
        deleteNoteDialog.show();

        WindowManager.LayoutParams lp = this.deleteNoteDialog.getWindow().getAttributes();
        Window window = this.deleteNoteDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
    }

    private void GotoDeleteNote() {
        Constant.hideKeyboard(ActivityNewCreateNotes.this);
        showConfirmDialogDeleteNote();
    }

}