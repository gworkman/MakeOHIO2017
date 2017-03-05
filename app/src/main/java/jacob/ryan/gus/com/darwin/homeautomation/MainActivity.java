package jacob.ryan.gus.com.darwin.homeautomation;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button lightsButton, tvButton, musicButton;
    TextView temperatureData, humidityData, powerData, entrancesData;
    ImageView lightsImage, tvImage, musicImage;
    View lightOptions, tvOptions;

    ImageView pickerRed, pickerOrange, pickerYellow, pickerGreen, pickerDarkGreen, pickerBlue, pickerDarkBlue, pickerPurple;
    RelativeLayout pickerWhite;

    boolean lightsOn = false, tvOn = false, musicOn = false;

    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initiate_views();
        setup_listeners();

    }

    public void initiate_views() {
        lightsButton = (Button) findViewById(R.id.button_lights);
        tvButton = (Button) findViewById(R.id.button_tv);
        musicButton = (Button) findViewById(R.id.button_music);
        temperatureData = (TextView) findViewById(R.id.data_temp);
        humidityData = (TextView) findViewById(R.id.data_humidity);
        powerData = (TextView) findViewById(R.id.data_power);
        entrancesData = (TextView) findViewById(R.id.data_entrances);
        lightsImage = (ImageView) findViewById(R.id.icon_lights);
        tvImage = (ImageView) findViewById(R.id.icon_tv);
        musicImage = (ImageView) findViewById(R.id.icon_music);
        lightOptions = findViewById(R.id.main_color_options);
        tvOptions = findViewById(R.id.main_tv_options);

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
    }

    public void setup_listeners() {
        lightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lightsOn) {
                    if (tvOptions.getVisibility() == View.VISIBLE) {
                        tvOptions.setVisibility(View.GONE);
                    }
                    lightOptions.setVisibility(View.VISIBLE);
                    lightsOn = true;
                } else {
                    lightOptions.setVisibility(View.GONE);
                    lightsOn = false;
                }
            }
        });

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvOn) {
                    if (lightOptions.getVisibility() == View.VISIBLE) {
                        lightOptions.setVisibility(View.GONE);
                    }
                    tvOptions.setVisibility(View.VISIBLE);
                    tvOn = true;
                } else {
                    tvOptions.setVisibility(View.GONE);
                    tvOn = false;
                }
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!musicOn) {
                    musicImage.setImageResource(R.drawable.ic_pause_white);
                    musicOn = true;
                } else {
                    musicImage.setImageResource(R.drawable.ic_play_grey);
                    musicOn = false;
                }
            }
        });

        pickerRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerRed.setImageResource(R.drawable.color_red);
            }
        });

        pickerOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerOrange.setImageResource(R.drawable.color_orange);
            }
        });

        pickerYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerYellow.setImageResource(R.drawable.color_yellow);
            }
        });

        pickerGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerGreen.setImageResource(R.drawable.color_green);
            }
        });

        pickerDarkGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerDarkGreen.setImageResource(R.drawable.color_dark_green);
            }
        });

        pickerBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerBlue.setImageResource(R.drawable.color_blue);
            }
        });

        pickerDarkBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerDarkBlue.setImageResource(R.drawable.color_dark_blue);
            }
        });

        pickerPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerPurple.setImageResource(R.drawable.color_purple);
            }
        });

        pickerWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_color_picker();
                pickerWhite.setBackgroundResource(R.drawable.color_white);
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
}
