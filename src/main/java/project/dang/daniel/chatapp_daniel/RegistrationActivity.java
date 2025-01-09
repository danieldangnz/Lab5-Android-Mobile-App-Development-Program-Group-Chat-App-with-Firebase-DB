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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    ///////////////////////////////////////////////////////////////////////////////////
    //1: Declare variables
    private Button mRegistration;
    private EditText mEmail, mPassword, mName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;//Firebase Authentication listener

    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        ///////////////////////////////////////////////////////////////////////
        //2: Listen the Authentication database. If the database exists, go back to MainActivity
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                    return;
                }
            }
        };

        //////////////////////////////////////////////////////////////////////
        //3: Get the entered log-in information ==> create a user in Authentication with those information
        mAuth = FirebaseAuth.getInstance();
        //
        mRegistration = findViewById(R.id.registration);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        //
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the entered user information
                final String name = mName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                //Check if name, email and password is empty or not. If being empty, do nothing
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    //ERROR, pop up a message
                    Toast.makeText(getApplication(), "Please enter all your name, email and password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    //ERROR, pop up a message
                                    Toast.makeText(getApplication(), "ERROR. EMAIL IS INVALID",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //SUCCESSFULLY create a new user with those information
                                    //1: setDisplayName of user while creating user in Authentication
                                    //FirebaseUser.getCurrentUser().setDisplayName(name);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                    user.updateProfile(profileUpdates);
                                    //2: Add a "user" child into the Real-time Database
                                    String userId = mAuth.getCurrentUser().getUid();
                                    DatabaseReference currentUserDb = FirebaseDatabase
                                            .getInstance()
                                            .getReference()
                                            .child("users")
                                            .child(userId);
                                    //3: Use "Map" type to add user profile
                                    Map userInfo = new HashMap();
                                    userInfo.put("email", email);
                                    userInfo.put("name", name);
                                    userInfo.put("profileImageUrl", "default");
                                    //Add new data to Real-time Database
                                    currentUserDb.updateChildren(userInfo);
                                    //4: Pop up a Toast message
                                    Toast.makeText(getApplication(), "REGISTERED SUCCESSFULLY",
                                            Toast.LENGTH_SHORT).show();
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
        //Add "Firebase Authentication listener" whenever start or start again the Activity
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();
        //Add "Firebase Authentication listener" whenever start or start again the Activity
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStop() {
        super.onStop();
        //When activity is stopped, remove "Firebase Authentication listener"
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
