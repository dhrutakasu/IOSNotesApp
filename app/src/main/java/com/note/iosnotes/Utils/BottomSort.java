package com.note.iosnotes.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.note.iosnotes.R;

public class BottomSort extends RoundedBottomDialogFragment {
    private setBottomSort setBottomSort;
    private RelativeLayout RlActionSortAscending;
    private RelativeLayout RlActionSortDescending;
    private RelativeLayout RlActionSortTime;
    private ImageButton BtnCloseBottomSort;

    public interface setBottomSort {
        void onSortAscending();

        void onSortDescending();

        void onSortTime();
    }

    public BottomSort(setBottomSort setBottomSort) {
        this.setBottomSort = setBottomSort;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.bottom_sort, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(view);
    }

    private void initView(View view) {
        BtnCloseBottomSort = (ImageButton) view.findViewById(R.id.BtnCloseBottomSort);
        RlActionSortAscending = (RelativeLayout) view.findViewById(R.id.RlActionSortAscending);
        RlActionSortDescending = (RelativeLayout) view.findViewById(R.id.RlActionSortDescending);
        RlActionSortTime = (RelativeLayout) view.findViewById(R.id.RlActionSortTime);
        BtnCloseBottomSort.setOnClickListener(view14 -> dismiss());
        RlActionSortAscending.setOnClickListener(view13 -> {
            setBottomSort.onSortAscending();
            dismiss();
        });
        RlActionSortDescending.setOnClickListener(view12 -> {
            setBottomSort.onSortDescending();
            dismiss();
        });
        RlActionSortTime.setOnClickListener(view1 -> {
            setBottomSort.onSortTime();
            dismiss();
        });
    }
}
