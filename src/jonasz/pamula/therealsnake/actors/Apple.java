package jonasz.pamula.therealsnake.actors;

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
import jonasz.pamula.therealsnake.drawing.Drawing;

public class Apple extends Actor {
    static final int DISAPPEAR_AFTER = 1500;

    public Point mPos;
    public final int RADIUS = 2;
    public boolean active = true;
    private long mEatenAt = 0;

    public void eat(){
        Utils.log("EAT");
        active = false;
        mEatenAt = Utils.getTime();
    }

    public Apple(Board board, Point coord_){
        super(board);
        mPos = coord_;
    }

    public void update(){
        if(!active){
            if(Utils.getTime() - mEatenAt > DISAPPEAR_AFTER){
                remove();
            }
        }
    }

    public void draw(Drawing d){
        if(active){
            d.putCircleOnBoard(mPos, 2, "red");
        } else {
            long passed = Utils.getTime() - mEatenAt;
            double len = (double)(passed)/DISAPPEAR_AFTER * 8.;
            int K = 9;
            for(int k=0; k<K; k++){
                double angle = 2.*Math.PI / K * k;
                Point p = new Point(len * Math.cos(angle), len * Math.sin(angle));
                p.add(mPos);
                //p.normalizeModulo(mBoard);
                d.putCircleOnBoard(p, 1, "red");
            }
        }
    }
}
