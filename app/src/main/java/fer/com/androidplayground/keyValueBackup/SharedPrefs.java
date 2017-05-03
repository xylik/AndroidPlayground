package fer.com.androidplayground.keyValueBackup;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Igor on 24/04/17.
 */

class SharedPrefs {
    public static final String FILE_NAME = "keyValueBackup_persistence";
    public static final String USER_ID_KEY = "fer.com.keyValueBackup.USER_ID_KEY";
    public static final String USER_ID_MODIFIED_KEY = "fer.com.keyValueBackup.USER_ID_MODIFIED_KEY";

    private static SharedPrefs instance;
    private SharedPreferences prefs;

    private SharedPrefs(Context ctx) {
        prefs = ctx.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    static SharedPrefs instance(Context ctx) {
        if(instance == null) instance = new SharedPrefs(ctx);
        return instance;
    }

    public String getUserId() {
        return prefs.getString(USER_ID_KEY, "");
    }

    public void setUserId(String userId) {
        prefs.edit().putString(USER_ID_KEY, userId).commit();
        setUserIdModifiedDate(System.currentTimeMillis());
    }

    public long getUserIdModified() {
        return prefs.getLong(USER_ID_MODIFIED_KEY, -1);
    }

    public void setUserIdModifiedDate(long dateModified) {
        prefs.edit().putLong(USER_ID_MODIFIED_KEY, dateModified).commit();
    }
}
