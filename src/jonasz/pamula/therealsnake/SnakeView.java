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
import android.view.MotionEvent;
import android.view.Display;
import android.view.WindowManager;
import android.graphics.Point;
import jonasz.pamula.therealsnake.board.Board;

class SnakeThread extends Thread {
    private final int FPS = 30;
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

    public void setBoard(Board board){
        synchronized(mBoard){
            mBoard = board;
        }
    }

    public void run() {
        Utils.log("Thread started");
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
        Utils.log("Thread stopped");
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
        //Utils.log("Last tick: " + (new Long(lasttick)).toString());

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
        Utils.log("onWindowFocusChanged");
    }

    // SurfaceHandler.Callback callbacks follow
    private int mSurfaceWidth = -1, mSurfaceHeight = -1;
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mBoard.setSurfaceSize(mSurfaceWidth, mSurfaceHeight);
        Utils.log("surfaceChanged");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Utils.log("surfaceCreated");
        mSnakeThread = new SnakeThread(holder, mBoard);
        mSnakeThread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Utils.log("surfaceDestroyed");
        mSnakeThread.stopRunning();
        while (true) {
            try {
                mSnakeThread.join();
                Utils.log("thread join succeeded");
                break;
            } catch (InterruptedException e) { }
        }
        mSnakeThread = null;
    }

    public void handleUserInput(int dir, int action){
        synchronized(mBoard){
            if(mBoard.mState == Board.STATE_GAME_OVER){
                if(action == Board.KEY_DOWN) {
                    mBoard = new Board();
                    mBoard.setSurfaceSize(mSurfaceWidth, mSurfaceHeight);
                    mSnakeThread.setBoard(mBoard);
                }
            } else {
                mBoard.handleUserInput(dir, action);
            }
        }
    }
}
