package com.zql.filepickerlib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;

/**
 * A subclass of ImageView that assumes to be fixed size
 * (not wrap_content / match_parent). Doing so it can
 * optimize the drawable change code paths.
 */
public class FixedSizeImageView extends androidx.appcompat.widget.AppCompatImageView {
    private boolean mSuppressLayoutRequest;

    public FixedSizeImageView(Context context) {
        super(context);
    }

    public FixedSizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedSizeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageResource(int resId) {
        mSuppressLayoutRequest = true;
        super.setImageResource(resId);
        mSuppressLayoutRequest = false;
    }

    public void setImageURI(Uri uri) {
        mSuppressLayoutRequest = true;
        super.setImageURI(uri);
        mSuppressLayoutRequest = false;
    }

    public void setImageDrawable(Drawable drawable) {
        mSuppressLayoutRequest = true;
        super.setImageDrawable(drawable);
        mSuppressLayoutRequest = false;
    }

    public void requestLayout() {
        if (!mSuppressLayoutRequest) {
            super.requestLayout();
        }
    }
}
