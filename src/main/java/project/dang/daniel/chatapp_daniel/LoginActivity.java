package project.dang.daniel.chatapp_daniel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    ///////////////////////////////////////////////////////////////////////////////////
    //1: Declare variables
    private Button mLogin;
    private EditText mEmail, mPassword;
    //
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;//Listener of Authentication database

    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //////////////////////////////////////////////////////////////////////
        //2: Listen the Authentication database. If the database exists, get the user (ID) account
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//Get user ID
                //Check whether the user exists in the database. If yes, go to MainActivity
                if (user != null) {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //////////////////////////////////////////////////////////////////////
        //3: Get the entered log-in information and sign-in Authentication with those information:
        mAuth = FirebaseAuth.getInstance();
        mLogin = findViewById(R.id.login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        //Set listener
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get entered data on email and password fields
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                //Check if email and password is empty or not. If being empty, do nothing
                if (email.isEmpty() || password.isEmpty()) {
                    //ERROR, pop up a message
                    Toast.makeText(getApplication(), "Please enter all your email and password",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //Process the sign-in
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    //Login unsuccessfully
                                    Toast.makeText(LoginActivity.this,
                                            "SIGN IN ERROR. WRONG EMAIL OR PASSWORD",
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
        //Add "listener" whenever start or start again the Activity
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStop() {
        super.onStop();
        //When activity is stopped, remove "listener"
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
