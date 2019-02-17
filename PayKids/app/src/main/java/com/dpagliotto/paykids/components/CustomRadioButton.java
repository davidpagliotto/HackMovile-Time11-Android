package com.dpagliotto.paykids.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    public CustomRadioButton(Context context) {
        super(context);
        init();
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");
        setTypeface(tf);

        setAllCaps(false);
    }
}
