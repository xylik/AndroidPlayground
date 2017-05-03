package fer.com.androidplayground;

/**
 * Created by Igor on 03/05/17.
 */

public class Constants {
    public static final String GOOGLE_PROJECT_ID = App.getProjectConfig().getProperty("GOOGLE_PROJECT_ID");
    public static final String GOOGLE_PROJECT_NUMBER = App.getProjectConfig().getProperty("GOOGLE_PROJECT_NUMBER");
    public static final String API_KEY = App.getProjectConfig().getProperty("API_KEY");
    public static final String DEBUG_SHA1 = App.getProjectConfig().getProperty("DEBUG_SHA1");

    private Constants() {
        //empty
    }
}
