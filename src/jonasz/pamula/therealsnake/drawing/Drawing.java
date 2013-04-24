package jonasz.pamula.therealsnake.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import jonasz.pamula.therealsnake.Utils;
import jonasz.pamula.therealsnake.actors.snake.Snake;
import jonasz.pamula.therealsnake.actors.snake.TurnCommand;
import jonasz.pamula.therealsnake.board.Point;
import jonasz.pamula.therealsnake.board.Board;
import jonasz.pamula.therealsnake.actors.Actor;

public class Drawing {
    static final double BOARD_WIDTH_PERC = 0.7; // * screen width
    static final double BOARD_HEIGHT_PERC = 0.95; // * screen width
    private double mBoardWidth = 0;
    private double mBoardHeight = 0;
    private int mSurfaceWidth = -1;
    private int mSurfaceHeight = -1;
    private double mScale = -1;
    private Board mBoard;
    private double boardW;
    private double boardH;
    private Point mBoardPos;
    Canvas mCanvas;

    static Map<String, Paint> COLORS = new TreeMap<String, Paint>();
    static {
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setAntiAlias(true);

        paint.setARGB(255, 0, 255, 0);
        COLORS.put("green", new Paint(paint));

        paint.setARGB(255, 255, 0, 0);
        COLORS.put("red", new Paint(paint));

        paint.setARGB(255, 150, 150, 255);
        COLORS.put("blue", new Paint(paint));
    }

    public Drawing(Board board){
        mBoard = board;
    }

    public void setSurfaceSize(int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;

        mBoardWidth = width * BOARD_WIDTH_PERC;
        mBoardHeight = height * BOARD_HEIGHT_PERC;

        if(mBoardWidth / mBoardHeight > mBoard.BOARD_WIDTH / mBoard.BOARD_HEIGHT){
            mBoardWidth = mBoard.BOARD_WIDTH / mBoard.BOARD_HEIGHT * mBoardHeight;
        } else {
            mBoardHeight = mBoard.BOARD_HEIGHT / mBoard.BOARD_WIDTH * mBoardWidth;
        }

        double widthLeft = width - mBoardWidth;
        double heightLeft = height - mBoardHeight;
        mBoardPos = new Point(widthLeft/2, heightLeft/2);

        double scaleX = mBoardWidth / mBoard.BOARD_WIDTH;
        double scaleY = mBoardHeight / mBoard.BOARD_HEIGHT;

        Utils.log("WIDTH: " + (new Double(mBoardWidth)));
        Utils.log("HEIGHT: " + (new Double(mBoardHeight)));

        mScale = Math.min(scaleX, scaleY);
        Utils.log("mScale: " + (new Double(mScale)).toString());
    }

    Point board2Screen(Point p){
        Point res = new Point(p.getX()*mScale, p.getY()*mScale);
        res.add(mBoardPos);
        return res;
    }
    
    double scaleLength(double x){
        return x*mScale;
    }

    public void putLine(Point p1, Point p2, String color){
        mCanvas.drawLine(
                (float)p1.getX(),
                (float)p1.getY(),
                (float)p2.getX(),
                (float)p2.getY(),
                COLORS.get(color));
    }

    public void putCircleOnBoard(Point pos, double r, String color){
        pos = board2Screen(pos);
        r = scaleLength(r);
        putCircle(pos, r, color);
    }

    public void putCircle(Point pos, double r, String color){
        mCanvas.drawCircle(
                (float)pos.getX(),
                (float)pos.getY(),
                (float)r,
                COLORS.get(color));
    }

    public void putText(String s, float x, float y, Paint p){
        mCanvas.drawText(s, x, y, p);
    }

    public void putText(String s, float x, float y, String color){
        putText(s, x, y, COLORS.get(color));
    }

    void drawBorder(){
        Point topLeft = new Point(mBoardPos);
        Point topRight = new Point(topLeft); topRight.add(mBoardWidth, 0);
        Point bottomLeft = new Point(topLeft).add(0, mBoardHeight);
        Point bottomRight = new Point(bottomLeft).add(mBoardWidth, 0);

        putLine(bottomLeft, bottomRight, "blue");
        putLine(topLeft, topRight, "blue");
        putLine(topLeft, bottomLeft, "blue");
        putLine(topRight, bottomRight, "blue");
    }

    void putStats(){
        LinkedList<String>L = new LinkedList<String>();
        L.addLast("SCORE");
        L.addLast("" + (new Integer(mBoard.mScore)));
        L.addLast("SPEED");
        L.addLast("" + (new Integer(mBoard.mSnake.getSpeed())));

        int X = 10;
        int Y = 100;
        for(String s: L){
            putText(s, X, Y, "red");
            Y += 50;
        }

        /*
        // XXX - need to improve the following code
        // (text dimensions should not be hardcoded)

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setARGB(255, 240, 120, 50);

        double max_w = 0.8 * mBoardWidth * (1. - BOARD_WIDTH_PERC) / 2.;
        double max_h = 0.3 * max_w;
        Rect rect = new Rect();

        for(String s: L){
            int size = 1;
            while(true){
                p.setTextSize(size);
                p.getTextBounds(s, 0, s.length(), rect);
                int w = rect.width();
                int h = rect.height();
                if(w < max_w && h < max_h) size+=10;
                else {
                    Utils.log(s + ", size: " + (new Integer(size)));
                    putText(s, X, Y, p);
                    Y += 1.3*h;
                    break;
                }
            }
        }
        */
    }

    public void draw(Canvas canvas_){
        mCanvas = canvas_;

        drawBorder();

        for(Actor a: mBoard.getActors()){
            a.draw(this);
        }

        putStats();
    }
}
