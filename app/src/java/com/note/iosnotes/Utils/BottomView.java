package com.note.iosnotes.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.note.iosnotes.R;
import com.note.iosnotes.dialog.NotesTextSizeDialog;

public class BottomView extends Dialog {
    private final FragmentManager manager;
    private setBottomMenu setBottomMenu;
    private boolean isLock;
    private boolean isPin;
    private int count;
    private int textSize;
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
    private TextView TvNoteLockBottomView;
    private TextView TvNotePinBottomView;
    private ImageView IvPinUnpin;
    private ImageView IvLockUnlock;
    private LinearLayout LlActionPin;
    private LinearLayout LlActionLock;
    private LinearLayout LlActionScan;
    private RelativeLayout RlActionDeleteNote;
    private RelativeLayout RlActionMoveNote;
    private RelativeLayout RlActionFontStyle;
    private ImageView IvAlignment;

    public interface setBottomMenu {
        void onCreateWidget();

        void onDeleteNote();

        void onLockNote();

        void onMoveNote();

        void onPinNote();

        void onScanNote();

        void onTextSize(int size);

        void onTitleNote(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike);

        void onContentNote(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike);

        void onAlignNote(int pos);
    }

    public BottomView(Context context, FragmentManager manager, String title, boolean isPin, boolean isLock, int count, boolean titleBold, boolean titleItalic, boolean titleUnderline, boolean titleStrike, boolean contentBold, boolean contentItalic, boolean contentUnderline, boolean contentStrike, int align, setBottomMenu Menu) {
        super(context);
        this.manager = manager;
        this.title = title;
        this.isPin = isPin;
        this.isLock = isLock;
        this.setBottomMenu = Menu;
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
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.bottom_view);
        initView();
    }

    private void initView() {
        TvNotePinBottomView = (TextView) findViewById(R.id.TvNotePinBottomView);
        TvNoteLockBottomView = (TextView) findViewById(R.id.TvNoteLockBottomView);
        IvPinUnpin = (ImageView) findViewById(R.id.IvPinUnpin);
        IvLockUnlock = (ImageView) findViewById(R.id.IvLockUnlock);
        LlActionPin = (LinearLayout) findViewById(R.id.LlActionPin);
        LlActionLock = (LinearLayout) findViewById(R.id.LlActionLock);
        LlActionScan = (LinearLayout) findViewById(R.id.LlActionScan);
        RlActionDeleteNote = (RelativeLayout) findViewById(R.id.RlActionDeleteNote);
        RlActionMoveNote = (RelativeLayout) findViewById(R.id.RlActionMoveNote);
        RlActionFontStyle = (RelativeLayout) findViewById(R.id.RlActionFontStyle);
        IvAlignment = (ImageView) findViewById(R.id.IvAlignment);

        ((RelativeLayout) findViewById(R.id.RlActionCreateWidget)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                setBottomMenu.onCreateWidget();
            }
        });
        if (isPin) {
            IvPinUnpin.setColorFilter(ContextCompat.getColor(getContext(), R.color.app_icon_color), android.graphics.PorterDuff.Mode.SRC_IN);
            TvNotePinBottomView.setText(getContext().getResources().getString(R.string.unpin));
            TvNotePinBottomView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_icon_color));
        } else {
            IvPinUnpin.setImageResource(R.drawable.ic_pin);
            TvNotePinBottomView.setText(getContext().getResources().getString(R.string.pin));
            TvNotePinBottomView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }
        if (isLock) {
            IvLockUnlock.setColorFilter(ContextCompat.getColor(getContext(), R.color.app_icon_color), android.graphics.PorterDuff.Mode.SRC_IN);
            TvNoteLockBottomView.setText(getContext().getResources().getString(R.string.unlock));
            TvNoteLockBottomView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_icon_color));
        } else {
            IvLockUnlock.setImageResource(R.drawable.ic_lock);
            TvNoteLockBottomView.setText(getContext().getResources().getString(R.string.lock));
            TvNoteLockBottomView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }
        if (align == 1) {
            IvAlignment.setImageResource(R.drawable.align_left);
        } else if (align == 2) {
            IvAlignment.setImageResource(R.drawable.align_center);
        } else if (align == 3) {
            IvAlignment.setImageResource(R.drawable.align_right);
        }
        LlActionPin.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                setBottomMenu.onPinNote();
            }
        });
        LlActionLock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                setBottomMenu.onLockNote();
            }
        });
        LlActionScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                setBottomMenu.onScanNote();
            }
        });
        RlActionDeleteNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                setBottomMenu.onDeleteNote();
            }
        });
        RlActionMoveNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                setBottomMenu.onMoveNote();
            }
        });
        RlActionFontStyle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                new BottomTextMenu(count,textSize,titleBold, titleItalic, titleUnderline, titleStrike, contentBold, contentItalic, contentUnderline, contentStrike, align, new BottomTextMenu.setBottomTextMenu() {
                    @Override
                    public void onTextSize(int size) {
                        setBottomMenu.onTextSize(size);
                    }

                    @Override
                    public void onTitle(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike) {
                        setBottomMenu.onTitleNote(IsBold, IsItalic, IsUnderline, IsStrike);
                    }

                    @Override
                    public void onContent(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike) {
                        setBottomMenu.onContentNote(IsBold, IsItalic, IsUnderline, IsStrike);
                    }

                    @Override
                    public void onAlign(int pos) {
                        setBottomMenu.onAlignNote(pos);
                        if (pos == 1) {
                            IvAlignment.setImageResource(R.drawable.align_left);
                        } else if (pos == 2) {
                            IvAlignment.setImageResource(R.drawable.align_center);
                        } else if (pos == 3) {
                            IvAlignment.setImageResource(R.drawable.align_right);
                        }
                    }
                }).show(manager, "BottomTextMenu");
            }
        });
    }
}
