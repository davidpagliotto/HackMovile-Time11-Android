package com.dpagliotto.paykids.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.dpagliotto.paykids.R;

public class CustomButtonSecondary extends android.support.v7.widget.AppCompatButton {

    public CustomButtonSecondary(Context context) {
        super(context);
        init();
    }

    public CustomButtonSecondary(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonSecondary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");
        setTypeface(tf);

        setAllCaps(false);
        setBackgroundResource(R.drawable.bg_button_secondary);
        setTextColor(getResources().getColor(R.color.colorTextSecondary));
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }
}
