package com.dpagliotto.paykids.components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.dpagliotto.paykids.R;

public class CustomToolBar extends android.support.v7.widget.Toolbar {

    CustomTextViewPAYKIDSTitle title;

    public CustomToolBar(Context context) {
        super(context);
        init(context);
    }

    public CustomToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 20, 0, 0);

        title = new CustomTextViewPAYKIDSTitle(context);
        addView(title);

        title.setLayoutParams(params);
        title.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
        title.setTextColor(getResources().getColor(R.color.colorTextPrimary));
        title.setTextSize(30);
        title.setTypeface(tf);
    }

    @Override
    public void setTitle(int resId) {
        String text = getResources().getString(resId);
        title.setText(text.toLowerCase());
    }

    @Override
    public void setTitle(CharSequence text) {
        if (text != null)
            title.setText(text.toString().toLowerCase());
    }
}
