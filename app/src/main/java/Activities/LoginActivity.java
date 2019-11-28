package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LOGIN_ACTIVITY";
    FirebaseAuth mAuth;
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    Button btnRegister;
    String email, pass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        etUsername = findViewById(R.id.login_et_username);
        etPassword = findViewById(R.id.login_et_password);
        btnLogin = findViewById(R.id.login_btn_login);
        btnRegister = findViewById(R.id.login_btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                if (validateEmailPass()) {
                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Đăng nhập thành công");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        etUsername.setText("");
                                        etPassword.setText("");
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }

                break;
            case R.id.login_btn_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }

    private boolean validateEmailPass() {
        String em = etUsername.getText().toString().trim();
        String ps = etPassword.getText().toString().trim();
        boolean ret;
        if (ps.length() < 8) {
            ret = false;
        } else if (em.length() < 3) {
            ret = false;
        } else if (em.contains("@") == false) {
            ret = false;
        } else {
            ret = true;
        }
        if (!ret) {
            Toast.makeText(this, "Wrong username and password format", Toast.LENGTH_SHORT).show();
        } else {
            email = em;
            pass = ps;
        }
        return ret;
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
