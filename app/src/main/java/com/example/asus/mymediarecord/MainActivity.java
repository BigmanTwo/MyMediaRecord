package com.example.asus.mymediarecord;


import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnPreparedListener{
    private MediaRecorder mediaRecorder;
    private SurfaceView surfaceView;
    private String filePath;
    private Camera camera;
    private Button mButton1,mButton2,mButton3,mButton4;
    private MediaPlayer mediaPlayer;
    private boolean b=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton1=(Button)findViewById(R.id.play_but);
        mButton2=(Button)findViewById(R.id.start_record);
        mButton3=(Button)findViewById(R.id.stop_record);
        mButton4=(Button)findViewById(R.id.stop_play);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        surfaceView=(SurfaceView)findViewById(R.id.screen_view);

        filePath= Environment.getExternalStorageDirectory().getAbsoluteFile()+"/录制2.mp4";
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            //在控件穿件的时候给mediaRecorder和camera初始化
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                camera=Camera.open();
                mediaRecorder=new MediaRecorder();
            }
            //当控件 发生变化时调用
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //自定义镜头方向
//                try {
//                    camera.setPreviewDisplay(holder);
//                    camera.setDisplayOrientation(getDegree(MainActivity.this));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            camera.startPreview();
            }
        });
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setKeepScreenOn(true);
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setDataSource(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void recordStart(){
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //调用相机
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //设置格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //存放路径
        mediaRecorder.setOutputFile(filePath);
        //设置文件编辑方式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }
    private int getDegree(Activity activity){
        int rotation=activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree=0;
        switch (rotation){
            case Surface.ROTATION_0:
                degree=90;
                break;
            case Surface.ROTATION_90:
                degree=0;
                break;
            case Surface.ROTATION_180:
                degree=270;
                break;
            case Surface.ROTATION_270:
                degree=180;
                break;
        }
        return degree;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_but:
                setPlayer();
                break;
            case R.id.start_record:
                recordStart();
                break;
            case R.id.stop_record:
                mediaRecorder.release();
                camera.release();
                mediaRecorder=null;
                break;
            case R.id.stop_play:
                break;

        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
    private void setPlayer(){
        if(b){
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepareAsync();
            b=false;
        }
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
//                if ()
            }
        };
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();

        super.onDestroy();
    }
}
