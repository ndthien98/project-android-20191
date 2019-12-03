package Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import io.github.ndthien98.app02messenger.R;

public class ListFriendAdapter extends BaseAdapter  {
    ArrayList<String> listFriendData;
    DatabaseReference listFriendRef;
    FirebaseUser mUser;
    Context context;

    public ListFriendAdapter(Context context) {
        this.context = context;
        this.listFriendData = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setListFriendData(List<String> listFriendData) {
        this.listFriendData = (ArrayList<String>) listFriendData;
    }

    @Override
    public int getCount() {
        return listFriendData.size();
    }

    @Override
    public Object getItem(int position) {
        return listFriendData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder tag = new ViewHolder();
        if (convertView != null) {
            tag = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.item_list_friend, null);
            tag.avatar = convertView.findViewById(R.id.item_friend_avatar);
            tag.name = convertView.findViewById(R.id.item_friend_name);
            tag.message = convertView.findViewById(R.id.item_friend_message);
            convertView.setTag(tag);
        }
        tag.name.setText(listFriendData.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView message;
    }
}
