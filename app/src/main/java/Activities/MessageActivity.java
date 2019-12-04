package Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import Adapters.ListMessageAdapter;
import Models.Message;
import io.github.ndthien98.app02messenger.R;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    String send_email;
    String recv_email;
    ListView list_message;
    ListMessageAdapter listMessageAdapter;
    EditText et_input;
    Button btn_send;
    String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send_email = getIntent().getExtras().getString("send").replace("@", "");
        recv_email = getIntent().getExtras().getString("recv").replace("@", "");
        setTitle(recv_email);

        path = validPath(path);

        list_message = findViewById(R.id.message_list_message);
        et_input = findViewById(R.id.message_et_input);
        btn_send = findViewById(R.id.message_btn_send);

        listMessageAdapter = new ListMessageAdapter(path, this, send_email, recv_email);
        list_message.setAdapter(listMessageAdapter);
        btn_send.setOnClickListener(this);

    }

    private String validPath(String path) {
        path = send_email.compareToIgnoreCase(recv_email) > 0
                ? send_email + recv_email :
                recv_email + send_email;
        while (path.contains("@") || path.contains(".") || path.contains("#") || path.contains("[") || path.contains("]")) {
            path = path.replace("@", "");
            path = path.replace(".", "");
            path = path.replace("]", "");
            path = path.replace("[", "");
            path = path.replace("#", "");
        }
        ;
        return path;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_btn_send:
                if (et_input.getText().toString().trim().length() > 0) {

                    Message newMess = new Message();
                    newMess.setSender(send_email);
                    newMess.setReceiver(recv_email);
                    newMess.setContent(et_input.getText().toString().trim());
                    newMess.setTimestamp(new Date().getTime());
                    DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference().child("messages").child(path).push();
                    messageRef.setValue(newMess);
                    et_input.setText("");
                }
                return;
        }
    }
}
