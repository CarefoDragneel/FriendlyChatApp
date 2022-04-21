package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //setting default length of the text message we can enter
    private static final int DEFAULT_TEXT_MSG_LENGTH = 1000;

    // all the hooks
    private ImageButton mImagePickerButton;
    private EditText mTextMessageEditText;
    private Button mSendButton;

    // reference to realtime firebase database
    private FirebaseDatabase mFirebaseDatabase;
    // this is used to access specific part of the database: here, only access messages
    private DatabaseReference mMessageDatabaseReference;

    // temporary username
    private String mUserName = "Anonymous";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // attaching all the hooks to layout elements
        mImagePickerButton = (ImageButton) findViewById(R.id.image_picker_button);
        mTextMessageEditText = (EditText) findViewById(R.id.text_message);
        mSendButton = (Button) findViewById(R.id.send_button);

        // disabling send button before any text is entered in the text message
        mTextMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()>0){
                    mSendButton.setEnabled(true);
                }
                else{
                    mSendButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // setting limit of the size of text message
        mTextMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_TEXT_MSG_LENGTH)});

        // creating firebase database object to create an access point to the database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // here create the reference to the 'message' child of the root reference of the database
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("message");

        // adding click listener to the save button
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendlyMessage friendlyMessage = new FriendlyMessage(mTextMessageEditText.getText().toString(), mUserName, null);
                mMessageDatabaseReference.push().setValue(friendlyMessage);
                // clearing the text in the edit text view
                mTextMessageEditText.setText("");
            }
        });


    }

    // this method is to inflate the overflow menu in the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_overflow_menu, menu);
        return true;
    }

    // this is to provide functionality to the options present in the overflow menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                // do your sign-out stuff
                break;
            }
            // case blocks for other MenuItems (if any)
        }
        return true;
    }
}