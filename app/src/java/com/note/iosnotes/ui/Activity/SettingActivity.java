package com.note.iosnotes.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.Utils.Pref;
import com.note.iosnotes.dialog.NoteWidgetBackgroundDialog;
import com.note.iosnotes.dialog.SettingPasswordDialog;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBackSetting;
    private RelativeLayout RlSettingSetPass;
    private RelativeLayout RlSettingWidgetBackground;
    private RelativeLayout RlSettingRatingApp;
    private RelativeLayout RlSettingPolicy;
    private TextView TvSetPassTitle;
    private boolean isPasswordExist;
    private String password;
    private View ViewWidgetBg;
    private int NoteWidgetBgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
        initListeners();
        initActions();
    }

    private void initViews() {
        context = this;
        IvBackSetting = (ImageView) findViewById(R.id.IvBackSetting);
        RlSettingSetPass = (RelativeLayout) findViewById(R.id.RlSettingSetPass);
        TvSetPassTitle = (TextView) findViewById(R.id.TvSetPassTitle);
        RlSettingWidgetBackground = (RelativeLayout) findViewById(R.id.RlSettingWidgetBackground);
        RlSettingRatingApp = (RelativeLayout) findViewById(R.id.RlSettingRatingApp);
        RlSettingPolicy = (RelativeLayout) findViewById(R.id.RlSettingPolicy);
        ViewWidgetBg = (View) findViewById(R.id.ViewWidgetBg);
    }

    private void initListeners() {
        IvBackSetting.setOnClickListener(this);
        RlSettingSetPass.setOnClickListener(this);
        RlSettingWidgetBackground.setOnClickListener(this);
    }

    private void initActions() {
        Pref pref = new Pref(this);
        password = pref.getString(Constant.STR_PASSWORD);
        NoteWidgetBgId = pref.getInt(Constant.TAG_WIDGET_BG_ID);
        isPasswordExist = !password.equals("");
        System.out.println("------ passs : "+isPasswordExist);
        setNoteWidgetBgView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IvBackSetting:
                onBackPressed();
                break;
            case R.id.RlSettingSetPass:
                GotoSetPassword();
                break;
            case R.id.RlSettingWidgetBackground:
                GotoDialogWidgetBg();
                break;
        }
    }

    private void GotoSetPassword() {
        String str;
        String str2;
        if (isPasswordExist) {
            str = getResources().getString(R.string.change_password);
            str2 = getResources().getString(R.string.change_password_msg);
        } else {
            str = getResources().getString(R.string.set_password);
            str2 = getResources().getString(R.string.set_pass_msg);
        }
       SettingPasswordDialog settingPasswordDialog= new SettingPasswordDialog(this, str, str2, true, isPasswordExist, password, new SettingPasswordDialog.SetPasswordListener() {
            public void SetOnPasswordEnter(String str) {
            }

            public void SetOnNewPasswordSet(String str) {
                if (isPasswordExist) {
                    Toast.makeText(context, context.getResources().getString(R.string.change_password_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.set_password_success), Toast.LENGTH_SHORT).show();
                }
                new Pref(context).putString(Constant.STR_PASSWORD, password);
                isPasswordExist = true;
                TvSetPassTitle.setText(context.getResources().getString(R.string.change_password));
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


    private void GotoDialogWidgetBg() {
        NoteWidgetBackgroundDialog noteWidgetBackgroundDialog=new NoteWidgetBackgroundDialog(this, i -> {
            NoteWidgetBgId = i;
            new Pref(context).putInt(Constant.TAG_WIDGET_BG_ID, i);
            setNoteWidgetBgView();
        });
        noteWidgetBackgroundDialog.show();

        WindowManager.LayoutParams lp = noteWidgetBackgroundDialog.getWindow().getAttributes();
        Window window = noteWidgetBackgroundDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
        noteWidgetBackgroundDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setNoteWidgetBgView() {
        int i = NoteWidgetBgId;
        if (i == 1) {
            ViewWidgetBg.setBackgroundResource(R.drawable.bg_widget_02);
        } else if (i == 2) {
            ViewWidgetBg.setBackgroundResource(R.drawable.bg_widget_03);
        } else if (i != 3) {
            ViewWidgetBg.setBackgroundResource(R.drawable.bg_widget_01);
        } else {
            ViewWidgetBg.setBackgroundResource(R.drawable.bg_widget_04);
        }
    }
}