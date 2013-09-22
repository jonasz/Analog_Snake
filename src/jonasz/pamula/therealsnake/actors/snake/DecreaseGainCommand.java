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

public class DecreaseGainCommand extends SnakeCommand {
    public DecreaseGainCommand(Snake snake_){
        super(snake_);
    }

    void perform(){
        snake.decreaseGain();
        performAt += 2 * 1000;
        snake.addCommand(this);
    }
}

