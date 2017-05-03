package fer.com.androidplayground.logger;

import android.util.Log;

/**
 * Created by mbarisa on 2/13/17.
 */

public class AndroidLogger implements ILogger {
    public static ILogger instance() {
        return new AndroidLogger();
    }

    private AndroidLogger() {
        //empty
    }

    @Override
    public void d(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    public void d(String tag, Throwable t) {
        Log.d(tag, Log.getStackTraceString(t));
    }

    @Override
    public void e(String tag, String message) {
        Log.e(tag, message);
    }

    @Override
    public void e(String tag, Throwable t) {
        Log.e(tag, Log.getStackTraceString(t));
    }
}
