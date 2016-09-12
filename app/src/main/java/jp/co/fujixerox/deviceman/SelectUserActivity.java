package jp.co.fujixerox.deviceman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import jp.co.fujixerox.deviceman.adapter.UserListAdapter;
import jp.co.fujixerox.deviceman.dto.User;
import jp.co.fujixerox.deviceman.dto.UserList;
import jp.co.fujixerox.deviceman.network.Apiclient;

public class SelectUserActivity extends AppCompatActivity {
    private static final String TAG = SelectUserActivity.class.getSimpleName();
    private static final int REQUEST_CODE_TO_SCAN_USER_ID = 9001;

    private View mView;

    /* dependency */
    private Apiclient mApiclient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        mApiclient = new Apiclient();

        requestAllUserInfoToSeriver();
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

    /*******************
     * API REQUEST
     *******************/

    /**
     * ユーザー情報を取得する
     *
     */
    private void requestAllUserInfoToSeriver() {
        // プログレスダイアログを表示する
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mApiclient.getAllUser(new Apiclient.ResponseListener<UserList>() {
            @Override
            public void onHttpSuccess(UserList users) {
                Log.d(TAG, "success to get users information.");
                Toast.makeText(getApplicationContext(), "ユーザーを選択して下さい", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


                UserListAdapter userListAdapter = new UserListAdapter(SelectUserActivity.this, users.getUsers());
                ListView userListView = (ListView) findViewById(R.id.list_user);
                userListView.setAdapter(userListAdapter);
                userListView.setOnItemClickListener(userListItemClickListener);
            }

            @Override
            public void onHttpError(int code, UserList users) {
                Toast.makeText(getApplicationContext(), "ユーザーリストの取得に失敗しました。管理者にお問い合わ下さい。", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                Log.d(TAG, "user information does not exist");
            }

            @Override
            public void failure() {
                Toast.makeText(getApplicationContext(), "通信失敗_(┐「ε:)_ユーザーリストの取得に失敗しました。管理者にお問い合わ下さい。", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                Log.d(TAG, "failure to get user information.");
            }
        });
    }


    AdapterView.OnItemClickListener userListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            // クリックされたアイテムを取得します
            User user = (User) listView.getItemAtPosition(position);

            Intent intent = new Intent(SelectUserActivity.this, SelectOperationActivity.class);
            intent.putExtra(SelectOperationActivity.EXTRA_KEY_USER, user);
            startActivity(intent);
            finish();
        }
    };
}
