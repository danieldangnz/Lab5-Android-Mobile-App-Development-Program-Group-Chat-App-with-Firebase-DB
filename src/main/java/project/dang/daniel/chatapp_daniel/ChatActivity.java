package project.dang.daniel.chatapp_daniel;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    ///////////////////////////////////////////////////////////////////////////////////
    //1: Declare variables
    //Sign-out - 1: Declare variables
    private TextView username;
    private Button logoutBtn;

    //Post a chat message - 1: Declare variables
    private FloatingActionButton sendBtn;
    private EditText input;

    //Display a chat message - 1: Declare variables
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listOfMessages;

    ///////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        ///////////////////////////////////////////////////////////////////////////////
        //Sign-out - 2: Find references for UI widgets
        username = (TextView) findViewById(R.id.userName);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);

        //Display current username
        String usernameString = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        username.setText(usernameString);

        ///////////////////////////////////////////////////////////////////////////////
        //Sign-out - 3: Set click listener for the logoutBtn
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1: Sign out the Firbase Authentication database
                FirebaseAuth.getInstance().signOut();
                //2: Go back to MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //3: Pop up a Toast message
                Toast.makeText(getApplicationContext(),
                        "Bye Bye " + username.getText().toString(), Toast.LENGTH_LONG).show();
                //
                return;
            }
        });


        ///////////////////////////////////////////////////////////////////////////////
        //Post a chat message - 2: Find references for UI widgets
        sendBtn = (FloatingActionButton) findViewById(R.id.sendBtn);
        input = (EditText) findViewById(R.id.input);

        ///////////////////////////////////////////////////////////////////////////////
        //Post a chat message - 3: Set click listener for the sendBtn
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if the input field is not empty, send the message & delete it after sending out
                if (!input.getText().toString().isEmpty()) {
                    //Read the input field and push a new instance of ChatMessage to the Firebase database
                    //We also create a node "GroupChatMessages" to store those chat messages
                    FirebaseDatabase.getInstance().getReference().child("GroupChatMessages")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName()));
                    //Clear the input
                    input.setText("");
                }
            }
        });

        ///////////////////////////////////////////////////////////////////////////////
        //Display a chat message - 2: displayChatMessages() method
        //Find reference for the listOfMessage listView
        listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        //Collect all the posted messages stored inside the node "GroupChatMessages" of Firebase Database
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message,
                FirebaseDatabase.getInstance().getReference().child("GroupChatMessages")) {
            @Override
            protected void populateView(View view, ChatMessage model, int position) {
                //1: Get references to the views of message.xml
                TextView messageText = (TextView) view.findViewById(R.id.message_text);
                TextView messageUser = (TextView) view.findViewById(R.id.message_user);
                TextView messageTime = (TextView) view.findViewById(R.id.message_time);
                //2: Set the chat text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                //3: Format the date before showing it on chat room
                messageTime.setText(android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));

            }
        };
        //Populate listview with chat messages
        listOfMessages.setAdapter(adapter);
    }
}
