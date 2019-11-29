package com.example.muisicplayerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.muisicplayerproject.Utils.list;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer=new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play=(Button)findViewById(R.id.bt_play);
        play.setOnClickListener(this);
        Button pause=(Button)findViewById(R.id.bt_pause);
        pause.setOnClickListener(this);
        Button stop=(Button)findViewById(R.id.bt_stop);
        stop.setOnClickListener(this);
        list = new ArrayList<>();
        list = Utils.getmusic(this);//获取音乐列表
        ListAdapter adapter=new ListAdapter(MainActivity.this,list);//设置List适配器
        ListView listView=(ListView)findViewById(R.id.list_song);
        listView.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String []{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }else{
//            initMediaPlayer();
        }

        //List点击监听
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                currentPosition=position;
//                musicPlay(currentPosition);//播放音乐
//                ListAdapter.changeSelected(currentPosition);//设置字体变色
//                play.setVisibility(View.INVISIBLE);//播放按钮消失
//                pause.setVisibility(View.VISIBLE);//暂停按钮出现
//                String song=list.get(currentPosition).song;//获取音乐名
//                String singer=list.get(currentPosition).singer;//获取歌手名
//                bottomSong.setText(song);//底栏显示音乐名
//                bottomSinger.setText(singer);//底栏显示歌手名
//                gang.setVisibility(View.VISIBLE);//横杠
//
//            }
//        });

    }
    //播放音乐
//    private void musicPlay(int currentPosition) {
//        seekBar.setMax(list.get(currentPosition).getDuration());
//        try {
//            // 重置音频文件，防止多次点击会报错
//            mediaPlayer.reset();
//            //调用方法传进播放地址
//            mediaPlayer.setDataSource(list.get(currentPosition).getPath());
//            //异步准备资源，防止卡顿
//            mediaPlayer.prepareAsync();
//            //调用音频的监听方法，音频准备完毕后响应该方法进行音乐播放
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    mediaPlayer.start();//开始播放
//                    Thread thread = new Thread(new SeekBarThread());//更新SeekBar的线程
//                    thread.start();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_play:
                if(!mediaPlayer.isPlaying()){
                    Log.d("kangon","onClick/Play");

                    mediaPlayer.start();
                }
                break;
            case R.id.bt_pause:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.bt_stop:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
    }
    private void initMediaPlayer(){
        Log.d("kangon","真实/init");
        try{
//            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.music);
//            File file=new File(Environment.getExternalStorageDirectory(),"music.mp3");
//            Utils mu=new Utils();
            List<Song> list=Utils.getmusic(MainActivity.this);
            mediaPlayer.setDataSource(list.get(0).getPath());
            mediaPlayer.prepareAsync();
            if(mediaPlayer!=null){
                Log.d("kangon","not nuull init play music");
            }
        }catch (IOException e){
            Log.d("kangon","Exception/init");
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else{
                    finish();
                }
                break;
            default:

        }
    }

    @Override
    protected void onDestroy() {//调用方法销毁mediaPlayer
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
