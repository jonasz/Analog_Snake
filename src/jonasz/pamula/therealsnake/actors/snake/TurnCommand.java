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

public class TurnCommand extends SnakeCommand {
    public static enum Direction {
        LEFT, RIGHT, STRAIGHT,
    }
    Direction dir;
    public TurnCommand(Snake snake_, Direction dir_){
        super(snake_);
        dir = dir_;
    }
    public void perform(){
        switch(dir){
            case LEFT: snake.turn(snake.TURN_ANGLE); break;
            case RIGHT: snake.turn(-snake.TURN_ANGLE); break;
            case STRAIGHT: snake.turn(0); break;
        }
    }
}
