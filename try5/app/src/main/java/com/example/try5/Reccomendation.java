package com.example.try5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class Reccomendation extends AppCompatActivity {
    ArrayList<String> newIds = new ArrayList<String>();
    String txt = "";
    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String token = extras.getString("tok");
        ArrayList<String> ids = extras.getStringArrayList( "ids");
        float[] filters = extras.getFloatArray("filt");
        SpoffEntity spoff = new SpoffEntity(token);

        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams[] lOs = new RelativeLayout.LayoutParams[4];
        for(int i=0; i < 4; i++)
            lOs[i] = new RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView title = new TextView(this);
        lOs[0].setMargins(15, 25, 0, 0);
        title.setWidth(1000);
        title.setText("Here is Your New Playlist");
        title.setTextSize(23);
        Button back = new Button(this);
        lOs[3].setMargins(750, 0, 0, 0);
        back.setWidth(300);
        back.setText("Back");
        back.setTextSize(24);
        TextView body = new TextView(this);
        lOs[1].setMargins(30, 150, 0, 0);
        body.setWidth(1000);
        body.setHeight(1700);
        body.setTextSize(24);
        body.canScrollVertically(2);
        Button add = new Button(this);
        lOs[2].setMargins(30, 1850, 0, 0);
        add.setWidth(1000);
        add.setHeight(200);
        add.setText("Add This Playlist to Your Account");
        add.setBackgroundColor(Color.rgb(00, 200, 100));
        add.setTextSize(24);
        back.setBackgroundColor(Color.rgb(00, 200, 100));

        layout.addView(title, lOs[0]);
        layout.addView(body, lOs[1]);
        layout.addView(add, lOs[2]);
        layout.addView(back, lOs[3]);

        setContentView(layout, lOs[0]);
        body.setMovementMethod(new ScrollingMovementMethod());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clicked) {
                    back.setBackgroundColor(Color.rgb(00, 200, 150));
                    Intent intent1 = new Intent(Reccomendation.this, FilterPick.class);
                    intent1.putExtra("tok", token);
                    intent1.putExtra("ids", ids);
                    startActivity(intent1);
                    clicked = true;
                }
            }
        });

        Thread addIt = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    spoff.addPlaylist(newIds);
                    Intent intent1 = new Intent(Reccomendation.this,MainActivity2.class);
                    intent1.putExtra("tok", token); //where user is an instance of User object
                    startActivity(intent1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newIds = spoff.getReccomendation(ids, (int)filters[0], filters[1], filters[2], filters[3], (int)filters[4], filters[5], (int)filters[6]);
                    txt = spoff.getList(newIds, (int)filters[0]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        while(thread.isAlive()) {}
        body.setText(txt);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clicked) {
                    add.setBackgroundColor(Color.rgb(00, 200, 150));
                    addIt.start();
                    clicked = true;
                }
            }
        });
    }
}