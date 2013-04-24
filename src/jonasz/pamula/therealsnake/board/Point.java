package jonasz.pamula.therealsnake.board;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.List;
import android.util.Log;
import jonasz.pamula.therealsnake.Utils;
import jonasz.pamula.therealsnake.actors.snake.Snake;
import jonasz.pamula.therealsnake.actors.snake.TurnCommand;
import jonasz.pamula.therealsnake.board.Board;

public class Point implements Cloneable {
    private double mX,mY;
    public double getX() { return mX; }
    public double getY() { return mY; }
    public Point(double x_, double y_){
        mX = x_; mY = y_;
    }
    public Point add(double dx, double dy){ mX += dx; mY += dy; return this; }
    public Point add(Point p){ return add(p.mX, p.mY); }
    public Point(Point x){
        mX = x.getX(); mY = x.getY();
    }
    public double dist(Point p){
        double x = mX - p.mX;
        double y = mY - p.mY;
        return Math.sqrt(x*x + y*y);
    }

    public void normalizeModulo(double width, double height){
        while(mX < -Utils.EPS) mX += width;
        while(mX > width+Utils.EPS) mX -= width;
        while(mY < -Utils.EPS) mY += height;
        while(mY > height+Utils.EPS) mY -= height;
    }
    public void normalizeModulo(Board b){
        normalizeModulo(b.BOARD_WIDTH, b.BOARD_HEIGHT);
    }
}

