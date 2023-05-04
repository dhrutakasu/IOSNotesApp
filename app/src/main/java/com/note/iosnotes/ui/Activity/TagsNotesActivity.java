package com.note.iosnotes.ui.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ligl.android.widget.iosdialog.IOSSheetDialog;
import com.note.iosnotes.Model.Note;
import com.note.iosnotes.Model.Tags;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.Utils.Pref;
import com.note.iosnotes.dialog.AddNewTagDialog;
import com.note.iosnotes.dialog.ChooseCreatedTagDialog;
import com.note.iosnotes.dialog.SettingPasswordDialog;
import com.note.iosnotes.ui.Adapter.AllNotesAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TagsNotesActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int NEW_CREATE_NOTES_CODE = 101;
    private static final int VIEW_NOTE_CODE = 102;
    private Context context;
    private TextView TvNotesTitle;
    private TextView TvNoteTitle;
    private TextView TvPinnedNotesTitle;
    private String StrTagName;
    private ImageView img_back_notes_list;
    private ImageView img_action_create_note;
    private RecyclerView rc_notes;
    private RecyclerView rc_Pinnednotes;
    private ArrayList<Note> NotesList;
    private boolean isEditMode = false;
    private int mNoteSelectedPos;
    private int tagId;
    private int itemId;
    private String mPassword;
    private AllNotesAdapter pinnotesAdapter;
    private AllNotesAdapter notesAdapter;
    private int numNotePine;
    private NotesDatabaseHelper helper;
    private LinearLayout ll_layout_no_note;
    private TextView tv_num_notes_list;
    private ImageView img_action_on_notes_list;
    private int colorCode;
    private TextView tv_done_edit_notes_list;
    private TextView tv_action_move;
    private TextView tv_action_delete;
    private ArrayList<Note> PinnedNotesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_notes);
        initViews();
        getIntents();
        initListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        helper = new NotesDatabaseHelper(context);
        TvNotesTitle = (TextView) findViewById(R.id.TvNotesTitle);
        TvNoteTitle = (TextView) findViewById(R.id.TvNoteTitle);
        TvPinnedNotesTitle = (TextView) findViewById(R.id.TvPinnedNotesTitle);
        img_back_notes_list = (ImageView) findViewById(R.id.img_back_notes_list);
        img_action_create_note = (ImageView) findViewById(R.id.img_action_create_note);
        rc_notes = (RecyclerView) findViewById(R.id.rc_notes);
        rc_Pinnednotes = (RecyclerView) findViewById(R.id.rc_Pinnednotes);
        ll_layout_no_note = (LinearLayout) findViewById(R.id.ll_layout_no_note);
        tv_num_notes_list = (TextView) findViewById(R.id.tv_num_notes_list);
        img_action_on_notes_list = (ImageView) findViewById(R.id.img_action_on_notes_list);
        tv_done_edit_notes_list = (TextView) findViewById(R.id.tv_done_edit_notes_list);
        tv_action_move = (TextView) findViewById(R.id.tv_action_move);
        tv_action_delete = (TextView) findViewById(R.id.tv_action_delete);
    }

    private void getIntents() {
        StrTagName = getIntent().getStringExtra(Constant.TAGS_NAME);
        colorCode = getIntent().getIntExtra(Constant.TAGS_COLOR_CODE, 0);
        tagId = getIntent().getIntExtra(Constant.TAGS_ID, -1);
        itemId = getIntent().getIntExtra(Constant.TAG_FOLDER_ID, 0);
    }

    private void initListeners() {
        img_back_notes_list.setOnClickListener(this);
        img_action_create_note.setOnClickListener(this);
        img_action_on_notes_list.setOnClickListener(this);
        tv_done_edit_notes_list.setOnClickListener(this);
        tv_action_move.setOnClickListener(this);
        tv_action_delete.setOnClickListener(this);
    }

    private void initActions() {
        NotesList = new ArrayList<>();
        if (StrTagName.equals("")) {
            StrTagName = getResources().getString(R.string.all_notes);
        }
        mPassword = new Pref(getApplicationContext()).getString(Constant.STR_PASSWORD);
        TvNotesTitle.setText(StrTagName);
        TvNoteTitle.setText(getResources().getString(R.string.all_notes));
        TvPinnedNotesTitle.setText(getResources().getString(R.string.pinned));
        getListNoteInFolder();

        rc_Pinnednotes.setLayoutManager(new LinearLayoutManager(context));
        pinnotesAdapter = new AllNotesAdapter(context, PinnedNotesList, StrTagName, new AllNotesAdapter.setNotesList() {
            public void onNoteSelected(int i) {
                if (!isEditMode) {
                    mNoteSelectedPos = i;
                    if (itemId == 1) {
                        showDialogRecoveryNote();
                    } else if (mPassword == null || mPassword.equals("") || !((Note) NotesList.get(i)).isLockedOrNot()) {
                        openNote(true);
                    } else {
                        confirmPassword();
                    }
                }
            }
        });
        rc_Pinnednotes.setAdapter(pinnotesAdapter);

        rc_notes.setLayoutManager(new LinearLayoutManager(context));
        notesAdapter = new AllNotesAdapter(context, NotesList, StrTagName, new AllNotesAdapter.setNotesList() {
            public void onNoteSelected(int i) {
                if (!isEditMode) {
                    mNoteSelectedPos = i;
                    if (itemId == 1) {
                        showDialogRecoveryNote();
                    } else if (mPassword == null || mPassword.equals("") || !((Note) NotesList.get(i)).isLockedOrNot()) {
                        openNote(false);
                    } else {
                        confirmPassword();
                    }
                }
            }
        });
        rc_notes.setAdapter(notesAdapter);

        if (itemId == 1) {
            img_action_create_note.setVisibility(View.GONE);
            ll_layout_no_note.setVisibility(View.GONE);
        }
        toggleLayoutNoNote();
        setCountNote();
    }

    @Override
    public void onBackPressed() {
        if (isEditMode) {
            toggleEditMode();
        } else {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_notes_list:
                onBackPressed();
                break;
            case R.id.img_action_create_note:
                GotoCreateNotes();
                break;
            case R.id.img_action_on_notes_list:
            case R.id.tv_done_edit_notes_list:
                GotoNOtesListMore();
                break;
            case R.id.tv_action_move:
                GotoMove();
                break;
            case R.id.tv_action_delete:
                GotoDelete();
                break;
        }
    }

    public void getListNoteInFolder() {
        NotesList = new ArrayList<>();
        int i = itemId;

        if (i == 0) {
            NotesList = helper.getIsDelete(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(NotesList, Comparator.comparing(Note::getDateTimeMills).reversed()
                        .thenComparing(Note::getIntPinOrder).reversed());
            }
        } else if (i != 1) {
            NotesList = helper.getFolderIdWithIsDeleteOrNot(tagId, 0,0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(NotesList, Comparator.comparing(Note::getDateTimeMills).reversed()
                        .thenComparing(Note::getIntPinOrder).reversed());
            }
        } else {
            NotesList = helper.getIsDelete(1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(NotesList, Comparator.comparing(Note::getDateTimeMills).reversed());
            }
        }
        PinnedNotesList = helper.getIsPin(1,tagId);
        if (PinnedNotesList.size() > 0) {
            TvPinnedNotesTitle.setVisibility(View.VISIBLE);
            rc_Pinnednotes.setVisibility(View.VISIBLE);
        } else {
            TvPinnedNotesTitle.setVisibility(View.GONE);
            rc_Pinnednotes.setVisibility(View.GONE);
        }
        if (NotesList.size() > 0) {
            TvNoteTitle.setVisibility(View.VISIBLE);
            rc_notes.setVisibility(View.VISIBLE);
        } else {
            TvNoteTitle.setVisibility(View.GONE);
            rc_notes.setVisibility(View.GONE);
        }
    }

    private void toggleLayoutNoNote() {
        if (NotesList.size() == 0) {
            ll_layout_no_note.setVisibility(View.VISIBLE);
        } else {
            ll_layout_no_note.setVisibility(View.GONE);
        }
    }

    private void setCountNote() {
        String str;
        int size = NotesList.size() + PinnedNotesList.size();
        if (size <= 1) {
            str = size + " " + getResources().getString(R.string.count_note_title_single);
        } else {
            str = size + " " + getResources().getString(R.string.count_note_title_multiple);
        }
        tv_num_notes_list.setText(str);
        helper.updateTags(new Tags(tagId, StrTagName, colorCode, size));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NEW_CREATE_NOTES_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        if (data != null && data.getIntExtra(Constant.NOTE_RETURN_RESULT, 0) == 10) {
                            getListNoteInFolder();
                            notesAdapter.updateData(NotesList);
                            pinnotesAdapter.updateData(PinnedNotesList);
                            toggleLayoutNoNote();
                            setCountNote();
                        }
                        break;
                    case RESULT_CANCELED:
                        return;
                }
                break;
            case VIEW_NOTE_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        int intExtra = data.getIntExtra(Constant.NOTE_RETURN_RESULT, 0);
                        if (intExtra == 11 || intExtra == 12 || intExtra == 13) {
                            getListNoteInFolder();
                            notesAdapter.updateData(NotesList);
                            pinnotesAdapter.updateData(PinnedNotesList);
                            setCountNote();
                            toggleLayoutNoNote();
                        }
                        break;
                    case RESULT_CANCELED:
                        return;
                }
                break;
        }
    }

    private void GotoCreateNotes() {
        Intent intent = new Intent(context, NewCreateNotesActivity.class);
        intent.putExtra(Constant.TAGS_NAME, StrTagName);
        intent.putExtra(Constant.TAG_FOLDER_ID, itemId);
        intent.putExtra(Constant.NOTE_ACTION_CODE, 0);
        long id = tagId;
        intent.putExtra(Constant.TAGS_ID, id);
        startActivityForResult(intent, NEW_CREATE_NOTES_CODE);
    }

    private void GotoNOtesListMore() {
        toggleEditMode();
    }

    private void GotoMove() {
        int i = 0;
        int j = 0;
        boolean z = itemId == 1;
        boolean[] listChecked = notesAdapter.getListChecked();
        boolean[] listPinChecked = pinnotesAdapter.getListChecked();
        if (z) {
            while (i < listChecked.length) {
                if (listChecked[i]) {
                    recoveryNote((Note) NotesList.get(i));
                }
                i++;
            }
            while (j < listPinChecked.length) {
                if (listPinChecked[j]) {
                    recoveryNote((Note) PinnedNotesList.get(j));
                }
                j++;
            }
            toggleEditMode();
            return;
        }
        ArrayList<Note> realmList = new ArrayList<>();
        while (i < listChecked.length) {
            if (listChecked[i]) {
                realmList.add(NotesList.get(i));
            }
            i++;
        }
        ArrayList<Note> realmPinList = new ArrayList<>();
        while (j < listPinChecked.length) {
            if (listPinChecked[j]) {
                realmPinList.add(PinnedNotesList.get(j));
            }
            j++;
        }

        ArrayList<Tags> allTags = new ArrayList();
        int getTags = helper.getTags().size();
        for (int p = 0; p < getTags; p++) {
            if (helper.getTags().get(p).getId() != tagId) {
                allTags.add(helper.getTags().get(p));
            }
        }
        if (realmPinList.size()>0) {
            realmList.addAll(realmPinList);
        }
        if (realmList.size() > 0) {
            if (allTags == null || allTags.size() == 0) {
                showDialogCreateNewTag(realmList);
            } else {
                showListTagToMove(allTags, realmList);
            }
        } else {
            Toast.makeText(context, "Please select Note", Toast.LENGTH_SHORT).show();
        }
    }

    private void showListTagToMove(final ArrayList<Tags> realmResults, final ArrayList<Note> realmList) {
        new ChooseCreatedTagDialog(context, new ChooseCreatedTagDialog.setListTag() {
            public Tags getTag(int i) {
                return (Tags) realmResults.get(i);
            }

            public int getTagsSize() {
                return realmResults.size();
            }

            public void onTagSelectedPosition(int i) {
                final Tags tag = (Tags) helper.getTagsRecord(i);
                tag.setCounterNote(tag.getCounterNote() + realmList.size());
                for (Note note : realmList) {
                    note.setTagId(tag.getId());
                    helper.updateNotesTags(note, tagId, note.getId());
                }
                if (tagId != 0) {
                    Tags tags = helper.getTagsRecord(tagId);
                    tags.setCounterNote(Math.max(tag.getCounterNote() - realmList.size(), 0));
                }
                helper.updateTags(tag);
                Toast.makeText(context, getResources().getString(R.string.moved_to_new_tag) + " " + StrTagName, Toast.LENGTH_SHORT).show();

                toggleEditMode();
                getListNoteInFolder();
                notesAdapter.updateData(NotesList);
                pinnotesAdapter.updateData(PinnedNotesList);
                setCountNote();
                toggleLayoutNoNote();
            }
        }).show();
    }

    private void showDialogCreateNewTag(final ArrayList<Note> realmList) {
        AddNewTagDialog addNewTagDialog = new AddNewTagDialog(context, getResources().getString(R.string.new_tag), getResources().getString(R.string.move_to_this_tag), getResources().getString(R.string.create_button), new AddNewTagDialog.CreateNewTagListeners() {
            public void onSaveTag(final String str) {
                int TagCount = (int) helper.getTagsCount();
                if (TagCount >= 0) {
                    TagCount++;
                }
                Tags tag = new Tags(TagCount, str, -1, realmList.size());

                helper.insertTags(tag);
                Iterator it = realmList.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    ((Note) it.next()).setTagId(TagCount);
                    i2++;
                }
                if (itemId != 0) {
                    Tags tag2 = (Tags) helper.getTagsRecord(itemId);
                    tag2.setCounterNote(Math.max(tag2.getCounterNote() - i2, 0));
                }
                for (int i = 0; i < realmList.size(); i++) {
                    Note note = realmList.get(i);
                    note.setTagId(TagCount);
                    helper.updateNotes(note);
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.moved_to_new_tag) + " " + str, Toast.LENGTH_SHORT).show();

                toggleEditMode();
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

    private void GotoDelete() {
        boolean isDeleted = true;
        if (itemId != 1) {
            isDeleted = false;
        }
        boolean[] listChecked = notesAdapter.getListChecked();
        boolean[] listPinChecked = pinnotesAdapter.getListChecked();
        List realmList = new ArrayList();
        for (int i = 0; i < listChecked.length; i++) {
            if (listChecked[i]) {
                realmList.add(NotesList.get(i));
            }
        } for (int i = 0; i < listPinChecked.length; i++) {
            if (listPinChecked[i]) {
                realmList.add(PinnedNotesList.get(i));
            }
        }
        Iterator iterator = realmList.iterator();
        while (iterator.hasNext()) {
            Note note = (Note) iterator.next();
            if (isDeleted) {
                deleteNotePermanently(note);
                getListNoteInFolder();
                notesAdapter.updateData(NotesList);
                pinnotesAdapter.updateData(PinnedNotesList);
                setCountNote();
                toggleLayoutNoNote();
            } else {
                deleteTemporarilyNote(note);
            }
        }
        toggleEditMode();
        if (!isDeleted) {
            getListNoteInFolder();
            notesAdapter.updateData(NotesList);
            pinnotesAdapter.updateData(PinnedNotesList);
            setCountNote();
            toggleLayoutNoNote();
        }
    }

    private void toggleEditMode() {
        if (isEditMode) {
            img_action_on_notes_list.setVisibility(View.VISIBLE);
            img_action_create_note.setVisibility(View.VISIBLE);
            tv_done_edit_notes_list.setVisibility(View.GONE);
            tv_action_move.setVisibility(View.GONE);
            tv_action_delete.setVisibility(View.GONE);
            isEditMode = false;
            notesAdapter.enableEditNote(false);
            pinnotesAdapter.enableEditNote(false);
            return;
        }
        img_action_on_notes_list.setVisibility(View.GONE);
        img_action_create_note.setVisibility(View.GONE);
        tv_done_edit_notes_list.setVisibility(View.VISIBLE);
        tv_action_move.setVisibility(View.VISIBLE);
        tv_action_delete.setVisibility(View.VISIBLE);
        isEditMode = true;
        notesAdapter.enableEditNote(true);
        pinnotesAdapter.enableEditNote(true);
    }

    public void showDialogRecoveryNote() {
        new IOSSheetDialog.Builder(context).setTitle(getResources().getString(R.string.choose_action)).setCancelText((CharSequence) getResources().getString(R.string.action_cancel)).setData(new IOSSheetDialog.SheetItem[]{new IOSSheetDialog.SheetItem(getResources().getString(R.string.action_recover), IOSSheetDialog.SheetItem.BLUE), new IOSSheetDialog.SheetItem(getResources().getString(R.string.action_delete_permanent), IOSSheetDialog.SheetItem.RED)}, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (i == 0) {
                    recoveryNote(NotesList.get(mNoteSelectedPos));
                    Toast.makeText(context, context.getResources().getString(R.string.recovery_note_successfully), Toast.LENGTH_SHORT).show();
                    getListNoteInFolder();
                    notesAdapter.updateData(NotesList);
                    pinnotesAdapter.updateData(PinnedNotesList);
                    setCountNote();
                    toggleLayoutNoNote();
                } else if (i == 1) {
                    deleteNotePermanently(NotesList.get(mNoteSelectedPos));
                    getListNoteInFolder();
                    notesAdapter.updateData(NotesList);
                    pinnotesAdapter.updateData(PinnedNotesList);
                    setCountNote();
                    toggleLayoutNoNote();
                }
            }
        }).create().show();
    }

    public void recoveryNote(final Note note) {
        if (note != null) {
            note.setDeletedOrNot(false);
            int folderId = note.getTagId();
            Tags tag = helper.getTagsRecord(folderId);
            tag.setCounterNote(tag.getCounterNote() + 1);
            helper.updateTags(tag);
            Tags tag4 = helper.getTagsRecord(1);
            tag4.setCounterNote(Math.max(tag4.getCounterNote() - 1, 0));
            helper.updateNotes(note);
            notesAdapter.notifyDataSetChanged();
            pinnotesAdapter.notifyDataSetChanged();
            onBackPressed();
        }
    }

    public void deleteNotePermanently(final Note note) {
        if (note != null) {
            helper.deleteNote(note.getTagId(), note.getId());
        }
    }

    private void deleteTemporarilyNote(final Note note) {
        if (note != null) {
            note.setDeletedOrNot(true);
            note.setPinnedOrNot(false);
            note.setIntPinOrder(0);
            helper.updateNotes(note);
            Tags tag = helper.getTagsRecord(1);
            Tags tag3 = tagId != 0 ? helper.getTagsRecord(tagId) : null;
            if (tag3 != null) {
                tag3.setCounterNote(Math.max(tag3.getCounterNote() - 1, 0));
            }

            if (tag != null) {
                tag.setCounterNote(tag.getCounterNote() + 1);
            }
        }
    }

    private void openNote(boolean b) {
        if (!b) {
            int openId = (NotesList.get(mNoteSelectedPos)).getTagId();
            Intent intent = new Intent(context, NewCreateNotesActivity.class);
            intent.putExtra(Constant.TAGS_NAME, StrTagName);
            intent.putExtra(Constant.TAG_FOLDER_ID, openId);
            intent.putExtra(Constant.NOTE_ACTION_CODE, 1);

            intent.putExtra(Constant.TAGS_ID, ((Note) NotesList.get(mNoteSelectedPos)).getId());
            startActivityForResult(intent, VIEW_NOTE_CODE);
        } else {
            int openId = (PinnedNotesList.get(mNoteSelectedPos)).getTagId();
            Intent intent = new Intent(context, NewCreateNotesActivity.class);
            intent.putExtra(Constant.TAGS_NAME, StrTagName);
            intent.putExtra(Constant.TAG_FOLDER_ID, openId);
            intent.putExtra(Constant.NOTE_ACTION_CODE, 1);

            intent.putExtra(Constant.TAGS_ID, ((Note) PinnedNotesList.get(mNoteSelectedPos)).getId());
            startActivityForResult(intent, VIEW_NOTE_CODE);

        }
    }

    private void confirmPassword() {
        SettingPasswordDialog settingPasswordDialog =
                new SettingPasswordDialog(context, getResources().getString(R.string.enter_password), getResources().getString(R.string.enter_password_to_view), false, false, mPassword, new SettingPasswordDialog.SetPasswordListener() {
                    public void SetOnNewPasswordSet(String str) {
                    }

                    public void SetOnPasswordEnter(String str) {
                        if (str.equals(mPassword)) {
                            openNote(true);
                        } else {
                            Toast.makeText(context, getResources().getString(R.string.password_incorrect), Toast.LENGTH_SHORT).show();
                        }
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
}