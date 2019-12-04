package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Models.Message;
import io.github.ndthien98.app02messenger.R;

public class ListMessageAdapter extends BaseAdapter {
    ArrayList<Message> listMessageData;
    String send_email;
    String recv_email;
    Context context;
    DatabaseReference dbref;

    public ListMessageAdapter(String send_email, String recv_email, Context context) {
        this.context = context;
        this.send_email = send_email;
        this.recv_email = recv_email;
        listMessageData = new ArrayList<>();
        dbref = FirebaseDatabase.getInstance().getReference().child("messages").getRef();
        String path = send_email.compareToIgnoreCase(recv_email) > 0
                ? send_email + recv_email :
                recv_email + send_email;
        path = path.replace("@", "");
        Message sample1 = new Message(100);
        sample1.setSender(send_email);
        Message sample2 = new Message(10000);
        sample2.setSender(send_email);
        Message sample3 = new Message(100100);
        sample3.setSender(recv_email);
        Message sample4 = new Message(100100100);
        sample4.setSender(recv_email);
        listMessageData.add(sample1);
        listMessageData.add(sample2);
        listMessageData.add(sample3);
        listMessageData.add(sample4);
        dbref.child("messages").child(path).setValue(listMessageData);
    }


    @Override
    public int getCount() {
        if (listMessageData == null) {
            listMessageData = new ArrayList<>();
        }
        return listMessageData.size();
    }

    @Override
    public Object getItem(int position) {
        if (listMessageData == null) {
            listMessageData = new ArrayList<>();
        }
        return listMessageData.get(position);
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
            convertView = View.inflate(context, R.layout.item_list_message, null);
            tag.author = convertView.findViewById(R.id.item_message_author);
            tag.content = convertView.findViewById(R.id.item_message_content);
            convertView.setTag(tag);
        }
        tag.author.setText(listMessageData.get(position).getSender());
        tag.content.setText(listMessageData.get(position).getContent());
        LinearLayout layout = convertView.findViewById(R.id.item_message_layout);
        LinearLayout background = convertView.findViewById(R.id.item_message_background);
        if (listMessageData.get(position).getSender().equalsIgnoreCase(send_email)) {
            layout.setGravity(Gravity.END);
            background.setBackgroundResource(R.drawable.gradient_background_gray);
        } else {
            layout.setGravity(Gravity.START);
            background.setBackgroundResource(R.drawable.gradient_background_light);
        }


        return convertView;
    }

    class ViewHolder {
        TextView content;
        TextView author;
    }
}
