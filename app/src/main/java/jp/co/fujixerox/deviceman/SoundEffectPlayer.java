package jp.co.fujixerox.deviceman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by TATSUYA-PC4 on 2016/07/18.
 */

public class SoundEffectPlayer {
    private Context mContext;

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
