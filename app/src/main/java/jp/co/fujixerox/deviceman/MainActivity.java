package jp.co.fujixerox.deviceman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import jp.co.fujixerox.deviceman.dto.User;
import jp.co.fujixerox.deviceman.network.Apiclient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_TO_SCAN_USER_ID = 9001;

    private View mView;

    /* dependency */
    private Apiclient mApiclient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mView = findViewById(R.id.main_scroll_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, QRScanActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_TO_SCAN_USER_ID);
                Intent intent = new Intent(MainActivity.this, SelectUserActivity.class);
                startActivity(intent);

            }
        });

        mApiclient = new Apiclient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_CANCELED){
            // ignore
            return;
        }

        if(resultCode == RESULT_OK && data != null) {

            switch (requestCode) {
                case REQUEST_CODE_TO_SCAN_USER_ID:
                    String userId = data.getStringExtra(QRScanActivity.DETECTED_CODE_VALUE);
                    Log.d(TAG, "detected barcode value -> : " + userId);

                    requestUserInformationToServer(userId);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }

        }else{
            // result failure
            Toast.makeText(getApplicationContext(), "結果を取得できなかた_(┐「ε:)_", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No barcode captured, intent data is null");
        }
    }


    /*******************
     * API REQUEST
     *******************/

    /**
     * ユーザー情報を取得する
     *
     * @param userId
     */
    private void requestUserInformationToServer(String userId) {
        // プログレスダイアログを表示する
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mApiclient.getUserById(userId, new Apiclient.ResponseListener<User>() {
            @Override
            public void onHttpSuccess(User user) {
                Log.d(TAG, "success to get user information. id: " + user.getId() + ", name: " + user.getName());
                Toast.makeText(getApplicationContext(), user.getId() + user.getName() + "さん、こんにちは(・８・)", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, SelectOperationActivity.class);
                intent.putExtra(SelectOperationActivity.EXTRA_KEY_USER, user);
                startActivity(intent);
                progressDialog.dismiss();
            }

            @Override
            public void onHttpError(int code, User body) {
                Toast.makeText(getApplicationContext(), "入力されたIDがサーバーに登録されていません。", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.d(TAG, "user information does not exist");
            }

            @Override
            public void failure() {
                Toast.makeText(getApplicationContext(), "通信失敗_(┐「ε:)_ユーザー情報を確認できませんでした。", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.d(TAG, "failure to get user information.");
            }
        });
    }
}
