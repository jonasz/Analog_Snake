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
import jonasz.pamula.therealsnake.actors.Explosion;

public class ShortenCommand extends SnakeCommand {
    public ShortenCommand(Snake snake_){
        super(snake_);
    }

    void perform(){
        Point p = snake.getTail();
        Board board = snake.mBoard;
        if(snake.shorten()){
            board.addActor(new Explosion(board, p, "pink"));
            board.sounds.play(board.sounds.POP);
        }
    }
}

