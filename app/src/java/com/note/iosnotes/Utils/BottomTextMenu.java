package com.note.iosnotes.Utils;

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

import com.note.iosnotes.R;
import com.note.iosnotes.dialog.NotesTextSizeDialog;

public class BottomTextMenu extends RoundedBottomDialogFragment {
    private final int textSize;
    private final int count;
    private setBottomTextMenu iBottomMenu;
    private boolean titleBold;
    private boolean titleItalic;
    private boolean titleUnderline;
    private boolean titleStrike;
    private boolean contentBold;
    private boolean contentItalic;
    private boolean contentUnderline;
    private boolean contentStrike;
    private int align;
    private LinearLayout ll_action_bold_title, ll_action_italic_title, ll_action_underline_title, ll_action_strik_title;
    private ImageView img_bold_title, img_italic_title, img_underline_title, img_strik_title;
    private LinearLayout ll_action_bold_content, ll_action_italic_content, ll_action_underline_content, ll_action_strik_content;
    private ImageView img_bold_content, img_italic_content, img_underline_content, img_strik_content;
    private LinearLayout ll_action_left, ll_action_right, ll_action_center;
    private ImageView img_left_align, img_right_align, img_center_align;
    private ImageView IvCloseBottomText;
    private TextView TvCount;
    private TextView TvSize;
    private RelativeLayout RlActionTextSize;

    public interface setBottomTextMenu {
        void onTextSize(int size);

        void onTitle(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike);

        void onContent(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike);

        void onAlign(int pos);

    }

    public BottomTextMenu(int count,int textSize,boolean titleBold, boolean titleItalic, boolean titleUnderline, boolean titleStrike, boolean contentBold, boolean contentItalic, boolean contentUnderline, boolean contentStrike, int align, setBottomTextMenu Menu) {
        this.count = count;
        this.textSize = textSize;
        this.titleBold = titleBold;
        this.titleItalic = titleItalic;
        this.titleUnderline = titleUnderline;
        this.titleStrike = titleStrike;
        this.contentBold = contentBold;
        this.contentItalic = contentItalic;
        this.contentUnderline = contentUnderline;
        this.contentStrike = contentStrike;
        this.align = align;
        this.iBottomMenu = Menu;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.bottom_sheet_text, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(view);
    }

