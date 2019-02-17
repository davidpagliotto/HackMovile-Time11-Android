package com.dpagliotto.paykids.view.helper;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class MaskWatcher implements TextWatcher {

    private boolean isRunning = false;
    private boolean isDeleting = false;
    private boolean isMonetary = false;
    private final String mask;

    public MaskWatcher(String mask, EditText text) {
        this.mask = mask;
        this.isMonetary = false;
        text.setFilters(new InputFilter[] {new InputFilter.LengthFilter(mask.length())});
    }

    public MaskWatcher(String mask, EditText text, boolean isMonetary) {
        this.mask = mask;
        this.isMonetary = isMonetary;
        text.setFilters(new InputFilter[] {new InputFilter.LengthFilter(mask.length())});
    }

    public static MaskWatcher buildCpf(EditText text) {
        return new MaskWatcher("###.###.###-##", text);
    }

    public static MaskWatcher buildTelefone(EditText text) {
        return new MaskWatcher("(##)#####-####", text);
    }

    public static MaskWatcher buildMonetario(EditText text) {
        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL)
                    monetaryMask(((EditText) v).getText());

                return false;
            }
        });

        return new MaskWatcher("$ ############.##", text, true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        isDeleting = count > after;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isRunning || isDeleting || editable.length() == 0) {
            return;
        }
        isRunning = true;

        if (isMonetary)
            monetaryMask(editable);
        else
            othersMasks(editable);

        isRunning = false;
    }

    private static void monetaryMask(Editable editable) {
        int idx = editable.toString().indexOf(".");
        if (idx > 0) {
            editable.replace(idx, idx+1, "");
        }

        int editableLength = editable.length();
        if (editableLength < 3)
            editable.insert(0, String.format("%" + (3-editableLength) + "s", "0").replace(' ', '0'));

        editable.insert(editable.length() - 2, ".");

        String[] arr = editable.toString().split("\\.");
        if (arr[0].startsWith("00")) {
            editable.replace(0, 1, "");
        }
        else if (arr[0].startsWith("0") && Integer.parseInt(arr[0]) > 0) {
            editable.replace(0, 1, "");
        }
    }

    private void othersMasks(Editable editable) {
        int editableLength = editable.length();
        if (editableLength < mask.length()) {
            if (mask.charAt(editableLength) != '#') {
                editable.append(mask.charAt(editableLength));
            } else if (mask.charAt(editableLength-1) != '#') {
                editable.insert(editableLength-1, mask, editableLength-1, editableLength);
            }
        }
    }


}