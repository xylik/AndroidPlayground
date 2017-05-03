package fer.com.androidplayground;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fer.com.androidplayground.analytics.AnalyticsActivity;
import fer.com.androidplayground.instanceId.InstanceIdActivity;
import fer.com.androidplayground.keyValueBackup.KeyValueBackupActivity;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.keyValueBTN)
    Button keyValueBTN;
    @Bind(R.id.instanceIdBTN)
    Button instanceIdBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.keyValueBTN)
    void onKeyValueClick() {
        startActivity(new Intent(this, KeyValueBackupActivity.class));
    }

    @OnClick(R.id.instanceIdBTN)
    void onInstanceIdClick() {
        startActivity(new Intent(this, InstanceIdActivity.class));
    }

    @OnClick(R.id.analyticsBTN)
    void onAnalyticsClick() {
        startActivity(new Intent(this, AnalyticsActivity.class));
    }
}
