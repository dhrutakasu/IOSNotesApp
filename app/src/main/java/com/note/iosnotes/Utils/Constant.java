package com.note.iosnotes.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;

import com.note.iosnotes.Model.Note;
import com.note.iosnotes.R;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Constant {
    public static final String STR_PASSWORD = "PASSWORD";
    public static final String TAGS_ID = "Tag_Id";
    public static final String TAGS_COLOR_CODE = "Tag_Color_Code";
    public static final String TAGS_NAME = "Tag_Name";

    private static final long LONG_DAY_MILLIS = 86400000;
    public static final String FORMAT_HOUR_AGO = "hh:mm a";
    public static final String FORMAT_DAY_AGO = "dd/MM/yyyy";
    public static final String STR_TEXT_SIZE = "Str_Text_Size";

    public static final int BITMAP_ROTATION_180 = 180;
    public static final int BITMAP_ROTATION_270 = 270;
    public static String NOTE_ACTION_CODE = "Note_Action";
    public static final String TAG_FOLDER_ID = "Folder_Id";
    public static String TAG_WIDGET_ID = "Widget_Id";
    public static String TAG_WIDGET_BG_ID = "Widget_Bg_Id";
    public static String NOTE_RETURN_RESULT = "Note_Return_Result";
    public static String SCAN_RESULT = "Scan_Result";

    public static final int IMAGE_MAX_HEIGHT = 1280;
    public static final int IMAGE_MAX_WIDTH = 720;
    public static String TAG_WIDGET_NOTE_TITLE = "WIDGET_NOTE_TITLE";
    public static String TAG_WIDGET_NOTE_ID = "WIDGET_NOTE_ID";
    public static String TAG_WIDGET_NOTE_CONTENT = "WIDGET_NOTE_CONTENT";
    public static String WIDGET_TEXT_COLOR = "WIDGET_TEXT_COLOR";

    public static String getTime(Date date) {
        long Millis = Calendar.getInstance().getTimeInMillis() - date.getTime();
        if (Millis < LONG_DAY_MILLIS) {
            return new SimpleDateFormat(Constant.FORMAT_HOUR_AGO, Locale.US).format(date);
        }
        if (Millis < 172800000) {
            return "Yesterday";
        }
        return new SimpleDateFormat(Constant.FORMAT_DAY_AGO, Locale.getDefault()).format(date);
    }

    public static Drawable getDrawableOfBackground(int pos, Context context, View view, int count, ArrayList<Note> notes) {
        int decreasePos = pos - 1;
        int increasePos = pos + 1;
        int itemCount = count;
        if (decreasePos >= 0) {
            Note note = notes.get(pos);
            if (notes.get(decreasePos).isPinnedOrNot()) {
                if (note.isPinnedOrNot()) {
                    if (increasePos >= itemCount) {
                        view.setVisibility(View.GONE);
                        return context.getResources().getDrawable(R.drawable.bg_note_item_round_bottom);
                    } else if (notes.get(increasePos).isPinnedOrNot()) {
                        view.setVisibility(View.VISIBLE);
                        return context.getResources().getDrawable(R.drawable.bg_note_item_middle);
                    } else {
                        view.setVisibility(View.GONE);
                        return context.getResources().getDrawable(R.drawable.bg_note_item_round_bottom);
                    }
                } else if (increasePos >= itemCount) {
                    view.setVisibility(View.GONE);
                    return context.getResources().getDrawable(R.drawable.bg_note_item_rounded);
                } else {
                    view.setVisibility(View.VISIBLE);
                    return context.getResources().getDrawable(R.drawable.bg_note_item_round_top);
                }
            } else if (increasePos >= itemCount) {
                view.setVisibility(View.GONE);
                return context.getResources().getDrawable(R.drawable.bg_note_item_round_bottom);
            } else {
                view.setVisibility(View.VISIBLE);
                return context.getResources().getDrawable(R.drawable.bg_note_item_middle);
            }
        } else if (increasePos >= itemCount) {
            view.setVisibility(View.GONE);
            return context.getResources().getDrawable(R.drawable.bg_note_item_rounded);
        } else if (notes.get(increasePos).isPinnedOrNot()) {
            return context.getResources().getDrawable(R.drawable.bg_note_item_round_top);
        } else {
            if (!notes.get(pos).isPinnedOrNot()) {
                return context.getResources().getDrawable(R.drawable.bg_note_item_round_top);
            }
            view.setVisibility(View.GONE);
            return context.getResources().getDrawable(R.drawable.bg_note_item_rounded);
        }
    }

    public static Bitmap getBitmapOfRotate(byte[] bytes, int orient) {
        if (!(bytes == null || bytes.length == 0)) {
            int rotate = 0;
            Bitmap stream = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes), (Rect) null, (BitmapFactory.Options) null);
            Matrix matrix = new Matrix();
            if (orient == 3) {
                rotate = Constant.BITMAP_ROTATION_180;
            } else if (orient == 6) {
                rotate = 90;
            } else if (orient == 8) {
                rotate = Constant.BITMAP_ROTATION_270;
            }
            matrix.postRotate((float) rotate);
            if (stream != null) {
                return Bitmap.createBitmap(stream, 0, 0, stream.getWidth(), stream.getHeight(), matrix, true);
            }
        }
        return null;
    }

    public static boolean checkPermission(Context context, String str) {
        return ActivityCompat.checkSelfPermission(context, str) == 0;
    }

    public static Bitmap getResizeOfBitmap(Bitmap bitmap, int mWidth, int mHeight) {
        if (mHeight <= 0 || mWidth <= 0) {
            return bitmap;
        }
        return Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, true);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        InputMethodManager methodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            methodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static Bitmap getRoundedCornerOfBitmap(Bitmap bitmap, int radius) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float f = (float) radius;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }
}
