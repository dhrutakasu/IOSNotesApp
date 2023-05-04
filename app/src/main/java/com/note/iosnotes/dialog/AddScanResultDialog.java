package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.note.iosnotes.R;

public class AddScanResultDialog extends Dialog {
    private SetScanResult scanResult;
    private String StrScanResult;

    public interface SetScanResult {
        void ResultCopyToNote();

        void onResultCancel();
    }

    public AddScanResultDialog(Context context, String str, SetScanResult scanResult1) {
        super(context);
        scanResult = scanResult1;
        StrScanResult = str;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_scan_result);
        ((TextView) findViewById(R.id.tv_scan_result_content)).setText(StrScanResult);
        ((TextView) findViewById(R.id.tv_cancel_scan_result)).setOnClickListener(view -> {
            dismiss();
            scanResult.onResultCancel();
        });
        ((TextView) findViewById(R.id.tv_add_to_note)).setOnClickListener(view -> {
            dismiss();
            scanResult.ResultCopyToNote();
        });
    }
}
