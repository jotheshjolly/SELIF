package com.jolly.jothesh.imagedector;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dyanamitechetan.vusikview.VusikView;

public class picturesandmusic extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener {


    ViewPager viewPager;
    Adapterviewer adapter;
    List<Modelviewer> models;

    private ImageButton btn_play_pause;
    private SeekBar seekbar;
    private TextView textview;
    private VusikView musicview;
    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realtimelength;
    final Handler handler= new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picturesandmusic);

        models = new ArrayList<>();
        models.add(new Modelviewer("https://bendyworks.com/assets/images/blog/2017-11-03-joyful-tech-44-3d9af336.jpg", "Covid-19", ""));
        models.add(new Modelviewer("https://media1.popsugar-assets.com/files/2013/02/06/1/192/1922398/netimgnDy6LR.png", "Covid-19", ""));
        models.add(new Modelviewer("https://wp-en.oberlo.com/wp-content/uploads/2019/11/motivational-quotes-about-strength.jpg", "Covid-19", ""));
        models.add(new Modelviewer("https://latestjokes.in/wp-content/uploads/2020/07/Funny-Jokes-In-English.png", "Covid-19", ""));
        models.add(new Modelviewer("https://i.ytimg.com/vi/-jhnxXp-g3c/maxresdefault.jpg", "Covid-19", ""));

        adapter = new Adapterviewer(models, this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        musicview = (VusikView)findViewById(R.id.musicView);

        textview=(TextView) findViewById(R.id.textTimer);
        seekbar= (SeekBar)findViewById(R.id.seekbar);
        seekbar.setMax(99);
        seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaPlayer.isPlaying())
                {
                    SeekBar seekBar = (SeekBar)v;
                    int playPosition = (mediaFileLength/100)*seekBar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                }
                return false;
            }
        });

        btn_play_pause = (ImageButton)findViewById(R.id.btn_play_pause);
        btn_play_pause.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                final ProgressDialog mdialog= new ProgressDialog(picturesandmusic.this);


                AsyncTask<String,String,String> mp3play = new AsyncTask<String, String, String>() {

                    @Override
                    protected void onPreExecute()
                    {
                        mdialog.setMessage("Please wait..");
                        mdialog.show();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        try
                        {
                            mediaPlayer.setDataSource(params[0]);
                            mediaPlayer.prepare();
                        }
                        catch (Exception ex)
                        {

                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s)
                    {
                        mediaFileLength=mediaPlayer.getDuration();
                        realtimelength=mediaFileLength;

                        if(!mediaPlayer.isPlaying())
                        {
                            mediaPlayer.start();
                            btn_play_pause.setImageResource(R.drawable.ic_pause_button);
                        }
                        else
                        {
                            mediaPlayer.stop();
                            btn_play_pause.setImageResource(R.drawable.ic_play_button);
                        }

                        updateSeekbar();
                        mdialog.dismiss();
                    }
                };

                mp3play.execute("https://bladed-conspiracy.000webhostapp.com/angry.mp3");
//                musicview.start();
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    private void updateSeekbar() {
        seekbar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLength)*100));
        if(mediaPlayer.isPlaying())
        {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekbar();
                    realtimelength-=1000;
                    textview.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realtimelength),
                            (TimeUnit.MILLISECONDS.toSeconds(realtimelength) -
                                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtimelength)))));
                }
            };

            handler.postDelayed(updater,1000);
        }


    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekbar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btn_play_pause.setImageResource(R.drawable.ic_play_button);
        musicview.stopNotesFall();
    }
}
