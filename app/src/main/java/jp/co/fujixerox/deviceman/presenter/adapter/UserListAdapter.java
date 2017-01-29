package jp.co.fujixerox.deviceman.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import jp.co.fujixerox.deviceman.R;
import jp.co.fujixerox.deviceman.service.dto.User;

public class UserListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<User> mUsers;

    public UserListAdapter(Context context, List<User> users){
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mUsers = users;
    }

    @Override
    public int getCount(){
        return mUsers.size();
    }

    @Override
    public User getItem(int position){
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = mLayoutInflater.inflate(R.layout.listitem_select_user, parent, false);
        TextView nameView = (TextView)convertView.findViewById(R.id.text_listitem_select_user_key);
        nameView.setText(mUsers.get(position).getId());

        TextView hobbyView = (TextView)convertView.findViewById(R.id.text_listitem_select_user_value);
        hobbyView.setText(mUsers.get(position).getName());

        return convertView;
    }
}