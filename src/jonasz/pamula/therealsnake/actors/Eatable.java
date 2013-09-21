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

public abstract class Eatable extends Actor {
    //after being eaten, will be displayed for DISAPPEAR_AFTER ms more

    public Point mPos;
    public boolean active = true;

    public abstract int getradius();

    public void eat(){
        Utils.log("EAT");
        remove();
    }

    public Eatable(Board board, Point coord_){
        super(board);
        mPos = coord_;
    }

    public void setPosition (Point coord_){
        mPos = coord_;
    }

    public void update(){
    }

    public abstract void draw(Drawing d);
}
