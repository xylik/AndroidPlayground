package fer.com.androidplayground.logger;

public interface ILogger {
    void d(String tag, String message);
    void d(String tag, Throwable t);
    void e(String tag, String message);
    void e(String tag, Throwable t);
}