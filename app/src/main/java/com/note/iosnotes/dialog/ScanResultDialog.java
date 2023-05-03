package com.note.iosnotes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.note.iosnotes.R;

public class ScanResultDialog extends Dialog {
    private IScanResult iScanResult;
    private String scanResult;

    public interface IScanResult {
        void copyResultToNote();

        void onCancelResult();
    }

    public ScanResultDialog(Context context, String str, IScanResult iScanResult2) {
        super(context);
        this.iScanResult = iScanResult2;
        this.scanResult = str;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_scan_result);
        ((TextView) findViewById(R.id.tv_scan_result_content)).setText(this.scanResult);
        ((TextView) findViewById(R.id.tv_cancel_scan_result)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ScanResultDialog.this.lambda$onCreate$0$ScanResultDialog(view);
            }
        });
        ((TextView) findViewById(R.id.tv_add_to_note)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ScanResultDialog.this.lambda$onCreate$1$ScanResultDialog(view);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0$ScanResultDialog(View view) {
        dismiss();
        this.iScanResult.onCancelResult();
    }

    public /* synthetic */ void lambda$onCreate$1$ScanResultDialog(View view) {
        dismiss();
        this.iScanResult.copyResultToNote();
    }
}
