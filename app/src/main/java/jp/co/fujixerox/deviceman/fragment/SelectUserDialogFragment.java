package jp.co.fujixerox.deviceman.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import jp.co.fujixerox.deviceman.R;
import jp.co.fujixerox.deviceman.adapter.UserListAdapter;
import jp.co.fujixerox.deviceman.dto.User;

public class SelectUserDialogFragment extends DialogFragment {
    private static String TAG = SelectUserDialogFragment.class.getSimpleName();
    public static String EXTRA_KEY_USER = "user";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        List<User> users = (List<User>) args.getSerializable("users");


        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getResources().getString(R.string.dialog_title_select_user));
        dialog.setContentView(R.layout.dialog_select_user);

        ListView userListView = (ListView) dialog.findViewById(R.id.list_user);
        UserListAdapter userListAdapter = new UserListAdapter(getActivity(), users);
        userListView.setAdapter(userListAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                // クリックされたアイテムを取得します
                User selectedUser = (User) listView.getItemAtPosition(position);

                dismiss();
                Intent intent = new Intent();
                intent.putExtra(SelectUserDialogFragment.EXTRA_KEY_USER, selectedUser);


                PendingIntent pendingIntent = getActivity().createPendingResult(getTargetRequestCode(), intent, PendingIntent.FLAG_ONE_SHOT);
                Log.d(TAG, "created pending intent: " + pendingIntent.toString());
                try {
                    pendingIntent.send(Activity.RESULT_OK);
                } catch (PendingIntent.CanceledException e) {
                    // send fail
                }
            }
        });


        return dialog;
    }
}
