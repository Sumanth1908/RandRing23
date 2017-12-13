package com.sumanthjillepally.randomringtones;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<SearchResults.SongData>
{
    private Activity context;
    public Uri sngUri;
    private MediaPlayer mp = new MediaPlayer();
    private ArrayList<SearchResults.SongData> sngList;
    //private long resourceid;
    public CustomAdapter(Activity context,int textViewResourceId,ArrayList<SearchResults.SongData> snglist) {
        super(context,textViewResourceId,snglist);
            this.context = context;
            this.sngList = snglist;
            //this.resourceid=resourceid;
    }
    @Override
    public View getView(final int position, View view, final ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowview = inflater.inflate(R.layout.custom_adapter,null,true);

        final TextView title =(TextView) rowview.findViewById(R.id.songtitle);
        //TextView sngID =(TextView) rowview.findViewById(R.id.songID);

        CheckBox sngID = (CheckBox) rowview.findViewById(R.id.checkBoxRow);
        title.setText(Integer.toString(position+1)+". "+sngList.get(position).getSongTitle());

        //sngID.setText(Integer.toString((int) sngList.get(position).getSongID()));
        //sngID.setText(Integer.toString(position+1)+" ");
        //play.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = sngList.get(position).getSongTitle();
                playSong(Uri.parse(sngList.get(position).getSongPath()));
            }
        });
        return rowview;
    }
    private void playSong(Uri sngUri)
    {
        try{
            if (mp.isPlaying())
            {
                mp.stop();
                //mp.release();
            }

            mp.reset();
            mp.setDataSource(String.valueOf(sngUri));
            mp.prepare();
            mp.start();
        } catch (IOException e){
            e.getMessage();
        }

    }
}