package com.note.iosnotes.Utils;

import android.app.Dialog;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.note.iosnotes.R;

public class RoundedBottomDialogFragment extends BottomSheetDialogFragment {
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        return new BottomSheetDialog(getContext(), getTheme());
    }
}
