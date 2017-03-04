package jacob.ryan.gus.com.darwin.homeautomation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button lightsButton, tvButton, musicButton;
    TextView temperatureData, humidityData, powerData, entrancesData;
    ImageView lightsImage, tvImage, musicImage;

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
    }

    public void setup_listeners() {
        lightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lightsOn) {
                    lightsImage.setImageResource(R.drawable.ic_power_white);
                    lightsOn = true;
                } else {
                    lightsImage.setImageResource(R.drawable.ic_power_grey);
                    lightsOn = false;
                }
            }
        });

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvOn) {
                    tvImage.setImageResource(R.drawable.ic_power_white);
                    tvOn = true;
                } else {
                    tvImage.setImageResource(R.drawable.ic_power_grey);
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

    }
}
