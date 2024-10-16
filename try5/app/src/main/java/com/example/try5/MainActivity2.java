package com.example.try5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;


public class MainActivity2 extends AppCompatActivity {
    ArrayList<String> current = new ArrayList<String>();
    ArrayList<String> trackIds = new ArrayList<String>();
    ArrayList<Button> slots = new ArrayList<Button>();
    private int idSize = 0;
    Context con = this;
    private boolean clicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        String token = extras.getString("tok");
        SpoffEntity spoff = new SpoffEntity(token);

        super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams[] lOs = new RelativeLayout.LayoutParams[15];
        for(int i=0; i < 15; i++)
            lOs[i] = new RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        TextView list = new TextView(this);
        list.setText("Recommendation List | 0 Songs");
        list.setTextSize(20);
        list.setWidth(800);
        lOs[13].setMargins(15, 1975, 0, 0);
        layout.addView(list, lOs[13]);
        EditText in = new EditText(this);
        in.setHint("Enter a Spotify Song Name");
        in.setWidth(1000);
        in.setTextSize(26);
        lOs[12].setMargins(30, 0, 0, 0);
        layout.addView(in, lOs[12]);
        Button gen = new Button(this);
        gen.setTextSize(24);
        gen.setText(">");
        gen.setWidth(50);
        gen.setBackgroundColor(Color.rgb(00, 200, 100));
        lOs[14].setMargins(800, 1950, 0, 0);
        layout.addView(gen, lOs[14]);
        setContentView(layout);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int y = 130;
                    for(int i=0; i < 12; i++) {
                        slots.add(new Button(con));
                        lOs[i].setMargins(30, y, 0, 0);
                        slots.get(i).setWidth(1000);
                        slots.get(i).setText("Song Name - Artist");
                        slots.get(i).setTextSize(24);
                        slots.get(i).setLines(1);
                        slots.get(i).setTextColor(Color.rgb(255, 255, 255));
                        slots.get(i).setBackgroundColor(Color.rgb(00, 200, 100));
                        y += 150;
                    }
                    current = spoff.search(slots);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        while(thread.isAlive()){} //need to make buttons in that thread and wait for it to finish to avoid errors
        for(int i=0; i < 12; i++)
            layout.addView(slots.get(i), lOs[i]);

        Thread throd = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!in.getText().toString().equals(""))
                        current = spoff.search(in.getText().toString(), slots);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        View.OnClickListener buttons = new View.OnClickListener(){
            public void onClick(View v){
                try {
                    Thread throod = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (trackIds == null) {
                                    spoff.addSong(current.get(slots.indexOf((Button) v)));
                                    idSize += 1;
                                } else if (!trackIds.contains(current.get(slots.indexOf((Button) v)))) {
                                    spoff.addSong(current.get(slots.indexOf((Button) v)));
                                    idSize += 1;
                                } else {
                                    spoff.removeSong(current.get(slots.indexOf((Button) v)));
                                    idSize -= 1;
                                }
                                trackIds = new ArrayList<String>();
                                trackIds = spoff.getInIds();
                                String txt = "Recommendation List | " + idSize + " Songs";
                                list.setText(txt);
                            }catch (IndexOutOfBoundsException e) {}
                        }
                    });
                    throod.start();
                } catch(Exception e){}
            }
        };

        for (int i = 0; i < slots.size(); i++)
            slots.get(i).setOnClickListener(buttons);

        in.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(throd.getState() == Thread.State.NEW)
                    throd.start();
                else if(throd.getState() == Thread.State.TERMINATED)
                    throd.run();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trackIds.size() > 0 && !clicked) {
                    gen.setBackgroundColor(Color.rgb(00, 200, 150));
                    Intent intent1 = new Intent(MainActivity2.this, FilterPick.class);
                    intent1.putExtra("tok", token);
                    intent1.putExtra("ids", trackIds);
                    startActivity(intent1);
                    clicked = true;
                }
            }
        });
    }
}