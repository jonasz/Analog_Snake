package jonasz.pamula.therealsnake.sounds;

//imports to be removed
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.Display;
import android.view.WindowManager;
import android.graphics.Point;
import jonasz.pamula.therealsnake.board.Board; //

import jonasz.pamula.therealsnake.Utils;
import jonasz.pamula.therealsnake.R;
import android.media.SoundPool;
import android.media.AudioManager;
import jonasz.pamula.therealsnake.MainActivity;
import java.util.Map;
import java.util.HashMap;

public class Sounds {
    private SoundPool sp = null;
    private Map<Integer,Integer> IDS = new HashMap<Integer, Integer>();

    public final int SONAR = 1;
    public final int GAMEOVER = 2;
    public final int NEWGAME = 3;
    public final int FANFARE = 4;
    public final int POP = 5;

    public void init(){
        Utils.log("Sounds.init()");
        if(sp!=null) finish();

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        
        IDS.put(SONAR, sp.load(MainActivity.getContext(), R.raw.sonar, 1));
        IDS.put(GAMEOVER, sp.load(MainActivity.getContext(), R.raw.gameover, 1));
        IDS.put(NEWGAME, sp.load(MainActivity.getContext(), R.raw.newgame, 1));
        IDS.put(FANFARE, sp.load(MainActivity.getContext(), R.raw.fanfare, 1));
        IDS.put(POP, sp.load(MainActivity.getContext(), R.raw.pop, 1));

        for(Map.Entry<Integer,Integer> e: IDS.entrySet()){
            Utils.log("IDS[ " + e.getKey().toString() +
                    " ] = " + e.getValue().toString());
        }
    }

    public void finish(){
        sp.release();
        sp = null;
    }

    public void play(int id){
        sp.play(
                IDS.get(id), //sound id
                1,  //left vol
                1,  //right vol
                0,  //priority
                0,  //loop
                1   //rate
               );
    }
}
