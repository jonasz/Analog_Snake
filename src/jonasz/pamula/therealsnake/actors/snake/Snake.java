package jonasz.pamula.therealsnake.actors.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.List;
import jonasz.pamula.therealsnake.Utils;
import jonasz.pamula.therealsnake.board.Board;
import jonasz.pamula.therealsnake.board.Point;
import jonasz.pamula.therealsnake.actors.snake.SnakeCommand;
import jonasz.pamula.therealsnake.actors.Actor;
import jonasz.pamula.therealsnake.actors.Apple;
import jonasz.pamula.therealsnake.drawing.Drawing;

public class Snake extends Actor {
    public static final double MIN_SPEED = 4; //units per second
    public static final double MAX_SPEED = 20;
    public static final double SPEED_CHANGE_PER_SEC = (MAX_SPEED - MIN_SPEED)/6;
    public static final double TURN_ANGLE = Math.PI/8;
    public static final double ONE_UNIT = 4;
    public static final int MAX_SNAKE_SIZE = 500;
    public static final int MIN_SNAKE_SIZE = 20;
    public final int HEAD_RADIUS = 3;
    public final int TAIL_RADIUS = 2;
    public double mSpeed = MAX_SPEED;
    private LinkedList<Point> mBody = new LinkedList<Point>();
    private int mCurrentSize = MIN_SNAKE_SIZE;
    private double mAngle = 0;
    private double mAngleDelta = 0; //how angle changes at each step
    private boolean initiated = false;
    public boolean mMoving = true;

    private Queue<SnakeCommand>mCmdQueue = new PriorityQueue<SnakeCommand>();
    long mPausedAt = -1;
    public void pause(){
        mPausedAt = Utils.getTime();
    }

    public void unpause(){
        if(mPausedAt == -1) return;

        long now = Utils.getTime();
        long shift = now-mPausedAt;
        mPausedAt = -1;

        Utils.log("SHIFT: " + (new Long(shift)));
        for(SnakeCommand cmd: mCmdQueue){
            cmd.performAt += shift;
        }

        // prevent snake from slowing down after unpausing
        mLastSpeedAdjustment = Utils.getTime();
    }

    public int getSpeed(){
        if(!mMoving) return 0;
        int res = (int)(10. * mSpeed);
        return res;
    }

    public void addCommand(SnakeCommand cmd){
        mCmdQueue.offer(cmd);
    }

    public List<Point> getBody(){
        return mBody;
    }

    public Snake(Board board){
        super(board);
        mBody.addFirst(new Point(board.BOARD_WIDTH/2, board.BOARD_HEIGHT/2));
    }

    public Point getHead(){
        return mBody.getFirst();
    }

    public void init(){
        addCommand(new CrawlCommand(this)); //initiate the movement
        initiated = true;
    }

    public void update(){
        if(!initiated) init();
        long now = Utils.getTime();
        while(!mCmdQueue.isEmpty() && mCmdQueue.peek().performAt <= now){
            SnakeCommand cmd = mCmdQueue.poll();
            cmd.perform();
        }
    }

    public void slowDown(double delta){
        mSpeed -= delta;
        if(mSpeed < MIN_SPEED) {
            mSpeed = MIN_SPEED;
            mMoving = false;
        }
    }
    public void speedUp(double delta){
        mSpeed += delta;
        if(mSpeed > MAX_SPEED) mSpeed = MAX_SPEED;
        mMoving = true;
    }

    long mLastSpeedAdjustment = 0;
    public void adjustSpeed(){
        if(mLastSpeedAdjustment==0){
            mLastSpeedAdjustment = Utils.getTime();
            return;
        }

        long now = Utils.getTime();
        double seconds = (double)(now - mLastSpeedAdjustment)/1000.;
        mLastSpeedAdjustment = now;
        if(Math.abs(mAngleDelta) < Utils.EPS) {
            slowDown(seconds * SPEED_CHANGE_PER_SEC);
        } else {
            speedUp(seconds * SPEED_CHANGE_PER_SEC);
        } //*/
    }

    public void crawlOneUnit(){
        mAngle += mAngleDelta;
        Point newHead = new Point(getHead());
        newHead.add(Math.cos(mAngle)*ONE_UNIT, -Math.sin(mAngle)*ONE_UNIT);
        newHead.normalizeModulo(mBoard);
        mBody.addFirst(newHead);
        while(mBody.size()>mCurrentSize) mBody.removeLast();
    }

    public void grow(){
        mCurrentSize+=5;
        if(mCurrentSize > MAX_SNAKE_SIZE) mCurrentSize = MAX_SNAKE_SIZE;
    }

    public boolean headCollides(Apple a){
        return a.mPos.dist(getHead()) <= HEAD_RADIUS + a.RADIUS;
    }

    public boolean headIntersectsTail(){
        int collisions = 0;
        for(Point c: getBody()){
            if(c.dist(getHead()) <= TAIL_RADIUS + HEAD_RADIUS - 0.1) {
                collisions++;
            }
        }
        return collisions > 2; //head always collides with the 'neck'
    }

    public boolean collides(Apple a){
        if(headCollides(a)) return true;

        for(Point c: getBody()){
            if(a.mPos.dist(c) <= TAIL_RADIUS + a.RADIUS) return true;
        }
        return false;
    }

    public void turn(double angle){
        mAngleDelta = angle;
        Utils.log("TURNING " + (new Double(angle)));
    }

    public void draw(Drawing d){
        d.putCircleOnBoard(getHead(), HEAD_RADIUS, "red");

        for(Point c: getBody()){
            d.putCircleOnBoard(c, TAIL_RADIUS, "green");
        }
    }
}
