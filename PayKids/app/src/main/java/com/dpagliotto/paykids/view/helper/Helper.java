package com.dpagliotto.paykids.view.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dpagliotto.paykids.R;
import com.dpagliotto.paykids.components.CustomTextView;
import com.dpagliotto.paykids.dto.CustomDialogButtonData;
import com.dpagliotto.paykids.enumerated.CustomDialogButtonTypeEnum;

import java.util.ArrayList;
import java.util.List;

public final class Helper {

    public static final Integer OPT_LOGIN_FRAGMENT = 0;
    public static final Integer OPT_TITULAR_HOME_FRAGMENT = 1;
    public static final Integer OPT_DEPENDENTE_HOME_FRAGMENT = 2;

    public static void customAlertDialog(final Context context, String message, CustomDialogButtonData button) {
        ArrayList<CustomDialogButtonData> list = new ArrayList<>();
        list.add(button);
        Helper.customAlertDialog(context, message, list, null);
    }

    public static void customAlertDialog(final Context context, String message, List<CustomDialogButtonData> listButtons, View customView) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.dialog_message, null);

        CustomTextView txtMessage = view.findViewById(R.id.txtDialogMessage);
        txtMessage.setText(message);

        if (customView != null) {
            LinearLayout layout = view.findViewById(R.id.vwDialogCustomView);
            layout.addView(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setVisibility(View.VISIBLE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.setView(view)
                .create();

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        for (final CustomDialogButtonData data : listButtons) {
            View child = inflater.inflate(R.layout.dialog_message_button, null);
            ((LinearLayout) view).addView(child);

            LinearLayout container = child.findViewById(R.id.containerDialogImgButton);
            if (data.getTypeEnum().equals(CustomDialogButtonTypeEnum.NEUTRAL))
                container.setBackground(context.getResources().getDrawable(R.drawable.bg_dialog_button_neutral));
            else if (data.getTypeEnum().equals(CustomDialogButtonTypeEnum.POSITIVE))
                container.setBackground(context.getResources().getDrawable(R.drawable.bg_dialog_button_positive));

            final Button button = child.findViewById(R.id.imgBtnButton);
            if (data.getResButtonText() > 0)
                button.setText(data.getResButtonText());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    if (data.getButtonClickListener() != null) {
                        new Handler(context.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    data.getButtonClickListener().onClick(button);
                                }
                            }
                        );
                    }
                }
            });
        }

        alert.show();
    }

    public static CustomDialogButtonData positiveBtnOK(View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.POSITIVE, android.R.string.ok, listener);
    }

}
