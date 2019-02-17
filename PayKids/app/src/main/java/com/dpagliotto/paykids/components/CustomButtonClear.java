package com.dpagliotto.paykids.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomButtonClear extends android.support.v7.widget.AppCompatButton {

    public CustomButtonClear(Context context) {
        super(context);
        init();
    }

    public CustomButtonClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonClear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");
        setTypeface(tf);

        setAllCaps(false);
    }
}
