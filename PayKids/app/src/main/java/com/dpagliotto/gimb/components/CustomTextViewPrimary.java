package com.dpagliotto.gimb.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.dpagliotto.gimb.R;

public class CustomTextViewPrimary extends android.support.v7.widget.AppCompatTextView {

    public CustomTextViewPrimary(Context context) {
        super(context);
        init();
    }

    public CustomTextViewPrimary(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewPrimary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");
            setTypeface(tf);

            setTextColor(getResources().getColor(R.color.colorTextPrimary));
        }
    }
}
