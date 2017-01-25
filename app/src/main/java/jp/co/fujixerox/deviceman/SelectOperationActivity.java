package jp.co.fujixerox.deviceman;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import jp.co.fujixerox.deviceman.dto.Device;
import jp.co.fujixerox.deviceman.dto.User;
import jp.co.fujixerox.deviceman.network.Apiclient;

/**
 * Created by TATSUYA-PC4 on 2016/07/03.
 */

public class SelectOperationActivity extends AppCompatActivity {
    private static final String TAG = SelectOperationActivity.class.getName();

    private static final int REQUEST_CODE_TO_SCAN_CHECKOUT_DEVICE_ID = 9002;
    private static final int REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID = 9003;

    public static String EXTRA_KEY_USER = "user";

    private User mUser;

    /* dependency */
    private Apiclient mApiclient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_operation);

        mApiclient = new Apiclient();
        mUser = (User) getIntent().getSerializableExtra(EXTRA_KEY_USER);
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

    private void setupView() {
        TextView userIdText = (TextView) findViewById(R.id.text_user_id);
        userIdText.setText(mUser.getName());

        Button lendButton = (Button) findViewById(R.id.button_lending);
        lendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectOperationActivity.this, QRScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TO_SCAN_CHECKOUT_DEVICE_ID);
            }
        });


        Button returnButton = (Button) findViewById(R.id.button_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectOperationActivity.this, QRScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TO_SCAN_RETURN_DEVICE_ID);
            }
        });

        Button finishButton = (Button) findViewById(R.id.button_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(getApplicationContext(), R.string.error_invalid_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure() {
                // result failure
                Toast.makeText(getApplicationContext(), R.string.error_connection, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No barcode captured, intent data is null");
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
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /* 返却処理実行 */
                                mApiclient.returnDevice(deviceId, userId, new Apiclient.ResponseListener<Void>() {
                                    @Override
                                    public void onHttpSuccess(Void _void) {
                                        Toast.makeText(getApplicationContext(), R.string.success_return, Toast.LENGTH_SHORT).show();
                                    }

                                    public void onHttpError(int code, Void _void) {
                                        switch (code) {
                                            case 400:
                                                Toast.makeText(getApplicationContext(), R.string.error_return_not_checked, Toast.LENGTH_SHORT).show();
                                                break;
                                            case 404:
                                                Toast.makeText(getApplicationContext(), R.string.error_invalid_id, Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                Toast.makeText(getApplicationContext(), R.string.error_connection, Toast.LENGTH_SHORT).show();
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
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            public void onHttpError(int code, Device device) {
                Toast.makeText(getApplicationContext(), R.string.error_invalid_id, Toast.LENGTH_SHORT).show();
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