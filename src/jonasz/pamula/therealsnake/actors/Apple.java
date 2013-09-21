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

public class Apple extends Eatable {
    static final int DISAPPEAR_AFTER = 1500;

    public int getradius(){
        return 3;
    }

    public Apple(Board board, Point coord_){
        super(board, coord_);
    }

    public void eat(){
        super.eat();
        mBoard.sounds.play(mBoard.sounds.SONAR);

        mBoard.updateScore(mBoard.mSnake.getSpeed());
        mBoard.mSnake.grow();

        mBoard.addActor(new Explosion(mBoard, mPos, "red"));
    }

    public void draw(Drawing d){
            d.putCircleOnBoard(mPos, getradius(), "red");
    }
}
