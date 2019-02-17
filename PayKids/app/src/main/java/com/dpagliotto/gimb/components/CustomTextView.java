package com.dpagliotto.gimb.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.dpagliotto.gimb.R;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");
        setTypeface(tf);

        setTextColor(getResources().getColor(R.color.colorTextEdit));
    }
}
