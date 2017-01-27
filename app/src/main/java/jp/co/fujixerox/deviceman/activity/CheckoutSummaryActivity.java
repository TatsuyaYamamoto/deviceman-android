package jp.co.fujixerox.deviceman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import jp.co.fujixerox.deviceman.R;
import jp.co.fujixerox.deviceman.dto.Device;
import jp.co.fujixerox.deviceman.dto.User;
import jp.co.fujixerox.deviceman.fragment.DatePickerDialogFragment;
import jp.co.fujixerox.deviceman.fragment.TimePickerDialogFragment;
import jp.co.fujixerox.deviceman.network.Apiclient;

public class CheckoutSummaryActivity extends AppCompatActivity {
    private static final String TAG = CheckoutSummaryActivity.class.getName();
    private static final int REQUEST_CODE_TO_GET_DATE_PICKER = 9001;
    private static final int REQUEST_CODE_TO_GET_TIME_PICKER = 9002;

    public static final String EXTRA_KEY_USER = "user";
    public static final String EXTRA_KEY_DEVICE = "device";

    private User mUser;
    private Device mDevice;

    private GregorianCalendar current = new GregorianCalendar();
    private int mYear = current.get(Calendar.YEAR);
    private int mMonth = current.get(Calendar.MONTH);
    private int mDay = current.get(Calendar.DAY_OF_MONTH) + 1;   // 今日の1日後をデフォルトの返却日にする
    private int mHour = current.get(Calendar.HOUR_OF_DAY);
    private int mMinute = current.get(Calendar.MINUTE);
    ;

    /* dependency */
    private Apiclient mApiclient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_summary);

        mApiclient = new Apiclient();

        mUser = (User) getIntent().getSerializableExtra(EXTRA_KEY_USER);
        mDevice = (Device) getIntent().getSerializableExtra(EXTRA_KEY_DEVICE);
        setupView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            // ignore
            return;
        }

        if (resultCode == RESULT_OK && data != null) {

            switch (requestCode) {
                case REQUEST_CODE_TO_GET_DATE_PICKER:
                    mYear = data.getIntExtra(DatePickerDialogFragment.ARGUMENT_KEY_YEAD, mYear);
                    mMonth = data.getIntExtra(DatePickerDialogFragment.ARGUMENT_KEY_MONTH, mMonth);
                    mDay = data.getIntExtra(DatePickerDialogFragment.ARGUMENT_KEY_DAY, mDay);

                    // list表示更新
                    GregorianCalendar date = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
                    ((TextView) findViewById(R.id.text_checkout_return_date_value)).setText(sdfDate.format(date.getTime()));
                    break;
                case REQUEST_CODE_TO_GET_TIME_PICKER:
                    mHour = data.getIntExtra(TimePickerDialogFragment.ARGUMENT_KEY_HOUR, mHour);
                    mMinute = data.getIntExtra(TimePickerDialogFragment.ARGUMENT_KEY_MINUTE, mMinute);

                    // list表示更新
                    GregorianCalendar time = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    ((TextView) findViewById(R.id.text_checkout_return_time_value)).setText(sdfTime.format(time.getTime()));
                    break;
            }
        }
    }

    private void setupView() {
        GregorianCalendar cal = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

        // 端末名
        TextView textDeviceName = (TextView) findViewById(R.id.text_checkout_device_name_value);
        textDeviceName.setText(mDevice.getName());
        // ユーザーID
        TextView textUserId = (TextView) findViewById(R.id.text_checkout_user_id_value);
        textUserId.setText(mUser.getId());
        // ユーザー名
        TextView textUserName = (TextView) findViewById(R.id.text_checkout_user_name_value);
        textUserName.setText(mUser.getName());
        // 返却予定日(初期値)
        TextView textReturnDate = (TextView) findViewById(R.id.text_checkout_return_date_value);
        textReturnDate.setText(sdfDate.format(cal.getTime()));

        FrameLayout layoutReturnDate = (FrameLayout) findViewById(R.id.text_checkout_return_date);
        layoutReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 返却日の更新 */
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.setTargetFragment(null, REQUEST_CODE_TO_GET_DATE_PICKER);
                datePickerDialogFragment.show(getSupportFragmentManager(), DatePickerDialogFragment.class.getName());
            }
        });
        // 返却予定時刻(初期値)
        TextView textReturnTime = (TextView) findViewById(R.id.text_checkout_return_time_value);
        textReturnTime.setText(sdfTime.format(cal.getTime()));
        FrameLayout layoutReturnTime = (FrameLayout) findViewById(R.id.text_checkout_return_time);
        layoutReturnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 返却時間の更新 */
                TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
                timePickerDialogFragment.setTargetFragment(null, REQUEST_CODE_TO_GET_TIME_PICKER);
                timePickerDialogFragment.show(getSupportFragmentManager(), TimePickerDialogFragment.class.getName());
            }
        });

        Button submitButton = (Button) findViewById(R.id.button_checkout_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceId = mDevice.getId();
                String userId = mUser.getId();
                long returnDate = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute).getTimeInMillis();

                requestCheckingOutDeviceToServer(deviceId, userId, returnDate);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.button_checkout_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /*******************
     * API REQUEST
     *******************/


    /**
     * 端末借り出しリスナ
     *
     * @param deviceId
     */
    private void requestCheckingOutDeviceToServer(final String deviceId, final String userId, final long returnTime) {
        mApiclient.checkout(deviceId, userId, returnTime, new Apiclient.ResponseListener<Void>() {
            @Override
            public void onHttpSuccess(Void _void) {
                finish();
                Toast.makeText(getApplicationContext(), R.string.success_checkout, Toast.LENGTH_SHORT).show();
            }

            public void onHttpError(int code, Void _void) {
                switch (code) {
                    case 400:
                        Toast.makeText(getApplicationContext(), R.string.error_checkout_later, Toast.LENGTH_SHORT).show();
                        break;
                    case 404:
                        Toast.makeText(getApplicationContext(), R.string.error_invalid_id, Toast.LENGTH_SHORT).show();
                        break;
                    case 409:
                        Toast.makeText(getApplicationContext(), R.string.error_checkout_confilt, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void failure() {
                // result failure
                Toast.makeText(getApplicationContext(), R.string.error_connection, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No barcode captured, intent data is null");
            }
        });
    }
}
