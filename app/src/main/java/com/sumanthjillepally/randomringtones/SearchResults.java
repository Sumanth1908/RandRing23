package com.sumanthjillepally.randomringtones;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ListView;

import java.util.ArrayList;

import static android.Manifest.*;

public class SearchResults extends AppCompatActivity {
    private ArrayList<SongData> songList = new ArrayList<SongData>();
    ListView list;
    public View mLayout;
    private static final int PERMISSION_REQUEST_READ_STORAGE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        mLayout = findViewById(R.id.list_for_search);
        permissionCheck();
        CustomAdapter listAdapt = new CustomAdapter(SearchResults.this, R.layout.custom_adapter,songList);
        setContentView(R.layout.search_results);
        list = (ListView) findViewById(R.id.list_for_search);
        list.setAdapter(listAdapt);
    }

    public class SongData {
        public long getSongID() {
            return songID;
        }

        public String getSongTitle() {
            return songTitle;
        }

        private long songID;
        private String songTitle;

        public String getSongPath() {
            return songPath;
        }

        private String songPath;

        private SongData(long Id, String Title, String SongPath) {
            songID = Id;
            songTitle = Title;
            songPath = SongPath;
        }

    }

public void permissionCheck() {

    if (ActivityCompat.checkSelfPermission(SearchResults.this, permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
        getSongsList();
    } else {
        requestPermission();
        }
}

public void requestPermission()
{
    if (ActivityCompat.shouldShowRequestPermissionRationale(SearchResults.this,
            permission.READ_EXTERNAL_STORAGE)) {
        // Provide an additional rationale to the user if the permission was not granted
        // and the user would benefit from additional context for the use of the permission.
        // Display a SnackBar with a button to request the missing permission.
        Snackbar.make(mLayout, "Storage access is required to read files.",
                Snackbar.LENGTH_INDEFINITE).setAction("ALLOW", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the permission
                ActivityCompat.requestPermissions(SearchResults.this,
                        new String[]{permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_READ_STORAGE);
            }
        }).show();

    } else {
        Snackbar.make(mLayout,"Permission is not available. Requesting Read permission.",Snackbar.LENGTH_SHORT).show();
        // Request the permission. The result will be received in onRequestPermissionResult().
        ActivityCompat.requestPermissions(SearchResults.this, new String[]{permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_READ_STORAGE);
    }
}
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_READ_STORAGE) {
            // Request for Storage permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.
                getSongsList();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Storage request was denied.", Snackbar.LENGTH_SHORT).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

public void getSongsList(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sel = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String ext = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
        String[] selExtARGS = new String[]{ext};
        Cursor musicCursor = musicResolver.query(musicUri, null, sel, selExtARGS, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int songDataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            //int column_index = musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            //add songs to list
            do {
                long id = musicCursor.getLong(idColumn);
                String title = musicCursor.getString(titleColumn);
                String sngData = musicCursor.getString(songDataColumn);
                songList.add(new SongData(id, title, sngData));
            }
            while (musicCursor.moveToNext());

        }
        musicCursor.close();
    }

}

