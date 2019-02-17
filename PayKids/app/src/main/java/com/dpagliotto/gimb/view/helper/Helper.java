package com.dpagliotto.gimb.view.helper;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dpagliotto.gimb.R;
import com.dpagliotto.gimb.components.CustomTextView;
import com.dpagliotto.gimb.dto.CustomDialogButtonData;
import com.dpagliotto.gimb.enumerated.CustomDialogButtonTypeEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by davidpagliotto on 31/07/17.
 */

public final class Helper {

    public static final Integer OPT_LOGIN_FRAGMENT = 0;
    public static final Integer OPT_TITULAR_HOME_FRAGMENT = 1;
    public static final Integer OPT_DEPENDENTE_HOME_FRAGMENT = 2;

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_JSON_FORMAT = "yyyy-MM-dd";

    private static SimpleDateFormat dateFormat;

    public static String dateFormat(Date date) {
        Helper.dateFormat = new SimpleDateFormat(Helper.DATE_FORMAT, Locale.getDefault());
        return String.valueOf(Helper.dateFormat.format(date));
    }

    public static String nowDateFormat() {
        Helper.dateFormat = new SimpleDateFormat(Helper.DATE_FORMAT, Locale.getDefault());
        return String.valueOf(Helper.dateFormat.format(Calendar.getInstance().getTime()));
    }

    public static String nowDateJsonFormat() {
        Helper.dateFormat = new SimpleDateFormat(Helper.DATE_JSON_FORMAT, Locale.getDefault());
        return String.valueOf(Helper.dateFormat.format(Calendar.getInstance().getTime()));
    }

    public static String nowDateTimeFormat() {
        Helper.dateFormat = new SimpleDateFormat(Helper.DATE_TIME_FORMAT, Locale.getDefault());
        return String.valueOf(Helper.dateFormat.format(Calendar.getInstance().getTime()));
    }

    public static Date stringToDate(String string) {
        try {
            Helper.dateFormat = new SimpleDateFormat(Helper.DATE_TIME_FORMAT, Locale.getDefault());
            return Helper.dateFormat.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date stringToDate(String string, String format) {
        try {
            Helper.dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return Helper.dateFormat.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void customAlertDialog(final Context context, int resMessageDialog, CustomDialogButtonData button) {
        String message = context.getResources().getString(resMessageDialog);
        Helper.customAlertDialog(context, message, button);
    }

    public static void customAlertDialog(final Context context, String message, CustomDialogButtonData button) {
        ArrayList<CustomDialogButtonData> list = new ArrayList<>();
        list.add(button);
        Helper.customAlertDialog(context, message, list, null);
    }

    public static void customAlertDialog(final Context context, int resMessageDialog, List<CustomDialogButtonData> listButtons) {
        String message = context.getResources().getString(resMessageDialog);
        Helper.customAlertDialog(context, message, listButtons, null);
    }

    public static void customAlertDialog(final Context context, String message, List<CustomDialogButtonData> listButtons) {
        Helper.customAlertDialog(context, message, listButtons, null);
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

    public static CustomDialogButtonData positiveBtn(int resText, View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.POSITIVE, resText, listener);
    }

    public static CustomDialogButtonData neutralBtn(int resText, View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.NEUTRAL, resText, listener);
    }

    public static CustomDialogButtonData positiveBtnOK(View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.POSITIVE, android.R.string.ok, listener);
    }

    public static CustomDialogButtonData neutralBtnOK(View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.NEUTRAL, android.R.string.ok, listener);
    }

    public static CustomDialogButtonData positiveBtnConfirm(View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.POSITIVE, R.string.confirmar, listener);
    }

    public static CustomDialogButtonData neutralBtnCancel(View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.NEUTRAL, R.string.confirmar, listener);
    }

    public static CustomDialogButtonData positiveBtnYes(View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.POSITIVE, R.string.sim, listener);
    }

    public static CustomDialogButtonData neutralBtnNo(View.OnClickListener listener) {
        return new CustomDialogButtonData(CustomDialogButtonTypeEnum.NEUTRAL, R.string.nao, listener);
    }

    public static void timerPickerDialog (Context context, TimePickerDialog.OnTimeSetListener timeSetListener) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        Helper.timerPickerDialog(context, timeSetListener, hour, minute);
    }

    public static void timerPickerDialog (Context context, TimePickerDialog.OnTimeSetListener timeSetListener, int hour, int minute) {
        if (hour == 0 && minute == 0) {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        TimePickerDialog timePicker = new TimePickerDialog(context, timeSetListener, hour, minute, true);
        timePicker.show();
    }

}
