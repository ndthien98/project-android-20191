package Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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

import Models.User;
import io.github.ndthien98.app02messenger.R;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    TextView email;
    EditText name;
    EditText age;
    FirebaseUser mUser;
    Button accept, deny;
    RadioButton male, female;
    User current;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        email = findViewById(R.id.info_email);
        name = findViewById(R.id.info_et_name);
        age = findViewById(R.id.info_et_age);
        accept = findViewById(R.id.info_btn_accept);
        deny = findViewById(R.id.info_btn_deny);
        male = findViewById(R.id.info_rbtn_male);
        female = findViewById(R.id.info_rbtn_female);

        FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).addListenerForSingleValueEvent(this);
        accept.setOnClickListener(this);
        deny.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(this, MainActivity.class);
        switch (v.getId()) {
            case R.id.info_btn_accept:
                if (current == null) return;
                current.setAge(Integer.parseInt(age.getText().toString()));
                current.setIsmale(male.isChecked());
                current.setName(name.getText().toString().trim());
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(current).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(intent);
                            Toast.makeText(InfoActivity.this, "Update successful!", Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            startActivity(intent);
                            Toast.makeText(InfoActivity.this, "Error Update data", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                break;
            case R.id.info_btn_deny:
                showAlertDialog();
                break;
            case R.id.info_rbtn_male:
                male.setChecked(true);
                female.setChecked(false);
                break;
            case R.id.info_rbtn_female:
                male.setChecked(false);
                female.setChecked(true);
                break;
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Messenger App");
        builder.setMessage("Bạn có muốn huỷ thay đổi?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    // pull data ve
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        current = (User) dataSnapshot.getValue(User.class);
        email.setText(current.getEmail());
        name.setText(current.getName());
        age.setText(current.getAge() + "");
        if (current.isIsmale()) {
            male.setChecked(true);
            female.setChecked(false);
        } else {
            male.setChecked(false);
            female.setChecked(true);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
