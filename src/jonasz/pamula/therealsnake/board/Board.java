package jonasz.pamula.therealsnake.board;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.LinkedList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.List;
import jonasz.pamula.therealsnake.Utils;
import jonasz.pamula.therealsnake.actors.snake.Snake;
import jonasz.pamula.therealsnake.actors.Apple;
import jonasz.pamula.therealsnake.actors.snake.TurnCommand;
import jonasz.pamula.therealsnake.board.Point;
import jonasz.pamula.therealsnake.drawing.Drawing;
import jonasz.pamula.therealsnake.actors.Actor;
import java.util.Set;
import java.util.TreeSet;

public class Board {
    public static final int STATE_PAUSED = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_RUNNING = 2;
    public static final int STATE_GAME_OVER_WAITING = 3;
    public static final int STATE_GAME_OVER = 4;
    public int mState;

    public static final double BOARD_WIDTH = 160;
    public static final double BOARD_HEIGHT = 120;

    private Drawing mDrawing;

    long mLastUpdated = Utils.getTime();
    public Snake mSnake;
    public int mScore = 0;

    private Set<Actor> mActors = new TreeSet<Actor>();
    public Actor[] getActors(){
        return mActors.toArray(new Actor[1]);
    }

    public void addActor(Actor a){
        mActors.add(a);
    }

    public void removeActor(Actor a){
        mActors.remove(a);
    }

    public Board(){
        mSnake = new Snake(this);
        addActor(mSnake);
        mState = STATE_READY;
        addRandomApple();
        mDrawing = new Drawing(this);
    }

    public synchronized void pause(){
        mState = STATE_PAUSED;
        mSnake.pause();
    }

    public synchronized void unpause(){
        mState = STATE_READY;
        // mSnake.unpause() will be called when the screen is touched
    }

    long mGameOverTime = -1;
    public synchronized void update(){
        long time = Utils.getTime();
        long deltaTime = time - mLastUpdated;
        mLastUpdated = time;

        if(mState == STATE_GAME_OVER_WAITING){
            if(Utils.getTime()-mGameOverTime > 2000) mState = STATE_GAME_OVER;
            return;
        }

        if(mState!=STATE_RUNNING) return;

        if(mSnake.headIntersectsTail()){
            pause();
            mState = STATE_GAME_OVER_WAITING;
            mGameOverTime = Utils.getTime();
            Utils.log("Game over");
            return;
        }

        for(Actor a: getActors()){
            a.update();
            if(a instanceof Apple){
                Apple apple = (Apple)a;
                if(apple.active && mSnake.collides(apple)){
                    Utils.log("BD go ZJAD");
                    apple.eat();
                    mScore+=mSnake.getSpeed();
                    mSnake.grow();
                    addRandomApple();
                }
            }
        }
    }
    // input handling
    public static final int KEY_LEFT = 1;
    public static final int KEY_RIGHT = 2;
    public static final int KEY_UP = 3;
    public static final int KEY_DOWN = 4;

    private int angle_mul = 0;
    public synchronized void handleUserInput(int dir, int action){
        Utils.log("handleKey: " + (new Integer(dir)).toString() +
                (new Integer(action).toString()));

        if(mState == STATE_READY){
            if(action == KEY_UP) return;

            mLastUpdated = Utils.getTime();
            mState = STATE_RUNNING;
            mSnake.unpause();
        }

        if(dir == KEY_LEFT && action == KEY_DOWN) angle_mul--;
        if(dir == KEY_LEFT && action == KEY_UP) angle_mul++;
        if(dir == KEY_RIGHT && action == KEY_DOWN) angle_mul++;
        if(dir == KEY_RIGHT && action == KEY_UP) angle_mul--;

        TurnCommand.Direction tdir;
        switch(angle_mul){
            case -1: tdir = TurnCommand.Direction.LEFT; break;
            case 1: tdir = TurnCommand.Direction.RIGHT; break;
            default:
            case 0: tdir = TurnCommand.Direction.STRAIGHT; break;
        }
        mSnake.addCommand(new TurnCommand(mSnake, tdir));
    }

    public void addRandomApple(){
        while(true){
            double x = Utils.randomDouble(0, BOARD_WIDTH);
            double y = Utils.randomDouble(0, BOARD_HEIGHT);
            Apple a = new Apple(this, new Point(x,y));
            if(!mSnake.collides(a)){
                addActor(a);
                break;
            }
        }
    }

    // drawing - delegation
    public synchronized void setSurfaceSize(int width, int height) {
        mDrawing.setSurfaceSize(width, height);
    }

    public void draw(Canvas canvas){
        mDrawing.draw(canvas);
    }
}
