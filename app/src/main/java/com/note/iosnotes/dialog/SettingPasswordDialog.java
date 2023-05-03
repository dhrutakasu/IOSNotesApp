package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.note.iosnotes.R;

public class SettingPasswordDialog extends Dialog {
    private SetPasswordListener setPasswordListener;
    private boolean IsChangePass;
    private boolean IsSetNewPass;
    private String StrMessage;
    private String StrOldPassword;
    private String StrTitle;

    public interface SetPasswordListener {
        void SetOnNewPasswordSet(String str);

        void SetOnPasswordEnter(String str);
    }

    public SettingPasswordDialog(Context context, String title, String msg, boolean isSetNewPass, boolean isChangePass, String oldPassword, SetPasswordListener setPasswordListener) {
        super(context);
        this.StrTitle = title;
        this.StrMessage = msg;
        this.setPasswordListener = setPasswordListener;
        this.IsSetNewPass = isSetNewPass;
        this.IsChangePass = isChangePass;
        this.StrOldPassword = oldPassword;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_setting_new_password);
        EditText EdtOldPasswordField = (EditText) findViewById(R.id.EdtOldPasswordField);
        EditText EdtPasswordField = (EditText) findViewById(R.id.EdtPasswordField);
        EditText EdtConfirmPasswordField = (EditText) findViewById(R.id.EdtConfirmPasswordField);
        TextView TvDialogHintPassword = (TextView) findViewById(R.id.TvDialogHintPassword);
        String hintPassword = getStrHintPassword(StrOldPassword);
        if (hintPassword != null) {
            TvDialogHintPassword.setText("Old Password Hint: " + hintPassword);
        } else {
            TvDialogHintPassword.setVisibility(View.GONE);
        }
        if (!IsSetNewPass) {
            EdtConfirmPasswordField.setVisibility(View.GONE);
            EdtOldPasswordField.setVisibility(View.GONE);
            EdtPasswordField.setHint("Password");
        } else if (!IsChangePass) {
            EdtOldPasswordField.setVisibility(View.GONE);
            TvDialogHintPassword.setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.TvDialogPasswordTitle)).setText(StrTitle);
        ((TextView) findViewById(R.id.TvDialogPasswordMsg)).setText(StrMessage);
        ((TextView) findViewById(R.id.TvCancelPassword)).setOnClickListener(view -> dismiss());
        ((TextView) findViewById(R.id.TvConfirmPassword)).setOnClickListener(view -> ConfirmPasswordDialog(EdtPasswordField, EdtConfirmPasswordField, EdtOldPasswordField));
    }

    public void ConfirmPasswordDialog(EditText edtPasswordField, EditText edtConfirmPasswordField, EditText edtOldPasswordField) {
        if (IsSetNewPass && !IsChangePass) {
            String strPass = edtPasswordField.getText().toString();
            String strConfirmPass = edtConfirmPasswordField.getText().toString();
            if (strPass.trim().equals("")) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.msg_enter_password), Toast.LENGTH_SHORT).show();
            } else if (strConfirmPass.trim().equals("")) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.msg_re_enter_password), Toast.LENGTH_SHORT).show();
            } else if (!strConfirmPass.equals(strPass)) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.msg_password_mismatch), Toast.LENGTH_SHORT).show();
            } else {
                setPasswordListener.SetOnNewPasswordSet(strPass);
                dismiss();
            }
        } else if (IsSetNewPass) {
            String strOldPass = edtOldPasswordField.getText().toString();
            String strPass = edtPasswordField.getText().toString();
            String strConfirmPass = edtConfirmPasswordField.getText().toString();
            if (!strOldPass.equals(StrOldPassword)) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.msg_incorrect_old_pass), Toast.LENGTH_SHORT).show();
            } else if (strPass.trim().equals("")) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.msg_enter_password), Toast.LENGTH_SHORT).show();
            } else if (strConfirmPass.trim().equals("")) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.msg_re_enter_password), Toast.LENGTH_SHORT).show();
            } else if (!strConfirmPass.equals(strPass)) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.msg_password_mismatch), Toast.LENGTH_SHORT).show();
            } else {
                setPasswordListener.SetOnNewPasswordSet(strPass);
                dismiss();
            }
        } else {
            String obj6 = edtPasswordField.getText().toString();
            if (obj6.trim().equals("")) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.msg_enter_password), Toast.LENGTH_SHORT).show();
                return;
            }
            if (obj6.equals(StrOldPassword)) {
                dismiss();
            }
            setPasswordListener.SetOnPasswordEnter(obj6);
        }
    }

    private String getStrHintPassword(String pass) {
        int iPass;
        int PassLength = pass.length();
        if (PassLength <= 1) {
            return null;
        }
        if (PassLength % 2 == 0) {
            iPass = (PassLength / 2) - 1;
        } else {
            iPass = (PassLength - 1) / 2;
        }
        StringBuilder builder = new StringBuilder();
        for (int i2 = 0; i2 < PassLength; i2++) {
            if (i2 <= iPass) {
                builder.append(pass.charAt(i2));
            } else {
                builder.append("*");
            }
        }
        return builder.toString();
    }
}
