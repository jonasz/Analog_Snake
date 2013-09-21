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

public class Explosion extends Actor {
    static final int DISAPPEAR_AFTER = 1500;
    final long mCreatedAt = Utils.getTime();
    String mColor;
    Point mPos;

    public Explosion(Board board, Point coord_, String color){
        super(board);
        mPos = coord_;
        mColor = color;
    }

    public void update(){
        if(Utils.getTime() > mCreatedAt + DISAPPEAR_AFTER){
            remove();
        }
    }

    public void draw(Drawing d){
        long passed = Utils.getTime() - mCreatedAt;
        double len = (double)(passed)/DISAPPEAR_AFTER * 8.;
        int K = 9;
        for(int k=0; k<K; k++){
            double angle = 2.*Math.PI / K * k;
            Point p = new Point(len * Math.cos(angle), len * Math.sin(angle));
            p.add(mPos);
            //p.normalizeModulo(mBoard);
            d.putCircleOnBoard(p, 1, mColor);
        }
    }
}
