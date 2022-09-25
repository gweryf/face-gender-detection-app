package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView myImage;
    private Button button;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    private static int Microphone_Permission_Code = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isMicrophonePresent()){
            getMicrophonePermission();
        }

        myImage = (ImageView) findViewById(R.id.myImage);
        button = (Button) findViewById(R.id.Button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

    }

    public void btnRecordPressed(View v){
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this, "Recording is started", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void btnStopPressed(View v){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(this, "Recording has been stopped", Toast.LENGTH_LONG).show();
    }

    public void btnPlayPressed(View v){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            Toast.makeText(this, "Recording is being played!", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isMicrophonePresent(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            return false;
        }
    }

    private void getMicrophonePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},Microphone_Permission_Code);
        }
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRecordingFile"+".mp3");
        return file.getPath();
    }

    private void takePicture() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap b = (Bitmap) data.getExtras().get("data");
            myImage.setImageBitmap(b);
        }
    }
}