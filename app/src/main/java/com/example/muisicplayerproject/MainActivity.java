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
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.muisicplayerproject.Utils.list;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer=new MediaPlayer();//播放器
    private int currentPosition;//当前List位置
    private ListAdapter adapter=null;//设置List适配器
    private int tag=0;//播放次序控制符（0为顺序，1为随机）
    Button play;
    Button pause;
    Button circleTag;
    Button randomTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pre=(Button)findViewById(R.id.bt_pre);
        pre.setOnClickListener(this);
        play=(Button)findViewById(R.id.bt_play);
        pause=(Button)findViewById(R.id.bt_pause);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        Button next=(Button)findViewById(R.id.bt_next);
        next.setOnClickListener(this);
        circleTag=(Button)findViewById(R.id.bt_circle);
        circleTag.setOnClickListener(this);
        randomTag=(Button)findViewById(R.id.bt_random);
        randomTag.setOnClickListener(this);
        randomTag.setVisibility(View.INVISIBLE);
        SeekBar seekBar=(SeekBar)findViewById(R.id.seekBar);//还没写


        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String []{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }else{

//            initMediaPlayer();
        }
        list = new ArrayList<>();
        list = Utils.getmusic(this);//获取音乐列表
        adapter=new ListAdapter(MainActivity.this,list);//设置List适配器
        final ListView listView=(ListView)findViewById(R.id.list_song);
        listView.setAdapter(adapter);

        //List点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition=position;
                musicPlay(currentPosition);
                adapter.changeSelected(currentPosition);
            }
        });
        //完成后自动下一曲
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(tag==0){
                    //顺序播放时
                    currentPosition++;
                    if(currentPosition>list.size()-1){
                        currentPosition=0;
                    }
                }else if(tag==1){
                    //随机播放
                    Random r=new Random();
                    currentPosition=r.nextInt(list.size());
                }

                musicPlay(currentPosition);
                adapter.changeSelected(currentPosition);
            }
        });
    }
    //播放音乐
    private void musicPlay(int currentPosition){
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(list.get(currentPosition).getPath());
            mediaPlayer.prepareAsync();
            //准备好后启动
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //点击事件
    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.bt_pre:
                currentPosition--;
                if (currentPosition > list.size() - 1) {
                    currentPosition = 0;
                }
                musicPlay(currentPosition);
                adapter.changeSelected(currentPosition);
                break;
            case R.id.bt_play:
                if(!mediaPlayer.isPlaying()){
                    play.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);
                    Log.d("kangon","onClick/Play");
                    mediaPlayer.start();
                }
                break;
            case R.id.bt_pause:
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.bt_next:
                if(tag==0){
                    currentPosition++;
                    if (currentPosition > list.size() - 1) {
                        currentPosition = 0;
                    }
                }else if(tag==1){
                    //随机播放
                    Random r=new Random();
                    currentPosition=r.nextInt(list.size());
                }


                musicPlay(currentPosition);
                adapter.changeSelected(currentPosition);
                break;
            case R.id.bt_random:
                circleTag.setVisibility(View.VISIBLE);
                randomTag.setVisibility(View.INVISIBLE);

                //点了随机后切换到顺序模式
                tag=0;
                break;
            case R.id.bt_circle:
                circleTag.setVisibility(View.INVISIBLE);
                randomTag.setVisibility(View.VISIBLE);

                //点了顺序后切换到随机模式
                tag=1;
                break;
            default:
                break;
        }
    }

    //前朝余孽
//    private void initMediaPlayer(){
//        try{
////            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.music);
////            File file=new File(Environment.getExternalStorageDirectory(),"music.mp3");
////            Utils mu=new Utils();
//            List<Song> list=Utils.getmusic(MainActivity.this);
//            mediaPlayer.setDataSource(list.get(0).getPath());
//            mediaPlayer.prepareAsync();
//            if(mediaPlayer!=null){
//                Log.d("kangon","not nuull init play music");
//            }
//        }catch (IOException e){
//            Log.d("kangon","Exception/init");
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch(requestCode){
//            case 1:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    initMediaPlayer();
//                }else{
//                    finish();
//                }
//                break;
//            default:
//
//        }
//    }

    @Override
    protected void onDestroy() {//调用方法销毁mediaPlayer
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
