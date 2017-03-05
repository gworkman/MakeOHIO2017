package jacob.ryan.gus.com.darwin.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Gus on 3/5/2017.
 */

public class OrderTeaService extends WearableListenerService {

    private GoogleApiClient googleApiClient;


    @Override
    public void onCreate() {
        super.onCreate();

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        }).addApi(Wearable.API).build();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        Intent intent = new Intent(this, MainActivity.class);
        if (messageEvent.getPath().equals("/maketea")) {
            intent.putExtra("tea", "maketea");
        } else if (messageEvent.getPath().equals("/stoptea")) {
            intent.putExtra("tea", "stoptea");
        }

        startActivity(intent);
    }
}
