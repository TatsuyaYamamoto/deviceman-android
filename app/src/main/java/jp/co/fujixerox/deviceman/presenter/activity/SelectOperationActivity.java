package jp.co.fujixerox.deviceman.presenter.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.co.fujixerox.deviceman.R;
import jp.co.fujixerox.deviceman.dto.Device;
import jp.co.fujixerox.deviceman.dto.User;
import jp.co.fujixerox.deviceman.network.Apiclient;

public class SelectOperationActivity extends BaseActivity {
    private static final String TAG = SelectOperationActivity.class.getName();

    private static final int REQUEST_CODE_TO_SCAN_CHECKOUT_DEVICE_ID = 9002;
    private static final int REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID = 9003;

    private static final String INTENT_EXTRA_PARAM_USER = "user";
    private static final String INSTANCE_STATE_PARAM_USER_ID = "org.android10.STATE_PARAM_USER_ID";

    private User mUser;
    private Unbinder mUnbinder;

    /* dependency */
    private Apiclient mApiclient;


    @BindView(R.id.text_user_id)
    TextView userIdText;

    @BindView(R.id.button_lending)
    Button lendButton;

    @BindView(R.id.button_return)
    Button returnButton;

    @BindView(R.id.button_finish)
    Button finishButton;

    /**
     * Goes to the user list screen.
     */
    @OnClick(R.id.button_lending)
    void handleClickLendingButton() {
        Intent intent = new Intent(this, QRScanActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TO_SCAN_CHECKOUT_DEVICE_ID);
    }

    @OnClick(R.id.button_return)
    void handleClick() {
        Intent intent = new Intent(this, QRScanActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID);
    }

    @OnClick(R.id.button_finish)
    void handleClickFisish() {
        finish();
    }


    public static Intent getCallingIntent(Context context, User user) {
        Intent callingIntent = new Intent(context, SelectOperationActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_USER, user);
        return callingIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_operation);
        ButterKnife.bind(this);

        mApiclient = new Apiclient();

        if (savedInstanceState == null) {
            mUser = (User) getIntent().getSerializableExtra(INTENT_EXTRA_PARAM_USER);
        } else {
            mUser = (User) savedInstanceState.getSerializable(INSTANCE_STATE_PARAM_USER_ID);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putSerializable(INSTANCE_STATE_PARAM_USER_ID, mUser);
        }
        super.onSaveInstanceState(outState);
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
                case REQUEST_CODE_TO_SCAN_CHECKOUT_DEVICE_ID:
                    /* 貸出要求する端末のIDをスキャンしたとき */
                    Log.d(TAG, "onActivityResult. case REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID");
                    String detectedCheckoutDeviceId = data.getStringExtra(QRScanActivity.DETECTED_CODE_VALUE);

                    checkIdAndGoToNextActivity(detectedCheckoutDeviceId);
                    break;

                case REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID:
                    /* 返却する端末のIDをスキャンしたとき */
                    Log.d(TAG, "onActivityResult. case REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID");

                    String detectedReturnDeviceId = data.getStringExtra(QRScanActivity.DETECTED_CODE_VALUE);
                    requestReturnDeviceToServer(detectedReturnDeviceId, mUser.getId());
                    break;
            }
        }
    }

    /*******************
     * API REQUEST
     *******************/


    /**
     * 端末借り出しリスナ
     *
     * @param deviceId
     */
    private void checkIdAndGoToNextActivity(final String deviceId) {

        /* 端末情報を取得する */
        mApiclient.getDeviceId(deviceId, new Apiclient.ResponseListener<Device>() {
            @Override
            public void onHttpSuccess(Device device) {
                Intent nextIntent = new Intent(SelectOperationActivity.this, CheckoutSummaryActivity.class);
                nextIntent.putExtra(CheckoutSummaryActivity.EXTRA_KEY_USER, mUser);
                nextIntent.putExtra(CheckoutSummaryActivity.EXTRA_KEY_DEVICE, device);
                startActivity(nextIntent);
            }

            public void onHttpError(int code, Device device) {
                showToastMessage(R.string.error_invalid_id);
            }

            @Override
            public void failure() {
                Log.d(TAG, "No barcode captured, intent data is null");
                showToastMessage(R.string.error_connection);
            }
        });
    }


    /**
     * 返却処理リスナ
     *
     * @param deviceId
     */
    private void requestReturnDeviceToServer(final String deviceId, final String userId) {
        /* 端末情報を取得する */
        mApiclient.getDeviceId(deviceId, new Apiclient.ResponseListener<Device>() {
            @Override
            public void onHttpSuccess(Device device) {
                /* 確認ダイアログ */
                new AlertDialog.Builder(SelectOperationActivity.this)
                        .setTitle("確認")
                        .setMessage(device.getName() + "を返却しますか？")
                        .setPositiveButton("OK", onClickListener)
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onHttpError(int code, Device device) {
                showToastMessage(R.string.error_invalid_id);
            }

            @Override
            public void failure() {
                Log.d(TAG, "No barcode captured, intent data is null");
                showToastMessage(R.string.error_connection);
            }

            private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mApiclient.returnDevice(deviceId, userId, new Apiclient.ResponseListener<Void>() {
                        @Override
                        public void onHttpSuccess(Void _void) {
                            showToastMessage(R.string.success_return);
                        }

                        public void onHttpError(int code, Void _void) {
                            switch (code) {
                                case 400:
                                    showToastMessage(R.string.error_return_not_checked);
                                    break;
                                case 404:
                                    showToastMessage(R.string.error_invalid_id);
                                    break;
                                default:
                                    showToastMessage(R.string.error_connection);
                                    break;
                            }
                        }

                        @Override
                        public void failure() {
                            Log.d(TAG, "No barcode captured, intent data is null");
                            showToastMessage(R.string.error_connection);

                        }
                    });
                }
            };
        });
    }
}