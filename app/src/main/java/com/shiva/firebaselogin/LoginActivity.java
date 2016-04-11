package com.shiva.firebaselogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mUserName, mPassword;

    /* FireBase object to authenticate with the firebase account*/
    private Firebase mFirebase;

    /* Progress dialog to display to the user while doing network operations*/
    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Set up a context to the FireBase to work with the app*/
        Firebase.setAndroidContext(this);

        /* Connect to FireBase with a url to handle the data in it*/
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        Button loginButton = (Button) findViewById(R.id.login);
        mUserName = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        if (loginButton != null) {
            loginButton.setOnClickListener(this);
        }


        /* Setup the progress dialog that is displayed while fetching data from Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        final String username = mUserName.getText().toString();
        final String password = mPassword.getText().toString();
        if ( v.getId() == R.id.login) {
            mAuthProgressDialog.show();

          //  loginWithPassword();
            //mFirebaseRef.authWithPassword("test@firebaseuser.com", "test1234", new AuthResultHandler("password"));

            /* EventListener to listen for the events when contacting the FireBase for the required resource*/
            mFirebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        User post = postSnapshot.getValue(User.class);
                        if(post.getOwner().equalsIgnoreCase(username) && post.getPassword().equalsIgnoreCase(password)) {
                            mAuthProgressDialog.dismiss();
                            View view = findViewById(R.id.coordinatorlayout);
                            if (view != null) {
                                Snackbar.make(view, "Login Successful", Snackbar.LENGTH_LONG).show();
                            }
                            return;
                        }
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    mAuthProgressDialog.dismiss();
                    View view = findViewById(R.id.coordinatorlayout);
                    if (view != null) {
                        Snackbar.make(view, "Fetching failed", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
