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

    float getSoundRate(){
        float gain = mBoard.mSnake.getGain();
        float ming = mBoard.mSnake.MIN_GAIN;
        float maxg = mBoard.mSnake.MAX_GAIN;
        float res = (gain - ming) / (maxg - ming); //now in [0,1]

        float minr = 0.7f;
        float maxr = 1.1f;
        return minr + (maxr-minr)*res;
    }

    public void eat(){
        super.eat();
        mBoard.sounds.playRate(mBoard.sounds.SONAR, getSoundRate());

        mBoard.updateScore(mBoard.mSnake.getGain() * mBoard.mSnake.getSpeed());
        mBoard.mSnake.increaseGain();
        mBoard.mSnake.increaseGain();

        mBoard.mSnake.grow();

        mBoard.addActor(new Explosion(mBoard, mPos, "red", 8.));

    }

    public void draw(Drawing d){
            d.putCircleOnBoard(mPos, getradius(), "red");
    }
}
