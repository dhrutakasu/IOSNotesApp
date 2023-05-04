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
import com.note.iosnotes.dialog.SettingPasswordDialog;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView IvBackSetting;
    private RelativeLayout RlSettingSetPass;
    private RelativeLayout RlSettingRemoveAds;
    private RelativeLayout RlSettingMoreApp;
    private RelativeLayout RlSettingWidgetBackground;
    private RelativeLayout RlSettingRatingApp;
    private RelativeLayout RlSettingPolicy;
    private TextView TvSetPassTitle;
    private boolean isPasswordExist;
    private String password;

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
        RlSettingRemoveAds = (RelativeLayout) findViewById(R.id.RlSettingRemoveAds);
        RlSettingMoreApp = (RelativeLayout) findViewById(R.id.RlSettingMoreApp);
        RlSettingWidgetBackground = (RelativeLayout) findViewById(R.id.RlSettingWidgetBackground);
        RlSettingRatingApp = (RelativeLayout) findViewById(R.id.RlSettingRatingApp);
        RlSettingPolicy = (RelativeLayout) findViewById(R.id.RlSettingPolicy);
    }

    private void initListeners() {
        IvBackSetting.setOnClickListener(this);
        RlSettingSetPass.setOnClickListener(this);
    }

    private void initActions() {
        Pref pref = new Pref(this);
        String string = pref.getString(Constant.STR_PASSWORD);
        this.password = string;
        this.isPasswordExist = !string.equals("");
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
        }
    }

    private void GotoSetPassword() {
        String str;
        String str2;
        if (this.isPasswordExist) {
            str = getResources().getString(R.string.change_password);
            str2 = getResources().getString(R.string.change_password_msg);
        } else {
            str = getResources().getString(R.string.set_password);
            str2 = getResources().getString(R.string.set_pass_msg);
        }
       SettingPasswordDialog settingPasswordDialog= new SettingPasswordDialog(this, str, str2, true, this.isPasswordExist, this.password, new SettingPasswordDialog.SetPasswordListener() {
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
}