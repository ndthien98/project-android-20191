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

import java.util.ArrayList;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send_email = getIntent().getExtras().getString("send").replace("@", "");
        recv_email = getIntent().getExtras().getString("recv").replace("@", "");
        setTitle(recv_email);

        list_message = findViewById(R.id.message_list_message);
        et_input = findViewById(R.id.message_et_input);
        btn_send = findViewById(R.id.message_btn_send);

        listMessageAdapter = new ListMessageAdapter(send_email,recv_email,this);
        list_message.setAdapter(listMessageAdapter);

        btn_send.setOnClickListener(this);

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

    }
}
