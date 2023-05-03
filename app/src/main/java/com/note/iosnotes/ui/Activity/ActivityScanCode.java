package com.note.iosnotes.ui.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.note.iosnotes.R;
import com.note.iosnotes.Utils.Constant;
import com.note.iosnotes.dialog.ScanResultDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ActivityScanCode extends AppCompatActivity {
    public CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_scan_code);
        this.scannerView = (CodeScannerView) findViewById(R.id.scanner_view);
        if (checkPermission(this)) {
            initScanner();
        } else {
            requestPermission(this);
        }
    }

    private void initScanner() {
        CodeScanner codeScanner = new CodeScanner(this, this.scannerView);
        this.mCodeScanner = codeScanner;
        codeScanner.setDecodeCallback(new DecodeCallback() {
            public void onDecoded(final Result result) {
                ActivityScanCode.this.runOnUiThread(new Runnable() {
                    public void run() {
                        ActivityScanCode.this.vibrateOnResult();
                        ActivityScanCode.this.showDialogScanResult(result.getText());
                    }
                });
            }
        });
        this.mCodeScanner.startPreview();
    }

    public void showDialogScanResult(final String str) {
        ScanResultDialog scanResultDialog = new ScanResultDialog(this, str, new ScanResultDialog.IScanResult() {
            public void copyResultToNote() {
                Intent intent = new Intent();
                intent.putExtra(Constant.SCAN_RESULT, str);
                ActivityScanCode.this.setResult(-1, intent);
                ActivityScanCode.this.onBackPressed();
            }

            public void onCancelResult() {
                ActivityScanCode.this.mCodeScanner.startPreview();
            }
        });
        scanResultDialog.show();
        WindowManager.LayoutParams lp = scanResultDialog.getWindow().getAttributes();
        Window window = scanResultDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;
    }

    public void vibrateOnResult() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, -1));
        } else {
            vibrator.vibrate(200);
        }
    }

    public boolean checkPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.CAMERA") == 0;
    }

    public void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{"android.permission.CAMERA"}, 101);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 101 && iArr.length > 0) {
            if (iArr[0] == 0) {
                initScanner();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.camera_permission_denied), Toast.LENGTH_LONG).show();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        CodeScanner codeScanner = this.mCodeScanner;
        if (codeScanner != null) {
            codeScanner.startPreview();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        CodeScanner codeScanner = this.mCodeScanner;
        if (codeScanner != null) {
            codeScanner.releaseResources();
        }
    }
}
