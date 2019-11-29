package com.example.muisicplayerproject;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.zip.Inflater;


public class ListAdapter extends BaseAdapter {
    Context context;
    List<Song> list;
    LayoutInflater inflater=null;
    private int mSelect;
    public ListAdapter(MainActivity mainActivity,List<Song> list){
        this.context=mainActivity;
        this.list=list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myholder myholder;
        if(convertView==null){
            myholder=new Myholder();
            convertView = inflater.inflate(R.layout.music_info, null);//list设置layout
            //初始化music_info格式
            myholder.t_song = convertView.findViewById(R.id.t_song);
            TextPaint paint =  myholder.t_song.getPaint();
            paint.setFakeBoldText(true);//歌名字体加粗
            myholder.t_singer = convertView.findViewById(R.id.t_singer);
            myholder.t_blank=convertView.findViewById(R.id.t_blank);
            myholder.t_albumn=convertView.findViewById(R.id.t_albumn);
            myholder.t_point=convertView.findViewById(R.id.t_point);
            myholder.t_duration = convertView.findViewById(R.id.t_duration);
            convertView.setTag(myholder);
        }else {
            myholder = (Myholder) convertView.getTag();
        }
        myholder.t_song.setText(list.get(position).song.toString());
        myholder.t_singer.setText(list.get(position).singer.toString());
        myholder.t_albumn.setText(list.get(position).album.toString());
        String time = Utils.formatTime(list.get(position).duration);
        myholder.t_duration.setText(time);
        if (mSelect == position) {
            myholder.t_song.setEnabled(true);
            myholder.t_singer.setEnabled(true);
            myholder.t_blank.setVisibility(View.VISIBLE);
            //选中项背景
            myholder.t_song.setTextColor(ContextCompat.getColor(context,R.color.aliceblue));
            myholder.t_singer.setTextColor(ContextCompat.getColor(context,R.color.aliceblue));
            myholder.t_albumn.setTextColor(ContextCompat.getColor(context,R.color.aliceblue));
            myholder.t_point.setTextColor(ContextCompat.getColor(context,R.color.aliceblue));
            myholder.t_duration.setTextColor(ContextCompat.getColor(context,R.color.aliceblue));
        } else {
            myholder.t_song.setEnabled(false);
            myholder.t_singer.setEnabled(false);
            myholder.t_blank.setVisibility(View.INVISIBLE);
            //其他项背景
            myholder.t_song.setTextColor(ContextCompat.getColor(context,R.color.greenyellow));
            myholder.t_singer.setTextColor(ContextCompat.getColor(context,R.color.green));
            myholder.t_albumn.setTextColor(ContextCompat.getColor(context,R.color.green));
            myholder.t_point.setTextColor(ContextCompat.getColor(context,R.color.green));
            myholder.t_duration.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        return convertView;
    }
    class Myholder {
        ImageView t_blank;
        TextView t_position, t_song, t_singer, t_duration,t_albumn,t_point;
    }
    public void changeSelected(int positon){
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
    }
}
