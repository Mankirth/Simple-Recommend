package com.example.try5;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "6720be7df48742f7aa9aed7f3c2ca5ee";
    private static final String REDIRECT_URI = "com.example.try5://callback";
    private static final int REQUEST_CODE = 1337;
    private boolean clicked = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creating GUI for the sign in page
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams main = new RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        main.setMargins(400, 1500, 0, 0);
        RelativeLayout.LayoutParams title = new RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        title.setMargins(75, 100, 30, 0);
        TextView titl = new TextView(this);
        titl.setTextSize(50);
        titl.setText("Simple\n   Recommend.");

        Button button = new Button(this);
        button.setText("Sign In");
        button.setTextSize(24);
        button.setWidth(300);
        button.setTextColor(Color.rgb(255, 255, 255));
        button.setBackgroundColor(Color.rgb(00, 200, 100));
        layout.addView(titl, title);
        layout.addView(button, main);
        setContentView(layout);

        //making the sign in button
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!clicked) {
                    button.setBackgroundColor(Color.rgb(00, 200, 150));
                    AuthorizationRequest.Builder builder =
                            new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

                    builder.setScopes(new String[]{"streaming", "user-top-read", "playlist-read-private", "playlist-modify-private", "playlist-modify-public"});
                    AuthorizationRequest request = builder.build();

                    AuthorizationClient.openLoginActivity(MainActivity.this, REQUEST_CODE, request);
                    clicked = true;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    String token = response.getAccessToken();
                    Intent intent1 = new Intent(MainActivity.this,MainActivity2.class);
                    intent1.putExtra("tok", token); //where user is an instance of User object
                    startActivity(intent1);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

}