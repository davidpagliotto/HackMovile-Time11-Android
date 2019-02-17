package com.dpagliotto.gimb.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.dpagliotto.gimb.R;

public class CustomButtonPrimary extends android.support.v7.widget.AppCompatButton {

    public CustomButtonPrimary(Context context) {
        super(context);
        init();
    }

    public CustomButtonPrimary(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonPrimary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");
        setTypeface(tf);

        setAllCaps(false);
        setBackgroundResource(R.drawable.bg_button_primary);
        setTextColor(getResources().getColor(R.color.colorTextPrimary));
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }
}
