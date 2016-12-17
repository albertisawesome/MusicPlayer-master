package com.example.student.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainSong extends AppCompatActivity {

    private MediaPlayer song;

    private Button pauseButton;
    private Button playButton;
    private Button stopButton;
    private Button rewindButton;
    private Button forwardButton;

    private TextView currentTimeView;
    private TextView totalTimeView;
    private TextView title;
    private TextView author;

    private double currentTimeMS;
    private double totalTimeMS;

    private Handler time = new Handler();

    private SeekBar seek;
    private int seekTime = 0;

    private int songNumber;
    private SongObject thisSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_song);

        //Getting the exact song from the Song Picker class
        Intent thisIntent = getIntent();
        String songID = thisIntent.getStringExtra("songMessage");

        //
        songNumber = Integer.parseInt( thisIntent.getStringExtra("songMessage"));
        thisSong = SongPicker.songList.get(songNumber);

        //Title + Author Config
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);

        //Song Config
        song = MediaPlayer.create(getApplicationContext(), thisSong.songID);

        //Total time related items
        totalTimeMS = song.getDuration();

        int totalMinutes = (int) (totalTimeMS / 1000 / 60);
        int totalSeconds = ((int) (totalTimeMS / 1000)) % 60;

        //Sets text total time view
        totalTimeView = (TextView) findViewById(R.id.totalTime);
        totalTimeView.setText(totalMinutes + " min, " + totalSeconds + " sec");

        //Button Config
        pauseButton = (Button) findViewById(R.id.pause);
        playButton = (Button) findViewById(R.id.play);
        stopButton = (Button) findViewById(R.id.stop);
        rewindButton = (Button) findViewById(R.id.rewind);
        forwardButton = (Button) findViewById(R.id.forward);

        //Seek Bar Config
        seek = (SeekBar) findViewById(R.id.seeker);
        seek.setMax((int) totalTimeMS);

        //Time Handler for current time view
        time.postDelayed(UpdateSongTime, 100);

        //Seek Bar Listener
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               seekTime = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                song.seekTo(seekTime);
                currentTimeMS = seekTime;
            }
        });

        //Retrieving Specific Song Info
        MediaMetadataRetriever songInfo = new MediaMetadataRetriever();

        Uri filepath= Uri.parse("android.resource://" + getPackageName() + "/" + thisSong.songID);
        songInfo.setDataSource(this, filepath);

        title.setText(thisSong.title);
        author.setText(thisSong.artist);
    }

    //Time Updater
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            currentTimeMS = song.getCurrentPosition();

            seek.setProgress((int) currentTimeMS);

            int currentMinutes = (int) (currentTimeMS / 1000 / 60);
            int currentSeconds = ((int) (currentTimeMS / 1000)) % 60;

            currentTimeView = (TextView) findViewById(R.id.currentTime);
            currentTimeView.setText(currentMinutes + " min, " + currentSeconds + " sec");

            //For Rewind / Forward Buttons
            if(currentTimeMS > 5000) {
                rewindButton.setEnabled(true);
            }
            else {
                rewindButton.setEnabled(false);
            }

            if(currentTimeMS < totalTimeMS - 5000) {
                forwardButton.setEnabled(true);
            }
            else {
                forwardButton.setEnabled(false);
            }

            time.postDelayed(this, 100);
        }
    };

    //Function Section of Button Config
    public void play(View view) {
        song.start();

        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
        playButton.setEnabled(false);

        Context context = getApplicationContext();
        CharSequence text = "The song is now playing.";
        int duration = Toast.LENGTH_SHORT;
        Toast playMessage= Toast.makeText(context, text, duration);
        playMessage.show();
    }

    public void pause(View view) {
        song.pause();

        playButton.setEnabled(true);
        pauseButton.setEnabled(false);

        Context context = getApplicationContext();
        CharSequence text = "The song is now paused.";
        int duration = Toast.LENGTH_SHORT;
        Toast pauseMessage= Toast.makeText(context, text, duration);
        pauseMessage.show();
    }

    public void stop(View view) {
        song.seekTo(0);
        song.pause();

        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        Context context = getApplicationContext();
        CharSequence text = "The song is now stopped.";
        int duration = Toast.LENGTH_SHORT;
        Toast stopMessage= Toast.makeText(context, text, duration);
        stopMessage.show();
    }

    public void rewind(View view) {
        song.seekTo((int) (currentTimeMS - 5000));
    }

    public void forward(View view) {
        song.seekTo((int) (currentTimeMS + 5000));
    }
}
