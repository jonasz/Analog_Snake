package jonasz.pamula.therealsnake;
import android.util.Log;
import java.util.Random;

public class Utils {
    public static final double EPS = 1e-7;
    private static Random mRandom = new Random(getTime());

    public static void log(String s){
        Log.i("snake", s);
    }

    public static long getTime(){
        return System.currentTimeMillis();
    }

    public static double randomDouble(double a, double b){
        double span = b-a;
        return a + span*mRandom.nextDouble();
    }
}
