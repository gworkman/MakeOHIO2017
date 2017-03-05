package jacob.ryan.gus.com.darwin.homeautomation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private Button lightsButton, tvButton, musicButton, raceHorseMode, refresh;
    private TextView temperatureData, humidityData;
    private ImageView lightsImage, tvImage, musicImage, volUp, volDown, chanUp, chanDown;
    private View lightOptions, tvOptions;

    private ImageView pickerRed, pickerOrange, pickerYellow, pickerGreen, pickerDarkGreen, pickerBlue, pickerDarkBlue, pickerPurple;
    private RelativeLayout pickerWhite;
    private Button changeInput, mute, playPause;
    private ImageView bright, mediumBright, mediumDim, dim;
    private LinearLayout brightnessPickerBackground;

    private RequestQueue queue;

    private boolean lightsOn = false, tvOn = false, musicOn = false;
    private String temperature = "70", humidity;


    private int num = 0;
    private Handler handler = new Handler();
    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            if ((num % 2) == 1) {
                get_humidity();
                handler.postDelayed(updater, 59000);
                num++;
            } else {
                get_temperature();
                handler.postDelayed(updater, 1000);
                num++;
            }
        }
    };

    private int tea = 0;
    private Handler teaHandler = new Handler();
    private Runnable teaRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: runningingingig");
            if (tea == 0) {
                send_request("maketea", 0);
                Log.d(TAG, "run: making ze teaaaaa");
                tea++;
                handler.postDelayed(teaRunnable, 120000);
            } else {
                send_request("stoptea", 0);
                Log.d(TAG, "run: tea is readyyyyyy");
                musicImage.setImageResource(R.drawable.ic_tea_grey);
                musicOn = false;
                tea = 0;
            }
        }
    };


    public static String URL = "http://homeautomation.ngrok.io/";

    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initiate_views();
        setup_listeners();

        queue = Volley.newRequestQueue(this);

        handler.post(updater);

        if (getIntent().getStringExtra("tea") != null) {
            if (getIntent().getStringExtra("tea").equals("maketea")) {
                send_request("maketea", 0);
            } else if (getIntent().getStringExtra("tea").equals("stoptea")) {
                send_request("stoptea", 0);
            }
        }

    }

    public void initiate_views() {
        lightsButton = (Button) findViewById(R.id.button_lights);
        tvButton = (Button) findViewById(R.id.button_tv);
        musicButton = (Button) findViewById(R.id.button_music);
        temperatureData = (TextView) findViewById(R.id.data_temp);
        humidityData = (TextView) findViewById(R.id.data_humidity);
        lightsImage = (ImageView) findViewById(R.id.icon_lights);
        tvImage = (ImageView) findViewById(R.id.icon_tv);
        musicImage = (ImageView) findViewById(R.id.icon_music);
        refresh = (Button) findViewById(R.id.refresh_button);


        // these are the color picker views
        pickerRed = (ImageView) findViewById(R.id.color_picker_red);
        pickerOrange = (ImageView) findViewById(R.id.color_picker_orange);
        pickerYellow = (ImageView) findViewById(R.id.color_picker_yellow);
        pickerGreen = (ImageView) findViewById(R.id.color_picker_green);
        pickerDarkGreen = (ImageView) findViewById(R.id.color_picker_dark_green);
        pickerBlue = (ImageView) findViewById(R.id.color_picker_blue);
        pickerDarkBlue = (ImageView) findViewById(R.id.color_picker_dark_blue);
        pickerPurple = (ImageView) findViewById(R.id.color_picker_purple);
        pickerWhite = (RelativeLayout) findViewById(R.id.color_picker_white);
        lightOptions = findViewById(R.id.main_color_options);
        bright = (ImageView) findViewById(R.id.picker_bright);
        mediumBright = (ImageView) findViewById(R.id.picker_medium_bright);
        mediumDim = (ImageView) findViewById(R.id.picker_medium_dim);
        dim = (ImageView) findViewById(R.id.picker_dim);
        brightnessPickerBackground = (LinearLayout) findViewById(R.id.brightness_background);
        raceHorseMode = (Button) findViewById(R.id.button_horse_race);

        // tv options views
        volUp = (ImageView) findViewById(R.id.button_volume_up);
        volDown = (ImageView) findViewById(R.id.button_volume_down);
        chanUp = (ImageView) findViewById(R.id.button_chan_up);
        chanDown = (ImageView) findViewById(R.id.button_chan_down);
        changeInput = (Button) findViewById(R.id.change_input_button);
        mute = (Button) findViewById(R.id.mute_button);
        playPause = (Button) findViewById(R.id.play_pause_button);
        tvOptions = findViewById(R.id.main_tv_options);

    }

    public void setup_listeners() {
        lightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvOptions.getVisibility() == View.VISIBLE) {
                    tvOptions.setVisibility(View.GONE);
                }
                if (lightOptions.getVisibility() == View.GONE) {
                    lightOptions.setVisibility(View.VISIBLE);
                } else {
                    lightOptions.setVisibility(View.GONE);
                    send_request("lightsoff", 0);
                    lightsImage.setImageResource(R.drawable.ic_power_grey);
                    lightsOn = false;
                }
            }
        });

        lightsButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!lightsOn) {
                    lightsImage.setImageResource(R.drawable.ic_power_white);
                    send_request("lightson", 0);
                    lightsOn = true;
                } else {
                    lightsImage.setImageResource(R.drawable.ic_power_grey);
                    send_request("lightsoff", 0);
                    lightsOn = false;
                }
                return true;
            }
        });

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightOptions.getVisibility() == View.VISIBLE) {
                    lightOptions.setVisibility(View.GONE);
                }

                if (tvOptions.getVisibility() == View.GONE) {
                    tvOptions.setVisibility(View.VISIBLE);
                } else {
                    tvOptions.setVisibility(View.GONE);
                    send_request("tvoff", 0);
                }
            }
        });

        tvButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!tvOn) {
                    tvImage.setImageResource(R.drawable.ic_power_white);
                    send_request("tvon", 0);
                    tvOn = true;
                } else {
                    tvImage.setImageResource(R.drawable.ic_power_grey);
                    send_request("tvoff", 0);
                    tvOn = false;
                }
                return true;
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!musicOn) {
                    musicImage.setImageResource(R.drawable.ic_tea_white);
                    musicOn = true;
                    teaHandler.post(teaRunnable);
                }
            }
        });

        pickerRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerRed.setImageResource(R.drawable.color_red);
                set_brightness_backgrounds(R.color.picker_red);
                send_request("red", 0);
            }
        });

        pickerOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerOrange.setImageResource(R.drawable.color_orange);

                set_brightness_backgrounds(R.color.picker_orange);
                send_request("orange", 0);
            }
        });

        pickerYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerYellow.setImageResource(R.drawable.color_yellow);
                set_brightness_backgrounds(R.color.picker_yellow);
                send_request("yellow", 0);
            }
        });

        pickerGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerGreen.setImageResource(R.drawable.color_green);
                set_brightness_backgrounds(R.color.picker_green);
                send_request("green", 0);
            }
        });

        pickerDarkGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerDarkGreen.setImageResource(R.drawable.color_dark_green);
                set_brightness_backgrounds(R.color.picker_dark_green);
                send_request("darkgreen", 0);
            }
        });

        pickerBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerBlue.setImageResource(R.drawable.color_blue);
                set_brightness_backgrounds(R.color.picker_blue);
                send_request("blue", 0);
            }
        });

        pickerDarkBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerDarkBlue.setImageResource(R.drawable.color_dark_blue);
                set_brightness_backgrounds(R.color.picker_dark_blue);
                send_request("darkblue", 0);
            }
        });

        pickerPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerPurple.setImageResource(R.drawable.color_purple);
                set_brightness_backgrounds(R.color.picker_purple);
                send_request("purple", 0);
            }
        });

        pickerWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerWhite.setBackgroundResource(R.drawable.color_white);
                set_brightness_backgrounds(R.color.white);
                send_request("white", 0);
            }
        });

        volUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_request("volup", 0);
            }
        });

        volDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_request("voldown", 0);
            }
        });

        chanUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_request("chanup", 0);
            }
        });

        chanDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_request("chandown", 0);
            }
        });

        bright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_brightness();
                bright.setImageResource(R.drawable.bright_outline);
                send_request("bright", 0);
            }
        });

        mediumBright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_brightness();
                mediumBright.setImageResource(R.drawable.medium_bright_outline);
                send_request("bright", 0);
                send_request("brightdown", 0);
            }
        });

        mediumDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_brightness();
                mediumDim.setImageResource(R.drawable.medium_dim_outline);
                send_request("dim", 0);
                send_request("brightup", 0);
            }
        });

        dim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_brightness();
                dim.setImageResource(R.drawable.dim_outline);
                send_request("dim", 0);
            }
        });

        raceHorseMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_brightness();
                reset_color_picker();
                send_request("horserace", 0);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_temperature();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                get_humidity();
            }
        });
    }

    public void reset_color_picker() {
        pickerRed.setImageResource(R.color.picker_red);
        pickerOrange.setImageResource(R.color.picker_orange);
        pickerYellow.setImageResource(R.color.picker_yellow);
        pickerGreen.setImageResource(R.color.picker_green);
        pickerDarkGreen.setImageResource(R.color.picker_dark_green);
        pickerBlue.setImageResource(R.color.picker_blue);
        pickerDarkBlue.setImageResource(R.color.picker_dark_blue);
        pickerPurple.setImageResource(R.color.picker_purple);
        pickerWhite.setBackgroundResource(R.color.white);
    }

    public void send_request(String sendCommand, final int responseType) {
        queue.add(new StringRequest(Request.Method.GET, URL + sendCommand, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: response is " + response);
                switch (responseType) {
                    case 1:
                        temperature = response;
                        break;
                    case 2:
                        humidity = response;
                        if (humidity != null) humidityData.setText(humidity.substring(0, 2));
                        if (temperature != null) temperatureData.setText(temperature.substring(0, 2));
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    public void clear_brightness() {
        dim.setImageResource(R.drawable.dim);
        mediumDim.setImageResource(R.drawable.medium_dim);
        mediumBright.setImageResource(R.drawable.medium_bright);
        bright.setImageResource(R.drawable.bright);
    }

    public void set_brightness_backgrounds(int colorResource) {
        brightnessPickerBackground.setBackgroundResource(colorResource);
    }

    public void get_temperature() {
        send_request("temperature", 1);
    }

    public void get_humidity() {
        send_request("humidity", 2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updater);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updater);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(updater);
    }
}
