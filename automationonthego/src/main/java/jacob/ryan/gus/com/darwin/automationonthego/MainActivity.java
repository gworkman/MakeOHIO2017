package jacob.ryan.gus.com.darwin.automationonthego;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks {

    private BoxInsetLayout mContainerView;
    private ImageView teaImage;
    private String TAG = "ANDROID WEAR APP";

    private Node phoneNode;
    private MessageApi.MessageListener messageListener;

    private int teaCount = 0;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            place_order();
        }
    };

    private GoogleApiClient googleApiClient;
    private ResultCallback<Status> resultCallback = new ResultCallback<Status>() {
        @Override
        public void onResult(@NonNull Status status) {
            // do things when the api is hooked up
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Log.d(TAG, "doInBackground: it is working on the connectioning");
                    sendStartMessage();
                    return null;
                }
            }.execute();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        Log.d(TAG, "onCreate: oncreate is started");


        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        teaImage = (ImageView) findViewById(R.id.tea_image);

        teaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order();
            }
        });


        googleApiClient = new GoogleApiClient.Builder(MainActivity.this).addApi(Wearable.API).build();
        googleApiClient.connect();
        Log.d(TAG, "onCreate: attempted to connect");
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundResource(R.color.orange);

        } else {
            mContainerView.setBackground(null);

        }
    }

    private void place_order() {

        String message;
        if (teaCount == 0) {
            message = "/maketea";
            teaCount++;
        } else {
            message = "/stoptea";
            teaCount = 0;
        }

        PendingResult<MessageApi.SendMessageResult> result = Wearable.MessageApi.sendMessage(googleApiClient, phoneNode.getId(), message, null);

        result.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                //do nothing
                handler.postDelayed(runnable, 120000);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: connected to google services api");
         messageListener = new MessageApi.MessageListener() {
            @Override
            public void onMessageReceived(MessageEvent messageEvent) {
                // do things when a message is sent from phone to watch
            }
        };
        Wearable.MessageApi.addListener(googleApiClient, messageListener).setResultCallback(resultCallback);
    }



    @Override
    public void onConnectionSuspended(int i) {
        // empty
    }

    private void sendStartMessage() {

        NodeApi.GetConnectedNodesResult rawNodes =
                Wearable.NodeApi.getConnectedNodes(googleApiClient).await();

        Log.d(TAG, "sendStartMessage: scanning nodes");

        for (final Node node : rawNodes.getNodes()) {
            Log.v(TAG, "Node: " + node.getId());
            PendingResult<MessageApi.SendMessageResult> result = Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), "/start", null);

            result.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                @Override
                public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                    Log.v(TAG, "callback is done.");
                    phoneNode = node;
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Wearable.MessageApi.removeListener(googleApiClient, messageListener);
    }
}
