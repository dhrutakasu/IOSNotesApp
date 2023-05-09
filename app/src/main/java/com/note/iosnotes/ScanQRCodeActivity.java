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
import com.note.iosnotes.dialog.AddScanResultDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ScanQRCodeActivity extends AppCompatActivity {
    public CodeScanner codeScanner;
    CodeScannerView ScannerView;
    private int CAMERA_PERMISSION=28;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_scan_qr_code);
        ScannerView = (CodeScannerView) findViewById(R.id.ScannerView);
        if (checkPermission(this)) {
            initActions();
        } else {
            CheckRequestPermission(this);
        }
    }

    private void initActions() {
        CodeScanner codeScanner = new CodeScanner(this, ScannerView);
        this.codeScanner = codeScanner;
        codeScanner.setDecodeCallback(new DecodeCallback() {
            public void onDecoded(final Result result) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        vibrateOnResult();
                        showDialogScanResult(result.getText());
                    }
                });
            }
        });
        codeScanner.startPreview();
    }

    public void showDialogScanResult(final String str) {
        AddScanResultDialog scanResultDialog = new AddScanResultDialog(this, str, new AddScanResultDialog.SetScanResult() {
            public void ResultCopyToNote() {
                Intent intent = new Intent();
                intent.putExtra(Constant.SCAN_RESULT, str);
                setResult(-1, intent);
                onBackPressed();
            }

            public void onResultCancel() {
                codeScanner.startPreview();
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
        Vibrator service = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            service.vibrate(VibrationEffect.createOneShot(200, -1));
        } else {
            service.vibrate(200);
        }
    }

    public boolean checkPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.CAMERA") == 0;
    }

    public void CheckRequestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{"android.permission.CAMERA"}, CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == 0) {
                initActions();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.camera_permission_denied), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onResume() {
        super.onResume();
        CodeScanner scanner = codeScanner;
        if (scanner != null) {
            scanner.startPreview();
        }
    }

    public void onStop() {
        super.onStop();
        CodeScanner scanner = codeScanner;
        if (scanner != null) {
            scanner.releaseResources();
        }
    }
}
