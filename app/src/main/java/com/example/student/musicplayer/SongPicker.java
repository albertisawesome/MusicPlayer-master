package com.example.student.musicplayer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.media.MediaMetadataRetriever;
import android.widget.ListView;

import java.util.ArrayList;

public class SongPicker extends AppCompatActivity {

    private int songID;
    private String songTitle;
    private String songArtist;

    private MediaMetadataRetriever songInfo;

    public static int[] songIDs;
    public static ArrayList<SongObject> songList ;

    private SongAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_picker);

        songInfo = new MediaMetadataRetriever();
        songList = new ArrayList<SongObject>();

        songIDs = new int[12];
        songIDs[0] = R.raw.bargains_in_a_tuxedo;
        songIDs[1] = R.raw.ablaze;
        songIDs[2] = R.raw.aqua;
        songIDs[3] = R.raw.arduous_task;
        songIDs[4] = R.raw.blow;
        songIDs[5] = R.raw.combat;
        songIDs[6] = R.raw.let_it_rip;
        songIDs[7] = R.raw.accusations;
        songIDs[8] = R.raw.nightmares;
        songIDs[9] = R.raw.courage;
        songIDs[10] = R.raw.madman;
        songIDs[11] = R.raw.ocean;

        for( int i=0;i<songIDs.length;i++) {
            songID = songIDs[i];

            Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + songID);
            songInfo.setDataSource(this, mediaPath);

            songTitle = songInfo.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            songArtist = songInfo.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            songList.add( new SongObject(songID, songTitle, songArtist) );
        }

        myAdapter = new SongAdapter(this, songList);

        ListView listView = (ListView) findViewById(R.id.songListView);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                launchPlayer(String.valueOf(position));
            }
        });
    }

    public void launchPlayer(String songID) {
        Intent launchSong = new Intent(this, MainSong.class);
        String message = String.valueOf(songID);
        launchSong.putExtra("songMessage", message);
        startActivity(launchSong);
    }
}
