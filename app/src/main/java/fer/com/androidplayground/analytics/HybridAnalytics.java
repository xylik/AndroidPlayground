package fer.com.androidplayground.analytics;

import android.content.Context;
import android.os.Bundle;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.Set;

import fer.com.androidplayground.logger.ILogger;

public class HybridAnalytics implements IAnalytics {
    public static final String TAG = HybridAnalytics.class.getSimpleName();
    private ILogger logger;
    private FirebaseAnalytics firebaseAnalytics;
    private AppEventsLogger facebookAnalytics;

    public static IAnalytics instance(Context ctx, ILogger logger, String userId) {
        return new HybridAnalytics(ctx, logger, userId);
    }

    private HybridAnalytics(Context ctx, ILogger logger, String userId) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(ctx);
        firebaseAnalytics.setUserId(userId);
        facebookAnalytics = AppEventsLogger.newLogger(ctx);
        facebookAnalytics.setUserID(userId);
        this.logger = logger;
    }

    @Override
    public void logEvent(String eventName, HashMap<String, Object> eventProperties) {
        logger.d(TAG, "Event name: " + eventName + " Event properties:\n" + concatEventProperties(eventProperties));

//        if(BuildConfig.DEBUG) return;
        Bundle properties = buildPropertiesBundle(eventProperties);
        firebaseAnalytics.logEvent(eventName, properties);
        facebookAnalytics.logEvent(eventName, properties);
    }

    private String concatEventProperties(HashMap<String, Object> properties) {
        StringBuilder sb = new StringBuilder();

        Set<String> keys = properties.keySet();
        for(String key: keys) sb.append("\t" + key + " : " + properties.get(key) + "\n");
        return sb.toString();
    }

    private Bundle buildPropertiesBundle(HashMap<String, Object> properties) {
        Bundle bundle = new Bundle();

        Set<String> keys = properties.keySet();
        for(String key: keys) {
            Object value = properties.get(key);

            if(value instanceof String) bundle.putString(key, (String)value);
            else if(value instanceof Integer) bundle.putInt(key, (Integer)value);
            else if(value instanceof Long) bundle.putLong(key, (Long)value);
            else if(value instanceof Float) bundle.putFloat(key, (Float)value);
            else if(value instanceof Double) bundle.putDouble(key, (Double)value);
            else throw new RuntimeException(ERR_UNSPORTED_TYPE);
        }
        return bundle;
    }
}