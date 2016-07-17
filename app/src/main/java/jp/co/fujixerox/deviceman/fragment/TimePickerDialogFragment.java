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

public class TimePickerDialogFragment extends AppCompatDialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static String TAG = TimePickerDialogFragment.class.getName();
    public static String ARGUMENT_KEY_HOUR = "hour";
    public static String ARGUMENT_KEY_MINUTE = "minute";

    public TimePickerDialogFragment(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d(TAG, "setted time -> " +  hourOfDay + ":" + minute);

        Intent intent = new Intent();
        intent.putExtra(ARGUMENT_KEY_HOUR, hourOfDay);
        intent.putExtra(ARGUMENT_KEY_MINUTE, minute);

        if(getTargetFragment() != null){
            // ignore
        }else{
            PendingIntent pendingIntent = getActivity().createPendingResult(getTargetRequestCode(),intent, PendingIntent.FLAG_ONE_SHOT);
            Log.d(TAG, "created pending intent: " + pendingIntent.toString());
            try{
                pendingIntent.send(Activity.RESULT_OK);
            }catch(PendingIntent.CanceledException e){
                // send fail
            }
        }
    }
}
