package jonasz.pamula.therealsnake;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends Activity {
    private SnakeView mSnakeView;
    private Button mButtonLeft, mButtonRight;

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

            if(action!=-1) mSnakeView.getBoard().handleKey(dir,action);

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        mSnakeView = (SnakeView) findViewById(R.id.snake_view);

        mButtonLeft = (Button) findViewById(R.id.button_left);
        mButtonRight = (Button) findViewById(R.id.button_right);

        mButtonLeft.setOnTouchListener( mTouchListener );
        mButtonRight.setOnTouchListener( mTouchListener );

        Log.i("snake", "onCreate");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i("snake", "onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("snake", "onRestart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("snake", "onResume");
        mSnakeView.unpause();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("snake", "onPause");
        mSnakeView.pause();
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i("snake", "onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("snake", "onSaveInstanceState");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("snake", "onRestoreInstanceState");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
