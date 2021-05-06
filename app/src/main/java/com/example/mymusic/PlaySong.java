package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    ImageView prev,next,play;
    TextView songName;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String TextContent;
    int pos;
    Thread updateSeek;
    SeekBar seekBar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);
        play=findViewById(R.id.play);
        songName=findViewById(R.id.name);
        seekBar=findViewById(R.id.seekBar);


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("songList");

        TextContent=bundle.getString("currentSong");
        songName.setText(TextContent);
        pos=bundle.getInt("position",0);

        Uri uri=Uri.parse(songs.get(pos).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        System.out.println(uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        updateSeek=new Thread() {
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getDuration();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

    }
}