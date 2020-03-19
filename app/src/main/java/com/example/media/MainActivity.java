package com.example.media;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageView imgIcon;
    ImageButton btnPrev, btnPlay, btnStop, btnNext;
    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mapping();
        addSong();

        btnPlay.setImageResource(R.drawable.pause);
        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        initMediaPlayer();

        ImageButton btnOnline = (ImageButton) findViewById(R.id.imageButtonOnline);
        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://mp3.zing.vn/bai-hat/Hon-Ca-Yeu-Duc-Phuc/ZWB0DFWZ.html";
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position < 0) {
                    position = arraySong.size() - 1;
                }
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                initMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.play);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position + 1 > arraySong.size()) {
                    position = 0;
                }
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                initMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.play);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
//                    mediaPlayer.prepareAsync();
                    btnPlay.setImageResource(R.drawable.play);
                    initMediaPlayer();
                }
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.prepareAsync();
                    btnPlay.setImageResource(R.drawable.pause);
                }else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.play);
                }
            }
        });
        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat temp = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(temp.format(mediaPlayer.getCurrentPosition()));
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if(position + 1 > arraySong.size()) {
                            position = 0;
                        }
                        if(mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        initMediaPlayer();
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.play);
                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);
    }
    private void setTimeTotal() {
        SimpleDateFormat temp = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(temp.format(mediaPlayer.getDuration()));
        skSong.setMax(mediaPlayer.getDuration());
        updateTimeSong();
        imgIcon.startAnimation(animation);
    }
    private  void initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
        setTimeTotal();
    }
    private void addSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Gat di nuoc mat", R.raw.gat_di_nuoc_mat));
        arraySong.add(new Song("Love", R.raw.love));
        arraySong.add(new Song("Su that sau mot loi hua", R.raw.su_that_sau_mot_loi_hua));
    }

    private void mapping() {
        txtTimeSong = (TextView) findViewById(R.id.textViewTimeSong);
        txtTimeTotal = (TextView) findViewById(R.id.textViewTimeSongTotal);
        txtTitle = (TextView) findViewById(R.id.textViewTitle);

        skSong = (SeekBar) findViewById(R.id.seekBar);

        imgIcon = (ImageView) findViewById(R.id.imageViewIcon);

        btnPrev = (ImageButton) findViewById(R.id.imageButtonPre);
        btnNext = (ImageButton) findViewById(R.id.imageButtonNext);
        btnPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
        btnStop = (ImageButton) findViewById(R.id.imageButtonStop);
    }
}
