package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Views
    Button mRegisterBtn,mLoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init views
        mRegisterBtn = findViewById(R.id.register_btn);
        mLoginBtn=findViewById(R.id.login_btn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start RegisterActivity
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        //handle login button click
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start LoginActivity
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }
}

/*In this Part(13):
 * ->Show Online/LastSeen status of user in toolbar of chat activity
 *  When user Register, add one more key named onlineStatus having value online
 *  Create method that will set the value on onlineStatus either online or Last Seen at...
 * Call this method in onStart with value online
 * Call this method in onPause with value as timestamp
 * Get the value if it is online simply show otherwise convert timestamp to time
 */

/*In this Part(14):
 * ->Show typing status of user in toolbar of chat activity
 *  When user Register, add one more key named typingTo having value receiver's uid
 *  Add textChange listener to edit text
 *  EditText not empty means user is typing something
 *    so set it's value like receiver's uid
 *  EditText empty means user is not typing
 *    so set it's value like noOne
 */

/*In this Part(15):
 * ->Delete Message
 *  Who can delete what?
 *  1) Sender can delete only his own message OR
 *  2) Sender can delete his and recevier's message
 *  After clicking delete what will happen?
 *  1) Remove message OR
 *  2) Update value of message to "This message was deleted..."
 */

//first of all lets check if there is any library update available or not


/*In this Part(26):
 * ->Show ChatList
 *  User Name
 *  User Profile Image
 *  Online/Offline Status
 *  Last Message
 */





