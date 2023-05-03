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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.note.iosnotes.R;

import androidx.core.content.ContextCompat;

public class BottomTextMenu extends RoundedBottomDialogFragment {
    private IBottomTextMenu iBottomMenu;
    boolean titleBold;
    boolean titleItalic;
    boolean titleUnderline;
    boolean titleStrike;
    boolean contentBold;
    boolean contentItalic;
    boolean contentUnderline;
    boolean contentStrike;
    int align;
    LinearLayout ll_action_bold_title, ll_action_italic_title, ll_action_underline_title, ll_action_strik_title;
    TextView tv_bold_title, tv_note_italic_title, tv_note_underline_title, tv_note_strik_title;
    ImageView img_bold_title, img_italic_title, img_underline_title, img_strik_title;
    LinearLayout ll_action_bold_content, ll_action_italic_content, ll_action_underline_content, ll_action_strik_content;
    TextView tv_bold_content, tv_note_italic_content, tv_note_underline_content, tv_note_strik_content;
    ImageView img_bold_content, img_italic_content, img_underline_content, img_strik_content;
    LinearLayout ll_action_left, ll_action_right, ll_action_center;
    TextView tv_left_align, tv_right_align, tv_center_align;
    ImageView img_left_align, img_right_align, img_center_align;
    private ImageButton imageButton;

    public interface IBottomTextMenu {
        void onTitle(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike);

        void onContent(boolean IsBold, boolean IsItalic, boolean IsUnderline, boolean IsStrike);

        void onAlign(int pos);

    }

    public BottomTextMenu(boolean titleBold, boolean titleItalic, boolean titleUnderline, boolean titleStrike, boolean contentBold, boolean contentItalic, boolean contentUnderline, boolean contentStrike, int align, IBottomTextMenu Menu) {
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
        imageButton = (ImageButton) view.findViewById(R.id.btn_close_bottom_text);
        ll_action_bold_title = (LinearLayout) view.findViewById(R.id.ll_action_bold_title);
        ll_action_italic_title = (LinearLayout) view.findViewById(R.id.ll_action_italic_title);
        ll_action_underline_title = (LinearLayout) view.findViewById(R.id.ll_action_underline_title);
        ll_action_strik_title = (LinearLayout) view.findViewById(R.id.ll_action_strik_title);
        tv_bold_title = (TextView) view.findViewById(R.id.tv_bold_title);
        tv_note_italic_title = (TextView) view.findViewById(R.id.tv_note_italic_title);
        tv_note_underline_title = (TextView) view.findViewById(R.id.tv_note_underline_title);
        tv_note_strik_title = (TextView) view.findViewById(R.id.tv_note_strik_title);
        img_bold_title = (ImageView) view.findViewById(R.id.img_bold_title);
        img_italic_title = (ImageView) view.findViewById(R.id.img_italic_title);
        img_underline_title = (ImageView) view.findViewById(R.id.img_underline_title);
        img_strik_title = (ImageView) view.findViewById(R.id.img_strik_title);

        ll_action_bold_content = (LinearLayout) view.findViewById(R.id.ll_action_bold_content);
        ll_action_italic_content = (LinearLayout) view.findViewById(R.id.ll_action_italic_content);
        ll_action_underline_content = (LinearLayout) view.findViewById(R.id.ll_action_underline_content);
        ll_action_strik_content = (LinearLayout) view.findViewById(R.id.ll_action_strik_content);
        tv_bold_content = (TextView) view.findViewById(R.id.tv_bold_content);
        tv_note_italic_content = (TextView) view.findViewById(R.id.tv_note_italic_content);
        tv_note_underline_content = (TextView) view.findViewById(R.id.tv_note_underline_content);
        tv_note_strik_content = (TextView) view.findViewById(R.id.tv_note_strik_content);
        img_bold_content = (ImageView) view.findViewById(R.id.img_bold_content);
        img_italic_content = (ImageView) view.findViewById(R.id.img_italic_content);
        img_underline_content = (ImageView) view.findViewById(R.id.img_underline_content);
        img_strik_content = (ImageView) view.findViewById(R.id.img_strik_content);

        ll_action_left = (LinearLayout) view.findViewById(R.id.ll_action_left);
        ll_action_center = (LinearLayout) view.findViewById(R.id.ll_action_center);
        ll_action_right = (LinearLayout) view.findViewById(R.id.ll_action_right);
        tv_left_align = (TextView) view.findViewById(R.id.tv_left_align);
        tv_center_align = (TextView) view.findViewById(R.id.tv_center_align);
        tv_right_align = (TextView) view.findViewById(R.id.tv_right_align);
        img_left_align = (ImageView) view.findViewById(R.id.img_left_align);
        img_center_align = (ImageView) view.findViewById(R.id.img_center_align);
        img_right_align = (ImageView) view.findViewById(R.id.img_right_align);

        if (titleBold) {
            ll_action_bold_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_bold_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_bold_title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        if (titleItalic) {
            ll_action_italic_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_italic_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_note_italic_title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        if (titleUnderline) {
            ll_action_underline_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_underline_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_note_underline_title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        if (titleStrike) {
            ll_action_strik_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_strik_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_note_strik_title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        if (contentBold) {
            ll_action_bold_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_bold_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_bold_content.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        if (contentItalic) {
            ll_action_italic_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_italic_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_note_italic_content.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        if (contentUnderline) {
            ll_action_underline_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_underline_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_note_underline_content.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
        if (contentStrike) {
            ll_action_strik_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_strik_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_note_strik_content.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        if (align == 1) {
            ll_action_left.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_left_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_left_align.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (align == 2) {
            ll_action_center.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_center_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_center_align.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else if (align == 3) {
            ll_action_right.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
            img_right_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            tv_right_align.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ll_action_bold_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleBold = true;
                ll_action_bold_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                img_bold_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_bold_title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                iBottomMenu.onTitle(titleBold, titleItalic, titleUnderline, titleStrike);
            }
        });
        ll_action_italic_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!titleItalic) {
                    titleItalic = true;
                    ll_action_italic_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                    img_italic_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_italic_title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    titleItalic = false;
                    ll_action_italic_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                    img_italic_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_italic_title.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                iBottomMenu.onTitle(titleBold, titleItalic, titleUnderline, titleStrike);
            }
        });
        ll_action_underline_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!titleUnderline) {
                    titleUnderline = true;
                    ll_action_underline_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                    img_underline_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_underline_title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    titleUnderline = false;
                    ll_action_underline_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                    img_underline_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_underline_title.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                iBottomMenu.onTitle(titleBold, titleItalic, titleUnderline, titleStrike);
            }
        });
        ll_action_strik_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!titleStrike) {
                    titleStrike = true;
                    ll_action_strik_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                    img_strik_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_strik_title.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    titleStrike = false;
                    ll_action_strik_title.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                    img_strik_title.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_strik_title.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                iBottomMenu.onTitle(titleBold, titleItalic, titleUnderline, titleStrike);
            }
        });

