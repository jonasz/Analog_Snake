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

public abstract class SnakeCommand implements Comparable{
    static long lastSerialNo = 0;

    long performAt;
    long serialNo;
    Snake snake;

    SnakeCommand (Snake snake_){
        performAt = Utils.getTime();
        snake = snake_;
        serialNo = ++lastSerialNo;
    }
    abstract void perform();

    public int compareTo(Object o){
        if(o instanceof SnakeCommand){
            SnakeCommand cmd = (SnakeCommand)o;
            if(performAt < cmd.performAt) return -1;
            if(performAt > cmd.performAt) return 1;
            return (new Long(serialNo)).compareTo(new Long(cmd.serialNo));
        } else {
            return 0;
        }
    }
    public SnakeCommand setTime(long time){
        performAt = time;
        return this;
    }

    public SnakeCommand delay(long time){
        performAt += time;
        return this;
    }
}
