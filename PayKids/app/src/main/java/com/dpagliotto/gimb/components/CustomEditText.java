package com.dpagliotto.gimb.components;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.dpagliotto.gimb.R;

import java.util.ArrayList;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    private ArrayList<TextWatcher> listeners;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        listeners = new ArrayList<>();

        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Bold.ttf");
            setTypeface(tf);

            setTextColor(getResources().getColor(R.color.colorTextEdit));
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            setHintTextColor(getResources().getColor(R.color.colorTextEditHint));
        }
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        listeners.add(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        super.removeTextChangedListener(watcher);
        listeners.remove(watcher);
    }

    public void clearTextChangedListener() {
        for (TextWatcher watcher: listeners)
            removeTextChangedListener(watcher);

        listeners.clear();
    }

}
