package fer.com.androidplayground.instanceId;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

import fer.com.androidplayground.App;
import fer.com.androidplayground.Constants;
import fer.com.androidplayground.R;
import fer.com.androidplayground.util.Preconditions;

public class InstanceIdActivity extends AppCompatActivity {
    public static final String TAG = "InstanceId";

    public static final String AUTHORIZED_ENTITY = Constants.GOOGLE_PROJECT_NUMBER;
    public static final String SCOPE = "GCM";
    private String iid;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_id);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchToken();
                checkIsAppAuthenticated(iid, token);
            }
        }).start();
    }

    private void fetchToken() {
        iid = InstanceID.getInstance(this).getId();
        try {
            token = InstanceID.getInstance(this).getToken(AUTHORIZED_ENTITY, "GCM");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIsAppAuthenticated(String iid, String token) {
        InstanceIdClient client = InstanceIdClient.instance();
        client.authenticateApplication(iid, token, new InstanceIdClient.Callback() {
            @Override
            public void onSuccess(InstanceIdClient.AuthenticationResponse response) {
                Preconditions.checkState(response.platform.equals("ANDROID"));
                Preconditions.checkState(response.attestStatus.equals("NOT_ROOTED"));
                Preconditions.checkState(response.applicationVersion.equals("1"));
                Preconditions.checkState(response.application.equals("fer.com.androidplayground"));
                Preconditions.checkState(response.authorizedEntity.equals(Constants.GOOGLE_PROJECT_NUMBER));
                String debug_sha1 = Constants.DEBUG_SHA1.replace(":", "").toLowerCase();
                Preconditions.checkState(response.appSigner.equals(debug_sha1));

                Log.d(TAG, "Successfull authentication!");
            }

            @Override
            public void onFail(Exception e) {
                Log.d(TAG, "Authentication error!");
            }
        });
    }

    public static class MyInstanceIDService extends InstanceIDListenerService {
        public void onTokenRefresh() {
            refreshAllTokens();
        }

        private void refreshAllTokens() {
//            // assuming you have defined TokenList as
//            // some generalized store for your tokens
//            ArrayList<TokenList> tokenList = TokensList.get();
//            InstanceID iid = InstanceID.getInstance(this);
//            for(tokenItem : tokenList) {
//                tokenItem.token =
//                        iid.getToken(tokenItem.authorizedEntity,tokenItem.scope,tokenItem.options);
//                // send this tokenItem.token to your server
//            }
        }
    };
}
