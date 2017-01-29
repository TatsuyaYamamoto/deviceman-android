package jp.co.fujixerox.deviceman.presenter.activity;

import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.fujixerox.deviceman.App;
import jp.co.fujixerox.deviceman.R;
import jp.co.fujixerox.deviceman.di.components.ActivityComponent;
import jp.co.fujixerox.deviceman.di.components.AppComponent;
import jp.co.fujixerox.deviceman.di.modules.ActivityModule;
import jp.co.fujixerox.deviceman.presenter.adapter.TutorialPagerAdapter;
import jp.co.fujixerox.deviceman.dto.User;
import jp.co.fujixerox.deviceman.dto.UserList;
import jp.co.fujixerox.deviceman.presenter.fragment.SelectUserDialogFragment;
import jp.co.fujixerox.deviceman.network.Apiclient;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_TO_SCAN_USER_ID = 9001;
    private static final int REQUEST_CODE_TO_SELECT_USER_BY_LIST = 9002;

    @Inject
    Apiclient mApiclient;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    TextView titleTextView;

    @BindView(R.id.viewpager_tutorial)
    ViewPager viewPager_tutorial;

    @BindView(R.id.pageIndicator)
    PageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        getActivityComponent().inject(this);

        /* toolbar ラベルフォントの設定 */
        toolbar.setTitle("");
        titleTextView.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/amemuchigothic-h.ttf"));
        setSupportActionBar(toolbar);

        Button startApplicationButton = (Button) findViewById(R.id.button_start_application);
        startApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserSelectDialog();
            }
        });

        /* チュートリアル用Pager */
        FragmentManager tutorialPagerFragmentManager = getSupportFragmentManager();
        TutorialPagerAdapter adapter = new TutorialPagerAdapter(tutorialPagerFragmentManager);
        viewPager_tutorial.setAdapter(adapter);
        indicator.setViewPager(viewPager_tutorial);
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

        if (resultCode == RESULT_CANCELED) {
            // ignore
            return;
        }

        if (resultCode == RESULT_OK && data != null) {

            switch (requestCode) {
                case REQUEST_CODE_TO_SCAN_USER_ID:
                    String userId = data.getStringExtra(QRScanActivity.DETECTED_CODE_VALUE);
                    Log.d(TAG, "detected barcode value -> : " + userId);

                    requestUserInformationToServer(userId);
                    break;

                case REQUEST_CODE_TO_SELECT_USER_BY_LIST:
                    User selectedUser = (User) data.getSerializableExtra(SelectUserDialogFragment.EXTRA_KEY_USER);

                    goNextActivity(selectedUser);
                    break;

                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }

        } else {
            // result failure
            Log.d(TAG, "Activity Result is failed. result code is ok, but it doesn't have a data.");
        }
    }

    private void goNextActivity(User selectedUser) {
        startActivity(SelectOperationActivity.getCallingIntent(this, selectedUser));
        overridePendingTransition(R.anim.in_right, R.anim.out_left);
    }

    /*******************
     * API REQUEST
     *******************/

    private void openUserSelectDialog() {
        // プログレスダイアログを表示する
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.dialog_message_default));
        progressDialog.show();

        mApiclient.getAllUser(new Apiclient.ResponseListener<UserList>() {
            @Override
            public void onHttpSuccess(UserList users) {
                Log.d(TAG, "success to get users information.");
                progressDialog.dismiss();

                Bundle args = new Bundle();
                args.putSerializable("users", (Serializable) users.getUsers());

                SelectUserDialogFragment dialogFragment = new SelectUserDialogFragment();
                dialogFragment.setTargetFragment(null, REQUEST_CODE_TO_SELECT_USER_BY_LIST);
                dialogFragment.setArguments(args);
                dialogFragment.show(getSupportFragmentManager(), SelectUserDialogFragment.class.getName());
            }

            @Override
            public void onHttpError(int code, UserList users) {
                showToastMessage("ユーザーリストの取得に失敗しました。管理者にお問い合わ下さい。");
                progressDialog.dismiss();
                finish();
                Log.d(TAG, "user information does not exist");
            }

            @Override
            public void failure() {
                showToastMessage("通信失敗_(┐「ε:)_ユーザーリストの取得に失敗しました。管理者にお問い合わ下さい。");
                progressDialog.dismiss();
                finish();
                Log.d(TAG, "failure to get user information.");
            }
        });
    }


    /**
     * ユーザー情報を取得する
     *
     * @param userId
     */
    private void requestUserInformationToServer(String userId) {
        // プログレスダイアログを表示する
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.dialog_message_default));
        progressDialog.show();

        mApiclient.getUserById(userId, new Apiclient.ResponseListener<User>() {
            @Override
            public void onHttpSuccess(User user) {
                Log.d(TAG, "success to get user information. id: " + user.getId() + ", name: " + user.getName());
                showToastMessage(user.getId() + user.getName() + "さん、こんにちは(・８・)");

                progressDialog.dismiss();
                goNextActivity(user);
            }

            @Override
            public void onHttpError(int code, User body) {
                showToastMessage("入力されたIDがサーバーに登録されていません。");
                progressDialog.dismiss();
                Log.d(TAG, "user information does not exist");
            }

            @Override
            public void failure() {
                showToastMessage("通信失敗_(┐「ε:)_ユーザー情報を確認できませんでした。");
                progressDialog.dismiss();
                Log.d(TAG, "failure to get user information.");
            }
        });
    }
}
