package Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapters.ListFriendAdapter;
import Models.User;
import io.github.ndthien98.app02messenger.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN_ACTIVITY";
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    ListView list_friend;
    ListFriendAdapter listFriendAdapter;
    User current;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();

        listFriendAdapter = new ListFriendAdapter(MainActivity.this);
        dbRef.child("users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current = dataSnapshot.getValue(User.class);
                listFriendAdapter.setListFriendData(current.getList_friends());
                listFriendAdapter.notifyDataSetChanged();
                for (String name : current.getList_friends()) {
                    Log.d(TAG, "onDataChange: " + name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        list_friend = findViewById(R.id.main_list_friend);
        list_friend.setAdapter(listFriendAdapter);
        list_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                intent.putExtra("send", current.getEmail());
                intent.putExtra("recv", listFriendAdapter.getItem(position).toString());
                startActivity(intent);
//                Toast.makeText(MainActivity.this, listFriendAdapter.getItem(position) + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.main_menu_add_friend:
                showAddFriendDialog();
                return true;
            case R.id.main_menu_info:
                Intent intent2 = new Intent(this, InfoActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Thêm liên hệ");

        final View inflateView = this.getLayoutInflater().inflate(R.layout.dialog_add_friend, null);
        final EditText friendemail = inflateView.findViewById(R.id.main_et_friend_email);
        builder.setView(inflateView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (current != null) {
                            final String email = friendemail.getText().toString().trim();
                            if (email.contains("@") && email.length() > 4 && !email.equalsIgnoreCase(current.getEmail())) {
                                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        boolean flag = true;
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            if (data.child("email").getValue().toString().equalsIgnoreCase(email)) {
                                                flag = false;
                                                current.addFriend(friendemail.getText().toString().trim());
                                                dbRef.child("users").child(current.getUid()).setValue(current).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        dialog.dismiss();
                                                        Toast.makeText(MainActivity.this, "Thêm liên hệ thành công", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                User u = data.getValue(User.class);
                                                u.addFriend(current.getEmail());
                                                FirebaseDatabase.getInstance().getReference().child("users").child(u.getUid())
                                                        .setValue(u);
                                            }

                                        }
                                        if (flag)
                                            Toast.makeText(MainActivity.this, "Khong tim thay lien he " + email, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                Toast.makeText(MainActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "fail to add friend", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }
}
