package jonasz.pamula.therealsnake;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.Button;
import jonasz.pamula.therealsnake.Utils;
import jonasz.pamula.therealsnake.board.Board;

public class MainActivity extends Activity {
    private SnakeView mSnakeView;
    private Button mButtonLeft, mButtonRight;
    static private Context context;

    public static Context getContext(){
        return context;
    }

    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event){
            int dir = (v == mButtonLeft) ? Board.KEY_LEFT : Board.KEY_RIGHT;

            int action = -1;
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                action = Board.KEY_DOWN;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                action = Board.KEY_UP;
            }

            if(action!=-1) mSnakeView.handleUserInput(dir, action);

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        mSnakeView = (SnakeView) findViewById(R.id.snake_view);

        mButtonLeft = (Button) findViewById(R.id.button_left);
        mButtonRight = (Button) findViewById(R.id.button_right);

        mButtonLeft.setOnTouchListener( mTouchListener );
        mButtonRight.setOnTouchListener( mTouchListener );

        Utils.log("onCreate");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Utils.log("onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Utils.log("onRestart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Utils.log("onResume");
        mSnakeView.unpause();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Utils.log("onPause");
        mSnakeView.pause();
    }
    @Override
    protected void onStop(){
        super.onStop();
        Utils.log("onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Utils.log("onSaveInstanceState");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Utils.log("onRestoreInstanceState");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