        ll_action_bold_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contentBold) {
                    contentBold = true;
                    ll_action_bold_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                    img_bold_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_bold_content.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    contentBold = false;
                    ll_action_bold_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                    img_bold_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_bold_content.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                iBottomMenu.onContent(contentBold, contentItalic, contentUnderline, contentStrike);
            }
        });
        ll_action_italic_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contentItalic) {
                    contentItalic = true;
                    ll_action_italic_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                    img_italic_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_italic_content.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    contentItalic = false;
                    ll_action_italic_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                    img_italic_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_italic_content.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                iBottomMenu.onContent(contentBold, contentItalic, contentUnderline, contentStrike);
            }
        });
        ll_action_underline_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contentUnderline) {
                    contentUnderline = true;
                    ll_action_underline_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                    img_underline_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_underline_content.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    contentUnderline = false;
                    ll_action_underline_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                    img_underline_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_underline_content.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                iBottomMenu.onContent(contentBold, contentItalic, contentUnderline, contentStrike);
            }
        });
        ll_action_strik_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contentStrike) {
                    contentStrike = true;
                    ll_action_strik_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                    img_strik_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_strik_content.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    contentStrike = false;
                    ll_action_strik_content.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                    img_strik_content.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                    tv_note_strik_content.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                iBottomMenu.onContent(contentBold, contentItalic, contentUnderline, contentStrike);
            }
        });
        ll_action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                align = 1;
                ll_action_left.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                img_left_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_left_align.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                ll_action_center.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                img_center_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_center_align.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                ll_action_right.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                img_right_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_right_align.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                iBottomMenu.onAlign(align);
            }
        });
        ll_action_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                align = 2;
                ll_action_left.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                img_left_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_left_align.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                ll_action_center.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                img_center_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_center_align.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                ll_action_right.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                img_right_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_right_align.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                iBottomMenu.onAlign(align);
            }
        });
        ll_action_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                align = 3;
                ll_action_left.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                img_left_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_left_align.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                ll_action_center.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_bg_color));
                img_center_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_center_align.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                ll_action_right.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.app_icon_color));
                img_right_align.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                tv_right_align.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                iBottomMenu.onAlign(align);
            }
        });
       /* this.tvTitle = (TextView) view.findViewById(R.id.tv_note_title_bottom_menu);
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
                BottomTextMenu.this.lambda$initView$0$BottomMenu(view);
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
                lambda$initView$1$BottomMenu(view);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                lambda$initView$2$BottomMenu(view);
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                lambda$initView$3$BottomMenu(view);
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                lambda$initView$4$BottomMenu(view);
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                lambda$initView$5$BottomMenu(view);
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                lambda$initView$6$BottomMenu(view);
            }
        });
        rl_action_font_style.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                Toast.makeText(getContext(), "testStyle Dialog...", Toast.LENGTH_SHORT).show();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                lambda$initView$7$BottomMenu(view);
            }
        });*/
    }
}
