package jonasz.pamula.therealsnake.board;

import jonasz.pamula.therealsnake.Utils;
import jonasz.pamula.therealsnake.actors.snake.Snake;
import jonasz.pamula.therealsnake.actors.Apple;
import jonasz.pamula.therealsnake.actors.Banana;
import jonasz.pamula.therealsnake.actors.Eatable;
import jonasz.pamula.therealsnake.actors.snake.TurnCommand;
import jonasz.pamula.therealsnake.board.Point;
import jonasz.pamula.therealsnake.drawing.Drawing;
import jonasz.pamula.therealsnake.actors.Actor;

public class FruitManager {
    Board mBoard;

    final static int BANANA_INTERVAL = 10 * 1000;
    long lastBanana;

    public FruitManager(Board board){
        mBoard = board;
        lastBanana = Utils.getTime();
    }

    public boolean addEatable(Eatable e){
        for(int i=0; i<100; i++){
            double x = Utils.randomDouble(0, mBoard.BOARD_WIDTH);
            double y = Utils.randomDouble(0, mBoard.BOARD_HEIGHT);
            e.setPosition(new Point(x,y));
            if(!mBoard.mSnake.collides(e)){
                mBoard.addActor(e);
                return true;
            }
        }
        return false;
    }

    public boolean addRandomApple(){
        Apple a = new Apple(mBoard, new Point(0,0));
        return addEatable(a);
    }

    public boolean addRandomBanana(){
        Banana b = new Banana(mBoard, new Point(0,0));
        return addEatable(b);
    }

    void update(){
        long now = Utils.getTime();
        int apples = 0, bananas = 0;
        for(Actor a: mBoard.getActors()){
            if(a instanceof Apple) apples++;
            if(a instanceof Banana) bananas++;
        }

        if(apples == 0) addRandomApple();
        if(bananas == 0){
            if(lastBanana + BANANA_INTERVAL < now && mBoard.mSnake.invincible==0) addRandomBanana();
        }
        if(bananas > 0) {
            lastBanana = now;
        }
    }
}
