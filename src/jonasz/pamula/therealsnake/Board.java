package jonasz.pamula.therealsnake;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.LinkedList;
import java.util.List;
import android.util.Log;

class Coordinate implements Cloneable {
    private double mX,mY;
    public double getX() { return mX; }
    public double getY() { return mY; }
    public Coordinate(double x_, double y_){
        mX = x_; mY = y_;
    }
    public void add(double dx, double dy){
        mX += dx; mY += dy;
    }
    public Coordinate clone(){
        return new Coordinate(mX, mY);
    }
}

class Snake {
    private LinkedList<Coordinate> mBody = new LinkedList<Coordinate>();
    private Board mBoard;
    private int mMaxSize = 100;
    private double mAngle = 0;

    List<Coordinate> getBody(){
        return mBody;
    }

    public Snake(Board board){
        mBoard = board;
        mBody.addFirst(new Coordinate(board.BOARD_WIDTH/2, board.BOARD_HEIGHT/2));
    }

    Coordinate getHead(){
        return mBody.getFirst();
    }

    private void move(Coordinate newHead){
        mBody.addFirst(newHead);
        while(mBody.size()>mMaxSize) mBody.removeLast();
    }

    void crawl(double units){
        //Log.i("snake", "update " + (new Long(deltaTimeMs)).toString());
        Coordinate newHead = getHead().clone();
        newHead.add(Math.cos(mAngle) * units, Math.sin(mAngle) * units);
        move(newHead);
    }

    void turn(double angle){
        mAngle += angle;
    }
}

class Board {
    static final int KEY_LEFT = 1;
    static final int KEY_RIGHT = 2;
    static final int KEY_UP = 3;
    static final int KEY_DOWN = 4;

    private final int STATE_PAUSED = 0;
    private final int STATE_READY = 1;
    private final int STATE_RUNNING = 2;

    static final double TURN_ANGLE = Math.PI/10;
    static final double UNITS_PER_MS = 0.05;

    static final double BOARD_WIDTH = 40;
    static final double BOARD_HEIGHT = 100;

    long mLastUpdated = System.currentTimeMillis();
    int mState;
    Snake mSnake;

    Board(){
        mSnake = new Snake(this);
        mState = STATE_READY;
    }

    void pause(){
        mState = STATE_PAUSED;
    }

    void unpause(){
        mState = STATE_READY;
    }

    void update(){
        long time = System.currentTimeMillis();
        long deltaTime = time - mLastUpdated;
        mLastUpdated = time;

        synchronized(this){
            if(mState!=STATE_RUNNING) return;
        }

        //Log.i("snake", "time passed: " + (new Float(deltaTime)).toString());
        mSnake.turn(angle_mul * TURN_ANGLE);
        mSnake.crawl(deltaTime * UNITS_PER_MS);
    }

    private int angle_mul = 0;
    public void handleKey(int dir, int action){
        Log.i("snake", "handleKey: " + (new Integer(dir)).toString() +
             (new Integer(action).toString()));
        synchronized(this){
            if(mState == STATE_READY){
                mLastUpdated = System.currentTimeMillis();
                mState = STATE_RUNNING;
            }

            if(dir == KEY_LEFT && action == KEY_DOWN) angle_mul--;
            if(dir == KEY_LEFT && action == KEY_UP) angle_mul++;
            if(dir == KEY_RIGHT && action == KEY_DOWN) angle_mul++;
            if(dir == KEY_RIGHT && action == KEY_UP) angle_mul--;
        }
    }

    /**************************** drawing ******************************/
    private int mSurfaceWidth = -1;
    private int mSurfaceHeight = -1;
    private double mScale = -1;

    public void setSurfaceSize(int width, int height) {
        synchronized (this) {
            mSurfaceWidth = width;
            mSurfaceHeight = height;
        }
        double scaleX = mSurfaceWidth / BOARD_WIDTH;
        double scaleY = mSurfaceHeight / BOARD_HEIGHT;
        mScale = Math.min(scaleX, scaleY);
        Log.i("snake", "mScale: " + (new Double(mScale)).toString());
    }

    void putCircle(Canvas canvas, Coordinate coord, double r){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255, 0, 255, 0);
        //Log.i("snake", "old x: " + (new Float(coord.getX())).toString());
        //Log.i("snake", "old y: " + (new Float(coord.getY())).toString());
        float x = (float)(coord.getX()*mScale);
        float y = (float)(coord.getY()*mScale);
        //Log.i("snake", "x: " + (new Float(x)).toString());
        //Log.i("snake", "y: " + (new Float(y)).toString());
        canvas.drawCircle(x,y, (float)r, paint);
    }

    void draw(Canvas canvas){
        Coordinate s = mSnake.getHead();
        putCircle(canvas, mSnake.getHead(), 20);

        for(Coordinate c: mSnake.getBody()){
            putCircle(canvas, c, 10);
        }
    }
}
