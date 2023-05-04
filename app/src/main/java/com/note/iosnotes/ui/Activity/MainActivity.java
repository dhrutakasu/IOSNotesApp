package com.note.iosnotes.ui.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ligl.android.widget.iosdialog.IOSSheetDialog;
import com.note.iosnotes.Model.NewTagColor;
import com.note.iosnotes.Model.Tags;
import com.note.iosnotes.NotesDatabaseHelper;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.dialog.AddNewTagDialog;
import com.note.iosnotes.dialog.TagsDeleteDialog;
import com.note.iosnotes.dialog.NewTagColorCodeDialog;
import com.note.iosnotes.ui.Adapter.NewTagAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private final int VIEW_TAGS_REQUEST = 100;
    private ImageView IvSettings;
    private TextView TvEditTag;
    private TextView TvAddNewTag;
    private RecyclerView RvTags;
    private ArrayList<Tags> tagsArrayList;
    private TAG_ACTION mTagAction;
    private AddNewTagDialog addNewTagDialog;
    private NewTagColorCodeDialog newTagColorCodeDialog;
    private TagsDeleteDialog deleteTagsDialog;
    private boolean isEditModeEnabled = false;
    private NewTagAdapter newTagAdapter;
    private List<NewTagColor> listNewTagColor;
    private NotesDatabaseHelper notesDatabaseHelper;
    private int AdapTagId, TagPos = 0;
    private boolean mIsFirstDataInitialized = false;

    enum TAG_ACTION {
        CREATE,
        RENAME,
        DELETE,
        UNDEFINE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        IvSettings = (ImageView) findViewById(R.id.IvSettings);
        TvEditTag = (TextView) findViewById(R.id.TvEditTag);
        TvAddNewTag = (TextView) findViewById(R.id.TvAddNewTag);
        RvTags = (RecyclerView) findViewById(R.id.RvTags);
    }

    private void initListeners() {
        IvSettings.setOnClickListener(this);
        TvEditTag.setOnClickListener(this);
        TvAddNewTag.setOnClickListener(this);
    }

    private void initActions() {
        notesDatabaseHelper = new NotesDatabaseHelper(context);
        RvTags.setLayoutManager(new LinearLayoutManager(context));
        tagsArrayList = new ArrayList<>();

        Tags tags = new Tags(0, "Notes", 2, 0);
        tagsArrayList.add(tags);
        tags = new Tags(0, "Recently deleted", 1, 0);
        tagsArrayList.add(tags);

        System.out.println("---- list size : " + notesDatabaseHelper.getTags().size() + " size : " + tagsArrayList.size());
        newTagAdapter = new NewTagAdapter(context, tagsArrayList, new NewTagAdapter.ITagAction() {
            @Override
            public void onActionEditTag(int id, int pos) {
                System.out.println("----- click : " + id + " pos : " + pos);
                showDialogActionOnTag(id, pos);
            }

            @Override
            public void onTagFolderSelected(Tags tags, int position) {
                if (!isEditModeEnabled) {
                    TagPos = position;
                    AdapTagId = tags.getId();
                    System.out.println("------ IDD : " + tags.toString() + " pos : " + position);
                    startActivityForResult(new Intent(context, TagsNotesActivity.class)
                            .putExtra(Constant.TAG_FOLDER_ID, position)
                            .putExtra(Constant.TAGS_ID, tags.getId())
                            .putExtra(Constant.TAGS_COLOR_CODE, tags.getColorCodeId())
                            .putExtra(Constant.TAGS_NAME, tags.getTagName()), VIEW_TAGS_REQUEST);
                }
            }
        });
        RvTags.setAdapter(newTagAdapter);
        initRecycler();
    }

    public void initRecycler() {
        int getTags = notesDatabaseHelper.getTags().size();
        for (int i = 0; i < getTags; i++) {
            tagsArrayList.add(notesDatabaseHelper.getTags().get(i));
        }
        newTagAdapter.notifyDataSetChanged();
    }

    public void showDialogActionOnTag(int id, int pos) {
        System.out.println("---- pos : " + pos);
        IOSSheetDialog dialog = new IOSSheetDialog.Builder(this).setTitle((CharSequence) "Choose Action").setCancelText((CharSequence) getResources().getString(R.string.action_cancel))
                .setData(new IOSSheetDialog.SheetItem[]{new IOSSheetDialog.SheetItem(getResources().getString(R.string.action_rename)
                        , IOSSheetDialog.SheetItem.BLUE), new IOSSheetDialog.SheetItem(getResources().getString(R.string.action_delete)
                        , IOSSheetDialog.SheetItem.RED)}, (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    if (i == 0) {
                        mTagAction = TAG_ACTION.RENAME;
                        showDialogAddOrUpdateTag(id, pos, context.getResources().getString(R.string.rename_tag), context.getResources().getString(R.string.rename_tag_des));
                    } else if (i == 1) {
                        mTagAction = TAG_ACTION.DELETE;
                        showDialogDeleteTag(id, pos);
                    }
                }).create();
        dialog.setOnDismissListener(dialogInterface -> {
            //todo editToggle
            handleEditModeToggle();
        });
        dialog.show();
    }

    public void showDialogDeleteTag(int id, int pos) {
        TagsDeleteDialog deleteTagsDialog = new TagsDeleteDialog(context, () -> {
            //todo delete
            deleteTag(id, pos);
        });
        this.deleteTagsDialog = deleteTagsDialog;
        deleteTagsDialog.show();

        WindowManager.LayoutParams lp = deleteTagsDialog.getWindow().getAttributes();
        Window window = deleteTagsDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
        deleteTagsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void deleteTag(int id, int pos) {
        deleteTagsDialog.dismiss();
        notesDatabaseHelper.deleteTags(id);
        tagsArrayList.remove(pos);
        newTagAdapter.notifyItemRemoved(pos);
    }

    private void getListTagAvailable() {
        ArrayList arrayList = new ArrayList();
        listNewTagColor = arrayList;
        arrayList.add(new NewTagColor(R.drawable.md_pink_500, "Pink", 3));
        listNewTagColor.add(new NewTagColor(R.drawable.md_yellow_500, "Yellow", 1));
        listNewTagColor.add(new NewTagColor(R.drawable.md_purple_500, "Purple", 4));
        listNewTagColor.add(new NewTagColor(R.drawable.md_blue_500, "Blue", 5));
        listNewTagColor.add(new NewTagColor(R.drawable.md_cyan_500, "Cyan", 6));
        listNewTagColor.add(new NewTagColor(R.drawable.md_teal_500, "Teal", 7));
        listNewTagColor.add(new NewTagColor(R.drawable.md_lime_500, "Lime", 8));
        listNewTagColor.add(new NewTagColor(R.drawable.md_amber_500, "Amber", 9));
        listNewTagColor.add(new NewTagColor(R.drawable.md_orange_500, "Orange", 10));
        listNewTagColor.add(new NewTagColor(R.drawable.md_gray, "Gray", 11));

        Iterator tagsIterator = tagsArrayList.iterator();
        while (tagsIterator.hasNext()) {
            Tags next = (Tags) tagsIterator.next();
            Iterator<NewTagColor> newTagColorIterator = listNewTagColor.iterator();
            while (true) {
                if (newTagColorIterator.hasNext()) {
                    if (newTagColorIterator.next().getTagColorId() == next.getColorCodeId()) {
                        newTagColorIterator.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    public void showDialogAddOrUpdateTag(int id, int pos, String str, String str2) {
        int i;
        Resources resources;
        if (mTagAction == TAG_ACTION.CREATE) {
            resources = getResources();
            i = R.string.create_button;
        } else {
            resources = getResources();
            i = R.string.save;
        }
        AddNewTagDialog addNewTagDialog = new AddNewTagDialog(this, str, str2, resources.getString(i), new AddNewTagDialog.CreateNewTagListeners() {
            public void onSaveTag(String str) {
                if (str == null || str.trim().equals("")) {
                    Toast.makeText(context, context.getResources().getString(R.string.tag_name_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                MainActivity.this.addNewTagDialog.cancel();
                //todo rename
                if (mTagAction == TAG_ACTION.RENAME) {
                    renameTag(id, pos, str);
                } else if (listNewTagColor.size() > 0) {
                    showTagColorDialog(str);
                } else {
                    createDefaultTag(str);
                }
            }
        });
        this.addNewTagDialog = addNewTagDialog;
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

    private void renameTag(int id, int i, String str) {
        Tags getTags = tagsArrayList.get(i);
        notesDatabaseHelper.updateTags(new Tags(id, str, getTags.getColorCodeId(), getTags.getCounterNote()));
        System.out.println("---- idd: " + id + " i : " + i);
        Tags tags = notesDatabaseHelper.getTagsRecord(id);
        tagsArrayList.set((i), tags);
        newTagAdapter.notifyItemChanged((i));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case VIEW_TAGS_REQUEST:
                switch (resultCode){
                    case RESULT_OK:
                        if (newTagAdapter != null) {
                            initActions();
                            Tags tags = new Tags(tagsArrayList.get(0).getTagName(), tagsArrayList.get(0).getColorCodeId(), notesDatabaseHelper.getAllNotes(0).size());
                            tagsArrayList.set(0, tags);
                            tags = new Tags(tagsArrayList.get(1).getTagName(), tagsArrayList.get(1).getColorCodeId(), notesDatabaseHelper.getAllNotes(1).size());
                            tagsArrayList.set(1, tags);
                            int getTags = notesDatabaseHelper.getTags().size();
                            System.out.println("------ POss : " + getTags);

                            for (int i = 0; i < getTags; i++) {
                                if (notesDatabaseHelper.getTags().get(i).getId() == AdapTagId) {
                                    tagsArrayList.set(TagPos, notesDatabaseHelper.getTags().get(i));
                                }
                                newTagAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tags tags = new Tags(tagsArrayList.get(0).getTagName(), tagsArrayList.get(0).getColorCodeId(), notesDatabaseHelper.getAllNotes(0).size());
        tagsArrayList.set(0, tags);
        tags = new Tags(tagsArrayList.get(1).getTagName(), tagsArrayList.get(1).getColorCodeId(), notesDatabaseHelper.getAllNotes(1).size());
        tagsArrayList.set(1, tags);

        newTagAdapter.notifyDataSetChanged();
    }

    private void createDefaultTag(String str) {
        int TagCount = (int) notesDatabaseHelper.getTagsCount();
        if (TagCount >= 0) {
            TagCount++;
        }
        Tags tag = new Tags(TagCount, str, -1, 0);
        tagsArrayList.add(tag);
        notesDatabaseHelper.insertTags(tag);
        newTagAdapter.notifyDataSetChanged();
    }

    public void showTagColorDialog(final String str) {
        NewTagColorCodeDialog newTagColorCodeDialog2 = new NewTagColorCodeDialog(context, new NewTagColorCodeDialog.NewTagColorListener() {
            public NewTagColor getNewTagColor(int i) {
                return MainActivity.this.listNewTagColor.get(i);
            }

            public int getNewTagColorCount() {
                return MainActivity.this.listNewTagColor.size();
            }

            public void onNewTagColorSelected(final int i) {
                System.out.println("---- count : " + notesDatabaseHelper.getTagsCount());
                int TagCount = (int) notesDatabaseHelper.getTagsCount();
                if (TagCount >= 0) {
                    TagCount++;
                }
                Tags tag = new Tags(TagCount, str, listNewTagColor.get(i).getTagColorId(), 0);
                tagsArrayList.add(tag);
                notesDatabaseHelper.insertTags(tag);
                newTagAdapter.notifyDataSetChanged();
//                MainActivity.this.mRealm.executeTransaction(new Realm.Transaction() {
//                    public void execute(Realm realm) {
//                        int i;
//                        Number max = realm.where(Tag.class).max(FacebookAdapter.KEY_ID);
//                        if (max == null) {
//                            i = 0;
//                        } else {
//                            i = max.intValue() + 1;
//                        }
//                        Tags tag = new Tags(i,str,listTagColor.get(i).getTagColorId(),0);
////                        tag.setId(i);
////                        tag.setTagName(str);
////                        tag.setColorCodeId(MainActivity.this.listTagColor.get(i).getTagColorId());
////                        tag.setCountNote(0);
//                        realm.insertOrUpdate((RealmModel) tag);
//                    }
//                });
                MainActivity.this.newTagColorCodeDialog.dismiss();
//                mListTags = mainActivity.mRealm.where(Tag.class).sort(FacebookAdapter.KEY_ID, Sort.ASCENDING).findAll();
//                MainActivity.this.mTagAdapter.updateData(MainActivity.this.mListTags);
            }
        });
        this.newTagColorCodeDialog = newTagColorCodeDialog2;
        newTagColorCodeDialog2.show();
        WindowManager.LayoutParams lp = newTagColorCodeDialog.getWindow().getAttributes();
        Window window = newTagColorCodeDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvSettings:
                GotoSetting();
                break;
            case R.id.TvEditTag:
                GotoEditTag();
                break;
            case R.id.TvAddNewTag:
                GotoAddNewTag();
                break;
        }
    }

    private void GotoSetting() {
        startActivity(new Intent(context, SettingActivity.class));
    }

    private void GotoEditTag() {
        handleEditModeToggle();
    }

    private void GotoAddNewTag() {
        mTagAction = TAG_ACTION.CREATE;
        getListTagAvailable();
        showDialogAddOrUpdateTag(0, 0, getResources().getString(R.string.new_tag), getResources().getString(R.string.new_tag_des));
    }

    public void handleEditModeToggle() {
        boolean z = !isEditModeEnabled;
        isEditModeEnabled = z;
        newTagAdapter.enableEditMode(z);
        if (isEditModeEnabled) {
            TvEditTag.setText(getResources().getString(R.string.cancel));
            TvAddNewTag.setVisibility(View.GONE);
            return;
        }
        TvEditTag.setText(getResources().getString(R.string.edit));
        TvAddNewTag.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isEditModeEnabled) {
            handleEditModeToggle();
        }
    }
}