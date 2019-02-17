package com.dpagliotto.gimb.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.dpagliotto.gimb.R;

public class CustomTextViewGIMBTitle extends android.support.v7.widget.AppCompatTextView {

    public CustomTextViewGIMBTitle(Context context) {
        super(context);
        init();
    }

    public CustomTextViewGIMBTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewGIMBTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");
        setTypeface(tf);

        setTextColor(getResources().getColor(R.color.colorTextPrimary));
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
}
