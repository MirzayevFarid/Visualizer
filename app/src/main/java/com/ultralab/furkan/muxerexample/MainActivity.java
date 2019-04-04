package com.ultralab.furkan.muxerexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;

//import

public class MainActivity extends AppCompatActivity {

    FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ffmpeg = FFmpeg.getInstance(this);

        //copyFiletoExternalStorage(R.drawable.example, "/sdcard/F3211/Download/giphy.gif");

        boolean a = ffmpeg.isSupported();

        String command = "-i /sdcard/enhance.gif -movflags faststart -pix_fmt yuv420p -vf scale=400*350 /sdcard/enhance.mp4";  //convert gif to mp4
//        String command = "-i /sdcard/deneme.wav -acodec libmp3lame /sdcard/audio.mp3";  //convert wav to mp3
//        String command = "-i /sdcard/Download/couchplayin.wav -acodec libmp3lame /sdcard/Download/audio.mp3";  //convert wav to mp3
//        String command = "-i /sdcard/out.mp4 -i /sdcard/audio.mp3 -map 0:v -map 1:a /sdcard/output_video.mp4";  //mux mp3 and mp4
        String[] commands = command.split(" ");
        execFFmpegBinary(commands);

    }

//    private void loadFFMpegBinary() {
//        try {
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onFailure() {
//                    showUnsupportedExceptionDialog();
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
//        }
//    }

    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Error")
                .setMessage("Hata!")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();

    }

    private void execFFmpegBinary(final String[] command) {
        ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
            @Override
            public void onFailure(String s) {
//                    addTextViewToLayout("FAILED with output : "+s);
                Toast.makeText(MainActivity.this, "Bir sıkıntı var şefim", Toast.LENGTH_LONG).show();

            }



            @Override
            public void onSuccess(String s) {
                Toast.makeText(MainActivity.this, "Afferin len", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(String s) {
                Log.d("Converting: ", "Started command : ffmpeg "+command);
            }

            @Override
            public void onStart() {
//                    outputLayout.removeAllViews();
//
//                    Log.d(TAG, "Started command : ffmpeg " + command);
//                    progressDialog.setMessage("Processing...");
//                    progressDialog.show();
            }

            @Override
            public void onFinish() {
                Log.d("Finish", "Finished command : ffmpeg "+command);
//                    progressDialog.dismiss();
            }
        });

    }

    private void copyFiletoExternalStorage(int resourceId, String path_to_copy){
        InputStream in = getResources().openRawResource(resourceId);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path_to_copy);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
