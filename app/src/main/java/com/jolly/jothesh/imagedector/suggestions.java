package com.jolly.jothesh.imagedector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class suggestions extends AppCompatActivity {

    CheckBox pictures,music;
    RadioButton video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        Intent intent = getIntent();
        String emotions = intent.getStringExtra("play");

        video=(RadioButton) findViewById(R.id.video);

        pictures=(CheckBox)findViewById(R.id.pictures);
        music=(CheckBox)findViewById(R.id.music);

       video.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(video.isChecked())
               {
                   pictures.setChecked(false);
                   music.setChecked(false);
               }
           }
       });

       pictures.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(pictures.isChecked()) {

                   video.setChecked(false);

               }
           }
       });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(music.isChecked()) {

                    video.setChecked(false);

                }
            }
        });

    }

    public void play(View view) {

        if(video.isChecked())
        {
            startActivity(new Intent(suggestions.this,videos.class));
            Toast.makeText(getApplicationContext(),"video",Toast.LENGTH_SHORT).show();
        }else {
            if(pictures.isChecked() && music.isChecked())
            {
                Toast.makeText(getApplicationContext(),"pictures & music",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(suggestions.this,picturesandmusic.class));
            }else
            {
                if(pictures.isChecked())
                {
                    startActivity(new Intent(suggestions.this,pictures.class));
                    Toast.makeText(getApplicationContext(),"pictures",Toast.LENGTH_SHORT).show();
                }else {
                    if(music.isChecked())
                    {
                        startActivity(new Intent(suggestions.this,music.class));
                        Toast.makeText(getApplicationContext(),"music",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"please select images/music/videos",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }





    }
}
