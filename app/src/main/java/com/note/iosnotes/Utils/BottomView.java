package com.note.iosnotes.Utils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.note.iosnotes.R;

public class BottomView extends RoundedBottomDialogFragment {
    private final FragmentManager manager;
    private String date;
    private IBottomMenu iBottomMenu;
    private byte[] imgByteArr;
    private int imgOrientCode;
    private boolean isLock;
    private boolean isPin;
    private int count;
    private boolean titleBold;
    private boolean titleItalic;
    private boolean titleUnderline;
    private boolean titleStrike;
    private boolean contentBold;
    private boolean contentItalic;
    private boolean contentUnderline;
    private boolean contentStrike;
    private int align;
    private String title;
    private TextView TvNoteDateBottomView;
    private TextView TvNoteLockBottomView;
    private TextView TvNotePinBottomView;
    private TextView tvNoteTitleBottomView;

    public interface IBottomMenu {
        void onCreateWidget();

        void onDeleteNote();

        void onLockNote();

        void onMoveNote();

        void onPinNote();

        void onScanNote();

        void onShareNote();

        void onTitleNote(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike);

        void onContentNote(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike);

        void onAlignNote(int pos);
    }

    public BottomView(FragmentManager manager, String title, String date, boolean isPin, boolean isLock, byte[] bytes, int imageOrientCOde, int count, boolean titleBold, boolean titleItalic, boolean titleUnderline, boolean titleStrike, boolean contentBold, boolean contentItalic, boolean contentUnderline, boolean contentStrike, int align, IBottomMenu Menu) {
        this.manager = manager;
        this.title = title;
        this.date = date;
        this.isPin = isPin;
        this.isLock = isLock;
        this.iBottomMenu = Menu;
        this.imgByteArr = bytes;
        this.imgOrientCode = imageOrientCOde;
        this.count = count;
        this.titleBold = titleBold;
        this.titleItalic = titleItalic;
        this.titleUnderline = titleUnderline;
        this.titleStrike = titleStrike;
        this.contentBold = contentBold;
        this.contentItalic = contentItalic;
        this.contentUnderline = contentUnderline;
        this.contentStrike = contentStrike;
        this.align = align;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.bottom_view, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(view);
    }

    private void initView(View view) {
        tvNoteTitleBottomView = (TextView) view.findViewById(R.id.tvNoteTitleBottomView);
        TvNoteDateBottomView = (TextView) view.findViewById(R.id.TvNoteDateBottomView);
        TvNotePinBottomView = (TextView) view.findViewById(R.id.TvNotePinBottomView);
        TvNoteLockBottomView = (TextView) view.findViewById(R.id.TvNoteLockBottomView);
        ImageView IvPinUnpin = (ImageView) view.findViewById(R.id.IvPinUnpin);
        ImageView IvLockUnlock = (ImageView) view.findViewById(R.id.IvLockUnlock);
        ImageView IvAttachThumbnailBottomView = (ImageView) view.findViewById(R.id.IvAttachThumbnailBottomView);
        LinearLayout LlActionPin = (LinearLayout) view.findViewById(R.id.LlActionPin);
        LinearLayout LlActionLock = (LinearLayout) view.findViewById(R.id.LlActionLock);
        LinearLayout LlActionDelete = (LinearLayout) view.findViewById(R.id.LlActionDelete);
        LinearLayout LlActionScan = (LinearLayout) view.findViewById(R.id.LlActionScan);
        ImageButton BtnCloseBottomView = (ImageButton) view.findViewById(R.id.BtnCloseBottomView);
        RelativeLayout RlActionShareNote = (RelativeLayout) view.findViewById(R.id.RlActionShareNote);
        RelativeLayout RlActionMoveNote = (RelativeLayout) view.findViewById(R.id.RlActionMoveNote);
        RelativeLayout RlActionFontStyle = (RelativeLayout) view.findViewById(R.id.RlActionFontStyle);
        TextView TvCount = (TextView) view.findViewById(R.id.TvCount);
        TextView TvAlignment = (TextView) view.findViewById(R.id.TvAlignment);
        TvCount.setText(count + "");
        ((RelativeLayout) view.findViewById(R.id.RlActionCreateWidget)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onCreateWidget();
            }
        });
        if (this.isPin) {
            IvPinUnpin.setImageResource(R.drawable.ic_un_pin);
            this.TvNotePinBottomView.setText(getResources().getString(R.string.unpin));
        } else {
            IvPinUnpin.setImageResource(R.drawable.ic_pin);
            this.TvNotePinBottomView.setText(getResources().getString(R.string.pin));
        }
        if (this.isLock) {
            IvLockUnlock.setImageResource(R.drawable.ic_unlock);
            this.TvNoteLockBottomView.setText(getResources().getString(R.string.unlock));
        } else {
            IvLockUnlock.setImageResource(R.drawable.ic_lock);
            this.TvNoteLockBottomView.setText(getResources().getString(R.string.lock));
        }
        this.TvNoteDateBottomView.setText(this.date);
        this.tvNoteTitleBottomView.setText(this.title);
        Bitmap bitmap = Constant.getBitmapOfRotate(this.imgByteArr, this.imgOrientCode);
        if (bitmap == null) {
            IvAttachThumbnailBottomView.setVisibility(View.GONE);
        } else {
            Glide.with((View) IvAttachThumbnailBottomView).load(bitmap).apply((BaseRequestOptions<?>) new RequestOptions().override(200, 200)).apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(new RoundedCorners(24))).into(IvAttachThumbnailBottomView);
            IvAttachThumbnailBottomView.setVisibility(View.VISIBLE);
        }
        BtnCloseBottomView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });
        LlActionPin.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onPinNote();
            }
        });
        LlActionLock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                iBottomMenu.onLockNote();
            }
        });
        LlActionScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                iBottomMenu.onScanNote();
            }
        });
        LlActionDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                iBottomMenu.onDeleteNote();
            }
        });
        RlActionMoveNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                iBottomMenu.onMoveNote();
            }
        });
        RlActionFontStyle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new BottomTextMenu(titleBold, titleItalic, titleUnderline, titleStrike, contentBold, contentItalic, contentUnderline, contentStrike, align, new BottomTextMenu.setBottomTextMenu() {
                    @Override
                    public void onTitle(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike) {
                        iBottomMenu.onTitleNote(IsBold, IsItalic, IsUnderline, IsStrike);
                    }

                    @Override
                    public void onContent(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike) {
                        iBottomMenu.onContentNote(IsBold, IsItalic, IsUnderline, IsStrike);
                    }

                    @Override
                    public void onAlign(int pos) {
                        iBottomMenu.onAlignNote(pos);
                        if (pos==1){
                            TvAlignment.setText("Left");
                        }else if (pos==2){
                            TvAlignment.setText("Center");
                        }else if (pos==3){
                            TvAlignment.setText("Right");
                        }
                    }
                }).show(manager, "BottomTextMenu");
            }
        });
        RlActionShareNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                iBottomMenu.onShareNote();
            }
        });
    }
}
