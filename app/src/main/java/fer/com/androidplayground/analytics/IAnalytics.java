package fer.com.androidplayground.analytics;

import java.util.HashMap;

public interface IAnalytics {
    String ERR_UNSPORTED_TYPE = "Type not supported!";

    void logEvent(String eventName, HashMap<String, Object> eventProperties);
}