    private void initView(View view) {
        IvCloseBottomText = (ImageView) view.findViewById(R.id.IvCloseBottomText);
        ll_action_bold_title = (LinearLayout) view.findViewById(R.id.ll_action_bold_title);
        ll_action_italic_title = (LinearLayout) view.findViewById(R.id.ll_action_italic_title);
        ll_action_underline_title = (LinearLayout) view.findViewById(R.id.ll_action_underline_title);
        ll_action_strik_title = (LinearLayout) view.findViewById(R.id.ll_action_strik_title);
        img_bold_title = (ImageView) view.findViewById(R.id.img_bold_title);
        img_italic_title = (ImageView) view.findViewById(R.id.img_italic_title);
        img_underline_title = (ImageView) view.findViewById(R.id.img_underline_title);
        img_strik_title = (ImageView) view.findViewById(R.id.img_strik_title);

        ll_action_bold_content = (LinearLayout) view.findViewById(R.id.ll_action_bold_content);
        ll_action_italic_content = (LinearLayout) view.findViewById(R.id.ll_action_italic_content);
        ll_action_underline_content = (LinearLayout) view.findViewById(R.id.ll_action_underline_content);
        ll_action_strik_content = (LinearLayout) view.findViewById(R.id.ll_action_strik_content);
        img_bold_content = (ImageView) view.findViewById(R.id.img_bold_content);
        img_italic_content = (ImageView) view.findViewById(R.id.img_italic_content);
        img_underline_content = (ImageView) view.findViewById(R.id.img_underline_content);
        img_strik_content = (ImageView) view.findViewById(R.id.img_strik_content);

        ll_action_left = (LinearLayout) view.findViewById(R.id.ll_action_left);
        ll_action_center = (LinearLayout) view.findViewById(R.id.ll_action_center);
        ll_action_right = (LinearLayout) view.findViewById(R.id.ll_action_right);
        img_left_align = (ImageView) view.findViewById(R.id.img_left_align);
        img_center_align = (ImageView) view.findViewById(R.id.img_center_align);
        img_right_align = (ImageView) view.findViewById(R.id.img_right_align);
        TvCount = (TextView) view.findViewById(R.id.TvCount);
        TvSize = (TextView) view.findViewById(R.id.TvSize);
        RlActionTextSize = (RelativeLayout) view.findViewById(R.id.RlActionTextSize);
        TvCount.setText(count + "");
        String size = new Pref(getContext()).getInt(Constant.STR_TEXT_SIZE) + "%";
        if (size.equalsIgnoreCase("0%")) {
            TvSize.setText("100%");
        } else {
            TvSize.setText(size);
        }
        if (titleBold) {
            img_bold_title.setImageResource(R.drawable.ic_bold_press);
        }
        if (titleItalic) {
            img_italic_title.setImageResource(R.drawable.ic_italic_press);
        }
        if (titleUnderline) {
            img_underline_title.setImageResource(R.drawable.ic_underline_press);
        }
        if (titleStrike) {
            img_strik_title.setImageResource(R.drawable.ic_strikethrough_press);
        }

        if (contentBold) {
            img_bold_content.setImageResource(R.drawable.ic_bold_press);
        }
        if (contentItalic) {
            img_italic_content.setImageResource(R.drawable.ic_italic_press);
        }
        if (contentUnderline) {
            img_underline_content.setImageResource(R.drawable.ic_underline_press);
        }
        if (contentStrike) {
            img_strik_content.setImageResource(R.drawable.ic_strikethrough_press);
        }

        if (align == 1) {
            img_left_align.setImageResource(R.drawable.ic_left_press);
        } else if (align == 2) {
            img_center_align.setImageResource(R.drawable.ic_center_press);
        } else if (align == 3) {
            img_right_align.setImageResource(R.drawable.ic_right_press);
        }
        RlActionTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesTextSizeDialog notesTextSizeDialog = new NotesTextSizeDialog(getContext(), new NotesTextSizeDialog.TextSizeListeners() {
                    @Override
                    public void setTextSize(int sizef) {
                        int size = new Pref(getContext()).getInt(Constant.STR_TEXT_SIZE);
                        iBottomMenu.onTextSize(size);
                        String sizeStr = new Pref(getContext()).getInt(Constant.STR_TEXT_SIZE) + "%";
                        TvSize.setText(sizeStr);
                    }
                });
                notesTextSizeDialog.show();

                WindowManager.LayoutParams lp = notesTextSizeDialog.getWindow().getAttributes();
                Window window = notesTextSizeDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                lp.gravity = Gravity.CENTER;
                notesTextSizeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        });
        IvCloseBottomText.setOnClickListener(view14 -> dismiss());
        ll_action_bold_title.setOnClickListener(view15 -> {
            titleBold = true;
            img_bold_title.setImageResource(R.drawable.ic_bold_press);
            iBottomMenu.onTitle(titleBold, titleItalic, titleUnderline, titleStrike);
        });
        ll_action_italic_title.setOnClickListener(view16 -> {
            if (!titleItalic) {
                titleItalic = true;
                img_italic_title.setImageResource(R.drawable.ic_italic_press);
            } else {
                titleItalic = false;
                img_italic_title.setImageResource(R.drawable.ic_italic);
            }
            iBottomMenu.onTitle(titleBold, titleItalic, titleUnderline, titleStrike);
        });
        ll_action_underline_title.setOnClickListener(view17 -> {
            if (!titleUnderline) {
                titleUnderline = true;
                img_underline_title.setImageResource(R.drawable.ic_underline_press);
            } else {
                titleUnderline = false;
                img_underline_title.setImageResource(R.drawable.ic_underline);
            }
            iBottomMenu.onTitle(titleBold, titleItalic, titleUnderline, titleStrike);
        });
        ll_action_strik_title.setOnClickListener(view18 -> {
            if (!titleStrike) {
                titleStrike = true;
                img_strik_title.setImageResource(R.drawable.ic_strikethrough_press);
            } else {
                titleStrike = false;
                img_strik_title.setImageResource(R.drawable.ic_strikethrough);
            }
            iBottomMenu.onTitle(titleBold, titleItalic, titleUnderline, titleStrike);
        });

        ll_action_bold_content.setOnClickListener(view19 -> {
            if (!contentBold) {
                contentBold = true;
                img_bold_content.setImageResource(R.drawable.ic_bold_press);
            } else {
                contentBold = false;
                img_bold_content.setImageResource(R.drawable.ic_bold);
            }
            iBottomMenu.onContent(contentBold, contentItalic, contentUnderline, contentStrike);
        });
        ll_action_italic_content.setOnClickListener(view110 -> {
            if (!contentItalic) {
                contentItalic = true;
                img_italic_content.setImageResource(R.drawable.ic_italic_press);
            } else {
                contentItalic = false;
                img_italic_content.setImageResource(R.drawable.ic_italic);
            }
            iBottomMenu.onContent(contentBold, contentItalic, contentUnderline, contentStrike);
        });
        ll_action_underline_content.setOnClickListener(view1 -> {
            if (!contentUnderline) {
                contentUnderline = true;
                img_underline_content.setImageResource(R.drawable.ic_underline_press);
            } else {
                contentUnderline = false;
                img_underline_content.setImageResource(R.drawable.ic_underline);
            }
            iBottomMenu.onContent(contentBold, contentItalic, contentUnderline, contentStrike);
        });
        ll_action_strik_content.setOnClickListener(view12 -> {
            if (!contentStrike) {
                contentStrike = true;
                img_strik_content.setImageResource(R.drawable.ic_strikethrough_press);
            } else {
                contentStrike = false;
                img_strik_content.setImageResource(R.drawable.ic_strikethrough);
            }
            iBottomMenu.onContent(contentBold, contentItalic, contentUnderline, contentStrike);
        });
        ll_action_left.setOnClickListener(view13 -> {
            align = 1;
            img_left_align.setImageResource(R.drawable.ic_left_press);
            img_center_align.setImageResource(R.drawable.ic_align_center);
            img_right_align.setImageResource(R.drawable.ic_align_right);
            iBottomMenu.onAlign(align);
        });
        ll_action_center.setOnClickListener(view111 -> {
            align = 2;
            img_left_align.setImageResource(R.drawable.ic_align_left);
            img_center_align.setImageResource(R.drawable.ic_center_press);
            img_right_align.setImageResource(R.drawable.ic_align_right);
            iBottomMenu.onAlign(align);
        });
        ll_action_right.setOnClickListener(view112 -> {
            align = 3;
            img_left_align.setImageResource(R.drawable.ic_align_left);
            img_center_align.setImageResource(R.drawable.ic_align_center);
            img_right_align.setImageResource(R.drawable.ic_right_press);
            iBottomMenu.onAlign(align);
        });
    }
}
