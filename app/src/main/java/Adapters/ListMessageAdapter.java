package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import Models.Message;
import io.github.ndthien98.app02messenger.R;

public class ListMessageAdapter extends BaseAdapter {
    private static final String TAG = "LIST_MESSAGE_ADAPTER";
    ArrayList<Message> listMessageData;
    String send_email;
    String recv_email;
    Context context;
    DatabaseReference dbref;

    public ListMessageAdapter(String path, final Context context,final String send_email, final String recv_email) {
        this.context = context;
        this.send_email = send_email;
        this.recv_email = recv_email;
        listMessageData = new ArrayList<>();

        dbref = FirebaseDatabase.getInstance().getReference().child("messages").child(path).getRef();


        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message newMess = (Message) dataSnapshot.getValue(Message.class);
                Log.d(TAG, "onChildAdded: " + newMess.getContent() + "string s: " + s);
                listMessageData.add(newMess);
                if (newMess.getSender().equalsIgnoreCase(recv_email)) {
                    Toast.makeText(context, "Có tin nhắn mới", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
