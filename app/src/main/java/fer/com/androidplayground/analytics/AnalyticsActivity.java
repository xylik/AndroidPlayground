package fer.com.androidplayground.analytics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fer.com.androidplayground.R;
import fer.com.androidplayground.logger.AndroidLogger;
import fer.com.androidplayground.logger.ILogger;

public class AnalyticsActivity extends AppCompatActivity {
    private ILogger logger;
    private IAnalytics analytics;
    private String userId = UUID.randomUUID().toString();
    private int numOfClicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        ButterKnife.bind(this);
        logger = AndroidLogger.instance();
        analytics = HybridAnalytics.instance(this, logger, userId);
    }

    @OnClick(R.id.sendEventBTN)
    void onSendEventClick() {
        HashMap<String, Object> properties = EventBuilder.instance()
                .addProperty("screen_name", "Analytics")
                .addProperty("num_of_clicks", ++numOfClicks)
                .build();
        analytics.logEvent("button_click", properties);
    }
}