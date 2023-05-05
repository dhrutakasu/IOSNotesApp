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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.divyanshu.draw.activity.DrawingActivity;
import com.ligl.android.widget.iosdialog.IOSSheetDialog;
import com.note.iosnotes.Model.Note;
import com.note.iosnotes.Model.Tags;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.BottomView;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.Utils.Pref;
import com.note.iosnotes.dialog.AddNewTagDialog;
import com.note.iosnotes.dialog.ChooseCreatedTagDialog;
import com.note.iosnotes.dialog.NoteDeleteDialog;
import com.note.iosnotes.dialog.SettingPasswordDialog;
import com.note.iosnotes.provider.NoteCreateWidgetProvider;
import com.note.iosnotes.provider.NoteWidgetCreatedReceiver;
import com.note.iosnotes.provider.WidgetService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import kotlin.text.Regex;
import kotlin.text.StringsKt;

public class NewCreateNotesActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    public static final int TAKE_PHOTO_CODE = 21;
    public static final int PICK_IMAGE_CODE = 22;
    private static final int DRAWING_CODE = 23;
    private static final int QR_CODE = 24;
    private int CAMERA_PERMISSION_CODE = 26;
    private int GALLERY_PERMISSION_CODE = 27;
    private int WRITE_PERMISSION_CODE = 28;
    private int CAMERA_GALLERY_PERMISSION_CODE = 29;
    private Date dateTimeMilles;
    private int TagFolderId;
    private int noteActionCode;
    private String TagFolderName;
    private long NoteId;
    int originTagFolderId;
    private byte[] ImageBytes;
    private int ImageOrientionCode;
    private boolean isDeletedOrNot;
    private boolean isLockedOrNot;
    private boolean isPinnedOrNot;
    private String StrNoteContent;
    private String StrNoteTitle;
    private int PinOrder;
    private static int NoteWidgetId;

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
    private String StrPassword;
    private Note mNote;
    private NotesDatabaseHelper noteHelper;
    private boolean isNoteEdited = false;
    private boolean isNoteMoved = false;
    public int returnResult;
    public int NoteMoveResult = 10;
    public int NotePinResult = 11;
    public int NoteUpdateResult = 12;
    public int NoteFolderResult = 13;
    private IMAGE_ACTION imageAction;
    private NoteDeleteDialog noteDeleteDialog;
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

    }

    enum IMAGE_ACTION {
        CLICK_PHOTO,
        CHOOSE_GALLERY_PHOTO,
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
        Note note = null;
        int folderId = intent.getIntExtra(Constant.TAG_FOLDER_ID, 0);
        TagFolderId = folderId;
        originTagFolderId = folderId;
        TagFolderName = intent.getStringExtra(Constant.TAGS_NAME);
        noteActionCode = intent.getIntExtra(Constant.NOTE_ACTION_CODE, 0);
        NoteId = getIntent().getLongExtra(Constant.TAGS_ID, 0);
        int widgetId = intent.getIntExtra(Constant.TAG_WIDGET_ID, -1);
        if (widgetId != -1 && (noteHelper.getWidgetId(widgetId)) != null) {
            int TagFolderId = note != null ? note.getTagFolderId() : 0;
            TagFolderId = TagFolderId;
            originTagFolderId = TagFolderId;
            NoteId = note != null ? note.getId() : 0;
            noteActionCode = 1;
            Tags tag = noteHelper.getTagsRecord(TagFolderId);
            if (tag != null) {
                TagFolderName = tag.getTagName();
            }
        }
    }

    private void getNoteData() {
        if (noteActionCode == 1) {
            mNote = (Note) noteHelper.getNoteRecord((int) NoteId);
            StrPassword = new Pref(context).getString(Constant.STR_PASSWORD);
            Note note = mNote;
            if (note != null) {
                StrNoteTitle = note.getNoteTitle();
                StrNoteContent = mNote.getNoteContent();
                dateTimeMilles = mNote.getDateTimeMills();
                ImageOrientionCode = mNote.getImgOrientionCode();
                ImageBytes = mNote.getImgByteFormat();
                isPinnedOrNot = mNote.isPinnedOrNot();
                PinOrder = mNote.getIntPinOrder();
                isLockedOrNot = mNote.isLockedOrNot();
                isDeletedOrNot = mNote.isDeletedOrNot();
                NoteWidgetId = mNote.getCreateWidgetId();
                titleBold = mNote.isTitleBold();
                titleItalic = mNote.isTitleItalic();
                titleUnderline = mNote.isTitleUnderline();
                titleStrike = mNote.isTitleStrike();
                contentBold = mNote.isContentBold();
                contentItalic = mNote.isContentItalic();
                contentUnderline = mNote.isContentUnderline();
                contentStrike = mNote.isContentStrike();
                align = mNote.getAlign();
                initActions();
                setTextStyles();
                return;
            }
            return;
        }
        StrNoteTitle = "";
        StrNoteContent = "";
        StrPassword = new Pref(context).getString(Constant.STR_PASSWORD);
        dateTimeMilles = null;
        ImageOrientionCode = 0;
        ImageBytes = null;
        isPinnedOrNot = false;
        PinOrder = 0;
        isLockedOrNot = false;
        isDeletedOrNot = false;
        NoteWidgetId = -1;
        titleBold = true;
        titleItalic = false;
        titleUnderline = false;
        titleStrike = false;
        contentBold = false;
        contentItalic = false;
        contentUnderline = false;
        contentStrike = false;
        align = 1;
        initActions();
        setTextStyles();
    }

    private void setTextStyles() {
        if (titleBold && titleItalic) {
            EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold_italic));
        } else if (titleBold) {
            EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold));
        } else if (titleItalic) {
            EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_italic));
        } else {
            EdtCreateNoteTitle.setTypeface(null);
        }


        if (titleUnderline && titleStrike) {
            System.out.println("-------- 1 true : true ");
            SpannableString spannableString1 = new SpannableString(EdtCreateNoteTitle.getText().toString());
            spannableString1.setSpan(new UnderlineSpan(), 0, EdtCreateNoteTitle.getText().toString().length(), 0);
            spannableString1.setSpan(new StrikethroughSpan(), 0, EdtCreateNoteTitle.getText().toString().length(), 0);
            EdtCreateNoteTitle.setText(spannableString1);
        } else if (titleStrike) {
            System.out.println("-------- 2 false : true ");
            SpannableString spannableString1 = new SpannableString(EdtCreateNoteTitle.getText().toString());
            spannableString1.setSpan(new StrikethroughSpan(), 0, EdtCreateNoteTitle.getText().toString().length(), 0);
            EdtCreateNoteTitle.setText(spannableString1);
        } else if (titleUnderline) {
            System.out.println("-------- 3 true : false ");
            SpannableString spannableString1 = new SpannableString(EdtCreateNoteTitle.getText().toString());
            spannableString1.setSpan(new UnderlineSpan(), 0, EdtCreateNoteTitle.getText().toString().length(), 0);
            EdtCreateNoteTitle.setText(spannableString1);
        } else {
            System.out.println("-------- 4 false : false ");
            SpannableString spannableString1 = new SpannableString(EdtCreateNoteTitle.getText().toString());
            EdtCreateNoteTitle.setText(spannableString1);
        }

        if (contentBold && contentItalic) {
            EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold_italic));
        } else if (contentBold) {
            EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold));
        } else if (contentItalic) {
            EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_italic));
        } else {
            EdtCreateNote.setTypeface(null);
        }

        if (contentUnderline && contentStrike) {
            System.out.println("-------- 5  true : true ");
            SpannableString spannableString1 = new SpannableString(EdtCreateNote.getText().toString());
            spannableString1.setSpan(new UnderlineSpan(), 0, EdtCreateNote.getText().toString().length(), 0);
            spannableString1.setSpan(new StrikethroughSpan(), 0, EdtCreateNote.getText().toString().length(), 0);
            EdtCreateNote.setText(spannableString1);
        } else if (contentStrike) {
            System.out.println("-------- 6 false : true ");
            SpannableString spannableString1 = new SpannableString(EdtCreateNote.getText().toString());
            spannableString1.setSpan(new StrikethroughSpan(), 0, EdtCreateNote.getText().toString().length(), 0);
            EdtCreateNote.setText(spannableString1);
        } else if (contentUnderline) {
            System.out.println("-------- 7 true : false ");
            SpannableString spannableString1 = new SpannableString(EdtCreateNote.getText().toString());
            spannableString1.setSpan(new UnderlineSpan(), 0, EdtCreateNote.getText().toString().length(), 0);
            EdtCreateNote.setText(spannableString1);
        } else {
            System.out.println("-------- 8 false : false ");
            SpannableString spannableString1 = new SpannableString(EdtCreateNote.getText().toString());
            EdtCreateNote.setText(spannableString1);
        }
        if (align == 1) {
            EdtCreateNoteTitle.setGravity(Gravity.TOP | Gravity.LEFT | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.START);
            EdtCreateNote.setGravity(Gravity.TOP | Gravity.LEFT | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.START);
        } else if (align == 2) {
            EdtCreateNoteTitle.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
            EdtCreateNote.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
        } else {
            EdtCreateNoteTitle.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.END);
            EdtCreateNote.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.END);
        }
    }

    private void GetNoteData() {
        if (noteActionCode == 0) {
            StrNoteTitle = EdtCreateNoteTitle.getText().toString();
            StrNoteContent = EdtCreateNote.getText().toString();
            dateTimeMilles = Calendar.getInstance().getTime();
            return;
        }
        String obj = EdtCreateNoteTitle.getText().toString();
        String obj2 = EdtCreateNote.getText().toString();
        if (!EdtCreateNoteTitle.equals(obj) || obj2.length() != EdtCreateNote.length()) {
            isNoteEdited = true;
        }
        StrNoteTitle = obj;
        StrNoteContent = obj2;
    }

    private void initListeners() {
        IvMoreCreateNotes.setOnClickListener(this);
        IvBackCreateNotes.setOnClickListener(this);
        IvCamera.setOnClickListener(this);
        IvDrawing.setOnClickListener(this);
        IvBtnRemoveAttach.setOnClickListener(this);
        IvDeleteNote.setOnClickListener(this);
        IvEdit.setOnClickListener(this);
        EdtCreateNoteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    if (titleBold && titleItalic) {
                        EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold_italic));
                    } else if (titleBold) {
                        EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold));
                    } else if (titleItalic) {
                        EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_italic));
                    } else {
                        EdtCreateNoteTitle.setTypeface(null);
                    }


                    if (titleUnderline && titleStrike) {
                        for (UnderlineSpan span : s.getSpans(0, s.length(), UnderlineSpan.class)) {
                            s.setSpan(span, 0, s.length(), 0);
                        }

                        for (StrikethroughSpan span : s.getSpans(0, s.length(), StrikethroughSpan.class)) {
                            s.setSpan(span, 0, s.length(), 0);
                        }
                    } else if (titleStrike) {
                        for (StrikethroughSpan span : s.getSpans(0, s.length(), StrikethroughSpan.class)) {
                            s.setSpan(span, 0, s.length(), 0);
                        }
                    } else if (titleUnderline) {
                        for (UnderlineSpan span : s.getSpans(0, s.length(), UnderlineSpan.class)) {
                            s.setSpan(span, 0, s.length(), 0);
                        }
                    } else {
//                        if (s.toString().length() >= 0) {
//                            EdtCreateNoteTitle.setText(s.toString());
//                        }
                    }
                }
            }
        });
        EdtCreateNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    if (contentBold && contentItalic) {
                        EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold_italic));
                    } else if (contentBold) {
                        EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold));
                    } else if (contentItalic) {
                        EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_italic));
                    } else {
                        EdtCreateNote.setTypeface(null);
                    }


                    if (contentUnderline && contentStrike) {
                        for (UnderlineSpan span : s.getSpans(0, s.length(), UnderlineSpan.class)) {
                            s.setSpan(span, 0, s.length(), 0);
                        }

                        for (StrikethroughSpan span : s.getSpans(0, s.length(), StrikethroughSpan.class)) {
                            s.setSpan(span, 0, s.length(), 0);
                        }
                    } else if (contentStrike) {
                        for (StrikethroughSpan span : s.getSpans(0, s.length(), StrikethroughSpan.class)) {
                            s.setSpan(span, 0, s.length(), 0);
                        }
                    } else if (contentUnderline) {
                        for (UnderlineSpan span : s.getSpans(0, s.length(), UnderlineSpan.class)) {
                            s.setSpan(span, 0, s.length(), 0);
                        }
                    } else {
//                        if (s.toString().length() > 0) {
//                            EdtCreateNote.setText(s.toString());
//                        }
                    }
                }
            }
        });
    }

    private void initActions() {
        TvFolderNameNote.setText(TagFolderName);
        EdtCreateNote.setText(StrNoteContent);
        EdtCreateNoteTitle.setText(StrNoteTitle);
        if (Constant.getBitmapOfRotate(ImageBytes, ImageOrientionCode) == null) {
            RlAttachViewer.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(Constant.getBitmapOfRotate(ImageBytes, ImageOrientionCode))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                    .into(IvAttach);
            RlAttachViewer.setVisibility(View.VISIBLE);
        }
        int size = new Pref(context).getInt(Constant.STR_TEXT_SIZE);
         if (size==0){
             EdtCreateNote.setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._6ssp) * Float.valueOf(100) / 100.0f);
             EdtCreateNoteTitle.setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._8ssp) * Float.valueOf(100) / 100.0f);
         }else {
             EdtCreateNote.setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._6ssp) * Float.valueOf(size) / 100.0f);
             EdtCreateNoteTitle.setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._8ssp) * Float.valueOf(size) / 100.0f);
         }
    }

    @Override
    public void onBackPressed() {
        NoteSave();
        int result = returnResult;
        if (result == NoteMoveResult || result == NotePinResult || result == NoteUpdateResult || result == NoteFolderResult) {
            Intent intent = new Intent();
            intent.putExtra(Constant.NOTE_RETURN_RESULT, returnResult);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }


    private void NoteSave() {
        GetNoteData();
        int actionCode = noteActionCode;
        if (actionCode == 0) {
            int notesCount = (int) noteHelper.getNotesCount();
            if (notesCount >= 0) {
                notesCount++;
            }

            count = notesCount;
            Note note = new Note();
            note.setId(notesCount);
            note.setTagId((int) NoteId);
            note.setNoteTitle(StrNoteTitle.trim());
            note.setNoteContent(StrNoteContent);
            note.setDateTimeMills(dateTimeMilles);
            note.setPinnedOrNot(isPinnedOrNot);
            note.setCreateWidgetId(NoteWidgetId);
            note.setImgOrientionCode(ImageOrientionCode);
            note.setImgByteFormat(ImageBytes);
            note.setDeletedOrNot(false);
            note.setIntPinOrder(PinOrder);
            note.setLockedOrNot(isLockedOrNot);
            note.setTagFolderId(TagFolderId);
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
            Tags tag = null;
            if (NoteId != 0) {
                tag = noteHelper.getTagCount((int) NoteId);
            }
            if (tag != null) {
                tag.setCounterNote(tag.getCounterNote() + 1);
            }
            if (!isNoteMoved) {
                returnResult = NoteMoveResult;
            } else if (originTagFolderId != 0) {
                returnResult = NoteFolderResult;
            } else {
                returnResult = NoteMoveResult;
            }
        } else if (actionCode == 1) {
            mNote.setId(NoteId);
            mNote.setTagId((int) TagFolderId);
            mNote.setNoteTitle(StrNoteTitle.trim());
            mNote.setNoteContent(StrNoteContent);
            mNote.setPinnedOrNot(isPinnedOrNot);
            mNote.setIntPinOrder(PinOrder);
            mNote.setImgByteFormat(ImageBytes);
            mNote.setImgOrientionCode(ImageOrientionCode);
            mNote.setDeletedOrNot(isDeletedOrNot);
            mNote.setLockedOrNot(isLockedOrNot);
            mNote.setCreateWidgetId(NoteWidgetId);
            mNote.setTitleBold(titleBold);
            mNote.setTitleItalic(titleItalic);
            mNote.setTitleUnderline(titleUnderline);
            mNote.setTitleStrike(titleStrike);
            mNote.setContentBold(contentBold);
            mNote.setContentItalic(contentItalic);
            mNote.setContentUnderline(contentUnderline);
            mNote.setContentStrike(contentStrike);
            mNote.setAlign(align);
            if (isNoteMoved) {
                mNote.setTagFolderId(TagFolderId);
                if (originTagFolderId != 0) {
                    returnResult = NoteFolderResult;
                }
            } else {
                returnResult = NoteUpdateResult;
            }
            if (isNoteEdited) {
                dateTimeMilles = Calendar.getInstance().getTime();
                mNote.setDateTimeMills(dateTimeMilles);
            }
            noteHelper.updateNotes(mNote);
        }
        int id = NoteWidgetId;
        if (id > 0) {
            NoteUpdateWidget(id, getPackageName(), context, StrNoteTitle, StrNoteContent, NoteId);
        }
    }

    public void requestAddWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppWidgetManager manager = (AppWidgetManager) getSystemService(AppWidgetManager.class);
            ComponentName name = new ComponentName(context, NoteCreateWidgetProvider.class);
            if (manager != null && manager.isRequestPinAppWidgetSupported()) {
                new Handler().postDelayed(() -> {
                    String title = EdtCreateNoteTitle.getText().toString();
                    String content = EdtCreateNote.getText().toString();
                    if (!noteHelper.checkRecordExist(title, content)) {
                        isInserted = true;
                        NoteSave();
                    }
                    Intent intent = new Intent(context, NoteWidgetCreatedReceiver.class);
                    int ids = (int) count;
                    intent.putExtra(Constant.TAG_WIDGET_NOTE_TITLE, title);
                    intent.putExtra(Constant.TAG_WIDGET_NOTE_CONTENT, content);
                    intent.putExtra(Constant.TAG_WIDGET_NOTE_ID, ids);
                    PendingIntent pendingIntent;
                    pendingIntent = PendingIntent.getBroadcast(
                            context,
                            0, intent,
                            PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    manager.requestPinAppWidget(name, (Bundle) null, pendingIntent);
                }, 100);
            }
        }
    }

    public static void NoteUpdateWidget(int id, String packageName, Context context, String title, String content, long NoteId) {
        int bgDrawable;
        RemoteViews WidgetViews = new RemoteViews(packageName, R.layout.widget_layout);
        NoteWidgetId = id;
        int bgId = new Pref(context).getInt(Constant.TAG_WIDGET_BG_ID);
        System.out.println("----- bgid : "+bgId);
        int color = R.color.black;
        if (bgId != 1) {
            if (bgId == 2) {
                bgDrawable = R.drawable.bg_widget_03;
            } else if (bgId != 3) {
                bgDrawable = R.drawable.bg_widget_01;
            } else {
                bgDrawable = R.drawable.bg_widget_04;
            }
            WidgetViews.setInt(R.id.note_widget_root_layout, "setBackgroundResource", bgDrawable);
            Intent serviceintent = new Intent(context, WidgetService.class);
            serviceintent.putExtra(Constant.TAG_WIDGET_NOTE_TITLE, title);
            serviceintent.putExtra(Constant.TAG_WIDGET_NOTE_CONTENT, content);
            serviceintent.putExtra(Constant.TAG_WIDGET_NOTE_ID, NoteId);
            serviceintent.putExtra(Constant.WIDGET_TEXT_COLOR, color);
            serviceintent.setData(Uri.fromParts("content", String.valueOf(new Random().nextInt()), (String) null));
            WidgetViews.setRemoteAdapter(R.id.note_widget_container, serviceintent);
            Intent NoteIntent = new Intent(context, NewCreateNotesActivity.class);
            NoteIntent.putExtra(Constant.TAG_WIDGET_ID, id);
            PendingIntent activity = PendingIntent.getActivity(context, id, NoteIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            WidgetViews.setOnClickPendingIntent(R.id.note_widget_root_layout, activity);
            WidgetViews.setPendingIntentTemplate(R.id.note_widget_container, activity);
            AppWidgetManager.getInstance(context).updateAppWidget(id, WidgetViews);
        }
        bgDrawable = R.drawable.bg_widget_02;
        color = R.color.white;
        WidgetViews.setInt(R.id.note_widget_root_layout, "setBackgroundResource", bgDrawable);
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(Constant.TAG_WIDGET_NOTE_TITLE, title);
        serviceIntent.putExtra(Constant.TAG_WIDGET_NOTE_CONTENT, content);
        serviceIntent.putExtra(Constant.TAG_WIDGET_NOTE_ID, NoteId);
        serviceIntent.putExtra(Constant.WIDGET_TEXT_COLOR, color);
        serviceIntent.setData(Uri.fromParts("content", String.valueOf(new Random().nextInt()), (String) null));
        WidgetViews.setRemoteAdapter(R.id.note_widget_container, serviceIntent);
        Intent NotesIntent = new Intent(context, NewCreateNotesActivity.class);
        NotesIntent.putExtra(Constant.TAG_WIDGET_ID, id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, NotesIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        WidgetViews.setOnClickPendingIntent(R.id.note_widget_root_layout, pendingIntent);
        WidgetViews.setPendingIntentTemplate(R.id.note_widget_container, pendingIntent);
        AppWidgetManager.getInstance(context).updateAppWidget(id, WidgetViews);
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

    public static final int countWords(String str) {
        CharSequence obj = StringsKt.trim((CharSequence) str).toString();
        if (obj.length() == 0) {
            return 0;
        }
        return new Regex("\\s+").split(obj, 0).size();
    }

    private void GotoMore() {
        Constant.hideKeyboard(NewCreateNotesActivity.this);
        String toString = EdtCreateNoteTitle.getText().toString();
        if (toString == null || toString.equals("")) {
            toString = getResources().getString(R.string.untitled);
        }
        String str = toString;
        if (dateTimeMilles == null) {
            dateTimeMilles = Calendar.getInstance().getTime();
        }

        new BottomView(getSupportFragmentManager(), str, Constant.getTime(dateTimeMilles), isPinnedOrNot, isLockedOrNot, ImageBytes, ImageOrientionCode, countWords(EdtCreateNote.getText().toString()), titleBold, titleItalic, titleUnderline, titleStrike, contentBold, contentItalic, contentUnderline, contentStrike, align, new BottomView.setBottomMenu() {
            public void onDeleteNote() {
                GotoConfirmDeleteNote();
            }

            public void onLockNote() {
                if (StrPassword == null || !StrPassword.equals("")) {
                    isLockedOrNot = !isLockedOrNot;
                    if (isLockedOrNot) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.note_locked), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.note_unlocked), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SettingPasswordDialog settingPasswordDialog = new SettingPasswordDialog(context, getResources().getString(R.string.set_password), getResources().getString(R.string.set_pass_msg), true, false, StrPassword, new SettingPasswordDialog.SetPasswordListener() {
                        public void SetOnPasswordEnter(String str) {
                        }

                        public void SetOnNewPasswordSet(String str) {
                            StrPassword = str;
                            new Pref(context).putString(Constant.STR_PASSWORD, StrPassword);
                            isLockedOrNot = true;

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
                NoteSave();
                if (noteActionCode == 1) {
                    isNoteEdited = true;
                }
            }

            public void onPinNote() {
                isPinnedOrNot = !isPinnedOrNot;
                if (isPinnedOrNot) {
                    int TagCount = (int) noteHelper.getIsPinOrder((int) NoteId);
                    if (TagCount == 0) {
                        PinOrder = 1;
                    } else {
                        PinOrder = TagCount + 1;
                    }
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.pin_to_top), Toast.LENGTH_SHORT).show();
                } else {
                    PinOrder = 0;
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.un_pin_note), Toast.LENGTH_SHORT).show();
                }
                if (noteActionCode == 1) {
                    isNoteEdited = true;
                    returnResult = NotePinResult;
                }
                Tags tags = noteHelper.getTagsRecord(TagFolderId);
                noteHelper.updateTags(new Tags(tags.getId(), tags.getTagName(), tags.getColorCodeId(), (tags.getCounterNote() + 1)));
            }

            public void onScanNote() {
                startActivityForResult(new Intent(context, ScanQRCodeActivity.class), QR_CODE);
            }

            public void onMoveNote() {
                ArrayList<Tags> tags = new ArrayList();
                int getTags = noteHelper.getTags().size();
                for (int p = 0; p < getTags; p++) {
                    if (noteHelper.getTags().get(p).getId() != TagFolderId) {
                        tags.add(noteHelper.getTags().get(p));
                    }
                }
                if (tags == null || tags.size() == 0) {
                    GotoDialogCreateNewTag();
                } else {
                    GotoListTagToMove(tags);
                }
            }

            public void onShareNote() {
                Intent intentShare = new Intent("android.intent.action.SEND");
                String noteName = EdtCreateNote.getText().toString();
                String title = EdtCreateNoteTitle.getText().toString();
                if (title.equals("")) {
                    title = getResources().getString(R.string.untitled);
                }
                intentShare.setType("text/plain");
                intentShare.putExtra("android.intent.extra.TEXT", title + "\n" + noteName);
                startActivity(Intent.createChooser(intentShare, "Share via"));
            }

            @Override
            public void onTextSize(int size) {
                EdtCreateNote.setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._6ssp) * Float.valueOf(size) / 100.0f);
                EdtCreateNoteTitle.setTextSize(getResources().getDimension(com.intuit.ssp.R.dimen._8ssp) * Float.valueOf(size) / 100.0f);
            }

            @Override
            public void onTitleNote(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike) {
                titleBold = IsBold;
                titleItalic = IsItalic;
                titleUnderline = IsUnderline;
                titleStrike = IsStrike;
                if (IsBold) {
                    EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold));
                }
                if (IsItalic) {
                    EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_italic));
                } else {
                    EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold));
                }
                if (IsBold && IsItalic) {
                    EdtCreateNoteTitle.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold_italic));
                }

                if (IsUnderline && IsStrike) {
                    System.out.println("-------- true : true ");
                    SpannableString spannableString1 = new SpannableString(EdtCreateNoteTitle.getText().toString());
                    spannableString1.setSpan(new UnderlineSpan(), 0, EdtCreateNoteTitle.getText().toString().length(), 0);
                    spannableString1.setSpan(new StrikethroughSpan(), 0, EdtCreateNoteTitle.getText().toString().length(), 0);
                    EdtCreateNoteTitle.setText(spannableString1);
                } else if (IsStrike) {
                    System.out.println("-------- false : true ");
                    SpannableString spannableString1 = new SpannableString(EdtCreateNoteTitle.getText().toString());
                    spannableString1.setSpan(new StrikethroughSpan(), 0, EdtCreateNoteTitle.getText().toString().length(), 0);
                    EdtCreateNoteTitle.setText(spannableString1);
                } else if (IsUnderline) {
                    System.out.println("-------- true : false ");
                    SpannableString spannableString1 = new SpannableString(EdtCreateNoteTitle.getText().toString());
                    spannableString1.setSpan(new UnderlineSpan(), 0, EdtCreateNoteTitle.getText().toString().length(), 0);
                    EdtCreateNoteTitle.setText(spannableString1);
                } else {
                    System.out.println("-------- false : false ");
                    SpannableString spannableString1 = new SpannableString(EdtCreateNoteTitle.getText().toString());
                    EdtCreateNoteTitle.setText(spannableString1);
                }
            }

            @Override
            public void onContentNote(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike) {
                contentBold = IsBold;
                contentItalic = IsItalic;
                contentUnderline = IsUnderline;
                contentStrike = IsStrike;
                if (IsBold && IsItalic) {
                    EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold_italic));
                } else if (IsBold) {
                    EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold));
                } else if (IsItalic) {
                    EdtCreateNote.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_italic));
                } else {
                    EdtCreateNote.setTypeface(null);
                }

                if (IsUnderline && IsStrike) {
                    System.out.println("-------- true : true ");
                    SpannableString spannableString1 = new SpannableString(EdtCreateNote.getText().toString());
                    spannableString1.setSpan(new UnderlineSpan(), 0, EdtCreateNote.getText().toString().length(), 0);
                    spannableString1.setSpan(new StrikethroughSpan(), 0, EdtCreateNote.getText().toString().length(), 0);
                    EdtCreateNote.setText(spannableString1);
                } else if (IsStrike) {
                    System.out.println("-------- false : true ");
                    SpannableString spannableString1 = new SpannableString(EdtCreateNote.getText().toString());
                    spannableString1.setSpan(new StrikethroughSpan(), 0, EdtCreateNote.getText().toString().length(), 0);
                    EdtCreateNote.setText(spannableString1);
                } else if (IsUnderline) {
                    System.out.println("-------- true : false ");
                    SpannableString spannableString1 = new SpannableString(EdtCreateNote.getText().toString());
                    spannableString1.setSpan(new UnderlineSpan(), 0, EdtCreateNote.getText().toString().length(), 0);
                    EdtCreateNote.setText(spannableString1);
                } else {
                    System.out.println("-------- false : false ");
                    SpannableString spannableString1 = new SpannableString(EdtCreateNote.getText().toString());
                    EdtCreateNote.setText(spannableString1);
                }
            }

            @Override
            public void onAlignNote(int pos) {
                align = pos;
                if (pos == 1) {
                    EdtCreateNoteTitle.setGravity(Gravity.TOP | Gravity.LEFT | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.START);
                    EdtCreateNote.setGravity(Gravity.TOP | Gravity.LEFT | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.START);
                } else if (pos == 2) {
                    EdtCreateNoteTitle.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
                    EdtCreateNote.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
                } else {
                    EdtCreateNoteTitle.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.END);
                    EdtCreateNote.setGravity(Gravity.TOP | Gravity.RIGHT | Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER | Gravity.END);
                }
            }

            public void onCreateWidget() {
                if (Build.VERSION.SDK_INT < 26) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.widget_notify_feature_not_supported), Toast.LENGTH_SHORT).show();
                } else if (NoteWidgetId > 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.widget_notify_note_already_added_msg), Toast.LENGTH_SHORT).show();
                } else {
                    requestAddWidget();
                }
            }
        }).show(getSupportFragmentManager(), "BottomMenu");
    }

    public void GotoListTagToMove(final ArrayList<Tags> realmResults) {
        ChooseCreatedTagDialog chooseTagDialog = new ChooseCreatedTagDialog(context, new ChooseCreatedTagDialog.setListTag() {
            public Tags getTag(int i) {
                return (Tags) realmResults.get(i);
            }

            public int getTagsSize() {
                return realmResults.size();
            }

            public void onTagSelectedPosition(int i) {
                final Tags tag = (Tags) noteHelper.getTagsRecord(i);
                tag.setCounterNote(tag.getCounterNote() + realmResults.size());
                Note note = noteHelper.getNoteRecord((int) NoteId);
                note.setTagId(tag.getId());
                noteHelper.updateNotesTags(note, TagFolderId, note.getId());
                if (TagFolderId != 0) {
                    Tags tags = noteHelper.getTagsRecord(TagFolderId);
                    tags.setCounterNote(Math.max(tag.getCounterNote() - realmResults.size(), 0));
                }
                TagFolderId = tag.getId();
                TagFolderName = tag.getTagName();
                TvFolderNameNote.setText(TagFolderName);
                isNoteMoved = true;
                Toast.makeText(context, getResources().getString(R.string.moved_to_new_tag) + " " + TagFolderName, Toast.LENGTH_SHORT).show();
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

    public void GotoDialogCreateNewTag() {
        AddNewTagDialog addNewTagDialog = new AddNewTagDialog(context, getResources().getString(R.string.new_tag), getResources().getString(R.string.move_to_this_tag), getResources().getString(R.string.create_button), new AddNewTagDialog.CreateNewTagListeners() {
            public void onSaveTag(final String str) {
                int TagCount = (int) noteHelper.getTagsCount();
                if (TagCount >= 0) {
                    TagCount++;
                }

                Tags tag = new Tags(TagCount, str, -1, 1);
                noteHelper.insertTags(tag);
                TagFolderId = TagCount;
                TagFolderName = str;
                TvFolderNameNote.setText(TagFolderName);
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

    private void GotoConfirmDeleteNote() {
        String str;
        String str2;
        String str3;
        if (noteActionCode == 1) {
            str3 = getResources().getString(R.string.note_keep_30_day);
            str2 = getResources().getString(R.string.delete_note);
            str = getResources().getString(R.string.delete_button);
        } else {
            str3 = getResources().getString(R.string.confirm_discard_note);
            str2 = getResources().getString(R.string.discard_note);
            str = getResources().getString(R.string.discard_button);
        }
        NoteDeleteDialog deleteNoteDialog = new NoteDeleteDialog(context, str2, str3, str, new NoteDeleteDialog.setDeleteNote() {
            public void onSetDeleteNote() {
                if (noteActionCode == 0) {
                    finish();
                    return;
                }
                isDeletedOrNot = true;
                isPinnedOrNot = false;
                PinOrder = 0;
                returnResult = NoteUpdateResult;
                Tags tag = noteHelper.getTagsRecord(1);
                Tags tag3 = TagFolderId != 0 ? noteHelper.getTagsRecord((int) TagFolderId) : null;
                if (tag3 != null) {
                    tag3.setCounterNote(Math.max(tag3.getCounterNote() - 1, 0));
                }
                if (tag != null) {
                    tag.setCounterNote(tag.getCounterNote() + 1);
                }
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
        Constant.hideKeyboard(NewCreateNotesActivity.this);
        showDialogAttachImage();
    }

    private void GotoDrawing() {
        Constant.hideKeyboard(NewCreateNotesActivity.this);
        startActivityForResult(new Intent(context, DrawingActivity.class), DRAWING_CODE);
    }

    private void showDialogAttachImage() {
        new IOSSheetDialog.Builder(context).setTitle((CharSequence) getResources().getString(R.string.choose_action)).setCancelText((CharSequence) getResources().getString(R.string.cancel)).setData(new IOSSheetDialog.SheetItem[]{new IOSSheetDialog.SheetItem(getResources().getString(R.string.take_photo), IOSSheetDialog.SheetItem.BLUE), new IOSSheetDialog.SheetItem(getResources().getString(R.string.choose_photo), IOSSheetDialog.SheetItem.BLUE)}, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (i == 0) {
                    imageAction = IMAGE_ACTION.CLICK_PHOTO;
                    actionTakePhoto();
                } else if (i == 1) {
                    imageAction = IMAGE_ACTION.CHOOSE_GALLERY_PHOTO;
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
        boolean perCamera = Constant.checkPermission(context, "android.permission.CAMERA");
        boolean perWrite = Constant.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (perCamera && perWrite) {
            takePhoto();
        } else if (!perCamera && perWrite) {
            ActivityCompat.requestPermissions(NewCreateNotesActivity.this, new String[]{"android.permission.CAMERA"}, GALLERY_PERMISSION_CODE);
        } else if (!perCamera || perWrite) {
            ActivityCompat.requestPermissions(NewCreateNotesActivity.this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, CAMERA_GALLERY_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(NewCreateNotesActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, WRITE_PERMISSION_CODE);
        }
    }

    public void actionPickPhoto() {
        if (Constant.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            selectImage();
        } else {
            ActivityCompat.requestPermissions(NewCreateNotesActivity.this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}, CAMERA_PERMISSION_CODE);
        }
    }

    private void takePhoto() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                selectImage();
            }
        } else if (requestCode == CAMERA_GALLERY_PERMISSION_CODE && imageAction == IMAGE_ACTION.CLICK_PHOTO) {
            if (grantResults.length >= 2 && grantResults[0] == 0 && grantResults[1] == 0) {
                takePhoto();
            }
        } else if (requestCode == WRITE_PERMISSION_CODE && imageAction == IMAGE_ACTION.CLICK_PHOTO) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                takePhoto();
            }
        } else if (requestCode == GALLERY_PERMISSION_CODE && imageAction == IMAGE_ACTION.CLICK_PHOTO && grantResults.length > 0 && grantResults[0] == 0) {
            takePhoto();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case PICK_IMAGE_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (intent != null) {
                            Uri selectedImage = intent.getData();
                            System.out.println("-- uRI  : "+selectedImage);
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            if (selectedImage != null) {
                                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    String picturePath = cursor.getString(columnIndex);
                                    System.out.println("-- path : "+picturePath);
                                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

                                    //todo check
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            GotoImagePicke(bitmap);
                                        }
                                    }, 1000);
                                }
                            }
                        }
                        break;
                }
                break;
            case TAKE_PHOTO_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (intent != null) {
                            Bitmap selectedImage = (Bitmap) intent.getExtras().get("data");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    GotoImagePicke(selectedImage);
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
                                    GotoImageDrawingBitmap(intent.getByteArrayExtra("bitmap"));
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
                            String obj = EdtCreateNote.getText().toString();
                            if (!obj.equals("")) {
                                stringExtra = obj + "\n" + stringExtra;
                            }
                            EdtCreateNote.setText(stringExtra);
                        }
                        break;
                }
                break;
        }

    }

    private void GotoImagePicke(Bitmap selectedImage) {

        Bitmap bitmapresize = Constant.getResizeOfBitmap(selectedImage, Constant.IMAGE_MAX_WIDTH, Constant.IMAGE_MAX_HEIGHT);
        Glide.with(context).load(bitmapresize).apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(new RoundedCorners(24))).into(IvAttach);
        RlAttachViewer.setVisibility(View.VISIBLE);
    }

    private void GotoImageDrawingBitmap(byte[] bArr) {
        if (bArr != null && bArr.length != 0) {
            Bitmap bitmap = Constant.getResizeOfBitmap(BitmapFactory.decodeByteArray(bArr, 0, bArr.length), Constant.IMAGE_MAX_WIDTH, Constant.IMAGE_MAX_HEIGHT);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            ImageBytes = toByteArray;
            ImageOrientionCode = 1;
            if (noteActionCode == 1) {
                isNoteEdited = true;
            }
            Glide.with(context).load(Constant.getBitmapOfRotate(toByteArray, 1)).apply(RequestOptions.bitmapTransform(new RoundedCorners(24))).into(IvAttach);
            RlAttachViewer.setVisibility(View.VISIBLE);
        }
    }

    private void GotoRemoveImage() {
        Constant.hideKeyboard(NewCreateNotesActivity.this);
        GotoDialogDeleteImage();
    }

    private void GotoDialogDeleteImage() {
        NoteDeleteDialog deleteNoteDialog = new NoteDeleteDialog(context, getResources().getString(R.string.delete_photo), getResources().getString(R.string.confirm_delete_image), getResources().getString(R.string.delete_button), new NoteDeleteDialog.setDeleteNote() {
            public void onSetDeleteNote() {
                ImageBytes = null;
                ImageOrientionCode = 0;
                IvAttach.setImageDrawable((Drawable) null);
                RlAttachViewer.setVisibility(View.GONE);
                if (noteActionCode == 1) {
                    isNoteEdited = true;
                }
            }
        });
        noteDeleteDialog = deleteNoteDialog;
        deleteNoteDialog.show();

        WindowManager.LayoutParams lp = noteDeleteDialog.getWindow().getAttributes();
        Window window = noteDeleteDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
    }

    private void GotoDeleteNote() {
        Constant.hideKeyboard(NewCreateNotesActivity.this);
        GotoConfirmDeleteNote();
    }
}