package fer.com.androidplayground.instanceId;

import android.provider.SyncStateContract;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import fer.com.androidplayground.Constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Igor on 28/04/17.
 */

public class InstanceIdClient {
    private final String INSTANCE_ID_URL = "https://iid.googleapis.com/iid/info/IID_TOKEN?details=true";
    private final String IID_TOKEN = "IID_TOKEN";
    private final String AUTH_HEADER = "Authorization";

    private String instanceId;
    private String token;
    private Gson gson = new Gson();

    interface Callback{
        void onSuccess(AuthenticationResponse response);
        void onFail(Exception e);
    }

    static class AuthenticationResponse {
        String applicationVersion;
        String application;
        String authorizedEntity;
        String platform;
        String attestStatus;
        String appSigner;
        String connectionType;
        String connectDate;
        RelationResponse rel;
    }

    static class RelationResponse {
        HashMap<String, DateResponse> topics;
    }

    static class DateResponse {
        String addDate;
    }

    private InstanceIdClient() {
        //empty
    }

    static InstanceIdClient instance() {
        return new InstanceIdClient();
    }

    public void authenticateApplication(String appInstanceId, String appToken, Callback resultCallback) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Request request = new Request.Builder()
                .url(INSTANCE_ID_URL.replace(IID_TOKEN, appToken))
                .addHeader(AUTH_HEADER, "key=" + Constants.API_KEY)
                .build();

        Response response = null;
        AuthenticationResponse authResponse = null;

        try {
            response = client.newCall(request).execute();
            String json = response.body().string();
            Log.d(InstanceIdActivity.TAG, json);
            authResponse = gson.fromJson(json, new TypeToken<AuthenticationResponse>(){}.getType());
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
            resultCallback.onFail(e);
            return;
        }
        resultCallback.onSuccess(authResponse);
    }
}
