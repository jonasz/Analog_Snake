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
import jonasz.pamula.therealsnake.sounds.Sounds;

public class FallingStar extends Actor {
    static final int DISAPPEAR_AFTER = 3500;
    final long mCreatedAt = Utils.getTime();
    String mColor;
    Point mPos;
    double mRadius;

    public FallingStar(Board board, Point coord_, String color, double radius){
        super(board);
        mPos = coord_;
        mColor = color;
        mRadius = radius;
    }

    public void update(){
        if(Utils.getTime() > mCreatedAt + DISAPPEAR_AFTER){
            remove();
        }
    }

    public void draw(Drawing d){
        long passed = Utils.getTime() - mCreatedAt;
        double LEN = Math.sin((double)(passed)/DISAPPEAR_AFTER * Math.PI) * mRadius;

        int K = 10;
        for(int k=0; k<K; k++){
            double angle = 2.*Math.PI / K * k;
            angle += (double)passed / DISAPPEAR_AFTER * 4 * Math.PI;

            double len = LEN;
            if(k%2==0) len *= 0.7;
            Point p = new Point(len * Math.cos(angle), len * Math.sin(angle));
            p.add(mPos);

            d.putLineOnBoard(mPos, p, "gray");
        }
    }
}
