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
import jonasz.pamula.therealsnake.actors.snake.ShortenCommand;
import jonasz.pamula.therealsnake.actors.snake.Snake;
import jonasz.pamula.therealsnake.actors.Actor;
import jonasz.pamula.therealsnake.drawing.Drawing;
import jonasz.pamula.therealsnake.sounds.Sounds;
import jonasz.pamula.therealsnake.Utils;

public class Peach extends Eatable {
    static final int DISAPPEAR_AFTER = 1500;
    static final int INVINCIBLE_FOR = 7 * 1000;
    long createdAt = 0;

    public int getradius(){
        return 3;
    }

    public Peach(Board board, Point coord_){
        super(board, coord_);
        createdAt = Utils.getTime();
    }

    public void eat(){
        super.eat();
        Utils.log("Peach eaten");

        mBoard.addActor(new Explosion(mBoard, mPos, "pink", 8.));

        int t = 250;
        for(int i=0; i<10; i++){
            mBoard.mSnake.addCommand(new ShortenCommand(mBoard.mSnake).delay(t));
            t += 250;
        }
    }

    public void draw(Drawing d){
        double t = (Utils.getTime() - createdAt)%1000 * (2.*Math.PI / 1000.);
        double w = getradius() * (1.+0.2*Math.sin(t));
        double h = w;
        d.putOvalOnBoard(mPos, w, h, "pink");
    }
}
