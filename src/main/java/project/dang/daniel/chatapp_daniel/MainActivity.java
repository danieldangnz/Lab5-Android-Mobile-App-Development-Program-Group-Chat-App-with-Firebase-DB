package project.dang.daniel.chatapp_daniel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //1: Declare variables
    public static Boolean started = false;
    private FirebaseAuth mAuth;
    private Button mLogin, mRegistration;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////////////////////////////////////////////////////////////////////////
        //2: Get userID in Firebase Authentication database
        mAuth = FirebaseAuth.getInstance();

        //////////////////////////////////////////////////////////////////////////////
        //2: Find reference and do casting for "mLogin"  & "mRegistration" buttons
        mLogin = (Button) findViewById(R.id.login);
        mRegistration = (Button) findViewById(R.id.registration);
        //At the beginning, make these two button invisible
        mLogin.setVisibility(View.INVISIBLE);
        mRegistration.setVisibility(View.INVISIBLE);

        //////////////////////////////////////////////////////////////////////////////
        //3: Check whether the userID exists in the Authentication database. If yes, go straight to
        //"ChatActivity". If not, display "Login" and "Registration" buttons for users options
        if (mAuth.getCurrentUser() != null) {
            //User already logged in yet, go straight to chat layout
            Intent intent = new Intent(getApplication(), ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;

        } else {
            //User hasn't logged in yet, make two buttons visible
            mLogin.setVisibility(View.VISIBLE);
            mRegistration.setVisibility(View.VISIBLE);
        }

        //////////////////////////////////////////////////////////////////////////////
        //4: Set listener for 'Login" button, open LoginActivity when clicking it
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                return;
            }
        });

        //////////////////////////////////////////////////////////////////////////////
        //4: Set listener for "Registration" button, open RegistrationActivity when clicking it
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RegistrationActivity.class);
                startActivity(intent);
                return;
            }
        });
    }
}
