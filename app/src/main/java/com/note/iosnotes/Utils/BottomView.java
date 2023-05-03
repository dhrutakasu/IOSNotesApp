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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.note.iosnotes.R;

import androidx.fragment.app.FragmentManager;

public class BottomView extends RoundedBottomDialogFragment {
    private final FragmentManager manager;
    String date;
    private IBottomMenu iBottomMenu;
    byte[] imgByteArr;
    int imgOrientCode;
    boolean isLock;
    boolean isPin;

    boolean titleBold;
    boolean titleItalic;
    boolean titleUnderline;
    boolean titleStrike;
    boolean contentBold;
    boolean contentItalic;
    boolean contentUnderline;
    boolean contentStrike;
    int align;
    String title;
    TextView tvDate;
    TextView tvLock;
    TextView tvPin;
    TextView tvTitle;

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

    public BottomView(FragmentManager manager, String title, String date, boolean isPin, boolean isLock, byte[] bytes, int imageOrientCOde, boolean titleBold, boolean titleItalic, boolean titleUnderline, boolean titleStrike, boolean contentBold, boolean contentItalic, boolean contentUnderline, boolean contentStrike, int align, IBottomMenu Menu) {
        this.manager = manager;
        this.title = title;
        this.date = date;
        this.isPin = isPin;
        this.isLock = isLock;
        this.iBottomMenu = Menu;
        this.imgByteArr = bytes;
        this.imgOrientCode = imageOrientCOde;
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
        return layoutInflater.inflate(R.layout.bottom_sheet_menu, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(view);
    }

    private void initView(View view) {
        this.tvTitle = (TextView) view.findViewById(R.id.tv_note_title_bottom_menu);
        this.tvDate = (TextView) view.findViewById(R.id.tv_note_date_bottom_menu);
        this.tvPin = (TextView) view.findViewById(R.id.tv_note_pin_bottom_menu);
        this.tvLock = (TextView) view.findViewById(R.id.tv_note_lock_bottom_menu);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_pin_unpin);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.img_lock_unlock);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.img_attach_thumbnail_bottom_menu);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_action_pin);
        LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.ll_action_lock);
        LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.ll_action_delete);
        LinearLayout linearLayout4 = (LinearLayout) view.findViewById(R.id.ll_action_scan);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.btn_close_bottom_menu);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_action_share_note);
        RelativeLayout relativeLayout2 = (RelativeLayout) view.findViewById(R.id.rl_action_move_note);
        RelativeLayout rl_action_font_style = (RelativeLayout) view.findViewById(R.id.rl_action_font_style);
        ((RelativeLayout) view.findViewById(R.id.rl_action_create_widget)).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onCreateWidget();
            }
        });
        if (this.isPin) {
            imageView.setImageResource(R.drawable.ic_un_pin);
            this.tvPin.setText(getResources().getString(R.string.unpin));
        } else {
            imageView.setImageResource(R.drawable.ic_pin);
            this.tvPin.setText(getResources().getString(R.string.pin));
        }
        if (this.isLock) {
            imageView2.setImageResource(R.drawable.ic_unlock);
            this.tvLock.setText(getResources().getString(R.string.unlock));
        } else {
            imageView2.setImageResource(R.drawable.ic_lock);
            this.tvLock.setText(getResources().getString(R.string.lock));
        }
        this.tvDate.setText(this.date);
        this.tvTitle.setText(this.title);
        Bitmap bitmap = Constant.getBitmap(this.imgByteArr, this.imgOrientCode);
        if (bitmap == null) {
            imageView3.setVisibility(View.GONE);
        } else {
            Glide.with((View) imageView3).load(bitmap).apply((BaseRequestOptions<?>) new RequestOptions().override(200, 200)).apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(new RoundedCorners(24))).into(imageView3);
            imageView3.setVisibility(View.VISIBLE);
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onPinNote();
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onLockNote();
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onScanNote();
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onDeleteNote();
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onMoveNote();
            }
        });
        rl_action_font_style.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                new BottomTextMenu(true, false, false, false, false, false, false, false, 0, new BottomTextMenu.IBottomTextMenu() {


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
                    }
                }).show(manager, "BottomTextMenu");
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dismiss();
                iBottomMenu.onShareNote();
            }
        });
    }
}
