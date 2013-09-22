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
import jonasz.pamula.therealsnake.actors.snake.InvincibleCommand;
import jonasz.pamula.therealsnake.actors.snake.ColorCommand;
import jonasz.pamula.therealsnake.actors.snake.Snake;
import jonasz.pamula.therealsnake.actors.Actor;
import jonasz.pamula.therealsnake.drawing.Drawing;
import jonasz.pamula.therealsnake.sounds.Sounds;
import jonasz.pamula.therealsnake.Utils;

public class Banana extends Eatable {
    static final int DISAPPEAR_AFTER = 1500;
    static final int INVINCIBLE_FOR = 7 * 1000;

    public int getradius(){
        return 3;
    }

    public Banana(Board board, Point coord_){
        super(board, coord_);
    }

    public void eat(){
        super.eat();
        mBoard.sounds.play(mBoard.sounds.FANFARE);
        Utils.log("Banana eaten");

        //mBoard.addActor(new Explosion(mBoard, mPos, "yellow"));
        {
            int i = 0;
            for(Point p: mBoard.mSnake.getBody()){
                if(i%4==0) mBoard.addActor(new Explosion(mBoard, p, "yellow"));
                i++;
            }
        }

        Snake snake = mBoard.mSnake;
        snake.addCommand(new InvincibleCommand(snake,1));
        snake.addCommand(new ColorCommand(snake, "yellow"));

        //three green flashes before returning to normal
        long interval = 250;
        long t = INVINCIBLE_FOR - 8*interval;
        for(int i=0; i<4; i++){
             snake.addCommand(new ColorCommand(snake, "green").delay(t));
             t += interval;
             snake.addCommand(new ColorCommand(snake, "yellow").delay(t));
             t += interval;
        }

        snake.addCommand(new InvincibleCommand(snake,-1).delay(INVINCIBLE_FOR));
        snake.addCommand(new ColorCommand(snake, "green").delay(INVINCIBLE_FOR));
    }

    public void draw(Drawing d){
        double t = (Utils.getTime())%1000 * (2.*Math.PI / 1000.);
        double w = getradius() * (1.+0.3*Math.sin(t));
        double h = getradius() * (1.+0.3*Math.cos(t));
        d.putOvalOnBoard(mPos, w, h, "yellow");
    }
}
