package jp.co.fujixerox.deviceman.service;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import javax.inject.Inject;

public class SoundEffectPlayer {

    private Context mContext;

    @Inject
    public SoundEffectPlayer(Context context){
        mContext = context;
    }

    public void play(int rawId){
        // SE再生
        MediaPlayer mediaPlayer = MediaPlayer.create(mContext, rawId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mediaPlayer){
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();
    }
}
