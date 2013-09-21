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

public class ColorCommand extends SnakeCommand {
    String mColor;

    public ColorCommand(Snake snake_, String color){
        super(snake_);
        mColor = color;
    }

    void perform(){
        snake.setColor(mColor);
    }
}

