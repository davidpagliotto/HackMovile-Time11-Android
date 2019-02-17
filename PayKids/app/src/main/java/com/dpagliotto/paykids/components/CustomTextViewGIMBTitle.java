package com.dpagliotto.paykids.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.dpagliotto.paykids.R;

public class CustomTextViewPAYKIDSTitle extends android.support.v7.widget.AppCompatTextView {

    public CustomTextViewPAYKIDSTitle(Context context) {
        super(context);
        init();
    }

    public CustomTextViewPAYKIDSTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewPAYKIDSTitle(Context context, AttributeSet attrs, int defStyleAttr) {
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
