package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.github.ndthien98.app02messenger.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "REGISTER_ACTIVITY";
    FirebaseAuth mAuth;
    EditText etUsername;
    EditText etPassword;
    EditText etPassword2;
    Button btnRegister;
    String email, pass, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        etUsername = findViewById(R.id.register_et_email);
        etPassword = findViewById(R.id.register_et_password);
        etPassword2 = findViewById(R.id.register_et_repass);
        btnRegister = findViewById(R.id.register_btn_register);
        btnRegister.setOnClickListener(this);
    }

    private boolean validateEmailPass() {
        String em = etUsername.getText().toString().trim();
        String ps = etPassword.getText().toString().trim();
        String ps2 = etPassword2.getText().toString().trim();

        boolean ret;
        if (ps.length() < 8) {
            ret = false;
        } else if (em.length() < 3) {
            ret = false;
        } else if (em.contains("@") == false) {
            ret = false;
        } else if (ps.compareTo(ps2) != 0) {
            ret = false;
        } else {
            ret = true;
        }
        if (!ret) {
            Toast.makeText(this, "Wrong username and password format", Toast.LENGTH_SHORT).show();
        } else {
            email = em;
            pass = ps;
            pass2 = ps2;
        }
        return ret;
    }

    @Override
    public void onClick(View v) {
        if (!validateEmailPass()) return;
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            etUsername.setText("");
                            etPassword.setText("");
                            etPassword2.setText("");

                        }

                        // ...
                    }
                });
    }
}