package jonasz.pamula.therealsnake;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.Display;
import android.view.WindowManager;
import android.graphics.Point;

class SnakeThread extends Thread {
    private final int FPS = 20;
    private final int FRAME_DURATION_MS = 1000/FPS;

    private SurfaceHolder mSurfaceHolder;
    private boolean mRunning = true;
    private int mSurfaceWidth = -1;
    private int mSurfaceHeight = -1;
    private Board mBoard;

    public void stopRunning(){
        mRunning = false;
    }

    public SnakeThread(SurfaceHolder holder, Board board){
        mSurfaceHolder = holder;
        mBoard = board;
    }

    public void run() {
        while (mRunning) {
            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas(null);
                if(c != null) doDraw(c);
                tick();
            } finally {
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
        Log.i("snake", "Thread stopped");
    }

    private void doDraw(Canvas canvas) {
        canvas.drawARGB(255,0,0,0);
        mBoard.update();
        mBoard.draw(canvas);
    }

    private long lasttick = 0;
    private void tick(){
        long current = System.currentTimeMillis();
        long elapsed = current - lasttick;
        long duration = FRAME_DURATION_MS - elapsed;

        lasttick = Math.max(current, lasttick + FRAME_DURATION_MS);
        //Log.i("snake", "Last tick: " + (new Long(lasttick)).toString());

        if(duration > 0){
            try {
                Thread.sleep(duration);
            } catch(InterruptedException e){ }
        }
    }
}

class SnakeView extends SurfaceView implements SurfaceHolder.Callback {
    private SnakeThread mSnakeThread;
    private Board mBoard = new Board();
    private Point displaySize = new Point();

    private void obtainDisplaySize(Context ctx){
        WindowManager wm =
            (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getSize(displaySize);
    }

    public Board getBoard(){
        return mBoard;
    }

    public SnakeView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        obtainDisplaySize(ctx);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        setFocusable(true); // make sure we get key events
    }

    public SnakeThread getThread() {
        return mSnakeThread;
    }

    void pause(){
        mBoard.pause();
    }

    void unpause(){
        mBoard.unpause();
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        Log.i("snake", "onWindowFocusChanged");
    }

    // SurfaceHandler.Callback callbacks follow
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        mBoard.setSurfaceSize(width, height);
        Log.i("snake", "surfaceChanged");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("snake", "surfaceCreated");
        mSnakeThread = new SnakeThread(holder, mBoard);
        mSnakeThread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("snake", "surfaceDestroyed");
        mSnakeThread.stopRunning();
        while (true) {
            try {
                mSnakeThread.join();
                break;
            } catch (InterruptedException e) { }
        }
        mSnakeThread = null;
    }
}
