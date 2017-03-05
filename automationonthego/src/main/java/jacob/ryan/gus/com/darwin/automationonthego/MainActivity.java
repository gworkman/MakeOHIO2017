package jacob.ryan.gus.com.darwin.automationonthego;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends WearableActivity {

    private BoxInsetLayout mContainerView;
    private ImageView teaImage, teaColor;
    private String TAG = "ANDROID WEAR APP";
    private int teaCount = 0;
    private boolean makeTea = false;

    public static String URL = "http://homeautomation.ngrok.io/";

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (makeTea) {
                place_order();

            }
        }
    };

    private RequestQueue queue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        Log.d(TAG, "onCreate: oncreate is started");


        queue = Volley.newRequestQueue(this);

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        teaImage = (ImageView) findViewById(R.id.tea_image);
        teaColor = (ImageView) findViewById(R.id.tea_white);

        teaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: it is running");
                if (teaCount == 0) {
                    makeTea = true;
                } else {
                    makeTea = false;
                }

                place_order();
            }
        });
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
    }

    private void place_order() {

        String message;
        if (teaCount == 0) {
            message = "maketea";
            handler.postDelayed(runnable, 120000);

            CountDownTimer count = new CountDownTimer(120000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    Log.d(TAG, "onTick: time until complete " + millisUntilFinished / 1000);

                    if (millisUntilFinished / 1000 < 20) {
                        teaColor.setBackgroundResource(R.drawable.ic_tea_full);
                    } else if (millisUntilFinished / 1000 < 40) {
                        teaColor.setBackgroundResource(R.drawable.ic_tea_80);
                    } else if (millisUntilFinished / 1000 < 60) {
                        teaColor.setBackgroundResource(R.drawable.ic_tea_60);
                    } else if (millisUntilFinished / 1000 < 80) {
                        teaColor.setBackgroundResource(R.drawable.ic_tea_40);
                    } else if (millisUntilFinished / 1000 < 100) {
                        teaColor.setBackgroundResource(R.drawable.ic_tea_20);
                    } else {
                        teaColor.setBackgroundResource(R.drawable.ic_tea_0);
                    }
                }

                @Override
                public void onFinish() {
                    teaColor.setBackgroundResource(R.drawable.ic_tea_full);
                }
            };
            count.start();

            teaCount++;
        } else {
            message = "stoptea";
            teaColor.setImageResource(R.drawable.ic_tea_full);
            teaCount = 0;
        }

        send_request(message);

    }

    public void send_request(String sendCommand) {
        queue.add(new StringRequest(Request.Method.GET, URL + sendCommand, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: response is " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }
}
