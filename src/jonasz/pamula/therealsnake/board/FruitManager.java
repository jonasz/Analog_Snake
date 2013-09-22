package jonasz.pamula.therealsnake.board;

import jonasz.pamula.therealsnake.Utils;
import jonasz.pamula.therealsnake.actors.snake.Snake;
import jonasz.pamula.therealsnake.actors.Banana;
import jonasz.pamula.therealsnake.actors.Peach;
import jonasz.pamula.therealsnake.actors.Apple;
import jonasz.pamula.therealsnake.actors.Eatable;
import jonasz.pamula.therealsnake.actors.snake.TurnCommand;
import jonasz.pamula.therealsnake.board.Point;
import jonasz.pamula.therealsnake.drawing.Drawing;
import jonasz.pamula.therealsnake.actors.Actor;
import jonasz.pamula.therealsnake.actors.Explosion;
import jonasz.pamula.therealsnake.actors.FallingStar;

public class FruitManager {
    Board mBoard;

    static final int EAT_HWMNY_APPLES_TO_GET_PEACH = 10;
    static final int BANANA_APPEARS_EVERY_MS = 20 * 1000;
    static final int STAR_APPEARS_EVERY_MS = 500;

    int mApplesToPeach = 0;
    long mLastBanana = 0;
    long mLastStar = 0;

    public FruitManager(Board board){
        mBoard = board;
        //mLastBanana = Utils.getTime();
    }

    Point getRandomPosOnBoard(){
        double x = Utils.randomDouble(0, mBoard.BOARD_WIDTH);
        double y = Utils.randomDouble(0, mBoard.BOARD_HEIGHT);
        return new Point(x,y);
    }

    public boolean addEatable(Eatable e){
        for(int i=0; i<100; i++){
            e.setPosition(getRandomPosOnBoard());
            if(!mBoard.mSnake.collides(e)){
                mBoard.addActor(e);
                return true;
            }
        }
        return false;
    }

    boolean shouldAddApple(int cnt){
        return cnt==0;
    }

    boolean shouldAddBanana(int cnt){
        long now = Utils.getTime();
        if(cnt>0) return false;
        if(mBoard.mSnake.bananaEaten()) return false;
        if(mLastBanana + BANANA_APPEARS_EVERY_MS > now) return false;
        return true;
    }

    boolean shouldAddPeach(int cnt){
        if(cnt>0) return false;
        if(mApplesToPeach > 0) return false;
        return true;
    }

    void update(){
        long now = Utils.getTime();
        int apples = 0, bananas = 0,peaches = 0, stars = 0;
        for(Actor a: mBoard.getActors()){
            if(a instanceof Apple) apples++;
            if(a instanceof Banana) bananas++;
            if(a instanceof Peach) peaches++;
            if(a instanceof FallingStar) stars++;
        }

        if(stars < 5 && mLastStar + STAR_APPEARS_EVERY_MS < now){
            Point pos = getRandomPosOnBoard();
            mLastStar = now;
            mBoard.addActor(new FallingStar(mBoard, pos, "white", 7.));
        }

        if(shouldAddApple(apples)){
            if(mApplesToPeach>0) mApplesToPeach--;
            addEatable(new Apple(mBoard, new Point(0,0)));
        }

        if(shouldAddBanana(bananas)){
            addEatable(new Banana(mBoard, new Point(0,0)));
        }
        if(bananas>0) {
            mLastBanana = Utils.getTime();
        }

        if(shouldAddPeach(peaches)){
            addEatable(new Peach(mBoard, new Point(0,0)));
            mApplesToPeach = EAT_HWMNY_APPLES_TO_GET_PEACH;
        }
    }
}
