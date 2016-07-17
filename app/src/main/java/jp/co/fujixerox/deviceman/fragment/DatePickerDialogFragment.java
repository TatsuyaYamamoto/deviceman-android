package jp.co.fujixerox.deviceman.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by TATSUYA-PC4 on 2016/07/03.
 */

public class DatePickerDialogFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {
    private static String TAG = DatePickerDialogFragment.class.getName();
    public static String ARGUMENT_KEY_YEAD  = "date";
    public static String ARGUMENT_KEY_MONTH = "month";
    public static String ARGUMENT_KEY_DAY   = "day";

    public DatePickerDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Log.d(TAG, "setted date -> " + year + "/" + monthOfYear + "/" + dayOfMonth);

        Intent intent = new Intent();
        intent.putExtra(ARGUMENT_KEY_YEAD, year);
        intent.putExtra(ARGUMENT_KEY_MONTH, monthOfYear);
        intent.putExtra(ARGUMENT_KEY_DAY, dayOfMonth);

        if (getTargetFragment() != null) {
            // ignore
        } else {
            PendingIntent pendingIntent = getActivity().createPendingResult(getTargetRequestCode(), intent, PendingIntent.FLAG_ONE_SHOT);
            Log.d(TAG, "created pending intent: " + pendingIntent.toString());
            try {
                pendingIntent.send(Activity.RESULT_OK);
            } catch (PendingIntent.CanceledException e) {
                // send fail
            }
        }
    }
}