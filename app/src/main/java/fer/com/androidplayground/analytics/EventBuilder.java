package fer.com.androidplayground.analytics;

import java.util.HashMap;

/**
 * Created by Igor on 20/04/17.
 */

public class EventBuilder {
    private HashMap<String, Object> properties = new HashMap<>();

    private EventBuilder() {
        //empty
    }

    public static EventBuilder instance() {
        return new EventBuilder();
    }

    public HashMap<String, Object> build() {
        return properties;
    }

    public EventBuilder addProperty(String key, Object value) {
        properties.put(key, value);
        return this;
    }
}
