package com.dpagliotto.paykids.dto;

import android.view.View;

import com.dpagliotto.paykids.enumerated.CustomDialogButtonTypeEnum;

public class CustomDialogButtonData {

    private CustomDialogButtonTypeEnum typeEnum;

    private int resButtonText;

    private View.OnClickListener buttonClickListener;

    public CustomDialogButtonData(CustomDialogButtonTypeEnum typeEnum, int resButtonText) {
        this.typeEnum = typeEnum;
        this.resButtonText = resButtonText;
    }

    public CustomDialogButtonData(CustomDialogButtonTypeEnum typeEnum, int resButtonText, View.OnClickListener buttonClickListener) {
        this.typeEnum = typeEnum;
        this.resButtonText = resButtonText;
        this.buttonClickListener = buttonClickListener;
    }

    public CustomDialogButtonTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public int getResButtonText() {
        return resButtonText;
    }

    public View.OnClickListener getButtonClickListener() {
        return buttonClickListener;
    }
}
