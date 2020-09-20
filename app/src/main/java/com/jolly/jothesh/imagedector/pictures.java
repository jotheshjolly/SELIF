package com.jolly.jothesh.imagedector;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class pictures extends AppCompatActivity {

    ViewPager viewPager;
    Adapterviewer adapter;
    List<Modelviewer> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);

        models = new ArrayList<>();
        models.add(new Modelviewer("https://bendyworks.com/assets/images/blog/2017-11-03-joyful-tech-44-3d9af336.jpg", "Covid-19", ""));
        models.add(new Modelviewer("https://media1.popsugar-assets.com/files/2013/02/06/1/192/1922398/netimgnDy6LR.png", "Covid-19", ""));
        models.add(new Modelviewer("https://wp-en.oberlo.com/wp-content/uploads/2019/11/motivational-quotes-about-strength.jpg", "Covid-19", ""));
        models.add(new Modelviewer("https://latestjokes.in/wp-content/uploads/2020/07/Funny-Jokes-In-English.png", "Covid-19", ""));
        models.add(new Modelviewer("https://i.ytimg.com/vi/-jhnxXp-g3c/maxresdefault.jpg", "Covid-19", ""));

        adapter = new Adapterviewer(models, this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

    }
}
