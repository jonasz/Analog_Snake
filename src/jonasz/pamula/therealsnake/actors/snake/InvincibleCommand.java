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

public class InvincibleCommand extends SnakeCommand {
    int mDelta;

    public InvincibleCommand(Snake snake_, int delta){
        super(snake_);
        mDelta = delta;
    }

    void perform(){
        snake.invincible += mDelta;
    }
}

