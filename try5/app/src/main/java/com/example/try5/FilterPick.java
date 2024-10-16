package com.example.try5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import android.widget.EditText;
import android.widget.TextView;

public class FilterPick extends AppCompatActivity {
    float aveDance = -5;
    float aveInst = -5;
    float avetemp = -5;
    float aveKey = -5;
    float aveHappy = -5;
    float aveLength = -5;
    float total = 0;
    int playSize = 10;
    float[] filters = new float[]{10, -5, -5, -5, -5, -5, -5};
    boolean clicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String token = extras.getString("tok");
        ArrayList<String> ids = extras.getStringArrayList( "ids");
        SpoffEntity spoff = new SpoffEntity(token);

        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams[] lOs = new RelativeLayout.LayoutParams[12];
        for(int i=0; i < 12; i++)
            lOs[i] = new RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int y = 200;
        for(int i=0; i < 6; i++){
            lOs[i].setMargins(30, y, 180, 0);
            lOs[i].width = 1000;
            lOs[i].height = 200;
            y += 150;
        }
        lOs[6].setMargins(200, 1950, 0, 0);
        lOs[8].setMargins(450, 1400, 180, 0);
        lOs[10].setMargins(30, 30, 0, 0);
        lOs[11].setMargins(100, 1300,0 ,0);

        Switch[] sw = new Switch[6];
        for(int i=0; i < 6; i++) {
            sw[i] = new Switch(this);
            sw[i].setTextSize(26);
        }

        sw[0].setText("Dancability");
        sw[1].setText("Instrumentalness");
        sw[2].setText("Tempo");
        sw[3].setText("Key");
        sw[4].setText("Happiness");
        sw[5].setText("Length");


        EditText size = new EditText(this);
        size.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        size.setTextSize(24);
        size.setHeight(150);
        size.setWidth(150);
        size.setText("10");
        TextView sizeTxt = new TextView(this);
        sizeTxt.setTextSize(24);
        sizeTxt.setHeight(150);
        sizeTxt.setWidth(1000);
        sizeTxt.setText("New Playlist Size (1 - 100)");
        TextView title = new TextView(this);
        title.setTextSize(30);
        title.setHeight(150);
        title.setWidth(1000);
        title.setText("Select Your Filters");

        playSize = Integer.parseInt(size.getText().toString());

        Button btn = new Button(this);

        btn.setText("Generate");
        btn.setTextSize(24);
        btn.setHeight(100);
        btn.setWidth(700);
        btn.setBackgroundColor(Color.rgb(00, 200, 100));
        setContentView(layout, lOs[7]);
        layout.addView(btn, lOs[6]);
        for(int i=0; i < 6; i++)
            layout.addView(sw[i], lOs[i]);
        layout.addView(size, lOs[8]);
        Button back = new Button(this);
        lOs[9].setMargins(750, 0, 0, 0);
        back.setWidth(300);
        back.setText("Back");
        back.setTextSize(24);
        back.setBackgroundColor(Color.rgb(00, 200, 100));
        layout.addView(back, lOs[9]);
        layout.addView(title, lOs[10]);
        layout.addView(sizeTxt, lOs[11]);

        Thread throod = new Thread(new Runnable() {
            @Override
            public void run() {
                if(sw[0].isChecked()){
                    total = 0;
                    for(int i=0; i < ids.size(); i++){
                        total += spoff.getFeatures(ids.get(i), 0);
                    }
                    aveDance = total / ids.size();

                }
                if(sw[1].isChecked()) {
                    total = 0;
                    for (int i = 0; i < ids.size(); i++) {
                        total += spoff.getFeatures(ids.get(i), 1);
                    }
                    aveInst = total / ids.size();
                }

                if(sw[2].isChecked()){
                    total = 0;
                    for(int i=0; i < ids.size(); i++){
                        total += spoff.getFeatures(ids.get(i), 2);
                    }
                    avetemp = total / ids.size();
                }

                if(sw[3].isChecked()){
                    total = 0;
                    for(int i=0; i < ids.size(); i++){
                        total += spoff.getFeatures(ids.get(i), 3);
                    }
                    aveKey = total / ids.size();
                }

                if(sw[4].isChecked()){
                    total = 0;
                    for(int i=0; i < ids.size(); i++){
                        total += spoff.getFeatures(ids.get(i), 4);
                    }
                    aveHappy = total / ids.size();
                }

                if(sw[5].isChecked()){
                    total = 0;
                    for(int i=0; i < ids.size(); i++){
                        total += spoff.getFeatures(ids.get(i), 5);
                    }
                    aveLength = total / ids.size();
                    Log.d("got", "Got average Length");
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clicked) {
                    back.setBackgroundColor(Color.rgb(00, 200, 150));
                    Intent intent1 = new Intent(FilterPick.this, MainActivity2.class);
                    intent1.putExtra("tok", token); //where user is an instance of User object
                    startActivity(intent1);
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clicked && playSize > 0 && playSize < 101) {
                    btn.setText("Generating...");
                    btn.setBackgroundColor(Color.rgb(00, 200, 150));
                    throod.start();

                    while(throod.isAlive()){}
                    filters[0] = playSize;
                    filters[1] = aveDance;
                    filters[2] = aveInst;
                    filters[3] = aveHappy;
                    filters[4] = aveLength;
                    filters[5] = avetemp;
                    filters[6] = aveKey;

                    Intent intent1 = new Intent(FilterPick.this, Reccomendation.class);
                    intent1.putExtra("tok", token);
                    intent1.putExtra("ids", ids);
                    intent1.putExtra("filt", filters);
                    startActivity(intent1);
                    clicked = true;
                }
            }
        });

        size.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(size.getText().toString()) > 0 && Integer.parseInt(size.getText().toString()) < 101)
                        playSize = Integer.parseInt(size.getText().toString());
                } catch(NumberFormatException e) {}
            }
        });



    }
}