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
import jonasz.pamula.therealsnake.drawing.Drawing;

public abstract class Actor implements Comparable{
    public Board mBoard;
    long id;
    static long last_id = 0;

    public Actor(Board board){
        mBoard = board;
        id = last_id++;
    }
    public abstract void update();
    public abstract void draw(Drawing d);
    
    public void remove(){
        mBoard.removeActor(this);
    }

    public int compareTo(Object o){
        if(o instanceof Actor){
            Actor actor = (Actor)o;
            if(id < actor.id) return -1;
            if(id > actor.id) return 1;
            return 0;
        } else {
            return 0;
        }
    }
}
