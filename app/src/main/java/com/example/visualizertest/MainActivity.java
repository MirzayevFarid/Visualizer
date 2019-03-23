package com.example.visualizertest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import com.squareup.gifencoder.GifEncoder;
//import com.squareup.gifencoder.ImageOptions;



/*
 * Copy test.mp3 to /res/raw/ folder
 *
 * needed in AndroidManifest.xml
 * android:minSdkVersion="9"
 * uses-permission of "android.permission.RECORD_AUDIO"
 *
 * reference: Android demo example -
 * ApiDemos > Media > AudioTx
 */

public class MainActivity extends AppCompatActivity {
    static final public String TAG = "TABAK";
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 9976;



    FloatingActionButton fab;
    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;
    public MusicWave musicWave;

    public Bitmap tempBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            initialise();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mVisualizer.release();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initialise() {
        musicWave = findViewById(R.id.musicWave);

        mMediaPlayer = MediaPlayer.create(this, R.raw.totk);
        prepareVisualizer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
                fab.setImageResource(android.R.drawable.ic_media_play);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status;
                if (mMediaPlayer.isPlaying()) {
                    status = "Sound Paused";
                    mMediaPlayer.pause();
                    fab.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    status = "Sound Started";
                    mVisualizer.setEnabled(true);
                    mMediaPlayer.start();
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                }
                Snackbar.make(view, status, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }

    private void prepareVisualizer() {
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        musicWave.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_color_solid) {
            Log.e("action_color_solid", "" + musicWave.getConfig().getColorGradient());
            musicWave.getConfig().setColorGradient(false);
            return true;
        }
        if (id == R.id.action_color_gradient) {
            Log.e("action_color_gradient", "" + musicWave.getConfig().getColorGradient());
            musicWave.getConfig().setColorGradient(true);
            return true;
        }
        if (id == R.id.action_sound_usage) {
            Snackbar.make(fab, "Sound used “Unrelenting” by Jay Man www.ourmusicbox.com", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initialise();
                } else {
                    Toast.makeText(MainActivity.this, "Allow Permission from settings", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

//    public  Bitmap loadBitmapFromView(View v) {
//        Bitmap bitmap;
//        v.setDrawingCacheEnabled(true);
//        bitmap = Bitmap.createBitmap(v.getDrawingCache());
//        v.setDrawingCacheEnabled(false);
//        return bitmap;
//    }
//
//    public void saveGifIntoSdCard(){
//        int[][] rgbDataFrame1 = ...;
//        int[][] rgbDataFrame2 = ...;
//        int[][] rgbDataFrame3 = ...;
//
//        OutputStream outputStream = new FileOutputStream("test.gif");
//        ImageOptions options = new ImageOptions();
//        new GifEncoder(outputStream, width, height, 0)
//                .addImage(rgbDataFrame1, options)
//                .addImage(rgbDataFrame2, options)
//                .addImage(rgbDataFrame3, options)
//                .finishEncoding();
//        outputStream.close();
//    }


}