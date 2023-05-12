package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.Utils.Pref;

public class NotesTextSizeDialog extends Dialog {
    private TextSizeListeners textSizeListeners;

    public interface TextSizeListeners {
        void setTextSize(int size);
    }

    public NotesTextSizeDialog(Context context, TextSizeListeners textSizeListeners) {
        super(context);
        this.textSizeListeners = textSizeListeners;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_note_text_size);
        RadioGroup RgSize = findViewById(R.id.RgSize);
        RadioButton Rb50 = findViewById(R.id.Rb50);
        RadioButton Rb75 = findViewById(R.id.Rb75);
        RadioButton Rb90 = findViewById(R.id.Rb90);
        RadioButton Rb100 = findViewById(R.id.Rb100);
        RadioButton Rb125 = findViewById(R.id.Rb125);
        RadioButton Rb150 = findViewById(R.id.Rb150);
        RadioButton Rb175 = findViewById(R.id.Rb175);
        RadioButton Rb200 = findViewById(R.id.Rb200);
        String size = new Pref(getContext()).getInt(Constant.STR_TEXT_SIZE) + "%";
        if (size.equalsIgnoreCase("50%")) {
            Rb50.setChecked(true);
        } else if (size.equalsIgnoreCase("75%")) {
            Rb75.setChecked(true);
        } else if (size.equalsIgnoreCase("90%")) {
            Rb90.setChecked(true);
        } else if (size.equalsIgnoreCase("100%")) {
            Rb100.setChecked(true);
        } else if (size.equalsIgnoreCase("125%")) {
            Rb125.setChecked(true);
        } else if (size.equalsIgnoreCase("150%")) {
            Rb150.setChecked(true);
        } else if (size.equalsIgnoreCase("175%")) {
            Rb175.setChecked(true);
        } else if (size.equalsIgnoreCase("200%")) {
            Rb200.setChecked(true);
        } else {
            Rb100.setChecked(true);
        }
        RgSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                new Pref(getContext()).putInt(Constant.STR_TEXT_SIZE, Integer.parseInt(radioButton.getText().toString().replace("%", "")));
                textSizeListeners.setTextSize(Integer.parseInt(radioButton.getText().toString().replace("%", "")));
                dismiss();
            }
        });
    }
}
