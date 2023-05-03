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
import com.note.iosnotes.ui.Activity.ActivityNewCreateNotes;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Constant {
    public static final String PASSWORD = "PASSWORD";
    public static final String TAGS_ID = "Tag_Id";
    public static final String TAGS_COLOR_CODE = "Tag_Color_Code";
    public static final String TAGS_NAME = "Tag_Name";

    private static final long DAY_MILLIS = 86400000;
    public static final String FORMAT_HOUR_AGO = "hh:mm a";
    public static final String FORMAT_DAY_AGO = "dd/MM/yyyy";

    public static final int ROTATION_180 = 180;
    public static final int ROTATION_270 = 270;
    public static String NOTE_ACTION = "Note_Action";
    public static final String FOLDER_ID = "Folder_Id";
    public static String WIDGET_ID = "Widget_Id";
    public static String WIDGET_BG_ID = "Widget_Bg_Id";
    public static String NOTE_RETURN_RESULT = "Note_Return_Result";
    public static String FIRST_USER = "First_User";
    public static String SCAN_RESULT = "Scan_Result";

    public static final int IMG_REL_MAX_HEIGHT = 1280;
    public static final int IMG_REL_MAX_WIDTH = 720;

    public static String getTimeAgo(Date date) {
        long timeInMillis = Calendar.getInstance().getTimeInMillis() - date.getTime();
        if (timeInMillis < DAY_MILLIS) {
            return new SimpleDateFormat(Constant.FORMAT_HOUR_AGO, Locale.US).format(date);
        }
        if (timeInMillis < 172800000) {
            return "Yesterday";
        }
        return new SimpleDateFormat(Constant.FORMAT_DAY_AGO, Locale.getDefault()).format(date);
    }

    public static Drawable getDrawableBackground(int i, Context context, View view, int iCount, ArrayList<Note> notes) {
        int i2 = i - 1;
        int i3 = i + 1;
        int itemCount = iCount;
        if (i2 >= 0) {
            Note note = (Note) notes.get(i);
            if (((Note) notes.get(i2)).isPinned()) {
                if (note.isPinned()) {
                    if (i3 >= itemCount) {
                        view.setVisibility(View.GONE);
                        return context.getResources().getDrawable(R.drawable.bg_note_item_round_bottom);
                    } else if (((Note) notes.get(i3)).isPinned()) {
                        view.setVisibility(View.VISIBLE);
                        return context.getResources().getDrawable(R.drawable.bg_note_item_middle);
                    } else {
                        view.setVisibility(View.GONE);
                        return context.getResources().getDrawable(R.drawable.bg_note_item_round_bottom);
                    }
                } else if (i3 >= itemCount) {
                    view.setVisibility(View.GONE);
                    return context.getResources().getDrawable(R.drawable.bg_note_item_rounded);
                } else {
                    view.setVisibility(View.VISIBLE);
                    return context.getResources().getDrawable(R.drawable.bg_note_item_round_top);
                }
            } else if (i3 >= itemCount) {
                view.setVisibility(View.GONE);
                return context.getResources().getDrawable(R.drawable.bg_note_item_round_bottom);
            } else {
                view.setVisibility(View.VISIBLE);
                return context.getResources().getDrawable(R.drawable.bg_note_item_middle);
            }
        } else if (i3 >= itemCount) {
            view.setVisibility(View.GONE);
            return context.getResources().getDrawable(R.drawable.bg_note_item_rounded);
        } else if (((Note) notes.get(i3)).isPinned()) {
            return context.getResources().getDrawable(R.drawable.bg_note_item_round_top);
        } else {
            if (!((Note) notes.get(i)).isPinned()) {
                return context.getResources().getDrawable(R.drawable.bg_note_item_round_top);
            }
            view.setVisibility(View.GONE);
            return context.getResources().getDrawable(R.drawable.bg_note_item_rounded);
        }
    }


    public static Bitmap getBitmap(byte[] bArr, int i) {
        if (!(bArr == null || bArr.length == 0)) {
            int i2 = 0;
            Bitmap decodeStream = BitmapFactory.decodeStream(new ByteArrayInputStream(bArr), (Rect) null, (BitmapFactory.Options) null);
            Matrix matrix = new Matrix();
            if (i == 3) {
                i2 = Constant.ROTATION_180;
            } else if (i == 6) {
                i2 = 90;
            } else if (i == 8) {
                i2 = Constant.ROTATION_270;
            }
            matrix.postRotate((float) i2);
            if (decodeStream != null) {
                return Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, true);
            }
        }
        return null;
    }

    public static boolean checkPer(Context context, String str) {
        return ActivityCompat.checkSelfPermission(context, str) == 0;
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight <= 0 || maxWidth <= 0) {
            return image;
        }
        float width = image.getWidth() / image.getHeight();
        float f = maxWidth;
        float f2 = maxHeight;
//        if (f / f2 > width) {
//            maxWidth = (int) (f2 * width);
//        } else {
//            maxHeight = (int) (f / width);
//        }
        System.out.println("------ width : " + image.getWidth() + " height - " + image.getHeight());

        return Bitmap.createScaledBitmap(image, maxWidth, maxHeight, true);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        InputMethodManager methodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            methodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float f = (float) i;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }
}
