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

public class CrawlCommand extends SnakeCommand {
    CrawlCommand(Snake snake_){
        super(snake_);
    }
    void perform(){
        snake.adjustSpeed();
        if(snake.mMoving){
            performAt += 1000./snake.mSpeed;
            snake.crawlOneUnit();
        } else {
            performAt += 50.;
        }
        snake.addCommand(this);
    }
}

