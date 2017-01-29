package jp.co.fujixerox.deviceman.presenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.fujixerox.deviceman.R;
import jp.co.fujixerox.deviceman.service.dto.Device;
import jp.co.fujixerox.deviceman.service.dto.User;
import jp.co.fujixerox.deviceman.presenter.fragment.DatePickerDialogFragment;
import jp.co.fujixerox.deviceman.presenter.fragment.TimePickerDialogFragment;
import jp.co.fujixerox.deviceman.service.network.Apiclient;

public class CheckoutSummaryActivity extends BaseActivity {
    private static final String TAG = CheckoutSummaryActivity.class.getName();
    private static final int REQUEST_CODE_TO_GET_DATE_PICKER = 9001;
    private static final int REQUEST_CODE_TO_GET_TIME_PICKER = 9002;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_TIME = new SimpleDateFormat("HH:mm");

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

    @Inject
    Apiclient mApiclient;


    @BindView(R.id.text_checkout_device_name_value)
    TextView textDeviceName;

    @BindView(R.id.text_checkout_user_id_value)
    TextView textUserId;

    @BindView(R.id.text_checkout_user_name_value)
    TextView textUserName;

    @BindView(R.id.text_checkout_return_date_value)
    TextView textReturnDate;

    @BindView(R.id.text_checkout_return_time_value)
    TextView textReturnTime;

    @BindView(R.id.layout_checkout_return_date)
    FrameLayout layoutReturnDate;

    @OnClick(R.id.layout_checkout_return_date)
    void handleCliclkReturnDate() {
        /* 返却日の更新 */
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.setTargetFragment(null, REQUEST_CODE_TO_GET_DATE_PICKER);
        datePickerDialogFragment.show(getSupportFragmentManager(), DatePickerDialogFragment.class.getName());
    }

    @BindView(R.id.layout_checkout_return_time)
    FrameLayout layoutReturnTime;

    @OnClick(R.id.layout_checkout_return_time)
    void handleClickReturnTime() {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.setTargetFragment(null, REQUEST_CODE_TO_GET_DATE_PICKER);
        datePickerDialogFragment.show(getSupportFragmentManager(), DatePickerDialogFragment.class.getName());

    }

    @BindView(R.id.button_checkout_submit)
    Button submitButton;

    @OnClick(R.id.button_checkout_submit)
    void handleClickSubmit() {
        String deviceId = mDevice.getId();
        String userId = mUser.getId();
        long returnDate = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute).getTimeInMillis();

        requestCheckingOutDeviceToServer(deviceId, userId, returnDate);
    }

    @BindView(R.id.button_checkout_cancel)
    Button cancelButton;

    @OnClick(R.id.button_checkout_cancel)
    void handleClickCancel() {
        finish();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_summary);

        ButterKnife.bind(this);
        getActivityComponent().inject(this);


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
                    textReturnDate.setText(SIMPLE_DATE_FORMAT_DATE.format(date.getTime()));
                    break;
                case REQUEST_CODE_TO_GET_TIME_PICKER:
                    mHour = data.getIntExtra(TimePickerDialogFragment.ARGUMENT_KEY_HOUR, mHour);
                    mMinute = data.getIntExtra(TimePickerDialogFragment.ARGUMENT_KEY_MINUTE, mMinute);

                    // list表示更新
                    GregorianCalendar time = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
                    textReturnTime.setText(SIMPLE_DATE_FORMAT_TIME.format(time.getTime()));
                    break;
            }
        }
    }

    private void setupView() {
        GregorianCalendar cal = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);

        // 端末名
        textDeviceName.setText(mDevice.getName());
        // ユーザーID
        textUserId.setText(mUser.getId());
        // ユーザー名
        textUserName.setText(mUser.getName());
        // 返却予定日(初期値)
        textReturnDate.setText(SIMPLE_DATE_FORMAT_DATE.format(cal.getTime()));
        // 返却予定時刻(初期値)
        textReturnTime.setText(SIMPLE_DATE_FORMAT_TIME.format(cal.getTime()));
    }

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
