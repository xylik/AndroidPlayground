package fer.com.androidplayground;

import android.app.Application;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Igor on 26/04/17.
 */

public class App extends Application {
    private static final String PROJECT_SETTINGS_PATH = "security/project.properties";
    private static Context globalContext;
    private static Properties projectProperties;

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
    }

    public static Context getGlobalContext() {
        return globalContext;
    }

    public static Properties getProjectConfig() {
        if(projectProperties != null) return projectProperties;

        try {
            InputStream is = globalContext.getAssets().open(PROJECT_SETTINGS_PATH);
            projectProperties = new Properties();
            projectProperties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectProperties;
    }
}
